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

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.service.CaptchaService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewAccountValidator;
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

import javax.servlet.http.HttpServletRequest;

@Controller
public class NewAccountController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final NewAccountService newAccountService;
    private final NewAccountValidator newAccountValidator;
    private final CaptchaService captchaService;


    @Autowired
    public NewAccountController(NewAccountService newAccountService, NewAccountValidator newAccountValidator, CaptchaService captchaService) {
        this.newAccountService = newAccountService;
        this.captchaService = captchaService;
        this.newAccountValidator = newAccountValidator;
    }

    @RequestMapping(value = "/newaccount.jsp")
    public void setUpForm(ModelMap model, HttpServletRequest request) {
        logger.debug("Initializing form");
        model.addAttribute("captchaHtml", captchaService.createHtml(request));
        model.addAttribute(ModelKeys.NEW_USER, new NewUser());
    }

    @RequestMapping(value = {"/newaccount", "/newacount/*"}, method = RequestMethod.POST)
    public String create(@ModelAttribute NewUser newUser, BindingResult results, Model model, HttpServletRequest request) {
        logger.debug("Creating a new user account");
        model.addAttribute(ModelKeys.NEW_USER, newUser);

        newAccountValidator.validate(newUser, results);
        if (results.hasErrors()) {
            logger.info("newaccount.jsp: shows validation errors");
            initializeCaptcha(model, request);
            return ViewNames.NEW_ACCOUNT;
        }

        //Now attempt to create the account.
        try {
            logger.debug("newaccount.jsp: passed form validation");

            if (captchaService.isValid(request)) {
                newAccountService.createNewAccount(newUser);
                return ViewNames.REDIRECT;
            } else {
                logger.debug("newaccount.jsp: failed  captcha validation");
                results.reject("Captcha validation failed", "Unable to create account, captcha validation failed");
                initializeCaptcha(model, request);
                return ViewNames.NEW_ACCOUNT;
            }
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
            //This exception is thrown if the account already exists.
            logger.info("Account creation failed: ", ex);
            results.reject("Account already exists", "Unable to create account");
            initializeCaptcha(model, request);
            return ViewNames.NEW_ACCOUNT;

        } catch (Exception ex) {
            //TODO RAVE-241 need to handle more specific exceptions
            if (logger.isDebugEnabled()) {
                logger.error("Account creation failed: ", ex);
            } else {
                logger.error("Account creation failed: {}", ex.getMessage());
            }
            results.reject("Unable to create account:" + ex.getMessage(), "Unable to create account");
            initializeCaptcha(model, request);
            return ViewNames.NEW_ACCOUNT;
        }

    }

    private void initializeCaptcha(Model model, HttpServletRequest request) {
        model.addAttribute("captchaHtml", captchaService.createHtml(request));
    }
}