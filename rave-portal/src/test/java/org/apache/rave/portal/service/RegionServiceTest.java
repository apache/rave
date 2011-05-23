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


import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.service.impl.DefaultRegionService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegionServiceTest {

    private RegionService regionService;
    private RegionRepository regionRepository;
    private RegionWidgetRepository regionWidgetRepository;
    private static final long REGION_WIDGET_ID = 5L;
    private static final long TO_REGION_ID = 1L;
    private static final long FROM_REGION_ID = 2L;
    private Region targetRegion;
    private Region originalRegion;

    @Before
    public void setup() {
        regionRepository = createNiceMock(RegionRepository.class);
        regionService = new DefaultRegionService(regionRepository);

        targetRegion = new Region();
        targetRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        targetRegion.getRegionWidgets().add(new RegionWidget(1L, 0));
        targetRegion.getRegionWidgets().add(new RegionWidget(2L, 1));
        targetRegion.getRegionWidgets().add(new RegionWidget(3L, 2));

        originalRegion = new Region();
        originalRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        originalRegion.getRegionWidgets().add(new RegionWidget(4L, 0));
        originalRegion.getRegionWidgets().add(new RegionWidget(5L, 1));
        originalRegion.getRegionWidgets().add(new RegionWidget(6L, 2));
    }

    @Test
    public void moveRegionWidget_validMiddle() {
        final int newPosition = 0;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }
    @Test
    public void moveRegionWidget_validStart() {
        final int newPosition = 1;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }
    @Test
    public void moveRegionWidget_validEnd() {
        final int newPosition = 2;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validMiddle_sameRegion() {
        final int newPosition = 0;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }
    @Test
    public void moveRegionWidget_validStart_sameRegion() {
        final int newPosition = 1;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }
    @Test
    public void moveRegionWidget_validEnd_sameRegion() {
        final int newPosition = 2;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = regionService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget() {
        createMoveBetweenRegionsExpectations();
        regionService.moveRegionWidget(-1L, 0, 1L, 2L);
    }
    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget_sameRegion() {
        createMoveBetweenRegionsExpectations();
        regionService.moveRegionWidget(-1L, 0, 1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget() {
        createMoveBetweenRegionsExpectations();
        regionService.moveRegionWidget(-1L, 0, 5L, 6L);
    }
    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget_sameRegion() {
        createMoveBetweenRegionsExpectations();
        regionService.moveRegionWidget(-1L, 0, 5L, 5L);
    }

    private void verifyPositions(int newPosition, RegionWidget widget, boolean sameRegion) {
        assertThat(widget.getRenderOrder(), is(equalTo(newPosition)));
        if(!sameRegion) {
            assertOrder(targetRegion);
            assertThat(targetRegion.getRegionWidgets().contains(widget), is(true));
        }
        assertOrder(originalRegion);
        assertThat(originalRegion.getRegionWidgets().contains(widget), is(sameRegion));
    }

    private void createMoveBetweenRegionsExpectations() {
        expect(regionRepository.getById(TO_REGION_ID)).andReturn(targetRegion);
        expect(regionRepository.getById(FROM_REGION_ID)).andReturn(originalRegion);
        expect(regionRepository.save(targetRegion)).andReturn(targetRegion);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(regionRepository);
    }
    private void createMoveWithinRegionsExpectations() {
        expect(regionRepository.getById(FROM_REGION_ID)).andReturn(originalRegion);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(regionRepository);
    }

    private void assertOrder(Region region) {
        for(int i=0; i<region.getRegionWidgets().size(); i++) {
            assertThat(region.getRegionWidgets().get(i).getRenderOrder(), is(equalTo(i)));
        }
    }

}
