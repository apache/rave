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
import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.WidgetService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link DefaultWidgetService}
 */
public class DefaultWidgetServiceTest {

    private WidgetService widgetService;
    private WidgetRepository widgetRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Before
    public void setup() {
        widgetRepository = createMock(WidgetRepository.class);
        userRepository = createMock(UserRepository.class);
        categoryRepository = createMock(CategoryRepository.class);
        widgetService = new DefaultWidgetService(widgetRepository, userRepository, categoryRepository);
    }

    @Test
    public void getAvailableWidgets() {
        List<Widget> widgets = new ArrayList<Widget>();
        expect(widgetRepository.getCountAll()).andReturn(1);
        expect(widgetRepository.getAll()).andReturn(widgets);
        replay(widgetRepository);

        List<Widget> result = widgetService.getAll().getResultSet();
        assertThat(result, is(sameInstance(widgets)));

        verify(widgetRepository);
    }

    @Test
    public void getLimitedList() {
        Widget widget1 = new WidgetImpl("2", "http://example.com/widget1.xml");
        Widget widget2 = new WidgetImpl("3", "http://example.com/widget2.xml");
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget1);
        widgets.add(widget2);
        final int pageSize = 10;
        expect(widgetRepository.getCountAll()).andReturn(2);
        expect(widgetRepository.getLimitedList(0, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getLimitedList(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(widgets, result.getResultSet());
        verify(widgetRepository);
    }

    @Test
    public void getPublishedWidgets() {
        Widget widget1 = new WidgetImpl("1", "http://example.com/widget1.xml");
        widget1.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget widget2 = new WidgetImpl("2", "http://example.com/widget2.xml");
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
        Widget w = new WidgetImpl();
        expect(widgetRepository.get("1")).andReturn(w);
        replay(widgetRepository);

        Widget result = widgetService.getWidget("1");
        assertThat(result, is(sameInstance(w)));
        verify(widgetRepository);
    }

    @Test
    public void getWidgetsForSearchTerm() {
        final String searchTerm = "gAdGet";
        int offset = 0;
        int pageSize = 10;
        int totalResults = 2;
        WidgetImpl widget = new WidgetImpl("1");
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
        WidgetImpl widget = new WidgetImpl("1");
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
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
        WidgetImpl widget = new WidgetImpl("1");
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        final String type = "OpenSocial";
        widget.setType(type);
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
        final UserImpl user = new UserImpl("5");
        expect(userRepository.get(user.getId())).andReturn(user);
        replay(userRepository);

        final List<Widget> widgets = new ArrayList<Widget>();
        final Widget widget = new WidgetImpl("3", "http://www.widgetsRus.com/");
        widgets.add(widget);

        expect(widgetRepository.getCountByOwner(user, offset, pageSize)).andReturn(widgets.size());
        expect(widgetRepository.getByOwner(user, offset, pageSize)).andReturn(widgets);
        replay(widgetRepository);

        SearchResult<Widget> result = widgetService.getWidgetsByOwner(user.getId(), offset, pageSize);
        assertNotNull(result);
        assertEquals(offset, result.getOffset());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(widgets, result.getResultSet());

        verify(userRepository);
        verify(widgetRepository);
    }

    @Test
    public void getWidget_null() {
        expect(widgetRepository.get("1")).andReturn(null);
        replay(widgetRepository);

        Widget result = widgetService.getWidget("1");
        assertThat(result, is(nullValue()));
        verify(widgetRepository);
    }

    @Test
    public void getWidgetByUrl() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        Widget widget = new WidgetImpl();
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
        Widget widget = new WidgetImpl();
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
        Widget widget = new WidgetImpl();
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
        WidgetImpl widget = new WidgetImpl();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.getByUrl(widgetUrl)).andReturn(null);
        expect(widgetRepository.save(widget)).andReturn(widget);
        replay(widgetRepository);

        Widget savedWidget = widgetService.registerNewWidget(widget);
        assertNotNull(savedWidget);
        assertEquals(widget.getId(), savedWidget.getId());

        verify(widgetRepository);
    }

    @Test(expected = DuplicateItemException.class)
    public void registerExistingWidgetAsNew() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        WidgetImpl widget = new WidgetImpl();
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
        Widget widget = new WidgetImpl();
        widget.setUrl(widgetUrl);
        expect(widgetRepository.save(widget)).andReturn(widget).once();
        replay(widgetRepository);

        widgetService.updateWidget(widget);
        verify(widgetRepository);

        assertTrue("Save called", true);
    }

    @Test
    public void widgetStatistics() {
        expect(widgetRepository.getWidgetStatistics("1", "1")).andReturn(new WidgetStatistics());
        replay(widgetRepository);

        widgetService.getWidgetStatistics("1", "1");
        verify(widgetRepository);
    }


    @Test
    public void allWidgetStatistics() {
        expect(widgetRepository.getAllWidgetStatistics("1")).andReturn(new HashMap<String, WidgetStatistics>());
        replay(widgetRepository);

        widgetService.getAllWidgetStatistics("1");
        verify(widgetRepository);
    }

    @Test
    public void getWidgetsByCategory_valid(){
        String id = "1";
        int offset = 0;
        int pageSize = 10;
        String categoryText = "Social";
        Widget w = new WidgetImpl();
        List<Category> categories = new ArrayList<Category>();
        Category c = new CategoryImpl();
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(w);
        c.setWidgets(widgets);
        c.setId(id);
        c.setText(categoryText);
        categories.add(c);
        w.setCategories(categories);
        expect(categoryRepository.get(id)).andReturn(c);
        replay(categoryRepository);
        SearchResult<Widget> result = widgetService.getWidgetsByCategory(id,offset,pageSize);
        verify(categoryRepository);
        assertEquals("number of widgets", 1, result.getTotalResults());
        assertSame(w, result.getResultSet().get(0));
        assertEquals(c.getId(), result.getResultSet().get(0).getCategories().get(0).getId());
    }

    @Test
    public void getWidgetTag() {
        WidgetTagImpl tag = new WidgetTagImpl("1", new Date(), "1");
        expect(widgetRepository.getTagById("1")).andReturn(tag);
        replay(widgetRepository);

        assertEquals(tag, widgetService.getWidgetTag("1"));
        verify(widgetRepository);
    }

    @Test
    public void saveWidgetTag() {
        try {

            WidgetTagImpl wtag = new WidgetTagImpl("1", new Date(), "1");
            expect(widgetRepository.saveWidgetTag("1", wtag)).andReturn(wtag);
            replay(widgetRepository);

            widgetService.createWidgetTag("1", wtag);
            verify(widgetRepository);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void getWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetRepository.getCommentById("1", "1")).andReturn(comment);
        replay(widgetRepository);

        assertEquals(comment, widgetService.getWidgetComment("1", "1"));
        verify(widgetRepository);
    }

    @Test
    public void createWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetRepository.createWidgetComment("1", comment)).andReturn(comment);
        replay(widgetRepository);

        widgetService.createWidgetComment("1", comment);
        verify(widgetRepository);
    }

    @Test
    public void deleteWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetRepository.getCommentById("1","1")).andReturn(comment);
        widgetRepository.deleteWidgetComment("1", comment);
        replay(widgetRepository);

        widgetService.removeWidgetComment("1", "1");
        verify(widgetRepository);
    }

    @Test
    public void deleteAllComments() {
        final String USER_ID = "33";
        final int EXPECTED_COUNT = 43;

        expect(widgetRepository.deleteAllWidgetComments(USER_ID)).andReturn(EXPECTED_COUNT);
        replay(widgetRepository);
        assertThat(widgetService.deleteAllWidgetComments(USER_ID), is(EXPECTED_COUNT));
        verify(widgetRepository);
    }

    @Test
    public void testGetByWidgetIdAndUserId() {
        WidgetRating widgetRating = new WidgetRatingImpl("1", "3", 5);
        expect(widgetRepository.getWidgetRatingsByWidgetIdAndUserId("2", "3")).andReturn(widgetRating);
        replay(widgetRepository);
        final WidgetRating rating = widgetService.getWidgetRatingByWidgetIdAndUserId("2", "3");
        assertEquals("Score is 5", Integer.valueOf(5), rating.getScore());
        verify(widgetRepository);
    }

    @Test
    public void updateScore() {
        WidgetRating widgetRating = createMock(WidgetRatingImpl.class);
        widgetRating.setScore(10);

        expectLastCall().once();
        expect(widgetRepository.updateWidgetRating("2", widgetRating)).andReturn(widgetRating);
        replay(widgetRepository, widgetRating);
        widgetService.updateWidgetRatingScore("2", widgetRating, 10);

        verify(widgetRepository, widgetRating);
    }

    @Test
    public void saveWidgetRating_new() {
        WidgetRating newRating = new WidgetRatingImpl();
        newRating.setUserId("1");
        newRating.setScore(10);

        expect(widgetRepository.getWidgetRatingsByWidgetIdAndUserId("2", "1")).andReturn(null);
        expect(widgetRepository.createWidgetRating("2", newRating)).andReturn(newRating);
        replay(widgetRepository);

        widgetService.saveWidgetRating("2", newRating);
        verify(widgetRepository);
    }

    @Test
    public void saveWidgetRating_existing() {
        WidgetRating existingRating = new WidgetRatingImpl("1", "1", 5);
        WidgetRating newRating = new WidgetRatingImpl();
        newRating.setUserId("1");
        newRating.setScore(10);

        expect(widgetRepository.getWidgetRatingsByWidgetIdAndUserId("1", "1")).andReturn(existingRating);
        expect(widgetRepository.updateWidgetRating("1", existingRating)).andReturn(existingRating);
        replay(widgetRepository);

        widgetService.saveWidgetRating("1", newRating);
        verify(widgetRepository);

        assertEquals("Updated score", Integer.valueOf(10), existingRating.getScore());
    }

    @Test
    public void removeWidgetRating_existingRating() {
        final WidgetRating widgetRating = new WidgetRatingImpl("1", "1", 5);

        expect(widgetRepository.getWidgetRatingsByWidgetIdAndUserId("1", "1")).andReturn(widgetRating);
        widgetRepository.deleteWidgetRating("1", widgetRating);
        expectLastCall();
        replay(widgetRepository);

        widgetService.removeWidgetRating("1", "1");
        verify(widgetRepository);
    }

    @Test
    public void removeWidgetRating_notExisting() {
        expect(widgetRepository.getWidgetRatingsByWidgetIdAndUserId("1", "2")).andReturn(null);
        expectLastCall();
        replay(widgetRepository);
        widgetService.removeWidgetRating("1", "2");
        verify(widgetRepository);
    }

    @Test
    public void deleteAll() {
        final String USER_ID = "33";
        final int EXPECTED_COUNT = 43;

        expect(widgetRepository.deleteAllWidgetRatings(USER_ID)).andReturn(EXPECTED_COUNT);
        replay(widgetRepository);
        assertThat(widgetService.removeAllWidgetRatings(USER_ID), is(EXPECTED_COUNT));
        verify(widgetRepository);
    }
}
