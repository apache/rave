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

import org.apache.rave.persistence.jpa.util.JpaUtil;
import org.apache.rave.portal.model.JpaWidgetTag;
import org.apache.rave.model.WidgetTag;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Converts from a {@link org.apache.rave.model.WidgetTag} to a {@link org.apache.rave.portal.model.JpaWidgetTag}
 */
@Component
public class JpaWidgetTagConverter {

    @PersistenceContext
    private EntityManager manager;

    public Class<WidgetTag> getSourceType() {
        return WidgetTag.class;
    }

    public JpaWidgetTag convert(WidgetTag source, String widgetId) {
        return source instanceof JpaWidgetTag ? (JpaWidgetTag)source : createEntity(source, widgetId);
    }

    private JpaWidgetTag createEntity(WidgetTag source, String widgetId) {
        Long widgetEntityId = Long.parseLong(widgetId);
        JpaWidgetTag convertedWidgetTag;
        TypedQuery<JpaWidgetTag> query = manager.createNamedQuery(JpaWidgetTag.FIND_BY_WIDGETID_AND_TAGID, JpaWidgetTag.class);
        query.setParameter(JpaWidgetTag.WIDGET_ID_PARAM, widgetEntityId);
        query.setParameter(JpaWidgetTag.TAG_ID_PARAM, Long.parseLong(source.getTagId()));
        convertedWidgetTag = JpaUtil.getSingleResult(query.getResultList());

        if (convertedWidgetTag == null){
            convertedWidgetTag = new JpaWidgetTag();
        }
        updateProperties(source, convertedWidgetTag, widgetEntityId);
        return convertedWidgetTag;
    }

    private void updateProperties(WidgetTag source, JpaWidgetTag convertedWidgetTag, Long widgetId) {
        convertedWidgetTag.setCreatedDate(source.getCreatedDate());
        convertedWidgetTag.setTagEntityId(Long.parseLong(source.getTagId()));
        convertedWidgetTag.setUserEntityId(Long.parseLong(source.getUserId()));
        convertedWidgetTag.setWidgetEntityId(widgetId);
    }
}
