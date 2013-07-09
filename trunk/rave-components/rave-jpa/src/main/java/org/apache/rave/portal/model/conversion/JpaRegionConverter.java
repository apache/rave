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
import org.apache.rave.portal.model.JpaRegion;
import org.apache.rave.model.Region;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaRegionConverter implements ModelConverter<Region, JpaRegion> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Region> getSourceType() {
        return Region.class;
    }

    @Override
    public JpaRegion convert(Region source) {
        return source instanceof JpaRegion ? (JpaRegion) source : createEntity(source);
    }

    private JpaRegion createEntity(Region source) {
        JpaRegion converted = null;
        if (source != null) {
            TypedQuery<JpaRegion> query = manager.createNamedQuery(JpaRegion.FIND_BY_ENTITY_ID, JpaRegion.class);
            query.setParameter(JpaRegion.ENTITY_ID_PARAM, source.getId() == null ? null : Long.parseLong(source.getId()));
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaRegion();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(Region source, JpaRegion converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setLocked(source.isLocked());
        converted.setPage(source.getPage());
        converted.setRegionWidgets(source.getRegionWidgets());
        converted.setRenderOrder(source.getRenderOrder());
    }
}
