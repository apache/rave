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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.portal.model.Widget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link org.apache.rave.portal.repository.impl.JpaWidgetRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:portal-test-dataContext.xml", "classpath:portal-test-applicationContext.xml"})
public class JpaWidgetRepositoryTest {

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
}
