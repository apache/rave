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

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.*;
import org.apache.rave.portal.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class DefaultUserServiceTest {

    private static final Long USER_ID = 1234L;
    private UserService service;
    private UserRepository userRepository;
    private PageRepository pageRepository;
    private PageTemplateRepository pageTemplateRepository;
    private WidgetCommentRepository widgetCommentRepository;
    private WidgetRatingRepository widgetRatingRepository;
    private WidgetRepository widgetRepository;

    private static final String USER_NAME = "1234";
    private static final String USER_EMAIL = "test@test.com";
    private static final Long VALID_WIDGET_ID = 1L;
    private static final Long INVALID_USER_ID = -9999L;

    @Before
    public void setup() {
        userRepository = createMock(UserRepository.class);
        pageRepository = createMock(PageRepository.class);
        pageTemplateRepository = createMock(PageTemplateRepository.class);
        widgetCommentRepository = createMock(WidgetCommentRepository.class);
        widgetRatingRepository = createMock(WidgetRatingRepository.class);
        widgetRepository = createMock(WidgetRepository.class);

        service = new DefaultUserService(pageRepository, userRepository, widgetRatingRepository, widgetCommentRepository, widgetRepository, pageTemplateRepository);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getAuthenticatedUser_validUser() {
        final User authUser = new User(USER_ID);
        AbstractAuthenticationToken auth = createNiceMock(AbstractAuthenticationToken.class);
        expect(auth.getPrincipal()).andReturn(authUser).anyTimes();
        replay(auth);

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        User result = service.getAuthenticatedUser();

        assertThat(result, is(sameInstance(authUser)));
        verify(auth);
    }

    @Test(expected = SecurityException.class)
    public void getAuthenticatedUser_nullAuth() {

        SecurityContext context = new SecurityContextImpl();
        SecurityContextHolder.setContext(context);
        service.getAuthenticatedUser();
    }

    @Test(expected = SecurityException.class)
    public void getAuthenticatedUser_wrongPrincipalType() {
        AbstractAuthenticationToken auth = createNiceMock(AbstractAuthenticationToken.class);
        expect(auth.getPrincipal()).andReturn(USER_ID).anyTimes();
        replay(auth);

        SecurityContext context = new SecurityContextImpl();
        SecurityContextHolder.setContext(context);

        service.getAuthenticatedUser();
        verify(auth);
    }
    

    @Test
    public void setAuthenticatedUser_valid() {
        final User authUser = new User(USER_ID);
        expect(userRepository.get(USER_ID)).andReturn(authUser).anyTimes();
        replay(userRepository);

        service.setAuthenticatedUser(USER_ID);
        assertThat((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                is(sameInstance(authUser)));
        verify(userRepository);
    }

    @Test
    public void setAuthenticatedUser_validRole() {
        final User authUser = new User(USER_ID);
        final Authority userRole = new Authority();
        userRole.setAuthority("admin");
        authUser.addAuthority(userRole);
        expect(userRepository.get(USER_ID)).andReturn(authUser).anyTimes();
        replay(userRepository);

        service.setAuthenticatedUser(USER_ID);
        assertThat((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                is(sameInstance(authUser)));
        final GrantedAuthority grantedAuthority =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
        assertEquals("has authority admin", "admin", grantedAuthority.getAuthority());
        verify(userRepository);
    }


    @Test(expected = UsernameNotFoundException.class)
    public void setAuthenticatedUser_invalid_null() {
        expect(userRepository.get(USER_ID)).andReturn(null).anyTimes();
        replay(userRepository);

        service.setAuthenticatedUser(USER_ID);
        verify(userRepository);
    }

    @Test
    public void loadByUsername_valid() {
        final User authUser = new User(USER_ID, USER_NAME);
        expect(userRepository.getByUsername(USER_NAME)).andReturn(authUser).anyTimes();
        replay(userRepository);

        UserDetails result = service.loadUserByUsername(USER_NAME);
        assertThat((User)result, is(sameInstance(authUser)));
        verify(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_invalid_exception() {
        expect(userRepository.getByUsername(USER_NAME)).andReturn(null);
        replay(userRepository);
        service.loadUserByUsername(USER_NAME);
        verify(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_invalid_null() {
        expect(userRepository.get(USER_ID)).andReturn(null).anyTimes();
        replay(userRepository);

        service.setAuthenticatedUser(USER_ID);
        verify(userRepository);
    }

     @Test
     public void getUserByEmail_valid() {
          final User authUser=new User(USER_ID,USER_NAME);
          authUser.setEmail(USER_EMAIL);
        expect(userRepository.getByUserEmail(USER_EMAIL)).andReturn(authUser).anyTimes();
        replay(userRepository);

        UserDetails result = service.getUserByEmail(USER_EMAIL);
        assertThat((User)result, is(sameInstance(authUser)));
        verify(userRepository);
     }


    @Test
    public void clearAuthentication() {
        SecurityContext context = new SecurityContextImpl();
        SecurityContextHolder.setContext(context);
        service.clearAuthenticatedUser();
        assertThat(SecurityContextHolder.getContext(), not(sameInstance(context)));
    }

    @Test
    public void getLimitedListOfUsers() {
        User user1 = new User(123L, "john.doe.sr");
        User user2 = new User(456L, "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int offset = 0;
        final int pageSize = 10;
        expect(userRepository.getCountAll()).andReturn(users.size());
        expect(userRepository.getLimitedList(offset, pageSize)).andReturn(users);
        replay(userRepository);

        SearchResult<User> result = service.getLimitedListOfUsers(offset, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertEquals(users.size(), result.getResultSet().size());
        assertEquals(user1, result.getResultSet().get(0));
        verify(userRepository);
    }

    @Test
    public void getUsersByFreeTextSearch() {
        final String searchTerm = "Doe";
        User user1 = new User(123L, "john.doe.sr");
        User user2 = new User(456L, "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int offset = 0;
        final int pageSize = 10;
        expect(userRepository.getCountByUsernameOrEmail(searchTerm)).andReturn(users.size());
        expect(userRepository.findByUsernameOrEmail(searchTerm, offset, pageSize)).andReturn(users);
        replay(userRepository);

        SearchResult<User> result = service.getUsersByFreeTextSearch(searchTerm, offset, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertEquals(users.size(), result.getResultSet().size());
        assertEquals(user1, result.getResultSet().get(0));
        verify(userRepository);
    }

    @Test
    public void updateUserProfile() {
        User user = new User(USER_ID, USER_NAME);
        expect(userRepository.save(user)).andReturn(user).once();
        replay(userRepository);

        service.updateUserProfile(user);
        assertTrue("Save called", true);
        verify(userRepository);
    }

    @Test
    public void deleteUser() {
        final int NUM_COMMENTS = 33;
        final int NUM_RATINGS = 99;
        final int NUM_WIDGETS_OWNED = 4;
        User user = new User(USER_ID, USER_NAME);
        Page page = new Page(1L, user);
        List<Page> pages = new ArrayList<Page>();
        pages.add(page);
        
        expect(userRepository.get(USER_ID)).andReturn(user);
        expect(pageRepository.deletePages(USER_ID, PageType.USER)).andReturn(pages.size());
        expect(pageRepository.deletePages(USER_ID, PageType.PERSON_PROFILE)).andReturn(pages.size());
        expect(widgetCommentRepository.deleteAll(USER_ID)).andReturn(NUM_COMMENTS);
        expect(widgetRatingRepository.deleteAll(USER_ID)).andReturn(NUM_RATINGS);       
        expect(widgetRepository.unassignWidgetOwner(USER_ID)).andReturn( NUM_WIDGETS_OWNED);       
        userRepository.delete(user);
        expectLastCall();
        replay(userRepository, pageRepository, widgetCommentRepository, widgetRatingRepository, widgetRepository);

        service.deleteUser(USER_ID);

        verify(userRepository, pageRepository, widgetCommentRepository, widgetRatingRepository, widgetRepository);
    }

    @Test
    public void deleteUser_invalidUserId() {
        expect(userRepository.get(INVALID_USER_ID)).andReturn(null);
        replay(userRepository, pageRepository, widgetCommentRepository, widgetRatingRepository, widgetRepository);

        service.deleteUser(INVALID_USER_ID);

        verify(userRepository, pageRepository, widgetCommentRepository, widgetRatingRepository, widgetRepository);
    }

    @Test
    public void getAllByAddedWidget() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User());
        userList.add(new User());

        List<Person> personList = new ArrayList<Person>();
        personList.add(userList.get(0).toPerson());
        personList.add(userList.get(1).toPerson());
        
        expect(userRepository.getAllByAddedWidget(VALID_WIDGET_ID)).andReturn(userList);
        replay(userRepository);

        assertThat(service.getAllByAddedWidget(VALID_WIDGET_ID), is(personList));
        
        verify(userRepository);
    }

    @Test
    public void registerNewUser_valid(){
        User user = new User();
        expect(userRepository.save(user)).andReturn(user).once();
        expect(pageTemplateRepository.getDefaultPersonPage()).andReturn(new PageTemplate()).once();
        expect(pageRepository.createPersonPageForUser(isA(User.class), isA(PageTemplate.class))).andReturn(new Page());
        replay(userRepository, pageTemplateRepository, pageRepository);
        service.registerNewUser(user);
        verify(userRepository, pageTemplateRepository, pageRepository);
    }
}
