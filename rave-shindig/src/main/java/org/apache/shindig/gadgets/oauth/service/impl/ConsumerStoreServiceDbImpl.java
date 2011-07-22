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

package org.apache.shindig.gadgets.oauth.service.impl;

import com.google.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.gadgets.oauth.model.OAuthConsumerStoreDb;
import org.apache.shindig.gadgets.oauth.service.ConsumerStoreService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * DB (OpenJPA) implementation of {@link ConsumerStoreService}
 */
public class ConsumerStoreServiceDbImpl implements ConsumerStoreService {
    private static final String GADGET_URI_PARAM = "gadgetUriParam";
    private static final String SERVICE_NAME_PARAM = "serviceNameParam";

    /**
     * This is the JPA entity manager, shared by all threads accessing this service (need to check
     * that its really thread safe).
     */
    private EntityManager entityManager;

    @Inject
    public ConsumerStoreServiceDbImpl(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    public OAuthConsumerStoreDb findOAuthConsumerStore(final String gadgetUri,
                                                       final String serviceName) {
        if (StringUtils.isBlank(gadgetUri) || StringUtils.isBlank(serviceName)) {
            return null;
        }
        final Query query = getFindOAuthConsumerStoreQuery(gadgetUri, serviceName);
        final List results = query.getResultList();
        if (CollectionUtils.isNotEmpty(results)) {
            return (OAuthConsumerStoreDb) results.get(0);
        }
        return null;
    }

    /**
     * TODO check how we can use @Transactional
     * {@inheritDoc}
     */
    @Override
    public void save(OAuthConsumerStoreDb consumerStoreDb) {
        entityManager.getTransaction().begin();
        entityManager.persist(consumerStoreDb);
        entityManager.getTransaction().commit();
    }

    /**
     * TODO check how we can use @Transactional
     * {@inheritDoc}
     */
    @Override
    public void delete(OAuthConsumerStoreDb consumerStoreDb) {
        entityManager.getTransaction().begin();
        entityManager.remove(consumerStoreDb);
        entityManager.getTransaction().commit();
    }

    /**
     * Builds {@link Query} to find a single {@link OAuthConsumerStoreDb}
     *
     * @param gadgetUri   location of the gadget definition
     * @param serviceName name of the service provider
     * @return {@link Query}
     */
    Query getFindOAuthConsumerStoreQuery(final String gadgetUri, final String serviceName) {
        StringBuilder qString = new StringBuilder();
        qString.append("SELECT x FROM OAuthConsumerStoreDb x ");
        qString.append("WHERE x.gadgetUri = :").append(GADGET_URI_PARAM);
        qString.append(" and x.serviceName = :").append(SERVICE_NAME_PARAM);
        Query q = entityManager.createQuery(qString.toString());
        q.setParameter(GADGET_URI_PARAM, gadgetUri).setParameter(SERVICE_NAME_PARAM, serviceName);
        q.setFirstResult(0).setMaxResults(1);
        return q;
    }

}
