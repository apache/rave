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
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link UserProfileValidator}
 */
public class UserProfileValidatorTest {
    private static final String VALID_NAME = "valid.name";
    private static final String VALID_PASSWORD = "valid.password";
    private static final String VALID_EMAIL = "valid@example.com";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";
    private static final String USER = "user";

    private UserProfileValidator validator;

    @Test
    public void testSupports() throws Exception {
        assertTrue("Can validate org.apache.rave.model.User", validator.supports(UserImpl.class));
    }

    @Test
    public void testValidate() throws Exception {
        User user = new UserImpl();
        user.setUsername(VALID_NAME);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword(VALID_PASSWORD);
        user.setEmail(VALID_EMAIL);

        Errors errors = new BindException(user, USER);
        validator.validate(user, errors);

        assertFalse("No errors", errors.hasErrors());
    }

    @Test
    public void testValidateFailsOnEmptyPassword() throws Exception {
        User user = new UserImpl();
        user.setUsername(VALID_NAME);
        user.setEmail(VALID_EMAIL);

        Errors errors = new BindException(user, USER);
        validator.validate(user, errors);

        assertTrue("Validation errors", errors.hasErrors());
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));

    }

    @Test
    public void testValidateFailsOnShortPassword() throws Exception {
        User user = new UserImpl();
        user.setUsername(VALID_NAME);
        user.setPassword("123");
        user.setPassword("123");

        Errors errors = new BindException(user, USER);
        validator.validate(user, errors);

        assertTrue("Validation errors", errors.hasErrors());
        assertNotNull(errors.getFieldError(FIELD_PASSWORD));

    }

    @Test
    public void testValidateFailsOnPasswordMismatch() throws Exception {
        User user = new UserImpl();
        user.setUsername(VALID_NAME);
        user.setPassword(VALID_PASSWORD);
        user.setConfirmPassword("DoesNotMatch");

        Errors errors = new BindException(user, USER);
        validator.validate(user, errors);

        assertTrue("Validation errors", errors.hasErrors());
        assertNotNull(errors.getFieldError(FIELD_CONFIRM_PASSWORD));

    }


    @Before
    public void setUp() throws Exception {
        UserService mockUserService = createMock(UserService.class);
        validator = new UserProfileValidator(mockUserService);
    }
}
