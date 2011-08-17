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

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.impl.DefaultWidgetService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link DefaultWidgetService}
 */
public class WidgetServiceTest {

    private WidgetService service;
    private WidgetRepository repository;

    @Before
    public void setup() {
        repository = createNiceMock(WidgetRepository.class);
        service = new DefaultWidgetService(repository);
    }

    @Test
    public void getAvailableWidgets() {
        List<Widget> widgets = new ArrayList<Widget>();
        expect(repository.getAll()).andReturn(widgets);
        replay(repository);

        List<Widget> result = service.getAllWidgets().getResultSet();
        assertThat(result, is(sameInstance(widgets)));
    }

    @Test
    public void getWidget() {
        Widget w = new Widget();
        expect(repository.get(1L)).andReturn(w);
        replay(repository);

        Widget result = service.getWidget(1L);
        assertThat(result, is(sameInstance(w)));

    }

    @Test
    public void getWidgetsForSearchTerm() {
        String searchTerm = "gAdGet";
        int offset = 0;
        int pagesize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);
        
        expect(repository.getCountFreeTextSearch(searchTerm)).andReturn(totalResults);
        expect(repository.getByFreeTextSearch(searchTerm, offset, pagesize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getWidgetsByFreeTextSearch(searchTerm, offset, pagesize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pagesize, result.getPageSize());

    }

    @Test
    public void getWidget_null() {
        expect(repository.get(1L)).andReturn(null);
        replay(repository);

        Widget result = service.getWidget(1L);
        assertThat(result, is(nullValue()));

    }

}
