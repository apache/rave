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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link PortalPreferenceRepository}
 */
@Repository
public class JpaPortalPreferenceRepository extends AbstractJpaRepository<PortalPreference> implements PortalPreferenceRepository {

    public JpaPortalPreferenceRepository() {
        super(PortalPreference.class);
    }

    @Override
    public List<PortalPreference> getAll() {
        final TypedQuery<PortalPreference> query =
                manager.createNamedQuery(PortalPreference.GET_ALL, PortalPreference.class);
        return query.getResultList();
    }

    @Override
    public PortalPreference getByKey(String key) {
        final TypedQuery<PortalPreference> query =
                manager.createNamedQuery(PortalPreference.GET_BY_KEY, PortalPreference.class);
        query.setParameter(PortalPreference.PARAM_KEY, key);
        return getSingleResult(query.getResultList());
    }
}
