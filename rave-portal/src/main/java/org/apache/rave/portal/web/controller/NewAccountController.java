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

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.web.validator.NewAccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class NewAccountController {

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 private final NewAccountService newAccountService;
	 private final NewAccountValidator newAccountValidator;


    @Autowired
		  public NewAccountController(NewAccountService newAccountService, NewAccountValidator newAccountValidator) {
		  this.newAccountService=newAccountService;
		  this.newAccountValidator=newAccountValidator;
    }

    @RequestMapping(value ="/newaccount.jsp")
	 public void setUpForm(ModelMap model) {
		  logger.debug("Initializing form");
		  //TODO this should use view keys like other pages.
		  model.addAttribute("newUser",new User());
	 }

    @RequestMapping(value = { "/newaccount","/newacount/*"}, method = RequestMethod.POST)
		  public String create(@ModelAttribute User user, BindingResult results, Model model,@RequestParam String username, @RequestParam String password){
		  logger.debug("Creating a new user account");
		  model.addAttribute("newUser",user);
		  
		  newAccountValidator.validate(user,results);
		  if(results.hasErrors()){
				return "newaccount";
		  }

		  //Now attempt to create the account.
		  try {
				newAccountService.createNewAccount(username,password);
				return "redirect:/";
		  }
		  
		  catch (org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
				//This exception is thrown if the account already exists.
				logger.error("Account creation failed: "+ex.getMessage());
				results.reject("Account already exists","Unable to create account");
				//TODO: change this to a viewname
				return "newaccount";
				
		  }
		  //TODO need to handle more specific exceptions
		  catch (Exception ex) {
				logger.error("Account creation failed: "+ex.getMessage());
				results.reject("Unable to create account:"+ex.getMessage(),"Unable to create account");
				//TODO: change this to a viewname
				return "newaccount";
		  }
		  
	 }
}