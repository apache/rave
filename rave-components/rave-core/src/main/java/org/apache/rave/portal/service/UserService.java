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

package org.apache.rave.portal.service;

import java.util.HashMap;
import java.util.List;

import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.rest.model.SearchResult;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.openid.OpenIDAuthenticationToken;

public interface UserService extends UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    /**
     * Get the currently authenticated user.
     *
     * @return The authenticated user.
     */
    User getAuthenticatedUser();

    /**
     * Set the currently authenticated user to the user with a given userId.
     *
     * @param userId the unique id of the use
     */
    void setAuthenticatedUser(String userId);

    /**
     * Un-sets the currently authenticated user
     */
    void clearAuthenticatedUser();

    /**
     * Registers a new user object.
     *
     * @param user the new user object to register with the data management system.
     */
    void registerNewUser(User user);

    /**
     * Return the requested user object using the user's name.
     *
     * @param userName (unique) name of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
    User getUserByUsername(String userName);

    /**
     * Return a user object by the user ID.
     *
     * @param id the user ID
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
    User getUserById(String id);

    /**
     * Return a user object by the user email.
     *
     * @param userEmail email address of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
    User getUserByEmail(String userEmail);

    /**
     * Update the user profile information.
     *
     * @param user the modified user object
     */
    void updateUserProfile(User user);

    //TODO: Put correct spring security annotations on following three methods (getAll, getLimitedList, getCountAll)
    /**
     * Gets a {@link org.apache.rave.rest.model.SearchResult} for {@link User}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<User> getAll();

    /**
     * Gets a limited {@link SearchResult} for {@link org.apache.rave.model.User}'s
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<User> getLimitedList(int offset, int pageSize);

    /**
     * Gets a {@link SearchResult} for {@link org.apache.rave.model.User}'s that match the search term
     *
     * @param searchTerm free text input to search on users
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<User> getUsersByFreeTextSearch(String searchTerm, int offset, int pageSize);

    /**
     * Deletes a User
     *
     * @param userId {@link Long} id if the user
     */
    void deleteUser(String userId);

    /**
     * List of persons whom have added the supplied widget to one or more pages
     *
     * @param widgetId the entityId of the Widget to search
     * @return List of Person objects in alphabetical order sorted by familyname, givenname
     */
    List<Person> getAllByAddedWidget(String widgetId);

    /**
     * Sends an email which contains link for changing user password
     *
     * @param user the {@link org.apache.rave.model.User} which requested password change
     */
    void sendPasswordReminder(User user);

    /**
     * Sends an email which contains username
     *
     * @param user the {@link org.apache.rave.model.User} which requested username reminder
     */
    void sendUserNameReminder(User user);


    /**
     * Changes password for given user
     *
     * @param user the {@link org.apache.rave.model.User} which requested password change
     * @throws Exception in case something goes wrong
     */
    void updatePassword(User user);

    /**
     * Check if username/email reminder request is still valid (not expired)
     *
     * @param forgotPasswordHash hash provided by user
     * @return true if forgotPasswordHash is stil within given time range
     * @throws Exception in case something goes wrong
     */
    boolean isValidReminderRequest(String forgotPasswordHash, int nrOfMinutesValid);

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
	HashMap<String, List<Person>> getFriendsAndRequests(String username);

    /**
     * Finds the list of friend requests received by the user
     * @param username the username of the user who receives the friend request
     * @return a valid List of people who sent friend request to the current user
     */
	List<Person> getFriendRequestsReceived(String username);

    /**
     * Incoming friend request is accepted by the user
     *
     * @param friendUsername is the username of the user who sent the friend request
     * @param username is the username of the user who receives the friend request(current user)
     * @return true is the request was successful accepted
     */
	boolean acceptFriendRequest(String friendUsername, String username);


    /**
     * Return a user object by the openId.
     *
     * @param openId OpenId url of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
	User getUserByOpenId(String openId);

    /**
     * Gets a limited {@link SearchResult} for {@link org.apache.rave.model.Person}'s
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Person> getLimitedListOfPersons(int offset, int pageSize);

    /**
     * Gets a {@link SearchResult} for {@link org.apache.rave.model.Person}'s that match the search term
     *
     * @param searchTerm free text input to search on users
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Person> getPersonsByFreeTextSearch(String searchTerm, int offset, int pageSize);

}
