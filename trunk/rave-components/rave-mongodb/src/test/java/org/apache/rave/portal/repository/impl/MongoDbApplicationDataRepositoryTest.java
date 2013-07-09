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

import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created with IntelliJ IDEA.
 * User: dsullivan
 * Date: 11/29/12
 * Time: 7:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class MongoDbApplicationDataRepositoryTest {
    private MongoDbApplicationDataRepository applicationDataRepository;
    private MongoOperations template;

    @Before
    public void setup() {
        applicationDataRepository = new MongoDbApplicationDataRepository();
        template = createMock(MongoOperations.class);
        applicationDataRepository.setTemplate(template);
    }

    @Test
    public void getApplicationData_Valid() {
        List<String> userIds = Arrays.asList("123");
        String appId = "blah";
        List<ApplicationDataImpl> found = new ArrayList<ApplicationDataImpl>();

        expect(template.find(query(where("appUrl").is(appId).andOperator(where("userId").in(userIds))), applicationDataRepository.CLASS, CollectionNames.APP_DATA_COLLECTION)).andReturn(found);
        replay(template);
        List<ApplicationData> appData = applicationDataRepository.getApplicationData(userIds, appId);
    }

    @Test
    public void getApplicationData_Single_Valid_(){
        String personId = "personid";
        String appId = "appId";
        ApplicationDataImpl found = new ApplicationDataImpl();
        expect(template.findOne(query(where("appUrl").is(appId).andOperator(where("userId").is(personId))),applicationDataRepository.CLASS, CollectionNames.APP_DATA_COLLECTION )).andReturn(found);
        replay(template);

        assertThat((ApplicationDataImpl)applicationDataRepository.getApplicationData(personId, appId), is(sameInstance(found)));
    }

    @Test
    public void getType_Valid(){
        assertThat((Class<ApplicationDataImpl>)applicationDataRepository.getType(), is(equalTo(ApplicationDataImpl.class)));
    }

    @Test
    public void get_Valid(){
        String id = "123";
        ApplicationDataImpl found = new ApplicationDataImpl();
        expect(template.findById(id, applicationDataRepository.CLASS, CollectionNames.APP_DATA_COLLECTION)).andReturn(found);
        replay(template);

        assertThat(found, is(sameInstance(applicationDataRepository.get(id))));
    }

    @Test
    public void save_Valid(){
        ApplicationData item = new ApplicationDataImpl();

        template.save(item, CollectionNames.APP_DATA_COLLECTION);
        expectLastCall();
        replay(template);

        ApplicationData saved = applicationDataRepository.save(item);
        verify(template);
    }

    @Test
    public void save_Id_Set(){
         ApplicationData item = new ApplicationDataImpl();
        item.setId("123");

        template.save(item, CollectionNames.APP_DATA_COLLECTION);
        expectLastCall();
        replay(template);

        ApplicationData saved = applicationDataRepository.save(item);
        assertThat(saved, is(sameInstance(item)));
    }

    @Test
    public void delete_Valid(){
        ApplicationData item = new ApplicationDataImpl();

        template.remove(item, CollectionNames.APP_DATA_COLLECTION);
        expectLastCall();
        replay(template);

        applicationDataRepository.delete(item);
        verify(template);
    }
}

