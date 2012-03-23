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

import org.apache.rave.gadgets.oauth.model.OAuthConsumerStore;
import org.apache.rave.gadgets.oauth.repository.OAuthConsumerStoreRepository;
import org.apache.rave.gadgets.oauth.service.impl.DefaultOAuthConsumerStoreService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * Test class for {@link DefaultOAuthConsumerStoreService}
 */
public class DefaultOAuthConsumerStoreServiceTest {
    private static final String GADGET_URI = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String SERVICE_NAME_GOOGLE = "Google";
    private static final String SERVICE_NAME_FOO = "FOO";

    private OAuthConsumerStoreService service;
    private OAuthConsumerStoreRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = createNiceMock(OAuthConsumerStoreRepository.class);
        service = new DefaultOAuthConsumerStoreService(repository);
    }

    @Test
    public void testFindByUriAndServiceName() throws Exception {
        OAuthConsumerStore dbConsumerStore = getOAuthConsumerStore();
        expect(repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_GOOGLE)).andReturn(dbConsumerStore);
        replay(repository);

        OAuthConsumerStore consumerStore = service.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_GOOGLE);
        assertNotNull("Found consumerStore in repo", consumerStore);
        assertEquals("gadgetConsumer", consumerStore.getConsumerKey());
        assertEquals(OAuthConsumerStore.KeyType.HMAC_SYMMETRIC, consumerStore.getKeyType());
    }

    @Test
    public void testFindByUriAndServiceName_nullValue() throws Exception {
        expect(repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_FOO)).andReturn(null);
        OAuthConsumerStore consumerStore = service.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_FOO);
        assertNull(consumerStore);
    }

    OAuthConsumerStore getOAuthConsumerStore() {
        OAuthConsumerStore consumerStore = new OAuthConsumerStore();
        consumerStore.setCallbackUrl("http://oauth.gmodules.com/gadgets/oauthcallback");
        consumerStore.setConsumerKey("gadgetConsumer");
        consumerStore.setConsumerSecret("gadgetSecret");
        consumerStore.setGadgetUri(GADGET_URI);
        consumerStore.setServiceName(SERVICE_NAME_GOOGLE);
        consumerStore.setKeyName("keyName");
        consumerStore.setKeyType(OAuthConsumerStore.KeyType.HMAC_SYMMETRIC);
        return consumerStore;
    }

}
