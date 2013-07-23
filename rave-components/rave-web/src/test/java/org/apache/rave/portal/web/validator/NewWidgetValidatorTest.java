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

import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.WidgetService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link NewWidgetValidator}
 */
public class NewWidgetValidatorTest {

    private static final String VALID_TITLE = "My widget";
    private static final String VALID_URL = "http://example.com/widget.xml";
    private static final String VALID_TYPE = "OpenSocial";
    private static final String VALID_DESCRIPTION = "Lorem ipsum dolor sit amet.";
    private static final String WIDGET = "widget";

    private NewWidgetValidator widgetValidator;
    private WidgetService widgetService;

    @Test
    public void testValidateValidFormData() throws Exception {
        Widget widget = new WidgetImpl();
        widget.setTitle(VALID_TITLE);
        widget.setUrl(VALID_URL);
        widget.setType(VALID_TYPE);
        widget.setDescription(VALID_DESCRIPTION);
        Errors errors = new BindException(widget, WIDGET);

        expect(widgetService.isRegisteredUrl(VALID_URL)).andReturn(false);
        replay(widgetService);
        widgetValidator.validate(widget, errors);
        verify(widgetService);

        assertFalse("No validation errors", errors.hasErrors());
    }

    @Test
    public void testValidationFailsOnEmptyValues() {
        Widget widget = new WidgetImpl();
        Errors errors = new BindException(widget, WIDGET);

        widgetValidator.validate(widget, errors);

        assertEquals(4, errors.getErrorCount());
    }

    @Test
    public void testValidationFailsOnDuplicateUrl() {
        final String existingUrl = "http://example.com/existing_widget.xml";

        WidgetImpl widget = new WidgetImpl();
        widget.setId("123");
        widget.setTitle(VALID_TITLE);
        widget.setType(VALID_TYPE);
        widget.setDescription(VALID_DESCRIPTION);
        widget.setUrl(existingUrl);

        Widget newWidget = new WidgetImpl();
        newWidget.setTitle(VALID_TITLE);
        newWidget.setType(VALID_TYPE);
        newWidget.setDescription(VALID_DESCRIPTION);
        newWidget.setUrl(existingUrl);
        Errors errors = new BindException(newWidget, WIDGET);

        expect(widgetService.isRegisteredUrl(existingUrl)).andReturn(true);
        replay(widgetService);

        widgetValidator.validate(newWidget, errors);
        verify(widgetService);
        assertEquals(1, errors.getErrorCount());
        assertNotNull("Field error for duplicate url", errors.getFieldError("url"));
    }

    @Test
    public void testValidationFailsOnInvalidUrl() {
        Widget widget = new WidgetImpl();
        widget.setTitle(VALID_TITLE);
        widget.setType(VALID_TYPE);
        widget.setUrl("http:/this.is/invalid?url=true&reject=true");
        widget.setScreenshotUrl("https://///invalid/screenshot");
        widget.setThumbnailUrl("thumbnail");
        widget.setTitleUrl("titleUrl");
        widget.setDescription(VALID_DESCRIPTION);
        Errors errors = new BindException(widget, WIDGET);

        widgetValidator.validate(widget, errors);
        assertEquals(4, errors.getErrorCount());
        assertNotNull("Field error on url", errors.getFieldError("url"));
        assertNotNull("Field error on screenshot url", errors.getFieldError("screenshotUrl"));
        assertNotNull("Field error on thumbnail url", errors.getFieldError("thumbnailUrl"));
        assertNotNull("Field error on title url", errors.getFieldError("titleUrl"));
    }

    @Before
    public void setup() {
        widgetService = createMock(WidgetService.class);
        widgetValidator = new NewWidgetValidator(widgetService);
    }
}
