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

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.JpaGroup;
import org.apache.rave.portal.model.JpaPerson;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.portal.model.JpaWidget;
import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.conversion.JpaPersonConverter;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        List<Person> connections = new ArrayList<Person>();
        connections.addAll(findFriends(username));
        TypedQuery<JpaPerson> members = manager.createNamedQuery(JpaPerson.FIND_BY_GROUP_MEMBERSHIP, JpaPerson.class);
        members.setParameter(JpaPerson.USERNAME_PARAM, username);
        CollectionUtils.addUniqueValues(CollectionUtils.<Person>toBaseTypedList(members.getResultList()), connections);
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
        return CollectionUtils.<Person>toBaseTypedList(friends.getResultList());
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public List<Person> findFriends(String username, String appId) {
    	List<Person> friendsUsingWidget = new ArrayList<Person>();

    	TypedQuery query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_URL, JpaWidget.class);
    	query.setParameter(JpaWidget.PARAM_URL, appId);
		final List<JpaWidget> resultList = query.getResultList();
        Widget widget = getSingleResult(resultList);

        query = manager.createNamedQuery(JpaUser.USER_GET_ALL_FOR_ADDED_WIDGET, JpaUser.class);
        query.setParameter(JpaUser.PARAM_WIDGET_ID, Long.parseLong(widget.getId()));
        List<User> widgetUsers = query.getResultList();

        List<Person> userFriends = findFriends(username);
        for(Person userFriend : userFriends) {
    		for (User widgetUser : widgetUsers) {
    			if(userFriend.getUsername().equals(widgetUser.getUsername())) {
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
    	for(Person userFriend : userFriends) {
    		for (Person friendFriend : friendFriends) {
    			if(userFriend.getUsername().equals(friendFriend.getUsername())) {
    				friendsWithFriend.add(userFriend);
    			}
    		}
    	}
    	return friendsWithFriend;
    }

    @Override
    public List<Person> findByGroup(String groupId) {
        TypedQuery<JpaGroup> query = manager.createNamedQuery(JpaGroup.FIND_BY_TITLE, JpaGroup.class);
        query.setParameter(JpaGroup.GROUP_ID_PARAM, groupId);
        JpaGroup result = getSingleResult(query.getResultList());
        return result == null ? new ArrayList<Person>() : CollectionUtils.<Person>toBaseTypedList(result.getMembers());
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
        return manager.find(JpaPerson.class, id);
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
}
