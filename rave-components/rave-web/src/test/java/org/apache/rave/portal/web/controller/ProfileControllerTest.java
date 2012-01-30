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

import static org.junit.Assert.*;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test class for {@link ProfileController}
 */
public class ProfileControllerTest {

private ProfileController userInfoController;
	
	private UserService userService;
	
	@Before
	public void setup() {
		userService = createMock(UserService.class);
		userInfoController = new ProfileController(userService);
	}
	
	@Test
	public void setUpForm_ShouldAddAttributeForAuthenticatedUser() {
		//creating a mock authenticated user
		final User authUser = new User();
		final ModelMap model = new ModelMap();
		final int modelSize = 3;
		final long referringPageId = 1L;
		String userProfile = new String(ModelKeys.USER_PROFILE);
		
		expect(userService.getAuthenticatedUser()).andReturn(authUser).anyTimes();
		replay(userService);
		
		userInfoController.setUpForm(model, referringPageId);
		
		//assert that the model is not null
		assertThat(model, CoreMatchers.notNullValue());
	
		//assert that the model size is three
		assertThat(model.size(), CoreMatchers.equalTo(modelSize));
		
		//assert that the model does contain an attribute associated with the authenticated user after setUpForm() is called
		assertThat(model.containsAttribute(userProfile), CoreMatchers.equalTo(true)); 
		
		//assert that the model does not contain authenticated user as null
		assertThat(model.get(userProfile), CoreMatchers.notNullValue());
		
		verify(userService);
	}
	
	@Test
	public void updateUserProfile_ShouldUpdateAuthenticatedUser() {
		//This test will just show the successful updation of user status
		final ModelMap model = new ModelMap();
		final int modelSize = 3;
		final long referringPageId = 1L;
		String userProfile = new String(ModelKeys.USER_PROFILE);
		
		//creating a mock authenticated user
		final User authUser = new User();
		//set existing status
		authUser.setStatus("Single");
		//set other paramters
		authUser.setGivenName("Test");
		authUser.setFamilyName("Rave");
		authUser.setAboutMe("Test User");
		authUser.setEmail("testuser@rave.com");
		
		//creating a mock updated user
		final User updatedUser = new User();
		//set the updated status
		updatedUser.setStatus("Married");
		updatedUser.setGivenName("Test");
		updatedUser.setFamilyName("Rave");
		updatedUser.setAboutMe("Test User");
		updatedUser.setEmail("testuser@rave.com");
		
		expect(userService.getAuthenticatedUser()).andReturn(authUser).anyTimes();
		userService.updateUserProfile(authUser);
		replay(userService);
		
		userInfoController.updateUserProfile(model, referringPageId, updatedUser);
		
		//assert that the model is not null
		assertThat(model, CoreMatchers.notNullValue());
	
		//assert that the model size is three
		assertThat(model.size(), CoreMatchers.equalTo(modelSize));
		
		//assert that the model does contain an attribute associated with the authenticated user
		assertThat(model.containsAttribute(userProfile), CoreMatchers.equalTo(true)); 
		
		//assert that the model does not contain authenticated user as null
		assertThat(model.get(userProfile), CoreMatchers.notNullValue());
		
		//assert that the status of user is updated
		assertEquals(updatedUser.getStatus(), authUser.getStatus());
		
		verify(userService);
		
	}

}
