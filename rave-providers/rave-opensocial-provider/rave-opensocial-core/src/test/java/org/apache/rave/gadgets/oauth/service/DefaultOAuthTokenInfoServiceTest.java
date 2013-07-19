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

import org.apache.rave.gadgets.oauth.service.impl.DefaultOAuthTokenInfoService;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link org.apache.rave.gadgets.oauth.service.impl.DefaultOAuthTokenInfoService}
 */
public class DefaultOAuthTokenInfoServiceTest {
    private SecurityToken securityToken;
    OAuthStore.TokenInfo tokenInfo;
    private OAuthTokenInfoService service;
    private OAuthTokenInfoRepository repository;

    private static final String VALID_USER = "john.doe";
    private static final String INVALID_USER = "Invalid user";
    private static final String APP_URL = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String NOT_USED = "NOT USED";
    private static final String TOKEN_NAME = "";
    private static final String SERVICE_NAME = "testService";

    @Before
    public void setUp() throws Exception {
        securityToken = createNiceMock(SecurityToken.class);
        tokenInfo = createNiceMock(OAuthStore.TokenInfo.class);
        repository = createNiceMock(OAuthTokenInfoRepository.class);
        service = new DefaultOAuthTokenInfoService(repository);

    }

    @Test
    public void testFindOAuthTokenInfo() throws Exception {
        OAuthTokenInfo dbOAuthTokenInfo = getOAuthTokenInfo();
        expect(repository.findOAuthTokenInfo(VALID_USER, APP_URL, NOT_USED, TOKEN_NAME, SERVICE_NAME))
                .andReturn(dbOAuthTokenInfo);
        replay(repository);
        OAuthTokenInfo oAuthTokenInfo = service.findOAuthTokenInfo(VALID_USER, APP_URL, NOT_USED,
                TOKEN_NAME, SERVICE_NAME);
        assertNotNull(oAuthTokenInfo);
        assertEquals(APP_URL, oAuthTokenInfo.getAppUrl());

    }

    @Test
    public void testFindOAuthTokenInfo_nullValue() throws Exception {
        expect(repository.findOAuthTokenInfo(INVALID_USER, APP_URL, NOT_USED, TOKEN_NAME, SERVICE_NAME))
                .andReturn(null);
        replay(repository);

        OAuthTokenInfo oAuthTokenInfo = service.findOAuthTokenInfo(INVALID_USER, APP_URL, NOT_USED,
                TOKEN_NAME, SERVICE_NAME);

        assertNull(oAuthTokenInfo);

    }

    @Test
    public void testDeleteOAuthTokenInfo() {
        OAuthTokenInfo dbOAuthTokenInfo = getOAuthTokenInfo();
        expect(repository.findOAuthTokenInfo(VALID_USER, APP_URL, NOT_USED, TOKEN_NAME, SERVICE_NAME))
                .andReturn(dbOAuthTokenInfo);
        repository.delete(dbOAuthTokenInfo);
        expectLastCall();
        replay(repository);
        service.deleteOAuthTokenInfo(VALID_USER, APP_URL, NOT_USED, TOKEN_NAME, SERVICE_NAME);
        verify(repository);
    }

    private OAuthTokenInfo getOAuthTokenInfo() {
        expect(securityToken.getAppUrl()).andReturn(APP_URL);
        expect(tokenInfo.getAccessToken()).andReturn("accessToken");
        expect(tokenInfo.getSessionHandle()).andReturn("sessionHandle");
        expect(tokenInfo.getTokenExpireMillis()).andReturn(3600000L);
        expect(tokenInfo.getTokenSecret()).andReturn("tokenSecret");
        replay(securityToken, tokenInfo);
        return new OAuthTokenInfoImpl(securityToken.getAppUrl(), SERVICE_NAME, TOKEN_NAME, tokenInfo.getAccessToken(),
                tokenInfo.getSessionHandle(), tokenInfo.getTokenSecret(),
                securityToken.getViewerId(), tokenInfo.getTokenExpireMillis());
    }
}
