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

package org.apache.rave.portal.service.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.Page;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageType;
import org.apache.rave.model.PageUser;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultPageServiceTest {
    private PageService pageService;

    private PageRepository pageRepository;
    private PageTemplateRepository pageTemplateRepository;
    private RegionRepository regionRepository;
    private WidgetRepository widgetRepository;
    private RegionWidgetRepository regionWidgetRepository;
    private PageLayoutRepository pageLayoutRepository;
    private UserService userService;

    private final String REGION_WIDGET_ID = "5";
    private final String TO_REGION_ID = "1";
    private final String FROM_REGION_ID = "2";
    private final String PAGE_LAYOUT_CODE = "layout1";
    private final String PAGE_ID = "1";
    private final String PAGE_ID2 = "99";
    private final String INVALID_PAGE_ID = "-1";
    private final String VALID_REGION_WIDGET_ID = "1";
    private final String INVALID_REGION_WIDGET_ID = "100";
    private final String USER_PAGE_TYPE_ID = "1";
    private final String VALID_USER_ID = "9876";

    private Region targetRegion, originalRegion, lockedRegion;
    private Widget validWidget;
    private RegionWidget validRegionWidget;
    private UserImpl user;
    private PageLayout pageLayout;
    private String defaultPageName = "Main";
    private Page page, page2;
    private PageUser pageUser, pageUser2;
    private List<Page> pageList;
    private List<PageUser> pageUserList;

    @Before
    public void setup() {

        pageLayout = new PageLayoutImpl();
        pageLayout.setCode(PAGE_LAYOUT_CODE);
        pageLayout.setNumberOfRegions(3L);

        user = new UserImpl();
        user.setUsername("acarlucci");
        user.setId("1");
        user.setDefaultPageLayout(pageLayout);

        pageRepository = createMock(PageRepository.class);
        pageTemplateRepository = createMock(PageTemplateRepository.class);
        regionRepository = createMock(RegionRepository.class);
        widgetRepository = createMock(WidgetRepository.class);
        regionWidgetRepository = createMock(RegionWidgetRepository.class);
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        userService = createMock(UserService.class);

        pageService = new DefaultPageService(pageRepository, pageTemplateRepository, regionRepository, widgetRepository, regionWidgetRepository,
                                             pageLayoutRepository, userService, defaultPageName);

        validWidget = new WidgetImpl("1", "http://dummy.apache.org/widgets/widget.xml");

        page = new PageImpl(PAGE_ID, user.getId());
        pageUser = new PageUserImpl(user.getId(), page, 1L);
        page.setMembers(new ArrayList<PageUser>());
        page.getMembers().add(pageUser);

        page2 = new PageImpl(PAGE_ID2, user.getId());
        pageUser2 = new PageUserImpl(user.getId(), page2, 2L);
        page2.setMembers(new ArrayList<PageUser>());
        page2.getMembers().add(pageUser2);

        targetRegion = new RegionImpl("2");
        targetRegion.setLocked(false);
        targetRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        targetRegion.getRegionWidgets().add(new RegionWidgetImpl("1", validWidget.getId(), targetRegion, 0));
        targetRegion.getRegionWidgets().add(new RegionWidgetImpl("2", validWidget.getId(), targetRegion, 1));
        targetRegion.getRegionWidgets().add(new RegionWidgetImpl("3", validWidget.getId(), targetRegion, 2));
        targetRegion.setPage(page);

        originalRegion = new RegionImpl("2");
        originalRegion.setLocked(false);
        originalRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        originalRegion.getRegionWidgets().add(new RegionWidgetImpl("4", validWidget.getId(), targetRegion, 0));
        originalRegion.getRegionWidgets().add(new RegionWidgetImpl("5", validWidget.getId(), targetRegion, 1));
        originalRegion.getRegionWidgets().add(new RegionWidgetImpl("6", validWidget.getId(), targetRegion, 2));

        lockedRegion = new RegionImpl();
        lockedRegion.setLocked(true);
        lockedRegion.setPage(page);

        pageList = new ArrayList<Page>();
        pageList.add(page2);
        pageList.add(page);

        pageUserList = new ArrayList<PageUser>();
        pageUserList.add(pageUser);
        pageUserList.add(pageUser2);

        validRegionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        validRegionWidget.setWidgetId(validWidget.getId());
        validRegionWidget.setRegion(originalRegion);
    }

    @Test
    public void getAllPages() {
        final List<Page> VALID_PAGES = new ArrayList<Page>();
        expect(pageRepository.getAll()).andReturn(VALID_PAGES);
        expect(pageRepository.getCountAll()).andReturn(0);
        replay(pageRepository);

        SearchResult<Page> result = pageService.getAll();

        assertThat(result.getResultSet(), sameInstance(VALID_PAGES));
        assertEquals(result.getTotalResults(), 0);

        verify(pageRepository);
    }

    @Test
    public void getLimitedPages() {
        final List<Page> VALID_PAGES = new ArrayList<Page>();
        expect(pageRepository.getLimitedList(1, 5)).andReturn(VALID_PAGES);
        expect(pageRepository.getCountAll()).andReturn(20);
        replay(pageRepository);

        SearchResult<Page> result = pageService.getLimited(1, 5);

        assertThat(result.getResultSet(), sameInstance(VALID_PAGES));
        assertEquals(result.getTotalResults(), 20);
        assertEquals(result.getOffset(), 1);
        assertEquals(result.getPageSize(), 5);
        assertEquals(result.getCurrentPage(), 1);
        assertEquals(result.getNumberOfPages(), 4);

        verify(pageRepository);
    }

    @Test
    public void getAllUserPages() {
        final List<Page> VALID_PAGES = new ArrayList<Page>();

        expect(pageRepository.getAllPagesForUserType(VALID_USER_ID, PageType.USER.toString())).andReturn(VALID_PAGES);
        replay(pageRepository);

        assertThat(pageService.getAllUserPages(VALID_USER_ID), sameInstance(VALID_PAGES));

        verify(pageRepository);
    }


    @Test
    public void getAllPersonProfilePages_userHasPersonPage() {
        List<Page> VALID_PAGES = new ArrayList<Page>();
        Page personPage = new PageImpl();
        VALID_PAGES.add(personPage);

        expect(pageRepository.getAllPagesForUserType(VALID_USER_ID, PageType.PERSON_PROFILE.toString())).andReturn(VALID_PAGES);
        replay(pageRepository,userService,pageTemplateRepository);

        assertThat(pageService.getPersonProfilePage(VALID_USER_ID), sameInstance(personPage));

        verify(pageRepository,userService,pageTemplateRepository);
    }

    @Test
    public void getAllPersonProfilePages_noPersonPage() {
        List<Page> VALID_PAGES = new ArrayList<Page>();
        Page personPage = new PageImpl();
        PageTemplate pageTemplate = new PageTemplateImpl();
        UserImpl user = new UserImpl();

        expect(pageRepository.getAllPagesForUserType(VALID_USER_ID, PageType.PERSON_PROFILE.toString())).andReturn(VALID_PAGES);
        expect(userService.getUserById(isA(String.class))).andReturn(user).once();
        expect(pageTemplateRepository.getDefaultPage(PageType.PERSON_PROFILE.toString())).andReturn(pageTemplate).once();
        expect(pageRepository.createPageForUser(user, pageTemplate)).andReturn(personPage);
        replay(pageRepository, userService, pageTemplateRepository);

        assertThat(pageService.getPersonProfilePage(VALID_USER_ID), sameInstance(personPage));

        verify(pageRepository, userService, pageTemplateRepository);
    }

    @Test
    public void addNewUserPage_noExistingPages() {
        final String PAGE_NAME = "my new page";
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
        PageTemplate pageTemplate = new PageTemplateImpl() ;
        Page expectedPage = new PageImpl();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        expectedPage.setPageType(PageType.USER.toString());
        PageUser lPageUser = new PageUserImpl(user.getId(), expectedPage, EXPECTED_RENDER_SEQUENCE);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(lPageUser);
        expectedPage.setMembers(members);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTemplateRepository.getDefaultPage(PageType.USER.toString())).andReturn(pageTemplate);
        expect(pageRepository.createPageForUser(user, pageTemplate)).andReturn(expectedPage);
        expect(pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString())).andReturn(new ArrayList<Page>());

        replay(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);

        Page newPage = pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(PageType.USER.toString()));

        verify(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);
    }

    @Test
    public void addNewUserPage_noExistingPages_and_have_template() {
        final String PAGE_NAME = "my new page";
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
        PageTemplate pageTemplate = new PageTemplateImpl();
        Page userPage = new PageImpl();
        userPage.setName("Page Template");
        userPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        Page expectedPage = new PageImpl();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        expectedPage.setPageType(PageType.USER.toString());
        PageUser lPageUser = new PageUserImpl(user.getId(), expectedPage, EXPECTED_RENDER_SEQUENCE);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(lPageUser);
        userPage.setMembers(members);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString())).andReturn(new ArrayList<Page>());
        expect(pageRepository.createPageForUser(user, pageTemplate)).andReturn(userPage);
        expect(pageTemplateRepository.getDefaultPage(PageType.USER.toString())).andReturn(pageTemplate);
        replay(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);

        Page newPage = pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE);

        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is("Page Template"));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));

        verify(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);
    }


    @Test
    public void addNewUserPage_existingPages() {
        final String PAGE_NAME = "my new page";
        final Long EXPECTED_RENDER_SEQUENCE = 2L;
        List<Page> existingPages = new ArrayList<Page>();
        existingPages.add(new PageImpl());

        Page expectedPage = new PageImpl();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        PageUser lPageUser = new PageUserImpl(user.getId(), expectedPage, EXPECTED_RENDER_SEQUENCE);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(lPageUser);
        expectedPage.setMembers(members);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageRepository.save(expectedPage)).andAnswer(new IAnswer<Page>() {
            @Override
            public Page answer() throws Throwable {
                return (Page)EasyMock.getCurrentArguments()[0];
            }
        });
        expect(pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString())).andReturn(existingPages);
        replay(userService, pageLayoutRepository, pageRepository);

        Page newPage = pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(PageType.USER.toString()));

        for (Region region : newPage.getRegions()) {
            assertThat(region.isLocked(), is(false));
        }

        verify(userService, pageLayoutRepository, pageRepository);
    }

    @Test
    public void addNewSubPage_noExistingPages() {
        final String PAGE_NAME = "my new page";
        final String PARENT_PAGE_NAME = "my parent page";
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
        final Long EXPECTED_PARENT_RENDER_SEQUENCE = 1L;

        Page expectedPage = new PageImpl();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        Page parentPage = new PageImpl();
        parentPage.setName(PARENT_PAGE_NAME);
        parentPage.setOwnerId(user.getId());
        parentPage.setPageLayout(pageLayout);

        parentPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        replay(userService, pageLayoutRepository,  pageRepository);

        Page newPage = pageService.addNewSubPage(PAGE_NAME, PAGE_LAYOUT_CODE, parentPage);
        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(PageType.SUB_PAGE.toString()));
        assertThat(newPage.getParentPage(), is(parentPage));
        assertTrue(parentPage.getSubPages().contains(newPage));

        verify(userService, pageLayoutRepository,  pageRepository);
    }

    @Test
    public void addNewSubPage_existingPages() {
        final String PAGE_NAME = "my new page";
        final String PARENT_PAGE_NAME = "my parent page";
        final Long EXPECTED_RENDER_SEQUENCE = 2L;
        List<Page> existingPages = new ArrayList<Page>();
        existingPages.add(new PageImpl());

        Page expectedPage = new PageImpl();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        Page parentPage = new PageImpl();
        parentPage.setName(PARENT_PAGE_NAME);
        parentPage.setOwnerId(user.getId());
        parentPage.setPageLayout(pageLayout);
        parentPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        parentPage.setSubPages(existingPages);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        replay(userService, pageLayoutRepository,  pageRepository);

        Page newPage = pageService.addNewSubPage(PAGE_NAME, PAGE_LAYOUT_CODE, parentPage);
        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(PageType.SUB_PAGE.toString()));
        assertThat(newPage.getParentPage(), is(parentPage));
        assertTrue(parentPage.getSubPages().contains(newPage));

        verify(userService, pageLayoutRepository,  pageRepository);
    }

    @Test
    public void addNewDefaultUserPage() {

        final Long EXPECTED_RENDER_SEQUENCE = 1L;
        PageTemplate pageTemplate = new PageTemplateImpl();
        Page expectedPage = new PageImpl();
        expectedPage.setName(defaultPageName);
        expectedPage.setOwnerId(user.getId());
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        PageUser lPageUser = new PageUserImpl(user.getId(), expectedPage, EXPECTED_RENDER_SEQUENCE);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(lPageUser);
        expectedPage.setMembers(members);

        expect(userService.getUserById(user.getId())).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTemplateRepository.getDefaultPage(PageType.USER.toString())).andReturn(pageTemplate);
        expect(pageRepository.createPageForUser(user, pageTemplate)).andReturn(expectedPage);
        expect(pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString())).andReturn(new ArrayList<Page>());
        replay(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);

        Page newPage = pageService.addNewDefaultUserPage(user.getId());
        assertThat(newPage.getMembers().get(0).getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(defaultPageName));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));

        verify(userService, pageLayoutRepository, pageRepository, pageTemplateRepository);
    }

    @Test
    public void getDefaultPageName() {
        assertThat(pageService.getDefaultPageName(), is(defaultPageName));
    }

    @Test
    public void deletePage() {
        List<PageUser> pageUserListAfterDelete = new ArrayList<PageUser>(pageUserList);
        pageUserListAfterDelete.remove(pageUser);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(PAGE_ID)).andReturn(page);
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserListAfterDelete);
        expect(pageRepository.save(page2)).andReturn(page2);

        pageRepository.delete(page);
        expectLastCall();
        replay(userService);
        replay(pageRepository);
        pageService.deletePage(PAGE_ID);
        verify(userService);
        verify(pageRepository);
    }

    @Test
    public void deletePage_invalidId() {
        final String INVALID_PAGE_ID = "-999";
        List<PageUser> pageUserListAfterDelete = new ArrayList<PageUser>(pageUserList);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(INVALID_PAGE_ID)).andReturn(page);
        pageRepository.delete(page);
        expectLastCall();
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserListAfterDelete);
        expect(pageRepository.save(page2)).andReturn(page2);
        expect(pageRepository.save(page)).andReturn(page);
        replay(userService);
        replay(pageRepository);

        pageService.deletePage(INVALID_PAGE_ID);
        verify(userService);
        verify(pageRepository);
    }

    @Test
    public void deletePages() {
        final int EXPECTED_DELETED_PAGE_COUNT = 7;
        expect(pageRepository.deletePages(VALID_USER_ID, PageType.USER.toString())).andReturn(EXPECTED_DELETED_PAGE_COUNT);
        replay(pageRepository);
        assertThat(pageService.deletePages(VALID_USER_ID, PageType.USER.toString()), is(EXPECTED_DELETED_PAGE_COUNT));
        verify(pageRepository);
    }

    @Test
    public void moveRegionWidget_validMiddle() {
        final int newPosition = 0;
        createMoveBetweenRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validStart() {
        final int newPosition = 1;
        createMoveBetweenRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validEnd() {
        final int newPosition = 2;
        createMoveBetweenRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validMiddle_sameRegion() {
        final int newPosition = 0;
        createMoveWithinRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test
    public void moveRegionWidget_validStart_sameRegion() {
        final int newPosition = 1;
        createMoveWithinRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test
    public void moveRegionWidget_validEnd_sameRegion() {
        final int newPosition = 2;
        createMoveWithinRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository, regionWidgetRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidget_lockedSourceRegion() {
        originalRegion.setLocked(true);

        final int newPosition = 0;
        createMoveWithinRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidget_lockedTargetRegion() {
        targetRegion.setLocked(true);

        final int newPosition = 1;
        createMoveBetweenRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidget_lockedRegionWidget() {
        validRegionWidget.setLocked(true);

        final int newPosition = 1;
        createMoveBetweenRegionsExpectations();

        expect(regionWidgetRepository.get(REGION_WIDGET_ID)).andReturn(validRegionWidget);
        replay(regionWidgetRepository);

        pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget() {
        createMoveBetweenRegionsExpectations();
        expect(regionWidgetRepository.get("-1")).andReturn(null);
        replay(regionWidgetRepository);
        pageService.moveRegionWidget("-1", 0, "1", "2");
        verify(regionWidgetRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget_sameRegion() {
        createMoveBetweenRegionsExpectations();
        expect(regionWidgetRepository.get("-1")).andReturn(null);
        replay(regionWidgetRepository);
        pageService.moveRegionWidget("-1", 0, "1", "2");
        verify(regionWidgetRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget() {
        pageService.moveRegionWidget("-1", 0, "5", "6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget_sameRegion() {
        pageService.moveRegionWidget("-1", 0, "5", "5");
    }

    @Test
    public void addWigetToPage_valid() {
        final String WIDGET_ID = "1";

        Page value = new PageImpl();
        value.setRegions(new ArrayList<Region>());
        value.getRegions().add(originalRegion);
        value.getRegions().add(targetRegion);
        Widget widget = new WidgetImpl();

        expect(pageRepository.get(PAGE_ID)).andReturn(value);
        expect(widgetRepository.get(WIDGET_ID)).andReturn(widget);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        RegionWidget instance = pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);

        verify(pageRepository);
        verify(regionRepository);
        verify(widgetRepository);

        verifyPositions(0, instance, true);
        assertThat(originalRegion.getRegionWidgets().get(0), is(sameInstance(instance)));
        assertThat(instance.getWidgetId(), is(equalTo(widget.getId())));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWigetToPage_lockedRegion() {
        originalRegion.setLocked(true);
        final String WIDGET_ID = "1";

        Page value = new PageImpl();
        value.setRegions(new ArrayList<Region>());
        value.getRegions().add(originalRegion);
        value.getRegions().add(targetRegion);
        Widget widget = new WidgetImpl();

        expect(pageRepository.get(PAGE_ID)).andReturn(value);
        expect(widgetRepository.get(WIDGET_ID)).andReturn(widget);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        RegionWidget instance = pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);

        verify(pageRepository);
        verify(regionRepository);
        verify(widgetRepository);

        verifyPositions(0, instance, true);
        assertThat(originalRegion.getRegionWidgets().get(0), is(sameInstance(instance)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void addWigetToPage_nullRegion() {
        originalRegion = null;
        final String WIDGET_ID = "1";

        Page value = new PageImpl();
        value.setRegions(new ArrayList<Region>());
        value.getRegions().add(originalRegion);
        value.getRegions().add(targetRegion);
        Widget widget = new WidgetImpl();

        expect(pageRepository.get(PAGE_ID)).andReturn(value);
        expect(widgetRepository.get(WIDGET_ID)).andReturn(widget);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(pageRepository,regionRepository,widgetRepository);

        RegionWidget instance = pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);

        verify(pageRepository,regionRepository,widgetRepository);

        verifyPositions(0, instance, true);
        assertThat(originalRegion.getRegionWidgets().get(0), is(sameInstance(instance)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWidgetToPage_invalidWidget() {
        final String WIDGET_ID = "1";
        expect(pageRepository.get(PAGE_ID)).andReturn(new PageImpl());
        expect(widgetRepository.get(WIDGET_ID)).andReturn(null);
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWidgetToPage_invalidPage() {
        String WIDGET_ID = "-1";
        expect(pageRepository.get(PAGE_ID)).andReturn(null);
        expect(widgetRepository.get(WIDGET_ID)).andReturn(new WidgetImpl());
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);
    }

    @Test
    public void removeWidgetFromPage_validWidget() {
        String WIDGET_ID = "1";
        String REGION_ID = "2";
        RegionWidget regionWidget = new RegionWidgetImpl(WIDGET_ID);
        regionWidget.setRegion(new RegionImpl(REGION_ID));
        Region region = new RegionImpl();

        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget);
        regionWidgetRepository.delete(regionWidget);
        expectLastCall();
        replay(regionWidgetRepository);
        expect(regionRepository.get(REGION_ID)).andReturn(region);
        replay(regionRepository);

        Region result = pageService.removeWidgetFromPage(WIDGET_ID);
        verify(regionWidgetRepository);
        verify(regionRepository);
        assertThat(result, is(sameInstance(region)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeWidgetFromPage_lockedRegion() {
        String WIDGET_ID = "1";
        String REGION_ID = "2";
        Region region = new RegionImpl(REGION_ID);
        region.setLocked(true);
        RegionWidget regionWidget = new RegionWidgetImpl(WIDGET_ID);
        regionWidget.setRegion(region);

        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget);
        regionWidgetRepository.delete(regionWidget);
        expectLastCall();
        replay(regionWidgetRepository);
        expect(regionRepository.get(REGION_ID)).andReturn(region);
        replay(regionRepository);

        pageService.removeWidgetFromPage(WIDGET_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeWidgetFromPage_lockedRegionWidget() {
        String WIDGET_ID = "1";
        String REGION_ID = "2";
        Region region = new RegionImpl(REGION_ID);
        region.setLocked(true);
        RegionWidget regionWidget = new RegionWidgetImpl(WIDGET_ID);
        regionWidget.setLocked(true);
        regionWidget.setRegion(region);

        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget);
        regionWidgetRepository.delete(regionWidget);
        expectLastCall();
        replay(regionWidgetRepository);
        expect(regionRepository.get(REGION_ID)).andReturn(region);
        replay(regionRepository);

        pageService.removeWidgetFromPage(WIDGET_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeWidgetFromPage_invalidWidget() {
        String WIDGET_ID = "-1";
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(null);
        replay(regionWidgetRepository);
        replay(regionRepository);

        pageService.removeWidgetFromPage(WIDGET_ID);
    }

    @Test
    public void getPage() {
        expect(pageRepository.get(PAGE_ID)).andReturn(page);
        replay(pageRepository);

        assertThat(pageService.getPage(PAGE_ID), is(page));

        verify(pageRepository);
    }

    @Test
    public void getPageFromList() {
        assertThat(pageService.getPageFromList(PAGE_ID, pageList), is(page));
    }

    @Test
    public void getPageFromList_invalidId() {
        assertThat(pageService.getPageFromList(INVALID_PAGE_ID, pageList), is(nullValue(Page.class)));
    }

    @Test
    public void getDefaultPageFromList_validList() {
        assertThat(pageService.getDefaultPageFromList(pageList), is(page2));
    }

    @Test
    public void getDefaultPageFromList_emptyList() {
        assertThat(pageService.getDefaultPageFromList(new ArrayList<Page>()), is(nullValue(Page.class)));
    }

    @Test
    public void getDefaultPageFromList_nullList() {
        assertThat(pageService.getDefaultPageFromList(null), is(nullValue(Page.class)));
    }

    @Test
    public void movePage() {
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.getSingleRecord(user.getId(), PAGE_ID)).andReturn(pageUser);
        expect(pageRepository.getSingleRecord(user.getId(), page2.getId())).andReturn(pageUser2);
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserList);

        expect(pageRepository.save(page)).andReturn(page);
        expect(pageRepository.save(page2)).andReturn(page);
        replay(userService);
        replay(pageRepository);

        Page p = pageService.movePage(PAGE_ID, page2.getId());

        assertThat(pageUser.getRenderSequence(), is(2L));
        assertThat(pageUser2.getRenderSequence(), is(1L));

        verify(userService);
        verify(pageRepository);
    }

    @Test(expected=RuntimeException.class)
    public void movePage_invalidPageId() {
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.getSingleRecord(user.getId(), INVALID_PAGE_ID)).andReturn(null);
        expect(pageRepository.getSingleRecord(user.getId(), page2.getId())).andReturn(pageUser2);
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserList);

        expect(pageRepository.get(INVALID_PAGE_ID)).andReturn(null);
        expect(pageRepository.get(page2.getId())).andReturn(page2);
        expect(pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString())).andReturn(pageList);
        replay(userService);
        replay(pageRepository);

        pageService.movePage(INVALID_PAGE_ID, page2.getId());

        verify(userService);
        verify(pageRepository);
    }

    @Test
    public void movePageToDefault() {
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.getSingleRecord(user.getId(), page2.getId())).andReturn(pageUser2);
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserList);
        expect(pageRepository.save(page)).andReturn(page);
        expect(pageRepository.save(page2)).andReturn(page);
        replay(userService);
        replay(pageRepository);

        pageService.movePageToDefault(page2.getId());
        assertThat(pageUser2.getRenderSequence(), is(1L));
        assertThat(pageUser.getRenderSequence(), is(2L));

        verify(userService);
        verify(pageRepository);
    }

    @Test(expected=RuntimeException.class)
    public void movePageToDefault_invalidPageId() {
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.getSingleRecord(user.getId(), INVALID_PAGE_ID)).andReturn(null);
        expect(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString())).andReturn(pageUserList);
        replay(userService);
        replay(pageRepository);

        pageService.movePageToDefault(INVALID_PAGE_ID);

        verify(userService);
        verify(pageRepository);
    }

    @Test
    public void updatePage_nameUpdate() {
        String newName = "new page name";
        String layoutName = "layout name";

        PageLayoutImpl layout = createStrictMock(PageLayoutImpl.class);
        expect(layout.getNumberOfRegions()).andReturn(Long.valueOf(2L)).anyTimes();
        replay(layout);

        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(PageImpl.class);
        expect(curPage.getPageLayout()).andReturn(layout);
        curPage.setName(newName);
        curPage.setPageLayout(layout);
        replay(curPage);

        expect(pageRepository.get(PAGE_ID)).andReturn(curPage);
        expect(pageRepository.save(curPage)).andReturn(curPage);
        replay(pageRepository);

        expect(pageLayoutRepository.getByPageLayoutCode(layoutName)).andReturn(layout);
        replay(pageLayoutRepository);

        pageService.updatePage(PAGE_ID, newName, layoutName);

        verify(curPage);
    }

    @Test
    public void updatePage_addRegion() {
        String newName = "new page name";
        String layoutName = "layout name";
        String layoutCode = "CODE";

        List<Region> regions = new ArrayList<Region>();
        Region region = new RegionImpl("99");
        region.setRenderOrder(1);
        regions.add(new RegionImpl());
        regions.add(region);

        PageLayout prevLayout = new PageLayoutImpl();
        prevLayout.setNumberOfRegions(2L);
        prevLayout.setCode("NEW");

        PageLayoutImpl layout = new PageLayoutImpl();
        layout.setNumberOfRegions(3L);
        layout.setCode(layoutCode);


        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
        expect(curPage.getPageLayout()).andReturn(prevLayout);
        expect(curPage.getRegions()).andReturn(regions);
        expect(curPage.getId()).andReturn(PAGE_ID).anyTimes();
        /*expect(curPage.getMembers()).andReturn(new ArrayList<PageUser>());
        expect(curPage.getName()).andReturn(newName);
        expect(curPage.getOwner()).andReturn(user);
        expect(curPage.getPageLayout()).andReturn(layout);
        expect(curPage.getPageType()).andReturn(PageType.USER.toString());
        expect(curPage.getParentPage()).andReturn(null);
        expect(curPage.getRegions()).andReturn(regions);
        expect(curPage.getSubPages()).andReturn(new ArrayList<Page>()); */
        curPage.setName(newName);
        expectLastCall();
        curPage.setPageLayout(layout);
        expectLastCall();
        replay(curPage);

        expect(pageRepository.get(PAGE_ID)).andReturn(curPage);
        expect(pageRepository.save(curPage)).andReturn(curPage);
        replay(pageRepository);

        expect(pageLayoutRepository.getByPageLayoutCode(layoutName)).andReturn(layout);
        replay(pageLayoutRepository);

        pageService.updatePage(PAGE_ID, newName, layoutName);
        assertThat(regions.size(), is(3));
        //assertThat(regions.get(regions.size()-1).getPage().getId(), is(PAGE_ID));
        verify(curPage);
    }


    @Test
    public void updatePage_removeRegion_noWidgets() {
        String newName = "new page name";
        String layoutName = "layout name";

        List<Region> regions = new ArrayList<Region>();
        Region region = createStrictMock(Region.class);
        expect(region.getRegionWidgets()).andReturn(new ArrayList<RegionWidget>());
        expect(region.getRenderOrder()).andReturn(1);
        replay(region);
        regions.add(region);

        Region deletedRegion = createStrictMock(Region.class);
        expect(deletedRegion.getRegionWidgets()).andReturn(new ArrayList<RegionWidget>());
        replay(deletedRegion);
        regions.add(deletedRegion);

        PageLayout prevLayout = createStrictMock(PageLayoutImpl.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);

        PageLayoutImpl layout = createStrictMock(PageLayoutImpl.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);

        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);

        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(PageImpl.class);
        expect(curPage.getPageLayout()).andReturn(prevLayout);
        expect(curPage.getRegions()).andReturn(regions);
        curPage.setName(newName);
        curPage.setPageLayout(layout);
        replay(curPage);

        expect(pageRepository.get(PAGE_ID)).andReturn(curPage);
        expect(pageRepository.save(curPage)).andReturn(curPage);
        replay(pageRepository);

        expect(pageLayoutRepository.getByPageLayoutCode(layoutName)).andReturn(layout);
        replay(pageLayoutRepository);

        pageService.updatePage(PAGE_ID, newName, layoutName);

        verify(curPage);
    }


    @Test
    public void updatePage_removeRegion_moveWidgetToEmptyColumn() {
        String newName = "new page name";
        String layoutName = "layout name";


        List<RegionWidget> newLastWidgetColumn = new ArrayList<RegionWidget>();

        List<Region> regions = new ArrayList<Region>();
        Region region = createStrictMock(Region.class);
        expect(region.getRegionWidgets()).andReturn(newLastWidgetColumn).times(2);
        expect(region.getRenderOrder()).andReturn(1);
        replay(region);
        regions.add(region);


        RegionWidget widget = createStrictMock(RegionWidget.class);
        widget.setRegion(region);
        widget.setRenderOrder(1);
        replay(widget);
        List<RegionWidget> movedWidgets = new ArrayList<RegionWidget>();
        movedWidgets.add(widget);

        Region deletedRegion = createStrictMock(Region.class);
        expect(deletedRegion.getRegionWidgets()).andReturn(movedWidgets);
        replay(deletedRegion);
        regions.add(deletedRegion);

        PageLayout prevLayout = createStrictMock(PageLayoutImpl.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);

        PageLayoutImpl layout = createStrictMock(PageLayoutImpl.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);

        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);

        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(PageImpl.class);
        expect(curPage.getPageLayout()).andReturn(prevLayout);
        expect(curPage.getRegions()).andReturn(regions);
        curPage.setName(newName);
        curPage.setPageLayout(layout);
        replay(curPage);

        expect(pageRepository.get(PAGE_ID)).andReturn(curPage);
        expect(pageRepository.save(curPage)).andReturn(curPage);
        replay(pageRepository);

        expect(pageLayoutRepository.getByPageLayoutCode(layoutName)).andReturn(layout);
        replay(pageLayoutRepository);

        pageService.updatePage(PAGE_ID, newName, layoutName);
        assertThat(newLastWidgetColumn.size(), is (1));

        verify(curPage);
    }


    @Test
    public void updatePage_removeRegion_moveWidgetToNonEmptyColumn() {
        String newName = "new page name";
        String layoutName = "layout name";


        RegionWidget widget = createStrictMock(RegionWidget.class);
        expect(widget.getRenderOrder()).andReturn(0).anyTimes();
        replay(widget);
        List<RegionWidget> newLastWidgetColumn = new ArrayList<RegionWidget>();
        newLastWidgetColumn.add(widget);

        List<Region> regions = new ArrayList<Region>();
        Region region = createStrictMock(Region.class);
        expect(region.getRegionWidgets()).andReturn(newLastWidgetColumn).times(2);
        expect(region.getRenderOrder()).andReturn(1);
        replay(region);
        regions.add(region);


        widget = createStrictMock(RegionWidget.class);
        widget.setRegion(region);
        widget.setRenderOrder(1);
        expect(widget.getRenderOrder()).andReturn(1).anyTimes();
        replay(widget);
        List<RegionWidget> movedWidgets = new ArrayList<RegionWidget>();
        movedWidgets.add(widget);

        Region deletedRegion = createStrictMock(Region.class);
        expect(deletedRegion.getRegionWidgets()).andReturn(movedWidgets);
        replay(deletedRegion);
        regions.add(deletedRegion);

        PageLayout prevLayout = createStrictMock(PageLayoutImpl.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);

        PageLayoutImpl layout = createStrictMock(PageLayoutImpl.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);

        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);

        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(PageImpl.class);
        expect(curPage.getPageLayout()).andReturn(prevLayout);
        expect(curPage.getRegions()).andReturn(regions);
        curPage.setName(newName);
        curPage.setPageLayout(layout);
        replay(curPage);

        expect(pageRepository.get(PAGE_ID)).andReturn(curPage);
        expect(pageRepository.save(curPage)).andReturn(curPage);
        replay(pageRepository);

        expect(pageLayoutRepository.getByPageLayoutCode(layoutName)).andReturn(layout);
        replay(pageLayoutRepository);

        pageService.updatePage(PAGE_ID, newName, layoutName);
        assertThat(newLastWidgetColumn.size(), is (2));
        assertThat(newLastWidgetColumn.get(0).getRenderOrder(), is (0));
        assertThat(newLastWidgetColumn.get(1).getRenderOrder(), is (1));

        verify(curPage);
    }
    // private methods
    private void verifyPositions(int newPosition, RegionWidget widget, boolean sameRegion) {
        assertThat(widget.getRenderOrder(), is(equalTo(newPosition)));
        assertOrder(originalRegion);
        assertThat(originalRegion.getRegionWidgets().contains(widget), is(sameRegion));
        if (!sameRegion) {
            assertOrder(targetRegion);
            assertThat(targetRegion.getRegionWidgets().contains(widget), is(true));
        }
    }

    private void createMoveBetweenRegionsExpectations() {
        expect(regionRepository.get(TO_REGION_ID)).andReturn(targetRegion);
        expect(regionRepository.get(FROM_REGION_ID)).andReturn(originalRegion);
        expect(regionRepository.save(targetRegion)).andReturn(targetRegion);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(regionRepository);
    }

    private void createMoveWithinRegionsExpectations() {
        expect(regionRepository.get(FROM_REGION_ID)).andReturn(originalRegion);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        replay(regionRepository);
    }

    private void assertOrder(Region region) {
        for (int i = 0; i < region.getRegionWidgets().size(); i++) {
            assertThat(region.getRegionWidgets().get(i).getRenderOrder(), is(equalTo(i)));
        }
    }

    private List<Region> createEmptyRegionList(long numberOfRegions) {
        List<Region> regions = new ArrayList<Region>();
        int regionCount;
        for (regionCount = 0; regionCount < numberOfRegions; regionCount++) {
              Region region = new RegionImpl();
              regions.add(region);
        }
        return regions;
    }

    @Test
    public void moveRegionWidgetToPage_valid() {
        final String WIDGET_ID = "1";
        final String CURRENT_PAGE_ID = "1";
        final String TO_PAGE_ID = "2";

        Page currentPageValue = new PageImpl();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new PageImpl();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        Region region = new RegionImpl();
        region.setLocked(false);
        region.setRegionWidgets(Lists.<RegionWidget>newArrayList());

        RegionWidget regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        regionWidget.setRegion(region);
        region.getRegionWidgets().add(regionWidget);

        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget).times(2);
        expect(regionRepository.save(originalRegion)).andReturn(originalRegion);
        expect(regionRepository.save(region)).andReturn(region);

        replay(pageRepository, regionWidgetRepository, regionRepository);

        RegionWidget updatedRegionWidget = pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);

        verify(pageRepository);
        verify(regionWidgetRepository);
        verifyPositions(0, regionWidget, true);
        assertThat(region.getRegionWidgets().isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidgetToPage_invalidWidget() {
        String WIDGET_ID = "-1";
        final String CURRENT_PAGE_ID = "1";
        final String TO_PAGE_ID = "2";

        Page currentPageValue = new PageImpl();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new PageImpl();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        expect(pageRepository.get(CURRENT_PAGE_ID)).andReturn(currentPageValue);
        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(null);

        pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidgetToPage_lockedRegionWidget() {
        final String WIDGET_ID = "1";
        final String CURRENT_PAGE_ID = "1";
        final String TO_PAGE_ID = "2";

        Page currentPageValue = new PageImpl();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new PageImpl();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        Region region = new RegionImpl();
        region.setLocked(false);

        RegionWidget regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        regionWidget.setRegion(region);
        regionWidget.setLocked(true);

        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget).times(2);
        replay(pageRepository,regionWidgetRepository);

        pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidgetToPage_lockedTargetRegion() {
        targetRegion.setLocked(true);

        final String WIDGET_ID = "1";
        final String CURRENT_PAGE_ID = "1";
        final String TO_PAGE_ID = "2";

        Page currentPageValue = new PageImpl();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new PageImpl();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        Region region = new RegionImpl();
        region.setLocked(false);

        RegionWidget regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        regionWidget.setRegion(region);
        regionWidget.setLocked(true);

        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget).times(2);
        replay(pageRepository,regionWidgetRepository);

        pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void moveRegionWidgetToPage_lockedSourceRegion() {
        originalRegion.setLocked(true);

        final String WIDGET_ID = "1";
        final String CURRENT_PAGE_ID = "1";
        final String TO_PAGE_ID = "2";

        Page currentPageValue = new PageImpl();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new PageImpl();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        Region region = new RegionImpl();
        region.setLocked(false);

        RegionWidget regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        regionWidget.setRegion(region);
        regionWidget.setLocked(true);

        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget).times(2);
        replay(pageRepository,regionWidgetRepository);

        pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);
    }
}
