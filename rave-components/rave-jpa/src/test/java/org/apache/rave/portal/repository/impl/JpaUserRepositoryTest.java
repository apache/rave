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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.Authority;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaUserRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    private static final Long USER_ID = 1L;
    private static final Long USER_ID_OPENID_USER = 13L;
    private static final String USER_NAME = "canonical";

    private static final Long INVALID_USER = -2L;
    private static final String USER_EMAIL = "canonical@example.com";
    private static final String OPENID = "http://rave2011.myopenid.com/";
    private static final Long VALID_WIDGET_ID = 1L;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void getById_validId() {
        User user = repository.get(USER_ID.toString());
        assertThat(user, notNullValue());
        assertThat(user.getUsername(), is(equalTo(USER_NAME)));
        passwordEncoder.encode(USER_NAME);
        assertThat(true, is(passwordEncoder.matches(USER_NAME, user.getPassword())));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }

    @Test
    public void getById_invalidId() {
        User user = repository.get(INVALID_USER.toString());
        assertThat(user, is(nullValue()));
    }

    @Test
    public void getByUsername_valid() {
        JpaUser user = (JpaUser)repository.getByUsername(USER_NAME);
        assertThat(user, notNullValue());
        assertThat(user.getEntityId(), is(equalTo(USER_ID)));
        assertThat(true, is(passwordEncoder.matches(USER_NAME, user.getPassword())));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }

    @Test
    public void getByUsername_invalid() {
        User user = repository.get(INVALID_USER.toString());
        assertThat(user, is(nullValue()));
    }

    @Test
    public void getByUserEmail_valid() {
        JpaUser user = (JpaUser)repository.getByUserEmail(USER_EMAIL);
        assertThat(user, notNullValue());
        assertThat(user.getEntityId(), is(equalTo(USER_ID)));
        assertThat(true, is(passwordEncoder.matches(USER_NAME, user.getPassword())));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }
    
    @Test
    public void getByOpenId_valid() {
        JpaUser user = (JpaUser)repository.getByOpenId(OPENID);
        assertThat(user, notNullValue());
        assertThat(user.getEntityId(), is(equalTo(USER_ID_OPENID_USER)));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getOpenId(), is(equalTo(OPENID)));
    }

    @Test
    public void addOrDeleteUserDoesNotAffectAuthority() {
        Authority authority = authorityRepository.get("1");
        Assert.assertNotNull("Existing authority", authority);

        int usercount = authority.getUsers().size();
        User user = new JpaUser();
        user.setUsername("dummy");
        authority.addUser(user);
        authorityRepository.save(authority);
        assertNull("Persisting an Authority does not persist an unknown user", repository.getByUsername("dummy"));
        Assert.assertEquals("Authority has 1 more user", usercount + 1, authority.getUsers().size());

        repository.save(user);
        user = repository.getByUsername("dummy");
        Assert.assertNotNull(user);
        Assert.assertEquals("Authority has 1 more user", usercount + 1, authority.getUsers().size());

        repository.delete(user);
        authority = authorityRepository.get("1");
        Assert.assertNotNull("Authority has not been removed after deleting user", authority);
        Assert.assertEquals("Authority has original amount of users", usercount, authority.getUsers().size());
    }

    @Test
    public void getAll(){
        List<User> users = repository.getAll();
        assertNotNull(users);
        assertThat(users.size(), is(13));
    }

    @Test
    public void getLimitedList() {
        final int offset = 0;
        final int pageSize = 5;
        List<User> users = repository.getLimitedList(offset, pageSize);
        assertNotNull(users);
        assertTrue(users.size() <= pageSize);
    }

    @Test
    public void countAll() {
        int count = repository.getCountAll();
        assertTrue(count >= 6);
    }

    @Test
    public void getSearchResult() {
        String searchTerm = "Doe";
        final int offset = 0;
        final int pageSize = 10;
        List<User> users = repository.findByUsernameOrEmail(searchTerm, offset, pageSize);
        assertNotNull(users);
        assertTrue(users.size() > 0 && users.size() <= pageSize);
    }

    @Test
    public void countSearchResult() {
        String searchTerm = "Doe";
        int count = repository.getCountByUsernameOrEmail(searchTerm);
        assertTrue(count > 1);
    }

    @Test
    @Ignore("TODO: FIX BEFORE RAVE-729")
    public void getAllByAddedWidget() {
        String searchTerm = "Doe";
        List<User> users = repository.getAllByAddedWidget(VALID_WIDGET_ID.toString());
        // verify that the names are in alphabetical order
        assertThat(users.isEmpty(), is(false));
        String previousFamilyName = "";
        for (User user : users) {
            String familyName = user.getFamilyName();
            assertThat(previousFamilyName.compareTo(familyName) <= 0 , is(true));
            previousFamilyName = familyName;
        }
    }

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaUser.class);
    }

    @Test
    public void getByForgotPasswordHash() {
        User user = repository.getByForgotPasswordHash("ABC123");
        assertThat(user.getId(), is("4"));
    }

}
