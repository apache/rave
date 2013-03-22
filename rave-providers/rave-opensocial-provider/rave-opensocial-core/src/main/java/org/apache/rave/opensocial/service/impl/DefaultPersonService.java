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
import org.apache.rave.opensocial.repository.OpenSocialPersonRepository;
import org.apache.rave.opensocial.service.SimplePersonService;
import org.apache.rave.util.CollectionUtils;
import org.apache.shindig.auth.AbstractSecurityToken;
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
import java.util.logging.Logger;

/**
 * Implementation of the {@link PersonService} SPI
 */
@Service
public class DefaultPersonService implements PersonService, SimplePersonService {

    private final OpenSocialPersonRepository repository;
    private static Logger log = Logger.getLogger(DefaultPersonService.class.getName());

    @Autowired
    public DefaultPersonService(OpenSocialPersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds,
                                                       GroupId groupId,
                                                       CollectionOptions collectionOptions,
                                                       Set<String> fields,
                                                       SecurityToken token) throws ProtocolException {

    	collectionOptions = manipulateCollectionOptions(collectionOptions,token);
        List<org.apache.rave.model.Person> people = getPeople(userIds, groupId, collectionOptions, token);
        return ImmediateFuture.newInstance(new RestfulCollection<Person>(convertPeople(people, fields)));
    }

    @Override
    public Future<Person> getPerson(UserId id, Set<String> fields, SecurityToken token) throws ProtocolException {

        return ImmediateFuture.newInstance(convertPerson(getPersonForId(id, token), fields));
    }

    @Override
    public Future<Person> updatePerson(UserId id, Person person, SecurityToken token) throws ProtocolException {
        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public List<org.apache.rave.model.Person> getPeople(Set<UserId> userIds, GroupId groupId,
                                                                   CollectionOptions collectionOptions,
                                                                   SecurityToken token) {
        switch (groupId.getType()) {
            case all:
                return getUniqueListOfConnectedPeople(userIds, collectionOptions, token);
            case friends:
                return getUniqueListOfFriends(userIds, collectionOptions, token);
            case objectId:
                return getGroupMembersFromRepository(collectionOptions, groupId.getObjectId().toString(), token.getAppId());
            case self:
                UserId id = userIds.size() == 1 ? userIds.iterator().next() : new UserId(UserId.Type.me, null);
                return Lists.newArrayList(getPersonForId(id, token));
            case custom:
                throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Custom GroupIDs are not tracked by the container");
            default:
                throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Invalid group id specified by request");
        }
    }

    private List<org.apache.rave.model.Person> getUniqueListOfFriends(Set<UserId> userIds,
                                                                                 CollectionOptions collectionOptions,
                                                                                 SecurityToken token) {

        List<org.apache.rave.model.Person> people = new ArrayList<org.apache.rave.model.Person>();
        for (UserId id : userIds) {
            org.apache.rave.model.Person person = this.getPersonForId(id, token);
            CollectionUtils.addUniqueValues(getFriendsFromRepository(collectionOptions, token.getAppId(), person.getUsername()), people);
        }
        return people;
    }

    private List<org.apache.rave.model.Person> getUniqueListOfConnectedPeople(Set<UserId> userIds,
                                                                                         CollectionOptions collectionOptions,
                                                                                         SecurityToken token) {

        List<org.apache.rave.model.Person> people = new ArrayList<org.apache.rave.model.Person>();
        for (UserId id : userIds) {
        	 org.apache.rave.model.Person person = this.getPersonForId(id, token);
            CollectionUtils.addUniqueValues(getConnectedPeopleFromRepository(collectionOptions, token.getAppId(), person.getUsername()), people);
        }
        return people;
    }

    private List<org.apache.rave.model.Person> getFriendsFromRepository(CollectionOptions collectionOptions,
                                                                                   String appId,
                                                                                   String userId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.model.Person> current;
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

    private List<org.apache.rave.model.Person> getConnectedPeopleFromRepository(CollectionOptions collectionOptions,
                                                                                           String appId,
                                                                                           String userId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.model.Person> current;
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

    private List<org.apache.rave.model.Person> getGroupMembersFromRepository(CollectionOptions collectionOptions,
                                                                                        String groupId,
                                                                                        String appId) {

        String filter = collectionOptions == null ? null : collectionOptions.getFilter();
        List<org.apache.rave.model.Person> current;

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

    private org.apache.rave.model.Person getPersonForId(UserId id, SecurityToken token) {

        return getFromRepository(id.getUserId(token));
    }

    private org.apache.rave.model.Person getFromRepository(String userId) {

        org.apache.rave.model.Person person = repository.findByUsername(userId);
        if (person == null) {
            throw new ProtocolException(HttpServletResponse.SC_NOT_FOUND, "The person with the id " + userId + " was not found.");
        }
        return person;
    }

    private static List<Person> convertPeople(List<org.apache.rave.model.Person> people, Set<String> fields) {
        List<Person> wrappedPeople = new ArrayList<Person>();
        for (org.apache.rave.model.Person person : people) {
            wrappedPeople.add(convertPerson(person, fields));
        }
        return wrappedPeople;
    }

    private static Person convertPerson(org.apache.rave.model.Person person, Set<String> fields) {
        return new FieldRestrictingPerson(person, fields);
    }

    private CollectionOptions manipulateCollectionOptions(CollectionOptions options, SecurityToken token) {
    	if(options!=null && options.getFilterValue()!=null) {
            if(options.getFilterValue().equalsIgnoreCase(AbstractSecurityToken.Keys.OWNER.name()))
            	options.setFilterValue(token.getOwnerId());
            else if(options.getFilterValue().equalsIgnoreCase(AbstractSecurityToken.Keys.VIEWER.name()))
            	options.setFilterValue(token.getViewerId());
    	}
    	return options;
    }
}
