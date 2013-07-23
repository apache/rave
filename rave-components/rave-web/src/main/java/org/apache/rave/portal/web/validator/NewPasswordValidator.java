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

package org.apache.rave.portal.web.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.User;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validates password change requests. Valid requests are only ones for users we can find by email address
 *
 * @version "$Id$"
 */
@Component
public class NewPasswordValidator extends NewAccountValidator {

    private static Logger log = LoggerFactory.getLogger(NewPasswordValidator.class);


    @Autowired
    public NewPasswordValidator(UserService userService) {
        super(userService);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("Password validator called");
        User newUser = (User) target;
        // we only check for existing (and valid) email
        String email = newUser.getEmail();
        validateEmail(errors, email);
        if (errors.hasErrors()) {
            return;
        }
        // check if account exists and if it is locked or expired:
        User user = getUserService().getUserByEmail(email);
        if (user == null) {
            errors.rejectValue(FIELD_EMAIL, "account.invalid");
            log.info("Couldn't find user for email {}", email);
            return;
        }
        if (user.isLocked() || user.isExpired() || !user.isEnabled()) {
            errors.rejectValue(FIELD_EMAIL, "account.invalid");
        }

    }

    private void validateEmail(Errors errors, String email) {
        if (StringUtils.isBlank(email)) {
            errors.rejectValue(FIELD_EMAIL, "email.required");
        } else if (isInvalidEmailAddress(email)) {
            errors.rejectValue(FIELD_EMAIL, "email.invalid");
        } else if (!isExistingEmailAddress(email)) {
            errors.rejectValue(FIELD_EMAIL, "email.doesnot.exist");
        }
    }


}
