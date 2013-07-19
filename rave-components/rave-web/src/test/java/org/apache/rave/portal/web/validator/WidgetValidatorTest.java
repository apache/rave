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

import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link WidgetValidator}
 */
public class WidgetValidatorTest {
    WidgetValidator widgetValidator;

    @Before
    public void setUp() throws Exception {
        widgetValidator = new MockWidgetValidator();

    }

    @Test
    public void testSupports() throws Exception {
        assertTrue("Supports org.apache.rave.model.Widget", widgetValidator.supports(WidgetImpl.class));
    }

  /*
    * Testing the same method of URL validator used in validate method. Its was really hard to create Error object
    */
 @Test
 public void testValidation() throws Exception {
      RegexValidator regex = new RegexValidator(new String[]{"http","https","((localhost)(:[0-9]+))",".*\\.linux-server(:[0-9]+)"});
      UrlValidator validator = new UrlValidator(regex, 0);
      assertTrue("localhost URL should validate",
                validator.isValid("http://localhost:8080/demogadgets/CTSSResourcesMapView.xml"));
        assertTrue("127.0.0.1 should validate",
                validator.isValid("http://127.0.0.1:8080/demogadgets/CTSSResourcesMapView.xml"));
        assertTrue("my.linux-server should validate",
                validator.isValid("http://my.linux-server:8080/demogadgets/CTSSResourcesMapView.xml"));

        assertFalse("broke.my-test should not validate",
                validator.isValid("http://broke.my-test/test/index.html"));

        assertTrue("www.apache.org should still validate",
                validator.isValid("http://www.apache.org/test/index.html"));
    }

     private class MockWidgetValidator extends WidgetValidator {

        @Override
        protected void validateIfWidgetAlreadyExists(Widget widget, Errors errors) {
        }
    }
}
