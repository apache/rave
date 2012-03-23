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

import org.apache.rave.gadgets.oauth.model.OAuthTokenInfo;
import org.apache.rave.gadgets.oauth.repository.OAuthTokenInfoRepository;
import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.springframework.stereotype.Repository;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link OAuthTokenInfoRepository}
 */
@Repository
public class JpaOAuthTokenInfoRepository extends AbstractJpaRepository<OAuthTokenInfo>
        implements OAuthTokenInfoRepository {

    @PersistenceContext
    private EntityManager manager;

    public JpaOAuthTokenInfoRepository() {
        super(OAuthTokenInfo.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                             String tokenName, String serviceName) {
        TypedQuery<OAuthTokenInfo> query = manager.createNamedQuery(OAuthTokenInfo.FIND_OAUTH_TOKEN_INFO,
                OAuthTokenInfo.class);
        query.setParameter(OAuthTokenInfo.USER_ID_PARAM, userId);
        query.setParameter(OAuthTokenInfo.APP_URL_PARAM, appUrl);
        query.setParameter(OAuthTokenInfo.MODULE_ID_PARAM, moduleId);
        query.setParameter(OAuthTokenInfo.TOKEN_NAME_PARAM, tokenName);
        query.setParameter(OAuthTokenInfo.SERVICE_NAME_PARAM, serviceName);
        return getSingleResult(query.getResultList());
    }

}
