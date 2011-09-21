/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.service.impl;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class DefaultLockServiceTest {
    private DefaultLockService service;

    private static final String KEY = "KEY";
    private static final String DISCRIMINATOR = "FOO";
    private static final long ID = 1L;

    @Before
    public void setup() {
        service = new DefaultLockService();
    }

    @Test
    public void borrowLockByKey() {
        Lock lock = service.borrowLock(KEY);
        assertThat(lock, is(notNullValue()));
    }

    @Test
    public void borrowLockByDiscriminatorAndId() {
        Lock lock = service.borrowLock(DISCRIMINATOR, String.valueOf(ID));
        assertThat(lock, is(notNullValue()));
    }

    @Test
    public void verifyComputedKeyContract() {
        String expectedKey = DISCRIMINATOR + "-" + ID;
        DefaultLockService.ReferenceTrackingLock lock =
                (DefaultLockService.ReferenceTrackingLock) service.borrowLock(DISCRIMINATOR, String.valueOf(ID));

        assertThat(lock.getKey(), is(expectedKey));

        Lock secondLock = service.borrowLock(expectedKey);
        assertThat(lock, is(sameInstance(secondLock)));
    }

    @Test
    public void borrowLockByEmptyDiscriminatorAndId() {
        Lock lock = service.borrowLock("", String.valueOf(ID));
        assertThat(lock, is(notNullValue()));
    }

    @Test
    public void borrowAndReturnTwiceGivesDifferentInstances() {
        Lock lock = service.borrowLock(KEY);
        service.returnLock(lock);
        Lock lock2 = service.borrowLock(KEY);
        service.returnLock(lock2);
        assertThat(lock, not(CoreMatchers.sameInstance(lock2)));
    }

    @Test
    public void borrowAndNoReturnTwiceGivesSameInstances() {
        Lock lock = service.borrowLock(KEY);
        Lock lock2 = service.borrowLock(KEY);
        assertThat(lock, is(CoreMatchers.sameInstance(lock2)));
    }

    @Test
    public void borrowAndOneReturnThreeTimesGivesSameInstances() {
        Lock lock = service.borrowLock(KEY);
        Lock lock2 = service.borrowLock(KEY);
        service.returnLock(lock);
        Lock lock3 = service.borrowLock(KEY);
        assertThat(lock, is(CoreMatchers.sameInstance(lock2)));
        assertThat(lock, is(CoreMatchers.sameInstance(lock3)));
    }

    @Test
    public void testThreadsWithSharedKeys() throws InterruptedException {
        long startTime = System.nanoTime();
        testThreads(true);
        long endTime = System.nanoTime();
        final long durationInMillis = (endTime - startTime) / 1000000L;

        //running with shared keys the test should take at least 1.25 seconds due to blocking
        assertThat(durationInMillis, is(greaterThanOrEqualTo(1250L)));
    }

    @Test
    public void testThreadsWithUniqueKey() throws InterruptedException {
        long startTime = System.nanoTime();
        testThreads(false);
        long endTime = System.nanoTime();
        final long durationInMillis = (endTime - startTime) / 1000000L;

        //running with unique keys the test should take no more than half a second
        assertThat(durationInMillis, is(lessThanOrEqualTo(500L)));
    }

    private void testThreads(final boolean useSharedLock) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Collection<Callable<String>> tasks = new ArrayList<Callable<String>>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String threadName = Thread.currentThread().getName();
                    String lockKey = useSharedLock ? "SHARED" : threadName;
                    Lock lock = service.borrowLock(lockKey);
                    try {
                        lock.lock();
                        Thread.sleep(125);
                        System.out.println(threadName);
                        return lockKey;
                    } finally {
                        lock.unlock();
                        service.returnLock(lock);
                    }
                }
            });
        }
        executorService.invokeAll(tasks);
    }
}