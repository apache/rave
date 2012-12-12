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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoDbPageRepositoryTest {

    private MongoPageOperations template;
    private MongoDbPageRepository repo;


    @Before
    public void setUp(){
        template = createMock(MongoPageOperations.class);
        repo = new MongoDbPageRepository();
        repo.setTemplate(template);
    }

    @Test
    public void deletePages(){
        int resultCount;
        long userID = 1111L;

        expect((int)template.count(isA(Query.class))).andReturn(1);
        template.remove(isA(Query.class));
        expectLastCall();
        replay(template);

        resultCount = repo.deletePages(userID, PageType.USER);
        assertThat(resultCount, is(equalTo(1)));
        verify(template);
    }

    @Test
    public void getPagesForUser(){
        Page p = new PageImpl();
        PageUser user1 = new PageUserImpl(1111L);
        User user2 = new UserImpl(2222L);
        user1.setUser(user2);
        List<PageUser> pageUser = Lists.newArrayList();
        pageUser.add(user1);
        p.setMembers(pageUser);
        List<Page> pages = Lists.newArrayList(p);

        List<PageUser> result;
        Long userId = 2222L;

        expect(template.find(query(where("members").elemMatch(where("userId").is(userId)).andOperator(where("pageType").is("USER"))))).andReturn(pages);
        replay(template);
        result = repo.getPagesForUser(userId, PageType.USER);

        assertThat(result.get(0).getUser(), is(equalTo(user2)));
        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).getUser().getId(), is(equalTo(2222L)));

    }

    @Test
    public void getSingleRecord_valid(){
        Long userId = 1111L;
        Long pageId = 2222L;

        Page testPage = new PageImpl(2222L);
        PageUser pu = new PageUserImpl(3333L);
        User u = new UserImpl(1111L);
        pu.setUser(u);
        List<PageUser> users = Lists.newArrayList();
        users.add(pu);
        testPage.setMembers(users);
        PageUser result;

        expect(template.get(pageId)).andReturn(testPage);
        replay(template);

        result = repo.getSingleRecord(userId, pageId);
        assertThat(result, is(sameInstance(pu)));
        assertThat(result.getId(), is(equalTo(3333L)));
        assertThat(result.getUser().getId(), is(equalTo(1111L)));

    }

    @Test
    public void getSingleRecord_null(){
        Long userId = 1111L;
        Long pageId = 2222L;

        Page testPage = new PageImpl(2222L);
        PageUser pu = new PageUserImpl(3333L);
        User u = new UserImpl(1234L);
        pu.setUser(u);
        List<PageUser> users = Lists.newArrayList();
        users.add(pu);
        testPage.setMembers(users);
        PageUser result;

        expect(template.get(pageId)).andReturn(testPage);
        replay(template);

        result = repo.getSingleRecord(userId, pageId);
        assertNull(result);
    }

}
