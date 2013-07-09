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
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.JpaPortalPreference;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts an PortalPreference JpaPortalPreference
 */
@Component
public class JpaPortalPreferenceConverter implements ModelConverter<PortalPreference, JpaPortalPreference> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PortalPreference> getSourceType() {
        return PortalPreference.class;
    }

    @Override
    public JpaPortalPreference convert(PortalPreference source) {
        return source instanceof JpaPortalPreference ? (JpaPortalPreference) source : createEntity(source);
    }

    private JpaPortalPreference createEntity(PortalPreference source) {
        JpaPortalPreference converted = null;
        if (source != null) {
            TypedQuery<JpaPortalPreference> query = manager.createNamedQuery(JpaPortalPreference.GET_BY_KEY, JpaPortalPreference.class);
            query.setParameter(JpaPortalPreference.PARAM_KEY, source.getKey());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaPortalPreference();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PortalPreference source, JpaPortalPreference converted) {
        converted.setKey(source.getKey());
        converted.setValues(source.getValues());
    }
}
