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

import org.apache.rave.portal.model.JpaOAuthTokenInfo;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test class for {@link org.apache.rave.portal.repository.impl.JpaOAuthTokenInfoRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml",
        "classpath:test-dataContext.xml"})
public class JpaOAuthTokenInfoRepositoryTest {

    private static final Long VALID_ID = 1L;
    private static final String INVALID_USER = "Invalid user";
    private static final String VALID_USER = "john.doe";
    private static final String APP_URL = "http://localhost:8080/samplecontainer/examples/oauth.xml";
    private static final String TOKEN_NAME = "tokenName";
    private static final String SERVICE_NAME = "serviceName";

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    OAuthTokenInfoRepository repository;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaOAuthTokenInfo.class);
    }

    @Test
    public void get() {
        JpaOAuthTokenInfo oAuthTokenInfo = (JpaOAuthTokenInfo) repository.get(VALID_ID.toString());
        assertThat(oAuthTokenInfo.getEntityId(), is(VALID_ID));
        assertThat(oAuthTokenInfo.getAppUrl(), is(APP_URL));
        assertThat(oAuthTokenInfo.getTokenName(), is(TOKEN_NAME));
        assertThat(oAuthTokenInfo.getServiceName(), is(SERVICE_NAME));
    }

    @Test
    public void testFindOAuthTokenInfo() throws Exception {
        OAuthTokenInfo tokenInfo = repository.findOAuthTokenInfo(VALID_USER,
                APP_URL, JpaOAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNotNull(tokenInfo);
        assertEquals("accessToken", tokenInfo.getAccessToken());
    }

    @Test
    public void testFindOAuthTokenInfo_nullValue() throws Exception {
        OAuthTokenInfo tokenInfo = repository.findOAuthTokenInfo(INVALID_USER,
                APP_URL, JpaOAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNull(tokenInfo);
    }

    @Test
    @Rollback
    public void testDeleteOAuthTokenInfo() throws Exception {
        OAuthTokenInfo tokenInfo = repository.findOAuthTokenInfo(VALID_USER,
                APP_URL, JpaOAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNotNull(tokenInfo);
        repository.delete(tokenInfo);
        tokenInfo = repository.findOAuthTokenInfo(VALID_USER,
                APP_URL, JpaOAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNull(tokenInfo);
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        final String NEW_URL = "testurl";
        final String NEW_SERVICE_NAME = "testservice";
        JpaOAuthTokenInfo oAuthTokenInfo = new JpaOAuthTokenInfo();
        oAuthTokenInfo.setAppUrl(NEW_URL);
        oAuthTokenInfo.setServiceName(NEW_SERVICE_NAME);

        assertThat(oAuthTokenInfo.getEntityId(), is(nullValue()));
        repository.save(oAuthTokenInfo);
        Long newId = oAuthTokenInfo.getEntityId();
        assertThat(newId > 0, is(true));
        JpaOAuthTokenInfo newOAuthTokenInfo = (JpaOAuthTokenInfo) repository.get(newId.toString());
        assertThat(newOAuthTokenInfo.getServiceName(), is(NEW_SERVICE_NAME));
        assertThat(newOAuthTokenInfo.getAppUrl(), is(NEW_URL));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        final String UPDATED_SERVICE_NAME = "updated service name";
        OAuthTokenInfo oAuthTokenInfo = repository.get(VALID_ID.toString());
        assertThat(oAuthTokenInfo.getServiceName(), is(not(UPDATED_SERVICE_NAME)));
        oAuthTokenInfo.setServiceName(UPDATED_SERVICE_NAME);
        repository.save(oAuthTokenInfo);
        assertThat(repository.get(VALID_ID.toString()).getServiceName(), is(UPDATED_SERVICE_NAME));
    }
}
