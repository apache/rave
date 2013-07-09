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

package org.apache.rave.portal.model.util;

import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ModelUtilsTest {
    String VALID_REGION_WIDGET_ID = "1";

    @Test
    public void normalizeRegionWidgetPreference() {
        RegionWidgetPreference testPreference = new RegionWidgetPreferenceImpl( null, "camelCaseName", "FOO");
        ModelUtils.normalizeRegionWidgetPreference(VALID_REGION_WIDGET_ID, testPreference);

        assertTrue(testPreference.getRegionWidgetId() == VALID_REGION_WIDGET_ID);
    }

    @Test
    public void normalizeRegionWidgetPreferences() {
        List<RegionWidgetPreference> testPreferences = getTestRegionWidgetPreferences();
        ModelUtils.normalizeRegionWidgetPreferences(VALID_REGION_WIDGET_ID, testPreferences);
        for (RegionWidgetPreference testPreference : testPreferences) {
            assertTrue(testPreference.getRegionWidgetId() == VALID_REGION_WIDGET_ID);
        }
    }

    public List<RegionWidgetPreference> getTestRegionWidgetPreferences() {
        return Arrays.asList((RegionWidgetPreference)new RegionWidgetPreferenceImpl( null, "camelCaseName", "FOO"),
                (RegionWidgetPreference)new RegionWidgetPreferenceImpl("20", "lowercasename", "FOO"),
                (RegionWidgetPreference)new RegionWidgetPreferenceImpl( "-100", "UPPERCASENAME", "FOO"));
    }
}