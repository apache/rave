/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.rest.impl;

import org.apache.rave.portal.service.UserService;
import org.apache.rave.rest.UsersResource;
import org.apache.rave.rest.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class DefaultUsersResource implements UsersResource {
    private static final Logger log = LoggerFactory.getLogger(DefaultUsersResource.class);

    public static final String SELF = "@self";
    private UserService userService;

    @Override
    public Response getUsers() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Response getUser(String id) {
        String userId = SELF.equals(id) ? userService.getAuthenticatedUser().getId() : id;
        org.apache.rave.model.User user = userService.getUserById(userId);
        if (user != null) {
            return Response.ok(new User(user)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response updateUser(String id, User newUser) {
        org.apache.rave.model.User user = userService.getUserById(id);
        if (user != null) {
            newUser.saveToUser(user);
            userService.updateUserProfile(user);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
