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

import org.apache.rave.repository.Repository;
import org.apache.rave.model.User;


import java.util.List;

public interface UserRepository extends Repository<User> {

    /**
     * Gets a {@link org.apache.rave.model.User} by its username
     *
     * @param username the (unique) name of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
    User getByUsername(String username);

    /**
     * Gets a {@link org.apache.rave.model.User} by its email address
     *
     * @param userEmail the (unique) email address of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
    User getByUserEmail(String userEmail);
    
    /**
     * Gets a {@link org.apache.rave.model.User} by openId
     *
     * @param openId the (unique) openIdURL of the user
     * @return {@link org.apache.rave.model.User} if one exists, otherwise {@literal null}
     */
	User getByOpenId(String openId);

    /**
     * List of {@link org.apache.rave.model.User}'s with a limited resultset
     *
     * @param offset   start point within the total resultset
     * @param pageSize maximum number of items to be returned (for paging)
     * @return a List of Users with of at most the number of items in pageSize
     */
    List<User> getLimitedList(int offset, int pageSize);

    /**
     * @return the total number of {@link org.apache.rave.model.User}'s in the repository. Useful for paging.
     */
    int getCountAll();

    /**
     * List of {@link org.apache.rave.model.User}'s that match a searchterm in their username or email address
     *
     * @param searchTerm search term
     * @param offset     start point within the total resultset
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return a List of Users with of at most the number of items in pageSize
     */
    List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize);

    /**
     *
     * @param searchTerm search term
     * @return the total number of {@link org.apache.rave.model.User}'s that match a searchterm in their username or email address.
     *         Useful for paging.
     */
    int getCountByUsernameOrEmail(String searchTerm);

    /**
     * List of users whom have added the supplied widget to one or more pages
     *
     * @param widgetId the entityId of the Widget to search
     * @return List of User objects in alphabetical order sorted by familyname, givenname
     */
    List<User> getAllByAddedWidget(String widgetId);

    /**
     * Gets a {@link org.apache.rave.model.User} by generated forgot email hash
     *
     * @param hash unique generated hash
     * @return {@link org.apache.rave.model.User} if one exists for given hash, otherwise {@literal null}
     */
    User getByForgotPasswordHash(String hash);

}

