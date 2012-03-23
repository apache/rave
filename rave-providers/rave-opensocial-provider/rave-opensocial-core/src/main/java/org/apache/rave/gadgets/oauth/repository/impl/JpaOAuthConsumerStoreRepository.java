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

package org.apache.rave.gadgets.oauth.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.rave.gadgets.oauth.model.OAuthConsumerStore;
import org.apache.rave.gadgets.oauth.repository.OAuthConsumerStoreRepository;
import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.springframework.stereotype.Repository;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link OAuthConsumerStoreRepository}
 */
@Repository
public class JpaOAuthConsumerStoreRepository extends AbstractJpaRepository<OAuthConsumerStore>
        implements OAuthConsumerStoreRepository {

    @PersistenceContext
    private EntityManager manager;


    public JpaOAuthConsumerStoreRepository() {
        super(OAuthConsumerStore.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthConsumerStore findByUriAndServiceName(String gadgetUri, String serviceName) {
        TypedQuery<OAuthConsumerStore> query = manager.createNamedQuery(
                OAuthConsumerStore.FIND_BY_URI_AND_SERVICE_NAME, OAuthConsumerStore.class);
        query.setParameter(OAuthConsumerStore.GADGET_URI_PARAM, gadgetUri);
        query.setParameter(OAuthConsumerStore.SERVICE_NAME_PARAM, serviceName);
        return getSingleResult(query.getResultList());
    }
}
