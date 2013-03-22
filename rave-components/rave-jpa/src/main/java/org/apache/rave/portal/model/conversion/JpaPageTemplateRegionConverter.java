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
import org.apache.rave.portal.model.JpaPageTemplateRegion;
import org.apache.rave.model.PageTemplateRegion;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class JpaPageTemplateRegionConverter implements ModelConverter<PageTemplateRegion, JpaPageTemplateRegion> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageTemplateRegion> getSourceType() {
        return PageTemplateRegion.class;
    }

    @Override
    public JpaPageTemplateRegion convert(PageTemplateRegion source) {
        return source instanceof JpaPageTemplateRegion ? (JpaPageTemplateRegion) source : createEntity(source);
    }

    private JpaPageTemplateRegion createEntity(PageTemplateRegion source) {
        JpaPageTemplateRegion converted = null;

        if (source != null) {
            converted = source.getId() == null ? new JpaPageTemplateRegion() : manager.find(JpaPageTemplateRegion.class, Long.parseLong(source.getId()));    if(converted == null) {
                converted = new JpaPageTemplateRegion();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PageTemplateRegion source, JpaPageTemplateRegion converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setRenderSequence(source.getRenderSequence());
        converted.setPageTemplateWidgets(source.getPageTemplateWidgets());
        converted.setPageTemplate(source.getPageTemplate());
        converted.setLocked(source.isLocked());
    }
}
