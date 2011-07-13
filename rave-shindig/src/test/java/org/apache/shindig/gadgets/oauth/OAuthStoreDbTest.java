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

package org.apache.shindig.gadgets.oauth;

import net.oauth.OAuthServiceProvider;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.oauth.jpa.OAuthConsumerStoreDb;
import org.apache.shindig.social.core.oauth.OAuthSecurityToken;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link OAuthStoreDb}
 */
public class OAuthStoreDbTest {
    private static final long NEXT_HOUR = 1000L * 60L * 60L;
    @Test
    public void testGetConsumerKeyAndSecret() throws Exception {
        //String userId, String appUrl, String appId, String domain,
        //String container, Long expiresAt
        SecurityToken token = new OAuthSecurityToken("john.doe", "http://localhost:8080/myapp",
                "myapp", "localhost", "default", new Date().getTime() + NEXT_HOUR);
        String serviceName = "testService";

        OAuthStoreDb oAuthStoreDb = new OAuthStoreDb("http://localhost:8080","keys/oauthkey.pem",
                "consumer-test-key");

        OAuthConsumerStoreDb consumerStore = new OAuthConsumerStoreDb();
        final String gadgetUri = "http://localhost:8080/samplecontainer/examples/oauth.xml";
        consumerStore.setGadgetUri(gadgetUri);
        consumerStore.setConsumerKey("gadgetConsumer");
        consumerStore.setConsumerSecret("gadgetSecret");
        consumerStore.setKeyType(OAuthConsumerStoreDb.KeyType.HMAC_SYMMETRIC);

        OAuthServiceProvider provider = new OAuthServiceProvider(null, null, null);
        final OAuthStore.ConsumerInfo keyAndSecret = oAuthStoreDb.getConsumerKeyAndSecret(token, serviceName, provider);
        assertNull(keyAndSecret);

        
    }

    @Test
    public void testLoadDefaultKey() throws Exception {
        BasicOAuthStoreConsumerKeyAndSecret defaultKey =
                OAuthStoreDb.loadDefaultKey("keys/oauthkey.pem", "consumer-test-key");
        assertNotNull("defaultKey", defaultKey);
        assertEquals(BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE, defaultKey.getKeyType());
        assertEquals("consumer-test-key", defaultKey.getKeyName());
        final String keyFileContents = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANXjbMKL9N+9950V7QaDhr7JbF5uJtFgsiCsRjYDT9SaVCaNk2zXRXzqj2acKpAthV0R+4cVeWBN0mDL8CE/Rjo0r+9375DrSpi+jb+hnhYTGfiGbpJrUfCxlOXrvdsw4kZpLVKaj8wZFPb11Cnl5s1QBpPGWs1ij/qj/V04xRwXAgMBAAECgYEA0jXUPFgE8KjpZQ+Zhl9Z3MRlp2Em8XzRVF88GfWjTdXngoR+MehYuO5mxXgSNOUoP1JfHGI0ijux2cRVWrevMdO+0bkezMmgWlBTAqgoidwauX+0NyIRJOOG0anggmSrrf8jocjDLp7ZEhVjmtMzvys6P4RyFaNKXNyxK7J1/LECQQD/fB8vKvenzm9NNEdQyap3d0LYqWd/47NiOCCoS8K/DGVVIZiQQigUOi3ZTY6LV2Eb2RtwRnwBXMsKuQUTHZGDAkEA1lHU2OYGYP7SMu+fxzwFRNx0DrXWqIe0XFQX6EDRbk5H/eKgVt1tER8Mbin/z5utvXXiGJAj6+Eop6uqNPUq3QJAWbFZwVV0XJU8vf38i4BBOG/GKApRK7Tk5TaPQIZYeHoBmUGSLhMLvw4tynxP7tteXEh8OY6FOnU5UyphfbSDwQJBAIC0oesjsH79aMQ4DS77x3pEHdpbry6EWHb99WF/04W3sxovx/SCgyY+DBv4UuydZCgcLAxuO3RDQkP3Hn8xIG0CQQC9keRoDlA7RkwCXq76vEzLUqWiRSeHbXkaniSp7/xqzlw1IeMTtzoqMcJAulNr9W+vKKBYtE3sjXLcmO/CvWk/";
        assertEquals(keyFileContents, defaultKey.getConsumerSecret());
    }
}
