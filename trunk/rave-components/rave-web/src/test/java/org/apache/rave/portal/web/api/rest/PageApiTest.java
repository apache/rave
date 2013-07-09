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

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.apache.rave.portal.service.OmdlService;
import org.apache.rave.portal.service.PageService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author CARLUCCI
 */
public class PageApiTest {
    private PageApi pageApi;
    private PageService pageService;
    private OmdlService omdlService;
    private MockHttpServletResponse response;

    private final String PAGE_ID = "1";

    @Before
    public void setUp() {
        response = new MockHttpServletResponse();
        pageService = createMock(PageService.class);
        omdlService = createMock(OmdlService.class);
        pageApi = new PageApi(pageService, omdlService);
    }

    @Test
    public void getPage_validId() {
        Page p = new PageImpl();
        expect(pageService.getPage(PAGE_ID)).andReturn(p).once();
        replay(pageService);

        Page returned = pageApi.getPage(PAGE_ID, false);
        assertThat(returned, is(sameInstance(p)));
    }

    @Test
    public void getPage_validId_export() {
        Page p = new PageImpl();
        p.setRegions(new ArrayList<Region>());
        p.setOwnerId("");
        Region region = new RegionImpl();
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        RegionWidget w = new RegionWidgetImpl();
        w.setPreferences(new ArrayList<RegionWidgetPreference>());
        w.getPreferences().add(new RegionWidgetPreferenceImpl());
        region.getRegionWidgets().add(w);
        p.getRegions().add(region);

        expect(pageService.getPage(PAGE_ID)).andReturn(p).once();
        replay(pageService);

        Page returned = pageApi.getPage(PAGE_ID, true);
        assertThat(returned, is(sameInstance(p)));
        assertThat(returned.getOwnerId(), is(nullValue()));
        assertThat(returned.getRegions().get(0).getRegionWidgets().get(0).getPreferences(), is(nullValue()));
    }

    @Test
    public void testDeletePage() {
        pageService.deletePage(PAGE_ID);
        expectLastCall();
        replay(pageService);

        pageApi.deletePage(PAGE_ID, response);

        assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
        verify(pageService);
    }
}
