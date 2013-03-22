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

import com.google.common.collect.Lists;
import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.MongoDbAuthority;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.AUTHORITY_COLLECTION;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Test for MongoDb Authority Repository class
 */
public class MongoDbAuthorityRepositoryTest {

    private MongoOperations template;
    private MongoDbAuthorityRepository repo;
    private HydratingConverterFactory converter;
    public static final Class<MongoDbAuthority> CLASS = MongoDbAuthority.class;

    @Before
    public void setup() {
        template = createMock(MongoOperations.class);
        converter = createNiceMock(HydratingConverterFactory.class);
        repo = new MongoDbAuthorityRepository();
        repo.setTemplate(template);
        repo.setConverter(converter);

    }

    @Test
    public void save_validNew(){
        Authority returnedAuth;
        Authority savedAuth = new MongoDbAuthority();
        savedAuth.setAuthority("test");

        template.save(isA(Authority.class), eq(AUTHORITY_COLLECTION));
        expectLastCall();
        expect(template.findOne(query(where("authority").is("test")), CLASS, AUTHORITY_COLLECTION)).andReturn(null);
        replay(template);
        expect(converter.convert(savedAuth, Authority.class)).andReturn(savedAuth);
        replay(converter);

        returnedAuth = repo.save(savedAuth);
        assertNotNull(template);
        assertThat(returnedAuth, is(equalTo(savedAuth)));
        verify(template);
    }

    @Test
    public void save_valid(){
        Authority savedAuth = new MongoDbAuthority();
        Authority returnedAuth;
        savedAuth.setAuthority("test");

        expect(template.findOne(query(where("authority").is("test")), CLASS, AUTHORITY_COLLECTION)).andReturn((MongoDbAuthority)savedAuth);
        template.save(isA(Authority.class), eq(AUTHORITY_COLLECTION));
        expectLastCall();

        replay(template);

        returnedAuth = repo.save(savedAuth);
        assertThat(savedAuth.isDefaultForNewUser(), is(equalTo(returnedAuth.isDefaultForNewUser())));
        assertNotNull(template);
        assertThat(savedAuth, is(sameInstance(returnedAuth)));
        verify(template);

    }

    @Test
    public void delete(){
        Authority deleted = new MongoDbAuthority();
        deleted.setAuthority("deleted");

        template.remove(isA(Authority.class), eq(AUTHORITY_COLLECTION));
        expectLastCall();
        expect(template.findOne(query(where("authority").is("deleted")), CLASS, AUTHORITY_COLLECTION)).andReturn((MongoDbAuthority)deleted);
        replay(template);

        repo.delete(deleted);

        verify(template);
    }

    @Test
    public void getByAuthority(){
        Authority authority = new MongoDbAuthority();
        authority.setAuthority("test");
        Authority result;
        template.save(authority, AUTHORITY_COLLECTION);

        expect(template.findOne(query(where("authority").is("test")), CLASS, AUTHORITY_COLLECTION)).andReturn((MongoDbAuthority)authority);
        replay(template);

        result = repo.getByAuthority("test");
        assertThat(result, is(sameInstance(authority)));

    }


    @Test
    public void getAll(){
        List<Authority> allAuth = Lists.newLinkedList();
        Authority authority = new MongoDbAuthority();
        authority.setAuthority("test");
        allAuth.add(authority);

        expect(CollectionUtils.<Authority>toBaseTypedList(template.findAll(CLASS, AUTHORITY_COLLECTION))).andReturn(allAuth);
        replay(template);

        List<Authority> result;

        result = repo.getAll();
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(sameInstance(authority)));

    }

    @Test
    public void getAllDefault(){
        List<Authority> allDefaultAuth = Lists.newLinkedList();
        Authority authority = new MongoDbAuthority();
        authority.setAuthority("test");
        authority.setDefaultForNewUser(true);
        allDefaultAuth.add(authority);

        expect(CollectionUtils.<Authority>toBaseTypedList(template.find(query(where("defaultForNewUser").is(true)), CLASS, AUTHORITY_COLLECTION))).andReturn(allDefaultAuth);
        replay(template);

        List<Authority> result;

        result = repo.getAllDefault();
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(sameInstance(authority)));
        assertThat(result.get(0).getAuthority(), is(equalTo("test")));

    }


    @Test
    public void getAllDefault_false(){
        List<Authority> allDefaultAuth = Lists.newLinkedList();
        Authority authority = new MongoDbAuthority();
        authority.setDefaultForNewUser(false);
        allDefaultAuth.add(authority);

        expect(CollectionUtils.<Authority>toBaseTypedList(template.find(query(where("defaultForNewUser").is(true)), CLASS, AUTHORITY_COLLECTION))).andReturn(null);
        replay(template);

        List<Authority> result;

        result = repo.getAllDefault();
        assertNull(result);
    }


    @Test
    public void getTemplate(){
        MongoOperations temp1;

        temp1 = repo.getTemplate();
        assertThat(temp1, is(sameInstance(template)));
    }

    @Test
    public void setTemplate(){
        MongoOperations temp1 = createNiceMock(MongoOperations.class);

        repo.setTemplate(temp1);
        assertThat(repo.getTemplate(), is(sameInstance(temp1)));

    }

    @Test
    public void getConverter(){
        HydratingConverterFactory converter1;

        converter1 = repo.getConverter();
        assertThat(converter1, is(sameInstance(converter)));
    }

    @Test
    public void setConverter(){
        HydratingConverterFactory converter1 = createNiceMock(HydratingConverterFactory.class);

        repo.setConverter(converter1);
        assertThat(repo.getConverter(), is(sameInstance(converter1)));

    }



}
