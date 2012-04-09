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

package org.apache.rave.portal.repository;

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.WidgetRating;

public interface WidgetRatingRepository extends Repository<WidgetRating> {

    /**
     * Tries to find a {@link WidgetRating} by the id's of a Widget and USer
     *
     * @param widgetId unique identifier of a Widget
     * @param userId   unique identifier of a User
     * @return {@link WidgetRating} if it exists, otherwise {@literal null}
     */
    WidgetRating getByWidgetIdAndUserId(Long widgetId, Long userId);

    /**
     * Delete all Widget Ratings for a userId
     *
     * @param userId
     * @return count of ratings deleted
     */
    int deleteAll(Long userId);
}
