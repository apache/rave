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

package org.apache.rave.portal.service;

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;

import java.util.List;

public interface RegionWidgetService {
    /**
     * Gets the RegionWidget with the specified ID.
     *
     * @param regionWidgetId The ID of the RegionWidget to fetch.
     * @return The RegionWidget or null if not found.
     */
    RegionWidget getRegionWidget(long regionWidgetId);

    /**
     * Saves the given RegionWidget and returns the updated instance.
     *
     * @param regionWidget The RegionWidget to save.
     * @return The updated RegionWidget with all ID numbers populated.
     */
    RegionWidget saveRegionWidget(RegionWidget regionWidget);

    /**
     * Saves the new collection of RegionWidgetPreference, replacing any existing preferences that may already exist.
     *
     * @param regionWidgetId The ID of the RegionWidget to save preferences for.
     * @param preferences    The collection of new RegionWidgetPreferences.
     * @return The updated RegionWidgetPreferences with all ID numbers populated.
     */
    List<RegionWidgetPreference> saveRegionWidgetPreferences(long regionWidgetId,
                                                             List<RegionWidgetPreference> preferences);

    /**
     * Saves the given RegionWidgetPreference for the given RegionWidget, replacing the existing preference value if
     * it already exists.
     *
     * @param regionWidgetId The ID of the RegionWidget to save the preference for.
     * @param preference     The RegionWidgetPreference to save.
     * @return The updated RegionWidgetPreference with all ID numbers populated.
     */
    RegionWidgetPreference saveRegionWidgetPreference(long regionWidgetId, RegionWidgetPreference preference);
}