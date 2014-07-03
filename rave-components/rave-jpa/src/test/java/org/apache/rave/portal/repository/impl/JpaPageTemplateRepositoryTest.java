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

import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateWidget;
import org.apache.rave.model.PageType;
import org.apache.rave.portal.model.JpaPageTemplate;
import org.apache.rave.portal.model.JpaPageTemplateRegion;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageTemplateRepositoryTest {

    private static final Long USER_ID = 1L;
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
    private PageTemplateRepository pageTemplateRepository;

    @Test
    public void getAll_valid(){
        List<PageTemplate> pageTemplateList = pageTemplateRepository.getAll();
        assertThat(pageTemplateList.size(), is(equalTo(3)));
    }

    @Test
    public void getAll_forType_valid(){
        List<PageTemplate> pageTemplateList = pageTemplateRepository.getAll("PERSON_PROFILE");
        assertThat(pageTemplateList.size(), is(equalTo(2)));
    }

    @Test
    public void getAll_limited(){
        List<PageTemplate> pageTemplateList = pageTemplateRepository.getLimitedList(0, 1);
        assertThat(pageTemplateList.size(), is(equalTo(1)));
    }

    @Test
    public void get_valid(){
        PageTemplate pageTemplateList = pageTemplateRepository.get("1");
        assertNotNull(pageTemplateList);
        assertThat(pageTemplateList.getId(), is(equalTo("1")));
        assertThat(pageTemplateList.getPageType(), is(equalTo("PERSON_PROFILE")));
    }
    
    @Test
    public void getDefaultPersonPage_valid(){
        // get default page template
        JpaPageTemplate pt = (JpaPageTemplate)pageTemplateRepository.getDefaultPage(PageType.PERSON_PROFILE.toString().toUpperCase());
        // default page tests
        assertNotNull(pt);
        assertEquals("Template for person profile pages", pt.getDescription());
        assertEquals("Person Profile", pt.getName());
        assertEquals(PageType.PERSON_PROFILE.toString().toUpperCase(), pt.getPageType());
        assertEquals(0, pt.getRenderSequence());
        assertTrue(pt.isDefaultTemplate());
        assertEquals("# of regions for parent page", 1, pt.getPageTemplateRegions().size());
        assertEquals("person_profile", pt.getPageLayout().getCode());
        assertEquals("# of widgets on parent page region", 2, pt.getPageTemplateRegions().get(0).getPageTemplateWidgets().size());
        assertEquals("# of sub pages for parent page",2, pt.getSubPageTemplates().size());
        // get default page sub pages
        JpaPageTemplate subPage1 = (JpaPageTemplate)pt.getSubPageTemplates().get(0);
        JpaPageTemplate subPage2 = (JpaPageTemplate)pt.getSubPageTemplates().get(1);
        // sub page 1 tests
        assertNotNull(subPage1);
        assertEquals("Template for the About sub page for the person profile", subPage1.getDescription());
        assertEquals("About", subPage1.getName());
        assertEquals(PageType.SUB_PAGE.toString().toUpperCase(), subPage1.getPageType());
        assertEquals(0, subPage1.getRenderSequence());
        assertFalse(subPage1.isDefaultTemplate());
        assertEquals("# of regions for sub page 1", 1, subPage1.getPageTemplateRegions().size());
        assertEquals("columns_1", subPage1.getPageLayout().getCode());
        assertEquals("# of widgets on sub page 1 region 1", 2, subPage1.getPageTemplateRegions().get(0).getPageTemplateWidgets().size());
        assertEquals("# of sub pages for sub page 1", 0, subPage1.getSubPageTemplates().size());
        // sub page 2 tests
        assertNotNull(subPage2);
        assertEquals("Template for the My Activity sub page for the person profile", subPage2.getDescription());
        assertEquals("My Activity", subPage2.getName());
        assertEquals(PageType.SUB_PAGE.toString().toUpperCase(), subPage2.getPageType());
        assertEquals(1, subPage2.getRenderSequence());
        assertFalse(subPage2.isDefaultTemplate());
        assertEquals("# of regions for sub page 2", 1, subPage2.getPageTemplateRegions().size());
        assertEquals("columns_1", subPage2.getPageLayout().getCode());
        assertEquals("# of widgets on sub page 2 region 1", 1, subPage2.getPageTemplateRegions().get(0).getPageTemplateWidgets().size());
        assertEquals("# of sub pages for sub page 2", 0, subPage2.getSubPageTemplates().size());
        // parent page region tests
        JpaPageTemplateRegion ptRegion1 = (JpaPageTemplateRegion)pt.getPageTemplateRegions().get(0);
        assertEquals(pt.getEntityId(), ptRegion1.getPageTemplate().getEntityId());
        assertEquals(0, ptRegion1.getRenderSequence());
        assertEquals(2, ptRegion1.getPageTemplateWidgets().size());
        assertTrue(ptRegion1.isLocked());
        // parent page region 1 widget 1 tests
        PageTemplateWidget ptw1 = ptRegion1.getPageTemplateWidgets().get(0);
        assertEquals(((JpaPageTemplateRegion)ptw1.getPageTemplateRegion()).getEntityId(), ptRegion1.getEntityId());
        assertEquals(0, ptw1.getRenderSeq());
        assertNotNull(ptw1.getWidgetId());
        assertTrue(ptw1.isLocked());
        // parent page region widget 2 tests
        PageTemplateWidget ptw2 = ptRegion1.getPageTemplateWidgets().get(1);
        assertEquals(((JpaPageTemplateRegion)ptw2.getPageTemplateRegion()).getEntityId(), ptRegion1.getEntityId());
        assertEquals(1, ptw2.getRenderSeq());
        assertNotNull(ptw2.getWidgetId());
        assertTrue(ptw2.isLocked());
        // sub page 1 region 1 tests
        JpaPageTemplateRegion sp1Region1 = (JpaPageTemplateRegion)subPage1.getPageTemplateRegions().get(0);
        assertEquals(subPage1.getEntityId(), sp1Region1.getPageTemplate().getEntityId());
        assertEquals(0, sp1Region1.getRenderSequence());
        assertEquals(2, sp1Region1.getPageTemplateWidgets().size());
        assertTrue(sp1Region1.isLocked());
        // sub page 1 region 1 widget 1 tests
        PageTemplateWidget spw1 = sp1Region1.getPageTemplateWidgets().get(0);
        assertEquals(((JpaPageTemplateRegion)spw1.getPageTemplateRegion()).getEntityId(), sp1Region1.getEntityId());
        assertEquals(0, spw1.getRenderSeq());
        assertNotNull(spw1.getWidgetId());
        assertTrue(spw1.isLocked());
        // sub page 1 region 1 widget 2 tests
        PageTemplateWidget spw2 = sp1Region1.getPageTemplateWidgets().get(1);
        assertEquals(((JpaPageTemplateRegion)spw2.getPageTemplateRegion()).getEntityId(), sp1Region1.getEntityId());
        assertEquals(1, spw2.getRenderSeq());
        assertNotNull(spw2.getWidgetId());
        assertTrue(spw2.isLocked());
        // sub page 2 region tests
        JpaPageTemplateRegion sp2Region1 = (JpaPageTemplateRegion)subPage2.getPageTemplateRegions().get(0);
        assertEquals(subPage2.getEntityId(), sp2Region1.getPageTemplate().getEntityId());
        assertEquals(0, sp2Region1.getRenderSequence());
        assertEquals(1, sp2Region1.getPageTemplateWidgets().size());
        assertTrue(sp2Region1.isLocked());
        // sub page 2 region widget 1 tests
        PageTemplateWidget sp2w1 = sp2Region1.getPageTemplateWidgets().get(0);
        assertEquals(((JpaPageTemplateRegion)sp2w1.getPageTemplateRegion()).getEntityId(), sp2Region1.getEntityId());
        assertEquals(0, sp2w1.getRenderSeq());
        assertNotNull(sp2w1.getWidgetId());
        assertTrue(sp2w1.isLocked());

    }


    @Test
    public void getDefaultUserPage_valid(){
        // get default page template
        JpaPageTemplate pt = (JpaPageTemplate)pageTemplateRepository.getDefaultPage(PageType.USER.toString().toUpperCase());
        // default page tests
        assertNotNull(pt);
        assertEquals("User profile pages", pt.getDescription());
        assertEquals("User Profile", pt.getName());
        assertEquals(PageType.USER.toString().toUpperCase(), pt.getPageType());
        assertEquals(0, pt.getRenderSequence());
        assertTrue(pt.isDefaultTemplate());
        assertEquals("# of regions for parent page", 1, pt.getPageTemplateRegions().size());
        assertEquals("person_profile", pt.getPageLayout().getCode());
        assertEquals("# of widgets on parent page region", 2, pt.getPageTemplateRegions().get(0).getPageTemplateWidgets().size());

        // parent page region tests
        JpaPageTemplateRegion ptRegion1 = (JpaPageTemplateRegion)pt.getPageTemplateRegions().get(0);
        assertEquals(pt.getEntityId(), ptRegion1.getPageTemplate().getEntityId());
        assertEquals(0, ptRegion1.getRenderSequence());
        assertEquals(2, ptRegion1.getPageTemplateWidgets().size());
        assertTrue(ptRegion1.isLocked());
        // parent page region 1 widget 1 tests
        PageTemplateWidget ptw1 = ptRegion1.getPageTemplateWidgets().get(0);
        assertEquals(((JpaPageTemplateRegion)ptw1.getPageTemplateRegion()).getEntityId(), ptRegion1.getEntityId());
        assertEquals(0, ptw1.getRenderSeq());
        assertNotNull(ptw1.getWidgetId());
        assertTrue(ptw1.isLocked());
        // parent page region widget 2 tests
        PageTemplateWidget ptw2 = ptRegion1.getPageTemplateWidgets().get(1);
        assertEquals(((JpaPageTemplateRegion)ptw2.getPageTemplateRegion()).getEntityId(), ptRegion1.getEntityId());
        assertEquals(1, ptw2.getRenderSeq());
        assertNotNull(ptw2.getWidgetId());
        assertTrue(ptw2.isLocked());
    }
}
