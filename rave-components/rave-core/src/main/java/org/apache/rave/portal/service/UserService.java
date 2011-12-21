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

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
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
    void setAuthenticatedUser(long userId);

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
     * @return {@link User} if one exists, otherwise {@literal null}
     */
    User getUserByUsername(String userName);

    /**
     * Return a user object by the user ID.
     *
     * @param id the user ID
     * @return {@link User} if one exists, otherwise {@literal null}
     */
    User getUserById(Long id);

    /**
     * Return a user object by the user email.
     *
     * @param userEmail email address of the user
     * @return {@link User} if one exists, otherwise {@literal null}
     */
    User getUserByEmail(String userEmail);

    /**
     * Update the user profile information.
     *
     * @param user the modified user object
     */
    void updateUserProfile(User user);

    /**
     * Gets a limited {@link SearchResult} for {@link User}'s
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<User> getLimitedListOfUsers(int offset, int pageSize);

    /**
     * Gets a {@link SearchResult} for {@link User}'s that match the search term
     *
     * @param searchTerm free text input to search on users
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<User> getUsersByFreeTextSearch(String searchTerm, int offset, int pageSize);

    /**
     * Deletes a User
     *
     * @param userId {@link Long} id if the user
     */
    void deleteUser(Long userId);

    /**
     * List of persons whom have added the supplied widget to one or more pages
     *
     * @param widgetId the entityId of the Widget to search
     * @return List of Person objects in alphabetical order sorted by familyname, givenname
     */
    List<Person> getAllByAddedWidget(long widgetId);
}