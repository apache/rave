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
import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.model.PageLayout;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaPageLayoutConverter implements ModelConverter<PageLayout, JpaPageLayout> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageLayout> getSourceType() {
        return PageLayout.class;
    }

    @Override
    public JpaPageLayout convert(PageLayout source) {
        return source instanceof JpaPageLayout ? (JpaPageLayout) source : createEntity(source);
    }

    private JpaPageLayout createEntity(PageLayout source) {
        JpaPageLayout converted = null;
        if (source != null) {
            TypedQuery<JpaPageLayout> query = manager.createNamedQuery(JpaPageLayout.PAGELAYOUT_GET_BY_LAYOUT_CODE, JpaPageLayout.class);
            query.setParameter(JpaPageLayout.CODE_PARAM, source.getCode());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaPageLayout();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PageLayout source, JpaPageLayout converted) {
        converted.setCode(source.getCode());
        converted.setNumberOfRegions(source.getNumberOfRegions());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setUserSelectable(source.isUserSelectable());
    }
}
