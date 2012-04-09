/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.web.validator;

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * Test class for {@link NewAccountValidator}
 */
public class NewAccountValidatorTest {
    private static final String VALID_NAME = "valid.name";
    private static final String VALID_PASSWORD = "valid.password";
    private static final String VALID_PAGELAYOUT = "valid.pagelayout";
    private static final String VALID_EMAIL = "valid.email@example.com";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";
    private static final String FIELD_EMAIL = "email";
    private static final String NEW_USER = "newUser";

    private NewAccountValidator newAccountValidator;
    private UserService mockUserService;

    @Test
    public void testSupports() throws Exception {
        assertTrue("Can validate org.apache.rave.portal.model.NewUser",
                newAccountValidator.supports(NewUser.class));
    }

    @Test
    public void testValidate() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_NAME);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);

        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertFalse("No validation errors", errors.hasErrors());
    }

    @Test
    public void testValidationFailsOnEmptyNewUser() throws Exception {
        NewUser newUser = new NewUser();
        Errors errors = new BindException(newUser, NEW_USER);
        expect(mockUserService.getUserByUsername("")).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("4 Validation errors", 4, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));
        assertNotNull(errors.getFieldError(FIELD_CONFIRM_PASSWORD));
        assertNotNull(errors.getFieldError(FIELD_EMAIL));

    }


    @Test
    public void testValidationFailsOnExistingUser() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername("ExistingUser");
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);

        User user = createMock(User.class);
        expect(mockUserService.getUserByUsername("ExistingUser")).andReturn(user);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnExistingEmail() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_NAME);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail("existing@localhost");
        Errors errors = new BindException(newUser, NEW_USER);

        User user = createMock(User.class);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail("existing@localhost")).andReturn(user);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_EMAIL));
    }


    @Test
    public void testValidationFailsOnShortUserName() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername("A");
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);
        expect(mockUserService.getUserByUsername("A")).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnIllegalUsername() throws Exception {
        NewUser newUser = new NewUser();
        final String badUsername = "x'; DROP TABLE members; --";
        newUser.setUsername(badUsername);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword(VALID_PASSWORD);
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);
        expect(mockUserService.getUserByUsername(badUsername)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnShortPassword() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_NAME);
        newUser.setPassword("123");
        newUser.setConfirmPassword("123");
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));
    }

    @Test
    public void testValidationFailsOnNonMatchingPassword() throws Exception {
        NewUser newUser = new NewUser();
        newUser.setUsername(VALID_NAME);
        newUser.setPassword(VALID_PASSWORD);
        newUser.setConfirmPassword("doesnotmatch");
        newUser.setPageLayout(VALID_PAGELAYOUT);
        newUser.setEmail(VALID_EMAIL);
        Errors errors = new BindException(newUser, NEW_USER);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(newUser, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_CONFIRM_PASSWORD));
    }

    @Before
    public void setup() {
        mockUserService = createMock("mockUserService", UserService.class);
        newAccountValidator = new NewAccountValidator(mockUserService);
    }


}
