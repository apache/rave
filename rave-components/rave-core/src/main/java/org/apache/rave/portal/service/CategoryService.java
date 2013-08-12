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

package org.apache.rave.portal.service;

import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.rest.model.SearchResult;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CategoryService {

    /**
     * @param id unique identifier of the {@link org.apache.rave.model.Category}
     * @return Category if it can be found, otherwise {@literal null}
     */
    @PostAuthorize("returnObject == null or hasPermission(returnObject, 'read')")
    Category get(String id);

    /**
     * @return a {@link java.util.List} with all {@link org.apache.rave.model.Category}'s
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<Category> getAllList();

    //TODO: Put correct spring security annotations on following three methods (getAll, getLimitedList, getCountAll)
    /**
     * Gets a {@link SearchResult} for {@link Category}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<Category> getAll();


    /**
     * Gets a limited {@link org.apache.rave.rest.model.SearchResult} for {@link Category}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Category> getLimitedList(int offset, int pageSize);

    /**
     * Creates a new Category object
     * @param text the category text value
     * @param createdUser the user creating this category
     * @return a Category object representing the new entity
     */
    @PostAuthorize("hasPermission(returnObject, 'create')")
    Category create(String text, User createdUser);

    /**
     * Updates a Category
     *
     * @param categoryId  the entityId of the Category to update
     * @param text the new text value
     * @param lastModifiedUser the user performing the update
     * @return the updated Category object
     */
    @PreAuthorize("hasPermission(#categoryId, 'org.apache.rave.model.Category', 'update')")
    Category update(String categoryId, String text, User lastModifiedUser);

    /**
     * Deletes a Category
     *
     * @param category
     */
    @PreAuthorize("hasPermission(#category.id, 'org.apache.rave.model.Category', 'delete')")
    void delete(Category category);
}
