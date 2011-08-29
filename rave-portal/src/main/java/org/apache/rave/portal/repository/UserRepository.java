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

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.User;

public interface UserRepository extends Repository<User> {

    /**
     * Gets a {@link User} by its username
     *
     * @param username the (unique) name of the user
     * @return {@link User} if one exists, otherwise {@literal null}
     */
    User getByUsername(String username);

    /**
     * Gets a {@link User} by its email address
     *
     * @param userEmail the (unique) email address of the user
     * @return {@link User} if one exists, otherwise {@literal null}
     */
    User getByUserEmail(String userEmail);
}
