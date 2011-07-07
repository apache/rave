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

import org.apache.rave.portal.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

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
     * @param userId the unique id of the user
     */
    void setAuthenticatedUser(long userId);

    /**
     * Un-sets the currently authenticated user
     */
    void clearAuthenticatedUser();

	 /**
	  * Registers a new user object.
	  * @param user the new user object to register with the data management system.
	  */
	 void registerNewUser(User user);

	 /**
	  * Return the requested user object using the user's name.
	  */ 
	 User getUserByUsername(String userName);
	 
	 /**
	  * Return a user object by the user ID.
	  */
	 User getUserById(Long id);
}