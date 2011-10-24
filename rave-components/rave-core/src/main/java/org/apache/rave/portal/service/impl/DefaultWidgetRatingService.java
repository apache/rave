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

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.apache.rave.portal.service.WidgetRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
Default implementation for {@link org.apache.rave.portal.service.WidgetRatingService}
 */
@Service
public class DefaultWidgetRatingService implements WidgetRatingService{

    private final WidgetRatingRepository repository;

    @Autowired
    public DefaultWidgetRatingService(WidgetRatingRepository repository) {
        this.repository = repository;
    }

    @Override
    public WidgetRating getByWidgetIdAndUserId(Long widgetId, Long userId) {
        return repository.getByWidgetIdAndUserId(widgetId, userId);
    }

    @Override
    public void updateScore(WidgetRating widgetRating, Integer score) {
        widgetRating.setScore(score);
        repository.save(widgetRating);
    }
}
