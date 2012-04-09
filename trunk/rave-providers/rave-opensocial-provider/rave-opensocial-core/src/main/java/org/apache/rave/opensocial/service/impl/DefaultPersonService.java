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

package org.apache.rave.opensocial.service.impl;

import com.google.common.collect.Lists;
import org.apache.rave.opensocial.repository.PersonRepository;
import org.apache.rave.opensocial.service.SimplePersonService;
import org.apache.rave.util.CollectionUtils;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Implementation of the {@link PersonService} SPI
 */
@Service
public class DefaultPersonService implements PersonService, SimplePersonService {

    private final PersonRepository repository;

    @Autowired
    public DefaultPersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds,
                                                       GroupId groupId,
                                                       CollectionOptions collectionOptions,
                                                       Set<String> fields,
                                                       SecurityToken token) throws ProtocolException {

        List<org.apache.rave.portal.model.Person> people = getPeople(userIds, groupId, collectionOptions, token);
        return ImmediateFuture.newInstance(new RestfulCollection<Person>(convertPeople(people, fields)));
    }

    @Override
    public Future<Person> getPerson(UserId id, Set<String> fields, SecurityToken token) throws ProtocolException {
        return ImmediateFuture.newInstance(convertPerson(getPersonForId(id, token), fields));
    }

    @Override
    public List<org.apache.rave.portal.model.Person> getPeople(Set<UserId> userIds, GroupId groupId,
                                                                   CollectionOptions collectionOptions,
                                                                   SecurityToken token) {
        switch (groupId.getType()) {
            case all:
                return getUniqueListOfConnectedPeople(userIds, collectionOptions, token);
            case friends:
                return getUniqueListOfFriends(userIds, collectionOptions, token);
            case groupId:
                return getGroupMembersFromRepository(collectionOptions, groupId.getGroupId(), token.getAppId());
            case self:
                return Lists.newArrayList(getPersonForId(new UserId(UserId.Type.me, null), token));
            case deleted:
                throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Deleted Friends are not tracked by the container");
            default:
                throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Invalid group id specified by request");
        }
    }

    private List<org.apache.rave.portal.model.Person> getUniqueListOfFriends(Set<UserId> userIds,
                                                                                 CollectionOptions collectionOptions,
                                                                                 SecurityToken token) {

        List<org.apache.rave.portal.model.Person> people = new ArrayList<org.apache.rave.portal.model.Person>();
        for (UserId id : userIds) {
            CollectionUtils.addUniqueValues(getFriendsFromRepository(collectionOptions, token.getAppId(), id.getUserId(token)), people);
        }
        return people;
    }

    private List<org.apache.rave.portal.model.Person> getUniqueListOfConnectedPeople(Set<UserId> userIds,
                                                                                         CollectionOptions collectionOptions,
                                                                                         SecurityToken token) {

        List<org.apache.rave.portal.model.Person> people = new ArrayList<org.apache.rave.portal.model.Person>();
        for (UserId id : userIds) {
            CollectionUtils.addUniqueValues(getConnectedPeopleFromRepository(collectionOptions, token.getAppId(), id.getUserId(token)), people);
        }
        return people;
    }

    private List<org.apache.rave.portal.model.Person> getFriendsFromRepository(CollectionOptions collectionOptions,
                                                                                   String appId,
                                                                                   String userId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.portal.model.Person> current;
        //Currently ignoring TOP FRIENDS as it hasn't been defined what a top friend is
        if (filter == null || filter.equals(PersonService.ALL_FILTER) || filter.equals(PersonService.TOP_FRIENDS_FILTER)) {
            current = repository.findFriends(userId);

        } else if (filter.equals(PersonService.HAS_APP_FILTER)) {
            current = repository.findFriends(userId, appId);

        } else if (filter.equals(PersonService.IS_WITH_FRIENDS_FILTER)) {
            current = repository.findFriendsWithFriend(userId, collectionOptions.getFilterValue());

        //Represents the default case (filter by field)
        } else {
            current = repository.findFriends(userId, filter, collectionOptions.getFilterOperation(), collectionOptions.getFilterValue());
        }
        return current;
    }

    private List<org.apache.rave.portal.model.Person> getConnectedPeopleFromRepository(CollectionOptions collectionOptions,
                                                                                           String appId,
                                                                                           String userId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.portal.model.Person> current;
        //Currently ignoring TOP FRIENDS as it hasn't been defined what a top friend is

        if (filter == null || filter.equals(PersonService.ALL_FILTER) || filter.equals(PersonService.TOP_FRIENDS_FILTER)) {
            current = repository.findAllConnectedPeople(userId);

        } else if (filter.equals(PersonService.HAS_APP_FILTER)) {
            current = repository.findAllConnectedPeople(userId, appId);

        } else if (filter.equals(PersonService.IS_WITH_FRIENDS_FILTER)) {
            current = repository.findAllConnectedPeopleWithFriend(userId, collectionOptions.getFilterValue());

        //Represents the default case (filter by field)
        } else {
            current = repository.findAllConnectedPeople(userId, filter, collectionOptions.getFilterOperation(), collectionOptions.getFilterValue());
        }
        return current;
    }

    private List<org.apache.rave.portal.model.Person> getGroupMembersFromRepository(CollectionOptions collectionOptions,
                                                                                        String groupId,
                                                                                        String appId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.portal.model.Person> current;

        if (filter == null || filter.equals(PersonService.ALL_FILTER) || filter.equals(PersonService.TOP_FRIENDS_FILTER)) {
            current = repository.findByGroup(groupId);

        } else if (filter.equals(PersonService.HAS_APP_FILTER)) {
            current = repository.findByGroup(groupId, appId);

        } else if (filter.equals(PersonService.IS_WITH_FRIENDS_FILTER)) {
            current = repository.findByGroupWithFriend(groupId, collectionOptions.getFilterValue());

        //Represents the default case (filter by field)
        } else {
            current = repository.findByGroup(groupId, filter, collectionOptions.getFilterOperation(), collectionOptions.getFilterValue());
        }
        return current;
    }

    private org.apache.rave.portal.model.Person getPersonForId(UserId id, SecurityToken token) {
        return getFromRepository(id.getUserId(token));
    }

    private org.apache.rave.portal.model.Person getFromRepository(String userId) {
        long id = Long.parseLong(userId);
        org.apache.rave.portal.model.Person person = repository.get(id);
        if (person == null) {
            throw new ProtocolException(HttpServletResponse.SC_NOT_FOUND, "The person with the id " + userId + " was not found.");
        }
        return person;
    }

    private static List<Person> convertPeople(List<org.apache.rave.portal.model.Person> people, Set<String> fields) {
        List<Person> wrappedPeople = new ArrayList<Person>();
        for (org.apache.rave.portal.model.Person person : people) {
            wrappedPeople.add(convertPerson(person, fields));
        }
        return wrappedPeople;
    }

    private static Person convertPerson(org.apache.rave.portal.model.Person person, Set<String> fields) {
        return new FieldRestrictingPerson(person, fields);
    }
}
