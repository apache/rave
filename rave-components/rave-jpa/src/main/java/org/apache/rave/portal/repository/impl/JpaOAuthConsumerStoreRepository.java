/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.JpaOAuthConsumerStore;
import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.portal.model.conversion.JpaOAuthConsumerStoreConverter;
import org.apache.rave.portal.repository.OAuthConsumerStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link OAuthConsumerStoreRepository}
 */
@Repository
public class JpaOAuthConsumerStoreRepository implements OAuthConsumerStoreRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaOAuthConsumerStoreConverter converter;

    @Override
    public Class<? extends OAuthConsumerStore> getType() {
        return JpaOAuthConsumerStore.class;
    }

    @Override
    public OAuthConsumerStore get(String id) {
        return manager.find(JpaOAuthConsumerStore.class, Long.parseLong(id));
    }

    @Override
    public OAuthConsumerStore save(OAuthConsumerStore item) {
        JpaOAuthConsumerStore jpaItem = converter.convert(item);
        return saveOrUpdate(jpaItem.getEntityId(), manager, jpaItem);
    }

    @Override
    public void delete(OAuthConsumerStore item) {
        manager.remove(converter.convert(item));
    }

    @Override
    public List<OAuthConsumerStore> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<OAuthConsumerStore> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthConsumerStore findByUriAndServiceName(String gadgetUri, String serviceName) {
        TypedQuery<JpaOAuthConsumerStore> query = manager.createNamedQuery(
                JpaOAuthConsumerStore.FIND_BY_URI_AND_SERVICE_NAME, JpaOAuthConsumerStore.class);
        query.setParameter(JpaOAuthConsumerStore.GADGET_URI_PARAM, gadgetUri);
        query.setParameter(JpaOAuthConsumerStore.SERVICE_NAME_PARAM, serviceName);
        return getSingleResult(query.getResultList());
    }
}
