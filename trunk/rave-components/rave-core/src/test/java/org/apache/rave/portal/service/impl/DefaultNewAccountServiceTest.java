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

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test class for {@link org.apache.rave.portal.service.impl.DefaultNewAccountService}
 */
public class DefaultNewAccountServiceTest {
    private UserService userService;
    private PageLayoutService pageLayoutService;
    private NewAccountService newAccountService;
    private AuthorityService authorityService;

    private UserDetails userDetails;
    private PasswordEncoder passwordEncoder;

    private final String VALID_USER = "valid.user";
    private final String VALID_PASSWORD = "valid.password";
    private final String VALID_LAYOUT_CODE = "valid.layout";
    private final String VALID_EMAIL = "valid.email";
    private PageLayout validPageLayout;
    private SearchResult<Authority> validAuthoritySearchResult;
    private List<Authority> validAuthorityList;
    
    private final Logger logger = LoggerFactory.getLogger(DefaultNewAccountServiceTest.class);

    @Before
    public void setup() {
        userService = createMock(UserService.class);
        pageLayoutService = createMock(PageLayoutService.class);
        userDetails = createMock(UserDetails.class);
        passwordEncoder = createMock(PasswordEncoder.class);
        authorityService = createMock(AuthorityService.class);

        newAccountService = new DefaultNewAccountService(userService, pageLayoutService, authorityService);
        
        validPageLayout = new PageLayout();
        validPageLayout.setEntityId(99L);
        validPageLayout.setNumberOfRegions(4L);
        validPageLayout.setCode(VALID_LAYOUT_CODE);
        
        Authority role1 = new Authority();
        role1.setAuthority("DEFAULT_ROLE1");
        Authority role2 = new Authority();
        role2.setAuthority("DEFAULT_ROLE2");
        
        validAuthorityList = new ArrayList<Authority>();
        validAuthorityList.add(role1);
        validAuthorityList.add(role2);
        validAuthoritySearchResult = new SearchResult<Authority>(validAuthorityList, validAuthorityList.size());        
    }    
    
    @Test
    public void createNewAccountTest() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_USER);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setEmail(VALID_EMAIL);
        newUser.setPageLayout(VALID_LAYOUT_CODE);
        
        User expectedUser = new User();
        expectedUser.setUsername(newUser.getUsername());
        expectedUser.setPassword(newUser.getPassword());
        expectedUser.setEmail(newUser.getEmail());
        expectedUser.setDefaultPageLayout(validPageLayout);
        expectedUser.setExpired(false);
        expectedUser.setLocked(false);
        expectedUser.setEnabled(true);                

        ReflectionTestUtils.setField(newAccountService, "passwordEncoder", passwordEncoder);

        expect(passwordEncoder.encode("valid.password")).andReturn("valid.password");
        expect(userService.getUserByUsername(VALID_USER)).andReturn(null);
        expect(userService.getUserByEmail(VALID_EMAIL)).andReturn(null);                
        expect(pageLayoutService.getPageLayoutByCode(VALID_LAYOUT_CODE)).andReturn(validPageLayout);        
        expect(authorityService.getDefaultAuthorities()).andReturn(validAuthoritySearchResult);
        userService.registerNewUser(expectedUser);  
        expectLastCall();                
        replay(userDetails, passwordEncoder, userService, pageLayoutService, authorityService);
               
        newAccountService.createNewAccount(newUser);
        
        verify(userDetails, passwordEncoder, userService, pageLayoutService);
    }

    @Test
    public void createNewAccountTest_blankEmail() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_USER);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setEmail("");
        newUser.setPageLayout(VALID_LAYOUT_CODE);

        User expectedUser = new User();
        expectedUser.setUsername(newUser.getUsername());
        expectedUser.setPassword(newUser.getPassword());
        expectedUser.setEmail(newUser.getEmail());
        expectedUser.setDefaultPageLayout(validPageLayout);
        expectedUser.setExpired(false);
        expectedUser.setLocked(false);
        expectedUser.setEnabled(true);

        ReflectionTestUtils.setField(newAccountService, "passwordEncoder", passwordEncoder);

        expect(passwordEncoder.encode("valid.password")).andReturn("valid.password");
        expect(userService.getUserByUsername(VALID_USER)).andReturn(null);
        //if the email is blank getUserByEmail should not be called so dont expect it
        expect(pageLayoutService.getPageLayoutByCode(VALID_LAYOUT_CODE)).andReturn(validPageLayout);
        expect(authorityService.getDefaultAuthorities()).andReturn(validAuthoritySearchResult);
        userService.registerNewUser(expectedUser);
        expectLastCall();
        replay(userDetails, passwordEncoder, userService, pageLayoutService, authorityService);

        newAccountService.createNewAccount(newUser);

        verify(userDetails, passwordEncoder, userService, pageLayoutService);
    }

    @Test
    public void failedAccountCreationTest_duplicateUsername() throws Exception {
        String duplicateUserName = "duplicateUserName";
        NewUser newUser = new NewUser();
        newUser.setUsername(duplicateUserName);
        User existingUser = new User();
        existingUser.setUsername(duplicateUserName);

        expect(userService.getUserByUsername(duplicateUserName)).andReturn(existingUser);
        replay(userService);

        try {
            newAccountService.createNewAccount(newUser);
            fail();
        } catch (IllegalArgumentException e) {
            logger.debug("Expected failure of account creation due to duplicate name");
        }
    }

    @Test
    public void failedAccountCreationTest_duplicateEmail() throws Exception {
        String duplicateEmail = "duplicateEmail";
        NewUser newUser = new NewUser();
        newUser.setUsername("newUser");
        newUser.setEmail(duplicateEmail);
        User existingUser = new User();
        existingUser.setEmail(duplicateEmail);

        expect(userService.getUserByUsername("newUser")).andReturn(null);
        expect(userService.getUserByEmail(duplicateEmail)).andReturn(existingUser);
        replay(userService);

        try {
            newAccountService.createNewAccount(newUser);
            fail();
        } catch (IllegalArgumentException e) {
            logger.debug("Expected failure of account creation due to duplicate email");
        }
    }

}