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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.rave.portal.web.util.PortalPreferenceKeys.INITIAL_WIDGET_STATUS;
import static org.apache.rave.portal.web.util.PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE;
import static org.apache.rave.portal.web.util.PortalPreferenceKeys.PAGE_SIZE;
import static org.apache.rave.portal.web.util.PortalPreferenceKeys.TITLE_SUFFIX;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link PortalPreferenceForm}
 */
public class PortalPreferenceFormTest {
    private Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

    @Before
    public void setUp() throws Exception {
        PortalPreference titlePref = new PortalPreferenceImpl(TITLE_SUFFIX, "Test portal");
        preferenceMap.put(TITLE_SUFFIX, titlePref);
        PortalPreference pageSizePref = new PortalPreferenceImpl(PAGE_SIZE, "20");
        preferenceMap.put(PAGE_SIZE, pageSizePref);
        PortalPreference javaScriptDebugMode = new PortalPreferenceImpl(JAVASCRIPT_DEBUG_MODE, "0");
        preferenceMap.put(JAVASCRIPT_DEBUG_MODE, javaScriptDebugMode);
        PortalPreference initialWidgetStatus = new PortalPreferenceImpl(INITIAL_WIDGET_STATUS, "PUBLISHED");
        preferenceMap.put(INITIAL_WIDGET_STATUS, initialWidgetStatus);
    }

    @Test
    public void testForm_populatedMap() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        assertEquals("Test portal", form.getTitleSuffix().getValue());
        assertEquals("20", form.getPageSize().getValue());
        assertEquals("0", form.getJavaScriptDebugMode().getValue());
        assertEquals("PUBLISHED", form.getInitialWidgetStatus().getValue());
    }

    @Test
    public void testForm_emptyMap() throws Exception {
        PortalPreferenceForm form = new PortalPreferenceForm(new HashMap<String, PortalPreference>());
        assertEquals(PortalPreferenceForm.DEFAULT_PAGE_SIZE, form.getPageSize().getValue());
        assertEquals(PortalPreferenceForm.DEFAULT_TITLE_SUFFIX, form.getTitleSuffix().getValue());
        assertEquals(PortalPreferenceForm.DEFAULT_JAVASCRIPT_DEBUG_MODE, form.getJavaScriptDebugMode().getValue());
        assertEquals(PortalPreferenceForm.DEFAULT_INITIAL_WIDGET_STATUS, form.getInitialWidgetStatus().getValue());
    }
}
