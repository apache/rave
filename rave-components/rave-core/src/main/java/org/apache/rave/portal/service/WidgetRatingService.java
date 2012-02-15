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
     * Updates the score of a {@link WidgetRating}
     *
     * @param widgetRating WidgetRating
     * @param score        value of the rating
     */
    @PreAuthorize("hasPermission(#widgetRating.entityId, 'org.apache.rave.portal.model.WidgetRating', 'update')")
    void updateScore(WidgetRating widgetRating, Integer score);

    /**
     * Saves a {@link WidgetRating} for a widget
     *
     * @param rating   WidgetRating
     */
    @PreAuthorize("hasPermission(#rating, 'create')")
    void saveWidgetRating(WidgetRating rating);

    /**
     * Removes the rating of a widget
     *
     * @param widgetId unique identifier of a {@link org.apache.rave.portal.model.Widget}
     * @param userId   unique identifier of a {@link org.apache.rave.portal.model.User}
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetRating', 'delete')")
    void removeWidgetRating(Long widgetId, Long userId);

    /**
     * Removes all widget ratings for a userId, for all widgets
     *
     * @param userId
     * @return the number of widget ratings deleted
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetRating', 'delete')")
    int removeAllWidgetRatings(Long userId);
}
