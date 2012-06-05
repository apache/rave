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

import org.apache.rave.portal.model.JpaPortalPreference;
import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public DefaultPortalPreferenceService(PortalPreferenceRepository repository) {
        this.repository = repository;
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
    public void savePreference(String key, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        this.savePreference(key, values);
    }

    @Override
    public void savePreference(String key, List<String> values) {
        PortalPreference preference = getPreference(key);
        if (preference == null) {
            preference = new JpaPortalPreference(key, values);
        } else {
            preference.setValues(values);
        }
        this.savePreference(preference);
    }

    @Override
    public void savePreference(PortalPreference preference) {
        repository.save(preference);
    }
}
