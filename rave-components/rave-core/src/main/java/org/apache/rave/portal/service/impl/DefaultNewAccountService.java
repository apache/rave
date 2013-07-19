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

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultNewAccountService implements NewAccountService {

    private final UserService userService;
    private final PageLayoutService pageLayoutService;
    private final AuthorityService authorityService;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    @Autowired
    public DefaultNewAccountService(UserService userService,
                                    PageLayoutService pageLayoutService,
                                    AuthorityService authorityService) {
        this.userService = userService;
        this.pageLayoutService = pageLayoutService;
        this.authorityService = authorityService;
    }

    @Override
    public void createNewAccount(User newUser) throws Exception {
        final String userName = newUser.getUsername();
        final String password = newUser.getPassword();
        final String defaultPageLayoutCode = newUser.getDefaultPageLayoutCode();
        final String email = newUser.getEmail();

        //capture optional fields
        final String givenName = newUser.getGivenName();
        final String familyName = newUser.getFamilyName();
        final String displayName = newUser.getDisplayName();
        final String status = newUser.getStatus();
        final String aboutMe = newUser.getAboutMe();
        final String openId = newUser.getOpenId();

        throwExceptionIfUserExists(userName, email);

        User user = new UserImpl();
        //set the required fields
        user.setUsername(userName);
        user.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        user.setExpired(false);
        user.setLocked(false);
        user.setEnabled(true);
        user.setDefaultPageLayout(pageLayoutService.getPageLayoutByCode(defaultPageLayoutCode));
        user.setAuthorities(authorityService.getDefaultAuthorities().getResultSet());

        //set the optional fields
        user.setGivenName(givenName);
        user.setFamilyName(familyName);
        user.setDisplayName(displayName);
        user.setStatus(status);
        user.setAboutMe(aboutMe);
        user.setOpenId(openId);

        userService.registerNewUser(user);
    }

    private void throwExceptionIfUserExists(String userName, String email) {
        User existingUser = userService.getUserByUsername(userName);
        if (existingUser != null) {
            throw new IllegalArgumentException("A user already exists for username " + userName);
        }

        //Implementors who use an alternative store for profile data probably wont be including email when creating new
        //Rave accounts -- they will likely only be setting username to create a stub entry in the database just so they
        //have something to tie the rest of the Rave entities to.  All the other profile data (like email) will be looked
        //up elsewhere.
        if (StringUtils.isNotEmpty(email)) {
            existingUser = userService.getUserByEmail(email);
            if (existingUser != null) {
                throw new IllegalArgumentException("A user already exists for email " + email);
            }
        }
    }
}