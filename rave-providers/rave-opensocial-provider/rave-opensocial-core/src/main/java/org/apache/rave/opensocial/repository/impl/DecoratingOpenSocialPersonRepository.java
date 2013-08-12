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

package org.apache.rave.opensocial.repository.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.opensocial.repository.OpenSocialPersonRepository;
import org.apache.rave.model.Person;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.shindig.protocol.model.FilterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
@Repository
public class DecoratingOpenSocialPersonRepository implements OpenSocialPersonRepository {

    @Autowired
    private PersonRepository underlying;

    @Override
    public List<Person> findAllConnectedPeople(String username, String field, FilterOperation operation, String value) {
        throw new NotSupportedException();
    }

    @Override
    public List<Person> findFriends(String username, String field, FilterOperation operation, String value) {
        throw new NotSupportedException();    }

    @Override
    public List<Person> findByGroup(String groupId, String field, FilterOperation operation, String value) {
        throw new NotSupportedException();
    }

    @Override
    public Person findByUsername(String username) {
        return underlying.findByUsername(username);
    }

    @Override
    public List<Person> findAllConnectedPeople(String username) {
        return underlying.findAllConnectedPeople(username);
    }

    @Override
    public List<Person> findAllConnectedPeople(String username, String appId) {
        return underlying.findAllConnectedPeople(username, appId);
    }

    @Override
    public List<Person> findAllConnectedPeopleWithFriend(String username, String friendUsername) {
        return underlying.findAllConnectedPeopleWithFriend(username, friendUsername);
    }

    @Override
    public List<Person> findFriends(String username) {
        return underlying.findFriends(username);
    }

    @Override
    public List<Person> findFriends(String username, String appId) {
        return underlying.findFriends(username, appId);
    }

    @Override
    public List<Person> findFriendsWithFriend(String username, String friendUsername) {
        return underlying.findFriendsWithFriend(username, friendUsername);
    }

    @Override
    public List<Person> findByGroup(String groupId) {
        return underlying.findByGroup(groupId);
    }

    @Override
    public List<Person> findByGroup(String groupId, String appId) {
        return underlying.findByGroup(groupId, appId);
    }

    @Override
    public List<Person> findByGroupWithFriend(String groupId, String friendUsername) {
        return underlying.findByGroupWithFriend(groupId, friendUsername);
    }

    @Override
    public Class<? extends Person> getType() {
        return underlying.getType();
    }

    @Override
    public Person get(String id) {
        return underlying.get(id);
    }

    @Override
    public Person save(Person item) {
        return underlying.save(item);
    }

    @Override
    public void delete(Person item) {
        underlying.delete(item);
    }

	@Override
	public boolean addFriend(String friendUsername, String username) {
		return underlying.addFriend(friendUsername, username);
	}

	@Override
	public void removeFriend(String friendUsername, String username) {
		underlying.removeFriend(friendUsername, username);
	}

	@Override
	public HashMap<String, List<Person>> findFriendsAndRequests(String username) {
		return underlying.findFriendsAndRequests(username);
	}

	@Override
	public boolean acceptFriendRequest(String friendUsername, String username) {
		return underlying.acceptFriendRequest(friendUsername, username);
	}

	@Override
	public List<Person> findFriendRequestsReceived(String username) {
		return underlying.findFriendRequestsReceived(username);
	}

	@Override
	public List<Person> findFriendRequestsSent(String username) {
		return underlying.findFriendRequestsSent(username);
	}

	@Override
	public int removeAllFriendsAndRequests(String userid) {
		return underlying.removeAllFriendsAndRequests(userid);
	}

    @Override
    public List<Person> getAll() {
        throw new NotSupportedException();
    }

    @Override
    public List<Person> getLimitedList(int offset, int limit) {
        throw new NotSupportedException();
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException();
    }
}
