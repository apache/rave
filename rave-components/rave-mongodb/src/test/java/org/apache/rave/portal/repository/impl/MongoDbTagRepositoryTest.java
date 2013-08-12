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
import org.apache.rave.model.Tag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.repository.MongoTagOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test for MongoDb Tag Repository class
 */
public class MongoDbTagRepositoryTest {

    private MongoTagOperations tagTemplate;
    private MongoDbTagRepository repo;

    @Before
    public void setUp(){
        tagTemplate = createMock(MongoTagOperations.class);
        repo = new MongoDbTagRepository();
        repo.setWidgetTemplate(tagTemplate);

    }

    @Test
    public void getAll(){
        List<Tag> tags = Arrays.<Tag>asList(new TagImpl(), new TagImpl());

        expect(tagTemplate.find(new Query())).andReturn(tags);
        replay(tagTemplate);

        List<Tag> result = repo.getAll();
        assertNotNull(result);
        assertThat(result.size(), is(equalTo(tags.size())));

    }

    @Test
    public void getLimitedList(){
        List<Tag> tags = Arrays.<Tag>asList(new TagImpl(), new TagImpl());

        expect(tagTemplate.find(new Query())).andReturn(tags);
        replay(tagTemplate);

        List<Tag> result = repo.getAll();
        assertNotNull(result);
        assertThat(result.size(), is(equalTo(tags.size())));
    }

    @Test
    public void countAll(){

        expect(tagTemplate.count(new Query())).andReturn(2L);
        replay(tagTemplate);

        int result = repo.getCountAll();
        assertThat(result, is(equalTo(2)));

    }

    @Test
    public void getAll_null(){

        expect(tagTemplate.find(new Query())).andReturn(Lists.<Tag>newArrayList());
        replay(tagTemplate);

        List<Tag> result = repo.getAll();
        assertThat(result.size(), is(equalTo(0)));

    }

    @Test
    public void getByKeyword() {
        String keyword = "key";
        Tag t = new TagImpl("1", keyword);
        expect(tagTemplate.findOne(Query.query(Criteria.where("keyword").is(keyword)))).andReturn(t);
        replay(tagTemplate);

        Tag fromRepo = repo.getByKeyword(keyword);
        assertThat(fromRepo.getKeyword(), is(equalTo(keyword)));
    }

    @Test
    public void get() {
        String keyword = "key";
        String id = "1";
        Tag t = new TagImpl(id, keyword);
        expect(tagTemplate.get(id)).andReturn(t);
        replay(tagTemplate);

        Tag fromRepo = repo.get(id);
        assertThat(fromRepo.getId(), is(equalTo(id)));
        assertThat(fromRepo.getKeyword(), is(equalTo(keyword)));
    }

    @Test
    public void save(){
        String keyword = "KEYWORD";
        Tag tag = new TagImpl("ID", keyword);
        expect(tagTemplate.count(Query.query(Criteria.where("keyword").is(keyword)))).andReturn(0L);
        expect(tagTemplate.save(tag)).andReturn(tag);
        replay(tagTemplate);
        Tag returned = repo.save(tag);
        verify(tagTemplate);
        assertThat(returned, is(sameInstance(tag)));
    }

    @Test
    public void save_more(){
        String keyword = "KEYWORD";
        Tag tag = new TagImpl("ID", keyword);
        expect(tagTemplate.count(Query.query(Criteria.where("keyword").is(keyword)))).andReturn(1L);
        replay(tagTemplate);
        Tag returned = repo.save(tag);
        verify(tagTemplate);
        assertThat(returned, is(sameInstance(tag)));
    }

    @Test
    public void delete(){
        String id ="id";
        Tag tag = new TagImpl(id, "keyword");
        tagTemplate.remove(Query.query(Criteria.where("_id").is(id)));
        expectLastCall();
        replay(tagTemplate);

        repo.delete(tag);
        verify(tagTemplate);

    }
}
