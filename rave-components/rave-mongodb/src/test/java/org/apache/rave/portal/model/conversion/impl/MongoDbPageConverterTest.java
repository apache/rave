/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.MongoDbPage;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test class for MongoDb Page Converter
 */
public class MongoDbPageConverterTest {

    private PageLayoutRepository pageLayoutRepository;

    private MongoDbPageConverter converter;
    public static final String USER1ID = "1234";
    public static final String USER2ID = "1222";
    public static final String PAGEID = "1234";

    @Before
    public void setup() {
        converter = new MongoDbPageConverter();
        pageLayoutRepository = createNiceMock(PageLayoutRepository.class);
        converter.setPageLayoutRepository(pageLayoutRepository);

    }

    @Test
    public void convertPage_valid() {

        MongoDbPage results;
        PageUser pageUser = new PageUserImpl(USER1ID);
        Page sourcePage = new PageImpl();
        pageUser.setUserId(USER2ID);

        List<PageUser> pageMembers = Lists.newArrayList();
        pageMembers.add(pageUser);
        sourcePage.setMembers(pageMembers);

        Region region = new RegionImpl();
        region.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        RegionWidgetImpl rw = new RegionWidgetImpl();
        rw.setId("2222");
        rw.setWidgetId("3333");
        rw.setPreferences(Lists.<RegionWidgetPreference>newLinkedList());

        region.getRegionWidgets().add(rw);
        sourcePage.setRegions(Lists.<Region>newLinkedList());
        sourcePage.getRegions().add(region);

        Page parentPage = new PageImpl();
        PageLayout pagelayout = new PageLayoutImpl();
        pagelayout.setCode("asdf");

        sourcePage.setId(PAGEID);
        sourcePage.setOwnerId(USER2ID);
        sourcePage.setContextId(USER2ID);
        sourcePage.setPageLayout(pagelayout);
        sourcePage.setName("Carol");
        sourcePage.setParentPage(parentPage);
        sourcePage.setPageType(PageType.USER.toString());

        results = converter.convert(sourcePage);

        assertThat(results.getName(), is(equalTo("Carol")));
        assertThat(results.getPageType(), is("USER"));
        assertThat(results.getContextId(), is(USER2ID));
        assertThat(results.getRegions().size(), is(1));
        assertNotNull(results.getRegions().get(0).getId());
        assertNull(results.getRegions().get(0).getPage());
        assertThat(results.getPageLayout(), is(nullValue(PageLayout.class)));
        assertThat(results.getParentPage(), is(nullValue()));
        assertThat(results.getMembers().size(), is(1));
        assertThat(results.getRegions().get(0).getRegionWidgets().get(0), is(instanceOf(RegionWidgetImpl.class)));
        assertNotNull(results.getRegions().get(0).getRegionWidgets().get(0).getId());
        assertNull(results.getRegions().get(0).getRegionWidgets().get(0).getRegion());
        assertThat(results.getRegions().get(0).getRegionWidgets().get(0).getPreferences(), is(equalTo(sourcePage.getRegions().get(0).getRegionWidgets().get(0).getPreferences())));

    }//end convert page test

    @Test
    public void convertPage_Id_Null(){
        Page sourcePage = new MongoDbPage();
        RegionImpl region = new RegionImpl();
        region.setId("123");
        sourcePage.setRegions(Arrays.<Region>asList(region));
        sourcePage.setPageLayout(new PageLayoutImpl());
        sourcePage.setOwnerId(USER1ID);
        sourcePage.setMembers(new ArrayList<PageUser>());


        Page subPage = new MongoDbPage();
        List<Page> subPages = Arrays.asList(subPage);
        subPage.setPageLayout(new PageLayoutImpl());
        subPage.setOwnerId(USER1ID);
        subPage.setId("321");
        subPage.setMembers(new ArrayList<PageUser>());
        subPage.setRegions(new ArrayList<Region>());
        sourcePage.setSubPages(subPages);


        MongoDbPage converted = converter.convert(sourcePage);

        assertTrue(converted.getSubPages().get(0).getId().equals(sourcePage.getSubPages().get(0).getId()));
        assertTrue(converted.getRegions().get(0).equals(region));

    }


    //Convert user test
    @Test
    public void convertUser_valid() {

        PageUserImpl mongoUser;
        PageUser sourceUser = new PageUserImpl(USER1ID);
        sourceUser.setUserId(USER2ID);
        sourceUser.setEditor(true);
        sourceUser.setPageStatus(PageInvitationStatus.OWNER);
        sourceUser.setRenderSequence(1234L);

        mongoUser = converter.convert(sourceUser);
        assertThat(mongoUser.getId(), is(equalTo(USER1ID)));
        assertThat(mongoUser.getUserId(), is(equalTo(USER2ID)));
        assertTrue(mongoUser.isEditor());
        assertThat(mongoUser.getPageStatus(), is(PageInvitationStatus.OWNER));
        assertThat(mongoUser.getRenderSequence(), is(equalTo(1234L)));
        assertThat(mongoUser.getPage(), is(nullValue()));

    }//end convertUser_valid

    @Test
    public void convertUser_MongoInstance(){
        PageUser sourceUser = new PageUserImpl();
        sourceUser.setUserId("1234");

        PageUserImpl converted = converter.convert(sourceUser);
        assertNotNull(converted.getId());
    }

    //Convert Widget test
    @Test
    public void convertWidget_valid() {

        RegionWidgetImpl rw = new RegionWidgetImpl();
        RegionWidgetImpl rwResults;

        rw.setPreferences(Lists.<RegionWidgetPreference>newLinkedList());
        RegionWidgetPreference preference = new RegionWidgetPreferenceImpl();
        preference.setName("name");
        preference.setValue("value");
        rw.setWidgetId("1234L");
        rw.getPreferences().add(preference);
        rw.setLocked(false);
        rw.setCollapsed(false);
        rw.setHideChrome(false);
        rw.setRenderPosition("test");
        rw.setRenderOrder(1);

        rwResults = converter.convert(rw);

        assertThat(rwResults.getWidgetId(), is(equalTo("1234L")));
        assertNotNull(rwResults.getId());
        assertThat(rwResults.getPreferences().get(0), is(equalTo(preference)));
        assertThat(rwResults.isCollapsed(), is(false));
        assertThat(rwResults.isHideChrome(), is(false));
        assertThat(rwResults.isLocked(), is(false));
        assertThat(rwResults.getRenderPosition(), is(equalTo("test")));
        assertThat(rwResults.getRenderOrder(), is(1));
        assertThat(rwResults.getRegion(), is(nullValue()));

    }//end convertRegion_valid

    @Test
    public void convertWidget_NullPreferences(){
        RegionWidgetImpl rw = new RegionWidgetImpl();
        RegionWidgetImpl rwResults;

        rw.setWidgetId("1234L");
        rw.setLocked(false);
        rw.setCollapsed(false);
        rw.setHideChrome(false);
        rw.setRenderPosition("test");
        rw.setRenderOrder(1);

        rwResults = converter.convert(rw);

        assertThat(rwResults.getWidgetId(), is(equalTo("1234L")));
        assertNotNull(rwResults.getId());
        assertNotNull(rwResults.getPreferences());
        assertThat(rwResults.isCollapsed(), is(false));
        assertThat(rwResults.isHideChrome(), is(false));
        assertThat(rwResults.isLocked(), is(false));
        assertThat(rwResults.getRenderPosition(), is(equalTo("test")));
        assertThat(rwResults.getRenderOrder(), is(1));
        assertThat(rwResults.getRegion(), is(nullValue()));
    }

    @Test
    public void hydratePage_valid() {

        MongoDbPage page = new MongoDbPage();
        page.setRegions(Lists.<Region>newLinkedList());
        Region region1 = new RegionImpl();
        Region region2 = new RegionImpl();

        region1.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        region2.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        RegionWidget rw1 = new RegionWidgetImpl();
        region1.getRegionWidgets().add(rw1);

        RegionWidget rw2 = new RegionWidgetImpl();
        region2.getRegionWidgets().add(rw2);

        page.getRegions().add(region1);
        page.getRegions().add(region2);

        page.setMembers(Lists.<PageUser>newLinkedList());
        PageUserImpl member1 = new PageUserImpl();
        PageUser member2 = new PageUserImpl();
        page.getMembers().add(member1);
        page.getMembers().add(member2);

        Region subRegion = new RegionImpl();
        subRegion.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        RegionWidget subRegionWidget = new RegionWidgetImpl();
        subRegion.getRegionWidgets().add(subRegionWidget);

        page.setSubPages(Lists.<Page>newLinkedList());
        MongoDbPage subPage1 = new MongoDbPage();
        PageUserImpl subMember = new PageUserImpl();
        subPage1.setMembers(Lists.<PageUser>newLinkedList());
        subPage1.setRegions(Lists.<Region>newLinkedList());
        subPage1.getMembers().add(subMember);
        subPage1.getRegions().add(subRegion);
        Page subPage2 = new PageImpl();
        subPage2.setMembers(Lists.<PageUser>newLinkedList());
        subPage2.setRegions(Lists.<Region>newLinkedList());
        page.getSubPages().add(subPage1);
        page.getSubPages().add(subPage2);

        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode("asdf");
        page.setPageLayoutCode(pageLayout.getCode());

        expect(pageLayoutRepository.getByPageLayoutCode(page.getPageLayoutCode())).andReturn(pageLayout);
        replay(pageLayoutRepository);

        converter.hydrate(page);
        assertThat(page.getPageLayout(), is(sameInstance(pageLayout)));
        assertThat(page.getMembers().get(0), is(instanceOf(PageUserImpl.class)));
        assertThat((MongoDbPage)region1.getPage(), is(sameInstance(page)));
        assertThat((MongoDbPage)subPage1.getParentPage(), is(sameInstance(page)));
        assertThat((MongoDbPage)subPage2.getParentPage(), is(sameInstance(page)));
        assertThat(page.getRegions().get(0).getRegionWidgets().get(0), is(sameInstance(rw1)));

    }//end hydratePage_valid

    @Test
    public void hydrateWidgetRegion_valid() {

        RegionWidgetImpl rw = new RegionWidgetImpl();
        Region region = new RegionImpl();

        converter.hydrate(rw, region);
        assertThat(rw.getRegion(), is(sameInstance(region)));

    }

    @Test
    public void hydratePage_null() {
        MongoDbPage page = null;
        converter.hydrate(page);

        assertThat(true, is(true));
    }

}
