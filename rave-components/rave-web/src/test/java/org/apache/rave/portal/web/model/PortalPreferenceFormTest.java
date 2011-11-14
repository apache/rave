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

package org.apache.rave.portal.web.model;

import org.apache.rave.portal.model.PortalPreference;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.apache.rave.portal.web.model.PortalPreferenceForm.KEY_PAGE_SIZE;
import static org.apache.rave.portal.web.model.PortalPreferenceForm.KEY_TITLE_SUFFIX;

/**
 * Test for {@link PortalPreferenceForm}
 */
public class PortalPreferenceFormTest {
    private Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

    @Before
    public void setUp() throws Exception {
        PortalPreference titlePref = new PortalPreference(KEY_TITLE_SUFFIX, "Test portal");
        preferenceMap.put(KEY_TITLE_SUFFIX, titlePref);
        PortalPreference pageSizePref = new PortalPreference(KEY_PAGE_SIZE, "20");
        preferenceMap.put(KEY_PAGE_SIZE, pageSizePref);
    }

    @Test
    public void testEmptyConstructor() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm();
        assertNull(form.getPageSize());
        assertNull(form.getTitleSuffix());
        assertTrue(form.getPreferenceMap().isEmpty());
    }

    @Test
    public void testPopulatedForm() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        assertEquals("Test portal", form.getTitleSuffix().getValue());
        assertEquals("20", form.getPageSize().getValue());
    }
}
