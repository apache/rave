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

package org.apache.rave.portal.web.controller;

import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.CaptchaService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.controller.util.ModelUtils;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewPasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles password ans username reminder requests
 *
 * @version "$Id$"
 */
@Controller
public class ReminderController {

    private static Logger log = LoggerFactory.getLogger(ReminderController.class);

    private final UserService userService;
    private final NewPasswordValidator passwordValidator;
    private final CaptchaService captchaService;


    @Autowired
    protected ReminderController(UserService userService, NewPasswordValidator passwordValidator, CaptchaService captchaService) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.captchaService = captchaService;
    }


    @RequestMapping(value = {"/retrieveusername", "/newpassword"})
    public String initialize(ModelMap model, HttpServletRequest request) {
        model.addAttribute(ModelKeys.CAPTCHA_HTML, captchaService.createHtml(request));
        model.addAttribute(ModelKeys.USER, new UserImpl());
        return "/retrieveusername".equals(request.getPathInfo()) ? ViewNames.USERNAME_REQUEST :
                ViewNames.NEW_PASSWORD_REQUEST;
    }


    /**
     * Processes username requests
     */
    @RequestMapping(value = {"/retrieveusername"}, method = RequestMethod.POST)
    public String requestUsername(@ModelAttribute UserForm userForm, BindingResult results, Model model,
                                  HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.debug("Requesting username reminder");
        User user = ModelUtils.convert(userForm);
        if (!validateEmail(user, results, model, request)) {
            return captchaRequest(model, request, ViewNames.USERNAME_REQUEST);
        }
        try {
            userService.sendUserNameReminder(user);
            populateRedirect(user, redirectAttributes);
            return ViewNames.REDIRECT_RETRIEVE_USERNAME;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("Exception while sending username reminder  {}", e);
            }
            results.reject("Unable to send username reminder :" + e.getMessage(), "Unable to send username reminder.");
            return captchaRequest(model, request, ViewNames.USERNAME_REQUEST);
        }
    }


    /**
     * Processes new password requests
     */
    @RequestMapping(value = {"/newpassword"}, method = RequestMethod.POST)
    public String requestPassword(@ModelAttribute UserForm userForm, BindingResult results, Model model,
                                  HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.debug("Requesting password reminder");
        User user = ModelUtils.convert(userForm);
        if (!validateEmail(user, results, model, request)) {
            return captchaRequest(model, request, ViewNames.NEW_PASSWORD_REQUEST);
        }
        try {
            userService.sendPasswordReminder(user);
            populateRedirect(user, redirectAttributes);
            return ViewNames.REDIRECT_NEW_PASSWORD;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("Exception while sending password reminder  {}", e);
            }
            results.reject("Unable to send password reminder :" + e.getMessage(), "Unable to send password reminder.");
            return captchaRequest(model, request, ViewNames.NEW_PASSWORD_REQUEST);
        }


    }


    private boolean validateEmail(User user, BindingResult results, Model model, HttpServletRequest request) {
        model.addAttribute(ModelKeys.USER, user);
        passwordValidator.validate(user, results);
        if (results.hasErrors()) {
            log.info("newpassword request contains validation errors");
            return false;
        }

        String email = user.getEmail();
        log.debug("Submitted email {} is valid", email);
        if (!captchaService.isValid(request)) {
            log.debug("Captcha was invalid for user with email {}", email);
            return false;
        }
        return true;
    }


    private void populateRedirect(User newUser, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("email", newUser.getEmail());
    }

    private String captchaRequest(Model model, HttpServletRequest request, String view) {
        model.addAttribute(ModelKeys.CAPTCHA_HTML, captchaService.createHtml(request));
        return view;
    }

}
