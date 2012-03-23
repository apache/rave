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

package org.apache.rave.opensocial.service.impl;

import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.social.core.oauth2.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class DefaultOAuth2Service implements OAuth2Service {
    @Override
    public OAuth2DataService getDataService() {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public void authenticateClient(OAuth2NormalizedRequest req) throws OAuth2Exception {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public void validateRequestForAuthCode(OAuth2NormalizedRequest req) throws OAuth2Exception {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public void validateRequestForAccessToken(OAuth2NormalizedRequest req) throws OAuth2Exception {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public void validateRequestForResource(OAuth2NormalizedRequest req, Object resourceRequest) throws OAuth2Exception {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code grantAuthorizationCode(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code grantAccessToken(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code grantRefreshToken(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code generateAuthorizationCode(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code generateAccessToken(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public OAuth2Code generateRefreshToken(OAuth2NormalizedRequest req) {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }
}
