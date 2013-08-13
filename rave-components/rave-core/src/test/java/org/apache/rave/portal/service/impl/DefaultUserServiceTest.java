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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
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
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;

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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultUserServiceTest {

    private static final String USER_ID = "1234";
    private UserService service;
    private UserRepository userRepository;
    private PageRepository pageRepository;
    private PageTemplateRepository pageTemplateRepository;
    private WidgetRepository widgetRepository;
    private CategoryRepository categoryRepository;
    private PersonRepository personRepository;

    private static final String USER_NAME = "1234";
    private static final String USER_EMAIL = "test@test.com";
    private static final String OPENID_INVALID = "http://user.myopenid.com/";
    private static final String OPENID_VALID = "http://rave2011.myopenid.com/";
    private static final String VALID_WIDGET_ID = "1";
    private static final String INVALID_USER_ID = "-9999";

    @Before
    public void setup() {
        userRepository = createMock(UserRepository.class);
        pageRepository = createMock(PageRepository.class);
        pageTemplateRepository = createMock(PageTemplateRepository.class);
        widgetRepository = createMock(WidgetRepository.class);
        categoryRepository = createMock(CategoryRepository.class);
        personRepository = createMock(PersonRepository.class);

        service = new DefaultUserService(pageRepository, userRepository,
                                         widgetRepository, pageTemplateRepository, categoryRepository, personRepository);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getAuthenticatedUser_validUser() {
        final User authUser = new UserImpl(USER_ID);
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
        final User authUser = new UserImpl(USER_ID);
        expect(userRepository.get(USER_ID)).andReturn(authUser).anyTimes();
        replay(userRepository);

        service.setAuthenticatedUser(USER_ID);
        assertThat((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                is(sameInstance(authUser)));
        verify(userRepository);
    }

    @Test
    public void setAuthenticatedUser_validRole() {
        final User authUser = new UserImpl(USER_ID);
        final Authority userRole = new AuthorityImpl();
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
        final User authUser = new UserImpl(USER_ID, USER_NAME);
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
        final User authUser=new UserImpl(USER_ID,USER_NAME);
        authUser.setEmail(USER_EMAIL);
        expect(userRepository.getByUserEmail(USER_EMAIL)).andReturn(authUser).anyTimes();
        replay(userRepository);

        UserDetails result = service.getUserByEmail(USER_EMAIL);
        assertThat((User)result, is(sameInstance(authUser)));
        verify(userRepository);
     }
     
     @Test
     public void getUserByOpenId_valid() {
    	final User authUser=new UserImpl(USER_ID,USER_NAME);
        authUser.setOpenId(OPENID_VALID);
        expect(userRepository.getByOpenId(OPENID_VALID)).andReturn(authUser).anyTimes();
        replay(userRepository);

        UserDetails result = service.getUserByOpenId(OPENID_VALID);
        assertThat((User)result, is(sameInstance(authUser)));
        verify(userRepository);
     }
     
     @Test
     public void loadUserDetails_valid() {
     	final User authUser=new UserImpl(USER_ID,USER_NAME);
        authUser.setOpenId(OPENID_VALID);
        expect(userRepository.getByOpenId(OPENID_VALID)).andReturn(authUser).anyTimes();
        replay(userRepository);
         OpenIDAuthenticationToken postAuthToken = new OpenIDAuthenticationToken(OpenIDAuthenticationStatus.SUCCESS,OPENID_VALID, 
         		"Some message", new ArrayList<OpenIDAttribute>());
         UserDetails result = service.loadUserDetails(postAuthToken);
         assertThat((User)result, is(sameInstance(authUser)));
         verify(userRepository);
     }
     
     @Test(expected = UsernameNotFoundException.class)
     public void loadUserDetails_invalid_exception() {
         expect(userRepository.getByOpenId(OPENID_INVALID)).andReturn(null);
         replay(userRepository);
         OpenIDAuthenticationToken postAuthToken = new OpenIDAuthenticationToken(OpenIDAuthenticationStatus.SUCCESS,OPENID_INVALID, 
         		"Some message", new ArrayList<OpenIDAttribute>());
         service.loadUserDetails(postAuthToken);
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
    public void getAll(){
        List<User> users = new ArrayList<User>();
        expect(userRepository.getCountAll()).andReturn(1);
        expect(userRepository.getAll()).andReturn(users);
        replay(userRepository);

        List<User> result = service.getAll().getResultSet();
        assertThat(result, is(sameInstance(users)));

        verify(userRepository);
    }

    @Test
    public void getLimitedList() {
        User user1 = new UserImpl("123", "john.doe.sr");
        User user2 = new UserImpl("456", "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int offset = 0;
        final int pageSize = 10;
        expect(userRepository.getCountAll()).andReturn(users.size());
        expect(userRepository.getLimitedList(offset, pageSize)).andReturn(users);
        replay(userRepository);

        SearchResult<User> result = service.getLimitedList(offset, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertEquals(users.size(), result.getResultSet().size());
        assertEquals(user1, result.getResultSet().get(0));
        verify(userRepository);
    }

    @Test
    public void getUsersByFreeTextSearch() {
        final String searchTerm = "Doe";
        User user1 = new UserImpl("123", "john.doe.sr");
        User user2 = new UserImpl("456", "john.doe.jr");
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
        User user = new UserImpl(USER_ID, USER_NAME);
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
        UserImpl user = new UserImpl(USER_ID, USER_NAME);
        Page page = new PageImpl("1", user.getId());
        List<Page> pages = new ArrayList<Page>();
        pages.add(page);

        expect(userRepository.get(USER_ID)).andReturn(user);
        expect(pageRepository.deletePages(USER_ID, PageType.USER.toString())).andReturn(pages.size());
        expect(pageRepository.deletePages(USER_ID, PageType.PERSON_PROFILE.toString())).andReturn(pages.size());
        expect(widgetRepository.deleteAllWidgetComments(USER_ID)).andReturn(NUM_COMMENTS);
        expect(widgetRepository.deleteAllWidgetRatings(USER_ID)).andReturn(NUM_RATINGS);
        expect(widgetRepository.unassignWidgetOwner(USER_ID)).andReturn( NUM_WIDGETS_OWNED);
        expect(categoryRepository.removeFromCreatedOrModifiedFields(USER_ID)).andReturn( NUM_WIDGETS_OWNED);
        userRepository.delete(user);
        expectLastCall();
        replay(userRepository, pageRepository, widgetRepository, categoryRepository);

        service.deleteUser(USER_ID);

        verify(userRepository, pageRepository, widgetRepository, categoryRepository);
    }

    @Test
    public void deleteUser_invalidUserId() {
        expect(userRepository.get(INVALID_USER_ID)).andReturn(null);
        replay(userRepository, pageRepository, widgetRepository);

        service.deleteUser(INVALID_USER_ID);

        verify(userRepository, pageRepository, widgetRepository);
    }

    @Test
    public void getAllByAddedWidget() {
        List<User> userList = new ArrayList<User>();
        userList.add(new UserImpl());
        userList.add(new UserImpl());

        List<Person> personList = new ArrayList<Person>();
        personList.add(userList.get(0).toPerson());
        personList.add(userList.get(1).toPerson());

        expect(userRepository.getAllByAddedWidget(VALID_WIDGET_ID)).andReturn(userList);
        replay(userRepository);

        List<Person> allByAddedWidget = service.getAllByAddedWidget(VALID_WIDGET_ID);
        assertThat(allByAddedWidget, is(equalTo(personList)));

        verify(userRepository);
    }

    @Test
    public void registerNewUser_valid(){
        User user = new UserImpl();
        expect(userRepository.save(user)).andReturn(user).once();
        expect(pageTemplateRepository.getDefaultPage(PageType.PERSON_PROFILE.toString())).andReturn(new PageTemplateImpl()).once();
        expect(pageRepository.createPageForUser(isA(UserImpl.class), isA(PageTemplate.class))).andReturn(new PageImpl());
        replay(userRepository, pageTemplateRepository, pageRepository);
        service.registerNewUser(user);
        verify(userRepository, pageTemplateRepository, pageRepository);
    }
}
