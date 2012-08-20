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
package org.apache.rave.portal.web.api.rpc;

import java.util.HashMap;
import java.util.List;

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Defines RPC operations for adding and removing friends
 */
@Controller(value="rpcPersonApi")
@RequestMapping(value = "/api/rpc/person/*")
public class PersonApi {

    private final UserService userService;

    @Autowired
    public PersonApi(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "getFriends", method = RequestMethod.POST)
    public RpcResult<HashMap<String, List<Person>>> getFriends() {
        return new RpcOperation<HashMap<String, List<Person>>>() {
            @Override
            public HashMap<String, List<Person>> execute() {
                return userService.getFriends(userService.getAuthenticatedUser().getUsername());
            }
        }.getResult();
    }

    /**
     * Adds a friend to the current user
     *
     * @param friendId
     *            the userID of the user to add as a friend of the current user
     * @return true if adding friend was successful or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(value = "{friendId}/addfriend", method = RequestMethod.POST)
    public RpcResult<Boolean> addFriend(@PathVariable final String friendId) {
    	return new RpcOperation<Boolean>() {
    		@Override
    		public Boolean execute() {
    			boolean result = userService.addFriend(friendId, userService.getAuthenticatedUser().getId().toString());
    			return result;
    		}
    	}.getResult();
    }

    /**
     * Removes a user from the friends list of a user
     *
     * @param friendId
     *            the userID of the user to remove from the friend list of current user
     * @return true if removing friend was successful or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(value = "{friendId}/removefriend", method = RequestMethod.POST)
    public RpcResult<Boolean> removeFriend(@PathVariable final String friendId) {
    	return new RpcOperation<Boolean>() {
    		@Override
    		public Boolean execute() {
    			userService.removeFriend(friendId, userService.getAuthenticatedUser().getId().toString());
    			return true;
    		}
    	}.getResult();
    }
}