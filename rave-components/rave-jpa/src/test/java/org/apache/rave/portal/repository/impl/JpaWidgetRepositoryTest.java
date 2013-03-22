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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Ignore;
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaWidget.class);
    }

    @Test
    public void getById_valid() {
        JpaWidget widget = (JpaWidget)repository.get("1");
        assertThat(widget, is(notNullValue()));
        assertThat(widget.getEntityId(), is(equalTo(1L)));
    }

    @Test
    public void getById_invValid() {
        Widget widget = repository.get("-1");
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

        assertThat(repository.getByFreeTextSearch("", 1, 1).isEmpty(), is(true));
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

        assertThat(repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PREVIEW, type, "", 0, 1).size(), is(1));
        assertThat(repository.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, "", searchTerm, 0, 1).size(), is(1));
        assertThat(repository.getByStatusAndTypeAndFreeTextSearch(null, type, searchTerm, 0, 1).size(), is(1));
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
        final User user = new JpaUser(2L);
        List<Widget> widgets = repository.getByOwner(user, 0, 10);
        assertEquals(1, widgets.size());
    }

    @Test
    public void getCountByOwner() {
        final User user = new JpaUser(2L);
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

        Widget widget = new JpaWidget();
        widget.setTitle("Widget with long description");
        widget.setUrl(url);
        widget.setDescription(longDescription);
        widget = repository.save(widget);
        assertNotNull(widget.getId());
        assertEquals(longDescription, widget.getDescription());
    }

    @Test
    public void getAllWidgetStatistics() {
        Map<String, WidgetStatistics> widgetStatistics = repository.getAllWidgetStatistics("1");

        WidgetStatistics gadgetOne = widgetStatistics.get("1");
        assertNotNull(gadgetOne);
        assertEquals(0, gadgetOne.getTotalLike());
        assertEquals(1, gadgetOne.getTotalDislike());
        assertEquals(0, gadgetOne.getUserRating());
        assertEquals(10, gadgetOne.getTotalUserCount());

        WidgetStatistics gadgetTwo = widgetStatistics.get("2");
        assertNotNull(gadgetTwo);
        assertEquals(1, gadgetTwo.getTotalLike());
        assertEquals(1, gadgetTwo.getTotalDislike());
        assertEquals(10, gadgetTwo.getUserRating());
        assertEquals(10, gadgetOne.getTotalUserCount());
    }

    @Test
    public void getUserWidgetRatings() {
        Map<String, WidgetRating> widgetRatings = repository.getUsersWidgetRatings("1");

        WidgetRating gadgetOne = widgetRatings.get("1");
        assertNotNull(gadgetOne);
        assertEquals(JpaWidgetRating.DISLIKE, gadgetOne.getScore());
        assertEquals("1", gadgetOne.getUserId());
        assertEquals("1", gadgetOne.getId());

        WidgetRating gadgetTwo = widgetRatings.get("2");
        assertNotNull(gadgetTwo);
        assertEquals(JpaWidgetRating.LIKE, gadgetTwo.getScore());
        assertEquals("1", gadgetTwo.getUserId());
        assertEquals("2", gadgetTwo.getId());
    }

    @Test
    public void getEmptyUserWidgetStatistics() {
        //ensure that a bogus user has only UNSET widget ratings
        for (Map.Entry<String, WidgetStatistics> entry : repository.getAllWidgetStatistics("99999").entrySet()) {
            assertEquals(JpaWidgetRating.UNSET.intValue(), entry.getValue().getUserRating());
        }
    }

    @Test
    public void getWidgetStatistics() {
        Widget widget = repository.get("1");
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(1, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getId(), "1");
        widgetStatistics.toString();
        assertNotNull(widgetStatistics);
        assertEquals(0, widgetStatistics.getTotalLike());
        assertEquals(1, widgetStatistics.getTotalDislike());
        assertEquals(10, widgetStatistics.getTotalUserCount());
        assertEquals(JpaWidgetRating.DISLIKE.intValue(), widgetStatistics.getUserRating());
    }

    @Test
    public void getPositiveWidgetStatistics() {
        Widget widget = repository.get("2");
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(2, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getId(), "1");
        assertNotNull(widgetStatistics);
        assertEquals(1, widgetStatistics.getTotalLike());
        assertEquals(1, widgetStatistics.getTotalDislike());
        assertEquals(10, widgetStatistics.getTotalUserCount());
        assertEquals(JpaWidgetRating.LIKE.intValue(), widgetStatistics.getUserRating());
    }

    @Test
    public void getMissingWidgetStatistics() {
        Widget widget = repository.get("3");
        List<WidgetRating> ratings = widget.getRatings();
        assertNotNull(ratings);
        assertEquals(0, ratings.size());

        WidgetStatistics widgetStatistics = repository.getWidgetStatistics(widget.getId(), "1");
        assertNotNull(widgetStatistics);
        assertEquals(0, widgetStatistics.getTotalDislike());
        assertEquals(0, widgetStatistics.getTotalLike());
        assertEquals(JpaWidgetRating.UNSET.intValue(), widgetStatistics.getUserRating());
    }

    // This test fails because Widget no longer works with the generic converter concept.
    @Test
    @Ignore
    @Transactional(readOnly = false)
    @Rollback
    public void addWidgetRating() {
        Widget widget = repository.get("3");
        assertNotNull(widget.getRatings());
        WidgetRating widgetRating = new JpaWidgetRating();
        widgetRating.setScore(10);
        widgetRating.setUserId("1");
        widget.getRatings().add(widgetRating);

        repository.save(widget);

        Widget reloadedWidget = repository.get("3");
        List<WidgetRating> widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        WidgetRating reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());
    }

    // This test fails because Widget no longer works with the generic converter concept.
    @Test
    @Ignore
    @Transactional(readOnly = false)
    @Rollback
    public void updateWidgetRating() {
        Widget widget = repository.get("4");
        assertNotNull(widget.getRatings());
        WidgetRating widgetRating = new JpaWidgetRating();
        widgetRating.setScore(10);
        widgetRating.setUserId("1");
        widget.getRatings().add(widgetRating);

        repository.save(widget);

        Widget reloadedWidget = repository.get("4");
        List<WidgetRating> widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        WidgetRating reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());

        reloadedWidgetRating.setScore(0);

        repository.save(reloadedWidget);
        reloadedWidget = repository.get("4");
        widgetRatings = reloadedWidget.getRatings();
        assertNotNull(widgetRatings);
        assertEquals(1, widgetRatings.size());
        reloadedWidgetRating = widgetRatings.get(0);
        assertNotNull(reloadedWidgetRating);
        assertEquals(widgetRating.getScore(), reloadedWidgetRating.getScore());
        assertEquals(widgetRating.getUserId(), reloadedWidgetRating.getUserId());
    }

    @Test
    public void getWidgetTag() {
        Widget widget = repository.get("3");
        Tag news = tagRepository.getByKeyword("news");
        assertNotNull(widget);
        assertEquals(widget.getTags().iterator().next().getTagId(), news.getId());
        widget = repository.get("1");
        Tag wikipedia = tagRepository.getByKeyword("wikipedia");
        assertNotNull(widget);
        assertEquals(widget.getTags().iterator().next().getTagId(), wikipedia.getId());
    }

    @Test
    public void getWidgetsByTag() {
        String tag = "news";
        List<Widget> widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getId().equals("3"));
        assertTrue(repository.getCountByTag(tag) == 1);

        tag = "wikipedia";
        widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getId().equals("1"));
        assertTrue(repository.getCountByTag(tag) == 1);

        tag = "aaanews";
        widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.size() == 0);
        assertTrue(repository.getCountByTag(tag) == 0);

        widgets = repository.getWidgetsByTag("NEWS", 0, 10);
        assertTrue(widgets.size() == 1);
        assertTrue(widgets.iterator().next().getId().equals("3"));
        assertTrue(repository.getCountByTag("NEWS") == 1);

        tag = null;
        widgets = repository.getWidgetsByTag(tag, 0, 10);
        assertTrue(widgets.isEmpty());
        assertTrue(repository.getCountByTag(tag) == 0);

    }

    @Test
    @Transactional(readOnly = false)
    @Rollback
    public void addWidgetCategory() {
        final String WIDGET_ID = "1";
        final User user = new JpaUser(1L);

        Category category = new JpaCategory();
        category.setId("1");
        category.setText("Sample Category");
        category.setCreatedUserId(user.getId());
        category.setCreatedDate(new Date());
        category.setLastModifiedUserId(user.getId());
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
            if (c.getId().equals(WIDGET_ID)) {
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
        final String WIDGET_ID = "1";

        Widget widget = repository.get(WIDGET_ID);
        assertThat(widget.getCategories().size(), is(2));

        widget.getCategories().remove(0);
        repository.save(widget);

        Widget reloadedWidget = repository.get(WIDGET_ID);
        assertThat(reloadedWidget.getCategories().size(), is(1));
        assertThat(reloadedWidget.getCategories().get(0).getId(), is("4"));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback
    public void unassignWidgetOwner() {
        final String WIDGET_ID = "2";
        final String USER_ID = "1";
        final int NUM_WIDGETS_OWNED_BY_USER = 16;

        Widget widget = repository.get(WIDGET_ID);
        assertThat(widget.getOwnerId(), is(USER_ID));
        assertThat(repository.unassignWidgetOwner(USER_ID), is(NUM_WIDGETS_OWNED_BY_USER));
        sharedManager.flush();
        sharedManager.refresh(widget);
        assertThat(widget.getOwnerId(), is(nullValue()));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback
    public void delete() {
        final String WIDGET_ID = "2";

        Widget widget = repository.get(WIDGET_ID);
        assertThat(widget, is(notNullValue()));
        repository.delete(widget);
        assertThat(repository.get(WIDGET_ID), is(nullValue()));
    }
}
