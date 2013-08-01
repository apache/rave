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

import org.apache.rave.model.Authority;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewAccountValidator;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link UserController}
 */
public class UserControllerTest {

    private static final String TABS = "tabs";
    private static final String REFERRER_ID = "35";

    private UserController controller;
    private UserService userService;
    private NewAccountService newAccountService;
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

        expect(userService.getLimitedList(offset, pageSize)).andReturn(searchResult);
        replay(userService);

        String adminUsersView = controller.viewUsers(offset, referer,REFERRER_ID, model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertTrue(model.containsAttribute(TABS));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
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

        String adminUsersView = controller.searchUsers(searchTerm, offset,REFERRER_ID, model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertEquals(searchTerm, model.asMap().get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(TABS));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }


    @Test
    public void viewAdminUserDetail() throws Exception {
        Model model = new ExtendedModelMap();
        String userid = "123";
        User user = new UserImpl(userid, "john.doe.sr");

        expect(userService.getUserById(userid)).andReturn(user);
        replay(userService);

        String adminUserDetailView = controller.viewUserDetail(userid,REFERRER_ID, model);
        verify(userService);

        assertEquals(ViewNames.ADMIN_USERDETAIL, adminUserDetailView);
        assertTrue(model.containsAttribute(TABS));
        assertEquals(user, model.asMap().get("user"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }


    @Test
    public void updateUserDetail_success() {
        ModelMap modelMap = new ExtendedModelMap();
        String userid = "123";
        final String email = "john.doe.sr@example.net";
        UserImpl user = new UserImpl(userid, "john.doe.sr");
        user.setPassword("secrect");
        user.setConfirmPassword("secrect");
        user.setEmail(email);
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");

        SessionStatus sessionStatus = createMock(SessionStatus.class);

        expect(userService.getUserByEmail(email)).andReturn(user);
        userService.updateUserProfile(user);
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, sessionStatus);

        final String view = controller.updateUserDetail(user, errors, validToken, validToken,REFERRER_ID, modelMap, sessionStatus);
        verify(userService, sessionStatus);

        assertFalse(errors.hasErrors());
        assertEquals("redirect:/app/admin/users?action=update&referringPageId=" + REFERRER_ID, view);

    }

    @Test
    public void updateUserDetail_withErrors() {
        ModelMap modelMap = new ExtendedModelMap();
        String userid = "123";
        UserImpl user = new UserImpl(userid, "john.doe.sr");
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");

        SessionStatus sessionStatus = createMock(SessionStatus.class);
        replay(sessionStatus);
        final String view = controller.updateUserDetail(user, errors, validToken, validToken,REFERRER_ID, modelMap, sessionStatus);
        verify(sessionStatus);

        assertTrue(errors.hasErrors());
        assertEquals(ViewNames.ADMIN_USERDETAIL, view);
    }

    @Test(expected = SecurityException.class)
    public void updateUserDetail_wrongToken() {
        ModelMap modelMap = new ExtendedModelMap();
        UserImpl user = new UserImpl("123", "john.doe.sr");
        final BindingResult errors = new BeanPropertyBindingResult(user, "user");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        sessionStatus.setComplete();

        expectLastCall();
        replay(sessionStatus);

        String otherToken = AdminControllerUtil.generateSessionToken();

        controller.updateUserDetail(user, errors, validToken, otherToken,REFERRER_ID, modelMap, sessionStatus);
        verify(sessionStatus);

        assertFalse("SecurityException", true);

    }
    @Test
    public void deleteUserDetail_success() {
        ModelMap modelMap = new ExtendedModelMap();
        String userid = "123";
        final String email = "john.doe.sr@example.net";
        User user = new UserImpl(userid, "john.doe.sr");
        user.setPassword("secrect");
        user.setConfirmPassword(user.getConfirmPassword());
        user.setEmail(email);

        SessionStatus sessionStatus = createMock(SessionStatus.class);

        userService.deleteUser(user.getId());
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, sessionStatus);

        final String view = controller.deleteUserDetail(user, validToken, validToken, "true",REFERRER_ID, modelMap, sessionStatus);
        verify(userService, sessionStatus);

        assertEquals("redirect:/app/admin/users?action=delete&referringPageId=" + REFERRER_ID, view);

    }

    @Test
    public void deleteUserDetail_noConfirmChecked() {
        ModelMap modelMap = new ExtendedModelMap();
        String userid = "123";
        User user = new UserImpl(userid, "john.doe.sr");

        SessionStatus sessionStatus = createMock(SessionStatus.class);
        replay(sessionStatus);
        final String view = controller.deleteUserDetail(user, validToken, validToken, null,REFERRER_ID, modelMap, sessionStatus);
        verify(sessionStatus);

        assertEquals(ViewNames.ADMIN_USERDETAIL, view);
        assertThat((String) modelMap.get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test(expected = SecurityException.class)
    public void deleteUserDetail_wrongToken() {
        ModelMap modelMap = new ExtendedModelMap();
        User user = new UserImpl("123", "john.doe.sr");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        sessionStatus.setComplete();

        expectLastCall();
        replay(sessionStatus);

        String otherToken = AdminControllerUtil.generateSessionToken();

        controller.deleteUserDetail(user, validToken, otherToken, "true",REFERRER_ID, modelMap, sessionStatus);
        verify(sessionStatus);

        assertFalse("SecurityException", true);

    }

    @Test
    public void setupForm() {
        ModelMap modelMap = new ExtendedModelMap();
        final String viewName = controller.setUpForm(modelMap,REFERRER_ID);
        assertEquals(ViewNames.ADMIN_NEW_ACCOUNT, viewName);
        assertTrue(modelMap.containsAttribute(TABS));
        assertTrue(modelMap.get(ModelKeys.NEW_USER) instanceof UserImpl);
        assertThat((String) modelMap.get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void create_ValidFormSubmitted() throws Exception {
        final Model model = createNiceMock(Model.class);
        final RedirectAttributes redirectAttributes = createNiceMock(RedirectAttributes.class);
        final UserForm User = new UserForm();
        final BindingResult errors = new BeanPropertyBindingResult(User, ModelKeys.NEW_USER);
        final String username = "username";
        final String password = "password";
        final String email = "User@example.com";
        final String confirmPassword = password;

        User.setUsername(username);
        User.setPassword(password);
        User.setConfirmPassword(confirmPassword);
        User.setEmail(email);

        expect(userService.getUserByUsername(username)).andReturn(null);
        expect(userService.getUserByEmail(email)).andReturn(null);

        newAccountService.createNewAccount(isA(User.class));

        expectLastCall();
        replay(userService, model, newAccountService, redirectAttributes);

        String result = controller.create(User, errors, model,REFERRER_ID,redirectAttributes);
        verify(userService, model, newAccountService, redirectAttributes);

        assertFalse(errors.hasErrors());
        assertEquals("redirect:/app/admin/users?referringPageId=" +REFERRER_ID, result);

    }
    @Test
    public void create_EmptyForm() throws Exception {
        final Model model = createNiceMock(Model.class);
        final RedirectAttributes redirectAttributes = createNiceMock(RedirectAttributes.class);
        final UserForm User = new UserForm();
        final BindingResult errors = new BeanPropertyBindingResult(User, ModelKeys.NEW_USER);
        final String username = "";
        final String password = "";
        final String email = "";
        final String confirmPassword = password;

        User.setUsername(username);
        User.setPassword(password);
        User.setConfirmPassword(confirmPassword);
        User.setEmail(email);

        newAccountService.createNewAccount(isA(User.class));

        replay(model);

        String result = controller.create(User, errors, model,REFERRER_ID, redirectAttributes);
        verify(model);

        assertTrue(errors.hasErrors());
        assertEquals(ViewNames.ADMIN_NEW_ACCOUNT, result);

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

        final NewAccountValidator validator = new NewAccountValidator(userService);
        controller.setNewAccountValidator(validator);

        newAccountService = createMock(NewAccountService.class);
        controller.setNewAccountService(newAccountService);
    }


    private static SearchResult<User> createSearchResultWithTwoUsers() {
        UserImpl user1 = new UserImpl("123", "john.doe.sr");
        UserImpl user2 = new UserImpl("456", "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int totalResult = 12390;
        return new SearchResult<User>(users, totalResult);
    }

    private static SearchResult<Authority> createSearchResultWithTwoAuthorities() {
        List<Authority> authorities = new ArrayList<Authority>();
        Authority foo = new AuthorityImpl();
        foo.setAuthority("FOO");
        Authority bar = new AuthorityImpl();
        bar.setAuthority("BAR");
        authorities.add(foo);
        authorities.add(bar);
        return new SearchResult<Authority>(authorities, authorities.size());
    }
}
