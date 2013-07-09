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

import org.apache.rave.model.Page;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link ProfileController}
 */
public class ProfileControllerTest {

    private ProfileController profileController;

	private UserService userService;
	private PageService pageService;
    private MockHttpServletResponse response;

    private Page defaultPage, otherPage;
    private List<Page> allProfilePages;
    private List<PageLayout> allPageLayouts;

    private final String DEFAULT_PAGE_ID = "99";
    private final String OTHER_PAGE_ID = "22";
    private final String USER_ID = "1";
    private final String VALID_PAGE_LAYOUT_CODE = "layout98";
    private PageLayout validPageLayout;

	@Before
	public void setup() {
		userService = createMock(UserService.class);
		pageService = createMock(PageService.class);
        response = new MockHttpServletResponse();
		profileController = new ProfileController(userService, pageService);

        validPageLayout = new PageLayoutImpl();
        validPageLayout.setCode(VALID_PAGE_LAYOUT_CODE);

        defaultPage = new PageImpl(DEFAULT_PAGE_ID);
        defaultPage.setPageLayout(validPageLayout);
        otherPage = new PageImpl(OTHER_PAGE_ID);
        otherPage.setPageLayout(validPageLayout);

        allProfilePages = new ArrayList<Page>();
        allProfilePages.add(defaultPage);
        allProfilePages.add(otherPage);

        allPageLayouts = new ArrayList<PageLayout>();
        allPageLayouts.add(validPageLayout);
	}

	@Test
	public void viewPerson_ShouldAddAttributeForUser() {
		//creating a mock user
		final UserImpl user = new UserImpl();
		final ModelMap model = new ModelMap();
		final int modelSize = 4;
		final String username="canonical";
        user.setUsername(username);
        user.setId(USER_ID);
		String userProfile = new String(ModelKeys.USER_PROFILE);
        Page personProfile = new PageImpl();
        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode(VALID_PAGE_LAYOUT_CODE);
        personProfile.setPageLayout(pageLayout);
        List<Person> personObjects = new ArrayList<Person>();

		expect(userService.getUserByUsername(username)).andReturn(user).once();
        expect(pageService.getPersonProfilePage(user.getId())).andReturn(personProfile);
		expect(userService.getFriendRequestsReceived(username)).andReturn(personObjects);

		replay(userService, pageService);

		String view = profileController.viewProfileByUsername(username, model, null, response);

		//assert that the model is not null
		assertThat(model, CoreMatchers.notNullValue());

		//assert that the model size is four
		assertThat(model.size(), CoreMatchers.equalTo(modelSize));

		//assert that the model does contain an attribute associated with the authenticated user after setUpForm() is called
		assertThat(model.containsAttribute(userProfile), CoreMatchers.equalTo(true));

		//assert that the model does not contain authenticated user as null
		assertThat(model.get(userProfile), CoreMatchers.notNullValue());

        assertThat(view, is(ViewNames.PERSON_PROFILE + "." + VALID_PAGE_LAYOUT_CODE));

		verify(userService, pageService);
	}
	
	@Test
	public void viewPerson_userid() {
		//creating a mock user
		final UserImpl user = new UserImpl();
		final ModelMap model = new ModelMap();
		final int modelSize = 4;
		final String username="canonical";
        user.setUsername(username);
        user.setId(USER_ID);
		String userProfile = new String(ModelKeys.USER_PROFILE);
        Page personProfile = new PageImpl();
        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode(VALID_PAGE_LAYOUT_CODE);
        personProfile.setPageLayout(pageLayout);
        List<Person> personObjects = new ArrayList<Person>();

		expect(userService.getUserById(USER_ID)).andReturn(user).once();
        expect(pageService.getPersonProfilePage(user.getId())).andReturn(personProfile);
		expect(userService.getFriendRequestsReceived(username)).andReturn(personObjects);

		replay(userService, pageService);

		String view = profileController.viewProfile(USER_ID, model, null, response);

		//assert that the model is not null
		assertThat(model, CoreMatchers.notNullValue());

		//assert that the model size is four
		assertThat(model.size(), CoreMatchers.equalTo(modelSize));

		//assert that the model does contain an attribute associated with the authenticated user after setUpForm() is called
		assertThat(model.containsAttribute(userProfile), CoreMatchers.equalTo(true));

		//assert that the model does not contain authenticated user as null
		assertThat(model.get(userProfile), CoreMatchers.notNullValue());

        assertThat(view, is(ViewNames.PERSON_PROFILE + "." + VALID_PAGE_LAYOUT_CODE));

		verify(userService, pageService);
	}

    @Test
    public void viewPersonProfile_invalidUser() {
        //creating a mock user
        final User user = null;
        final ModelMap model = new ModelMap();
        final int modelSize = 4;
        final String username="Canonical";
        Page personProfile = new PageImpl();
        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode("person_profile");
        personProfile.setPageLayout(pageLayout);

        expect(userService.getUserByUsername(username)).andThrow(new UsernameNotFoundException("Username does not exist"));

        replay(userService, pageService);

        String view = profileController.viewProfileByUsername(username, model, null, response);
        assertThat(view, is(ViewNames.USER_NOT_FOUND));
        assertThat(response.getStatus(), is(HttpStatus.NOT_FOUND.value()));

        verify(userService, pageService);
    }

    @Test
    public void viewProfile_invalidUser() {
        //creating a mock user
        final User user = null;
        final ModelMap model = new ModelMap();
        final int modelSize = 4;
        final String username="Canonical";
        Page personProfile = new PageImpl();
        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode("person_profile");
        personProfile.setPageLayout(pageLayout);

        expect(userService.getUserById(username)).andThrow(new UsernameNotFoundException("Username does not exist"));

        replay(userService, pageService);

        String view = profileController.viewProfile(username, model, null, response);
        assertThat(view, is(ViewNames.USER_NOT_FOUND));
        assertThat(response.getStatus(), is(HttpStatus.NOT_FOUND.value()));

        verify(userService, pageService);
    }

	@Test
	public void updateProfile_ShouldUpdateAuthenticatedUser() {
		//This test will just show the successful updation of user status
		final ModelMap model = new ModelMap();
		final int modelSize = 2;
		final String referringPageId = "1";
        final String USERNAME = "canonical";
		String userProfile = new String(ModelKeys.USER_PROFILE);

		//creating a mock authenticated user
		final User authUser = new UserImpl();
        authUser.setUsername(USERNAME);
		//set existing status
		authUser.setStatus("Single");
		//set other paramters
		authUser.setGivenName("Test");
		authUser.setFamilyName("Rave");
		authUser.setAboutMe("Test User");
		authUser.setEmail("testuser@rave.com");

		//creating a mock updated user
		final UserForm updatedUser = new UserForm();
		//set the updated status
		updatedUser.setStatus("Married");
		updatedUser.setGivenName("Test");
		updatedUser.setFamilyName("Rave");
		updatedUser.setAboutMe("Test User");
		updatedUser.setEmail("testuser@rave.com");

		expect(userService.getAuthenticatedUser()).andReturn(authUser).anyTimes();
		userService.updateUserProfile(authUser);
		replay(userService);

		String view = profileController.updateProfile(model, referringPageId, updatedUser);

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

        assertThat(view, is("redirect:/app/person/" + USERNAME));

		verify(userService);

	}

}
