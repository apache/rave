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
import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.MongoModelOperations;
import org.apache.rave.portal.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 */
@Component
public class MongoDbPersonRepository implements PersonRepository {

    @Autowired
    private MongoModelOperations.MongoUserOperations template;

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
        throw new NotImplementedException();  //TODO build query
    }

    @Override
    public List<Person> findAllConnectedPeopleWithFriend(String username, String friendUsername) {
        return findFriendsWithFriend(username, friendUsername); //TODO update when groups is implemented
    }

    @Override
    public List<Person> findFriends(String username) {
        MongoDbUser user = (MongoDbUser)template.find(getUsernameQuery(username));
        return Lists.transform(user.getFriends(), new Function<MongoDbPersonAssociation, Person>() {
            @Override
            public Person apply(@Nullable MongoDbPersonAssociation input) {
                return input == null || input.getRequestStatus() != FriendRequestStatus.ACCEPTED ? null : template.get(input.getPersonId()).toPerson();
            }
        });
    }
    @Override
    public List<Person> findFriends(String username, String appId) {
        throw new NotImplementedException();  //TODO build query
    }

    @Override
    public List<Person> findFriendsWithFriend(String username, String friendUsername) {
        MongoDbUser follower = (MongoDbUser)template.find(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser)template.find(getUsernameQuery(friendUsername));
        List<Long> commonFriends = getCommonFriends(follower, followed);
        return Lists.transform(commonFriends, new Function<Long, Person>() {
            @Override
            public Person apply(@Nullable Long id) {
                return id == null? null : template.get(id).toPerson();
            }
        });
    }

    @Override
    public List<Person> findByGroup(String groupId) {
        throw new NotImplementedException(); //TODO build query
    }

    @Override
    public List<Person> findByGroup(String groupId, String appId) {
        throw new NotImplementedException();  //TODO build query
    }

    @Override
    public List<Person> findByGroupWithFriend(String groupId, String friendUsername) {
        throw new NotImplementedException();  //TODO build query
    }

    @Override
    public boolean addFriend(String friendUsername, String username) {
        MongoDbUser follower = (MongoDbUser)template.find(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser)template.find(getUsernameQuery(friendUsername));

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
        MongoDbUser follower = (MongoDbUser)template.find(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser)template.find(getUsernameQuery(friendUsername));
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
        MongoDbUser follower = (MongoDbUser)template.find(getUsernameQuery(username));
        MongoDbUser followed = (MongoDbUser)template.find(getUsernameQuery(friendUsername));
        setAccepted(follower, followed);
        setAccepted(followed, follower);
        template.save(follower);
        template.save(followed);
        return true;
    }

    @Override
    public List<Person> findFriendRequestsReceived(String username) {
        MongoDbUser followed = (MongoDbUser)template.find(getUsernameQuery(username));
        return Lists.transform(followed.getFriends(), new Function<MongoDbPersonAssociation, Person>() {
            @Override
            public Person apply(@Nullable MongoDbPersonAssociation input) {
                return input == null || input.getRequestStatus() != FriendRequestStatus.RECEIVED ? null : template.get(input.getPersonId()).toPerson();
            }
        });
    }

    @Override
    public List<Person> findFriendRequestsSent(String username) {
        MongoDbUser follower = (MongoDbUser)template.find(getUsernameQuery(username));
        return Lists.transform(follower.getFriends(), new Function<MongoDbPersonAssociation, Person>() {
            @Override
            public Person apply(@Nullable MongoDbPersonAssociation input) {
                return input == null  || input.getRequestStatus() != FriendRequestStatus.SENT ? null : template.get(input.getPersonId()).toPerson();
            }
        });
    }

    @Override
    public Class<? extends Person> getType() {
        return Person.class;
    }

    @Override
    public Person get(long id) {
        return template.get(id);
    }

    @Override
    public Person save(Person item) {
        //TODO Support saving people other than users
        return item instanceof User ? template.save((User)item) : null;
    }

    @Override
    public void delete(Person item) {
        template.remove(getUsernameQuery(item.getUsername()));
    }

    private Query getUsernameQuery(String username) {
        return query(where("username").is(username));
    }


    private Query query(Criteria criteria) {
        return new Query(criteria);
    }

    private void removeAssociation(MongoDbUser friend, MongoDbUser person) {
        for(MongoDbPersonAssociation association : person.getFriends()) {
            if(association.getPersonId().equals(friend.getId())) {
                person.getFriends().remove(association);
                break;
            }
        }
    }

    private void setAccepted(MongoDbUser friend, MongoDbUser person) {
        for(MongoDbPersonAssociation association : person.getFriends()) {
            if(association.getPersonId().equals(friend.getId())) {
                association.setRequestStatus(FriendRequestStatus.ACCEPTED);
            }
        }
    }

    private List<Long> getCommonFriends(MongoDbUser follower, MongoDbUser followed) {
        List<Long> ids= Lists.newArrayList();
        for(MongoDbPersonAssociation association : follower.getFriends()) {
            for(MongoDbPersonAssociation friendAssociation : followed.getFriends()) {
                if(association.getPersonId().equals(friendAssociation.getPersonId()) &&
                        association.getRequestStatus().equals(FriendRequestStatus.ACCEPTED) &&
                        friendAssociation.getRequestStatus().equals(FriendRequestStatus.ACCEPTED)) {
                    ids.add(friendAssociation.getPersonId());
                }
            }
        }
        return ids;
    }
}
