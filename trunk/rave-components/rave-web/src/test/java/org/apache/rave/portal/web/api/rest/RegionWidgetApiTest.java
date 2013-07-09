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

package org.apache.rave.portal.web.api.rest;

import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.portal.service.RegionWidgetService;
import org.apache.rave.portal.web.model.RegionWidgetPreferenceListWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class RegionWidgetApiTest {
    private RegionWidgetService regionWidgetService;
    private RegionWidgetApi regionWidgetApi;

    private String VALID_REGION_WIDGET_ID = "1";
    private String INVALID_REGION_WIDGET_ID = "100";

    private String VALID_PREFERENCE_NAME = "color";
    private String VALID_PREFERENCE_VALUE = "blue";

    @Before
    public void setup() {
        regionWidgetService = createNiceMock(RegionWidgetService.class);
        regionWidgetApi = new RegionWidgetApi(regionWidgetService);
    }

    @Test
    public void replaceAllRegionWidgetPreferences_validParams() {
        RegionWidgetPreferenceListWrapper LIST_WRAPPER = new RegionWidgetPreferenceListWrapper(Arrays.asList( (RegionWidgetPreference)
                new RegionWidgetPreferenceImpl(), new RegionWidgetPreferenceImpl()
        ));

        expect(regionWidgetService.saveRegionWidgetPreferences(VALID_REGION_WIDGET_ID, LIST_WRAPPER.getPreferences())).
                andReturn(LIST_WRAPPER.getPreferences());
        replay(regionWidgetService);

        RegionWidgetPreferenceListWrapper result = regionWidgetApi.replaceAllRegionWidgetPreferences(VALID_REGION_WIDGET_ID,
                LIST_WRAPPER);

        verify(regionWidgetService);
        assertThat(result.getPreferences(), sameInstance(LIST_WRAPPER.getPreferences()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceAllRegionWidgetPreferences_invalidParams() {
        RegionWidgetPreferenceListWrapper LIST_WRAPPER = new RegionWidgetPreferenceListWrapper(Arrays.asList( (RegionWidgetPreference)
                new RegionWidgetPreferenceImpl(), new RegionWidgetPreferenceImpl()
        ));

        expect(regionWidgetService.saveRegionWidgetPreferences(INVALID_REGION_WIDGET_ID, LIST_WRAPPER.getPreferences())).
                andThrow(new IllegalArgumentException());
        replay(regionWidgetService);

        regionWidgetApi.replaceAllRegionWidgetPreferences(INVALID_REGION_WIDGET_ID, LIST_WRAPPER);
    }

    @Test
    public void createOrReplaceRegionWidgetPreference_validParams() {
        RegionWidgetPreference PREFERENCE = new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, VALID_PREFERENCE_NAME,
                VALID_PREFERENCE_VALUE);

        expect(regionWidgetService.saveRegionWidgetPreference(VALID_REGION_WIDGET_ID, PREFERENCE)).andReturn(PREFERENCE);
        replay(regionWidgetService);

        RegionWidgetPreference result = regionWidgetApi.createOrReplaceRegionWidgetPreference(VALID_REGION_WIDGET_ID,
                VALID_PREFERENCE_NAME, PREFERENCE);

        verify(regionWidgetService);
        assertThat(result, sameInstance(PREFERENCE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrReplaceRegionWidgetPreference_invalidParams_preferenceName() {
        RegionWidgetPreference PREFERENCE = new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, VALID_PREFERENCE_NAME,
                VALID_PREFERENCE_VALUE);

        regionWidgetApi.createOrReplaceRegionWidgetPreference(VALID_REGION_WIDGET_ID, "different", PREFERENCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrReplaceRegionWidgetPreference_invalidParams_regionWidgetId() {
        RegionWidgetPreference PREFERENCE = new RegionWidgetPreferenceImpl(VALID_REGION_WIDGET_ID, VALID_PREFERENCE_NAME,
                VALID_PREFERENCE_VALUE);

        expect(regionWidgetService.saveRegionWidgetPreference(INVALID_REGION_WIDGET_ID, PREFERENCE)).andThrow(
                new IllegalArgumentException());
        replay(regionWidgetService);

        regionWidgetApi.createOrReplaceRegionWidgetPreference(INVALID_REGION_WIDGET_ID, VALID_PREFERENCE_NAME, PREFERENCE);
    }
        
    @Test
    public void updateRegionWidgetCollapsedStatus() {
        final boolean COLLAPSED = true;       
        
        RegionWidget expectedRegionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        expectedRegionWidget.setCollapsed(COLLAPSED);

        expect(regionWidgetService.saveRegionWidgetCollapsedState(VALID_REGION_WIDGET_ID, COLLAPSED)).andReturn(expectedRegionWidget); 
        replay(regionWidgetService);
        RegionWidget result = regionWidgetApi.updateRegionWidgetCollapsedStatus(VALID_REGION_WIDGET_ID, COLLAPSED);
        verify(regionWidgetService);
        
        assertThat(result, sameInstance(expectedRegionWidget));
    }
}