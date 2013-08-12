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
import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.service.RegionWidgetService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DefaultRegionWidgetServiceTest {
    private RegionWidgetRepository regionWidgetRepository;
    private RegionWidgetService regionWidgetService;

    final String VALID_REGION_WIDGET_ID = "1";
    final String INVALID_REGION_WIDGET_ID = "100";

    @Before
    public void setup() {
        regionWidgetRepository = createNiceMock(RegionWidgetRepository.class);
        regionWidgetService = new DefaultRegionWidgetService(regionWidgetRepository);
    }

    @Test
    public void getRegionWidget_validId() {
        final RegionWidget VALID_REGION_WIDGET = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);

        expect(regionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(VALID_REGION_WIDGET);
        replay(regionWidgetRepository);

        assertThat(regionWidgetService.getRegionWidget(VALID_REGION_WIDGET_ID), sameInstance(VALID_REGION_WIDGET));
    }

    @Test
    public void getRegionWidget_invalidId() {
        expect(regionWidgetRepository.get(INVALID_REGION_WIDGET_ID)).andReturn(null);
        replay(regionWidgetRepository);

        assertThat(regionWidgetService.getRegionWidget(INVALID_REGION_WIDGET_ID), is(nullValue()));
    }

    @Test
    public void getAll() {
        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();
        expect(regionWidgetRepository.getCountAll()).andReturn(1);
        expect(regionWidgetRepository.getAll()).andReturn(regionWidgets);
        replay(regionWidgetRepository);

        List<RegionWidget> result = regionWidgetService.getAll().getResultSet();
        assertThat(result, is(sameInstance(regionWidgets)));

        verify(regionWidgetRepository);
    }

    @Test
    public void getLimitedList() {
        Page page = new PageImpl("1", "3");
        Region region = new RegionImpl("4", page, 0);
        RegionWidget rw1 = new RegionWidgetImpl("1", "4", region);
        RegionWidget rw2 = new RegionWidgetImpl("2", "7", region);
        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();
        regionWidgets.add(rw1);
        regionWidgets.add(rw2);
        final int pageSize = 10;
        expect(regionWidgetRepository.getCountAll()).andReturn(2);
        expect(regionWidgetRepository.getLimitedList(0, pageSize)).andReturn(regionWidgets);
        replay(regionWidgetRepository);

        SearchResult<RegionWidget> result = regionWidgetService.getLimitedList(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(regionWidgets, result.getResultSet());
        verify(regionWidgetRepository);
    }

    @Test
    public void saveRegionWidget() {
        final RegionWidget VALID_REGION_WIDGET = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);

        expect(regionWidgetRepository.save(VALID_REGION_WIDGET)).andReturn(VALID_REGION_WIDGET);
        replay(regionWidgetRepository);

        assertThat(regionWidgetService.saveRegionWidget(VALID_REGION_WIDGET), sameInstance(VALID_REGION_WIDGET));
    }

    @Test
    public void saveRegionWidgetPreferences() {
        final RegionWidget VALID_REGION_WIDGET = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        VALID_REGION_WIDGET.setPreferences(getTestExistingRegionWidgetPreferences());

        expect(regionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(VALID_REGION_WIDGET);
        expect(regionWidgetRepository.save(VALID_REGION_WIDGET)).andReturn(VALID_REGION_WIDGET);
        replay(regionWidgetRepository);

        //Make sure that removing and changing existing preferences is handled properly, and also ensure adding a preference works.
        List<RegionWidgetPreference> updatedPreferences = getTestUpdatedRegionWidgetPreferences();
        List<RegionWidgetPreference> savedPreferences = regionWidgetService.saveRegionWidgetPreferences(VALID_REGION_WIDGET_ID, updatedPreferences);
        assertTrue(preferenceCollectionsMatch(updatedPreferences, savedPreferences));
        assertTrue(preferencesHaveValidRegionWidgetId(savedPreferences));
    }

    @Test
    public void saveRegionWidgetPreference() {
        final RegionWidget VALID_REGION_WIDGET = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        VALID_REGION_WIDGET.setPreferences(getTestExistingRegionWidgetPreferences());

        expect(regionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(VALID_REGION_WIDGET).anyTimes();
        expect(regionWidgetRepository.save(VALID_REGION_WIDGET)).andReturn(VALID_REGION_WIDGET).anyTimes();
        replay(regionWidgetRepository);

        //Add and update a preference.
        RegionWidgetPreference newPreference = new RegionWidgetPreferenceImpl(null, "age", "30");
        RegionWidgetPreference savedNewPreference = regionWidgetService.saveRegionWidgetPreference(VALID_REGION_WIDGET_ID, newPreference);
        RegionWidgetPreference updatedPreference = new RegionWidgetPreferenceImpl(null, "color", "purple");
        RegionWidgetPreference savedUpdatedPreference = regionWidgetService.saveRegionWidgetPreference(VALID_REGION_WIDGET_ID, updatedPreference);

        //Make sure the new and updated preference got mixed in properly with the existing preferences.
        List<RegionWidgetPreference> existingPreferences = getTestExistingRegionWidgetPreferences();
        existingPreferences.add(savedNewPreference);
        existingPreferences.get(0).setValue("purple");
        assertTrue(preferenceCollectionsMatch(existingPreferences, VALID_REGION_WIDGET.getPreferences()));
        assertTrue(preferencesHaveValidRegionWidgetId(VALID_REGION_WIDGET.getPreferences()));
    }
        
    @Test
    public void saveRegionWidgetCollapsedState() {
        final boolean COLLAPSED = true;
        RegionWidget regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        
        expect(regionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);                
        regionWidget.setCollapsed(COLLAPSED);
        expect(regionWidgetRepository.save(regionWidget)).andReturn(regionWidget);
        replay(regionWidgetRepository);

        assertThat(regionWidgetService.saveRegionWidgetCollapsedState(VALID_REGION_WIDGET_ID, COLLAPSED).isCollapsed(), is(COLLAPSED));
    }

    @Test(expected=IllegalArgumentException.class)
    public void saveRegionWidgetCollapsedState_invalidWidgetId() {
        final boolean COLLAPSED = true;
      
        expect(regionWidgetRepository.get(INVALID_REGION_WIDGET_ID)).andReturn(null);  
        replay(regionWidgetRepository);

        regionWidgetService.saveRegionWidgetCollapsedState(INVALID_REGION_WIDGET_ID, COLLAPSED);
    }    
    
    private boolean preferencesHaveValidRegionWidgetId(List<RegionWidgetPreference> savedPreferences) {
        for (RegionWidgetPreference savedPreference : savedPreferences) {
            if (!savedPreference.getRegionWidgetId().equals(VALID_REGION_WIDGET_ID)) {
                return false;
            }
        }

        return true;
    }

    private boolean preferenceCollectionsMatch(List<RegionWidgetPreference> updatedPreferences, List<RegionWidgetPreference> savedPreferences) {
        Map<String, RegionWidgetPreference> updatedPreferencesMap = new HashMap<String, RegionWidgetPreference>();

        //Make sure they're the same size
        if (updatedPreferences.size() != savedPreferences.size()) {
            return false;
        }

        //Map out the updated preferences so we can compare them to the saved preferences
        for (RegionWidgetPreference updatedPreference : updatedPreferences) {
            updatedPreferencesMap.put(updatedPreference.getName(), updatedPreference);
        }

        //We know the lists are the same length - so as long as all the savedPreferences exist and match the updated
        //preferences then we are all good.
        for (RegionWidgetPreference savedPreference : savedPreferences) {
            RegionWidgetPreference updatedPreference = updatedPreferencesMap.get(savedPreference.getName());
            if (updatedPreference == null || !StringUtils.equals(savedPreference.getName(), updatedPreference.getName())
                    || !StringUtils.equals(savedPreference.getValue(), updatedPreference.getValue())) {
                return false;
            }
        }

        return true;
    }

    private List<RegionWidgetPreference> getTestExistingRegionWidgetPreferences() {
        ArrayList<RegionWidgetPreference> regionWidgetPreferences = new ArrayList<RegionWidgetPreference>();
        regionWidgetPreferences.add(new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, "color", "blue"));
        regionWidgetPreferences.add(new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, "speed", "fast"));
        return regionWidgetPreferences;
    }

    private List<RegionWidgetPreference> getTestUpdatedRegionWidgetPreferences() {
        List<RegionWidgetPreference> regionWidgetPreferences = getTestExistingRegionWidgetPreferences();
        regionWidgetPreferences.remove(0);
        regionWidgetPreferences.get(0).setValue("slow");
        regionWidgetPreferences.add(new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, "size", "small"));
        return regionWidgetPreferences;
    }
}
