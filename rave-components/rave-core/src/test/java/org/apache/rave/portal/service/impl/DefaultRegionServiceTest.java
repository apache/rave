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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.service.RegionService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link DefaultRegionService}
 */
public class DefaultRegionServiceTest {

    private RegionService regionService;
    private RegionRepository regionRepository;

    @Before
    public void setup() {
        regionRepository = createMock(RegionRepository.class);
        regionService = new DefaultRegionService(regionRepository);
    }

    @Test
    public void getAll() {
        List<Region> regions = new ArrayList<Region>();
        expect(regionRepository.getCountAll()).andReturn(1);
        expect(regionRepository.getAll()).andReturn(regions);
        replay(regionRepository);

        List<Region> result = regionService.getAll().getResultSet();
        assertThat(result, is(sameInstance(regions)));

        verify(regionRepository);
    }

    @Test
    public void getLimitedList() {
        Page page = new PageImpl("1", "3");
        Region region1 = new RegionImpl("4", page, 0);
        Region region2 = new RegionImpl("5", page, 1);
        List<Region> regions = new ArrayList<Region>();
        regions.add(region1);
        regions.add(region2);
        final int pageSize = 10;
        expect(regionRepository.getCountAll()).andReturn(2);
        expect(regionRepository.getLimitedList(0, pageSize)).andReturn(regions);
        replay(regionRepository);

        SearchResult<Region> result = regionService.getLimitedList(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(regions, result.getResultSet());
        verify(regionRepository);
    }
}
