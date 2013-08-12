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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.rave.model.FriendRequestStatus;
import org.apache.rave.model.Page;
import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.MongoDbPersonAssociation;
import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.MongoUserOperations;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 */
@Component
public class MongoDbPersonRepository implements PersonRepository {

    @Autowired
    private MongoUserOperations template;

    @Autowired
    private MongoPageOperations pageTemplate;

    @Autowired
    private MongoWidgetOperations widgetOperations;

    @Override
    public Person findByUsername(String username) {
        return template.findOne(getUsernameQuery(username)).toPerson();
    }

    @Override
    public List<Person> findAllConnectedPeople(String username) {
        return findFriends(username);  //TODO update when groups is implemented
    }

    @Override
    public List<Person> findAllConnectedPeople(String username, String appId) {
        return findFriends(username, appId);  //TODO update when groups is implemented
    }

    @Override
    public List<Person> findAllConnectedPeopleWithFriend(String username, String friendUsername) {
        return findFriendsWithFriend(username, friendUsername); //TODO update when groups is implemented
    }

    @Override
    public List<Person> findFriends(String username) {
        MongoDbUser user = (MongoDbUser) template.findOne(getUsernameQuery(username));
        return Lists.transform(filterByRequestType(user.getFriends(), FriendRequestStatus.ACCEPTED), new Function<MongoDbPersonAssociation, Person>() {
            @Override
            public Person apply(@Nullable MongoDbPersonAssociation input) {
                return input == null ? null : template.get(input.getPersonId()).toPerson();
            }
        });
    }

    @Override
    public List<Person> findFriends(String username, String appId) {
        MongoDbUser user = (MongoDbUser) template.findOne(getUsernameQuery(username));
        Widget w = widgetOperations.findOne(query(where("url").is(appId)));
        Query q = query(where("ownerId").in(getFriendIds(user.getFriends())).and("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(w.getId()))));
        List<Page> pages = pageTemplate.find(q);
        return getPersonListFromPages(pages);
    }

    @Override
    public List<Person> findFriendsWithFriend(String username, String friendUsername) {
        MongoDbUser follower = (MongoDbUser) template.findOne(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser) template.findOne(getUsernameQuery(friendUsername));
        List<String> commonFriends = getCommonFriends(follower, followed);
        return Lists.transform(commonFriends, new Function<String, Person>() {
            @Override
            public Person apply(@Nullable String id) {
                return id == null ? null : template.get(id).toPerson();
            }
        });
    }

    @Override
    public List<Person> findByGroup(String groupId) {
        throw new UnsupportedOperationException("Not implemented"); //TODO build query
    }

    @Override
    public List<Person> findByGroup(String groupId, String appId) {
        throw new UnsupportedOperationException("Not implemented");  //TODO build query
    }

    @Override
    public List<Person> findByGroupWithFriend(String groupId, String friendUsername) {
        throw new UnsupportedOperationException("Not implemented");  //TODO build query
    }

    @Override
    public boolean addFriend(String friendUsername, String username) {
        MongoDbUser follower = (MongoDbUser) template.findOne(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser) template.findOne(getUsernameQuery(friendUsername));

        MongoDbPersonAssociation outgoing = new MongoDbPersonAssociation(followed.getId(), FriendRequestStatus.SENT, MongoDbPersonAssociation.Direction.OUTGOING);
        MongoDbPersonAssociation incoming = new MongoDbPersonAssociation(follower.getId(), FriendRequestStatus.RECEIVED, MongoDbPersonAssociation.Direction.INCOMING);

        follower.getFriends().add(outgoing);
        followed.getFriends().add(incoming);

        template.save(followed);
        template.save(follower);
        return true;
    }

    @Override
    public void removeFriend(String friendUsername, String username) {
        MongoDbUser follower = (MongoDbUser) template.findOne(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser) template.findOne(getUsernameQuery(friendUsername));
        removeAssociation(follower, followed);
        removeAssociation(followed, follower);
        template.save(follower);
        template.save(followed);
    }

    @Override
    public HashMap<String, List<Person>> findFriendsAndRequests(String username) {
        HashMap<String, List<Person>> friendsAndRequests = new HashMap<String, List<Person>>();
        friendsAndRequests.put(FriendRequestStatus.ACCEPTED.toString(), findFriends(username));
        friendsAndRequests.put(FriendRequestStatus.SENT.toString(), findFriendRequestsSent(username));
        friendsAndRequests.put(FriendRequestStatus.RECEIVED.toString(), findFriendRequestsReceived(username));
        return friendsAndRequests;
    }

    @Override
    public boolean acceptFriendRequest(String friendUsername, String username) {
        MongoDbUser follower = (MongoDbUser) template.findOne(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser) template.findOne(getUsernameQuery(friendUsername));
        setAccepted(follower, followed);
        setAccepted(followed, follower);
        template.save(follower);
        template.save(followed);
        return true;
    }

    @Override
    public List<Person> findFriendRequestsReceived(String username) {
        MongoDbUser followed = (MongoDbUser) template.findOne(getUsernameQuery(username));
        return getPersonListFromAssociations(followed, FriendRequestStatus.RECEIVED);
    }

    @Override
    public List<Person> findFriendRequestsSent(String username) {
        MongoDbUser follower = (MongoDbUser) template.findOne(getUsernameQuery(username));
        return getPersonListFromAssociations(follower, FriendRequestStatus.SENT);
    }

    @Override
    public int removeAllFriendsAndRequests(String userid) {
        MongoDbUser person = (MongoDbUser) template.get(userid);
        int count = person.getFriends().size();
        person.setFriends(Lists.<MongoDbPersonAssociation>newArrayList());
        save(person);
        return count;
    }

    @Override
    public Class<? extends Person> getType() {
        return Person.class;
    }

    @Override
    public Person get(String id) {
        return template.get(id);
    }

    @Override
    public Person save(Person item) {
        //TODO Support saving people other than users
        return item instanceof User ? template.save((User) item) : null;
    }

    @Override
    public void delete(Person item) {
        template.remove(getUsernameQuery(item.getUsername()));
    }

    private List<Person> getPersonListFromAssociations(MongoDbUser follower, FriendRequestStatus status) {
        return Lists.transform(filterByRequestType(follower.getFriends(), status), new Function<MongoDbPersonAssociation, Person>() {
            @Override
            public Person apply(@Nullable MongoDbPersonAssociation input) {
                return input == null ? null : template.get(input.getPersonId()).toPerson();
            }
        });
    }

    private List<Person> getPersonListFromPages(List<Page> pages) {
        return Lists.transform(pages, new Function<Page, Person>() {
            @Override
            public Person apply(@Nullable Page input) {
                return input == null ? null : template.get(input.getOwnerId()).toPerson();
            }
        });
    }

    private List<String> getFriendIds(List<MongoDbPersonAssociation> friends) {
        List<String> ids = Lists.newArrayList();
        for (MongoDbPersonAssociation friend : friends) {
            if (friend.getRequestStatus().equals(FriendRequestStatus.ACCEPTED)) {
                ids.add(friend.getPersonId());
            }
        }
        return ids;
    }

    private Query getUsernameQuery(String username) {
        return query(where("username").is(username));
    }

    private List<MongoDbPersonAssociation> filterByRequestType(List<MongoDbPersonAssociation> friends, FriendRequestStatus received) {
        List<MongoDbPersonAssociation> filtered = Lists.newArrayList();
        for (MongoDbPersonAssociation association : friends) {
            if (association.getRequestStatus().equals(received)) {
                filtered.add(association);
            }
        }
        return filtered;
    }

    private void removeAssociation(MongoDbUser friend, MongoDbUser person) {
        for (MongoDbPersonAssociation association : person.getFriends()) {
            if (association.getPersonId().equals(friend.getId())) {
                person.getFriends().remove(association);
                break;
            }
        }
    }

    private void setAccepted(MongoDbUser friend, MongoDbUser person) {
        for (MongoDbPersonAssociation association : person.getFriends()) {
            if (association.getPersonId().equals(friend.getId())) {
                association.setRequestStatus(FriendRequestStatus.ACCEPTED);
            }
        }
    }

    private List<String> getCommonFriends(MongoDbUser follower, MongoDbUser followed) {
        List<String> ids = Lists.newArrayList();
        Map<String, MongoDbPersonAssociation> friendHash = Maps.newHashMap();
        for (MongoDbPersonAssociation association : follower.getFriends()) {
            friendHash.put(association.getPersonId(), association);
        }
        for (MongoDbPersonAssociation friendAssociation : followed.getFriends()) {
            if (friendHash.containsKey(friendAssociation.getPersonId()) &&
                    friendHash.get(friendAssociation.getPersonId()).getRequestStatus().equals(FriendRequestStatus.ACCEPTED) &&
                    friendAssociation.getRequestStatus().equals(FriendRequestStatus.ACCEPTED)) {
                ids.add(friendAssociation.getPersonId());
            }
        }
        return ids;
    }

    public void setTemplate(MongoUserOperations template) {
        this.template = template;
    }

    public void setPageTemplate(MongoPageOperations pageTemplate) {
        this.pageTemplate = pageTemplate;
    }

    public void setWidgetOperations(MongoWidgetOperations widgetOperations) {
        this.widgetOperations = widgetOperations;
    }

    @Override
    public List<Person> getAll() {
        List<User> users = template.find(new Query());
        ArrayList<Person> persons= new ArrayList<Person>();
        for(User user : users) {
            persons.add(user.toPerson());
        }
        return persons;
    }

    @Override
    public List<Person> getLimitedList(int offset, int limit) {
        List<User> users = template.find(new Query().skip(offset).limit(limit));
        ArrayList<Person> persons= new ArrayList<Person>();
        for(User user : users) {
            persons.add(user.toPerson());
        }
        return persons;
    }

    @Override
    public int getCountAll() {
        return (int) template.count(new Query());
    }
}
