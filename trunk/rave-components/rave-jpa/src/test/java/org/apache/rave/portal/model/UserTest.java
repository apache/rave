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
package org.apache.rave.portal.model;

import org.apache.rave.model.Authority;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Tests the User class.
        */
 @Ignore
public class UserTest {
    private JpaUser user1;
    private JpaUser user2;
    private JpaUser user3;
    private long id;
    private String userName;
    private String userPassword;

    @Before
    public void setup() {

        id = 123L;
        userName = "testUser";
        userPassword = "qwerty";
        boolean user1Enabled = true;
        boolean user2Enabled = false;
        boolean user3Enabled = true;
        boolean user1Locked = false;
        boolean user2Locked = true;
        boolean user3Locked = false;
        boolean user1Expired = false;
        boolean user2Expired = true;
        boolean user3Expired = false;


        user1 = new JpaUser();
        user1.setUsername(userName);
        user1.setEntityId(id);
        user1.setPassword(userPassword);
        user1.setEnabled(user1Enabled);
        user1.setExpired(user1Expired);
        user1.setLocked(user1Locked);

        user2 = new JpaUser(id);
        user2.setExpired(user2Expired);
        user2.setEnabled(user2Enabled);
        user2.setLocked(user2Locked);

        user3 = new JpaUser(id, userName);
        user3.setExpired(user3Expired);
        user3.setEnabled(user3Enabled);
        user3.setLocked(user3Locked);

    }

    @Test
    public void testAccessorMethods() {
        assertTrue(user1.getUsername().equals(userName));
        assertTrue(user1.getPassword().equals(userPassword));
        assertTrue(user1.getEntityId().equals(id));
        assertTrue(user1.isEnabled());
        assertFalse(user2.isEnabled());
        assertTrue(user3.isEnabled());
    }

    @Test
    public void testAltConstructors() {
        assertTrue(user1.getEntityId().equals(user2.getEntityId()));
        assertTrue(user1.getEntityId().equals(user3.getEntityId()));
        assertTrue(user1.getUsername().equals(user3.getUsername()));
    }

    @Test
    public void testExpirations() {
        //Credentials and accounts seem entangled, so need tests
        //to make sure implementation doesn't chage.
        assertTrue(user1.isAccountNonExpired());
        assertFalse(user2.isAccountNonExpired());
        assertTrue(user3.isAccountNonExpired());
        assertTrue("Account and credential nonexpirations handled correctly", user1.isAccountNonExpired() && user1.isCredentialsNonExpired());
        assertTrue("Account and credential expirations handled correctly", !user2.isAccountNonExpired() && !user2.isCredentialsNonExpired());
    }

    @Test
    public void preRemove() {
        JpaUser user = new JpaUser(123L, "DummyUser");

        Collection<Authority> authorities = new ArrayList<Authority>();
        JpaAuthority authority = new JpaAuthority();
        authority.setEntityId(456L);
        authority.setAuthority("DummyAuthority");
        authority.addUser(user);
        authorities.add(authority);

        user.addAuthority(authority);

        assertFalse(user.getAuthorities().isEmpty());
        assertEquals(authorities, user.getAuthorities());
        assertEquals(user, authority.getUsers().iterator().next());

        user.preRemove();

        assertTrue(user.getAuthorities().isEmpty());
        assertTrue(authority.getUsers().isEmpty());
    }

}
