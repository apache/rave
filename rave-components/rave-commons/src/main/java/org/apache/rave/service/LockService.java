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

package org.apache.rave.service;

import java.util.concurrent.locks.Lock;

/**
 * Service used to manage shared Lock objects to be used for item level synchronization.
 */
public interface LockService {
    /**
     * Borrow a lock from the service using the specified key as a unique identifier for the shared lock.
     *
     * @param key The unique identifier for the shared lock.
     * @return The shared lock.
     */
    Lock borrowLock(String key);

    /**
     * Borrow a lock from the service using the specified discriminator and key to construct a unique identifier for the
     * shared lock.  Keys are constructed by concatenating the discriminator and id with a hyphen.  This implementation
     * detail is made public as an explicit part of the API so that callers who need to call both this method as well as
     * the borrowLock(String key) method know how the key is being constructed when calling this method.
     *
     * @param discriminator The discriminator to use in the key.
     * @param id            The id to use in the key.
     * @return The shared lock retrieved using the computed key.
     */
    Lock borrowLock(String discriminator, String id);

    /**
     * Return a borrowed lock to the service.  Locks should be unlocked and returned in a finally block to ensure they
     * are always properly returned to the service.
     *
     * @param lock The lock to return.
     */
    void returnLock(Lock lock);
}