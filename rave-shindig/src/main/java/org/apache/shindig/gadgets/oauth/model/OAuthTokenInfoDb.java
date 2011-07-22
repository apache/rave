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

package org.apache.shindig.gadgets.oauth.model;

/**
 * Bean for OAuth TokenInfo
 */

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.social.opensocial.jpa.api.DbObject;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "oauth_token_info")
public class OAuthTokenInfoDb implements DbObject {
    /**
     * @see {@link org.apache.shindig.social.core.oauth.OAuthSecurityToken#getModuleId()}
     */
    public static final String MODULE_ID = "NOT_USED";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "oid")
    private long objectId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_secret")
    private String tokenSecret;

    @Column(name = "session_handle")
    private String sessionHandle;

    @Column(name = "token_expire_millis")
    private long tokenExpireMillis;

    @Column(name = "app_url")
    private String appUrl;

    @Column(name = "module_id")
    private String moduleId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "token_name")
    private String tokenName;

    @Column(name = "user_id")
    private String userId;

    public OAuthTokenInfoDb() {
        super();
    }

    public OAuthTokenInfoDb(SecurityToken securityToken, String serviceName,
                            String tokenName, OAuthStore.TokenInfo tokenInfo) {
        this.setAccessToken(tokenInfo.getAccessToken());
        this.setAppUrl(securityToken.getAppUrl());
        this.setModuleId(MODULE_ID);
        this.setServiceName(serviceName);
        this.setSessionHandle(tokenInfo.getSessionHandle());
        this.setTokenExpireMillis(tokenInfo.getTokenExpireMillis());
        this.setTokenName(tokenName);
        this.setTokenSecret(tokenInfo.getTokenSecret());
        this.setUserId(securityToken.getViewerId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getObjectId() {
        return objectId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getSessionHandle() {
        return sessionHandle;
    }

    public void setSessionHandle(String sessionHandle) {
        this.sessionHandle = sessionHandle;
    }

    public long getTokenExpireMillis() {
        return tokenExpireMillis;
    }

    public void setTokenExpireMillis(long tokenExpireMillis) {
        this.tokenExpireMillis = tokenExpireMillis;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
