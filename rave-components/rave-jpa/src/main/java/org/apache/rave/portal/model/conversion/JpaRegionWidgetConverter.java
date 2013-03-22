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
import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.model.RegionWidget;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaRegionWidgetConverter implements ModelConverter<RegionWidget, JpaRegionWidget> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<RegionWidget> getSourceType() {
        return RegionWidget.class;
    }

    @Override
    public JpaRegionWidget convert(RegionWidget source) {
        return source instanceof JpaRegionWidget ? (JpaRegionWidget) source : createEntity(source);
    }

    private JpaRegionWidget createEntity(RegionWidget source) {
        JpaRegionWidget converted = null;
        if (source != null) {
            TypedQuery<JpaRegionWidget> query = manager.createNamedQuery(JpaRegionWidget.FIND_BY_ID, JpaRegionWidget.class);
            query.setParameter(JpaRegionWidget.PARAM_WIDGET_ID, source.getId() == null ? null : Long.parseLong(source.getId()));
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaRegionWidget();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(RegionWidget source, JpaRegionWidget converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setLocked(source.isLocked());
        converted.setCollapsed(source.isCollapsed());
        converted.setHideChrome(source.isHideChrome());
        converted.setPreferences(source.getPreferences());
        converted.setRegion(source.getRegion());
        converted.setRenderPosition(source.getRenderPosition());
        converted.setWidgetId(source.getWidgetId());
        converted.setRenderOrder(source.getRenderOrder());
    }
}
