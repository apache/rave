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

package org.apache.rave.portal.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.portal.model.User;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:portal-test-dataContext.xml", "classpath:portal-test-applicationContext.xml"})
public class JpaUserRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "canonical";
    //The password value depends on the hash algorithm and salt used, so this
    //may need updating in the future.
    private static final String HASHED_SALTED_PASSWORD="b97fd0fa25ba8a504309be2b6651ac6dee167ded";
    private static final Long INVALID_USER = -2L;
	 private static final String USER_EMAIL = "canonical@test.com";

    @Autowired
    private UserRepository repository;

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
        assertThat(user.getId(), is(equalTo(USER_ID)));
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
        assertThat(user.getId(), is(equalTo(USER_ID)));
        assertThat(user.getPassword(), is(equalTo(HASHED_SALTED_PASSWORD)));
        assertThat(user.isAccountNonExpired(), is(true));
		  assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
    }
}
