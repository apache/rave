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
package org.apache.rave.portal.service;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;

import java.util.List;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author carlucci
 */
public interface PageService {
    /**
     * Gets a page based on the id
     * 
     * @param pageId to lookup
     * @return the Page object 
     */
    @PostAuthorize("hasPermission(returnObject, 'read')")    
    Page getPage(long pageId);
    
    /**
     * Gets all user pages for the given user.
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */   
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Page', 'read')") 
    List<Page> getAllUserPages(long userId);
    
    /**
     * Gets all person profile pages for the given user.
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<Page> getAllPersonProfilePages(long userId);

    /**
     * Return the page object from a list of pages given the pageId
     *
     * @param pageId the pageId to look for
     * @param pages a list of pages to search in
     * @return the Page object representing the pageId, or null if not found
     */
    Page getPageFromList(long pageId, List<Page> pages);
    
    /**
     * Given a List of Page objects, return the default one
     * 
     * @param pages the List of Page objects
     * @return the default Page in the list
     */
    Page getDefaultPageFromList(List<Page> pages);
    
    /**
     * Creates a new user page with the supplied pageName and pageLayoutCode
     * 
     * @param pageName the name of the new page
     * @param pageLayoutCode the page layout code
     * @return the new Page object
     */
    @PostAuthorize("hasPermission(returnObject, 'create')") 
    Page addNewUserPage(String pageName, String pageLayoutCode);
    
    /**
     * Creates a new default user page for the supplied user, and uses the
     * defaultPageLayout attribute of the User to determine the layout of the
     * new page.
     * 
     * @param userId the entityId of the user to create a default page for
     * @return the new default Page object for the user
     */        
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Page', 'create')")         
    Page addNewDefaultUserPage(long userId);

    /**
     * Creates a new sub page with the supplied pageName and pageLayoutCode for the parentPage
     *
     * @param pageName the name of the new page
     * @param pageLayoutCode the page layout code
     * @param parentPage the parent of the subpage
     * @return the new Page object
     */
    @PostAuthorize("hasPermission(returnObject, 'create')")
    Page addNewSubPage(String pageName, String pageLayoutCode, Page parentPage);

    /**
     * Get the default page name used by Rave
     * 
     * @return the name of the default page used by Rave
     */
    String getDefaultPageName();
    
    /**
     * Deletes the page with the supplied pageId
     * 
     * @param pageId the pageId to delete
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'delete')") 
    void deletePage(long pageId);

    /**
     * Deletes all pages of the supplied pageType for the userId
     *
     * @param userId
     * @param pageType
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Page', 'delete')")
    int deletePages(long userId, PageType pageType);
    
    /**
     * Moves a Region widget's position in a region or across regions
     *
     * @param regionWidgetId the id of the moved RegionWidget
     * @param newPosition the new index of the RegionWidget within the target region (0 based index)
     * @param toRegionId the id of the Region to move the RegionWidget to
     * @param fromRegionId the id of the Region where the RegionWidget currently resides
     * @return the updated RegionWidget
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.portal.model.RegionWidget', 'update') and " +
                  "hasPermission(#toRegionId, 'org.apache.rave.portal.model.Region', 'update') and " +
                  "hasPermission(#fromRegionId, 'org.apache.rave.portal.model.Region', 'update')")
    RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegionId, long fromRegionId);

    /**
     * Moves a RegionWidget from one page to another
     *       
     * @param regionWidgetId  the RegionWidget to move
     * @param toPageId the new page to move the regionWidgetTo
     * @return the updated RegionWidget object
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.portal.model.RegionWidget', 'update') and " +
                  "hasPermission(#toPageId, 'org.apache.rave.portal.model.Page', 'update')")
    RegionWidget moveRegionWidgetToPage(long regionWidgetId, long toPageId);    
    
    /**
     * Creates a new instance of a widget and adds it to the first position of the first region on the page
     * @param pageId the id of the page to add the widget to
     * @param widgetId the {@link org.apache.rave.portal.model.Widget} id to add
     * @return a valid widget instance
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'update') and " +
                  "hasPermission(#widgetId, 'org.apache.rave.portal.model.Widget', 'read')") 
    RegionWidget addWidgetToPage(long pageId, long widgetId);

    /**
     * Deletes the specified widget from the page.
     *
     * @param regionWidgetId the id of the region widget to delete.\
     * @return the region from which the widget was deleted
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.portal.model.RegionWidget', 'delete')")
    Region removeWidgetFromPage(long regionWidgetId);

    /**
     * Updates the page properties.
     *
     * @param pageId the id of the page to update
     * @param name the new name for the page
     * @param pageLayoutCode the new layout for the page
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'update')") 
    Page updatePage(long pageId, String name, String pageLayoutCode);
    
    /**
     * Moves a page to be rendered after another page in order for a user
     * 
     * @param pageId the pageId of the page to move
     * @param moveAfterPageId the pageId of the page you want to move after or 
     *                        -1 if you want this to be the first page
     * @return the updated Page object containing its new render sequence
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'update') and " +
                  "hasPermission(#moveAfterPageId, 'org.apache.rave.portal.model.Page', 'update')")
    Page movePage(long pageId, long moveAfterPageId);
    
    /**
     * Moves a page to be rendered as the first page for a user
     * 
     * @param pageId the pageId of the page to move to the default position
     * @return the updated Page object containing its new render sequence
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'update')") 
    Page movePageToDefault(long pageId);
}