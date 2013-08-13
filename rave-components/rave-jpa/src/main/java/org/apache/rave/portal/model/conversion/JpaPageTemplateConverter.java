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
import org.apache.rave.portal.model.JpaPageTemplate;
import org.apache.rave.model.PageTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class JpaPageTemplateConverter implements ModelConverter<PageTemplate, JpaPageTemplate> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageTemplate> getSourceType() {
        return PageTemplate.class;
    }

    @Override
    public JpaPageTemplate convert(PageTemplate source) {
        if (source != null) {
            //Enforce consistent casing for page types
            source.setPageType(source.getPageType() == null ? null : source.getPageType().toUpperCase());
            return source instanceof JpaPageTemplate ? (JpaPageTemplate) source : createEntity(source);
        }
        return null;
    }

    private JpaPageTemplate createEntity(PageTemplate source) {
        JpaPageTemplate converted = source.getId() == null ? new JpaPageTemplate() : manager.find(JpaPageTemplate.class, Long.parseLong(source.getId()));
        if (converted == null) {
            converted = new JpaPageTemplate();
        }
        updateProperties(source, converted);

        return converted;
    }

    private void updateProperties(PageTemplate source, JpaPageTemplate converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setName(source.getName());
        converted.setDescription(source.getDescription());
        converted.setPageType(source.getPageType());
        converted.setParentPageTemplate(source.getParentPageTemplate());
        converted.setSubPageTemplates(source.getSubPageTemplates());
        converted.setPageLayout(source.getPageLayout());
        converted.setPageTemplateRegions(source.getPageTemplateRegions());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setDefaultTemplate(source.isDefaultTemplate());
    }
}
