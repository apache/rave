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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.UserService;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

/**
 * Mapping class between a Rave User and LDAP user
 */
public class LdapUserDetailsContextMapper implements UserDetailsContextMapper {

    private final UserService userService;

    private final NewAccountService newAccountService;

    private final String mailAttributeName;

    private final String displayNameAttributeName;

    private final String pageLayoutCode;

    /**
     * Creates new {@link LdapUserDetailsContextMapper}
     *
     * @param userService              service that can look up user details within Rave
     * @param newAccountService        service that creates new accounts in Rave
     * @param mailAttributeName        name of the email attribute in LDAP
     * @param displayNameAttributeName name of the displayName (pretty name) attribute in LDAP
     * @param pageLayoutCode           defines which page layout is assigned in case of a new Rave user
     */
    public LdapUserDetailsContextMapper(UserService userService, NewAccountService newAccountService,
                                        String mailAttributeName, String displayNameAttributeName,
                                        String pageLayoutCode) {
        this.userService = userService;
        this.newAccountService = newAccountService;
        this.mailAttributeName = mailAttributeName;
        this.displayNameAttributeName = displayNameAttributeName;
        this.pageLayoutCode = pageLayoutCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Empty username from LDAP");
        }

        User byUsername = userService.getUserByUsername(username);
        if (byUsername == null) {
            createRaveUserFromLdapInfo(ctx, username);
            byUsername = userService.getUserByUsername(username);
        }

        return byUsername;
    }

    private void createRaveUserFromLdapInfo(DirContextOperations ctx, String username) {
        User newUser = new UserImpl();
        newUser.setUsername(username);

        if (!ctx.attributeExists(mailAttributeName) || StringUtils.isBlank(ctx.getStringAttribute(mailAttributeName))) {
            throw new RuntimeException("Missing LDAP attribute for email for user " + username);
        }

        newUser.setEmail(ctx.getStringAttribute(mailAttributeName));
        if (ctx.attributeExists(displayNameAttributeName)) {
            newUser.setDisplayName(ctx.getStringAttribute(displayNameAttributeName));
        }
        newUser.setPassword(RandomStringUtils.random(16));
        newUser.setDefaultPageLayoutCode(pageLayoutCode);
        try {
            newAccountService.createNewAccount(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Could not bind LDAP username '{" + username + "}' to a user", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * LDAP is leading, no updates from the Rave user database to LDAP
     */
    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
    }
}
