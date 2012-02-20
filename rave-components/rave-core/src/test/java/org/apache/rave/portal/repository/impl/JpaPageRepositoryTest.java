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

import org.apache.rave.portal.model.Page;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import org.apache.rave.portal.repository.PageRepository;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER = -1L;
    private static final String WIDGET_URL = "http://www.widget-dico.com/wikipedia/google/wikipedia.xml";
    private static final Long USER_PAGE_TYPE_ID = 1L;
    private static final Long PERSON_PROFILE_PAGE_TYPE_ID = 2L;
    private static final Long SUB_PAGE_PAGE_TYPE_ID = 3L;

    private static final Long VALID_PARENT_PAGE_ID = 3L;
    private static final Long INVALID_PARENT_PAGE_ID = -1L;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PageRepository repository;

    @Test
    public void getAllPages_validUser_validUserPageSet() {
        List<Page> pages = repository.getAllPages(USER_ID, USER_PAGE_TYPE_ID);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), equalTo(2));
        assertThat(pages.get(0).getRegions().size(), equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().size(), equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().get(0).getWidget().getUrl(), equalTo(WIDGET_URL));
        
        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (Page p : pages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllPages_validUser_validPersonProfilePageSet() {
        List<Page> pages = repository.getAllPages(USER_ID, PERSON_PROFILE_PAGE_TYPE_ID);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), equalTo(1));
        assertThat(pages.get(0).getRegions().size(), equalTo(2));

        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (Page p : pages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllPages_validUser_validSubPagePageSet() {
        List<Page> pages = repository.getAllPages(USER_ID, SUB_PAGE_PAGE_TYPE_ID);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), equalTo(2));
        assertThat(pages.get(0).getRegions().size(), equalTo(2));
        assertThat(pages.get(0).getParentPage().getEntityId(), equalTo(3L));

        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (Page p : pages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllPages_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPages(INVALID_USER, USER_PAGE_TYPE_ID);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getAllPages_nullUser_emptySet() {
        List<Page> pages = repository.getAllPages(null, USER_PAGE_TYPE_ID);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getById_valid_userPage() {
        Page p = repository.get(USER_PAGE_TYPE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getEntityId(), is(equalTo(USER_PAGE_TYPE_ID)));
    }

    @Test
    public void getById_valid_personProfilePage() {
        Page p = repository.get(PERSON_PROFILE_PAGE_TYPE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getEntityId(), is(equalTo(PERSON_PROFILE_PAGE_TYPE_ID)));
    }

    @Test
    public void getById_valid_subPagePage() {
        Page p = repository.get(SUB_PAGE_PAGE_TYPE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getEntityId(), is(equalTo(SUB_PAGE_PAGE_TYPE_ID)));
    }

    @Test
    public void getById_invalid() {
        Page p = repository.get(-1L);
        assertThat(p, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deletePages_userPageType() {
        int numPages = repository.getAllPages(USER_ID, USER_PAGE_TYPE_ID).size();
        assertThat(numPages > 0, is(true));
        int deletedPages = repository.deletePages(USER_ID, USER_PAGE_TYPE_ID);
        assertThat(deletedPages, is(numPages));
        // ensure pages are deleted
        assertThat(repository.getAllPages(USER_ID, USER_PAGE_TYPE_ID).isEmpty(), is(true));
    }
}
