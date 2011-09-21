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

package org.apache.rave.portal.service;

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.impl.DefaultNewAccountService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * Test class for {@link org.apache.rave.portal.service.impl.DefaultNewAccountService}
 */
public class NewAccountServiceTest {
    private UserService userService;
    private PageLayoutService pageLayoutService;

    private NewAccountService newAccountService;

    private final Logger logger = LoggerFactory.getLogger(NewAccountServiceTest.class);

    @Test
    public void createNewAccountTest() throws Exception {
        String validUser = "valid.user";
        String validPass = "valid.password";
        String validLayout = "valid.layout";
        String validEmail = "valid.email";

        NewUser newUser = new NewUser();
        newUser.setUsername(validUser);
        newUser.setPassword(validPass);
        newUser.setConfirmPassword(validPass);
        newUser.setEmail(validEmail);


        SaltSource saltSource = createNiceMock(SaltSource.class);
        UserDetails userDetails = createNiceMock(UserDetails.class);
        expect(userDetails.getUsername()).andReturn("valid.user");
        expect(userDetails.getPassword()).andReturn("valid.password");
        expect(saltSource.getSalt(userDetails)).andReturn("salt");
        PasswordEncoder passwordEncoder = createNiceMock(PasswordEncoder.class);
        expect(passwordEncoder.encodePassword("valid.password", "salt")).andReturn("valid.password");
        replay(saltSource, userDetails, passwordEncoder);

        ReflectionTestUtils.setField(newAccountService, "saltSource", saltSource);
        ReflectionTestUtils.setField(newAccountService, "passwordEncoder", passwordEncoder);

        expect(userService.getUserByUsername(validUser)).andReturn(null);
        PageLayout pageLayout = new PageLayout();
        pageLayout.setNumberOfRegions(4L);
        expect(pageLayoutService.getPageLayoutByCode(validLayout)).andReturn(pageLayout);
        replay(userService, pageLayoutService);

        newAccountService.createNewAccount(newUser);
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


    @Before
    public void setup() {
        userService = createNiceMock(UserService.class);
        PageService pageService = createNiceMock(PageService.class);
        pageLayoutService = createNiceMock(PageLayoutService.class);
        RegionService regionService = createNiceMock(RegionService.class);

        newAccountService = new DefaultNewAccountService(userService, pageService, pageLayoutService, regionService);

    }
}