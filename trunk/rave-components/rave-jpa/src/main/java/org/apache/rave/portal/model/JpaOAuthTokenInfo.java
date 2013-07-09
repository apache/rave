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

package org.apache.rave.portal.model;

import org.apache.rave.model.OAuthTokenInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Bean for OAuth security TokenInfo
 */
@Entity
@Table(name = "oauth_token_info")
@NamedQueries(value = {
        @NamedQuery(name = JpaOAuthTokenInfo.FIND_OAUTH_TOKEN_INFO,
                query = "SELECT ti FROM JpaOAuthTokenInfo ti WHERE ti.userId = :userIdParam AND ti.appUrl = :appUrlParam AND ti.moduleId = :moduleIdParam AND ti.tokenName = :tokenNameParam AND ti.serviceName = :serviceNameParam")
})
public class JpaOAuthTokenInfo implements BasicEntity, OAuthTokenInfo {

    /**
     * Named query identifier to find {@link OAuthTokenInfo}
     */
    public static final String FIND_OAUTH_TOKEN_INFO = "OAuthTokenInfo.findOAuthTokenInfo";

    /**
     * Query param for user entityId
     */
    public static final String USER_ID_PARAM = "userIdParam";

    /**
     * Query param for the app url
     */
    public static final String APP_URL_PARAM = "appUrlParam";

    /**
     * Identifier of the module. In case of Shindig, use {@link #MODULE_ID}
     */
    public static final String MODULE_ID_PARAM = "moduleIdParam";

    /**
     * Name of the Token
     */
    public static final String TOKEN_NAME_PARAM = "tokenNameParam";

    /**
     * Name of the OAuth service
     */
    public static final String SERVICE_NAME_PARAM = "serviceNameParam";

    private static final int HASH_START = 7;
    private static final int HASH_INCREASE = 67;

    /**
     * The internal object ID used for references to this object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tokenInfoIdGenerator")
    @TableGenerator(name = "tokenInfoIdGenerator", table = "RAVE_SHINDIG_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "token_info", allocationSize = 1, initialValue = 1)
    @Column(name = "entity_id")
    private Long entityId;

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


/*    public JpaOAuthTokenInfo(SecurityToken securityToken, String serviceName,
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
    }*/

    @Override
    public String getId() {
        return getEntityId().toString();
    }

    @Override
    public void setId(String id) {
        setEntityId(id == null ? null : Long.parseLong(id));
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaOAuthTokenInfo other = (JpaOAuthTokenInfo) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_START;
        hash = HASH_INCREASE * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OAuthTokenInfo{" +
                "entityId=" + entityId +
                ", accessToken='" + accessToken + '\'' +
                ", tokenSecret='" + tokenSecret + '\'' +
                ", sessionHandle='" + sessionHandle + '\'' +
                ", tokenExpireMillis=" + tokenExpireMillis +
                ", appUrl='" + appUrl + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
