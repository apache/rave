/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.ChangePasswordValidator;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @version "$Id$"
 */
public class ChangePasswordControllerTest {

    private ChangePasswordValidator passwordValidator;
    private ChangePasswordController controller;

    @Before
    public void setUp() throws Exception {
        UserService userService = createNiceMock(UserService.class);

        expect(userService.isValidReminderRequest(null, 0)).andReturn(true).anyTimes();
        replay(userService);
        passwordValidator = new ChangePasswordValidator(userService);
        controller = new ChangePasswordController(userService, passwordValidator);
    }


    @Test
    public void testInitialize() throws Exception {
        final Model model = createNiceMock(Model.class);
        RedirectAttributes redirectAttributes = createNiceMock(RedirectAttributes.class);
        replay(redirectAttributes);
        replay(model);
        String viewName = controller.initialize(model, null);
        assertThat(viewName, CoreMatchers.equalTo(ViewNames.PASSWORD_CHANGE));
    }

    @Test
    public void testUpdate() throws Exception {
        final Model model = createNiceMock(Model.class);
        RedirectAttributes redirectAttributes = createNiceMock(RedirectAttributes.class);
        UserForm newUser = new UserForm();
        replay(redirectAttributes);
        replay(model);
        BindingResult results = new DirectFieldBindingResult(newUser, ModelKeys.USER);
        String viewName = controller.update(newUser, results, model, redirectAttributes);
        assertThat(viewName, CoreMatchers.equalTo(ViewNames.PASSWORD_CHANGE));
        assertThat(results.hasErrors(), CoreMatchers.equalTo(true));
        assertThat(results.getErrorCount(), CoreMatchers.equalTo(2));
        // invalid password, to short:
        newUser.setPassword("123");
        results = new DirectFieldBindingResult(newUser, ModelKeys.USER);
        controller.update(newUser, results, model, redirectAttributes);
        assertEquals("Expected password errors", 2, results.getErrorCount());
        assertEquals("Expected password errors", "password.invalid.length", results.getFieldError().getCode());
        // missing password confirm:
        newUser.setPassword("1234");
        results = new DirectFieldBindingResult(newUser, ModelKeys.USER);
        controller.update(newUser, results, model, redirectAttributes);
        assertEquals("Expected password errors", 1, results.getErrorCount());
        assertEquals("Expected password errors", "confirmPassword.required", results.getFieldError().getCode());
        // password confirm not equal:
        newUser.setPassword("1234");
        newUser.setConfirmPassword("12345");
        results = new DirectFieldBindingResult(newUser, ModelKeys.USER);
        controller.update(newUser, results, model, redirectAttributes);
        assertEquals("Expected password errors", 1, results.getErrorCount());
        assertEquals("Expected password errors", "confirmPassword.mismatch", results.getFieldError().getCode());

        // ok request
        newUser.setPassword("1234");
        newUser.setConfirmPassword("1234");
        results = new DirectFieldBindingResult(newUser, ModelKeys.USER);
        controller.update(newUser, results, model, redirectAttributes);
        assertEquals("Expected password errors", 0, results.getErrorCount());


    }
}
