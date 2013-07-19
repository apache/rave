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
package org.apache.rave.portal.web.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.rave.model.User;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

@Component
public class UserProfileValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;
    private static final String FIELD_EMAIL = "email";

    @Autowired
    public UserProfileValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    public void validate(Object obj, Errors errors) {
        logger.debug("Validator called");
        User user = (User) obj;

        //check if the password is null or empty
        if (StringUtils.isBlank(user.getPassword())) {
            errors.rejectValue("password", "password.required");
            logger.info("Password required");
        }
        //check if the password length is less than 4
        else if (user.getPassword().length() < 4) {
            errors.rejectValue("password", "password.invalid.length");
            logger.info("Password must be at least 4 characters long");
        }
        //check if the confirm password is null or empty
        if (StringUtils.isBlank(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "confirmPassword.required");
            logger.info("Confirm Password required");
        }

        //check if the confirm password matches the previous entered password
        if (user.getConfirmPassword() != null && !(user.getConfirmPassword().equals(user.getPassword()))) {
            errors.rejectValue("confirmPassword", "confirmPassword.mismatch");
            logger.info("Password mismatch");
        }

        validateEmail(errors, user);

        writeResultToLog(errors);
    }

    private void validateEmail(Errors errors, User user) {
        final String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            errors.rejectValue(FIELD_EMAIL, "email.required");
        } else if (isInvalidEmailAddress(email)) {
            errors.rejectValue(FIELD_EMAIL, "email.invalid");
        } else if (isExistingEmailAddress(user, email)) {
            errors.rejectValue(FIELD_EMAIL, "email.exists");
        }
    }

    private boolean isInvalidEmailAddress(String emailAddress) {
        return !EmailValidator.getInstance().isValid(emailAddress);
    }

    private boolean isExistingEmailAddress(User user, String email) {
        final User userByEmail = userService.getUserByEmail(email);
        return userByEmail != null && !userByEmail.getId().equals(user.getId());
    }

        private void writeResultToLog(Errors errors) {
        if (errors.hasErrors()) {
            if (logger.isInfoEnabled()) {
                for (ObjectError error : errors.getAllErrors()) {
                    logger.info("Validation error: {}", error.toString());
                }
            }
        } else {
            logger.debug("Validation successful");
        }
    }
}
