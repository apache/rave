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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.portal.model.JpaOAuthConsumerStore;
import org.apache.rave.portal.repository.OAuthConsumerStoreRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link org.apache.rave.portal.repository.impl.JpaOAuthConsumerStoreRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml",
        "classpath:test-dataContext.xml"})
public class JpaOAuthConsumerStoreRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final String GADGET_URI = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String SERVICE_NAME_GOOGLE = "Google";
    private static final String SERVICE_NAME_FOO = "Foo";


    @PersistenceContext
    private EntityManager manager;

    @Autowired
    OAuthConsumerStoreRepository repository;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaOAuthConsumerStore.class);
    }

    @Test
    public void get() {
        JpaOAuthConsumerStore oAuthConsumerStore = (JpaOAuthConsumerStore) repository.get(VALID_ID.toString());
        assertThat(oAuthConsumerStore.getEntityId(), is(VALID_ID));
        assertThat(oAuthConsumerStore.getGadgetUri(), is(GADGET_URI));
        assertThat(oAuthConsumerStore.getServiceName(), is(SERVICE_NAME_GOOGLE));
    }

    @Test
    public void testFindByUriAndServiceName() throws Exception {
        final OAuthConsumerStore store = repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_GOOGLE);
        assertNotNull("OAuthConsumerStore In test db", store);
        assertEquals("gadgetSecret", store.getConsumerSecret());
        assertEquals(JpaOAuthConsumerStore.KeyType.HMAC_SYMMETRIC, store.getKeyType());
    }

    @Test
    public void testFindByUriAndServiceName_nullValue() throws Exception {
        final OAuthConsumerStore store = repository.findByUriAndServiceName(GADGET_URI, SERVICE_NAME_FOO);
        assertNull(store);
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        final String NEW_URL = "testurl";
        final String NEW_SERVICE_NAME = "testservice";
        JpaOAuthConsumerStore oAuthConsumerStore = new JpaOAuthConsumerStore();
        oAuthConsumerStore.setCallbackUrl(NEW_URL);
        oAuthConsumerStore.setServiceName(NEW_SERVICE_NAME);

        assertThat(oAuthConsumerStore.getEntityId(), is(nullValue()));
        repository.save(oAuthConsumerStore);
        Long newId = oAuthConsumerStore.getEntityId();
        assertThat(newId > 0, is(true));
        JpaOAuthConsumerStore newOAuthConsumerStore = (JpaOAuthConsumerStore) repository.get(newId.toString());
        assertThat(newOAuthConsumerStore.getServiceName(), is(NEW_SERVICE_NAME));
        assertThat(newOAuthConsumerStore.getCallbackUrl(), is(NEW_URL));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        final String UPDATED_SERVICE_NAME = "updated service name";
        JpaOAuthConsumerStore oAuthConsumerStore = (JpaOAuthConsumerStore) repository.get(VALID_ID.toString());
        assertThat(oAuthConsumerStore.getServiceName(), is(not(UPDATED_SERVICE_NAME)));
        oAuthConsumerStore.setServiceName(UPDATED_SERVICE_NAME);
        repository.save(oAuthConsumerStore);
        assertThat(repository.get(VALID_ID.toString()).getServiceName(), is(UPDATED_SERVICE_NAME));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete() {
        OAuthConsumerStore oAuthConsumerStore = repository.get(VALID_ID.toString());
        assertThat(oAuthConsumerStore, is(notNullValue()));
        repository.delete(oAuthConsumerStore);
        oAuthConsumerStore = repository.get(VALID_ID.toString());
        assertThat(oAuthConsumerStore, is(nullValue()));
    }
}
