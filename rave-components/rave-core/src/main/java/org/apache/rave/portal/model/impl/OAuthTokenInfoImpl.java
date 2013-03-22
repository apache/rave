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

package org.apache.rave.portal.model.impl;

import org.apache.rave.model.OAuthTokenInfo;

public class OAuthTokenInfoImpl implements OAuthTokenInfo {
    private String id;
    private String accessToken;
    private String tokenSecret;
    private String sessionHandle;
    private long tokenExpireMillis;
    private String appUrl;
    private String moduleId;
    private String serviceName;
    private String tokenName;
    private String userId;

    public OAuthTokenInfoImpl() {
    }

    public OAuthTokenInfoImpl(String appUrl, String serviceName,
                              String tokenName, String accessToken, String sessionHandle,
                              String tokenSecret, String userId, long tokenExpireMillis) {
        this.setAccessToken(accessToken);
        this.setAppUrl(appUrl);
        this.setModuleId(MODULE_ID);
        this.setServiceName(serviceName);
        this.setSessionHandle(sessionHandle);
        this.setTokenExpireMillis(tokenExpireMillis);
        this.setTokenName(tokenName);
        this.setTokenSecret(tokenSecret);
        this.setUserId(userId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getTokenSecret() {
        return tokenSecret;
    }

    @Override
    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public String getSessionHandle() {
        return sessionHandle;
    }

    @Override
    public void setSessionHandle(String sessionHandle) {
        this.sessionHandle = sessionHandle;
    }

    @Override
    public long getTokenExpireMillis() {
        return tokenExpireMillis;
    }

    @Override
    public void setTokenExpireMillis(long tokenExpireMillis) {
        this.tokenExpireMillis = tokenExpireMillis;
    }

    @Override
    public String getAppUrl() {
        return appUrl;
    }

    @Override
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getTokenName() {
        return tokenName;
    }

    @Override
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
