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

package org.apache.rave.opensocial.repository;

import org.apache.rave.portal.model.Person;
import org.apache.rave.persistence.Repository;
import org.apache.shindig.protocol.model.FilterOperation;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;

import java.util.List;


public interface PersonRepository extends Repository<Person> {
    /**
     * Gets a user by their username
     *
     *
     * @param username string representing the username
     * @return valid Person if found, null otherwise
     */
    Person findByUsername(String username);

    /**
     * Gets all people connected to the given user including friends, fellow group members, etc
     * @param username the user to find connected individuals for
     * @return valid List of people connected to the person
     */
    List<Person> findAllConnectedPeople(String username);

    /**
     * Gets all people connected to the given user including friends, fellow group members, etc who have the specified application
     * @param username the user to find connected individuals for
     * @param appId the ID of the application
     * @return a list of connected people who have the specified application installed
     */
    List<Person> findAllConnectedPeople(String username, String appId);

    /**
     * Gets all people connected to the given user including friends, fellow group members, etc, filtered by the specified field
     *
     * @param username the username of the person to query for
     * @param field the field to filter on
     * @param operation the type of filter to apply
     * @param value the value of the specified filter
     * @return a filtered list of connected individuals
     */
    List<Person> findAllConnectedPeople(String username, String field, FilterOperation operation, String value);

    /**
     * Finds a list of all people connected to the given person who are friends with the second user
     * @param username the user to find connected individuals for
     * @param friendUsername the username of the person to filter connections by
     * @return a list of people who are connected to the first user and friends with the second
     */
    List<Person> findAllConnectedPeopleWithFriend(String username, String friendUsername);

    /**
     * Finds the list of friends for a given person
     * @param username the username of the user to find friends for
     * @return a valid List of people that are explicit friends of the person
     */
    List<Person> findFriends(String username);

    /**
     * Finds a list of friends for the given person who have the given app installed on some page
     *
     * @param username the username of the user to find friends for
     * @param appId the ID of the application
     * @return a list of friends with the application
     */
    List<Person> findFriends(String username, String appId);

    /**
     * Finds the list of friends for the given person, filtered by the specified field
     *
     * @param username the username of the user to find friends for
     * @param field the field to filter on
     * @param operation the type of filter to apply
     * @param value the value of the specified filter
     * @return a filtered list of friends
     */
    List<Person> findFriends(String username, String field, FilterOperation operation, String value);

    /**
     * Finds the list of friends for the user who are also friends of the given person
     *
     * @param username the username of the user to find friends for
     * @param friendUsername the username of the person to filter connections by
     * @return a list of people who are friends of both individuals
     */
    List<Person> findFriendsWithFriend(String username, String friendUsername);

    /**
     * Finds a List of people based on their group membership
     *
     * @param groupId the Id of the group to query
     * @return a valid List of people in the group
     */
    List<Person> findByGroup(String groupId);

    /**
     * Finds a List of people based on their group membership who have the specified app
     * @param groupId the Id of the group to query
     * @param appId the ID of the application
     * @return a list of people in the group with the application
     */
    List<Person> findByGroup(String groupId, String appId);

    /**
     * Finds a subset of people in the specified group filtered by the specified field
     *
     * @param groupId the Id of the group to query
     * @param field the field to filter on
     * @param operation the type of filter to apply
     * @param value the value of the specified filter
     * @return a filtered list of group members
     */
    List<Person> findByGroup(String groupId, String field, FilterOperation operation, String value);

    /**
     * Finds a subset of people in teh specified group who have the given friend
     *
     * @param groupId the Id of the group to query
     * @param friendUsername the username of the friend to filter the group by
     * @return a list of people in the group who have the specified friend
     */
    List<Person> findByGroupWithFriend(String groupId, String friendUsername);
}

