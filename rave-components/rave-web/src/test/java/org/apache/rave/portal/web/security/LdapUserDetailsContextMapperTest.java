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

package org.apache.rave.portal.web.security;

import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link LdapUserDetailsContextMapper}
 */
public class LdapUserDetailsContextMapperTest {

    private static final String MAIL_ATTRIBUTE_NAME = "mail";
    private static final String DISPLAY_NAME_ATTRIBUTE_NAME = "displayName";

    private LdapUserDetailsContextMapper contextMapper;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = createMock(UserService.class);
        NewAccountService newAccountService = createMock(NewAccountService.class);
        contextMapper = new LdapUserDetailsContextMapper(userService, newAccountService,
                MAIL_ATTRIBUTE_NAME, DISPLAY_NAME_ATTRIBUTE_NAME, "columns_3");
    }

    @Test
    public void testMapUserFromContext_new() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "johnldap";
        User user = new UserImpl("123", username);

        expect(userService.getUserByUsername(username)).andReturn(null).once();
        expect(ctx.attributeExists(MAIL_ATTRIBUTE_NAME)).andReturn(true);
        expect(ctx.getStringAttribute(MAIL_ATTRIBUTE_NAME)).andReturn("johnldap@example.com").times(2);
        expect(ctx.attributeExists(DISPLAY_NAME_ATTRIBUTE_NAME)).andReturn(true);
        expect(ctx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE_NAME)).andReturn("John Ldap");
        expect(userService.getUserByUsername(username)).andReturn(user).once();
        expectLastCall();

        replay(userService, ctx);

        final UserDetails userDetails =
                contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        verify(userService, ctx);
        assertEquals(user, userDetails);
    }

    @Test
    public void testMapUserFromContext_new_no_displayname() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "johnldap";
        User user = new UserImpl("123", username);

        expect(userService.getUserByUsername(username)).andReturn(null).once();
        expect(ctx.attributeExists(MAIL_ATTRIBUTE_NAME)).andReturn(true);
        expect(ctx.getStringAttribute(MAIL_ATTRIBUTE_NAME)).andReturn("johnldap@example.com").times(2);
        expect(ctx.attributeExists(DISPLAY_NAME_ATTRIBUTE_NAME)).andReturn(false);
        expect(userService.getUserByUsername(username)).andReturn(user).once();
        expectLastCall();

        replay(userService, ctx);

        final UserDetails userDetails =
                contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        verify(userService, ctx);
        assertEquals(user, userDetails);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapUserFromContext_new_empty_username() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "";

        contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        assertFalse("Exception thrown", true);
    }

    @Test(expected = RuntimeException.class)
    public void testMapUserFromContext_missing_mail() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "johnldap";

        expect(userService.getUserByUsername(username)).andReturn(null).once();
        expect(ctx.attributeExists(MAIL_ATTRIBUTE_NAME)).andReturn(false);

        replay(userService, ctx);

        contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        verify(userService, ctx);
        assertFalse("Exception thrown", true);
    }

    @Test(expected = RuntimeException.class)
    public void testMapUserFromContext_empty_mail() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "johnldap";

        expect(userService.getUserByUsername(username)).andReturn(null).once();
        expect(ctx.attributeExists(MAIL_ATTRIBUTE_NAME)).andReturn(true);
        expect(ctx.getStringAttribute(MAIL_ATTRIBUTE_NAME)).andReturn("").times(1);

        replay(userService, ctx);

        contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        verify(userService, ctx);
        assertFalse("Exception thrown", true);
    }

    @Test
    public void testMapUserFromContext_existing() throws Exception {
        DirContextOperations ctx = createMock(DirContextOperations.class);

        final String username = "johnldap";
        User user = new UserImpl("123", username);

        expect(userService.getUserByUsername(username)).andReturn(user);
        expectLastCall();

        replay(userService);

        final UserDetails userDetails =
                contextMapper.mapUserFromContext(ctx, username, Collections.<GrantedAuthority>emptyList());

        verify(userService);
        assertEquals(user, userDetails);
    }

    @Test
    public void testMapUserToContext() throws Exception {
        User user = new UserImpl();
        DirContextAdapter adapter = new DirContextAdapter();

        contextMapper.mapUserToContext(user, adapter);

        assertTrue("Nothing happened", true);
    }
}
