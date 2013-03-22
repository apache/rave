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

package org.apache.rave.gadgets.oauth.service;

import org.apache.rave.model.OAuthTokenInfo;

/**
 * Service to handle OAuth Tokens
 */
public interface OAuthTokenInfoService {
    /**
     * Retrieves {@link org.apache.rave.model.OAuthTokenInfo}
     *
     * @param userId      unique identifier of gadget viewer
     * @param appUrl      URL of the gadget
     * @param moduleId    the module ID of the application
     * @param tokenName   gadget's nickname for the token to use
     * @param serviceName name of the service provider
     * @return {@link org.apache.rave.model.OAuthTokenInfo} or {@literal null} if none matches the criteria
     */
    OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId,
                                             String tokenName, String serviceName);

    /**
     * Persists the {@link org.apache.rave.model.OAuthTokenInfo} to the data store
     *
     * @param tokenInfo {@link org.apache.rave.model.OAuthTokenInfo} to save
     * @return persisted OAuthTokenInfo
     */
    OAuthTokenInfo saveOAuthTokenInfo(OAuthTokenInfo tokenInfo);

    /**
     * Removes the {@link org.apache.rave.model.OAuthTokenInfo}'s that match the criteria from the data store
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
