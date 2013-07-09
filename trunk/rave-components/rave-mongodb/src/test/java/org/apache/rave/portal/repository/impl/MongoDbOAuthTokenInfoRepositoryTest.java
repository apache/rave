/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import static org.apache.rave.portal.repository.util.CollectionNames.OAUTH_TOKEN_COLLECTION;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Test for MongoDb OAuthTokenInfo Repository class.
 */
public class MongoDbOAuthTokenInfoRepositoryTest {

    private MongoOperations template;
    private MongoDbOAuthTokenInfoRepository repo;
    private static final Class<OAuthTokenInfo> CLASS1 = OAuthTokenInfo.class;
    public static final Class<OAuthTokenInfoImpl> CLASS2 = OAuthTokenInfoImpl.class;

    @Before
    public void setUp(){
        template = createMock(MongoOperations.class);
        repo = new MongoDbOAuthTokenInfoRepository();
        repo.setTemplate(template);
    }

    @Test
    public void findOAuthTokenInfo(){
        OAuthTokenInfo info = new OAuthTokenInfoImpl();
        String userId = "1234";
        String appUrl = "www.test.com";
        String moduleId = "2222";
        String tokenName = "token";
        String serviceName = "service";
        info.setUserId(userId);
        info.setAppUrl(appUrl);
        info.setModuleId(moduleId);
        info.setTokenName(tokenName);
        info.setServiceName(serviceName);
        expect(template.findOne(query(where("userId").is(userId)
                .andOperator(where("appUrl").is(appUrl))
                .andOperator(where("moduleId").is(moduleId))
                .andOperator(where("tokenName").is(tokenName))
                .andOperator(where("serviceName").is(serviceName))
        ), CLASS1, OAUTH_TOKEN_COLLECTION)).andReturn(info);
        replay(template);

    }

    @Test
    public void save_null(){
        OAuthTokenInfo info = new OAuthTokenInfoImpl();
        OAuthTokenInfo result;

        template.save(isA(OAuthTokenInfo.class), eq(OAUTH_TOKEN_COLLECTION));
        expectLastCall();
        replay(template);

        result = repo.save(info);
        verify(template);

    }

    @Test
    public void save(){
        OAuthTokenInfo item = new OAuthTokenInfoImpl("appUrl", "serviceName",
                "tokenName", "accessToken", "sessionHandle",
                "tokenSecret", "userId", 1111L);
        OAuthTokenInfo result;
        item.setId("1234L");

        template.save(isA(OAuthTokenInfo.class), eq(OAUTH_TOKEN_COLLECTION));
        expectLastCall();

        result = repo.save(item);
        assertNotNull(result.getId());
        assertThat(result.getId(), is(equalTo("1234L")));

    }

    @Test
    public void get(){
        OAuthTokenInfo result;
        OAuthTokenInfo item = new OAuthTokenInfoImpl("appUrl", "serviceName",
                "tokenName", "accessToken", "sessionHandle",
                "tokenSecret", "userId", 1111L);
        item.setId("1234L");

        expect(template.findById("1234L", CLASS2, OAUTH_TOKEN_COLLECTION)).andReturn((OAuthTokenInfoImpl)item);
        replay(template);

        result = repo.get("1234L");
        assertThat(result.getId(), is(equalTo("1234L")));

    }

    @Test
    public void delete(){
        OAuthTokenInfo item = new OAuthTokenInfoImpl("appUrl", "serviceName",
                "tokenName", "accessToken", "sessionHandle",
                "tokenSecret", "userId", 1111L);
        item.setId("1234L");

        template.remove(item);
        expectLastCall();
        expect(template.findById("1234L", CLASS2, OAUTH_TOKEN_COLLECTION)).andReturn((OAuthTokenInfoImpl)item);
        replay(template);

        repo.delete(item);
        verify(template);

    }


}
