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

import org.apache.rave.portal.model.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:portal-test-dataContext.xml", "classpath:portal-test-applicationContext.xml"})
public class JpaPageRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER = -1L;
    private static final String WIDGET_URL = "http://www.widget-dico.com/wikipedia/google/wikipedia.xml";

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PageRepository repository;

    @Test
    public void getAllPages_validUser_validPageSet() {
        List<Page> pages = repository.getAllPages(USER_ID);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), equalTo(2));
        assertThat(pages.get(0).getRegions().size(), equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().size(), equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().get(0).getWidget().getUrl(), equalTo(WIDGET_URL));
    }
    @Test
    public void getAllPages_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPages(INVALID_USER);
        assertThat(pages.isEmpty(), is(true));
    }
    @Test
    public void getAllPages_nullUser_emptySet() {
        List<Page> pages = repository.getAllPages(null);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getById_valid() {
        Page p = repository.get(1L);
        assertThat(p, is(notNullValue()));
        assertThat(p.getId(), is(equalTo(1L)));
    }

    @Test
    public void getById_invalid() {
        Page p = repository.get(-1L);
        assertThat(p, is(nullValue()));
    }


}
