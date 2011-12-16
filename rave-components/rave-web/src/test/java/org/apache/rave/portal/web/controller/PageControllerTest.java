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

package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.web.controller.util.MockHttpUtil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.web.util.ViewNames;
import static org.hamcrest.CoreMatchers.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;

public class PageControllerTest {
    private UserService userService;
    private PageService pageService;
    private PageLayoutService pageLayoutService;
    private PageController pageController;
    private MockHttpServletRequest request;
    
    private Model model;
    private Page defaultPage, otherPage;
    private List<Page> allPages;
    private List<PageLayout> allPageLayouts;
    
    private final Long DEFAULT_PAGE_ID = 99L;
    private final Long OTHER_PAGE_ID = 22L;
    private final Long USER_ID = 1L;
    private final String VALID_PAGE_LAYOUT_CODE = "layout98";
    private User validUser;
    private PageLayout validPageLayout;

    @Before
    public void setup() {
        userService = createMock(UserService.class);
        pageService = createMock(PageService.class);
        pageLayoutService = createMock(PageLayoutService.class);
        pageController = new PageController(pageService, userService, pageLayoutService);
        model = new ExtendedModelMap();
        request = new MockHttpServletRequest();

        validPageLayout = new PageLayout();
        validPageLayout.setEntityId(33L);
        validPageLayout.setCode(VALID_PAGE_LAYOUT_CODE);

        defaultPage = new Page(DEFAULT_PAGE_ID);
        defaultPage.setPageLayout(validPageLayout);
        otherPage = new Page(OTHER_PAGE_ID);
        otherPage.setPageLayout(validPageLayout);
        
        allPages = new ArrayList<Page>();
        allPages.add(defaultPage);   
        allPages.add(otherPage);

        allPageLayouts = new ArrayList<PageLayout>();
        allPageLayouts.add(validPageLayout);
        
        validUser = new User(USER_ID);
        validUser.setDefaultPageLayout(validPageLayout);
    }

    @Test
    public void view_pageId() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);        
        
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes(); 
        expect(pageService.getAllPages(USER_ID)).andReturn(allPages);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, allPages)).andReturn(otherPage);
        expect(pageLayoutService.getAll()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.view(OTHER_PAGE_ID, model, request);
        
        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(otherPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));
        
        verify(userService, pageService, pageLayoutService);
    }
    
    @Test
    public void view_pageId_mobileClient() {       
        MockHttpUtil.setupRequestAsMobileUserAgent(request);
        
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes(); 
        expect(pageService.getAllPages(USER_ID)).andReturn(allPages);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, allPages)).andReturn(otherPage);
        expect(pageLayoutService.getAll()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.view(OTHER_PAGE_ID, model, request);
        
        assertThat(results, equalTo(ViewNames.MOBILE_HOME));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(otherPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));
        
        verify(userService, pageService, pageLayoutService);
    }
    
    @Test
    public void view_pageId_zeroExistingPages() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);        
        List<Page> pages = new ArrayList<Page>();
        
        assertThat(pages.isEmpty(), is(true));
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes(); 
        expect(pageService.getAllPages(USER_ID)).andReturn(pages).times(2);
        expect(pageService.addNewDefaultPage(validUser.getEntityId())).andReturn(defaultPage);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, pages)).andReturn(defaultPage);
        expect(pageLayoutService.getAll()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);
        
        String results = pageController.view(OTHER_PAGE_ID, model, request);
        
        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(pages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));
        
        verify(userService, pageService, pageLayoutService);
    }    
    
    @Test
    public void viewDefault_pageId() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);        
        
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes(); 
        expect(pageService.getAllPages(USER_ID)).andReturn(allPages);
        expect(pageService.getDefaultPageFromList(allPages)).andReturn(defaultPage);
        expect(pageLayoutService.getAll()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.viewDefault(model, request);
        
        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));
        
        verify(userService, pageService, pageLayoutService);
    }
    
    @Test
    public void viewDefault_pageId_zeroExistingPages() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);        
        List<Page> pages = new ArrayList<Page>();
        
        assertThat(pages.isEmpty(), is(true));
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes(); 
        expect(pageService.getAllPages(USER_ID)).andReturn(pages).times(2);
        expect(pageService.addNewDefaultPage(validUser.getEntityId())).andReturn(defaultPage);
        expect(pageService.getDefaultPageFromList(pages)).andReturn(defaultPage);
        expect(pageLayoutService.getAll()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.viewDefault(model, request);
        
        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(pages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));
        
        verify(userService, pageService, pageLayoutService);
    }    
}