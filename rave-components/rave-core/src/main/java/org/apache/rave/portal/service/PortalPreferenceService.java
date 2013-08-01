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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.rest.model.SearchResult;

import java.util.List;
import java.util.Map;

/**
 * Service for portal preferences
 */
public interface PortalPreferenceService {

    /**
     * Creates a Map of all {@link org.apache.rave.model.PortalPreference}'s using the preference key as key for the Map.Entry
     *
     * @return Map of PortalPreference's
     */
    Map<String, PortalPreference> getPreferencesAsMap();

    /**
     * Gets a {@link org.apache.rave.model.PortalPreference} by its key
     *
     * @param key unique name of the preference
     * @return PortalPreference if it exists, otherwise {@literal null}
     */
    PortalPreference getPreference(String key);

    //TODO: Put correct spring security annotations on following three methods (getAll, getLimitedList, getCountAll)
    /**
     * Gets a {@link org.apache.rave.rest.model.SearchResult} for {@link PortalPreference}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<PortalPreference> getAll();


    /**
     * Gets a limited {@link org.apache.rave.rest.model.SearchResult} for {@link PortalPreference}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<PortalPreference> getLimitedList(int offset, int pageSize);

    /**
     * Saves a {@link org.apache.rave.model.PortalPreference} with a single value.
     * If a PortalPreference already exists with this key, its value(s) will be overwritten.
     *
     * @param key   of the preference, e.g. {@literal title}
     * @param value of the preference, e.g. {@literal "Rave"}
     */
    void savePreference(String key, String value);

    /**
     * Saves a {@link org.apache.rave.model.PortalPreference} with a List of values.
     * If a PortalPreference already exists with this key, its value(s) will be overwritten.
     *
     * @param key    of the preference, e.g. {@literal colors}
     * @param values List of values of the preference, e.g. {@literal red}, {@literal yellow}, {@literal blue}
     */
    void savePreference(String key, List<String> values);

    /**
     * Saves a {@link org.apache.rave.model.PortalPreference}
     *
     * @param preference PortalPreference to save
     */
    void savePreference(PortalPreference preference);
}
