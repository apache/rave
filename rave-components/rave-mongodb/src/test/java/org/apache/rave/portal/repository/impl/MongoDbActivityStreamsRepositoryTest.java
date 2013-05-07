/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.repository.impl;


import com.google.common.collect.Lists;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.MongoDbActivityStreamsEntry;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.ACTIVITIES;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class MongoDbActivityStreamsRepositoryTest {
    public static final String ID = "1";
    public static final Class<MongoDbActivityStreamsEntry> ENTITY_CLASS = MongoDbActivityStreamsEntry.class;
    private MongoOperations template;
    private HydratingConverterFactory factory;
    private ActivityStreamsRepository repository;

    @Before
    public void setup() {
        factory = createMock(HydratingConverterFactory.class);
        template = createMock(MongoOperations.class);
        repository = new MongoDbActivityStreamsRepository(template, factory);
    }

    @Test
    public void getAll() {
        List<MongoDbActivityStreamsEntry> result = Lists.newArrayList();
        expect(template.findAll(eq(ENTITY_CLASS), eq(ACTIVITIES))).andReturn(result);
        replay(template);

        List<ActivityStreamsEntry> entries = repository.getAll();
        assertThat((Object)entries, is(sameInstance((Object)result)));
    }

    @Test
    public void getByUserId() {
        List<MongoDbActivityStreamsEntry> result = Lists.newArrayList();
        expect(template.find(Query.query(Criteria.where("actor._id").is(ID)), ENTITY_CLASS, ACTIVITIES)).andReturn(result);
        replay(template);

        List<ActivityStreamsEntry> entries = repository.getByUserId(ID);
        assertThat((Object)entries, is(sameInstance((Object)result)));
    }

    @Test
    public void deleteByUserId() {
        template.remove(Query.query(Criteria.where("_id").is(ID)), ACTIVITIES);
        expectLastCall();
        replay(template);

        repository.deleteById(ID);
        verify(template);
    }

    @Test
    public void delete() {
        ActivityStreamsEntryImpl entry = new ActivityStreamsEntryImpl();
        entry.setId(ID);
        template.remove(Query.query(Criteria.where("_id").is(ID)), ACTIVITIES);
        expectLastCall();
        replay(template);
        repository.delete(entry);
        verify(template);
    }

    @Test
    public void delete_null() {
        replay(template);
        repository.delete(null);
        verify(template);
    }

    @Test
    public void getById() {
        MongoDbActivityStreamsEntry entry = new MongoDbActivityStreamsEntry();
        entry.setId(ID);
        expect(template.findById(ID, ENTITY_CLASS, ACTIVITIES)).andReturn(entry);
        replay(template);

        ActivityStreamsEntry result = repository.get(ID);
        assertThat(result, is(sameInstance((ActivityStreamsEntry)entry)));
    }

    @Test
    public void save() {
        ActivityStreamsEntry original = new ActivityStreamsEntryImpl();
        ActivityStreamsEntry converted = new ActivityStreamsEntryImpl();
        expect(factory.convert(original, ActivityStreamsEntry.class)).andReturn(converted);
        template.save(converted, ACTIVITIES);
        expectLastCall();
        replay(factory, template);

        ActivityStreamsEntry result = repository.save(original);
        assertThat(result, is(sameInstance(converted)));
        verify(factory, template);
    }
}
