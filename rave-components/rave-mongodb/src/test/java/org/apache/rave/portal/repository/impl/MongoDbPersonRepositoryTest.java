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
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.MongoUserOperations;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Test for MongoDb Person Repository class
 */
public class MongoDbPersonRepositoryTest {

    private MongoUserOperations template;
    private MongoPageOperations pageTemplate;
    private MongoWidgetOperations widgetOperations;
    private MongoDbPersonRepository repo;

    @Before
    public void setUp() {
        template = createMock(MongoUserOperations.class);
        pageTemplate = createMock(MongoPageOperations.class);
        widgetOperations = createMock(MongoWidgetOperations.class);
        repo = new MongoDbPersonRepository();
        repo.setTemplate(template);
        repo.setPageTemplate(pageTemplate);
        repo.setWidgetOperations(widgetOperations);
    }

    @Test
    public void findFriends_username() {
        String username = "username";
        MongoDbUser user = new MongoDbUser("1234L");
        user.setUsername(username);
        List<MongoDbPersonAssociation> friends = Lists.newLinkedList();
        MongoDbPersonAssociation friend1 = new MongoDbPersonAssociation("1111L", FriendRequestStatus.ACCEPTED, MongoDbPersonAssociation.Direction.INCOMING);
        friends.add(friend1);
        MongoDbUser friend_1 = new MongoDbUser("1111L");
        user.setFriends(friends);

        expect(template.findOne(isA(Query.class))).andReturn(user);
        expect(template.get("1111L")).andReturn(friend_1);
        replay(template);
        List<Person> results = repo.findFriends(username);
        assertNotNull(results.get(0));
        assertThat(results.size(), is(equalTo(1)));

    }


    @Test
    public void findFriends_appId() {
        String username = "username";
        String appId = "www.test.com";
        String id = "2222L";
        String userId = "1111L";
        MongoDbUser user = new MongoDbUser(userId);
        user.setUsername(username);
        List<MongoDbPersonAssociation> friends = Lists.newLinkedList();
        MongoDbPersonAssociation friend = new MongoDbPersonAssociation(id, FriendRequestStatus.ACCEPTED, MongoDbPersonAssociation.Direction.INCOMING);
        friends.add(friend);
        user.setFriends(friends);

        Widget w = new WidgetImpl();
        w.setUrl(appId);
        Page page = new PageImpl();
        page.setOwnerId(id);

        List<Region> regions = Lists.newLinkedList();
        Region r = new RegionImpl();
        RegionWidget rw = new RegionWidgetImpl();
        List<RegionWidget> regionWidgets = Lists.newLinkedList();
        regionWidgets.add(rw);
        r.setRegionWidgets(regionWidgets);
        regions.add(r);
        page.setRegions(regions);

        List<Page> pages = Lists.newLinkedList();
        pages.add(page);
        List<String> ids = Lists.newArrayList();
        ids.add(id);

        expect(template.findOne(query(where("username").is(username)))).andReturn(user);
        expect(template.get(id)).andReturn(new UserImpl(id));
        replay(template);
        expect(widgetOperations.findOne(isA(Query.class))).andReturn(w);
        replay(widgetOperations);
        expect(pageTemplate.find(query(where("ownerId").in(ids).and("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(w.getId())))))).andReturn(pages);
        replay(pageTemplate);


        List<Person> results = repo.findFriends(username, appId);
        assertThat(results.get(0).getId(), is(equalTo(id)));
        assertThat(results.size(), is(equalTo(1)));
    }

    @Test
    public void findFriendsWithFriend() {
        String username = "follower";
        String friendUsername = "followed";
        MongoDbUser follower = new MongoDbUser("1111L");
        MongoDbUser followed = new MongoDbUser("2222L");
        MongoDbPersonAssociation friend = new MongoDbPersonAssociation("3333L", FriendRequestStatus.ACCEPTED, MongoDbPersonAssociation.Direction.INCOMING);
        MongoDbPersonAssociation friend2 = new MongoDbPersonAssociation("4444L", FriendRequestStatus.ACCEPTED, MongoDbPersonAssociation.Direction.INCOMING);
        List<MongoDbPersonAssociation> friends_follower = Lists.newLinkedList();
        List<MongoDbPersonAssociation> friends_followed = Lists.newLinkedList();

        friends_follower.add(friend);
        friends_followed.add(friend);
        friends_followed.add(friend2);
        follower.setFriends(friends_follower);
        followed.setFriends(friends_followed);

        expect(template.findOne(isA(Query.class))).andReturn(follower);
        expect(template.findOne(isA(Query.class))).andReturn(followed);
        expect(template.get("3333L")).andReturn(follower);
        replay(template);

        List<Person> results = repo.findFriendsWithFriend(username, friendUsername);
        assertThat(results.size(), is(equalTo(1)));

    }

    @Test
    public void addFriend() {
        String username = "Carol";
        String friendUsername = "Amy";
        MongoDbUser carol = new MongoDbUser("1111L");
        MongoDbUser amy = new MongoDbUser("2222L");

        List<MongoDbPersonAssociation> carols_friends = Lists.newArrayList();
        MongoDbPersonAssociation friendC = new MongoDbPersonAssociation();
        carols_friends.add(friendC);
        List<MongoDbPersonAssociation> amys_friends = Lists.newArrayList();
        MongoDbPersonAssociation friendA = new MongoDbPersonAssociation();
        amys_friends.add(friendA);
        carol.setFriends(carols_friends);
        amy.setFriends(amys_friends);

        expect(template.findOne(isA(Query.class))).andReturn(carol);
        expect(template.findOne(isA(Query.class))).andReturn(amy);
        expect(template.save(isA(MongoDbUser.class))).andReturn(amy);
        expect(template.save(isA(MongoDbUser.class))).andReturn(carol);
        replay(template);

        boolean result = repo.addFriend(friendUsername, username);
        assertTrue(result);
    }

    @Test
    public void findFriendsAndRequests() {
        HashMap<String, List<Person>> results;
        String username = "username";
        MongoDbUser user = new MongoDbUser();

        List<MongoDbPersonAssociation> friends = Lists.newArrayList();
        MongoDbPersonAssociation friend_accepted = new MongoDbPersonAssociation("1111L", FriendRequestStatus.ACCEPTED, MongoDbPersonAssociation.Direction.INCOMING);
        MongoDbPersonAssociation friend_sent = new MongoDbPersonAssociation("2222L", FriendRequestStatus.SENT, MongoDbPersonAssociation.Direction.INCOMING);
        MongoDbPersonAssociation friend_received = new MongoDbPersonAssociation("3333L", FriendRequestStatus.RECEIVED, MongoDbPersonAssociation.Direction.INCOMING);
        MongoDbPersonAssociation friend_received2 = new MongoDbPersonAssociation("4444L", FriendRequestStatus.RECEIVED, MongoDbPersonAssociation.Direction.INCOMING);
        MongoDbUser friend_a = new MongoDbUser("1111L");
        MongoDbUser friend_s = new MongoDbUser("2222L");
        MongoDbUser friend_r = new MongoDbUser("3333L");
        MongoDbUser friend_r2 = new MongoDbUser("4444L");

        friends.add(friend_sent);
        friends.add(friend_accepted);
        friends.add(friend_received);
        friends.add(friend_received2);
        user.setFriends(friends);

        expect(template.findOne(isA(Query.class))).andReturn(user);
        expect(template.get("1111L")).andReturn(friend_a);
        expect(template.findOne(isA(Query.class))).andReturn(user);
        expect(template.get("2222L")).andReturn(friend_s);
        expect(template.findOne(isA(Query.class))).andReturn(user);
        expect(template.get("3333L")).andReturn(friend_r);
        expect(template.get("4444L")).andReturn(friend_r2);
        replay(template);

        results = repo.findFriendsAndRequests(username);
        assertNotNull(results);
        assertThat(results.size(), equalTo(3));
        assertThat(results.get("received").size(), is(equalTo(2)));
    }

    @Test
    public void acceptFriendRequest() {
        String username = "username";
        String friendUsername = "friendUsername";
        MongoDbUser carol = new MongoDbUser("1111L");
        MongoDbUser amy = new MongoDbUser("2222L");

        List<MongoDbPersonAssociation> carols_friends = Lists.newArrayList();
        MongoDbPersonAssociation friendA = new MongoDbPersonAssociation("1111L", FriendRequestStatus.SENT, MongoDbPersonAssociation.Direction.OUTGOING);
        carols_friends.add(friendA);
        List<MongoDbPersonAssociation> amys_friends = Lists.newArrayList();
        MongoDbPersonAssociation friendC = new MongoDbPersonAssociation("2222L", FriendRequestStatus.RECEIVED, MongoDbPersonAssociation.Direction.INCOMING);

        amys_friends.add(friendC);
        carol.setFriends(carols_friends);
        amy.setFriends(amys_friends);

        expect(template.findOne(isA(Query.class))).andReturn(carol);
        expect(template.findOne(isA(Query.class))).andReturn(amy);
        expect(template.save(isA(MongoDbUser.class))).andReturn(carol);
        expect(template.save(isA(MongoDbUser.class))).andReturn(amy);
        replay(template);

        boolean result = repo.acceptFriendRequest(friendUsername, username);
        assertTrue(result);
    }

    @Test
    public void getAll(){
        List<Person> people = new ArrayList<Person>();
        List<User> users = new ArrayList<User>();
        expect(template.find(isA(Query.class))).andReturn(users);
        replay(template);
        assertThat(people, is(repo.getAll()));
    }

    @Test
    public void getLimitedList_Valid(){
        int offset = 234;
        int pageSize = 123;
        List<User> users = new ArrayList<User>();
        List<Person> people = new ArrayList<Person>();
        expect(template.find(isA(Query.class))).andReturn(users);
        replay(template);
        assertThat(people, is(repo.getLimitedList(offset, pageSize)));
    }

    @Test
    public void getCountAll_Valid(){
        long doubleOseven = 007;
        expect(template.count(new Query())).andReturn(doubleOseven);
        replay(template);
        assertThat((int)doubleOseven, is(sameInstance(repo.getCountAll())));
    }
}
