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

import org.apache.rave.portal.model.JpaWidgetComment;
import org.apache.rave.model.WidgetComment;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a WidgetComment to a JpaWidgetComment
 */
@Component
public class JpaWidgetCommentConverter {

    @PersistenceContext
    private EntityManager manager;

    public Class<WidgetComment> getSourceType() {
        return WidgetComment.class;
    }

    public JpaWidgetComment convert(WidgetComment source, String widgetId) {
        return source instanceof JpaWidgetComment ? (JpaWidgetComment) source : createEntity(source, widgetId);
    }

    private JpaWidgetComment createEntity(WidgetComment source, String widgetId) {
        JpaWidgetComment converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaWidgetComment() : manager.find(JpaWidgetComment.class, Long.parseLong(source.getId()));

            if (converted == null) {
                converted = new JpaWidgetComment();
            }
            updateProperties(source, converted, widgetId);
        }
        return converted;
    }

    private void updateProperties(WidgetComment source, JpaWidgetComment converted, String widgetId) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setCreatedDate(source.getCreatedDate());
        converted.setLastModifiedDate(source.getLastModifiedDate());
        converted.setText(source.getText());
        converted.setUserId(source.getUserId());
        converted.setWidgetId(widgetId);
    }
}
