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

import org.apache.rave.model.ModelConverter;
import org.apache.rave.persistence.jpa.util.JpaUtil;
import org.apache.rave.portal.model.JpaWidgetTag;
import org.apache.rave.portal.model.WidgetTag;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Converts from a {@link org.apache.rave.portal.model.WidgetTag} to a {@link org.apache.rave.portal.model.JpaWidgetTag}
 */
@Component
public class JpaWidgetTagConverter implements ModelConverter<WidgetTag, JpaWidgetTag> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<WidgetTag> getSourceType() {
        return WidgetTag.class;
    }

    @Override
    public JpaWidgetTag convert(WidgetTag source) {
        return source instanceof JpaWidgetTag ? (JpaWidgetTag)source : createEntity(source);
    }

    private JpaWidgetTag createEntity(WidgetTag source) {
        JpaWidgetTag convertedWidgetTag;
        TypedQuery<JpaWidgetTag> query = manager.createNamedQuery(JpaWidgetTag.FIND_BY_WIDGET_AND_KEYWORD, JpaWidgetTag.class);
        query.setParameter(JpaWidgetTag.WIDGET_ID_PARAM, source.getWidgetId());
        query.setParameter(JpaWidgetTag.TAG_KEYWORD_PARAM, source.getTag().getKeyword());
        convertedWidgetTag = JpaUtil.getSingleResult(query.getResultList());

        if (convertedWidgetTag == null){
            convertedWidgetTag = new JpaWidgetTag();
        }
        updateProperties(source, convertedWidgetTag);
        return convertedWidgetTag;
    }

    private void updateProperties(WidgetTag source, JpaWidgetTag convertedWidgetTag) {
        convertedWidgetTag.setCreatedDate(source.getCreatedDate());
        convertedWidgetTag.setTag(source.getTag());
        convertedWidgetTag.setUser(source.getUser());
        convertedWidgetTag.setWidgetId(source.getWidgetId());
    }
}
