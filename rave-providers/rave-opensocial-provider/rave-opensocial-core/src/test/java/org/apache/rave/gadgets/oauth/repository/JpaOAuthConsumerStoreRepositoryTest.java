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

package org.apache.rave.gadgets.oauth.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.gadgets.oauth.model.OAuthConsumerStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Test for {@link org.apache.rave.gadgets.oauth.repository.impl.JpaOAuthConsumerStoreRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:rave-shindig-test-applicationContext.xml",
        "classpath:rave-shindig-test-dataContext.xml"})
public class JpaOAuthConsumerStoreRepositoryTest {
    private static final String GADGET_URI = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String SERVICE_NAME_GOOGLE = "Google";
    private static final String SERVICE_NAME_FOO = "Foo";


    @PersistenceContext
    private EntityManager manager;

    @Autowired
    OAuthConsumerStoreRepository repository;

    @Test
    public void testFindByUriAndServiceName() throws Exception {
        final OAuthConsumerStore store = repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_GOOGLE);
        assertNotNull("OAuthConsumerStore In test db", store);
        assertEquals("gadgetSecret", store.getConsumerSecret());
        assertEquals(OAuthConsumerStore.KeyType.HMAC_SYMMETRIC, store.getKeyType());
    }

    @Test
    public void testFindByUriAndServiceName_nullValue() throws Exception {
        final OAuthConsumerStore store = repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_FOO);
        assertNull(store);
    }
}
