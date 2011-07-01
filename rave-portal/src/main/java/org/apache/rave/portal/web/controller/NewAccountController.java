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

package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//probably should be deleted
import java.util.Random;

@Controller
@RequestMapping(value = { "/newaccount/*", "/newaccount" })
public class NewAccountController {

    private final UserService userService;

    @Autowired
    public NewAccountController(UserService userService) {
        System.out.println("New Account Controller constructed");
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
	 //Needs to be specified by action.
    public String create(Model model, @RequestParam String userName, @RequestParam String password) {
        // Must be implemented
								 
								 System.out.println("===============Creating New Account================");
								 System.out.println("Username and password:"+userName+" "+password);
								 User user=new User();
								 user.setUsername(userName);
								 user.setPassword(password);
								 user.setExpired(false);
								 user.setLocked(false);
								 user.setEnabled(true);
								 userService.registerNewUser(user);
								 
        return "redirect:login.jsp";
    }
		  

}