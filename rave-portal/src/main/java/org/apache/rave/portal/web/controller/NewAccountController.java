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

import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
//@RequestMapping(value = { "/newaccount/*", "/newaccount" })
public class NewAccountController {

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 private final NewAccountService newAccountService;

    @Autowired
    public NewAccountController(NewAccountService newAccountService) {
		  this.newAccountService=newAccountService;
    }

    @RequestMapping(value ="/newaccount.jsp", method = RequestMethod.GET)
	 public String setUpForm(ModelMap model) {
		  logger.debug("Initializing form");
		  //TODO this should use view keys like other pages.
		  return "newaccount";
	 }

    @RequestMapping(value = { "/newaccount/*","/newaccount"}, method = RequestMethod.POST)
	 public String create(Model model,@RequestParam String userName, @RequestParam String password, @RequestParam String passwordConfirm) {	  
			logger.debug("Creating a new user account");

			 try {
			    //TODO need to validate input, Spring-style
				 newAccountService.createNewAccount(userName,password);
				 return "redirect:/";
			 }
											 
			  //TODO need to handle more specific exceptions
			  catch (Exception ex) {
				  logger.error("Account creation failed: "+ex.getMessage());
				  //TODO: change this to a viewname
				  return "newaccount";
			  }
	 
		 }
}