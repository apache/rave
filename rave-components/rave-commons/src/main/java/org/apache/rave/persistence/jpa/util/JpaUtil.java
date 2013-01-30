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

package org.apache.rave.persistence.jpa.util;

import org.apache.rave.exception.NotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * JPA utilities
 */
public class JpaUtil {

    private static final Logger log = LoggerFactory.getLogger(JpaUtil.class);

    private static final int LARGE_PAGESIZE = 1000;

    private JpaUtil() {}

    /**
     * Gets a single, unique result from the list
     * @param list list of results
     * @param <T> the type of the result item
     * @return mull if the list is empty; the item if there is exactly 1 item in the list; exception otherwise
     * @throws IncorrectResultSizeDataAccessException if there are more than 1 items in the list
     */
    public static <T> T getSingleResult(List<T> list) {
        if (list == null) {
            return null;
        }
        
        switch(list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                throw new IncorrectResultSizeDataAccessException(1);
        }
    }

    /**
     * Persists or merges the entity into the given {@link EntityManager} depending on whether or not the id is null
     * @param id the ID of the entity being saved or updated
     * @param entityManager the entity manager to persist or merge to
     * @param entity the entity to save or update
     * @param <T> the type of the Entity
     * @param <I> the type of the Entity's Id
     * @return the entity in the persistence context
     */
    public static <T, I> T saveOrUpdate(I id, EntityManager entityManager, T entity) {
        if (id == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    /**
     * Performs a query with a limit and offset
     *
     * @param query    {@link javax.persistence.TypedQuery}
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned
     * @return valid list of entities, can be empty
     */
    public static <T> List<T> getPagedResultList(TypedQuery<T> query, int offset, int pageSize) {
        if (pageSize >= LARGE_PAGESIZE) {
            log.warn("Requesting potentially large resultset. Pagesize is {}", pageSize);
        }
        query.setFirstResult(offset).setMaxResults(pageSize);
        return query.getResultList();
    }

    /**
     * Clears an original list and adds all items from the passed in base type list
     * @param target the list to replace
     * @param newList the list to replace with
     * @param clazz the target class
     * @param <E> The base type
     * @param <T> The target type
     */
    public static <E, T extends E> void clearAndAdd(List<T> target, List<E> newList, Class<T> clazz) {
        target.clear();
        if (newList != null) {
            for (E e : newList) {
                if (e.getClass().equals(clazz)) {
                    target.add((T)e);
                } else {
                    throw new NotSupportedException("Cannot directly set list composed of non JPA Entities");
                }
            }
        }
    }
}
