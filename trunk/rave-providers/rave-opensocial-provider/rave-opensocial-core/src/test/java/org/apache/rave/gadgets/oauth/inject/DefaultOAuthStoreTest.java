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

package org.apache.rave.gadgets.oauth.inject;

import net.oauth.OAuth;
import net.oauth.OAuthServiceProvider;
import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthConsumerStoreImpl;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.apache.rave.gadgets.oauth.service.OAuthConsumerStoreService;
import org.apache.rave.gadgets.oauth.service.OAuthTokenInfoService;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.social.core.oauth.OAuthSecurityToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link DefaultOAuthStore}
 */
public class DefaultOAuthStoreTest {
    private static final long NEXT_HOUR = 1000L * 60L * 60L;
    private static final String GADGET_URI = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String SERVICE_NAME = "testService";
    private static final String CONSUMER_SECRET = "gadgetSecret";

    private SecurityToken token;
    private DefaultOAuthStore oAuthStore;
    private OAuthConsumerStoreService consumerStoreService;
    private OAuthTokenInfoService tokenInfoService;

    @Test
    public void testGetConsumerKeyAndSecret() throws Exception {
        OAuthConsumerStore consumerStore = new OAuthConsumerStoreImpl();
        consumerStore.setGadgetUri(GADGET_URI);
        consumerStore.setConsumerKey("gadgetConsumer");
        consumerStore.setConsumerSecret(CONSUMER_SECRET);
        consumerStore.setKeyType(OAuthConsumerStore.KeyType.HMAC_SYMMETRIC);
        OAuthServiceProvider provider = new OAuthServiceProvider(null, null, null);

        expect(consumerStoreService.findByUriAndServiceName(GADGET_URI, SERVICE_NAME))
                .andReturn(consumerStore);
        replay(consumerStoreService);

        final OAuthStore.ConsumerInfo keyAndSecret =
                oAuthStore.getConsumerKeyAndSecret(token, SERVICE_NAME, provider);
        assertNotNull(keyAndSecret);
        assertEquals(OAuth.HMAC_SHA1, keyAndSecret.getConsumer().getProperty(
                OAuth.OAUTH_SIGNATURE_METHOD));

        verify(consumerStoreService);

    }

    @Test
    public void testGetTokenInfo() throws Exception {
        final String testTokenName = "testTokenName";

        OAuthTokenInfo oAuthTokenInfo = new OAuthTokenInfoImpl();
        oAuthTokenInfo.setTokenName(testTokenName);
        oAuthTokenInfo.setTokenSecret(CONSUMER_SECRET);
        OAuthStore.ConsumerInfo consumerInfo = createMock(OAuthStore.ConsumerInfo.class);

        expect(tokenInfoService.findOAuthTokenInfo(token.getViewerId(), token.getAppUrl(),
                OAuthTokenInfoImpl.MODULE_ID, testTokenName,
                SERVICE_NAME)).andReturn(oAuthTokenInfo);
        replay(tokenInfoService);

        final OAuthStore.TokenInfo tokenInfo = oAuthStore.getTokenInfo(token, consumerInfo,
                SERVICE_NAME, testTokenName);

        assertNotNull("Got TokenInfo", tokenInfo);
        assertEquals(CONSUMER_SECRET, tokenInfo.getTokenSecret());

        verify(tokenInfoService);
    }

    @Test
    public void testSetTokenInfo() throws Exception {
        BasicOAuthStoreConsumerKeyAndSecret defaultKey =
                DefaultOAuthStore.loadDefaultKey("keys/oauthkey.pem", "consumer-test-key");
        assertNotNull("defaultKey", defaultKey);
        assertEquals(BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE,
                defaultKey.getKeyType());
        assertEquals("consumer-test-key", defaultKey.getKeyName());
        final String keyFileContents = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANXjbMKL9N+9950V7QaDhr7JbF5uJtFgsiCsRjYDT9SaVCaNk2zXRXzqj2acKpAthV0R+4cVeWBN0mDL8CE/Rjo0r+9375DrSpi+jb+hnhYTGfiGbpJrUfCxlOXrvdsw4kZpLVKaj8wZFPb11Cnl5s1QBpPGWs1ij/qj/V04xRwXAgMBAAECgYEA0jXUPFgE8KjpZQ+Zhl9Z3MRlp2Em8XzRVF88GfWjTdXngoR+MehYuO5mxXgSNOUoP1JfHGI0ijux2cRVWrevMdO+0bkezMmgWlBTAqgoidwauX+0NyIRJOOG0anggmSrrf8jocjDLp7ZEhVjmtMzvys6P4RyFaNKXNyxK7J1/LECQQD/fB8vKvenzm9NNEdQyap3d0LYqWd/47NiOCCoS8K/DGVVIZiQQigUOi3ZTY6LV2Eb2RtwRnwBXMsKuQUTHZGDAkEA1lHU2OYGYP7SMu+fxzwFRNx0DrXWqIe0XFQX6EDRbk5H/eKgVt1tER8Mbin/z5utvXXiGJAj6+Eop6uqNPUq3QJAWbFZwVV0XJU8vf38i4BBOG/GKApRK7Tk5TaPQIZYeHoBmUGSLhMLvw4tynxP7tteXEh8OY6FOnU5UyphfbSDwQJBAIC0oesjsH79aMQ4DS77x3pEHdpbry6EWHb99WF/04W3sxovx/SCgyY+DBv4UuydZCgcLAxuO3RDQkP3Hn8xIG0CQQC9keRoDlA7RkwCXq76vEzLUqWiRSeHbXkaniSp7/xqzlw1IeMTtzoqMcJAulNr9W+vKKBYtE3sjXLcmO/CvWk/";
        assertEquals(keyFileContents, defaultKey.getConsumerSecret());
    }

    @Before
    public void setup() throws IOException {
        token = new OAuthSecurityToken("john.doe", GADGET_URI,
                "myapp", "localhost", "default", new Date().getTime() + NEXT_HOUR);
        consumerStoreService = createNiceMock(OAuthConsumerStoreService.class);
        tokenInfoService = createNiceMock(OAuthTokenInfoService.class);
        oAuthStore = new DefaultOAuthStore("http://localhost:8080", "keys/oauthkey.pem",
                "consumer-test-key");
        ReflectionTestUtils.setField(oAuthStore, "consumerStoreService", consumerStoreService);
        ReflectionTestUtils.setField(oAuthStore, "tokenInfoService", tokenInfoService);
    }
}
