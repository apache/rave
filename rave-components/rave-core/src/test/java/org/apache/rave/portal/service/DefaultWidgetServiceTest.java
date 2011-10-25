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

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.impl.DefaultWidgetService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link DefaultWidgetService}
 */
public class DefaultWidgetServiceTest {

    private WidgetService service;
    private WidgetRepository repository;

    @Before
    public void setup() {
        repository = createNiceMock(WidgetRepository.class);
        WidgetRatingService widgetRatingService = createMock(WidgetRatingService.class);
        service = new DefaultWidgetService(repository, widgetRatingService);
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
    public void getLimitedListOfWidgets() {
        Widget widget1 = new Widget(1L,"http://example.com/widget1.xml");
        Widget widget2 = new Widget(2L,"http://example.com/widget2.xml");
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget1);
        widgets.add(widget2);
        final int pageSize = 10;
        expect(repository.getLimitedList(0, pageSize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getLimitedListOfWidgets(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(widgets, result.getResultSet());

    }

    @Test
    public void getPublishedWidgets() {
        Widget widget1 = new Widget(1L,"http://example.com/widget1.xml");
        widget1.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget widget2 = new Widget(2L,"http://example.com/widget2.xml");
        widget2.setWidgetStatus(WidgetStatus.PUBLISHED);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget1);
        widgets.add(widget2);
        final int pageSize = 10;
        expect(repository.getByStatus(WidgetStatus.PUBLISHED, 0, pageSize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getPublishedWidgets(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(widgets, result.getResultSet());
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
        final String searchTerm = "gAdGet";
        int offset = 0;
        int pageSize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setEntityId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);
        
        expect(repository.getCountFreeTextSearch(searchTerm)).andReturn(totalResults);
        expect(repository.getByFreeTextSearch(searchTerm, offset, pageSize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getWidgetsByFreeTextSearch(searchTerm, offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
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

        expect(repository.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, null, searchTerm))
                .andReturn(totalResults);
        expect(repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, null, searchTerm,
                offset, pageSize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getPublishedWidgetsByFreeTextSearch(searchTerm,
                offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
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

        expect(repository.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, type, searchTerm))
                .andReturn(totalResults);
        expect(repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, type, searchTerm,
                offset, pageSize)).andReturn(widgets);
        replay(repository);

        SearchResult<Widget> result = service.getWidgetsBySearchCriteria(searchTerm, type,
                WidgetStatus.PUBLISHED.toString(), offset, pageSize);
        assertEquals(widget, result.getResultSet().get(0));
        assertEquals(totalResults, result.getTotalResults());
        assertEquals(pageSize, result.getPageSize());
    }


    @Test
    public void getWidget_null() {
        expect(repository.get(1L)).andReturn(null);
        replay(repository);

        Widget result = service.getWidget(1L);
        assertThat(result, is(nullValue()));

    }

    @Test
    public void getWidgetByUrl() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(repository.getByUrl(widgetUrl)).andReturn(widget);
        replay(repository);

        Widget result = service.getWidgetByUrl(widgetUrl);
        assertNotNull(result);
        assertEquals(result.getUrl(), widgetUrl);
    }

    @Test
    public void registerNewWidget() {
        final String widgetUrl =
                "http://example.com/newwidget.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(repository.getByUrl(widgetUrl)).andReturn(null);
        expect(repository.save(widget)).andReturn(widget);
        replay(repository);

        Widget savedWidget = service.registerNewWidget(widget);
        assertNotNull(savedWidget);
        assertEquals(widget.getEntityId(), savedWidget.getEntityId());
    }

    @Test
    public void registerExistingWidgetAsNew() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(repository.getByUrl(widgetUrl)).andReturn(widget);
        replay(repository);

        Widget noWidget = service.registerNewWidget(widget);
        assertNull("Widget already exists", noWidget);
    }

    @Test
    public void updateWidget() {
        final String widgetUrl =
                        "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new Widget();
        widget.setUrl(widgetUrl);
        expect(repository.save(widget)).andReturn(widget).once();
        replay(repository);

        service.updateWidget(widget);
        verify(repository);

        assertTrue("Save called", true);
    }

    @Test
    public void createWidgetRating() {
        Widget widget = new Widget(1L, "http://example.com/widget.xml");
        widget.setRatings(new ArrayList<WidgetRating>());
        expect(repository.get(1)).andReturn(widget);
        expect(repository.save(widget)).andReturn(widget);
        replay(repository);

        WidgetRating newWidgetRating = new WidgetRating(1L, 1L, 1L, 1);
        service.saveWidgetRating(1L, newWidgetRating);

        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(1, ratings.size());
        assertEquals(ratings.get(0), newWidgetRating);
    }

    @Test
    public void updateWidgetRating() {
        Widget widget = new Widget();
        List<WidgetRating> ratings = new ArrayList<WidgetRating>();
        ratings.add(new WidgetRating(1L, 1L, 1L, 5));
        widget.setRatings(ratings);
        expect(repository.get(1)).andReturn(widget);
        expect(repository.save(widget)).andReturn(widget);
        replay(repository);
        
        WidgetRating newWidgetRating = new WidgetRating();
        newWidgetRating.setWidgetId(1L);
        newWidgetRating.setUserId(1L);
        newWidgetRating.setScore(0);
        
        service.saveWidgetRating(1L, newWidgetRating);
        
        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertEquals(ratings.get(1).getScore(), new Integer(0));
    }
    
    @Test
    public void deleteWidgetRating() {
        Widget widget = new Widget();
        List<WidgetRating> ratings = new ArrayList<WidgetRating>();
        ratings.add(new WidgetRating(123L, 1L, 1L, 5));
        widget.setRatings(ratings);
        expect(repository.get(1)).andReturn(widget);
        expect(repository.save(widget)).andReturn(widget);
        replay(repository);
        
        service.removeWidgetRating(1, 1L);
        
        assertNotNull(ratings);
        assertEquals(1, ratings.size());
    }
    
    @Test
    public void deleteUnsavedWidgetRating() {
        Widget widget = new Widget();
        List<WidgetRating> ratings = new ArrayList<WidgetRating>();
        ratings.add(new WidgetRating(123L, 1L, 1L, 5));
        widget.setRatings(ratings);
        expect(repository.get(1)).andReturn(widget);
        expect(repository.save(widget)).andReturn(widget);
        replay(repository);
        
        service.removeWidgetRating(1, 2L);
        
        assertNotNull(ratings);
        assertEquals(1, ratings.size());
        assertEquals(ratings.get(0).getScore(), new Integer(5));
    }

    
}
