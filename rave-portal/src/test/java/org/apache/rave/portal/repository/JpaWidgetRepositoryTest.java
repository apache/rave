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

package org.apache.rave.portal.repository;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link org.apache.rave.portal.repository.impl.JpaWidgetRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:portal-test-dataContext.xml",
        "classpath:portal-test-applicationContext.xml"})
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
        assertThat(widget.getId(), is(equalTo(1L)));
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
        try{
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
    public void getByStatusAndFreeText() {
        final String searchTerm = "gAdGet";
        List<Widget> widgets = repository.getByStatusAndFreeTextSearch(WidgetStatus.PUBLISHED,
                searchTerm, 0, 1);
        assertEquals(1, widgets.size());

        List<Widget> preview = repository.getByStatusAndFreeTextSearch(WidgetStatus.PREVIEW,
                searchTerm, 0, 1);
        assertEquals(0, preview.size());
    }

    @Test
    public void countByStatusAndFreeText() {
        final String searchTerm = "gAdGet";
        int publishedCount = repository.getCountByStatusAndFreeText(WidgetStatus.PUBLISHED,
                searchTerm);
        assertTrue(publishedCount >= 2);
    }

    @Test
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
        assertNotNull(widget.getId());
        assertEquals(longDescription, widget.getDescription());
    }

}
