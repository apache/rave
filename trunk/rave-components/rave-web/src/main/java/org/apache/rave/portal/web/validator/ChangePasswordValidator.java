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

import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.model.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Validates  new password changes
 * @version "$Id$"
 */
@Component
public class ChangePasswordValidator extends NewAccountValidator {


    private static Logger log = LoggerFactory.getLogger(ChangePasswordValidator.class);

    @Value("${portal.mail.passwordservice.valid.minutes}")
    private int minutesValid;

    @Autowired
    public ChangePasswordValidator(UserService userService) {
        super(userService);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("ChangePasswordValidator validator called");
        UserForm newUser = (UserForm) target;
        boolean validHash = getUserService().isValidReminderRequest(newUser.getForgotPasswordHash(), minutesValid);
        if (!validHash) {
            errors.rejectValue(FIELD_PASSWORD, "page.changepassword.expired");
            // skip further validating anything else, does not make sense
            return;
        }

        // we only check for password:
        validatePassword(errors, newUser);
        validateConfirmPassword(errors, newUser);

    }

}
