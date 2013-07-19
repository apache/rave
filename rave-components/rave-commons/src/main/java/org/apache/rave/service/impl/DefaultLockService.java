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

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DefaultLockService implements LockService {
    private static Logger logger = LoggerFactory.getLogger(DefaultLockService.class);

    private Map<String, ReferenceTrackingLock> locks = new HashMap<String, ReferenceTrackingLock>();

    @Override
    public synchronized Lock borrowLock(String key) {
        logger.debug("BorrowLock called with key [{}]", key);
        ReferenceTrackingLock lock = locks.get(key);
        if (lock == null) {
            logger.debug("Existing lock not found under key [{}] - creating new lock", key);
            lock = new ReferenceTrackingLock(key);
            locks.put(key, lock);
        } else {
            logger.debug("Existing lock found under key [{}] - returning existing lock", key);
        }

        lock.incrementReferenceCount();

        logger.debug("Returning lock with key [{}] - and referenceCount [{}]", lock.getKey(), lock.getReferenceCount());
        return lock;
    }

    @Override
    public synchronized Lock borrowLock(String discriminator, String id) {
        if (StringUtils.isEmpty(discriminator)) {
            return borrowLock(id);
        }
        return borrowLock(new StringBuilder(discriminator).append("-").append(id).toString());
    }

    @Override
    public synchronized void returnLock(Lock lock) {
        ReferenceTrackingLock referenceTrackingLock = (ReferenceTrackingLock) lock;
        logger.debug("Lock with key [{}] - and referenceCount [{}] has been returned", referenceTrackingLock.getKey(),
                referenceTrackingLock.getReferenceCount());

        referenceTrackingLock.decrementReferenceCount();
        if (referenceTrackingLock.getReferenceCount() < 1) {
            logger.debug("Lock with key [{}] - and referenceCount [{}] is being removed from service",
                    referenceTrackingLock.getKey(), referenceTrackingLock.getReferenceCount());
            locks.remove(referenceTrackingLock.getKey());
        }
    }

    class ReferenceTrackingLock extends ReentrantLock {
        private String key;
        private volatile int referenceCount;

        private ReferenceTrackingLock(String key) {
            super();
            this.key = key;
            this.referenceCount = 0;
        }

        public String getKey() {
            return key;
        }

        public int getReferenceCount() {
            return referenceCount;
        }

        public void incrementReferenceCount() {
            referenceCount++;
        }

        public void decrementReferenceCount() {
            referenceCount--;
        }
    }
}
