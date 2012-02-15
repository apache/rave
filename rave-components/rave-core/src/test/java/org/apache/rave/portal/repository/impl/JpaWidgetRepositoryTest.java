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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link org.apache.rave.portal.repository.impl.JpaWidgetRepository}
 */
@Transactional(readOnly = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetRepositoryTest {

    private static Logger logger = LoggerFactory.getLogger(JpaWidgetRepositoryTest.class);

    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private WidgetRepository repository;

    @Test
    public void getById_valid() {
        Widget widget = repository.get(1L);
        assertThat(widget, is(notNullValue()));
        assertThat(widget.getEntityId(), is(equalTo(1L)));
    }

    @Test
    public void getById_invValid() {
        Widget widget = repository.get(-1L);
        assertThat(widget, is(nullValue()));
    }

    @Test
    public void getByUrl_valid() {
        final String widgetUrl =
                "http://hosting.gmodules.com/ig/gadgets/file/112581010116074801021/hamster.xml";
        final Widget widget = repository.getByUrl(widgetUrl);
        assertNotNull(widget);
        assertEquals(widgetUrl, widget.getUrl());
    }

    @Test
    public void getByUrl_empty() {
        try {
            repository.getByUrl("");
            fail();
        } catch (IllegalArgumentException e) {
            logger.debug("Expected to fail on empty URL", e.getMessage());
        }
    }

    @Test
    public void getByFreeTextSearch() {
        List<Widget> widgets = repository.getByFreeTextSearch("gAdGet", 1, 1);
        assertEquals(1, widgets.size());
    }

    @Test
    public void countFreeTextSearch() {
        int count = repository.getCountFreeTextSearch("gAdGet");
        assertTrue(count >= 2);
    }

    @Test
    public void getAll() {
        List<Widget> widgets = repository.getAll();
        assertThat(widgets, is(notNullValue()));
        assertThat(widgets.size() > 4, is(true));
    }

    @Test
    public void getLimitedList() {
        final int pageSize = 3;
        List<Widget> widgets = repository.getLimitedList(0, pageSize);
        assertNotNull(widgets);
        assertTrue(widgets.size() <= pageSize);
    }

    @Test
    public void countAll() {
        int count = repository.getCountAll();
        assertTrue(count > 4);
    }

    @Test
    public void getByStatus() {
        final int pageSize = 999;
        List<Widget> published = repository.getByStatus(WidgetStatus.PUBLISHED, 0, pageSize);
        assertNotNull(published);
        assertTrue(published.size() > 0);

        List<Widget> preview = repository.getByStatus(WidgetStatus.PREVIEW, 0, pageSize);
        assertNotNull(preview);
        assertTrue(preview.size() > 0);

        List<Widget> shouldBeEmpty = new ArrayList<Widget>();
        for (Widget previewWidget : preview) {
            if (published.contains(previewWidget)) {
                // should not happen
                shouldBeEmpty.add(previewWidget);
            }
        }
        assertEquals(0, shouldBeEmpty.size());
    }

    @Test
    public void countByStatus() {
        int publishedCount = repository.getCountByStatus(WidgetStatus.PUBLISHED);
        assertTrue(publishedCount > 0);
    }

    @Test
    public void getByStatusAndTypeAndFreeText() {
        final String searchTerm = "gAdGet";
        final String type = "OpenSocial";
        List<Widget> widgets = repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, type,
                searchTerm, 0, 1);
        assertEquals(1, widgets.size());

        List<Widget> preview = repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PREVIEW, type,
                searchTerm, 0, 1);
        assertEquals(0, preview.size());
    }

    @Test
    public void countByStatusAndTypeAndFreeText() {
        final String searchTerm = "gAdGet";
        final String type = "OpenSocial";
        int publishedCount = repository.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, type, searchTerm);
        assertTrue(publishedCount >= 2);
    }

    @Test
    public void getByOwner() {
        final User user = new User(2L);
        List<Widget> widgets = repository.getByOwner(user, 0, 10);
        assertEquals(1, widgets.size());
    }

    @Test
    public void getCountByOwner() {
        final User user = new User(2L);
        assertEquals(1, repository.getCountByOwner(user, 0, 10));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback
    public void saveWidgetWithLongDescription() {
        final String url = "http://example.com/doesnotexistyet";
        final String longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc dictum sodales erat consequat pulvinar. Pellentesque ut massa mi, sit amet imperdiet diam. Morbi nec magna quis nisi bibendum dignissim. Fusce et rhoncus turpis. Integer mollis magna sit amet nulla convallis placerat dignissim lorem blandit. Nulla magna justo, cursus ac semper sed, pulvinar in turpis. Donec ultricies nibh sed nulla congue ullamcorper. Fusce commodo ultrices nunc, interdum lacinia elit faucibus at. Fusce laoreet ultricies volutpat. ";

        Widget doesnotexist = repository.getByUrl(url);
        assertNull(doesnotexist);

        Widget widget = new Widget();
        widget.setTitle("Widget with long description");
        widget.setUrl(url);
        widget.setDescription(longDescription);
        widget = repository.save(widget);
        assertNotNull(widget.getEntityId());
        assertEquals(longDescription, widget.getDescription());
    }

    @Test
    public void getAllWidgetStatistics() {
        Map<Long, WidgetStatistics> widgetStatistics = repository.getAllWidgetStatistics(1L);

        WidgetStatistics gadgetOne = widgetStatistics.get(1L);
        assertEquals(0, gadgetOne.getTotalLike());
        assertEquals(1, gadgetOne.getTotalDislike());
        assertEquals(0, gadgetOne.getUserRating());
        assertEquals(10, gadgetOne.getTotalUserCount());

        WidgetStatistics gadgetTwo = widgetStatistics.get(2L);
        assertEquals(1, gadgetTwo.getTotalLike());
        assertEquals(0, gadgetTwo.getTotalDislike());
        assertEquals(10, gadgetTwo.getUserRating());
        assertEquals(10, gadgetOne.getTotalUserCount());
    }

    @Test
    public void getUserWidgetRatings() {
        Map<Long, WidgetRating> widgetRatings = repository.getUsersWidgetRatings(1L);

        WidgetRating gadgetOne = widgetRatings.get(1L);
        assertEquals(WidgetRating.DISLIKE, gadgetOne.getScore());
        assertEquals(new Long(1), gadgetOne.getUserId());
        assertEquals(new Long(1), gadgetOne.getEntityId());

        WidgetRating gadgetTwo = widgetRatings.get(2L);
        assertEquals(WidgetRating.LIKE, gadgetTwo.getScore());
        assertEquals(new Long(1), gadgetTwo.getUserId());
        assertEquals(new Long(2), gadgetTwo.getEntityId());
    }

    @Test
    public void getEmptyUserWidgetStatistics() {
        //ensure that a bogus user has only UNSET widget ratings
        for (Map.Entry<Long, WidgetStatistics> entry : repository.getAllWidgetStatistics(Long.MAX_VALUE).entrySet()) {
            assertEquals(WidgetRating.UNSET.intValue(), entry.getValue().getUserRating());
        }
    }

    @Test
    public void getWidgetStatistics() {
        Widget widget = repository.get(1L);
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(1, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getEntityId(), 1L);
        widgetStatistics.toString();
        assertNotNull(widgetStatistics);
        assertEquals(0, widgetStatistics.getTotalLike());
        assertEquals(1, widgetStatistics.getTotalDislike());
        assertEquals(10, widgetStatistics.getTotalUserCount());
        assertEquals(WidgetRating.DISLIKE.intValue(), widgetStatistics.getUserRating());
    }

    @Test
    public void getPositiveWidgetStatistics() {
        Widget widget = repository.get(2L);
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(1, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getEntityId(), 1L);
        assertNotNull(widgetStatistics);
        assertEquals(1, widgetStatistics.getTotalLike());
        assertEquals(0, widgetStatistics.getTotalDislike());
        assertEquals(10, widgetStatistics.getTotalUserCount());
        assertEquals(WidgetRating.LIKE.intValue(), widgetStatistics.getUserRating());
    }

    @Test
    public void getMissingWidgetStatistics() {
        Widget widget = repository.get(3L);
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(0, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getEntityId(), 1L);
        assertNotNull(widgetStatistics);
        assertEquals(0, widgetStatistics.getTotalDislike());
        assertEquals(0, widgetStatistics.getTotalLike());
        assertEquals(WidgetRating.UNSET.intValue(), widgetStatistics.getUserRating());
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback    
    public void addWidgetRating() {
        Widget widget = repository.get(3L);
        assertNotNull(widget.getRatings());
        WidgetRating widgetRating = new WidgetRating();
        widgetRating.setScore(10);
        widgetRating.setUserId(1L);
        widgetRating.setWidgetId(widget.getEntityId());
        widget.getRatings().add(widgetRating);

        repository.save(widget);

        Widget reloadedWidget = repository.get(3L);
        List<WidgetRating> widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        WidgetRating reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());
        assertEquals(widget.getEntityId(), reloadedWidgetRating.getWidgetId());
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback    
    public void updateWidgetRating() {
        Widget widget = repository.get(4L);
        assertNotNull(widget.getRatings());
        WidgetRating widgetRating = new WidgetRating();
        widgetRating.setScore(10);
        widgetRating.setUserId(1L);
        widgetRating.setWidgetId(widget.getEntityId());
        widget.getRatings().add(widgetRating);

        repository.save(widget);

        Widget reloadedWidget = repository.get(4L);
        List<WidgetRating> widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        WidgetRating reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());
        assertEquals(widget.getEntityId(), reloadedWidgetRating.getWidgetId());

        reloadedWidgetRating.setScore(0);

        repository.save(reloadedWidget);
        reloadedWidget = repository.get(4L);
        widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());
        assertEquals(widget.getEntityId(), reloadedWidgetRating.getWidgetId());
    }

    @Test
    public void getWidgetTag() {
        Widget widget = repository.get(3L);
        assertNotNull(widget);
        assertEquals(widget.getTags().iterator().next().getTag().getKeyword(), "news");
        widget = repository.get(1L);
        assertNotNull(widget);
        assertEquals(widget.getTags().iterator().next().getTag().getKeyword(), "wikipedia");
    }

    @Test
    public void getWidgetsByTag() {
      String tag = "news";
        List<Widget> widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getEntityId() == 3);
        assertTrue(repository.getCountByTag(tag) == 1);

        tag = "wikipedia";
        widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getEntityId() == 1);
        assertTrue(repository.getCountByTag(tag) == 1);

        tag = "aaanews";
        widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 0);
        assertTrue(repository.getCountByTag(tag) == 0);

        widgets = repository.getWidgetsByTag("NEWS", 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getEntityId() == 3);
        assertTrue(repository.getCountByTag("NEWS") == 1);
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback    
    public void addWidgetCategory() {
        final long WIDGET_ID = 1L;
        final User user = new User(1L);

        Category category = new Category();
        category.setEntityId(1L);
        category.setText("Sample Category");
        category.setCreatedUser(user);
        category.setCreatedDate(new Date());
        category.setLastModifiedUser(user);
        category.setLastModifiedDate(new Date());
        sharedManager.merge(category);


        Widget widget = repository.get(WIDGET_ID);        
        assertThat(widget.getCategories().size(), is(2));

        widget.getCategories().add(category);                
        repository.save(widget);
        
        Widget reloadedWidget = repository.get(WIDGET_ID);
        assertThat(reloadedWidget.getCategories().size(), is(3));
        
        // test that category is in list
        boolean foundNewCategory = false;
        for (Category c : reloadedWidget.getCategories()) {
            if (c.getEntityId().equals(WIDGET_ID)) {
                foundNewCategory = true;
                break;
            }
        }
        
        assertThat(foundNewCategory, is(true));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback    
    public void removeWidgetCategory() {
        final long WIDGET_ID = 1L;

        Widget widget = repository.get(WIDGET_ID);
        assertThat(widget.getCategories().size(), is(2));

        widget.getCategories().remove(0);
        repository.save(widget);

        Widget reloadedWidget = repository.get(WIDGET_ID);
        assertThat(reloadedWidget.getCategories().size(), is(1));
        assertThat(reloadedWidget.getCategories().get(0).getEntityId(), is(4L));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback
    public void unassignWidgetOwner() {
        final long WIDGET_ID = 2L;
        final long USER_ID = 1L;
        final int NUM_WIDGETS_OWNED_BY_USER = 11;
        
        Widget widget = repository.get(WIDGET_ID);
        assertThat(widget.getOwner().getEntityId(), is(USER_ID));        
        assertThat(repository.unassignWidgetOwner(USER_ID), is(NUM_WIDGETS_OWNED_BY_USER));
        sharedManager.flush();
        sharedManager.refresh(widget);
        assertThat(widget.getOwner(), is(nullValue(User.class)));
    }
}
