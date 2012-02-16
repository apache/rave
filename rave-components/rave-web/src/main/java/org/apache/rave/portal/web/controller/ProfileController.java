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

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/userInfo/*", "/userInfo", "/userInfo/"})
public class ProfileController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final UserService userService;
				    
	@Autowired
	public ProfileController(UserService userService) {
		this.userService = userService;
	}

    /**
	 * Views the main page of another user's profile
	 *
     * @param username			    username (allows for a period in the username)
     * @param model                 {@link Model} map
     * @param referringPageId		page reference id (optional)
	 * @return the view name of the user profile page
	 */
	@RequestMapping(value = {"/person/{username:.*}"}, method = RequestMethod.GET)
	public String setUpPersonProfile(@PathVariable String username, ModelMap model, @RequestParam(required=false) Long referringPageId) {
		logger.debug("Initializing User Info page");

		User user = userService.getUserByUsername(username);

		//set the posts tag page as default for first setup
        addAttributesToModel(model, user, referringPageId, ViewNames.ABOUT_TAG_PAGE);

		return ViewNames.ALL_USER_INFO;
	}
	
	/**
	 * Views the main page of the user profile
	 *
	 * @param model           			{@link Model} map
	 * @param referringPageId			page reference id
	 * @return the view name of the user profile page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String setUpForm(ModelMap model, 
							@RequestParam Long referringPageId) {
		logger.debug("Initializing User Info page");
		
		User user = userService.getAuthenticatedUser();
		
		//set the posts tag page as default for first setup 
        addAttributesToModel(model, user, referringPageId, ViewNames.ABOUT_TAG_PAGE);

		return ViewNames.USER_INFO;
	}
	
	/**
	 * Updates the user's personal information
	 *
	 * @param model           			{@link Model} map
	 * @param referringPageId			page reference id
	 * @param updatedUser				Updated user information 
	 * @return the view name of the user profile page
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String updateUserProfile(ModelMap model, 
									@RequestParam Long referringPageId,
									@ModelAttribute("updatedUser") User updatedUser) {
		logger.debug("Updating User's profile information");
		
		User user = userService.getAuthenticatedUser();
				                
        //set the updated fields for optional information
		user.setGivenName(updatedUser.getGivenName());
		user.setFamilyName(updatedUser.getFamilyName());
		user.setDisplayName(updatedUser.getDisplayName());
		user.setAboutMe(updatedUser.getAboutMe());
		user.setStatus(updatedUser.getStatus());
		user.setEmail(updatedUser.getEmail());
		
		//update the user profile
		userService.updateUserProfile(user);
			        
		//set about tag page as default page for the changes to be visible
		addAttributesToModel(model, user, referringPageId, ViewNames.ABOUT_TAG_PAGE);
		
		return ViewNames.USER_INFO;
	}
	
	/*
	 * Function to add attributes to model map
	 */
	private void addAttributesToModel(ModelMap model, User user, Long referringPageId, String defaultTagPage) {
    	model.addAttribute(ModelKeys.USER_PROFILE, user);
    	model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
    	model.addAttribute(ModelKeys.DEFAULT_TAG_PAGE, defaultTagPage);
    }
}
