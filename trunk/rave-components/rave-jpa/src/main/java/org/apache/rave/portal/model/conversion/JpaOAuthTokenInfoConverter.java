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
import org.apache.rave.portal.model.JpaOAuthTokenInfo;
import org.apache.rave.model.OAuthTokenInfo;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a OAuthTokenInfo to a JpaOAuthTokenInfo
 */
@Component
public class JpaOAuthTokenInfoConverter implements ModelConverter<OAuthTokenInfo, JpaOAuthTokenInfo> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<OAuthTokenInfo> getSourceType() {
        return OAuthTokenInfo.class;
    }

    @Override
    public JpaOAuthTokenInfo convert(OAuthTokenInfo source) {
        return source instanceof JpaOAuthTokenInfo ? (JpaOAuthTokenInfo) source : createEntity(source);
    }

    private JpaOAuthTokenInfo createEntity(OAuthTokenInfo source) {
        JpaOAuthTokenInfo converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaOAuthTokenInfo() : manager.find(JpaOAuthTokenInfo.class, Long.parseLong(source.getId()));  if(converted == null) {
                converted = new JpaOAuthTokenInfo();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(OAuthTokenInfo source, JpaOAuthTokenInfo converted) {
        converted.setId(source.getId());
        converted.setAccessToken(source.getAccessToken());
        converted.setAppUrl(source.getAppUrl());
        converted.setModuleId(source.getModuleId());
        converted.setServiceName(source.getServiceName());
        converted.setSessionHandle(source.getSessionHandle());
        converted.setTokenExpireMillis(source.getTokenExpireMillis());
        converted.setTokenName(source.getTokenName());
        converted.setTokenSecret(source.getTokenSecret());
        converted.setUserId(source.getUserId());
    }
}
