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
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping(value = { "/newaccount/*", "/newaccount" })
public class NewAccountController {

    private final UserService userService;
	 private final PageService pageService;
	 private final PageLayoutService pageLayoutService;
	 private final RegionService regionService;
	 
    @Autowired
    public NewAccountController(UserService userService, PageService pageService, PageLayoutService pageLayoutService, RegionService regionService) {
        this.userService = userService;
		  this.pageService = pageService;
		  this.pageLayoutService = pageLayoutService;
		  this.regionService = regionService;
    }

    @RequestMapping(method = RequestMethod.POST)
	 //Needs to be specified by action.
	 //TODO: have a more elegant way of adding a user
	 public String create(Model model, @RequestParam String userName, @RequestParam String password) {	  
	 //Create a new user object and register it.
		  User user=new User();
		  user.setUsername(userName);
		  user.setPassword(password);
		  user.setExpired(false);
		  user.setLocked(false);
		  user.setEnabled(true);
		  userService.registerNewUser(user);
		  
		  //Return the newly registered user
		  User registeredUser=userService.getUserByUsername(userName);
		  
		  //Create a PageLayout object.  We will default to 
		  //the two-column layout
		  PageLayout pageLayout=pageLayoutService.getPageLayoutByCode("columns_2");

		  //Create regions
		  List<Region> regions=new ArrayList<Region>();
		  Region region1=new Region();
		  Region region2=new Region();
		  regions.add(region1);
		  regions.add(region2);

		  //Create a Page object and register it.
		  Page page=new Page();
		  page.setName("main");
		  page.setOwner(registeredUser);
		  page.setPageLayout(pageLayout);
		  page.setRenderSequence(1L);
								 page.setRegions(regions);
		  pageService.registerNewPage(page);

        return "redirect:/";
    }

}