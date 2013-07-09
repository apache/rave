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
import org.apache.rave.model.Group;
import org.apache.rave.portal.model.impl.GroupImpl;
import org.apache.rave.portal.repository.MongoGroupOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MongoDbGroupRepositoryTest {

    private MongoGroupOperations groupTemplate;
    private MongoDbGroupRepository repo;

    @Before
    public void setUp(){
        groupTemplate = createMock(MongoGroupOperations.class);
        repo = new MongoDbGroupRepository();
        repo.setTemplate(groupTemplate);
    }

    @Test
    public void getByTitle() {
        String title = "title";
        Group group = new GroupImpl();
        group.setId("1");
        group.setTitle(title);
        expect(groupTemplate.findOne(Query.query(Criteria.where("title").is(title)))).andReturn(group);
        replay(groupTemplate);

        Group fromRepo = repo.findByTitle(title);
        assertThat(fromRepo.getTitle(), is(equalTo(title)));
    }

    @Test
    public void get() {
        String title = "title";
        String id = "1";
        Group group = new GroupImpl();
        group.setId(id);
        group.setTitle(title);
        expect(groupTemplate.get(id)).andReturn(group);
        replay(groupTemplate);

        Group fromRepo = repo.get(id);
        assertThat(fromRepo.getId(), is(equalTo(id)));
        assertThat(fromRepo.getTitle(), is(equalTo(title)));
    }

    @Test
    public void getAll() {
        List<Group> groups = Arrays.<Group>asList(new GroupImpl(), new GroupImpl());

        expect(groupTemplate.find(new Query())).andReturn(groups);
        replay(groupTemplate);

        List<Group> result = repo.getAll();
        assertNotNull(result);
        assertThat(result.size(), is(equalTo(groups.size())));
    }

    @Test
    public void save(){
        String title = "TITLE";
        Group group = new GroupImpl();
        group.setId("1");
        group.setTitle(title);
        expect(groupTemplate.save(group)).andReturn(group);
        replay(groupTemplate);
        Group returned = repo.save(group);
        verify(groupTemplate);
        assertThat(returned, is(sameInstance(group)));
    }


    @Test
    public void delete(){
        String id ="id";
        Group group = new GroupImpl();
        group.setId(id);
        group.setTitle("title");
        groupTemplate.remove(Query.query(Criteria.where("_id").is(id)));
        expectLastCall();
        replay(groupTemplate);

        repo.delete(group);
        verify(groupTemplate);

    }
}
