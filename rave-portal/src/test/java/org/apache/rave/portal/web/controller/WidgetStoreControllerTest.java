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

package org.apache.rave.portal.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link WidgetStoreController}
 */
public class WidgetStoreControllerTest {

    private static final long WIDGET_ID = 1L;
    private static final long REFERRER_ID = 35L;
    private WidgetStoreController controller;
    private WidgetService widgetService;

    @Before
    public void setup() {
        widgetService = createNiceMock(WidgetService.class);
        controller = new WidgetStoreController(widgetService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void view() {
        Model model = new ExtendedModelMap();
        List<Widget> widgets = new ArrayList<Widget>();
        SearchResult<Widget> emptyResult = new SearchResult<Widget>(widgets, 0);

        expect(widgetService.getAllWidgets()).andReturn(emptyResult);
        replay(widgetService);

        String view = controller.view(model, REFERRER_ID);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.STORE)));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS), is(true));
        assertThat((Long) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
        assertThat(widgets, is(sameInstance(emptyResult.getResultSet())));
    }

    @Test
    public void viewWidget() {
        Model model = new ExtendedModelMap();
        Widget w = new Widget();

        expect(widgetService.getWidget(WIDGET_ID)).andReturn(w);
        replay(widgetService);

        String view = controller.viewWidget(model, WIDGET_ID, REFERRER_ID);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.WIDGET)));
        assertThat(model.containsAttribute(ModelKeys.WIDGET), is(true));
        assertThat(((Widget) model.asMap().get(ModelKeys.WIDGET)), is(sameInstance(w)));
    }

    @Test
    public void searchWidgets() {
        Model model = new ExtendedModelMap();

        String searchTerm = "gAdGet";
        
        int offset = 0;
        int pagesize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);
        SearchResult<Widget> result = new SearchResult<Widget>(widgets, totalResults);
        result.setPageSize(pagesize);

        expect(widgetService.getWidgetsByFreeTextSearch(searchTerm, offset, pagesize)).andReturn(result);
        replay(widgetService);

        String view = controller.viewSearchResult(model,REFERRER_ID, searchTerm, offset);
        verify(widgetService);

        assertEquals(ViewNames.STORE, view);
        final Map<String,Object> modelMap = model.asMap();
        assertEquals(searchTerm, modelMap.get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(ModelKeys.WIDGETS));
        assertEquals(offset, modelMap.get(ModelKeys.OFFSET));
        assertEquals(result, modelMap.get(ModelKeys.WIDGETS));
    }
}
