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
import org.apache.rave.portal.model.Group;
import org.apache.rave.portal.model.JpaGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

public class JpaGroupConverter implements ModelConverter<Group, JpaGroup> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Group> getSourceType() {
        return Group.class;
    }

    @Override
    public JpaGroup convert(Group source) {
        return source instanceof JpaGroup ? (JpaGroup)source : createEntity(source);
    }

    private JpaGroup createEntity(Group source) {
        JpaGroup converted;
        TypedQuery<JpaGroup> query = manager.createNamedQuery(JpaGroup.FIND_BY_TITLE, JpaGroup.class);
        query.setParameter(JpaGroup.GROUP_ID_PARAM, source.getEntityId());
        converted = getSingleResult(query.getResultList());

        if(converted == null) {
            converted = new JpaGroup();
        }
        updateProperties(source, converted);
        return converted;
    }

    private void updateProperties(Group source, JpaGroup converted) {
        converted.setDescription(source.getDescription());
        converted.setTitle(source.getTitle());
        converted.setEntityId(source.getEntityId());
        converted.setOwner(source.getOwner());
        converted.setMembers(source.getMembers());
    }
}
