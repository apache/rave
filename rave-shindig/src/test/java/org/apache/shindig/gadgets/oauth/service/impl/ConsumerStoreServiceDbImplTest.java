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
import org.apache.shindig.gadgets.oauth.jpa.OAuthConsumerStoreDb;
import org.apache.shindig.gadgets.oauth.service.ConsumerStoreService;
import org.apache.shindig.social.opensocial.jpa.openjpa.OpenJPADbModule;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Test for {@link ConsumerStoreServiceDbImpl}
 */
public class ConsumerStoreServiceDbImplTest {
    private static final String GADGET_URI = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String SERVICE_NAME = "testService";
    private static final String CONSUMER_SECRET = "gadgetSecret";
    private static final String NEW_SERVICE_NAME = "new_service_name";

    private ConsumerStoreService service;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new OpenJPADbModule());
        service = injector.getInstance(ConsumerStoreServiceDbImpl.class);

    }

    @Test
    public void testCrudOperations() throws Exception {
        assertNull("Empty DB", service.findOAuthConsumerStore(GADGET_URI, SERVICE_NAME));
        
        OAuthConsumerStoreDb consumerStore = new OAuthConsumerStoreDb();
        consumerStore.setGadgetUri(GADGET_URI);
        consumerStore.setConsumerKey("gadgetConsumer");
        consumerStore.setConsumerSecret(CONSUMER_SECRET);
        consumerStore.setKeyType(OAuthConsumerStoreDb.KeyType.HMAC_SYMMETRIC);
        consumerStore.setServiceName(SERVICE_NAME);

        service.save(consumerStore);

        OAuthConsumerStoreDb consumerStoreDb = service.findOAuthConsumerStore(
                GADGET_URI, SERVICE_NAME);
        assertEquals("Saved 1 item", consumerStore, consumerStoreDb);
        
        long savedId = consumerStoreDb.getObjectId();
        consumerStoreDb.setServiceName(NEW_SERVICE_NAME);

        service.save(consumerStoreDb);

        assertNull("updated service name", service.findOAuthConsumerStore(
                GADGET_URI, SERVICE_NAME));

        assertEquals("No new id", savedId, service.findOAuthConsumerStore(
                GADGET_URI, NEW_SERVICE_NAME).getObjectId());

        service.delete(consumerStoreDb);

        assertNull("updated service name", service.findOAuthConsumerStore(
                GADGET_URI, NEW_SERVICE_NAME));

    }

}
