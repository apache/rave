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
import static org.apache.rave.portal.web.util.PortalPreferenceKeys.*;

/**
 * Test for {@link PortalPreferenceForm}
 */
public class PortalPreferenceFormTest {
    private Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

    @Before
    public void setUp() throws Exception {
        PortalPreference titlePref = new PortalPreference(TITLE_SUFFIX, "Test portal");
        preferenceMap.put(TITLE_SUFFIX, titlePref);
        PortalPreference pageSizePref = new PortalPreference(PAGE_SIZE, "20");
        preferenceMap.put(PAGE_SIZE, pageSizePref);
        PortalPreference javaScriptDebugMode = new PortalPreference(JAVASCRIPT_DEBUG_MODE, "0");
        preferenceMap.put(JAVASCRIPT_DEBUG_MODE, javaScriptDebugMode);
    }

    @Test
    public void testForm_populatedMap() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        assertEquals("Test portal", form.getTitleSuffix().getValue());
        assertEquals("20", form.getPageSize().getValue());
        assertEquals("0", form.getJavaScriptDebugMode().getValue());
    }

    @Test
    public void testForm_emptyMap() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm(new HashMap<String, PortalPreference>());
        assertEquals(PortalPreferenceForm.DEFAULT_PAGE_SIZE, form.getPageSize().getValue());
        assertEquals(PortalPreferenceForm.DEFAULT_TITLE_SUFFIX, form.getTitleSuffix().getValue());
        assertEquals(PortalPreferenceForm.DEFAULT_JAVASCRIPT_DEBUG_MODE, form.getJavaScriptDebugMode().getValue());
    }
}
