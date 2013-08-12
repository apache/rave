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

import org.apache.rave.portal.model.MongoDbPortalPreference;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.apache.rave.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.PREFERENCE_COLLECTION;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/6/12
 * Time: 8:01 AM
 */
public class MongoDbPortalPreferenceRepositoryTest {
    private MongoDbPortalPreferenceRepository preferenceRepository;
    private MongoOperations template;
    private HydratingConverterFactory converter;

    @Before
    public void setup(){
        preferenceRepository = new MongoDbPortalPreferenceRepository();
        template = createMock(MongoOperations.class);
        converter = createMock(HydratingConverterFactory.class);
        preferenceRepository.setConverter(converter);
        preferenceRepository.setTemplate(template);
    }

    @Test
    public void getAll_Valid(){
        List<PortalPreferenceImpl> found = new ArrayList<PortalPreferenceImpl>();
        expect(template.findAll(preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn(found);
        replay(template);

        assertThat(CollectionUtils.<PortalPreference>toBaseTypedList(found), is(sameInstance(preferenceRepository.getAll())));
    }

    @Test
    public void getLimitedList_Valid(){
        int offset = 234;
        int pageSize = 123;
        List<PortalPreferenceImpl> found = new ArrayList<PortalPreferenceImpl>();
        Query q = new Query().skip(offset).limit(pageSize);
        expect(template.find(q, preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn(found);
        replay(template);
        assertThat(CollectionUtils.<PortalPreference>toBaseTypedList(found), is(sameInstance(preferenceRepository.getLimitedList(offset, pageSize))));
    }

    @Test
    public void getCountAll(){
        long doubleOseven = 007;
        expect(template.count(new Query(), preferenceRepository.CLASS)).andReturn(doubleOseven);
        replay(template);
        assertThat((int)doubleOseven, is(sameInstance(preferenceRepository.getCountAll())));
    }

    @Test
    public void getByKey_Valid(){
        String key = "key";
        PortalPreferenceImpl found = new PortalPreferenceImpl();
        expect(template.findOne(query(where("key").is(key)), preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn(found);
        replay(template);

        assertThat(found, is(sameInstance(preferenceRepository.getByKey(key))));
    }

    @Test
    public void getType(){
        assertThat((Class<PortalPreferenceImpl>)(preferenceRepository.getType()), is(equalTo(PortalPreferenceImpl.class)));
    }

    @Test
    public void get_Valid(){
        String id = "123";
        PortalPreference found = new PortalPreferenceImpl();
        expect(template.findById(id, preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn((PortalPreferenceImpl)found);
        replay(template);

        assertThat(found, is(sameInstance(preferenceRepository.get(id))));
    }

    @Test
    public void save_Valid(){
        PortalPreference item = new PortalPreferenceImpl();
        item.setKey("123");
        PortalPreference fromDb = new MongoDbPortalPreference();
        ((MongoDbPortalPreference)fromDb).setId("123");
        PortalPreference converted = new MongoDbPortalPreference();
        expect(converter.convert(item, PortalPreference.class)).andReturn(converted);
        expect(template.findOne(query(where("key").is("123")), preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn((MongoDbPortalPreference)fromDb);
        template.save(converted, CollectionNames.PREFERENCE_COLLECTION);
        expectLastCall();
        converter.hydrate(converted, PortalPreference.class);
        expectLastCall();
        replay(template, converter);

        PortalPreference result = preferenceRepository.save(item);
        assertNotNull(((MongoDbPortalPreference) converted).getId());
        assertThat(result, is(sameInstance(converted)));
    }

    @Test
    public void save_Null(){
        PortalPreference item = new PortalPreferenceImpl();
        item.setKey("123");
        PortalPreference converted = new MongoDbPortalPreference();
        expect(template.findOne(query(where("key").is("123")), preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn(null);
        expect(converter.convert(item, PortalPreference.class)).andReturn(converted);
        template.save(converted, PREFERENCE_COLLECTION);
        expectLastCall();
        converter.hydrate(converted, PortalPreference.class);
        expectLastCall();
        replay(template, converter);

        PortalPreference result = preferenceRepository.save(item);
        assertThat(result, is(sameInstance(converted)));
    }

    @Test
    public void delete_Valid(){
        PortalPreference item = new PortalPreferenceImpl();
        item.setKey("123");
        PortalPreference found = new PortalPreferenceImpl();
        expect(template.findOne(query(where("key").is("123")), preferenceRepository.CLASS, CollectionNames.PREFERENCE_COLLECTION)).andReturn((PortalPreferenceImpl)found);
        template.remove(found, CollectionNames.PREFERENCE_COLLECTION);
        expectLastCall();
        replay(template);

        preferenceRepository.delete(item);

        verify(template);
    }
}


