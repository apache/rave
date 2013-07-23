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

import org.apache.rave.model.User;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.model.UserForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue("Can validate org.apache.rave.model.User",
                newAccountValidator.supports(UserForm.class));
    }

    @Test
    public void testValidate() throws Exception {
        UserForm user = new UserForm();
        user.setUsername(VALID_NAME);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);

        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertFalse("No validation errors", errors.hasErrors());
    }

    @Test
    public void testValidationFailsOnEmptyUser() throws Exception {
        UserForm user = new UserForm();
        Errors errors = new BindException(user, NEW_USER);
        expect(mockUserService.getUserByUsername("")).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("4 Validation errors", 4, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));
        assertNotNull(errors.getFieldError(FIELD_CONFIRM_PASSWORD));
        assertNotNull(errors.getFieldError(FIELD_EMAIL));

    }


    @Test
    public void testValidationFailsOnExistingUser() throws Exception {
        UserForm user = new UserForm();
        user.setUsername("ExistingUser");
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);

        User user1 = createMock(User.class);
        expect(mockUserService.getUserByUsername("ExistingUser")).andReturn(user1);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnExistingEmail() throws Exception {
        UserForm user = new UserForm();
        user.setUsername(VALID_NAME);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail("existing@localhost");
        Errors errors = new BindException(user, NEW_USER);

        User user1 = createMock(User.class);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail("existing@localhost")).andReturn(user1);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_EMAIL));
    }


    @Test
    public void testValidationFailsOnShortUserName() throws Exception {
        UserForm user = new UserForm();
        user.setUsername("A");
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);
        expect(mockUserService.getUserByUsername("A")).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnIllegalUsername() throws Exception {
        UserForm user = new UserForm();
        final String badUsername = "x'; DROP TABLE members; --";
        user.setUsername(badUsername);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);
        expect(mockUserService.getUserByUsername(badUsername)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_USERNAME));
    }

    @Test
    public void testValidationFailsOnShortPassword() throws Exception {
        UserForm user = new UserForm();
        user.setUsername(VALID_NAME);
        user.setPassword("123");
        user.setConfirmPassword("123");
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));
    }

    @Test
    public void testValidationFailsOnNonMatchingPassword() throws Exception {
        UserForm user = new UserForm();
        user.setUsername(VALID_NAME);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword("doesnotmatch");
        user.setDefaultPageLayoutCode(VALID_PAGELAYOUT);
        user.setEmail(VALID_EMAIL);
        Errors errors = new BindException(user, NEW_USER);
        expect(mockUserService.getUserByUsername(VALID_NAME)).andReturn(null);
        expect(mockUserService.getUserByEmail(VALID_EMAIL)).andReturn(null);
        replay(mockUserService);

        newAccountValidator.validate(user, errors);

        assertEquals("1 Validation error", 1, errors.getErrorCount());
        assertNotNull(errors.getFieldError(FIELD_CONFIRM_PASSWORD));
    }

    @Before
    public void setup() {
        mockUserService = createMock("mockUserService", UserService.class);
        newAccountValidator = new NewAccountValidator(mockUserService);
    }


}
