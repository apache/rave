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

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.util.ModelUtils;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.service.RegionWidgetService;
import org.apache.rave.synchronization.annotation.Synchronized;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service(value = "regionWidgetService")
public class DefaultRegionWidgetService implements RegionWidgetService {
    private RegionWidgetRepository regionWidgetRepository;

    @Autowired
    public DefaultRegionWidgetService(RegionWidgetRepository regionWidgetRepository) {
        this.regionWidgetRepository = regionWidgetRepository;
    }

    @Override
    public RegionWidget getRegionWidget(String regionWidgetId) {
        return regionWidgetRepository.get(regionWidgetId);
    }

    @Override
    public SearchResult<RegionWidget> getAll() {
        final int count = regionWidgetRepository.getCountAll();
        final List<RegionWidget> regionWidgets = regionWidgetRepository.getAll();
        return new SearchResult<RegionWidget>(regionWidgets, count);
    }

    @Override
    public SearchResult<RegionWidget> getLimitedList(int offset, int pageSize) {
        final int count = regionWidgetRepository.getCountAll();
        final List<RegionWidget> regionWidgets = regionWidgetRepository.getLimitedList(offset, pageSize);
        final SearchResult<RegionWidget> searchResult = new SearchResult<RegionWidget>(regionWidgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }


    @Override
    @Transactional
    public RegionWidget saveRegionWidget(RegionWidget regionWidget) {
        return regionWidgetRepository.save(regionWidget);
    }

    @Override
    @Synchronized(discriminator = "'RegionWidget'", id = "#regionWidgetId")
    @Transactional
    public List<RegionWidgetPreference> saveRegionWidgetPreferences(String regionWidgetId,
                                                                    List<RegionWidgetPreference> preferences) {
        RegionWidget regionWidget = this.getValidRegionWidget(regionWidgetId);
        ModelUtils.normalizeRegionWidgetPreferences(regionWidgetId, preferences);
        reconcileRegionWidgetPreferences(regionWidget.getPreferences(), preferences, true);
        return this.saveRegionWidget(regionWidget).getPreferences();
    }

    @Override
    @Synchronized(discriminator = "'RegionWidget'", id = "#regionWidgetId")
    @Transactional
    public RegionWidgetPreference saveRegionWidgetPreference(String regionWidgetId, RegionWidgetPreference preference) {
        RegionWidget regionWidget = this.getValidRegionWidget(regionWidgetId);
        ModelUtils.normalizeRegionWidgetPreference(regionWidgetId, preference);
        reconcileRegionWidgetPreferences(regionWidget.getPreferences(), Arrays.asList(preference), false);
        regionWidget = this.saveRegionWidget(regionWidget);

        for (RegionWidgetPreference regionWidgetPreference : regionWidget.getPreferences()) {
            if (regionWidgetPreference.getName().equals(preference.getName())) {
                preference = regionWidgetPreference;
                break;
            }
        }

        return preference;
    }

    @Override
    @Transactional
    public RegionWidget saveRegionWidgetCollapsedState(String regionWidgetId, boolean collapsed) {
        RegionWidget regionWidget = getValidRegionWidget(regionWidgetId);
        regionWidget.setCollapsed(collapsed);
        return saveRegionWidget(regionWidget);
    }

    private RegionWidget getValidRegionWidget(String regionWidgetId) {
        RegionWidget regionWidget = this.getRegionWidget(regionWidgetId);
        if (regionWidget == null) {
            throw new IllegalArgumentException("Invalid RegionWidget ID");
        }
        return regionWidget;
    }

    private static void reconcileRegionWidgetPreferences(List<RegionWidgetPreference> existingPreferences,
                                                         List<RegionWidgetPreference> updatedPreferences,
                                                         boolean removeMissingObjects) {
        CollectionUtils.reconcileObjectCollections(existingPreferences, updatedPreferences,
                new CollectionUtils.CollectionReconciliationHelper<RegionWidgetPreference, String>() {
                    @Override
                    public String extractKey(RegionWidgetPreference object) {
                        return object.getName().toLowerCase();
                    }

                    @Override
                    public void reconcileValues(RegionWidgetPreference existingObject, RegionWidgetPreference updatedObject) {
                        if (!StringUtils.equals(existingObject.getValue(), updatedObject.getValue())) {
                            existingObject.setValue(updatedObject.getValue());
                        }
                    }
                }, removeMissingObjects);
    }
}
