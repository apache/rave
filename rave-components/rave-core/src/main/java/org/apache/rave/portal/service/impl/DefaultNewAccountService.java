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

import org.apache.commons.lang.StringUtils;
import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultNewAccountService implements NewAccountService {

    protected final Logger logger=LoggerFactory.getLogger(getClass());

    private final UserService userService;
    private final PageService pageService;
    // TODO RAVE-236 why are these unused PageLayoutService and RegionService declared?
    private final PageLayoutService pageLayoutService;
    private final RegionService regionService;
    private final AuthorityService authorityService;

    @Autowired 
    private PasswordEncoder  passwordEncoder;

    @Autowired
    public DefaultNewAccountService(UserService userService, 
                                    PageService pageService, 
                                    PageLayoutService pageLayoutService, 
                                    RegionService regionService,
                                    AuthorityService authorityService) {
        this.userService = userService;
        this.pageService = pageService;
        this.pageLayoutService = pageLayoutService;
        this.regionService = regionService;
        this.authorityService = authorityService;
    }

    @Override
    public void createNewAccount(NewUser newUser) throws Exception {
        final String userName = newUser.getUsername();
        final String password = newUser.getPassword();
        final String defaultPageLayoutCode = newUser.getPageLayout();
        final String email = newUser.getEmail();
        
        //capture optional fields
        final String givenName = newUser.getFirstName();
        final String familyName = newUser.getLastName();
        final String displayName = newUser.getDisplayName();
        final String status = newUser.getStatus();
        final String aboutMe = newUser.getAboutMe();

        throwExceptionIfUserExists(userName, email);
                
        User user = new User();
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