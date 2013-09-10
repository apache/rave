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
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageRepositoryTest {

    private static final String USER_ID = "1";
    private static final String CREATED_USER_ID = "6";
    private static final String INVALID_USER = "-1";
    private static final String WIDGET_URL = "http://www.widget-dico.com/wikipedia/google/wikipedia.xml";
    private static final String USER_PAGE_ID = "1";
    private static final String PERSON_PROFILE_PAGE_ID = "3";
    private static final String SUB_PAGE_ID = "4";

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

    @Autowired
    private WidgetRepository widgetRepository;

    private JpaUser user;
    private PageTemplate defaultPageTemplate;

    @Before
    public void setup(){
        user = (JpaUser)userRepository.get(CREATED_USER_ID);
        defaultPageTemplate = pageTemplateRepository.getDefaultPage(PageType.PERSON_PROFILE.toString().toUpperCase());
    }

    @Test
    public void getAllPages_validUser_validUserPageSet() {
        List<Page> pages = repository.getAllPagesForUserType(USER_ID, PageType.USER.toString());
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), equalTo(2));
        assertThat(pages.get(0).getRegions().size(), equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().size(), equalTo(2));
        Widget widget = widgetRepository.get(pages.get(0).getRegions().get(0).getRegionWidgets().get(0).getWidgetId());
        assertThat(widget, is(notNullValue()));
        assertThat(widget.getUrl(), equalTo(WIDGET_URL));

        List<PageUser> pageUserPages = repository.getPagesForUser(USER_ID, PageType.USER.toString().toUpperCase());
        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (PageUser p : pageUserPages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllPages_validUser_validPersonProfilePageSet() {
        List<Page> pages = repository.getAllPagesForUserType(USER_ID, PageType.PERSON_PROFILE.toString());
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), is(1));
        assertThat(pages.get(0).getRegions().size(), is(1));

        List<PageUser> pageUserPages = repository.getPagesForUser(USER_ID, PageType.PERSON_PROFILE.toString().toUpperCase());
        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (PageUser p : pageUserPages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getPagesByContextAndType() {
        List<Page> pages = repository.getPagesForContextType("foo", PageType.USER.toString());
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), is(1));
    }

    @Test
    public void getAllPages_validUser_validSubPagePageSet() {
        List<Page> pages = repository.getAllPagesForUserType(USER_ID, PageType.SUB_PAGE.toString());
        assertThat(pages, is(notNullValue()));
        assertThat(pages.size(), is(2));
        assertThat(pages.get(0).getRegions().size(), is(1));
        assertThat(pages.get(0).getParentPage().getId(), is("3"));

        List<PageUser> pageUserPages = repository.getPagesForUser(USER_ID, PageType.SUB_PAGE.toString().toUpperCase());
        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (PageUser p : pageUserPages) {
            Long currentRenderSequence = p.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllPages_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPagesForUserType(INVALID_USER, PageType.USER.toString());
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getAllPages_person_profile_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPagesForUserType(INVALID_USER, PageType.PERSON_PROFILE.toString());
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getAllPages_nullUser_emptySet() {
        List<Page> pages = repository.getAllPagesForUserType(null, PageType.USER.toString());
        assertThat(pages.isEmpty(), is(true));
    }

    @Test
    public void getById_valid_userPage() {
        Page p = repository.get(USER_PAGE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getId(), is(equalTo(USER_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.USER.toString().toUpperCase()));
        assertThat(p.getParentPage(), is(nullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(true));
    }

    @Test
    public void getById_valid_personProfilePage() {
        Page p = repository.get(PERSON_PROFILE_PAGE_ID);
        assertThat(p.getId(), is(equalTo(PERSON_PROFILE_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.PERSON_PROFILE.toString().toUpperCase()));
        assertThat(p.getParentPage(), is(nullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(false));

        // verify that the sub pages are in proper order
        Long lastRenderSequence = -1L;
        PageUser pageUser;
        for (Page subPage : p.getSubPages()) {
            pageUser = repository.getSingleRecord(p.getOwnerId(), subPage.getId());
            Long currentRenderSequence =  pageUser.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }

    }

    @Test
    public void getById_valid_subPagePage() {
        Page p = repository.get(SUB_PAGE_ID);
        assertThat(p, is(notNullValue()));
        assertThat(p.getId(), is(equalTo(SUB_PAGE_ID)));
        assertThat(p.getPageType(), is(PageType.SUB_PAGE.toString().toUpperCase()));
        assertThat(p.getParentPage(), is(notNullValue(Page.class)));
        assertThat(p.getSubPages().isEmpty(), is(true));
    }

    @Test
    public void getById_invalid() {
        Page p = repository.get("-1");
        assertThat(p, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deletePages_userPageType() {
        int numPages = repository.getAllPagesForUserType(USER_ID, PageType.USER.toString()).size();
        assertThat(numPages > 0, is(true));
        int deletedPages = repository.deletePages(USER_ID, PageType.USER.toString().toUpperCase());
        assertThat(deletedPages, is(numPages));
        // ensure pages are deleted
        assertThat(repository.getAllPagesForUserType(USER_ID, PageType.USER.toString()).isEmpty(), is(true));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deletePage_pageWithSubPage() {
        Page p = repository.get(PERSON_PROFILE_PAGE_ID);
        assertThat(p, is(notNullValue()));
        // grab all the sub page ids
        List<String> subPageIds = new ArrayList<String>();
        assertThat(p.getSubPages().isEmpty(), is(false));
        for (Page subPage : p.getSubPages()) {
            subPageIds.add(subPage.getId());
        }

        repository.delete(p);
        p = repository.get(PERSON_PROFILE_PAGE_ID);
        // verify page is deleted along with all sub pages
        assertThat(p, is(nullValue()));
        for (String i : subPageIds){
            assertThat(repository.get(i), is(nullValue()));
        }
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deletePage_implObject() {
        Page p = repository.get(USER_ID);
        assertThat(p, is(notNullValue()));
        PageImpl impl = new PageImpl(p.getId());

        repository.delete(impl);
        p = repository.get(USER_ID);
        assertThat(p, is(nullValue()));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback(true)
    public void createPageForUser_validUser(){
        Page page = repository.createPageForUser(user, defaultPageTemplate);
        assertEquals(user.getId(), page.getOwnerId());
        assertEquals(page.getName(), defaultPageTemplate.getName());
        assertNull(page.getParentPage());
        assertEquals(2, page.getSubPages().size());
        assertNotNull(page.getPageLayout());
        assertEquals("person_profile", page.getPageLayout().getCode());
        assertEquals(1, page.getRegions().size());
        assertEquals(PageType.PERSON_PROFILE.toString().toUpperCase(), page.getPageType());
        Page subPage1 = page.getSubPages().get(0);
        Page subPage2 = page.getSubPages().get(1);
        assertEquals("Widgets on sub page 1", 2, subPage1.getRegions().get(0).getRegionWidgets().size());
        assertEquals("Widgets on sub page 2", 1, subPage2.getRegions().get(0).getRegionWidgets().size());
        assertEquals("Regions on sub page 1", 1, subPage1.getRegions().size());
        assertEquals("Regions on sub page 2", 1, subPage2.getRegions().size());
        assertThat(subPage1.getSubPages().isEmpty(), is(true));
        assertThat(subPage2.getSubPages().isEmpty(), is(true));
        assertEquals("sub page 1 refers to parent page", page.getId(), subPage1.getParentPage().getId());
        assertEquals("sub page 2 refers to parent page", page.getId(), subPage2.getParentPage().getId());
        assertEquals("sub page 1 regions refers to sub page 1", subPage1.getId(), subPage1.getRegions().get(0).getPage().getId());
        assertEquals("sub page 2 regions refers to sub page 2", subPage2.getId(), subPage2.getRegions().get(0).getPage().getId());
        assertEquals("sub page 1 has one column layout", "columns_1", subPage1.getPageLayout().getCode());
        assertEquals("sub page 2 has one column layout", "columns_1", subPage2.getPageLayout().getCode());
        assertEquals(PageType.SUB_PAGE.toString().toUpperCase(), subPage1.getPageType());
        assertEquals(defaultPageTemplate.getSubPageTemplates().get(0).getName(), subPage1.getName());
        assertEquals(PageType.SUB_PAGE.toString().toUpperCase(), subPage2.getPageType());
        assertEquals(defaultPageTemplate.getSubPageTemplates().get(1).getName(), subPage2.getName());
        assertEquals(user.getId(), subPage1.getOwnerId());
        assertEquals(user.getId(), subPage2.getOwnerId());
    }

    @Test
    public void hasPersonPage_true(){
        assertTrue(repository.hasPersonPage(USER_ID));
    }

    @Test
    public void hasPersonPage_false(){
        assertFalse(repository.hasPersonPage(CREATED_USER_ID));
    }

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaPage.class);
    }

    @Test
    public void getAll(){
        List<Page> pages = repository.getAll();
        assertThat(pages.size(), is(17));
    }

    @Test
    public void getLimitedList(){
        List<Page> pages = repository.getLimitedList(2, 5);
        assertThat(pages.size(), is(5));
        assertThat(pages.get(0).getId(), is("3"));
        assertThat(pages.get(1).getId(), is("4"));
        assertThat(pages.get(2).getId(), is("5"));
        assertThat(pages.get(3).getId(), is("6"));
        assertThat(pages.get(4).getId(), is("7"));
    }

    @Test
    public void getCountAll(){
        int count = repository.getCountAll();
        assertThat(count, is(17));
    }
}