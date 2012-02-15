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

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.*;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultPageService implements PageService {
    private final PageRepository pageRepository;
    private final RegionRepository regionRepository;
    private final RegionWidgetRepository regionWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final PageLayoutRepository pageLayoutRepository;
    private final PageTypeRepository pageTypeRepository;
    private final UserService userService;    
    private final String defaultPageName;
    
    private final long MOVE_PAGE_DEFAULT_POSITION_INDEX = -1L;
    private final long USER_PAGE_TYPE_ID;
    private final long PERSON_PROFILE_PAGE_TYPE_ID;

    @Autowired
    public DefaultPageService(PageRepository pageRepository, 
                              RegionRepository regionRepository, 
                              WidgetRepository widgetRepository, 
                              RegionWidgetRepository regionWidgetRepository,
                              PageLayoutRepository pageLayoutRepository,
                              PageTypeRepository pageTypeRepository,
                              UserService userService,
                              @Value("${portal.page.default_name}") String defaultPageName) {
        this.pageRepository = pageRepository;
        this.regionRepository = regionRepository;
        this.regionWidgetRepository = regionWidgetRepository;
        this.widgetRepository = widgetRepository;
        this.pageLayoutRepository = pageLayoutRepository;
        this.pageTypeRepository = pageTypeRepository;
        this.userService = userService;
        this.defaultPageName = defaultPageName;

        USER_PAGE_TYPE_ID = pageTypeRepository.getUserPageType().getEntityId();
        PERSON_PROFILE_PAGE_TYPE_ID = pageTypeRepository.getPersonProfilePageType().getEntityId();
    }

    @Override
    public Page getPage(long pageId) {
        return pageRepository.get(pageId);
    }
    
    @Override
    public List<Page> getAllUserPages(long userId) {
        return pageRepository.getAllPages(userId, USER_PAGE_TYPE_ID);
    }
    
    @Override
    public Page getPageFromList(long pageId, List<Page> pages) {
        Page pageToFind = new Page(pageId);
        int index = pages.indexOf(pageToFind);
        return index == -1 ? null : pages.get(index);
    }

    @Override
    public Page getDefaultPageFromList(List<Page> pages) {
        // the first sequenced ordered page is considered the user's default page
        return (pages == null || pages.isEmpty()) ? null : pages.get(0);
    }
    
    @Override
    @Transactional
    public Page addNewUserPage(String pageName, String pageLayoutCode) {
        return addNewUserPage(userService.getAuthenticatedUser(), pageName, pageLayoutCode);
    }    
    
    @Override
    @Transactional
    public Page addNewDefaultUserPage(long userId) {
        User user = userService.getUserById(userId);
        return addNewUserPage(user, defaultPageName, user.getDefaultPageLayout().getCode());
    }       
    
    @Override
    public String getDefaultPageName() {
        return defaultPageName;
    }

    @Override
    @Transactional
    public void deletePage(long pageId) {                    
        User user = userService.getAuthenticatedUser();
        // first delete the page        
        pageRepository.delete(pageRepository.get(pageId));
        // now re-sequence the page sequence numbers

        //TODO RAVE-237:  We should be able to delete these lines.  If there are gaps in the sequence numbers, then it will still
        //TODO RAVE-237:  return values in the correct order.  We only need to update sequences when there is a change in order
        List<Page> pages = pageRepository.getAllPages(user.getEntityId(), USER_PAGE_TYPE_ID);
        updatePageRenderSequences(pages);
    }

    @Override
    @Transactional
    public int deletePages(long userId, long pageTypeId) {
        return pageRepository.deletePages(userId, pageTypeId);
    }
    
    @Override
    @Transactional
    public RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegionId, long fromRegionId) {
        Region target = getFromRepository(toRegionId, regionRepository);
        if (toRegionId == fromRegionId) {
            moveWithinRegion(regionWidgetId, newPosition, target);
        } else {
            moveBetweenRegions(regionWidgetId, newPosition, fromRegionId, target);
        }
        target = regionRepository.save(target);
        return findRegionWidgetById(regionWidgetId, target.getRegionWidgets());
    }
    
    @Override
    @Transactional
    public RegionWidget moveRegionWidgetToPage(long regionWidgetId, long toPageId) {                
        // Get the new page
        Page toPage = getFromRepository(toPageId, pageRepository);
        // Get the region widget
        RegionWidget regionWidget = getFromRepository(regionWidgetId, regionWidgetRepository);
        
        // Move it to first position of the first region
        Region moveToRegion = toPage.getRegions().get(0);
        regionWidget.setRenderOrder(0);
        regionWidget.setRegion(moveToRegion);
        moveToRegion.getRegionWidgets().add(0, regionWidget);
        // update the rendersequences of the widgets in this region
        updateRenderSequences(moveToRegion.getRegionWidgets());
        // persist it
        regionRepository.save(moveToRegion);
        return getFromRepository(regionWidgetId, regionWidgetRepository);        
    }    

    @Override
    @Transactional
    public Region removeWidgetFromPage(long regionWidgetId) {
        RegionWidget widget = getFromRepository(regionWidgetId, regionWidgetRepository);
        regionWidgetRepository.delete(widget);
        return getFromRepository(widget.getRegion().getEntityId(), regionRepository);
    }

    @Override
    @Transactional
    public RegionWidget addWidgetToPage(long pageId, long widgetId) {
        Page page = getFromRepository(pageId, pageRepository);
        Widget widget = getFromRepository(widgetId, widgetRepository);
        Region region = page.getRegions().get(0);
        return createWidgetInstance(widget, region, 0);
    }
    
    @Override
    @Transactional
    public Page movePage(long pageId, long moveAfterPageId) {
        return doMovePage(pageId, moveAfterPageId);
    }

    @Override
    @Transactional
    public Page movePageToDefault(long pageId) {    
        return doMovePage(pageId, MOVE_PAGE_DEFAULT_POSITION_INDEX);    
    }

    @Override
    public PageType getUserPageType() {
        return pageTypeRepository.getUserPageType();
    }

    @Override
    public PageType getPersonProfilePageType() {
        return pageTypeRepository.getPersonProfilePageType();
    }

    @Override
    @Transactional
    public Page updatePage(long pageId, String name, String pageLayoutCode) {
        Page page = pageRepository.get(pageId);
        PageLayout newLayout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);
        PageLayout curLayout = page.getPageLayout();

        //if the region lengths of the layouts do not match then adjust the new layout
        if (isLayoutAdjustmentNeeded(newLayout, curLayout)) {
            //if the new layout has fewer regions than the previous layout the widgets from the
            //deleted regions need to be appended to the last valid region in the new layout
            if (curLayout.getNumberOfRegions() > newLayout.getNumberOfRegions()) {
                reduceRegionsForPage(page, newLayout.getNumberOfRegions());
            }
            //otherwise the new layout has more regions that the previous layout and
            //new regions need to be added to the page
            else {
                long numberOfNewRegionsToAdd = newLayout.getNumberOfRegions() - curLayout.getNumberOfRegions();
                createAdditionalRegionsForPage(page, numberOfNewRegionsToAdd);
            }
        }

        //save the new page properties
        page.setName(name);
        page.setPageLayout(newLayout);
        pageRepository.save(page);

        return page;
    }
    
    /**
     * Utility function to determine if a Page layout adjustment is needed
     * which comparing the existing and new PageLayout objects
     * 
     * @param newLayout the new PageLayout to be applied to the Page
     * @param curLayout the existing PageLayout of the Page
     * @return true if the Page Regions need to be adjusted up or down
     */
    private boolean isLayoutAdjustmentNeeded(PageLayout newLayout, PageLayout curLayout) {
        return newLayout != null && 
               !curLayout.equals(newLayout) && 
               !curLayout.getNumberOfRegions().equals(newLayout.getNumberOfRegions());
    }

    /***
     * Utility function to create additional empty regions for a page.
     * 
     * @param page the Page object to add new regions to
     * @param numberOfNewRegionsToAdd the number of new Region objects to add to the Page
     */
    private void createAdditionalRegionsForPage(Page page, long numberOfNewRegionsToAdd) {
        //the next render order to use for the region
        List<Region> regions = page.getRegions();
        int lastRegionRenderOrder = regions.get(regions.size()-1).getRenderOrder() + 1;

        //add as many missing regions as the new layout requires
        for (int i=0; i < numberOfNewRegionsToAdd; i++) {
            Region newRegion = new Region();
            newRegion.setPage(page);
            newRegion.setRenderOrder(lastRegionRenderOrder++);
            regions.add(newRegion);
        }
    }
    /***
     * Utility function to reduce the regions for a page and move any RegionWidgets
     * in the Regions getting removed into the region with the largest
     * remaining render sequence value
     * 
     * @param page the Page to remove Regions from
     * @param numberOfRegionsInNewLayout the number of regions in the new layout
     */
    private void reduceRegionsForPage(Page page, long numberOfRegionsInNewLayout) {
        List<Region> regions = page.getRegions();
        Region lastValidRegion = regions.get((int) (numberOfRegionsInNewLayout-1));
        
        //remove all of the extra regions for this new layout and append the widgets
        while (regions.size() > numberOfRegionsInNewLayout) {
            Region deletedRegion = regions.remove(regions.size()-1);

            //append the regions widgets to to last valid region in the new layout
            for (RegionWidget widget : deletedRegion.getRegionWidgets()) {
                moveRegionWidgetToNewRegion(widget, lastValidRegion);
            }
            regionRepository.delete(deletedRegion);
        }
        regionRepository.save(lastValidRegion);
    }
    
    /**
     * Utility function to move a RegionWidget to a new Region
     * 
     * @param regionWidget the RegionWidget to move
     * @param moveToRegion the new Region to move it to
     */
    private void moveRegionWidgetToNewRegion(RegionWidget regionWidget, Region moveToRegion) {
        List<RegionWidget> regionWidgets = moveToRegion.getRegionWidgets();                
        int renderOrder = regionWidgets.isEmpty() ? 1 : regionWidgets.get(regionWidgets.size()-1).getRenderOrder() + 1;
        regionWidget.setRegion(moveToRegion);
        regionWidget.setRenderOrder(renderOrder);
        moveToRegion.getRegionWidgets().add(regionWidget);        
    }
    
    private RegionWidget createWidgetInstance(Widget widget, Region region, int position) {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setRenderOrder(position);
        regionWidget.setWidget(widget);
        region.getRegionWidgets().add(position, regionWidget);
        updateRenderSequences(region.getRegionWidgets());
        Region persistedRegion = regionRepository.save(region);
        return persistedRegion.getRegionWidgets().get(position);
    }

    private void moveWithinRegion(long regionWidgetId, int newPosition, Region target) {
        replaceRegionWidget(regionWidgetId, newPosition, target, target);
        updateRenderSequences(target.getRegionWidgets());
    }

    private void moveBetweenRegions(long regionWidgetId, int newPosition, long fromRegion, Region target) {
        Region source = getFromRepository(fromRegion, regionRepository);
        replaceRegionWidget(regionWidgetId, newPosition, target, source);
        updateRenderSequences(source.getRegionWidgets());
        updateRenderSequences(target.getRegionWidgets());
        regionRepository.save(source);
    }

    private void replaceRegionWidget(long regionWidgetId, int newPosition, Region target, Region source) {
        RegionWidget widget = findRegionWidgetById(regionWidgetId, source.getRegionWidgets());
        source.getRegionWidgets().remove(widget);
        target.getRegionWidgets().add(newPosition, widget);
    }

    private Page addNewUserPage(User user, String pageName, String pageLayoutCode) {
        PageLayout pageLayout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);
        
        // Create regions
        List<Region> regions = new ArrayList<Region>();
        int regionCount;
        for (regionCount = 0; regionCount < pageLayout.getNumberOfRegions(); regionCount++) {
            Region region = new Region();
            region.setRenderOrder(regionCount);
            regions.add(region);
        }

        // Create a Page object and register it.
        long renderSequence = getAllUserPages(user.getEntityId()).size() + 1;
        Page page = new Page();
        page.setName(pageName);       
        page.setOwner(user);
        page.setPageLayout(pageLayout);
        page.setRenderSequence(renderSequence);
        page.setRegions(regions);
        // set this as a "user" page type
        page.setPageType(pageTypeRepository.getUserPageType());
        pageRepository.save(page);
        
        return page;
    }

    private void updatePageRenderSequences(List<Page> pages) {       
        if (pages != null && !pages.isEmpty()) {
            for (int i = 0; i < pages.size(); i++) {
                Page p = pages.get(i);                
                p.setRenderSequence((long)i+1);                               
            }

            for (Page page : pages) {
                pageRepository.save(page);
            }
        }       
    } 
    
    private Page doMovePage(long pageId, long moveAfterPageId) {
        // get the logged in user
        User user = userService.getAuthenticatedUser();

        // get the page to move and the page to move after
        Page movingPage = pageRepository.get(pageId);
        Page afterPage = null;
        int newIndex = 0;
        
        // check to see if we should move the page to beginning
        if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_INDEX) {
            afterPage = pageRepository.get(moveAfterPageId);
        }

        // get all of the user's pages
        // the pageRepository returns an un-modifiable list
        // so we need to create a modifyable arraylist
        List<Page> pages = new ArrayList<Page>(pageRepository.getAllPages(user.getEntityId(), USER_PAGE_TYPE_ID));

        // first remove it from the list         
        if (!pages.remove(movingPage)) {
            throw new RuntimeException("unable to find pageId " + pageId + " attempted to be moved for user " + user);
        }

        // ...now re-insert in new location
        if (afterPage != null) {
            newIndex = pages.indexOf(afterPage) + 1;
        }
        pages.add(newIndex, movingPage);

        // persist the new page order
        updatePageRenderSequences(pages);
        
        return movingPage;
    }

    private static <T> T getFromRepository(long id, Repository<T> repo) {
        T object = repo.get(id);
        if (object == null) {
            throw new IllegalArgumentException("Could not find object of given id in " + repo.getClass().getSimpleName());
        }
        return object;
    }

    private static void updateRenderSequences(List<RegionWidget> regionWidgets) {
        int count = 0;
        for (RegionWidget widget : regionWidgets) {
            widget.setRenderOrder(count);
            count++;
        }
    }

    private static RegionWidget findRegionWidgetById(Long id, List<RegionWidget> regionWidgets) {
        for (RegionWidget widget : regionWidgets) {
            if (widget.getEntityId().equals(id)) {
                return widget;
            }
        }
        throw new IllegalArgumentException("Invalid RegionWidget ID");
    }
}
