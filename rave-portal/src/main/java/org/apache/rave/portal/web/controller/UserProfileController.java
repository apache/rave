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

import java.util.List;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserProfileController {

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 private final UserService userService;
	 private final UserProfileValidator userProfileValidator;


    @Autowired
	 public UserProfileController(UserService userService, UserProfileValidator userProfileValidator) {
		  this.userService=userService;
		  this.userProfileValidator=userProfileValidator;
    }

    @RequestMapping(value ="/userProfile.jsp")
	 public void setUpForm(ModelMap model) {
		  logger.debug("Initializing form");
		  User user=userService.getAuthenticatedUser();
		  model.addAttribute(ModelKeys.USER_PROFILE,user);
	 }

    @RequestMapping(value = { "/updateUserProfile","/updateUserProfile/*"}, method = RequestMethod.POST)
	 public String create(@ModelAttribute User user, BindingResult results, Model model,@RequestParam String username, @RequestParam String password){
		  logger.debug("Updating user profile.");
		  model.addAttribute(ModelKeys.USER_PROFILE,user);
		  
		  userProfileValidator.validate(user,results);
		  if(results.hasErrors()){
			  logger.error("newaccount.jsp: shows validation errors");
			  //TODO: change this to a viewname (done)
			  return ViewNames.USER_PROFILE;
		  }

		  //Now attempt to create the account.
		  try {
			    logger.debug("userprofile: passed form validation");
			    
			    userService.updateUserProfile(user);
			    //TODO: change this to a viewname (done)
				return ViewNames.REDIRECT;
		  }
		  
		  catch (org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
				//This exception is thrown if the account already exists.
				logger.error("Account creation failed: "+ex.getMessage());
				results.reject("Account already exists","Unable to create account");
				//TODO: change this to a viewname (done)
				return ViewNames.USER_PROFILE;
				
		  }
		  //TODO need to handle more specific exceptions
		  catch (Exception ex) {
				logger.error("Account creation failed: "+ex.getMessage());
				results.reject("Unable to create account:"+ex.getMessage(),"Unable to create account");
				//TODO: change this to a viewname (done)
				return ViewNames.USER_PROFILE;
		  }
	 }
}