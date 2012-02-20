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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.*;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultPageServiceTest {
    private PageService pageService;

    private PageRepository pageRepository;
    private RegionRepository regionRepository;
    private WidgetRepository widgetRepository;
    private RegionWidgetRepository regionWidgetRepository;
    private PageLayoutRepository pageLayoutRepository;
    private PageTypeRepository pageTypeRepository;
    private UserService userService;

    private final long REGION_WIDGET_ID = 5L;
    private final long TO_REGION_ID = 1L;
    private final long FROM_REGION_ID = 2L;
    private final String PAGE_LAYOUT_CODE = "layout1";
    private final long PAGE_ID = 1L;
    private final long INVALID_PAGE_ID = -1L;
    private final Long VALID_REGION_WIDGET_ID = 1L;
    private final Long INVALID_REGION_WIDGET_ID = 100L;
    private final Long USER_PAGE_TYPE_ID = 1L;
    private final Long VALID_USER_ID = 9876L;
            
    private Region targetRegion;
    private Region originalRegion;
    private Widget validWidget;
    private User user;
    private PageLayout pageLayout;
    private String defaultPageName = "Main";
    private Page page, page2;
    private List<Page> pageList;
    private PageType userPageType, personProfilePageType, subPagePageType;

    @Before
    public void setup() {
        userPageType = new PageType(USER_PAGE_TYPE_ID, "USER", "User Page Type");
        personProfilePageType = new PageType(2L, "PERSON_PROFILE", "PP Page Type");
        subPagePageType = new PageType(3L, "SUB_PAGE", "Sub Page Page Type");

        pageRepository = createMock(PageRepository.class);
        regionRepository = createMock(RegionRepository.class);
        widgetRepository = createMock(WidgetRepository.class);
        regionWidgetRepository = createMock(RegionWidgetRepository.class);
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        pageTypeRepository = createMock(PageTypeRepository.class);
        userService = createMock(UserService.class);

        // mock methods that will execute in DefaultPageService constructor
        expect(pageTypeRepository.getUserPageType()).andReturn(userPageType);
        expect(pageTypeRepository.getPersonProfilePageType()).andReturn(personProfilePageType);
        expect(pageTypeRepository.getSubPagePageType()).andReturn(subPagePageType);
        replay(pageTypeRepository);
        pageService = new DefaultPageService(pageRepository, regionRepository, widgetRepository, regionWidgetRepository, 
                                             pageLayoutRepository, pageTypeRepository, userService, defaultPageName);

        verify(pageTypeRepository);

        // reset the pageTypeRepository after testing the constructor for method specific mocks
        reset(pageTypeRepository);
        
        validWidget = new Widget(1L, "http://dummy.apache.org/widgets/widget.xml");
        
        targetRegion = new Region();
        targetRegion.setEntityId(2L);
        targetRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        targetRegion.getRegionWidgets().add(new RegionWidget(1L, validWidget, targetRegion, 0));
        targetRegion.getRegionWidgets().add(new RegionWidget(2L, validWidget, targetRegion, 1));
        targetRegion.getRegionWidgets().add(new RegionWidget(3L, validWidget, targetRegion, 2));

        originalRegion = new Region();
        originalRegion.setEntityId(1L);
        originalRegion.setRegionWidgets(new ArrayList<RegionWidget>());
        originalRegion.getRegionWidgets().add(new RegionWidget(4L, validWidget, targetRegion, 0));
        originalRegion.getRegionWidgets().add(new RegionWidget(5L, validWidget, targetRegion, 1));
        originalRegion.getRegionWidgets().add(new RegionWidget(6L, validWidget, targetRegion, 2));
        
        pageLayout = new PageLayout();
        pageLayout.setEntityId(1L);
        pageLayout.setCode(PAGE_LAYOUT_CODE);
        pageLayout.setNumberOfRegions(3L);
        
        user = new User();
        user.setEntityId(1L);
        user.setUsername("acarlucci"); 
        user.setDefaultPageLayout(pageLayout);                
        
        page = new Page(PAGE_ID, user);
        page.setRenderSequence(1L);
        page2 = new Page(99L, user);
        page2.setRenderSequence(2L);
        
        pageList = new ArrayList<Page>();        
        pageList.add(page2);
        pageList.add(page);
    }

    @Test
    public void getAllPages() {
        final List<Page> VALID_PAGES = new ArrayList<Page>();

        expect(pageRepository.getAllPages(VALID_USER_ID, userPageType.getEntityId())).andReturn(VALID_PAGES);
        replay(pageRepository);

        assertThat(pageService.getAllUserPages(VALID_USER_ID), CoreMatchers.sameInstance(VALID_PAGES));
        
        verify(pageRepository);
    }

    @Test
    public void addNewUserPage_noExistingPages() {
        final String PAGE_NAME = "my new page";
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
                      
        Page expectedPage = new Page();
        expectedPage.setName(PAGE_NAME);       
        expectedPage.setOwner(user);
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));    
        expectedPage.setPageType(userPageType);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTypeRepository.getUserPageType()).andReturn(userPageType);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(new ArrayList<Page>());
        replay(userService, pageLayoutRepository, pageTypeRepository, pageRepository);

        Page newPage = pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        assertThat(newPage.getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(userPageType));

        verify(userService, pageLayoutRepository, pageTypeRepository, pageRepository);
    }
    
    @Test
    public void addNewUserPage_existingPages() {
        final String PAGE_NAME = "my new page";
        final Long EXPECTED_RENDER_SEQUENCE = 2L;
        List<Page> existingPages = new ArrayList<Page>();
        existingPages.add(new Page());
                      
        Page expectedPage = new Page();
        expectedPage.setName(PAGE_NAME);       
        expectedPage.setOwner(user);
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));    
                
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTypeRepository.getUserPageType()).andReturn(userPageType);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(existingPages);
        replay(userService, pageLayoutRepository, pageTypeRepository, pageRepository);

        Page newPage = pageService.addNewUserPage(PAGE_NAME, PAGE_LAYOUT_CODE);
        assertThat(newPage.getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(userPageType));
        
        verify(userService, pageLayoutRepository, pageTypeRepository, pageRepository);
    }

    @Test
    public void addNewSubPage_noExistingPages() {
        final String PAGE_NAME = "my new page";
        final String PARENT_PAGE_NAME = "my parent page";
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
        final Long EXPECTED_PARENT_RENDER_SEQUENCE = 1L;

        Page expectedPage = new Page();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwner(user);
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        Page parentPage = new Page();
        parentPage.setName(PARENT_PAGE_NAME);
        parentPage.setOwner(user);
        parentPage.setPageLayout(pageLayout);
        parentPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        parentPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTypeRepository.getSubPagePageType()).andReturn(subPagePageType);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        replay(userService, pageLayoutRepository, pageTypeRepository, pageRepository);

        Page newPage = pageService.addNewSubPage(PAGE_NAME, PAGE_LAYOUT_CODE, parentPage);
        assertThat(newPage.getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(subPagePageType));
        assertThat(newPage.getParentPage(), is(parentPage));
        assertTrue(parentPage.getSubPages().contains(newPage));

        verify(userService, pageLayoutRepository, pageTypeRepository, pageRepository);
    }

    @Test
    public void addNewSubPage_existingPages() {
        final String PAGE_NAME = "my new page";
        final String PARENT_PAGE_NAME = "my parent page";
        final Long EXPECTED_RENDER_SEQUENCE = 2L;
        List<Page> existingPages = new ArrayList<Page>();
        existingPages.add(new Page());

        Page expectedPage = new Page();
        expectedPage.setName(PAGE_NAME);
        expectedPage.setOwner(user);
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));

        Page parentPage = new Page();
        parentPage.setName(PARENT_PAGE_NAME);
        parentPage.setOwner(user);
        parentPage.setPageLayout(pageLayout);
        parentPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        parentPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));
        parentPage.setSubPages(existingPages);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageTypeRepository.getSubPagePageType()).andReturn(subPagePageType);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        replay(userService, pageLayoutRepository, pageTypeRepository, pageRepository);

        Page newPage = pageService.addNewSubPage(PAGE_NAME, PAGE_LAYOUT_CODE, parentPage);
        assertThat(newPage.getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(PAGE_NAME));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        assertThat(newPage.getPageType(), is(subPagePageType));
        assertThat(newPage.getParentPage(), is(parentPage));
        assertTrue(parentPage.getSubPages().contains(newPage));

        verify(userService, pageLayoutRepository, pageTypeRepository, pageRepository);
    }
   
    @Test
    public void addNewDefaultUserPage() {
        final Long EXPECTED_RENDER_SEQUENCE = 1L;
                      
        Page expectedPage = new Page();
        expectedPage.setName(defaultPageName);       
        expectedPage.setOwner(user);
        expectedPage.setPageLayout(pageLayout);
        expectedPage.setRenderSequence(EXPECTED_RENDER_SEQUENCE);
        expectedPage.setRegions(createEmptyRegionList(pageLayout.getNumberOfRegions()));    
                
        expect(userService.getUserById(user.getEntityId())).andReturn(user);
        expect(pageLayoutRepository.getByPageLayoutCode(PAGE_LAYOUT_CODE)).andReturn(pageLayout);
        expect(pageRepository.save(expectedPage)).andReturn(expectedPage);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(new ArrayList<Page>());
        expect(pageTypeRepository.getUserPageType()).andReturn(userPageType);
        replay(userService, pageLayoutRepository, pageRepository, pageTypeRepository);

        Page newPage = pageService.addNewDefaultUserPage(user.getEntityId());
        assertThat(newPage.getRenderSequence(), is(EXPECTED_RENDER_SEQUENCE));
        assertThat(newPage.getName(), is(defaultPageName));
        assertThat(newPage.getRegions().size(), is(pageLayout.getNumberOfRegions().intValue()));
        
        verify(userService, pageLayoutRepository, pageRepository, pageTypeRepository);
    }
    
    @Test
    public void getDefaultPageName() {
        assertThat(pageService.getDefaultPageName(), is(defaultPageName));
    }
  
    @Test
    public void deletePage() {
        List<Page> pageListAfterDelete = new ArrayList<Page>(pageList);
        pageListAfterDelete.remove(page);
        
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(PAGE_ID)).andReturn(page);
        pageRepository.delete(page);
        expectLastCall();
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageListAfterDelete);
        expect(pageRepository.save(page2)).andReturn(page2);
        replay(userService);
        replay(pageRepository);
        pageService.deletePage(PAGE_ID);
        verify(userService);
        verify(pageRepository);
    }
    
    @Test
    public void deletePage_invalidId() {               
        final long INVALID_PAGE_ID = -999L;
        
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(INVALID_PAGE_ID)).andReturn(page);
        pageRepository.delete(page);
        expectLastCall();
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageList);
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
        expect(pageRepository.deletePages(VALID_USER_ID, USER_PAGE_TYPE_ID)).andReturn(EXPECTED_DELETED_PAGE_COUNT);     
        replay(pageRepository);
        assertThat(pageService.deletePages(VALID_USER_ID, USER_PAGE_TYPE_ID), is(EXPECTED_DELETED_PAGE_COUNT));
        verify(pageRepository);
    }    
    
    
    @Test
    public void moveRegionWidget_validMiddle() {
        final int newPosition = 0;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validStart() {
        final int newPosition = 1;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validEnd() {
        final int newPosition = 2;
        createMoveBetweenRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, TO_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, false);
    }

    @Test
    public void moveRegionWidget_validMiddle_sameRegion() {
        final int newPosition = 0;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test
    public void moveRegionWidget_validStart_sameRegion() {
        final int newPosition = 1;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test
    public void moveRegionWidget_validEnd_sameRegion() {
        final int newPosition = 2;
        createMoveWithinRegionsExpectations();

        RegionWidget widget = pageService.moveRegionWidget(REGION_WIDGET_ID, newPosition, FROM_REGION_ID, FROM_REGION_ID);

        verify(regionRepository);
        verifyPositions(newPosition, widget, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget() {
        createMoveBetweenRegionsExpectations();
        pageService.moveRegionWidget(-1L, 0, 1L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidWidget_sameRegion() {
        createMoveBetweenRegionsExpectations();
        pageService.moveRegionWidget(-1L, 0, 1L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget() {       
        pageService.moveRegionWidget(-1L, 0, 5L, 6L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidget_invalidTarget_sameRegion() {       
        pageService.moveRegionWidget(-1L, 0, 5L, 5L);
    }

    @Test
    public void addWigetToPage_valid() {
        final long WIDGET_ID = 1L;

        Page value = new Page();
        value.setRegions(new ArrayList<Region>());
        value.getRegions().add(originalRegion);
        value.getRegions().add(targetRegion);
        Widget widget = new Widget();

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
        assertThat(instance.getWidget(), is(sameInstance(widget)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void addWidgetToPage_invalidWidget() {
        long WIDGET_ID = -1L;
        expect(pageRepository.get(PAGE_ID)).andReturn(new Page());
        expect(widgetRepository.get(WIDGET_ID)).andReturn(null);
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWidgetToPage_invalidPage() {
        long WIDGET_ID = -1L;
        expect(pageRepository.get(PAGE_ID)).andReturn(null);
        expect(widgetRepository.get(WIDGET_ID)).andReturn(new Widget());
        replay(pageRepository);
        replay(regionRepository);
        replay(widgetRepository);

        pageService.addWidgetToPage(PAGE_ID, WIDGET_ID);
    }

    @Test
    public void removeWidgetFromPage_validWidget() {
        long WIDGET_ID = 1L;
        long REGION_ID = 2L;
        RegionWidget widget = new RegionWidget(WIDGET_ID);
        widget.setRegion(new Region(REGION_ID));
        Region region = new Region();

        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(widget);
        regionWidgetRepository.delete(widget);
        expectLastCall();
        replay(regionWidgetRepository);
        expect(regionRepository.get(REGION_ID)).andReturn(region);
        replay(regionRepository);

        Region result = pageService.removeWidgetFromPage(WIDGET_ID);
        verify(regionWidgetRepository);
        verify(regionRepository);
        assertThat(result, is(sameInstance(region)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeWidgetFromPage_invalidWidget() {
        long WIDGET_ID = -1L;
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
        expect(pageRepository.get(PAGE_ID)).andReturn(page);
        expect(pageRepository.get(page2.getEntityId())).andReturn(page2);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageList);
        expect(pageRepository.save(page2)).andReturn(page2);
        expect(pageRepository.save(page)).andReturn(page);        
        replay(userService);
        replay(pageRepository);
                          
        assertThat(pageService.movePage(PAGE_ID, page2.getEntityId()).getRenderSequence(), is(2L));
        assertThat(page2.getRenderSequence(), is(1L));        
      
        verify(userService);        
        verify(pageRepository);
    }
    
    @Test(expected=RuntimeException.class)
    public void movePage_invalidPageId() {               
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(INVALID_PAGE_ID)).andReturn(null);
        expect(pageRepository.get(page2.getEntityId())).andReturn(page2);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageList);
        replay(userService);
        replay(pageRepository);
                          
        pageService.movePage(INVALID_PAGE_ID, page2.getEntityId());
      
        verify(userService);        
        verify(pageRepository);
    }    
  
    @Test
    public void movePageToDefault() {               
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(page2.getEntityId())).andReturn(page2);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageList);
        expect(pageRepository.save(page)).andReturn(page);
        expect(pageRepository.save(page2)).andReturn(page2);        
        replay(userService);
        replay(pageRepository);
                          
        assertThat(pageService.movePageToDefault(page2.getEntityId()).getRenderSequence(), is(1L));
        assertThat(page.getRenderSequence(), is(2L));        
      
        verify(userService);        
        verify(pageRepository);
    }    
    
    @Test(expected=RuntimeException.class)
    public void movePageToDefault_invalidPageId() {               
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(pageRepository.get(INVALID_PAGE_ID)).andReturn(null);
        expect(pageRepository.getAllPages(user.getEntityId(), userPageType.getEntityId())).andReturn(pageList);
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
        
        PageLayout layout = createStrictMock(PageLayout.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
//        expect(layout.equals(layout)).andReturn((boolean) true);
        replay(layout);
        
        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
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
        
        List<Region> regions = new ArrayList<Region>();
        Region region = createStrictMock(Region.class);
        expect(region.getRenderOrder()).andReturn(1);
        replay(region);
        regions.add(new Region());
        regions.add(region);
        
        PageLayout prevLayout = createStrictMock(PageLayout.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);
        
        PageLayout layout = createStrictMock(PageLayout.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(3)).anyTimes();
//        expect(layout.equals(layout)).andReturn((boolean) true);
        replay(layout);
        
        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
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
        assertThat(regions.size(), is(3));
        assertThat(regions.get(regions.size()-1).getPage(), is(curPage));
        
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
        
        PageLayout prevLayout = createStrictMock(PageLayout.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);
        
        PageLayout layout = createStrictMock(PageLayout.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);
        
        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);
        
        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
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
        
        PageLayout prevLayout = createStrictMock(PageLayout.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);
        
        PageLayout layout = createStrictMock(PageLayout.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);
        
        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);
        
        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
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
        
        PageLayout prevLayout = createStrictMock(PageLayout.class);
        expect(prevLayout.getNumberOfRegions()).andReturn(new Long(2)).anyTimes();
        replay(prevLayout);
        
        PageLayout layout = createStrictMock(PageLayout.class);
        expect(layout.getNumberOfRegions()).andReturn(new Long(1)).anyTimes();
        replay(layout);
        
        regionRepository.delete(deletedRegion);
        expect(regionRepository.save(region)).andReturn(region);
        replay(regionRepository);
        
        //create a strict mock that ensures that the appropriate setters are
        //called, rather than checking the return value from the function
        Page curPage = createStrictMock(Page.class);
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
              Region region = new Region();
              regions.add(region);
        }
        return regions;
    }

    @Test
    public void moveRegionWidgetToPage_valid() {
        final long WIDGET_ID = 1L;
        final long CURRENT_PAGE_ID = 1L;
        final long TO_PAGE_ID = 2L;

        Page currentPageValue = new Page();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new Page();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        RegionWidget regionWidget = new RegionWidget(VALID_REGION_WIDGET_ID);

        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(regionWidget).times(2);

        replay(pageRepository);
        replay(regionWidgetRepository);

        RegionWidget updatedRegionWidget = pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);

        verify(pageRepository);
        verify(regionWidgetRepository);
        verifyPositions(0, regionWidget, true);

    }

    @Test(expected = IllegalArgumentException.class)
    public void moveRegionWidgetToPage_invalidWidget() {
        long WIDGET_ID = -1L;
        final long CURRENT_PAGE_ID = 1L;
        final long TO_PAGE_ID = 2L;

        Page currentPageValue = new Page();
        currentPageValue.setRegions(new ArrayList<Region>());
        currentPageValue.getRegions().add(originalRegion);
        currentPageValue.getRegions().add(targetRegion);

        Page toPageValue = new Page();
        toPageValue.setRegions(new ArrayList<Region>());
        toPageValue.getRegions().add(originalRegion);
        toPageValue.getRegions().add(targetRegion);

        expect(pageRepository.get(CURRENT_PAGE_ID)).andReturn(currentPageValue);
        expect(pageRepository.get(TO_PAGE_ID)).andReturn(toPageValue);
        expect(regionWidgetRepository.get(WIDGET_ID)).andReturn(null);

        pageService.moveRegionWidgetToPage(VALID_REGION_WIDGET_ID, TO_PAGE_ID);
    }
    
    @Test
    public void getUserPageType() {
        expect(pageTypeRepository.getUserPageType()).andReturn(userPageType);
        replay(pageTypeRepository);
        assertThat(pageService.getUserPageType(), is(userPageType));
        verify(pageTypeRepository);
    }

    @Test
    public void getPersonProfilePageType() {
        expect(pageTypeRepository.getPersonProfilePageType()).andReturn(personProfilePageType);
        replay(pageTypeRepository);
        assertThat(pageService.getPersonProfilePageType(), is(personProfilePageType));
        verify(pageTypeRepository);
    }

    @Test
    public void getSubPagePageType() {
        expect(pageTypeRepository.getSubPagePageType()).andReturn(subPagePageType);
        replay(pageTypeRepository);
        assertThat(pageService.getSubPagePageType(), is(subPagePageType));
        verify(pageTypeRepository);
    }
}
