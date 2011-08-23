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
//TODO is this the right package name convention?
package org.apache.rave.portal.web.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

public class NewAccountValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String USERNAME_PATTERN = "[\\w\\+\\-\\.@]{2,}";
    private UserService userService;

    @Autowired
    public NewAccountValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class aClass) {
        return NewUser.class.isAssignableFrom(aClass);
    }

    public void validate(Object obj, Errors errors) {
        logger.debug("Validator called");
        NewUser newUser = (NewUser) obj;

        //check if the username is null or empty
        final String username = newUser.getUsername();
        if (StringUtils.isBlank(username)) {
            errors.rejectValue("username", "username.required");
            logger.info("Username required");
        }

        // at least 2 characters of the following: a-z A-Z 0-9 _ - + . @
        else if (!username.matches(USERNAME_PATTERN)) {
            errors.rejectValue("username", "username.invalid.pattern");
            logger.info("Username must be atleast 2 characters long");
        }

        //check if username is already in use
        else if (userService.getUserByUsername(username) != null) {
            errors.rejectValue("username", "username.exits");
            logger.info("Username already exists");
        }


        //check if the password is null
        if (StringUtils.isBlank(newUser.getPassword())) {
            errors.rejectValue("password", "password.required");
            logger.info("Password required");
        }

        //check if the password length is less than 4
        else if (newUser.getPassword().length() < 4) {
            errors.rejectValue("password", "password.invalid.lenght");
            logger.info("Password must be atleast 4 characters long");
        }

        //check if the confirm password is null
        if (StringUtils.isBlank(newUser.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "confirmPassword.required");
            logger.info("Confirm Password required");
        }

        //check if the confirm password matches the previous entered password
        //first check for null
        if (newUser.getConfirmPassword() != null &&
                !(newUser.getConfirmPassword().equals(newUser.getPassword()))) {
            errors.rejectValue("confirmPassword", "confirmPassword.mismatch");
            logger.info("Password mismatch");
        }

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.info("Validation error: " + error.toString());
            }
        } else {
            logger.debug("Validation successful");
        }
    }
}