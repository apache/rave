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

package org.apache.rave.gadgets.oauth.service.impl;

import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.apache.rave.gadgets.oauth.service.OAuthTokenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link OAuthTokenInfoService}
 */
@Service
public class DefaultOAuthTokenInfoService implements OAuthTokenInfoService {
    private final OAuthTokenInfoRepository repository;

    @Autowired
    public DefaultOAuthTokenInfoService(OAuthTokenInfoRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                             String tokenName, String serviceName) {
        return repository.findOAuthTokenInfo(userId, appUrl, moduleId, tokenName, serviceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthTokenInfo saveOAuthTokenInfo(OAuthTokenInfo tokenInfo) {
        return repository.save(tokenInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteOAuthTokenInfo(String userId, String appUrl, String moduleId, String tokenName,
                                     String serviceName) {
        final OAuthTokenInfo oAuthTokenInfo =
                findOAuthTokenInfo(userId, appUrl, moduleId, tokenName, serviceName);
        if (oAuthTokenInfo != null) {
            repository.delete(oAuthTokenInfo);
        }
    }
}
