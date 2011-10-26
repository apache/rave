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
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;

import java.util.List;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * TODO:
 * 1) RAVE-303: Protect addNewDefaultPage once the code has been re-factored to call 
 *    this method from PageController when there are 0 pages for a user instead
 *    of calling it from DefaultNewAccountService (the user is not authenticated
 *    at that point)
 * 
 * 2) Protect the rest of the non Page object related methods here after writing
 *    the associated Default<Model>PermissionEvaluator classes.  Also,
 *    determine if those functions should be re-factored into more appropriate
 *    service classes
 * 
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
     * Gets all pages for the given user.
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */   
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Page', 'read')") 
    List<Page> getAllPages(long userId);
    
    /**
     * Return the page object from a list of pages given the pageId
     * 
     * @param pageId the pageId to look for
     * @param pages a list of pages to search in
     * @return the Page object representing the pageId, or null if not found
     */
    Page getPageFromList(long pageId, List<Page> pages);
    
    /**
     * Creates a new page with the supplied pageName and pageLayoutCode
     * 
     * @param pageName the name of the new page
     * @param pageLayoutCode the page layout code
     * @return the new Page object
     */
    @PostAuthorize("hasPermission(returnObject, 'create')") 
    Page addNewPage(String pageName, String pageLayoutCode);
    
    /**
     * Creates a new default page for the supplied user using the
     * supplied pageLayoutCode.  This method should be used for new users
     * who just registered and need to create the default page
     * 
     * @param user
     * @param pageLayoutCode
     * @return 
     */       
    // TODO: RAVE-303: addNewDefaultPage is currently not security protected because
    //       the DefaultPagePermissionEvaluator requires an authenticated user.
    //       This function should be called in PageController if the user has
    //       0 pages instead of calling it in DefaultNewAccountService.  Once
    //       refactored there we can protect this method    
    Page addNewDefaultPage(User user, String pageLayoutCode);
    
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
     * 
     * TODO: add a second hasPermission clause for Widget once the WidgetPermissionEvaluator has been created     
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.portal.model.Page', 'update')") 
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