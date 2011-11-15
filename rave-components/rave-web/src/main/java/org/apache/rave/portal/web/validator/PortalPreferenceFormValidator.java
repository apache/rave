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

import org.apache.rave.portal.web.model.PortalPreferenceForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates {@link PortalPreferenceForm}
 */
@Component
public class PortalPreferenceFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PortalPreferenceForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PortalPreferenceForm form = (PortalPreferenceForm) target;

        validateRequiredFields(errors);
        validatePageSize(form, errors);
    }

    private void validateRequiredFields(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pageSize.value", "form.field.error.required");
    }

    private void validatePageSize(PortalPreferenceForm form, Errors errors) {
        if (form.getPageSize() != null) {
            int pageSizeValue;
            try {
                pageSizeValue = Integer.parseInt(form.getPageSize().getValue());
            } catch (NumberFormatException e) {
                pageSizeValue = 0;
            }
            if (pageSizeValue <= 0) {
                errors.rejectValue("pageSize.value", "admin.preferencedetail.pageSize.malformed");
            }
        }
    }
}
