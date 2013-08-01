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

import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.rest.model.SearchResult;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RegionWidgetService {
    /**
     * Gets the RegionWidget with the specified ID.
     *
     * @param regionWidgetId The ID of the RegionWidget to fetch.
     * @return The RegionWidget or null if not found.
     */
    @PostAuthorize("hasPermission(returnObject, 'read')")
    RegionWidget getRegionWidget(String regionWidgetId);

    //TODO: Put correct spring security annotations on following three methods (getAll, getLimitedList, getCountAll)
    /**
     * Gets a {@link org.apache.rave.rest.model.SearchResult} for {@link RegionWidget}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<RegionWidget> getAll();


    /**
     * Gets a limited {@link org.apache.rave.rest.model.SearchResult} for {@link RegionWidget}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<RegionWidget> getLimitedList(int offset, int pageSize);

    /**
     * Saves the given RegionWidget and returns the updated instance.
     *
     * @param regionWidget The RegionWidget to save.
     * @return The updated RegionWidget with all ID numbers populated.
     */
    @PreAuthorize("hasPermission(#regionWidget.id, 'org.apache.rave.model.RegionWidget', 'update')")
    RegionWidget saveRegionWidget(RegionWidget regionWidget);

    /**
     * Saves the new collection of RegionWidgetPreference, replacing any existing preferences that may already exist.
     *
     * @param regionWidgetId The ID of the RegionWidget to save preferences for.
     * @param preferences    The collection of new RegionWidgetPreferences.
     * @return The updated RegionWidgetPreferences with all ID numbers populated.
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'update')")
    List<RegionWidgetPreference> saveRegionWidgetPreferences(String regionWidgetId,
                                                             List<RegionWidgetPreference> preferences);

    /**
     * Saves the given RegionWidgetPreference for the given RegionWidget, replacing the existing preference value if
     * it already exists.
     *
     * @param regionWidgetId The ID of the RegionWidget to save the preference for.
     * @param preference     The RegionWidgetPreference to save.
     * @return The updated RegionWidgetPreference with all ID numbers populated.
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'update')")
    RegionWidgetPreference saveRegionWidgetPreference(String regionWidgetId, RegionWidgetPreference preference);
    
    /**
     * Saves the collapsed state of the given regionWidgetId
     * 
     * @param regionWidgetId The ID of the RegionWidget to save the collapsed value for
     * @param collapsed the collapsed state of the RegionWidget
     * @return The updated RegionWidget with the new collapsed value
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'update')")
    RegionWidget saveRegionWidgetCollapsedState(String regionWidgetId, boolean collapsed);
}
