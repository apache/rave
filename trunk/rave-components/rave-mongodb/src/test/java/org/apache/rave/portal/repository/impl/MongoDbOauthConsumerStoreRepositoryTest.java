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

import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.portal.model.impl.OAuthConsumerStoreImpl;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/5/12
 * Time: 9:21 AM
 */
public class MongoDbOauthConsumerStoreRepositoryTest {
    private MongoDbOauthConsumerStoreRepository oauthConsumerStoreRepository;
    private MongoOperations template;

    @Before
    public void setup() {
        oauthConsumerStoreRepository = new MongoDbOauthConsumerStoreRepository();
        template = createMock(MongoOperations.class);
        oauthConsumerStoreRepository.setTemplate(template);
    }

    @Test
    public void findUriAndServiceNameValid() {
        String gadgetUri = "gadgetUri";
        String serviceName = "serviceName";
        OAuthConsumerStoreImpl found = new OAuthConsumerStoreImpl();
        expect(template.findOne(query(where("gadgetUri").is(gadgetUri).andOperator(where("serviceName").is(serviceName))), MongoDbOauthConsumerStoreRepository.CLASS, CollectionNames.OAUTH_CONSUMER_COLLECTION)).andReturn(found);
        replay(template);

        OAuthConsumerStore returned = oauthConsumerStoreRepository.findByUriAndServiceName(gadgetUri, serviceName);

        assertThat((OAuthConsumerStoreImpl) returned, is(sameInstance(found)));
    }

    @Test
    public void getType_Valid() {
        assertThat((Class<OAuthConsumerStoreImpl>) oauthConsumerStoreRepository.getType(), is(equalTo(OAuthConsumerStoreImpl.class)));
    }

    @Test
    public void get_Valid() {
        String id = "123";
        OAuthConsumerStoreImpl found = new OAuthConsumerStoreImpl();
        expect(template.findById(id, oauthConsumerStoreRepository.CLASS, CollectionNames.OAUTH_CONSUMER_COLLECTION)).andReturn(found);
        replay(template);

        assertThat(found, is(sameInstance(oauthConsumerStoreRepository.get(id))));
    }

    @Test
    public void save_Valid(){
        OAuthConsumerStore item = new OAuthConsumerStoreImpl();
        template.save(item, CollectionNames.OAUTH_CONSUMER_COLLECTION);
        expectLastCall();
        replay(template);

        OAuthConsumerStore returned = oauthConsumerStoreRepository.save(item);

        verify(template);
    }

    @Test
    public void save_Null_Id(){
        OAuthConsumerStore item = new OAuthConsumerStoreImpl();
        item.setId("1232");
        template.save(item, CollectionNames.OAUTH_CONSUMER_COLLECTION);
        expectLastCall();
        replay(template);

        OAuthConsumerStore returned = oauthConsumerStoreRepository.save(item);

        assertThat(item, is(sameInstance(returned)));
    }

    @Test
    public void delete_Valid() {
        OAuthConsumerStore item = new OAuthConsumerStoreImpl();
        item.setId("123");

        expect(template.findById(item.getId(), oauthConsumerStoreRepository.CLASS, CollectionNames.OAUTH_CONSUMER_COLLECTION)).andReturn((OAuthConsumerStoreImpl)item);
        template.remove(item);
        expectLastCall();
        replay(template);

        oauthConsumerStoreRepository.delete(item);
        verify(template);
    }
}
