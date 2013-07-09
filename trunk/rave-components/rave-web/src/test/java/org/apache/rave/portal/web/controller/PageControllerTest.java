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

import org.apache.rave.model.Page;
import org.apache.rave.model.PageInvitationStatus;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.PageUser;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.controller.util.MockHttpUtil;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PageControllerTest {
    private UserService userService;
    private PageService pageService;
    private PageLayoutService pageLayoutService;
    private PageController pageController;
    private MockHttpServletRequest request;
    private WidgetService widgetService;

    private Model model;
    private Page defaultPage, otherPage;
    private PageUser defaultPageUser, otherPageUser;
    private List<Page> allPages;
    private List<PageLayout> allPageLayouts;

    private final String DEFAULT_PAGE_ID = "99";
    private final String OTHER_PAGE_ID = "22";
    private final String USER_ID = "1";
    private final String VALID_PAGE_LAYOUT_CODE = "layout98";
    private UserImpl validUser;
    private PageLayout validPageLayout;

    @Before
    public void setup() {

        userService = createMock(UserService.class);
        pageService = createMock(PageService.class);
        pageLayoutService = createMock(PageLayoutService.class);
        pageController = new PageController(pageService, userService, pageLayoutService);
        model = new ExtendedModelMap();
        request = new MockHttpServletRequest();

        validUser = new UserImpl(USER_ID);
        validPageLayout = new PageLayoutImpl();
        validPageLayout.setCode(VALID_PAGE_LAYOUT_CODE);
        defaultPageUser = new PageUserImpl(USER_ID, defaultPage, 1L);
        defaultPageUser.setPageStatus(PageInvitationStatus.OWNER);

        validUser.setDefaultPageLayout(validPageLayout);

        defaultPage = new PageImpl(DEFAULT_PAGE_ID, USER_ID);
        defaultPage.setPageLayout(validPageLayout);

        List<PageUser> members = new ArrayList<PageUser>();
        members.add(defaultPageUser);
        defaultPage.setMembers(members);

        otherPage = new PageImpl(OTHER_PAGE_ID, USER_ID);
        otherPage.setPageLayout(validPageLayout);
        otherPageUser = new PageUserImpl(USER_ID, otherPage, 2L);
        otherPageUser.setPageStatus(PageInvitationStatus.OWNER);

        List<PageUser> members2 = new ArrayList<PageUser>();
        members2.add(otherPageUser);
        otherPage.setMembers(members2);

        allPages = new ArrayList<Page>();
        allPages.add(defaultPage);
        allPages.add(otherPage);

        allPageLayouts = new ArrayList<PageLayout>();
        allPageLayouts.add(validPageLayout);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void view_pageId() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);

        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes();
        expect(pageService.getAllUserPages(USER_ID)).andReturn(allPages);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, allPages)).andReturn(otherPage);
        expect(pageLayoutService.getAllUserSelectable()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.view(OTHER_PAGE_ID, model, request);

        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(otherPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), equalTo(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));

        verify(userService, pageService, pageLayoutService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void view_pageId_mobileClient() {
        MockHttpUtil.setupRequestAsMobileUserAgent(request);

        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes();
        expect(pageService.getAllUserPages(USER_ID)).andReturn(allPages);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, allPages)).andReturn(otherPage);
        expect(pageLayoutService.getAllUserSelectable()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.view(OTHER_PAGE_ID, model, request);

        assertThat(results, equalTo(ViewNames.MOBILE_HOME));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(otherPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), equalTo(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));

        verify(userService, pageService, pageLayoutService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void view_pageId_zeroExistingPages() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);
        List<Page> pages = new ArrayList<Page>();

        assertThat(pages.isEmpty(), is(true));
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes();
        expect(pageService.getAllUserPages(USER_ID)).andReturn(pages).times(2);
        expect(pageService.addNewDefaultUserPage(validUser.getId())).andReturn(defaultPage);
        expect(pageService.getPageFromList(OTHER_PAGE_ID, pages)).andReturn(defaultPage);
        expect(pageLayoutService.getAllUserSelectable()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.view(OTHER_PAGE_ID, model, request);

        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(pages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));

        verify(userService, pageService, pageLayoutService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void viewDefault_pageId() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);

        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes();
        expect(pageService.getAllUserPages(USER_ID)).andReturn(allPages);
        expect(pageService.getDefaultPageFromList(allPages)).andReturn(defaultPage);
        expect(pageLayoutService.getAllUserSelectable()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.viewDefault(model, request);

        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), equalTo(allPages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));

        verify(userService, pageService, pageLayoutService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void viewDefault_pageId_zeroExistingPages() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);
        List<Page> pages = new ArrayList<Page>();

        assertThat(pages.isEmpty(), is(true));
        expect(userService.getAuthenticatedUser()).andReturn(validUser).anyTimes();
        expect(pageService.getAllUserPages(USER_ID)).andReturn(pages).times(2);
        expect(pageService.addNewDefaultUserPage(validUser.getId())).andReturn(defaultPage);
        expect(pageService.getDefaultPageFromList(pages)).andReturn(defaultPage);
        expect(pageLayoutService.getAllUserSelectable()).andReturn(allPageLayouts);
        replay(userService, pageService, pageLayoutService);

        String results = pageController.viewDefault(model, request);

        assertThat(results, equalTo(ViewNames.getPageView(VALID_PAGE_LAYOUT_CODE)));
        assertThat((Page) model.asMap().get(ModelKeys.PAGE), sameInstance(defaultPage));
        assertThat((List<Page>) model.asMap().get(ModelKeys.PAGES), sameInstance(pages));
        assertThat((List<PageLayout>) model.asMap().get(ModelKeys.PAGE_LAYOUTS), sameInstance(allPageLayouts));

        verify(userService, pageService, pageLayoutService);
    }
}
