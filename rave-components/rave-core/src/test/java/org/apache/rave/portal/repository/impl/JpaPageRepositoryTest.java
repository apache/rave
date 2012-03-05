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
import org.apache.rave.portal.model.PageTemplate;
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final Long CREATED_USER_ID = 6L;
    private static final Long INVALID_USER = -1L;
    private static final String WIDGET_URL = "http://www.widget-dico.com/wikipedia/google/wikipedia.xml";
    private static final Long USER_PAGE_ID = 1L;
    private static final Long PERSON_PROFILE_PAGE_ID = 3L;
    private static final Long SUB_PAGE_ID = 4L;

    private static final Long VALID_PARENT_PAGE_ID = 3L;
    private static final Long INVALID_PARENT_PAGE_ID = -1L;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PageRepository repository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageTemplateRepository pageTemplateRepository;

    private User user;
    private PageTemplate defaultPageTemplate;
    
    @Before
    public void setup(){
        user = userRepository.get(CREATED_USER_ID);
        defaultPageTemplate = pageTemplateRepository.getDefaultPersonPage();
    }

    @Test
    public void getAllPages_validUser_validUserPageSet() {
        List<Page> pages = repository.getAllPages(USER_ID, PageType.USER);
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
        List<Page> pages = repository.getAllPages(USER_ID, PageType.PERSON_PROFILE);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), is(1));
        assertThat(pages.get(0).getRegions().size(), is(1));

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
        List<Page> pages = repository.getAllPages(USER_ID, PageType.SUB_PAGE);
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), is(2));
        assertThat(pages.get(0).getRegions().size(), is(1));
        assertThat(pages.get(0).getParentPage().getEntityId(), is(3L));

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
        List<Page> pages = repository.getAllPages(INVALID_USER, PageType.USER);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getAllPages_person_profile_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPages(INVALID_USER, PageType.PERSON_PROFILE);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getAllPages_nullUser_emptySet() {
        List<Page> pages = repository.getAllPages(null, PageType.USER);
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getById_valid_userPage() {
        Page p = repository.get(USER_PAGE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getEntityId(), is(equalTo(USER_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.USER));
        assertThat(p.getParentPage(), is(nullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(true));
    }

    @Test
    public void getById_valid_personProfilePage() {
        Page p = repository.get(PERSON_PROFILE_PAGE_ID);
        assertThat(p.getEntityId(), is(equalTo(PERSON_PROFILE_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.PERSON_PROFILE));
        assertThat(p.getParentPage(), is(nullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(false));

        // verify that the sub pages are in proper order
        Long lastRenderSequence = -1L;
        for (Page subPage : p.getSubPages()) {
            Long currentRenderSequence = subPage.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
        
    }

    @Test
    public void getById_valid_subPagePage() {
        Page p = repository.get(SUB_PAGE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getEntityId(), is(equalTo(SUB_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.SUB_PAGE));
        assertThat(p.getParentPage(), is(notNullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(true));
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
        int numPages = repository.getAllPages(USER_ID, PageType.USER).size();
        assertThat(numPages > 0, is(true));
        int deletedPages = repository.deletePages(USER_ID, PageType.USER);
        assertThat(deletedPages, is(numPages));
        // ensure pages are deleted
        assertThat(repository.getAllPages(USER_ID, PageType.USER).isEmpty(), is(true));
    }
    
    @Test
    @Transactional(readOnly = false)
    @Rollback(true)
    public void createPersonPageForUser_validUser(){
        Page page = repository.createPersonPageForUser(user, defaultPageTemplate);
        assertSame(user, page.getOwner());
        assertEquals(page.getName(), defaultPageTemplate.getName());
        assertNull(page.getParentPage());
        assertEquals(2, page.getSubPages().size());
        assertNotNull(page.getRenderSequence());
        assertNotNull(page.getPageLayout());
        assertEquals("person_profile", page.getPageLayout().getCode());
        assertEquals(1, page.getRegions().size());
        assertEquals(PageType.PERSON_PROFILE, page.getPageType());
        Page subPage1 = page.getSubPages().get(0);
        Page subPage2 = page.getSubPages().get(1);
        assertEquals("Widgets on sub page 1", 2, subPage1.getRegions().get(0).getRegionWidgets().size());
        assertEquals("Widgets on sub page 2", 1, subPage2.getRegions().get(0).getRegionWidgets().size());
        assertEquals("Regions on sub page 1", 1, subPage1.getRegions().size());
        assertEquals("Regions on sub page 2", 1, subPage2.getRegions().size());
        assertNull("no sub pages of sub page 1", subPage1.getSubPages());
        assertNull("no sub pages of sub page 2", subPage2.getSubPages());
        assertEquals("sub page 1 refers to parent page", page.getEntityId(), subPage1.getParentPage().getEntityId());
        assertEquals("sub page 2 refers to parent page", page.getEntityId(), subPage2.getParentPage().getEntityId());
        assertEquals("sub page 1 regions refers to sub page 1", subPage1.getEntityId(), subPage1.getRegions().get(0).getPage().getEntityId());
        assertEquals("sub page 2 regions refers to sub page 2", subPage2.getEntityId(), subPage2.getRegions().get(0).getPage().getEntityId());
        assertEquals("sub page 1 has one column layout", "columns_1", subPage1.getPageLayout().getCode());
        assertEquals("sub page 2 has one column layout", "columns_1", subPage2.getPageLayout().getCode());
        assertEquals(PageType.SUB_PAGE, subPage1.getPageType());
        assertEquals(defaultPageTemplate.getSubPageTemplates().get(0).getName(), subPage1.getName());
        assertEquals(PageType.SUB_PAGE, subPage2.getPageType());
        assertEquals(defaultPageTemplate.getSubPageTemplates().get(1).getName(), subPage2.getName());
        assertSame(user, subPage1.getOwner());
        assertSame(user, subPage2.getOwner());
        assertNotNull(subPage1.getRenderSequence());
        assertNotNull(subPage2.getRenderSequence());
    }
    
    @Test
    public void hasPersonPage_true(){
        assertTrue(repository.hasPersonPage(USER_ID));
    }

    @Test
    public void hasPersonPage_false(){
        assertFalse(repository.hasPersonPage(CREATED_USER_ID));
    }
}