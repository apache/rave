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

package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test for {@link UserController}
 */
public class UserControllerTest {

    private static final String TABS = "tabs";

    private UserController controller;
    private UserService userService;
    private AuthorityService authorityService;
    private PortalPreferenceService preferenceService;
    private String validToken;

    @Test
    public void adminUsers() throws Exception {
        Model model = new ExtendedModelMap();
        final int offset = 0;
        final int pageSize = 10;
        final String referer = "http://example.com/index.html";
        SearchResult<User> searchResult = createSearchResultWithTwoUsers();

        expect(userService.getLimitedListOfUsers(offset, pageSize)).andReturn(searchResult);
        replay(userService);

        String adminUsersView = controller.viewUsers(offset, referer, model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertTrue(model.containsAttribute(TABS));
    }

    @Test
    public void searchUsers() throws Exception {
        Model model = new ExtendedModelMap();

        final String searchTerm = "Doe";
        final int offset = 0;
        final int pageSize = 10;
        SearchResult<User> searchResult = createSearchResultWithTwoUsers();

        expect(userService.getUsersByFreeTextSearch(searchTerm, offset, pageSize)).andReturn(searchResult);
        replay(userService);

        String adminUsersView = controller.searchUsers(searchTerm, offset, model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertEquals(searchTerm, model.asMap().get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(TABS));
    }


    @Test
    public void viewAdminUserDetail() throws Exception {
        Model model = new ExtendedModelMap();
        Long userid = 123L;
        User user = new User(userid, "john.doe.sr");

        expect(userService.getUserById(userid)).andReturn(user);
        replay(userService);

        String adminUserDetailView = controller.viewUserDetail(userid, model);
        verify(userService);
        
        assertEquals(ViewNames.ADMIN_USERDETAIL, adminUserDetailView);
        assertTrue(model.containsAttribute(TABS));
        assertEquals(user, model.asMap().get("user"));
    }


    @Test
    public void updateUserDetail_success() {
        ModelMap modelMap = new ExtendedModelMap();
        final Long userid = 123L;
        final String email = "john.doe.sr@example.net";
        User user = new User(userid, "john.doe.sr");
        user.setPassword("secrect");
        user.setConfirmPassword(user.getConfirmPassword());
        user.setEmail(email);
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");

        SessionStatus sessionStatus = createMock(SessionStatus.class);

        expect(userService.getUserByEmail(email)).andReturn(user);
        userService.updateUserProfile(user);
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, sessionStatus);

        final String view = controller.updateUserDetail(user, errors, validToken, validToken, modelMap, sessionStatus);
        verify(userService, sessionStatus);

        assertFalse(errors.hasErrors());
        assertEquals("redirect:/app/admin/users?action=update", view);
    }

    @Test
    public void updateUserDetail_withErrors() {
        ModelMap modelMap = new ExtendedModelMap();
        Long userid = 123L;
        User user = new User(userid, "john.doe.sr");
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");

        SessionStatus sessionStatus = createMock(SessionStatus.class);
        replay(sessionStatus);
        final String view = controller.updateUserDetail(user, errors, validToken, validToken, modelMap, sessionStatus);
        verify(sessionStatus);

        assertTrue(errors.hasErrors());
        assertEquals(ViewNames.ADMIN_USERDETAIL, view);
    }

    @Test(expected = SecurityException.class)
    public void updateUserDetail_wrongToken() {
        ModelMap modelMap = new ExtendedModelMap();
        User user = new User(123L, "john.doe.sr");
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        sessionStatus.setComplete();

        expectLastCall();
        replay(sessionStatus);

        String otherToken = AdminControllerUtil.generateSessionToken();

        controller.updateUserDetail(user, errors, validToken, otherToken, modelMap, sessionStatus);
        verify(sessionStatus);

        assertFalse("SecurityException", true);

    }
    @Test
    public void deleteUserDetail_success() {
        ModelMap modelMap = new ExtendedModelMap();
        final Long userid = 123L;
        final String email = "john.doe.sr@example.net";
        User user = new User(userid, "john.doe.sr");
        user.setPassword("secrect");
        user.setConfirmPassword(user.getConfirmPassword());
        user.setEmail(email);

        SessionStatus sessionStatus = createMock(SessionStatus.class);

        userService.deleteUser(user.getEntityId());
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, sessionStatus);

        final String view = controller.deleteUserDetail(user, validToken, validToken, "true", modelMap, sessionStatus);
        verify(userService, sessionStatus);

        assertEquals("redirect:/app/admin/users?action=delete", view);
    }

    @Test
    public void deleteUserDetail_noConfirmChecked() {
        ModelMap modelMap = new ExtendedModelMap();
        Long userid = 123L;
        User user = new User(userid, "john.doe.sr");

        SessionStatus sessionStatus = createMock(SessionStatus.class);
        replay(sessionStatus);
        final String view = controller.deleteUserDetail(user, validToken, validToken, null, modelMap, sessionStatus);
        verify(sessionStatus);

        assertEquals(ViewNames.ADMIN_USERDETAIL, view);
    }

    @Test(expected = SecurityException.class)
    public void deleteUserDetail_wrongToken() {
        ModelMap modelMap = new ExtendedModelMap();
        User user = new User(123L, "john.doe.sr");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        sessionStatus.setComplete();

        expectLastCall();
        replay(sessionStatus);

        String otherToken = AdminControllerUtil.generateSessionToken();

        controller.deleteUserDetail(user, validToken, otherToken, "true", modelMap, sessionStatus);
        verify(sessionStatus);

        assertFalse("SecurityException", true);

    }

    @Test
    public void getAuthoritiesForModelMap() {
        final SearchResult<Authority> authorities = createSearchResultWithTwoAuthorities();
        expect(authorityService.getAllAuthorities()).andReturn(authorities);
        replay(authorityService);
        final SearchResult<Authority> result = controller.populateAuthorityList();
        verify(authorityService);
        assertEquals(authorities, result);
    }

    @Before
    public void setUp() throws Exception {
        controller = new UserController();

        userService = createMock(UserService.class);
        controller.setUserService(userService);

        preferenceService = createMock(PortalPreferenceService.class);
        controller.setPreferenceService(preferenceService);

        authorityService = createMock(AuthorityService.class);
        controller.setAuthorityService(authorityService);

        UserProfileValidator userProfileValidator = new UserProfileValidator(userService);
        controller.setUserProfileValidator(userProfileValidator);
        validToken = AdminControllerUtil.generateSessionToken();
    }


    private static SearchResult<User> createSearchResultWithTwoUsers() {
        User user1 = new User(123L, "john.doe.sr");
        User user2 = new User(456L, "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int totalResult = 12390;
        return new SearchResult<User>(users, totalResult);
    }

    private static SearchResult<Authority> createSearchResultWithTwoAuthorities() {
        List<Authority> authorities = new ArrayList<Authority>();
        Authority foo = new Authority();
        foo.setAuthority("FOO");
        Authority bar = new Authority();
        bar.setAuthority("BAR");
        authorities.add(foo);
        authorities.add(bar);
        return new SearchResult<Authority>(authorities, authorities.size());
    }
}
