/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.web.api.rpc;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(value = "rpcUserApi")
@RequestMapping(value = "/api/rpc/users/*")
public class UserApi {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "get")
    public RpcResult<SearchResult<User>> viewUsers(@RequestParam final int offset) {
        return new RpcOperation<SearchResult<User>>() {
            @Override
            public SearchResult<User> execute() {
                return userService.getLimitedListOfUsers(offset, DEFAULT_PAGE_SIZE);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public RpcResult<SearchResult<User>> searchUsers(@RequestParam final String searchTerm, @RequestParam final int offset) {
        return new RpcOperation<SearchResult<User>>() {
            @Override
            public SearchResult<User> execute() {
                return userService.getUsersByFreeTextSearch(searchTerm, offset, DEFAULT_PAGE_SIZE);
            }
        }.getResult();
    }

}
