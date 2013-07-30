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

package org.apache.rave.portal.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.rave.repository.Repository;
import org.apache.rave.model.Person;



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
     * Finds a subset of people in teh specified group who have the given friend
     *
     * @param groupId the Id of the group to query
     * @param friendUsername the username of the friend to filter the group by
     * @return a list of people in the group who have the specified friend
     */
    List<Person> findByGroupWithFriend(String groupId, String friendUsername);

    /**
     * Registers a relationship between two users.
     *
     * @param friendUsername is the username of the user who receives the friend request
     * @param username is the user who sends the friend request
     * @return true is the relationship request was successful
     */
	boolean addFriend(String friendUsername, String username);

    /**
     * Removes the relationship between two users.
     *
     * @param friendUsername is the user who is removed from the friends list of user
     * @param username is the user who wants to remove a friend.
     */
	void removeFriend(String friendUsername, String username);

    /**
     * Get the friends and friends requests of a particular user.
     *
     * @param user is the user whose friends and requests are to be found
     * @return A hashmap which contains 3 lists of Person objects
     * 			1 containing friends
     * 			2 containing friend requests sent by the current user
     * 			3 containing friend requests received by the current user
     */
	HashMap<String, List<Person>> findFriendsAndRequests(String username);

    /**
     * Incoming friend request is accepted by the user
     *
     * @param friendUsername is the username of the user who sent the friend request
     * @param username is the username of the user who receives the friend request(current user)
     * @return true is the request was successful accepted
     */
	boolean acceptFriendRequest(String friendUsername, String username);

    /**
     * Finds the list of friend requests received by the user
     * 
     * @param username the username of the user who receives the friend request
     * @return a valid List of people who sent friend request to the current user
     */
	List<Person> findFriendRequestsReceived(String username);

    /**
     * Finds the list of friend requests sent by the user
     * 
     * @param username the username of the user who sent the friend request
     * @return a valid List of people to whom friend requests were sent
     */
	List<Person> findFriendRequestsSent(String username);

    /**
     * Deletes all the associations of a particular user with other users
     * 
     * @param userid is the userid of the user whose associations are to be deleted
     * @return count of the association items deleted
     */
	int removeAllFriendsAndRequests(String userid);
}

