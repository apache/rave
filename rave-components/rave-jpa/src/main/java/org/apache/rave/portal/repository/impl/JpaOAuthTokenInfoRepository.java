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
import org.apache.rave.portal.model.JpaOAuthTokenInfo;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.conversion.JpaOAuthTokenInfoConverter;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link OAuthTokenInfoRepository}
 */
@Repository
public class JpaOAuthTokenInfoRepository implements OAuthTokenInfoRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaOAuthTokenInfoConverter converter;

    @Override
    public Class<? extends OAuthTokenInfo> getType() {
        return JpaOAuthTokenInfo.class;
    }

    @Override
    public OAuthTokenInfo get(String id) {
        return manager.find(JpaOAuthTokenInfo.class, Long.parseLong(id));
    }

    @Override
    public OAuthTokenInfo save(OAuthTokenInfo item) {
        JpaOAuthTokenInfo jpaItem = converter.convert(item);
        return saveOrUpdate(jpaItem.getEntityId(), manager, jpaItem);
    }

    @Override
    public void delete(OAuthTokenInfo item) {
        manager.remove(converter.convert(item));
    }

    @Override
    public List<OAuthTokenInfo> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<OAuthTokenInfo> getLimitedList(int offset, int limit) {
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
    public OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                             String tokenName, String serviceName) {
        TypedQuery<JpaOAuthTokenInfo> query = manager.createNamedQuery(JpaOAuthTokenInfo.FIND_OAUTH_TOKEN_INFO, JpaOAuthTokenInfo.class);
        query.setParameter(JpaOAuthTokenInfo.USER_ID_PARAM, userId);
        query.setParameter(JpaOAuthTokenInfo.APP_URL_PARAM, appUrl);
        query.setParameter(JpaOAuthTokenInfo.MODULE_ID_PARAM, moduleId);
        query.setParameter(JpaOAuthTokenInfo.TOKEN_NAME_PARAM, tokenName);
        query.setParameter(JpaOAuthTokenInfo.SERVICE_NAME_PARAM, serviceName);
        return getSingleResult(query.getResultList());
    }
}
