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

import org.apache.rave.portal.model.Widget;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test class for {@link NewWidgetValidator}
 */
public class NewWidgetValidatorTest {

    private static final String VALID_TITLE = "My widget";
    private static final String VALID_URL = "http://example.com/widget.xml";
    private static final String VALID_TYPE = "OpenSocial";
    private static final String WIDGET = "widget";

    private NewWidgetValidator newWidgetValidator;

    @Test
    public void testSupports() throws Exception {
        assertTrue("Supports org.apache.rave.portal.model.Widget", newWidgetValidator.supports(Widget.class));
    }

    @Test
    public void testValidateValidFormData() throws Exception {
        Widget widget = new Widget();
        widget.setTitle(VALID_TITLE);
        widget.setUrl(VALID_URL);
        widget.setType(VALID_TYPE);
        Errors errors = new BindException(widget, WIDGET);

        newWidgetValidator.validate(widget, errors);
        assertFalse("No validation errors", errors.hasErrors());
    }

    @Test
    public void testValidationFailsOnEmptyValues() {
        Widget widget = new Widget();
        Errors errors = new BindException(widget, WIDGET);

        newWidgetValidator.validate(widget, errors);

        assertEquals(3, errors.getErrorCount());
    }

    @Test
    public void testValidationFailsOnInvalidUrl() {
        Widget widget = new Widget();
        widget.setTitle(VALID_TITLE);
        widget.setType(VALID_TYPE);
        widget.setUrl("http:/this.is/invalid?url=true&reject=true");
        widget.setScreenshotUrl("https://///invalid/screenshot");
        widget.setThumbnailUrl("thumbnail");
        Errors errors = new BindException(widget, WIDGET);

        newWidgetValidator.validate(widget, errors);
        assertEquals(3, errors.getErrorCount());
        assertNotNull("Field error on url", errors.getFieldError("url"));
        assertNotNull("Field error on screenshot url", errors.getFieldError("screenshotUrl"));
        assertNotNull("Field error on thumbnail url", errors.getFieldError("thumbnailUrl"));
    }

    @Before
    public void setup() {
        newWidgetValidator = new NewWidgetValidator();
    }
}
