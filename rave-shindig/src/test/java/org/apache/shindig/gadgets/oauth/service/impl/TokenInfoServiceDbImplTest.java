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

package org.apache.shindig.gadgets.oauth.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.gadgets.oauth.model.OAuthTokenInfoDb;
import org.apache.shindig.gadgets.oauth.service.TokenInfoService;
import org.apache.shindig.social.core.oauth.OAuthSecurityToken;
import org.apache.shindig.social.opensocial.jpa.openjpa.OpenJPADbModule;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Test class for {@link TokenInfoServiceDbImpl}
 */
public class TokenInfoServiceDbImplTest {
    private TokenInfoService service;
    private static final long EXPIRE_TIME = new Date().getTime() + 1000L + 60L + 60L;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new OpenJPADbModule());
        service = injector.getInstance(TokenInfoServiceDbImpl.class);

    }

    @Test
    public void testCrudOperations() throws Exception {
        assertNull("Empty db", service.findOAuthTokenInfo("userId", "appUrl",
                OAuthTokenInfoDb.MODULE_ID, "tokenName", "serviceName"));

        OAuthStore.TokenInfo oAuthTokenInfo = new OAuthStore.TokenInfo("accessToken",
                "tokenSecret", "sessionHandle", EXPIRE_TIME);

        SecurityToken securityToken = new OAuthSecurityToken("userId", "appUrl",
                "appId", "domain", "container", EXPIRE_TIME);

        OAuthTokenInfoDb tokenInfo = new OAuthTokenInfoDb(securityToken, "serviceName",
                "tokenName", oAuthTokenInfo);

        service.saveOAuthTokenInfo(tokenInfo);
        OAuthTokenInfoDb tokenInfoDb = service.findOAuthTokenInfo("userId", "appUrl",
                OAuthTokenInfoDb.MODULE_ID, "tokenName", "serviceName");
        assertEquals("Found in db", tokenInfo, tokenInfoDb);

        long tokenInfoId = tokenInfoDb.getObjectId();

        tokenInfoDb.setTokenName("newTokenName");
        service.saveOAuthTokenInfo(tokenInfoDb);
        assertNull("Cannot find with old criteria", service.findOAuthTokenInfo(
                "userId", "appUrl", OAuthTokenInfoDb.MODULE_ID, "tokenName", "serviceName"));
        assertEquals("Same id after update", tokenInfoId,
                service.findOAuthTokenInfo("userId", "appUrl", OAuthTokenInfoDb.MODULE_ID,
                        "newTokenName", "serviceName").getObjectId());

        service.deleteOAuthTokenInfo("userId", "appUrl", OAuthTokenInfoDb.MODULE_ID, "newTokenName",
                "serviceName");
        assertNull("Empty db", service.findOAuthTokenInfo(
                "userId", "appUrl", OAuthTokenInfoDb.MODULE_ID, "newTokenName", "serviceName"));

    }

}
