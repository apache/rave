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

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetCategory;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface WidgetCategoryService {

    /**
     * @param entityId unique identifier of the {@link org.apache.rave.portal.model.WidgetCategory}
     * @return WidgetCategory if it can be found, otherwise {@literal null}
     */
    @PostAuthorize("returnObject == null or hasPermission(returnObject, 'read')")
    WidgetCategory get(long entityId);

    /**
     * @return a {@link java.util.List} with all {@link org.apache.rave.portal.model.WidgetCategory}'s
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<WidgetCategory> getAll();

    /**
     * Creates a new WidgetCategory object
     * @param text the category text value
     * @param createdUser the user creating this category
     * @return a WidgetCategory object representing the new entity
     */
    @PostAuthorize("hasPermission(returnObject, 'create')")
    WidgetCategory create(String text, User createdUser);

    /**
     * Updates a widget category
     *
     * @param widgetCategoryId  the entityId of the WidgetCategory to update
     * @param text the new text value
     * @param lastModifiedUser the user performing the update
     * @return the updated WidgetCategory object
     */
    @PreAuthorize("hasPermission(#widgetCategoryId, 'org.apache.rave.portal.model.WidgetCategory', 'update')")
    WidgetCategory update(long widgetCategoryId, String text, User lastModifiedUser);

    /**
     * Deletes a WidgetCategory
     *
     * @param widgetCategory
     */
    @PreAuthorize("hasPermission(#widgetCategory.entityId, 'org.apache.rave.portal.model.WidgetCategory', 'delete')")
    void delete(WidgetCategory widgetCategory);
}
