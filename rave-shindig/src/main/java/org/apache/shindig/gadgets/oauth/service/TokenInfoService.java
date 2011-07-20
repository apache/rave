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

package org.apache.shindig.gadgets.oauth.service;

import org.apache.shindig.gadgets.oauth.model.OAuthTokenInfoDb;

/**
 * Service to handle OAuth Tokens
 */
public interface TokenInfoService {

    /**
     * Retrieves {@link OAuthTokenInfoDb}
     *
     * @param userId      unique identifier of gadget viewer
     * @param appUrl      URL of the gadget
     * @param moduleId    the module ID of the application
     * @param tokenName   gadget's nickname for the token to use
     * @param serviceName name of the service provider
     * @return {@link OAuthTokenInfoDb} or {@literal null} if none matches the criteria
     */
    public OAuthTokenInfoDb findOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                               String tokenName, String serviceName);

    /**
     * Persists the {@link OAuthTokenInfoDb} to the data store
     *
     * @param tokenInfoDb {@link OAuthTokenInfoDb} to save
     */
    void saveOAuthTokenInfo(OAuthTokenInfoDb tokenInfoDb);

    /**
     * Removes the {@link OAuthTokenInfoDb}'s that match the criteria from the data store
     *
     * @param userId      unique identifier of the gadget viewer
     * @param appUrl      URL of the gadget
     * @param moduleId    the module ID of the application
     * @param tokenName   gadget's nickname for the token to use
     * @param serviceName name of the service provider
     */
    void deleteOAuthTokenInfo(String userId, String appUrl, String moduleId,
                              String tokenName, String serviceName);
}
