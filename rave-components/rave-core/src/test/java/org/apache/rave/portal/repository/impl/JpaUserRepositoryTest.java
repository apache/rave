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

import junit.framework.Assert;
import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.User;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.repository.UserRepository;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaUserRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "canonical";
    //The password value depends on the hash algorithm and salt used, so this
    //may need updating in the future.
    private static final String HASHED_SALTED_PASSWORD = "b97fd0fa25ba8a504309be2b6651ac6dee167ded";
    private static final Long INVALID_USER = -2L;
    private static final String USER_EMAIL = "canonical@example.com";

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void getById_validId() {
        User user = repository.get(USER_ID);
        assertThat(user, CoreMatchers.notNullValue());
        assertThat(user.getUsername(), is(equalTo(USER_NAME)));
        assertThat(user.getPassword(), is(equalTo(HASHED_SALTED_PASSWORD)));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }

    @Test
    public void getById_invalidId() {
        User user = repository.get(INVALID_USER);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void getByUsername_valid() {
        User user = repository.getByUsername(USER_NAME);
        assertThat(user, CoreMatchers.notNullValue());
        assertThat(user.getEntityId(), is(equalTo(USER_ID)));
        assertThat(user.getPassword(), is(equalTo(HASHED_SALTED_PASSWORD)));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }

    @Test
    public void getByUsername_invalid() {
        User user = repository.get(INVALID_USER);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void getByUserEmail_valid() {
        User user = repository.getByUserEmail(USER_EMAIL);
        assertThat(user, CoreMatchers.notNullValue());
        assertThat(user.getEntityId(), is(equalTo(USER_ID)));
        assertThat(user.getPassword(), is(equalTo(HASHED_SALTED_PASSWORD)));
        assertThat(user.isAccountNonExpired(), is(true));
        assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }

    @Test
    public void addOrDeleteUserDoesNotAffectAuthority() {
        Authority authority = authorityRepository.get(1L);
        Assert.assertNotNull("Existing authority", authority);

        int usercount = authority.getUsers().size();
        User user = new User();
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
        authority = authorityRepository.get(1L);
        Assert.assertNotNull("Authority has not been removed after deleting user", authority);
        Assert.assertEquals("Authority has original amount of users", usercount, authority.getUsers().size());
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
}
