/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.FriendRequestStatus;
import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.JpaPersonConverter;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 *
 */
@Repository
public class JpaPersonRepository implements PersonRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaPersonConverter personConverter;

    @Override
    public Person findByUsername(String username) {
        TypedQuery<JpaPerson> query = manager.createNamedQuery(JpaPerson.FIND_BY_USERNAME, JpaPerson.class);
        query.setParameter(JpaPerson.USERNAME_PARAM, username);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<Person> findAllConnectedPeople(String username) {
        Person person = findByUsername(username);
        List<Person> connections = Lists.newLinkedList();
        if (person != null) {
            String personId = person.getId();
            connections.addAll(findFriends(username));
            TypedQuery<JpaGroup> members = manager.createQuery("SELECT g from JpaGroup g where :userId member of g.members", JpaGroup.class);
            members.setParameter("userId", personId);
            for (JpaGroup groups : members.getResultList()) {
                addPeopleByIds(groups, connections);
            }
        }
        connections.remove(person);
        return connections;
    }

    @Override
    public List<Person> findAllConnectedPeople(String username, String appId) {
        throw new NotSupportedException();
    }

    @Override
    public List<Person> findAllConnectedPeopleWithFriend(String username, String friendUsername) {
        throw new NotSupportedException();
    }

    @Override
    public List<Person> findFriends(String username) {
        TypedQuery<JpaPerson> friends = manager.createNamedQuery(JpaPerson.FIND_FRIENDS_BY_USERNAME, JpaPerson.class);
        friends.setParameter(JpaPerson.USERNAME_PARAM, username);
        friends.setParameter(JpaPerson.STATUS_PARAM, FriendRequestStatus.ACCEPTED);
        return CollectionUtils.<Person>toBaseTypedList(friends.getResultList());
    }

    @Override
    public List<Person> findFriends(String username, String appId) {
        List<Person> friendsUsingWidget = new ArrayList<Person>();

        TypedQuery<JpaWidget> widgetQuery = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_URL, JpaWidget.class);
        widgetQuery.setParameter(JpaWidget.PARAM_URL, appId);
        final List<JpaWidget> resultList = widgetQuery.getResultList();
        Widget widget = getSingleResult(resultList);

        TypedQuery<JpaUser> usersQuery = manager.createNamedQuery(JpaUser.USER_GET_ALL_FOR_ADDED_WIDGET, JpaUser.class);
        usersQuery.setParameter(JpaUser.PARAM_WIDGET_ID, Long.parseLong(widget.getId()));
        List<User> widgetUsers = CollectionUtils.<User>toBaseTypedList(usersQuery.getResultList());

        List<Person> userFriends = findFriends(username);
        for (Person userFriend : userFriends) {
            for (User widgetUser : widgetUsers) {
                if (userFriend.getUsername().equals(widgetUser.getUsername())) {
                    friendsUsingWidget.add(userFriend);
                }
            }
        }
        return friendsUsingWidget;
    }

    @Override
    public List<Person> findFriendsWithFriend(String username, String friendUsername) {
        List<Person> friendsWithFriend = new ArrayList<Person>();
        List<Person> userFriends = findFriends(username);
        List<Person> friendFriends = findFriends(friendUsername);
        for (Person userFriend : userFriends) {
            for (Person friendFriend : friendFriends) {
                if (userFriend.getUsername().equals(friendFriend.getUsername())) {
                    friendsWithFriend.add(userFriend);
                }
            }
        }
        return friendsWithFriend;
    }

    @Override
    public List<Person> findByGroup(String groupId) {
        TypedQuery<JpaGroup> query = manager.createNamedQuery(JpaGroup.FIND_BY_TITLE, JpaGroup.class);
        query.setParameter(JpaGroup.GROUP_TITLE_PARAM, groupId);
        return getPeopleByIds(getSingleResult(query.getResultList()));
    }

    @Override
    public List<Person> findByGroup(String groupId, String appId) {
        throw new NotSupportedException();
    }

    @Override
    public List<Person> findByGroupWithFriend(String groupId, String friendUsername) {
        throw new NotSupportedException();
    }

    @Override
    public Class<? extends Person> getType() {
        return JpaPerson.class;
    }

    @Override
    public Person get(String id) {
        return manager.find(JpaPerson.class, Long.parseLong(id));
    }

    @Override
    public Person save(Person item) {
        JpaPerson person = personConverter.convert(item);
        return saveOrUpdate(person.getEntityId(), manager, person);
    }

    @Override
    public void delete(Person item) {
        manager.remove(item instanceof JpaPerson ? item : findByUsername(item.getUsername()));
    }

    @Override
    public boolean addFriend(String friendUsername, String username) {
        JpaPersonAssociation senderItem = new JpaPersonAssociation();
        senderItem.setFollower(personConverter.convert(findByUsername(username)));
        senderItem.setFollowedby(personConverter.convert(findByUsername(friendUsername)));
        senderItem.setStatus(FriendRequestStatus.SENT);
        senderItem = saveOrUpdate(senderItem.getEntityId(), manager, senderItem);

        JpaPersonAssociation receiverItem = new JpaPersonAssociation();
        receiverItem.setFollower(personConverter.convert(findByUsername(friendUsername)));
        receiverItem.setFollowedby(personConverter.convert(findByUsername(username)));
        receiverItem.setStatus(FriendRequestStatus.RECEIVED);
        receiverItem = saveOrUpdate(receiverItem.getEntityId(), manager, receiverItem);

        return senderItem.getEntityId() != null && receiverItem.getEntityId() != null;
    }

    @Override
    public void removeFriend(String friendUsername, String username) {
        TypedQuery<JpaPersonAssociation> query = manager.createNamedQuery(JpaPersonAssociation.FIND_ASSOCIATION_ITEM_BY_USERNAMES, JpaPersonAssociation.class);
        query.setParameter(JpaPersonAssociation.FOLLOWER_USERNAME, username);
        query.setParameter(JpaPersonAssociation.FOLLOWEDBY_USERNAME, friendUsername);
        JpaPersonAssociation item = getSingleResult(query.getResultList());
        manager.remove(item);

        query = manager.createNamedQuery(JpaPersonAssociation.FIND_ASSOCIATION_ITEM_BY_USERNAMES, JpaPersonAssociation.class);
        query.setParameter(JpaPersonAssociation.FOLLOWER_USERNAME, friendUsername);
        query.setParameter(JpaPersonAssociation.FOLLOWEDBY_USERNAME, username);
        JpaPersonAssociation inverseItem = getSingleResult(query.getResultList());
        manager.remove(inverseItem);
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
    public List<Person> findFriendRequestsSent(String username) {
        TypedQuery<JpaPerson> friends = manager.createNamedQuery(JpaPerson.FIND_FRIENDS_BY_USERNAME, JpaPerson.class);
        friends.setParameter(JpaPerson.USERNAME_PARAM, username);
        friends.setParameter(JpaPerson.STATUS_PARAM, FriendRequestStatus.SENT);
        return CollectionUtils.<Person>toBaseTypedList(friends.getResultList());
    }

    @Override
    public List<Person> findFriendRequestsReceived(String username) {
        TypedQuery<JpaPerson> friends = manager.createNamedQuery(JpaPerson.FIND_FRIENDS_BY_USERNAME, JpaPerson.class);
        friends.setParameter(JpaPerson.USERNAME_PARAM, username);
        friends.setParameter(JpaPerson.STATUS_PARAM, FriendRequestStatus.RECEIVED);
        return CollectionUtils.<Person>toBaseTypedList(friends.getResultList());
    }

    @Override
    public boolean acceptFriendRequest(String friendUsername, String username) {
        TypedQuery<JpaPersonAssociation> query = manager.createNamedQuery(JpaPersonAssociation.FIND_ASSOCIATION_ITEM_BY_USERNAMES, JpaPersonAssociation.class);
        query.setParameter(JpaPersonAssociation.FOLLOWER_USERNAME, username);
        query.setParameter(JpaPersonAssociation.FOLLOWEDBY_USERNAME, friendUsername);
        JpaPersonAssociation receiverItem = getSingleResult(query.getResultList());
        receiverItem.setStatus(FriendRequestStatus.ACCEPTED);
        receiverItem = saveOrUpdate(receiverItem.getEntityId(), manager, receiverItem);

        query = manager.createNamedQuery(JpaPersonAssociation.FIND_ASSOCIATION_ITEM_BY_USERNAMES, JpaPersonAssociation.class);
        query.setParameter(JpaPersonAssociation.FOLLOWER_USERNAME, friendUsername);
        query.setParameter(JpaPersonAssociation.FOLLOWEDBY_USERNAME, username);
        JpaPersonAssociation senderItem = getSingleResult(query.getResultList());
        senderItem.setStatus(FriendRequestStatus.ACCEPTED);
        senderItem = saveOrUpdate(senderItem.getEntityId(), manager, senderItem);

        return receiverItem.getEntityId() != null && senderItem.getEntityId() != null;
    }

    @Override
    public int removeAllFriendsAndRequests(String userid) {
        TypedQuery<JpaPersonAssociation> query = manager.createNamedQuery(JpaPersonAssociation.DELETE_ASSOCIATION_ITEMS_BY_USERID, JpaPersonAssociation.class);
        query.setParameter(JpaPersonAssociation.USERID, Long.parseLong(userid));
        return query.executeUpdate();
    }

    private List<Person> getPeopleByIds(JpaGroup result) {
        List<Person> members = Lists.newLinkedList();
        addPeopleByIds(result, members);
        return members;
    }

    private void addPeopleByIds(JpaGroup result, List<Person> members) {
        if (result != null) {
            for (String personId : result.getMemberIds()) {
                Person person = get(personId);
                if (!members.contains(person)) {
                    members.add(person);
                }
            }
        }
    }

    @Override
    public List<Person> getAll() {
        TypedQuery<Person> query = manager.createNamedQuery(JpaPerson.GET_ALL, Person.class);
        return CollectionUtils.<Person>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Person> getLimitedList(int offset, int limit) {
        TypedQuery<Person> query = manager.createNamedQuery(JpaPerson.GET_ALL, Person.class);
        return CollectionUtils.<Person>toBaseTypedList(getPagedResultList(query, offset, limit));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaPerson.GET_COUNT);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }
}
