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

import org.apache.rave.portal.model.JpaPortalPreference;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.conversion.JpaPortalPreferenceConverter;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link PortalPreferenceRepository}
 */
@Repository
public class JpaPortalPreferenceRepository implements PortalPreferenceRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaPortalPreferenceConverter converter;


    @Override
    public List<PortalPreference> getAll() {
        final TypedQuery<JpaPortalPreference> query =
                manager.createNamedQuery(JpaPortalPreference.GET_ALL, JpaPortalPreference.class);
        return CollectionUtils.<PortalPreference>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<PortalPreference> getLimitedList(int offset, int pageSize) {
        final TypedQuery<JpaPortalPreference> query =
                manager.createNamedQuery(JpaPortalPreference.GET_ALL, JpaPortalPreference.class);
        return CollectionUtils.<PortalPreference>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaPortalPreference.COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public PortalPreference getByKey(String key) {
        final TypedQuery<JpaPortalPreference> query =
                manager.createNamedQuery(JpaPortalPreference.GET_BY_KEY, JpaPortalPreference.class);
        query.setParameter(JpaPortalPreference.PARAM_KEY, key);
        return getSingleResult(query.getResultList());
    }

    @Override
    public Class<? extends PortalPreference> getType() {
        return JpaPortalPreference.class;
    }

    @Override
    public PortalPreference get(String id) {
        return manager.find(JpaPortalPreference.class, Long.parseLong(id));
    }

    @Override
    public PortalPreference save(PortalPreference item) {
        JpaPortalPreference pref = converter.convert(item);
        return saveOrUpdate(pref.getEntityId(), manager, pref);
    }

    @Override
    public void delete(PortalPreference item) {
        manager.remove(converter.convert(item));
    }
}
