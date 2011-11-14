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

import java.util.HashMap;
import java.util.Map;

/**
 * Form object for portal preferences
 */
public class PortalPreferenceForm {

    public static final String KEY_PAGE_SIZE = "pageSize";
    public static final String DEFAULT_PAGE_SIZE = "10";

    public static final String KEY_TITLE_SUFFIX = "titleSuffix";
    public static final String DEFAULT_TITLE_SUFFIX = "- Rave";


    private Map<String, PortalPreference> preferenceMap;
    
    public PortalPreferenceForm() {
        // TODO RAVE-355 populate preferences if they don't exist in the db
        this(new HashMap<String, PortalPreference>());
    }

    public PortalPreferenceForm(Map<String, PortalPreference> preferenceMap) {
        super();
        this.preferenceMap = preferenceMap;
    }

    public PortalPreference getPageSize() {
        return preferenceMap.get(KEY_PAGE_SIZE);
    }

    public void setPageSize(PortalPreference pageSize) {
        preferenceMap.put(KEY_PAGE_SIZE, pageSize);
    }

    public PortalPreference getTitleSuffix() {
        return preferenceMap.get(KEY_TITLE_SUFFIX);
    }

    public void setTitleSuffix(PortalPreference titleSuffix) {
        preferenceMap.put(KEY_TITLE_SUFFIX, titleSuffix);
    }

    public Map<String, PortalPreference> getPreferenceMap() {
        return preferenceMap;
    }

    public void setPreferenceMap(Map<String, PortalPreference> preferenceMap) {
        this.preferenceMap = preferenceMap;
    }
}
