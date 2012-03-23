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

import org.apache.rave.gadgets.oauth.model.OAuthTokenInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link org.apache.rave.gadgets.oauth.repository.impl.JpaOAuthTokenInfoRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:rave-shindig-test-applicationContext.xml",
        "classpath:rave-shindig-test-dataContext.xml"})
public class JpaOAuthTokenInfoRepositoryTest {

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
    public void testFindOAuthTokenInfo() throws Exception {
        OAuthTokenInfo tokenInfo = repository.findOAuthTokenInfo(VALID_USER,
                APP_URL, OAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNotNull(tokenInfo);
        assertEquals("accessToken", tokenInfo.getAccessToken());
    }

    @Test
    public void testFindOAuthTokenInfo_nullValue() throws Exception {
        OAuthTokenInfo tokenInfo = repository.findOAuthTokenInfo(INVALID_USER,
                APP_URL, OAuthTokenInfo.MODULE_ID, TOKEN_NAME, SERVICE_NAME);
        assertNull(tokenInfo);
    }
}
