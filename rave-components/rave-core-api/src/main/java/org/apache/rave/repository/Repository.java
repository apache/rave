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

package org.apache.rave.repository;

import java.util.List;

/**
 * Defines generic operations for a repository
 */
public interface Repository<T> {
    /**
     * Gets the type of the object served by the repository
     * @return a {@link Class} representing the type of object
     */
    Class<? extends T> getType();

    /**
     * Gets the specified object from the persistence context by its id
     *
     * @param id the id of the object of type T to retrieve
     * @return a valid instance if id exists; null otherwise
     */
    T get(String id);

    /**
     * Save the given item in the repository
     * @param item the item to save
     * @return the persisted item
     */
    T save(T item);

    /**
     * Deletes the given item from the persistence context
     *
     * @param item the item to delete
     */
    void delete(T item);

    /**
     * Retrieve all objects from the persistence context.
     *
     * @return a list of all objects
     */
    List<T> getAll();

    /**
     * Retrieve a limited list of objects as a SearchResult
     * @param offset the integer offset from 0 index
     * @param limit the size limit of the result set
     * @return
     */
    List<T> getLimitedList(int offset, int limit);

    /**
     * Retrieves the count of all records
     * @return
     */
    int getCountAll();
}
