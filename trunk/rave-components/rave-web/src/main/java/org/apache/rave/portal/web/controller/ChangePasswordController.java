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

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.model.User;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.controller.util.ModelUtils;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.ChangePasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller which processes change password requests.
 * Only requests that have a valid (matched) forgotPasswordHash will be honored
 *
 * @version "$Id$"
 * @see org.apache.rave.model.User#getForgotPasswordHash()
 */
@Controller
public class ChangePasswordController {


    private static Logger log = LoggerFactory.getLogger(ChangePasswordController.class);
    private final UserService userService;
    private final ChangePasswordValidator passwordValidator;
    @Value("#{messages['page.newpassword.message.success']}")
    private String messagePasswordChanged;


    @Autowired
    public ChangePasswordController(UserService userService, ChangePasswordValidator passwordValidator) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
    }


    @RequestMapping(value = {"/changepassword/{passwordHash:.*}"}, method = RequestMethod.GET)
    public String initialize(Model model, @PathVariable("passwordHash") String passwordHash) {
        log.debug("Requesting user for hash: {}", passwordHash);
        User user = new UserImpl();
        model.addAttribute(ModelKeys.USER, user);
        user.setForgotPasswordHash(passwordHash);
        return ViewNames.PASSWORD_CHANGE;
    }


    @RequestMapping(value = {"/changepassword", "/changepassword/**"}, method = RequestMethod.POST)
    public String update(@ModelAttribute UserForm user, BindingResult results, Model model, RedirectAttributes redirectAttributes) {
        log.debug("updating user password for hash {}", user.getForgotPasswordHash());
        model.addAttribute(ModelKeys.USER, user);
        passwordValidator.validate(user, results);

        if (results.hasErrors()) {
            log.info("changepassword, request contains validation errors");
            return ViewNames.PASSWORD_CHANGE;
        }
        try {
            log.debug("Submitted passwords were valid");
            userService.updatePassword(ModelUtils.convert(user));
            redirectAttributes.addFlashAttribute(ModelKeys.REDIRECT_MESSAGE, messagePasswordChanged);
            return ViewNames.REDIRECT_LOGIN ;
        } catch (Exception ex) {
            results.reject("Unable to change password:" + ex.getMessage(), "Unable to change password.");
            return ViewNames.PASSWORD_CHANGE;
        }

    }
}
