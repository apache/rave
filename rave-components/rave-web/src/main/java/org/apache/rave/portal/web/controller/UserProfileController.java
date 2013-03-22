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
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.controller.util.ModelUtils;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserProfileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;
    private final UserProfileValidator userProfileValidator;

    @Value("#{messages['page.userprofile.message.success']}")
    private String profileUpdatedMessage;


    @Autowired
    public UserProfileController(UserService userService, UserProfileValidator userProfileValidator) {
        this.userService = userService;
        this.userProfileValidator = userProfileValidator;
    }

    // TODO RAVE-154 why .jsp?
    @RequestMapping(value = "/userProfile.jsp")
    public void setUpForm(ModelMap model) {
        logger.debug("Initializing form");
        User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.USER_PROFILE, user);
    }

    @RequestMapping(value = {"/updateUserProfile", "/updateUserProfile/*"}, method = RequestMethod.POST)
    public String create(@ModelAttribute UserForm user, BindingResult results, Model model, @RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        logger.debug("Updating user profile.");
        model.addAttribute(ModelKeys.USER_PROFILE, user);

        userProfileValidator.validate(user, results);
        if (results.hasErrors()) {
            logger.info("userProfile.jsp: shows validation errors");
            return ViewNames.USER_PROFILE;
        }

        //Now attempt to update the account.
        try {
            logger.debug("userprofile: passed form validation");
            userService.updateUserProfile(ModelUtils.convert(user));
            redirectAttributes.addFlashAttribute(ModelKeys.REDIRECT_MESSAGE, profileUpdatedMessage);
            return ViewNames.REDIRECT_LOGIN;
        }
        //TODO RAVE-154 need to handle more specific exceptions
        catch (Exception ex) {
            logger.info("Account creation failed: {}", ex.getMessage());
            results.reject("Unable to create account:" + ex.getMessage(), "Unable to create account");
            return ViewNames.USER_PROFILE;
        }
    }
}
