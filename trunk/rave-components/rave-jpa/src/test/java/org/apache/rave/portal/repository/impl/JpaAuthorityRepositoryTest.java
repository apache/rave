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
import org.apache.rave.portal.model.JpaAuthority;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.model.User;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaAuthorityRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private AuthorityRepository repository;

    @Autowired
    private UserRepository userRepository;

    private static final Long VALID_ID = 1L;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaAuthority.class);
    }

    @Test
    public void getById_validId() {
        final JpaAuthority authority = (JpaAuthority)repository.get(VALID_ID.toString());
        assertNotNull(authority);
        assertEquals(VALID_ID, authority.getEntityId());
    }

    @Test
    public void getByAuthorityName() {
        String authorityName = "administrator";
        Authority authority = repository.getByAuthority(authorityName);
        assertNotNull(authority);
        assertEquals(authorityName, authority.getAuthority());
        assertTrue(authority.getUsers().isEmpty());
    }

    @Test
    public void getUsersByAuthorityName() {
        String authorityName = "administrator";
        Authority authority = repository.getByAuthority(authorityName);
        assertNotNull(authority);
        assertEquals(authorityName, authority.getAuthority());
        assertTrue(authority.getUsers().isEmpty());

        User newUser = new JpaUser();
        newUser.setUsername("adminuser");
        newUser.addAuthority(authority);
        newUser = userRepository.save(newUser);
        assertEquals(authority, newUser.getAuthorities().iterator().next());

        authority = repository.getByAuthority(authorityName);
        assertEquals(1, authority.getUsers().size());
    }

    @Test
    public void addOrDeleteAuthorityDoesNotAffectUser() {
        final String authorityName = "guest";
        Authority authority = new JpaAuthority();
        authority.setAuthority(authorityName);
        User user = userRepository.get("1");

        Assert.assertNotNull("User is null", user);
        Assert.assertTrue("User has no authorities", user.getAuthorities().isEmpty());
        assertNull("No authority guest", repository.getByAuthority(authorityName));

        user.addAuthority(authority);
        user = userRepository.save(user);

        assertNull("Persisting a user does not persist an unknown Authority", repository.getByAuthority(authorityName));
        repository.save(authority);

        Assert.assertEquals("Found authority", authorityName, user.getAuthorities().iterator().next().getAuthority());
        Assert.assertNotNull("New authority: guest", authority);

        repository.delete(authority);
        assertNull("No authority guest", repository.getByAuthority(authorityName));

        user = userRepository.get("1");
        Assert.assertNotNull("User should not be deleted after removing an authority", user);
        Assert.assertTrue("User should have no authorities", user.getAuthorities().isEmpty());
    }

    @Test
    public void getAll() {
        List<Authority> allAuthorities = repository.getAll();
        assertFalse("Found authorities", allAuthorities.isEmpty());
    }

    @Test
    public void getAllDefault() {
        List<Authority> allAuthorities = repository.getAllDefault();
        assertThat(allAuthorities.isEmpty(), is(false));
        for (Authority authority : allAuthorities) {
            assertThat(authority.isDefaultForNewUser(), is(true));
        }
    }

    @Test
    public void countAll() {
        int numberOfAuthorities = repository.getCountAll();
        assertTrue("Found at least 1 Authority", numberOfAuthorities > 0);
    }
}
