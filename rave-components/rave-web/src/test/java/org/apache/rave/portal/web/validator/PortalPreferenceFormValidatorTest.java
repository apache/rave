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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.portal.web.model.PortalPreferenceForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

import static org.apache.rave.portal.web.util.PortalPreferenceKeys.PAGE_SIZE;
import static org.apache.rave.portal.web.util.PortalPreferenceKeys.TITLE_SUFFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
Test for {@link {PortalPreferenceFormValidator}
 */
public class PortalPreferenceFormValidatorTest {
    private PortalPreferenceFormValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new PortalPreferenceFormValidator();
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(validator.supports(PortalPreferenceForm.class));
    }

    @Test
    public void testValidate_Valid() throws Exception {
        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();
        preferenceMap.put(TITLE_SUFFIX, new PortalPreferenceImpl(TITLE_SUFFIX, "- Rave unit test"));
        preferenceMap.put(PAGE_SIZE, new PortalPreferenceImpl(PAGE_SIZE, "10"));
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        Errors errors = new BindException(form, "form");
        validator.validate(form, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void testValidate_missingRequiredFields() throws Exception {
        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        form.getPageSize().setValue(null);
        Errors errors = new BindException(form, "form");
        validator.validate(form, errors);

        assertEquals(2, errors.getErrorCount());
        assertNotNull(errors.getFieldError("pageSize.value"));
    }

    @Test
    public void testValidate_InvalidPageSize() throws Exception {
        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();
        preferenceMap.put(PAGE_SIZE, new PortalPreferenceImpl(PAGE_SIZE, "10.5"));
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        Errors errors = new BindException(form, "form");
        validator.validate(form, errors);

        assertEquals(1, errors.getErrorCount());
        assertNotNull(errors.getFieldError("pageSize.value"));
    }

}
