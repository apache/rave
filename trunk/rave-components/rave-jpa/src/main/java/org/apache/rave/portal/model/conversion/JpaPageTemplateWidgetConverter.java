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
import org.apache.rave.portal.model.JpaPageTemplateWidget;
import org.apache.rave.model.PageTemplateWidget;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class JpaPageTemplateWidgetConverter implements ModelConverter<PageTemplateWidget, JpaPageTemplateWidget> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageTemplateWidget> getSourceType() {
        return PageTemplateWidget.class;
    }

    @Override
    public JpaPageTemplateWidget convert(PageTemplateWidget source) {
        return source instanceof JpaPageTemplateWidget ? (JpaPageTemplateWidget) source : createEntity(source);
    }

    private JpaPageTemplateWidget createEntity(PageTemplateWidget source) {
        JpaPageTemplateWidget converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaPageTemplateWidget() : manager.find(JpaPageTemplateWidget.class, Long.parseLong(source.getId()));   if(converted == null) {
                converted = new JpaPageTemplateWidget();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PageTemplateWidget source, JpaPageTemplateWidget converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setHideChrome(source.isHideChrome());
        converted.setPageTemplateRegion(source.getPageTemplateRegion());
        converted.setRenderSeq(source.getRenderSeq());
        converted.setWidgetId(source.getWidgetId());
        converted.setLocked(source.isLocked());
    }
}
