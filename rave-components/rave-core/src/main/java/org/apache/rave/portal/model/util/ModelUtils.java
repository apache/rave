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

import java.util.List;

public class ModelUtils {

    private ModelUtils(){}

    /**
     * Represents the format used when converting {@link java.util.Date} objects to string representations for persistence
     *
     * ISO-8601 Compliant
     *
     * YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+0100)
     */

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static void normalizeRegionWidgetPreferences(String regionWidgetId, List<RegionWidgetPreference> preferences) {
        for (RegionWidgetPreference preference : preferences) {
            normalizeRegionWidgetPreference(regionWidgetId, preference);
        }
    }

    public static void normalizeRegionWidgetPreference(String regionWidgetId, RegionWidgetPreference regionWidgetPreference) {
        regionWidgetPreference.setRegionWidgetId(regionWidgetId);
    }
}
