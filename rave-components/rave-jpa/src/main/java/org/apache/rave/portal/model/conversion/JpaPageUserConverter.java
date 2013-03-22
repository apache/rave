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
import org.apache.rave.portal.model.JpaPageUser;
import org.apache.rave.model.PageUser;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a PageUser to a JpaPageUser
 */
@Component
public class JpaPageUserConverter implements ModelConverter<PageUser, JpaPageUser> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageUser> getSourceType() {
        return PageUser.class;
    }

    @Override
    public JpaPageUser convert(PageUser source) {
        return source instanceof JpaPageUser ? (JpaPageUser) source : createEntity(source);
    }

    private JpaPageUser createEntity(PageUser source) {
        JpaPageUser converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaPageUser() : manager.find(JpaPageUser.class, Long.parseLong(source.getId()));  if(converted == null) {
                converted = new JpaPageUser();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PageUser source, JpaPageUser converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setPage(source.getPage());
        converted.setPageStatus(source.getPageStatus());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setUserId(source.getUserId());
        converted.setEditor(source.isEditor());
    }
}
