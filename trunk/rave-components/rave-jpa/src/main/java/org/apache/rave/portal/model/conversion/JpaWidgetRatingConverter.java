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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.model.WidgetRating;
import org.springframework.stereotype.Component;

/**
 * Converts a WidgetRating to a JpaWidgetRating
 */
@Component
public class JpaWidgetRatingConverter {

    public Class<WidgetRating> getSourceType() {
        return WidgetRating.class;
    }

    public JpaWidgetRating convert(WidgetRating source, String widgetId) {
        return source instanceof JpaWidgetRating ? (JpaWidgetRating) source : createEntity(source, widgetId);
    }

    private JpaWidgetRating createEntity(WidgetRating source, String widgetId) {
        JpaWidgetRating converted = null;
        if(source != null) {
            converted = new JpaWidgetRating();
            updateProperties(source, converted, widgetId);
        }
        return converted;
    }

    private void updateProperties(WidgetRating source, JpaWidgetRating converted, String widgetId) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setScore(source.getScore());
        converted.setUserId(source.getUserId());
        converted.setWidgetId(widgetId);
    }
}
