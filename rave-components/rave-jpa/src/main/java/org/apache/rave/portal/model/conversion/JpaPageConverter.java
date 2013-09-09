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
import org.apache.rave.portal.model.JpaPage;
import org.apache.rave.model.Page;
import org.apache.rave.model.PageUser;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a Page to a JpaPage
 */
@Component
public class JpaPageConverter implements ModelConverter<Page, JpaPage> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Page> getSourceType() {
        return Page.class;
    }

    @Override
    public JpaPage convert(Page source) {
        //Enforce consistent casing for page types
        if (source != null) {
            source.setPageType(source.getPageType() == null ? null : source.getPageType().toUpperCase());
            return source instanceof JpaPage ? (JpaPage) source : createEntity(source);
        }
        return null;
    }

    private JpaPage createEntity(Page source) {
        JpaPage converted = source.getId() == null ? new JpaPage() : manager.find(JpaPage.class, Long.parseLong(source.getId()));
        if (converted == null) {
            converted = new JpaPage();
        }
        updateProperties(source, converted);
        return converted;
    }

    private void updateProperties(Page source, JpaPage converted) {
        replacePageReferences(source, converted);
        converted.setId(source.getId());
        converted.setMembers(source.getMembers());
        converted.setName(source.getName());
        converted.setOwnerId(source.getOwnerId());
        converted.setContextId(source.getContextId());
        converted.setPageLayout(source.getPageLayout());
        converted.setPageType(source.getPageType());
        converted.setParentPage(source.getParentPage());
        converted.setRegions(source.getRegions());
        converted.setSubPages(source.getSubPages());
    }

    private void replacePageReferences(Page source, JpaPage converted) {
        if (source.getMembers() != null) {
            for (PageUser user : source.getMembers()) {
                user.setPage(converted);
            }
        }
    }
}
