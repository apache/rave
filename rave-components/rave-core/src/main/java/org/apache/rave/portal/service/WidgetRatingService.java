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

import org.apache.rave.portal.model.WidgetRating;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Interface for {@link org.apache.rave.portal.model.WidgetRating}
 */
public interface WidgetRatingService {

    /**
     * Gets a {@link WidgetRating} for the widgetId and userId
     *
     * @param widgetId unique identifier for a Widget
     * @param userId   unique identifier for a User
     * @return WidgetRating or {@link null} if there is no such combination
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetRating', 'read')")
    WidgetRating getByWidgetIdAndUserId(Long widgetId, Long userId);

    /**
     * Saves a {@link WidgetRating} for a widget
     *
     * @param widgetId unique identifier of a {@link org.apache.rave.portal.model.Widget}
     * @param score    value of the rating
     * @param userId   unique identifier of a {@link org.apache.rave.portal.model.User}
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetRating', 'create')")
    void saveWidgetRating(Long widgetId, Integer score, Long userId);

    /**
     * Removes the rating of a widget
     *
     * @param widgetId unique identifier of a {@link org.apache.rave.portal.model.Widget}
     * @param userId   unique identifier of a {@link org.apache.rave.portal.model.User}
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetRating', 'delete')")
    void removeWidgetRating(Long widgetId, Long userId);

}
