/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.web.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.apache.rave.portal.model.Widget;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for adding a new {@link Widget}
 */
public class NewWidgetValidator implements Validator {
    private static final String FIELD_URL = "url";

    private UrlValidator validator;

    public NewWidgetValidator() {
        super();
        String[] allowedSchemes = {"http", "https"};
        validator = new UrlValidator(allowedSchemes);
    }

    /**
     * Supports {@link Widget}
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Widget.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        Widget widget = (Widget) target;

        validateRequiredFields(errors);
        validateUrlFields(widget, errors);
    }

    /**
     * Checks if the required fields contain a value
     *
     * @param errors {@link Errors}
     */
    private void validateRequiredFields(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "widget.title.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_URL, "widget.url.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "widget.type.required");
    }

    /**
     * Validates fields that may contain a URL
     *
     * @param widget {@link Widget} to validate
     * @param errors {@link org.springframework.validation.Errors}
     */
    private void validateUrlFields(Widget widget, Errors errors) {
        String url = widget.getUrl();
        if (StringUtils.isNotBlank(url) && !validator.isValid(url)) {
            errors.rejectValue(FIELD_URL, "widget.url.malformed");
        }

        String screenshotUrl = widget.getScreenshotUrl();
        if (StringUtils.isNotBlank(screenshotUrl) && !validator.isValid(screenshotUrl)) {
            errors.rejectValue("screenshotUrl", "widget.screenshotUrl.malformed");
        }

        String thumbnailUrl = widget.getThumbnailUrl();
        if (StringUtils.isNotBlank(thumbnailUrl) && !validator.isValid(thumbnailUrl)) {
            errors.rejectValue("thumbnailUrl", "widget.screenshotUrl.malformed");
        }
    }
}
