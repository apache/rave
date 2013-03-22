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

import org.apache.rave.model.Person;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.shindig.protocol.model.FilterOperation;

import java.util.List;


public interface OpenSocialPersonRepository extends PersonRepository {
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
     * Finds a subset of people in the specified group filtered by the specified field
     *
     * @param groupId the Id of the group to query
     * @param field the field to filter on
     * @param operation the type of filter to apply
     * @param value the value of the specified filter
     * @return a filtered list of group members
     */
    List<Person> findByGroup(String groupId, String field, FilterOperation operation, String value);
}

