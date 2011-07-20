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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shindig.gadgets.oauth.model.OAuthTokenInfoDb;
import org.apache.shindig.gadgets.oauth.service.TokenInfoService;

/**
 * DB (OpenJPA) implementation of {@link TokenInfoService}
 */
public class TokenInfoServiceDbImpl implements TokenInfoService {

    private static final String USER_ID_PARAM = "userIdParam";
    private static final String APP_URL_PARAM = "appUrlParam";
    private static final String MODULE_ID_PARAM = "moduleIdParam";
    private static final String TOKEN_NAME_PARAM = "tokenNameParam";
    private static final String SERVICE_NAME_PARAM = "serviceNameParam";

    /**
     * This is the JPA entity manager, shared by all threads accessing this service (need to check
     * that its really thread safe).
     */
    protected EntityManager entityManager;

    @Inject
    public TokenInfoServiceDbImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthTokenInfoDb findOAuthTokenInfo(String userId, String appUrl,
                                               String moduleId, String tokenName, String serviceName) {
        final Query query = getFindOAuthTokenInfoQuery(userId, appUrl, moduleId, tokenName, serviceName);
        final List results = query.getResultList();
        if (CollectionUtils.isNotEmpty(results)) {
            return (OAuthTokenInfoDb) results.get(0);
        }
        return null;
    }

    /**
     * TODO find how to use @Transactional
     * {@inheritDoc}
     */
    @Override
    public void saveOAuthTokenInfo(OAuthTokenInfoDb tokenInfoDb) {
        entityManager.getTransaction().begin();
        if (tokenInfoDb.getObjectId() == 0L) {
            entityManager.persist(tokenInfoDb);
        } else {
            entityManager.merge(tokenInfoDb);
        }
        entityManager.getTransaction().commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                     String tokenName, String serviceName) {
        OAuthTokenInfoDb tokenInfoDb = findOAuthTokenInfo(userId, appUrl, moduleId,
                tokenName, serviceName);
        if (tokenInfoDb != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(tokenInfoDb);
            entityManager.getTransaction().commit();
        }
    }


    /**
     * Builds {@link Query} to find a single {@link OAuthTokenInfoDb}
     *
     * @param userId      unique identifier of a user
     * @param appUrl      URL of the gadget
     * @param moduleId    the module ID of the application
     * @param tokenName   gadget's nickname for the token to use
     * @param serviceName name of the service provider
     * @return {@link Query}
     */
    Query getFindOAuthTokenInfoQuery(final String userId, final String appUrl, final String moduleId,
                                     final String tokenName, final String serviceName) {
        StringBuilder qString = new StringBuilder();
        qString.append("SELECT x FROM OAuthTokenInfoDb x ");
        qString.append("WHERE x.userId = :").append(USER_ID_PARAM);
        qString.append(" AND x.appUrl = :").append(APP_URL_PARAM);
        qString.append(" AND x.moduleId = :").append(MODULE_ID_PARAM);
        qString.append(" AND x.tokenName = :").append(TOKEN_NAME_PARAM);
        qString.append(" AND x.serviceName = :").append(SERVICE_NAME_PARAM);

        Query query = entityManager.createQuery(qString.toString());
        query.setParameter(USER_ID_PARAM, userId);
        query.setParameter(APP_URL_PARAM, appUrl);
        query.setParameter(MODULE_ID_PARAM, moduleId);
        query.setParameter(TOKEN_NAME_PARAM, tokenName);
        query.setParameter(SERVICE_NAME_PARAM, serviceName);
        query.setFirstResult(0).setMaxResults(1);
        return query;
    }

}
