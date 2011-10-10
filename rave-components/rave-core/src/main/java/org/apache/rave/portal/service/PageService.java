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

public interface PageService {
    /**
     * Gets a page based on the id
     * 
     * @param pageId to lookup
     * @return the Page object 
     */
    Page getPage(long pageId);
    
    /**
     * Gets all pages for the given user.
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */
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
    void deletePage(long pageId);
    
    /**
     * Moves a Region widget's position in a region or across regions
     *
     * @param regionWidgetId the id of the moved RegionWidget
     * @param newPosition the new index of the RegionWidget within the target region (0 based index)
     * @param toRegion the id of the Region to move the RegionWidget to
     * @param fromRegion the id of the Region where the RegionWidget currently resides
     * @return the updated RegionWidget
     */
    RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegion, long fromRegion);

    /**
     * Moves a RegionWidget from one page to another
     *       
     * @param regionWidgetId  the RegionWidget to move
     * @param toPageId the new page to move the regionWidgetTo
     * @return the updated RegionWidget object
     */
    RegionWidget moveRegionWidgetToPage(long regionWidgetId, long toPageId);    
    
    /**
     * Creates a new instance of a widget and adds it to the first position of the first region on the page
     * @param page_id the id of the page to add the widget to
     * @param widget_id the {@link org.apache.rave.portal.model.Widget} id to add
     * @return a valid widget instance
     */
    RegionWidget addWidgetToPage(long page_id, long widget_id);

    /**
     * Deletes the specified widget from the page.
     *
     * @param regionWidgetId the id of the region widget to delete.\
     * @return the region from which the widget was deleted
     */
    Region removeWidgetFromPage(long regionWidgetId);

    /**
     * Updates the page properties.
     *
     * @param pageId the id of the page to update
     * @param name the new name for the page
     * @param pageLayoutCode the new layout for the page
     */
    Page updatePage(long pageId, String name, String pageLayoutCode);
    
    /**
     * Moves a page to be rendered after another page in order for a user
     * 
     * @param pageId the pageId of the page to move
     * @param moveAfterPageId the pageId of the page you want to move after or 
     *                        -1 if you want this to be the first page
     * @return the updated Page object containing its new render sequence
     */
    Page movePage(long pageId, long moveAfterPageId);
    
    /**
     * Moves a page to be rendered as the first page for a user
     * 
     * @param pageId the pageId of the page to move to the default position
     * @return the updated Page object containing its new render sequence
     */
    Page movePageToDefault(long pageId);  
}