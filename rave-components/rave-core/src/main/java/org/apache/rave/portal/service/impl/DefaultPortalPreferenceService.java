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

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.events.PortalPreferenceJavascriptDebugModeSaveEvent;
import org.apache.rave.portal.events.RaveEventManager;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link PortalPreferenceService}
 */
@Service
public class DefaultPortalPreferenceService implements PortalPreferenceService {

    private final PortalPreferenceRepository repository;
    private final RaveEventManager eventManager;

    @Autowired
    public DefaultPortalPreferenceService(PortalPreferenceRepository repository, RaveEventManager manager) {
        this.repository = repository;
        this.eventManager = manager;
    }

    @Override
    public Map<String, PortalPreference> getPreferencesAsMap() {
        final List<PortalPreference> portalPreferences = repository.getAll();

        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();
        for (PortalPreference preference : portalPreferences) {
            preferenceMap.put(preference.getKey(), preference);
        }
        return preferenceMap;
    }

    @Override
    public PortalPreference getPreference(String key) {
        return repository.getByKey(key);
    }

    @Override
    public SearchResult<PortalPreference> getAll() {
        final int count = repository.getCountAll();
        final List<PortalPreference> portalPreferences = repository.getAll();
        return new SearchResult<PortalPreference>(portalPreferences, count);
    }

    @Override
    public SearchResult<PortalPreference> getLimitedList(int offset, int pageSize) {
        final int count = repository.getCountAll();
        final List<PortalPreference> portalPreferences = repository.getLimitedList(offset, pageSize);
        final SearchResult<PortalPreference> searchResult = new SearchResult<PortalPreference>(portalPreferences, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    @Transactional
    public void savePreference(String key, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        this.savePreference(key, values);
    }

    @Override
    @Transactional
    public void savePreference(String key, List<String> values) {
        PortalPreference preference = getPreference(key);
        if (preference == null) {
            preference = new PortalPreferenceImpl(key, values);
        } else {
            preference.setValues(values);
        }
        this.savePreference(preference);
    }

    @Override
    @Transactional
    public void savePreference(PortalPreference preference) {
        repository.save(preference);
        if (preference.getKey().equals(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE))  {
            eventManager.fireEvent(new PortalPreferenceJavascriptDebugModeSaveEvent(this));
        }
    }
}
