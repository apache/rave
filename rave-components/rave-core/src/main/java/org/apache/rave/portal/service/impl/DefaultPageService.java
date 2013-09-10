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

import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.repository.Repository;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.repository.WidgetRepository;
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
    private final UserService userService;
    private final PageTemplateRepository pageTemplateRepository;
    private final String defaultPageName;

    private final String MOVE_PAGE_DEFAULT_POSITION_INDEX = "-1";

    @Autowired
    public DefaultPageService(PageRepository pageRepository,
                              PageTemplateRepository pageTemplateRepository,
                              RegionRepository regionRepository,
                              WidgetRepository widgetRepository,
                              RegionWidgetRepository regionWidgetRepository,
                              PageLayoutRepository pageLayoutRepository,
                              UserService userService,
                              @Value("${portal.page.default_name}") String defaultPageName) {
        this.pageRepository = pageRepository;
        this.pageTemplateRepository = pageTemplateRepository;
        this.regionRepository = regionRepository;
        this.regionWidgetRepository = regionWidgetRepository;
        this.widgetRepository = widgetRepository;
        this.pageLayoutRepository = pageLayoutRepository;
        this.userService = userService;
        this.defaultPageName = defaultPageName;
    }

    @Override
    public SearchResult<Page> getAll() {
        List<Page> pages = pageRepository.getAll();
        int count = pageRepository.getCountAll();
        return new SearchResult<Page>(pages, count);
    }

    @Override
    public SearchResult<Page> getLimited(int offset, int limit) {
        List<Page> pages = pageRepository.getLimitedList(offset, limit);
        int count = pageRepository.getCountAll();
        SearchResult<Page> result = new SearchResult<Page>(pages, count);
        result.setOffset(offset);
        result.setPageSize(limit);
        return result;
    }

    @Override
    public Page getPage(String pageId) {
        return pageRepository.get(pageId);
    }

    @Override
    public List<Page> getAllUserPages(String userId) {
        return pageRepository.getAllPagesForUserType(userId, PageType.USER.toString());
    }

    @Override
    @Transactional
    public Page getPersonProfilePage(String userId) {
        List<Page> profilePages = pageRepository.getAllPagesForUserType(userId, PageType.PERSON_PROFILE.toString());
        Page personPage = null;
        if (profilePages.isEmpty()){
            personPage = pageRepository.createPageForUser(userService.getUserById(userId), pageTemplateRepository.getDefaultPage(PageType.PERSON_PROFILE.toString()));
        } else {
            personPage = profilePages.get(0);
        }
        return personPage;
    }

    @Override
    public List<Page> getPages(String context, String contextId) {
        return pageRepository.getPagesForContextType(contextId, context);
    }

    @Override
    public Page getPageFromList(String pageId, List<Page> pages) {
        for(Page page: pages) {
            if(page.getId().equals(pageId)){
                return page;
            }
        }
        return null;
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
    public Page addNewDefaultUserPage(String userId) {
        User user = userService.getUserById(userId);
        return addNewUserPage(user, defaultPageName, user.getDefaultPageLayout().getCode());
    }

    @Override
    @Transactional
    public Page addNewSubPage(String pageName, String pageLayoutCode, Page parentPage) {
        User user = userService.getAuthenticatedUser();
        PageLayout pageLayout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);

        // Create regions
        List<Region> regions = new ArrayList<Region>();
        List<Page> parentsSubPages = new ArrayList<Page>();
        int regionCount;
        for (regionCount = 0; regionCount < pageLayout.getNumberOfRegions(); regionCount++) {
            Region region = new RegionImpl();
            region.setRenderOrder(regionCount);
            // TODO: this should eventually be defined by the PageTemplateRegion.locked field
            region.setLocked(false);
            regions.add(region);
        }

        // Create a Page object and register it.
        long renderSequence = (parentPage.getSubPages() != null) ? parentPage.getSubPages().size() + 1 : 1;
        Page page = new PageImpl();
        page.setName(pageName);
        page.setOwnerId(user.getId());
        page.setPageLayout(pageLayout);
        page.setRegions(regions);
        // set this as a "sub-page" page type
        page.setPageType(PageType.SUB_PAGE.toString());

        PageUser pageUser = new PageUserImpl(page.getOwnerId(), page, renderSequence);
        pageUser.setPageStatus(PageInvitationStatus.OWNER);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(pageUser);
        page.setMembers(members);

        // Properly sets both sides of the circular parent-child reference
        page.setParentPage(parentPage);
        if (parentPage.getSubPages() != null){
            parentsSubPages = parentPage.getSubPages();
        }
        parentsSubPages.add(page);
        parentPage.setSubPages(parentsSubPages);
        pageRepository.save(page);

        return page;
    }

    @Override
    public String getDefaultPageName() {
        return defaultPageName;
    }

    @Override
    @Transactional
    public void deletePage(String pageId) {
        User user = userService.getAuthenticatedUser();
        // first delete the page
        pageRepository.delete(pageRepository.get(pageId));
        // now re-sequence the page sequence numbers

        //TODO RAVE-237:  We should be able to delete these lines.  If there are gaps in the sequence numbers, then it will still
        //TODO RAVE-237:  return values in the correct order.  We only need to update sequences when there is a change in order
        List<PageUser> thisUsersPages = new ArrayList<PageUser>(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString()));
        updatePageRenderSequences(thisUsersPages);
    }

    @Override
    @Transactional
    public int deletePages(String userId, String pageType) {
        return pageRepository.deletePages(userId, pageType);
    }

    @Override
    @Transactional
    public RegionWidget moveRegionWidget(String regionWidgetId, int newPosition, String toRegionId, String fromRegionId) {
        Region target = getFromRepository(toRegionId, regionRepository);
        // verify that the target Region and the RegionWidget are not locked and can be modified
        RegionWidget regionWidget = regionWidgetRepository.get(regionWidgetId);
        verifyRegionWidgetIsNotLocked(regionWidget);
        verifyRegionIsNotLocked(target);
        if (toRegionId.equals(fromRegionId)) {
            moveWithinRegion(regionWidgetId, newPosition, target);
        } else {
            moveBetweenRegions(regionWidgetId, newPosition, fromRegionId, target);
        }
        target = regionRepository.save(target);
        return findRegionWidgetById(regionWidgetId, target.getRegionWidgets());
    }

    @Override
    @Transactional
    public RegionWidget moveRegionWidgetToPage(String regionWidgetId, String toPageId) {
        // Get the new page
        Page toPage = getFromRepository(toPageId, pageRepository);
        // Get the region widget
        RegionWidget regionWidget = getFromRepository(regionWidgetId, regionWidgetRepository);

        Region moveFromRegion = regionWidget.getRegion();

        // Move it to first position of the first region
        Region moveToRegion = toPage.getRegions().get(0);

        // verify the region widget, source, and target regions not locked
        verifyRegionWidgetIsNotLocked(regionWidget);
        verifyRegionIsNotLocked(moveToRegion);

        regionWidget.setRenderOrder(0);
        regionWidget.setRegion(moveToRegion);
        moveToRegion.getRegionWidgets().add(0, regionWidget);
        //remove it from the old region
        moveFromRegion.getRegionWidgets().remove(regionWidget);
        // update the rendersequences of the widgets in this region
        updateRenderSequences(moveToRegion.getRegionWidgets());
        updateRenderSequences(moveFromRegion.getRegionWidgets());
        // persist it
        regionRepository.save(moveToRegion);
        regionRepository.save(moveFromRegion);
        return getFromRepository(regionWidgetId, regionWidgetRepository);
    }

    @Override
    @Transactional
    public Region removeWidgetFromPage(String regionWidgetId) {
        RegionWidget regionWidget = getFromRepository(regionWidgetId, regionWidgetRepository);
        verifyRegionWidgetIsNotLocked(regionWidget);
        regionWidgetRepository.delete(regionWidget);
        return getFromRepository(regionWidget.getRegion().getId(), regionRepository);
    }

    @Override
    @Transactional
    public RegionWidget addWidgetToPage(String pageId, String widgetId) {
        Page page = getFromRepository(pageId, pageRepository);
        Widget widget = getFromRepository(widgetId, widgetRepository);
        Region region = page.getRegions().get(0);
        verifyRegionIsNotLocked(region);
        return createWidgetInstance(widget, region, 0);
    }

    @Override
    @Transactional
    public RegionWidget addWidgetToPageRegion(String pageId, String widgetId, String regionId) {
        Page page = getFromRepository(pageId, pageRepository);
        Widget widget = getFromRepository(widgetId, widgetRepository);
        for(Region region : page.getRegions()){
            if(region.getId().equals(regionId)){
                verifyRegionIsNotLocked(region);
                return createWidgetInstance(widget, region, 0);
            }
        }
        // region not found
        return null;
    }

    @Override
    @Transactional
    public Page movePage(String pageId, String moveAfterPageId) {
        return doMovePage(pageId, moveAfterPageId);
    }

    @Override
    @Transactional
    public Page movePageToDefault(String pageId) {
        return doMovePage(pageId, MOVE_PAGE_DEFAULT_POSITION_INDEX);
    }

    @Override
    @Transactional
    public Page updatePage(String pageId, String name, String pageLayoutCode) {
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

    @Transactional
    public Boolean clonePageForUser(String pageId, String userId, String pageName) {
        Page page = getPage(pageId);
        if(pageName == null || pageName.equals("null")){
            // try to use the original page name if none supplied
            pageName = page.getName();
        }
        Page clonedPage = addNewUserPage(userService.getUserById(userId), pageName, page.getPageLayout().getCode());
        // populate all the widgets in cloned page from original
        for(int i=0; i<page.getRegions().size(); i++){
            for(int j=0; j<page.getRegions().get(i).getRegionWidgets().size(); j++){
                String widgetId = page.getRegions().get(i).getRegionWidgets().get(j).getWidgetId();
                addWidgetToPageRegion(clonedPage.getId(), widgetId, clonedPage.getRegions().get(i).getId());
            }
        }
        clonedPage = getFromRepository(clonedPage.getId(), pageRepository);
        // newly created page - so only one pageUser
        PageUser pageUser = clonedPage.getMembers().get(0);
        // update status to pending
        pageUser.setPageStatus(PageInvitationStatus.PENDING);
        if(pageRepository.save(clonedPage) != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Transactional
    public Boolean addMemberToPage(String pageId, String userId){
        Page page = getPage(pageId);
        PageUser pageUser = new PageUserImpl();
        pageUser.setUserId(userService.getUserById(userId).getId());
        pageUser.setPage(page);
        pageUser.setPageStatus(PageInvitationStatus.PENDING);
        List<PageUser> thisUsersPages = pageRepository.getPagesForUser(userService.getUserById(userId).getId(), PageType.USER.toString());
        pageUser.setRenderSequence(new Long(thisUsersPages.size() + 1));
        page.getMembers().add(pageUser);
        if(pageRepository.save(page) != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Transactional
    public Boolean removeMemberFromPage(String pageId, String userId){
        Page page = this.getPage(pageId);
        if(page.getOwnerId().equals(userId)){
            // If I am the owner, this page has been cloned
            // Declining a cloned page means there is no need to strip
            // out this users pageUser object, instead remove the page itself
            // which also removes the pageUser object further down the stack
            pageRepository.delete(page);
            return true;
        }
        PageUser pageUserToRemove = null;
        for(PageUser pageUser : page.getMembers()){
            if(pageUser.getUserId().equals(userId)){
                pageUserToRemove = pageUser;
                break;
            }
        }
        if(pageUserToRemove != null){
            page.getMembers().remove(pageUserToRemove);
            if(pageRepository.save(page) != null){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateSharedPageStatus(String pageId, String shareStatus) {
        Page page = this.getPage(pageId);
        for(PageUser pageUser : page.getMembers()){
            if(pageUser.getUserId().equals(userService.getAuthenticatedUser().getId())){
                pageUser.setPageStatus(PageInvitationStatus.get(shareStatus));
            }
        }
        if(pageRepository.save(page) != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Override
    @Transactional
    public Boolean updateSharedPageStatus(String pageId, String userId, String shareStatus) {
        Page page = this.getPage(pageId);
        for(PageUser pageUser : page.getMembers()){
            if(pageUser.getUserId().equals(userId)){
                pageUser.setPageStatus(PageInvitationStatus.get(shareStatus));
            }
        }
        if(pageRepository.save(page) != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Override
    @Transactional
    public Boolean updatePageEditingStatus(String pageId, String userId, boolean isEditor) {
        Page page = this.getPage(pageId);
        for(PageUser pageUser : page.getMembers()){
            if(pageUser.getUserId().equals(userService.getUserById(userId).getId())){
                pageUser.setEditor(isEditor);
            }
        }
        if(pageRepository.save(page) != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
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
            Region newRegion = new RegionImpl();
            newRegion.setPage(page);
            newRegion.setRenderOrder(lastRegionRenderOrder++);
            newRegion.setLocked(false);
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
        RegionWidget regionWidget = new RegionWidgetImpl();
        regionWidget.setRenderOrder(position);
        regionWidget.setWidgetId(widget.getId());
        // TODO: setLocked and setHideChrome are hard-coded to false for new widgets manually added by users
        //       which makes sense for most default cases.  However should we change them to a customizable property
        //       to allow for more flexibility?
        regionWidget.setLocked(false);
        regionWidget.setHideChrome(false);
        regionWidget.setRegion(region);
        region.getRegionWidgets().add(position, regionWidget);
        updateRenderSequences(region.getRegionWidgets());
        Region persistedRegion = regionRepository.save(region);
        return persistedRegion.getRegionWidgets().get(position);
    }

    private void moveWithinRegion(String regionWidgetId, int newPosition, Region target) {
        replaceRegionWidget(regionWidgetId, newPosition, target, target);
        updateRenderSequences(target.getRegionWidgets());
    }

    private void moveBetweenRegions(String regionWidgetId, int newPosition, String fromRegion, Region target) {
        Region source = getFromRepository(fromRegion, regionRepository);
        replaceRegionWidget(regionWidgetId, newPosition, target, source);
        updateRenderSequences(source.getRegionWidgets());
        updateRenderSequences(target.getRegionWidgets());
        regionRepository.save(source);
    }

    private void replaceRegionWidget(String regionWidgetId, int newPosition, Region target, Region source) {
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
            Region region = new RegionImpl();
            region.setRenderOrder(regionCount);
            // TODO: this should eventually be defined by the PageTemplateRegion.locked field
            region.setLocked(false);
            regions.add(region);
        }
        // Get all User Pages
        Page page = null;
        List<Page> defaultUserPage = pageRepository.getAllPagesForUserType(user.getId(), PageType.USER.toString());
        // Is there a default page for this user
        if (defaultUserPage.isEmpty()) {
            return pageRepository.createPageForUser(user, pageTemplateRepository.getDefaultPage(PageType.USER.toString()));
        }

        // If we have a page already or if there was an exception from above then create the page
        // Create the new page for the user
        long renderSequence = defaultUserPage.size() + 1;
        page = new PageImpl();
        page.setName(pageName);
        page.setOwnerId(user.getId());
        page.setPageLayout(pageLayout);
        PageUser pageUser = new PageUserImpl(page.getOwnerId(), page, renderSequence);
        pageUser.setPageStatus(PageInvitationStatus.OWNER);
        pageUser.setEditor(true);

        List<PageUser> members = new ArrayList<PageUser>();
        members.add(pageUser);
        page.setMembers(members);

        page.setRegions(regions);
        // set this as a "user" page type
        page.setPageType(PageType.USER.toString());
        return pageRepository.save(page);
    }

    private void updatePageRenderSequences(List<PageUser> pages) {
        if (pages != null && !pages.isEmpty()) {
            for (int i = 0; i < pages.size(); i++) {
                PageUser p = pages.get(i);
                p.setRenderSequence((long)i+1);
            }

            for (PageUser page : pages) {
                pageRepository.save(page.getPage());
            }
        }
    }

    private Page doMovePage(String pageId, String moveAfterPageId) {
        // get the logged in user
        User user = userService.getAuthenticatedUser();
        // get the page to move and the page to move after
        PageUser movingPageUser = pageRepository.getSingleRecord(user.getId(), pageId);
        PageUser afterPageUser = null;
        int newIndex = 0;
        // check to see if we should move the page to beginning
        if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_INDEX) {
            afterPageUser = pageRepository.getSingleRecord(user.getId(), moveAfterPageId);
        }

        // get all of the user's pages
        // the pageRepository returns an un-modifiable list
        // so we need to create a modifyable arraylist
        List<PageUser> thisUsersPages = new ArrayList<PageUser>(pageRepository.getPagesForUser(user.getId(), PageType.USER.toString()));
        // first remove it from the list
        if (!thisUsersPages.remove(movingPageUser)) {
            throw new RuntimeException("unable to find pageId " + pageId + " attempted to be moved for user " + user);
        }

        if (afterPageUser != null) {
            newIndex = thisUsersPages.indexOf(afterPageUser) + 1;
        }
        thisUsersPages.add(newIndex, movingPageUser);
        // persist the new page order
        updatePageRenderSequences(thisUsersPages);
        return movingPageUser.getPage();
    }

    private static <T> T getFromRepository(String id, Repository<T> repo) {
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

    private static RegionWidget findRegionWidgetById(String id, List<RegionWidget> regionWidgets) {
        for (RegionWidget widget : regionWidgets) {
            if (widget.getId().equals(id)) {
                return widget;
            }
        }
        throw new IllegalArgumentException("Invalid RegionWidget ID");
    }

    private void verifyRegionIsNotLocked(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("region is null");
        }
        if (region.isLocked()) {
            throw new UnsupportedOperationException("Can't modify locked Region: " + region);
        }
    }

    private void verifyRegionWidgetIsNotLocked(RegionWidget regionWidget) {
        if (regionWidget == null) {
            throw new IllegalArgumentException("regionWidget is null");
        }
        if (regionWidget.isLocked()) {
            throw new UnsupportedOperationException("Can't modify locked RegionWidget: " + regionWidget);
        }
        // also traverse up and make sure its region is also not locked
        verifyRegionIsNotLocked(regionWidget.getRegion());
    }
}
