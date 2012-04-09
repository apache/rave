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

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.service.CaptchaService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.impl.ReCaptchaService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewPasswordValidator;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @version "$Id$"
 */
public class ReminderControllerTest {

    private CaptchaService captchaService;
    private HttpServletRequest request;
    private ReminderController controller;

    @Before
    public void setUp() throws Exception {

        UserService userService = createNiceMock(UserService.class);
        replay(userService);
        request = new MockHttpServletRequest();
        NewPasswordValidator passwordValidator = new NewPasswordValidator(userService);
        captchaService = new ReCaptchaService(false, null, null, false, "error message");
        controller = new ReminderController(userService, passwordValidator, captchaService);
    }


    @Test
    public void testInitialize() throws Exception {
        // test is model is added:
        final ModelMap model = new ModelMap();
        controller.initialize(model, request);
        assertThat(model, CoreMatchers.notNullValue());
        // captcha & user mode should be
        assertEquals("Expected Captcha and NewUser model", 2, model.size());
        assertThat(model.containsAttribute(ModelKeys.NEW_USER), CoreMatchers.equalTo(true));
        assertThat(model.containsAttribute(ModelKeys.CAPTCHA_HTML), CoreMatchers.equalTo(true));
        assertThat(model.get(ModelKeys.NEW_USER), CoreMatchers.notNullValue());
        assertThat(model.get(ModelKeys.CAPTCHA_HTML), CoreMatchers.notNullValue());
        assertThat(captchaService.isValid(request), CoreMatchers.equalTo(true));

    }

    @Test
    public void testCreate() throws Exception {
        Model model = createNiceMock(Model.class);
        NewUser newUser = new NewUser();
        BindingResult results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        RedirectAttributes redirectAttributes = createNiceMock(RedirectAttributes.class);
        replay(redirectAttributes);
        replay(model);

        // user part
        // required email
        controller.requestUsername(newUser, results, model, request, redirectAttributes);
        assertThat(captchaService.isValid(request), CoreMatchers.equalTo(true));
        assertEquals("Expected email errors", 1, results.getErrorCount());
        assertEquals("Expected email errors", "email.required", results.getFieldError().getCode());
        // invalid email
        results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        newUser.setEmail("test_email");
        controller.requestUsername(newUser, results, model, request, redirectAttributes);
        assertEquals("Expected email errors", "email.invalid", results.getFieldError().getCode());
        // does not exists
        results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        newUser.setEmail("test@mail.com");
        String viewResult = controller.requestUsername(newUser, results, model, request, redirectAttributes);
        assertEquals("Expected email errors", 1, results.getErrorCount());
        assertEquals("Expected email errors", "email.doesnot.exist", results.getFieldError().getCode());
        assertThat(viewResult, CoreMatchers.equalTo(ViewNames.USERNAME_REQUEST));
        // password part:
        model = createNiceMock(Model.class);
        newUser = new NewUser();
        results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        redirectAttributes = createNiceMock(RedirectAttributes.class);
        replay(redirectAttributes);
        replay(model);
        // required email
        controller.requestPassword(newUser, results, model, request, redirectAttributes);
        assertThat(captchaService.isValid(request), CoreMatchers.equalTo(true));
        assertEquals("Expected email errors", 1, results.getErrorCount());
        assertEquals("Expected email errors", "email.required", results.getFieldError().getCode());
        // invalid email
        results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        newUser.setEmail("test_email");
        controller.requestPassword(newUser, results, model, request, redirectAttributes);
        assertEquals("Expected email errors", "email.invalid", results.getFieldError().getCode());
        // does not exists
        results = new DirectFieldBindingResult(newUser, ModelKeys.NEW_USER);
        newUser.setEmail("test@mail.com");
        viewResult = controller.requestPassword(newUser, results, model, request, redirectAttributes);
        assertEquals("Expected email errors", 1, results.getErrorCount());
        assertEquals("Expected email errors", "email.doesnot.exist", results.getFieldError().getCode());
        assertThat(viewResult, CoreMatchers.equalTo(ViewNames.NEW_PASSWORD_REQUEST));

    }
}
