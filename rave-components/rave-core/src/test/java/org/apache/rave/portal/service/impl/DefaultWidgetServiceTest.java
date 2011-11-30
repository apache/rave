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

import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.WidgetService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link DefaultWidgetService}
 */
public class DefaultWidgetServiceTest {

    private WidgetService widgetService;
    private WidgetRepository widgetRepository;
    private UserRepository userRepository;

    @Before
    public void setup() {
        widgetRepository = createMock(WidgetRepository.class);
        userRepository = createMock(UserRepository.class);
        widgetService = new DefaultWidgetService(widgetRepository, userRepository);
    }

    @Test
    public void getAvailableWidgets() {
        List<Widget> widgets = new ArrayList<Widget>();
        expect(widgetRepository.getCountAll()).andReturn(1);
        expect(widgetRepository.getAll()).andReturn(widgets);
        replay(widgetRepository);

        List<Widget> result = widgetService.getAllWidgets().getResultSet();
        assertThat(result, is(sameInstance(widgets)));
        
        verify(widgetRepository);
    }

    @Test
    public void getLimitedListOfWidgets() {
        Widget widget1 = new Widget(1L, "http://example.com/widget1.xml");
        Widget widget2 = new Widget(2L, "http://example.com/widget2.xml");
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget1);
        widgets.add(widget2);
        final int pageSize = 10;
        expect(widgetRepository.getCountAll()).andReturn(1);
        expect(widgetRepository.getLimitedList(0, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getLimitedListOfWidgets(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(widgets, result.getResultSet());
        verify(widgetRepository);
    }

    @Test
    public void getPublishedWidgets() {
        Widget widget1 = new Widget(1L, "http://example.com/widget1.xml");
        widget1.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget widget2 = new Widget(2L, "http://example.com/widget2.xml");
        widget2.setWidgetStatus(WidgetStatus.PUBLISHED);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget1);
        widgets.add(widget2);
        final int pageSize = 10;
        expect(widgetRepository.getCountByStatus(WidgetStatus.PUBLISHED)).andReturn(1);
        expect(widgetRepository.getByStatus(WidgetStatus.PUBLISHED, 0, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getPublishedWidgets(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(widgets, result.getResultSet());
        verify(widgetRepository);
    }

    @Test
    public void getWidget() {
        Widget w = new Widget();
        expect(widgetRepository.get(1L)).andReturn(w);
        replay(widgetRepository);

        Widget result = widgetService.getWidget(1L);
        assertThat(result, is(sameInstance(w)));
        verify(widgetRepository);
    }

    @Test
    public void getWidgetsForSearchTerm() {
        final String searchTerm = "gAdGet";
        int offset = 0;
        int pageSize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setEntityId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);

        expect(widgetRepository.getCountFreeTextSearch(searchTerm)).andReturn(totalResults);
        expect(widgetRepository.getByFreeTextSearch(searchTerm, offset, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getWidgetsByFreeTextSearch(searchTerm, offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
        verify(widgetRepository);
    }


    @Test
    public void getPublishedWidgetsForSearchTerm() {
        final String searchTerm = "gAdGet";
        int offset = 0;
        int pageSize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        widget.setEntityId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);

        expect(widgetRepository.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, null, searchTerm))
                .andReturn(totalResults);
        expect(widgetRepository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, null, searchTerm,
                offset, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm,
                offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
        verify(widgetRepository);
    }

    @Test
    public void getPublishedOpenSocialWidgetsForSearchTerm() {
        final String searchTerm = "gAdGet";
        int offset = 0;
        int pageSize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        final String type = "OpenSocial";
        widget.setType(type);
        widget.setEntityId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);

        expect(widgetRepository.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, type, searchTerm))
                .andReturn(totalResults);
        expect(widgetRepository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, type, searchTerm,
                offset, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getWidgetsBySearchCriteria(searchTerm, type,
                WidgetStatus.PUBLISHED.toString(), offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
        verify(widgetRepository);
    }

    @Test
    public void getWidgetsByOwner() {
        final int offset = 0;
        final int pageSize = 10;
        final User user = new User(5L);
        expect(userRepository.get(user.getEntityId())).andReturn(user);
        replay(userRepository);

        final List<Widget> widgets = new ArrayList();
        final Widget widget = new Widget(3L, "http://www.widgetsRus.com/");
        widgets.add(widget);

        expect(widgetRepository.getCountByOwner(user, offset, pageSize)).andReturn(widgets.size());
        expect(widgetRepository.getByOwner(user, offset, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getWidgetsByOwner(user.getEntityId(), offset, pageSize);
        assertNotNull(result);
        assertEquals(offset, result.getOffset());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(widgets, result.getResultSet());

        verify(userRepository);
        verify(widgetRepository);
    }

    @Test
    public void getWidget_null() {
        expect(widgetRepository.get(1L)).andReturn(null);
        replay(widgetRepository);

        Widget result = widgetService.getWidget(1L);
        assertThat(result, is(nullValue()));
        verify(widgetRepository);
    }

    @Test
    public void getWidgetByUrl() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(widget);
        replay(widgetRepository);

        Widget result = widgetService.getWidgetByUrl(widgetUrl);
        assertNotNull(result);
        assertEquals(result.getUrl(), widgetUrl);
        verify(widgetRepository);
    }

    @Test
    public void isRegisteredWidget() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(widget);
        replay(widgetRepository);

        boolean isExisting = widgetService.isRegisteredUrl(widgetUrl);
        verify(widgetRepository);
        assertTrue("Expecting existing widget for url " + widgetUrl, isExisting);
    }

    @Test
    public void isNotRegisteredWidget_() {
        final String widgetUrl =
                "http://example.com/doesnotexistinrepository.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(null);
        replay(widgetRepository);

        boolean isExisting = widgetService.isRegisteredUrl(widgetUrl);
        verify(widgetRepository);
        assertFalse("Not expecting widget for url " + widgetUrl, isExisting);
    }

    @Test
    public void registerNewWidget() {
        final String widgetUrl = "http://example.com/newwidget.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(null);
        expect(widgetRepository.save(widget)).andReturn(widget);
        replay(widgetRepository);

        Widget savedWidget = widgetService.registerNewWidget(widget);
        assertNotNull(savedWidget);
        assertEquals(widget.getEntityId(), savedWidget.getEntityId());
        
        verify(widgetRepository);
    }

    @Test(expected = DuplicateItemException.class)
    public void registerExistingWidgetAsNew() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(widget);
        replay(widgetRepository);

        widgetService.registerNewWidget(widget);
        verify(widgetRepository);
        assertFalse("Expecting an exception", true);
    }

    @Test
    public void updateWidget() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.save(widget)).andReturn(widget).once();
        replay(widgetRepository);

        widgetService.updateWidget(widget);
        verify(widgetRepository);

        assertTrue("Save called", true);
    }

    @Test
    public void widgetStatistics() {
        expect(widgetRepository.getWidgetStatistics(1L, 1L)).andReturn(new WidgetStatistics());
        replay(widgetRepository);

        widgetService.getWidgetStatistics(1L, 1L);
        verify(widgetRepository);
    }


    @Test
    public void allWidgetStatistics() {
        expect(widgetRepository.getAllWidgetStatistics(1L)).andReturn(new HashMap<Long, WidgetStatistics>());
        replay(widgetRepository);
    
        widgetService.getAllWidgetStatistics(1L);
        verify(widgetRepository);
    }
}
