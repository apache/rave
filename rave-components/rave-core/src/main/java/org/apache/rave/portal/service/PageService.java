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

import org.apache.rave.model.Page;
import org.apache.rave.model.PageType;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.rest.model.SearchResult;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author carlucci
 */
public interface PageService {

    /**
     * Gets a list of all pages
     *
     * @return search results
     */
    SearchResult<Page> getAll();

    /**
     * Gets a limited list of pages
     *
     * @param offset integer offset from 0
     * @param limit  number of results to return
     * @return search results
     */
    SearchResult<Page> getLimited(int offset, int limit);

    /**
     * Gets a page based on the id
     *
     * @param pageId to lookup
     * @return the Page object
     */
    @PostAuthorize("returnObject == null or hasPermission(returnObject, 'read')")
    Page getPage(String pageId);

    /**
     * Gets all user pages for the given user.
     *
     * @deprecated method is deprecated in favor of getPages("portal", userId);
     *
     * @param userId The user to retrieve pages for.
     * @return A non null possible empty list of pages for the given user.
     */
    @Deprecated
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.Page', 'read')")
    List<Page> getAllUserPages(String userId);

    /**
     * Gets the profile page for the given user.
     *
     * @deprecated method is deprecated in favor of getPages("profile", userId);
     *
     * @param userId The user to retrieve the page for.
     * @return The profile page
     */
    @Deprecated
    @PostAuthorize("returnObject == null or hasPermission(returnObject, 'read')")
    Page getPersonProfilePage(String userId);

    /**
     * Gets the set of pages for the given user and context
     *
     * @since 0.22
     * @param context the context for the pages ex: "portal", "profile", etc.
     * @param contextId the identifier of the item in the context that matches the page.
     *                  examples:
     *                      context: "person_profile", contextId: "profile owner's id"
     *                      context: "group", contextId: "group id"
     *                      context: "project", contextId: "project number"
     *                      context: "dashboard", contextId: "subject"
     *                      context: "portal", contextId: "owner's id"
     *
     * @return A non-null, possibly empty list of page for the given user.
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<Page> getPages(String context, String contextId);

    /**
     * Return the page object from a list of pages given the pageId
     *
     * @param pageId the pageId to look for
     * @param pages  a list of pages to search in
     * @return the Page object representing the pageId, or null if not found
     */
    Page getPageFromList(String pageId, List<Page> pages);

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
     * @param pageName       the name of the new page
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
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.Page', 'create')")
    Page addNewDefaultUserPage(String userId);

    /**
     * Creates a new sub page with the supplied pageName and pageLayoutCode for the parentPage
     *
     * @param pageName       the name of the new page
     * @param pageLayoutCode the page layout code
     * @param parentPage     the parent of the subpage
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
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'delete')")
    void deletePage(String pageId);

    /**
     * Deletes all pages of the supplied pageType for the userId
     *
     * @param userId
     * @param pageType
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.Page', 'delete')")
    int deletePages(String userId, String pageType);
    
    /**
     * Moves a Region widget's position in a region or across regions
     *
     * @param regionWidgetId the id of the moved RegionWidget
     * @param newPosition    the new index of the RegionWidget within the target region (0 based index)
     * @param toRegionId     the id of the Region to move the RegionWidget to
     * @param fromRegionId   the id of the Region where the RegionWidget currently resides
     * @return the updated RegionWidget
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'update') and " +
            "hasPermission(#toRegionId, 'org.apache.rave.model.Region', 'update') and " +
            "hasPermission(#fromRegionId, 'org.apache.rave.model.Region', 'update')")
    RegionWidget moveRegionWidget(String regionWidgetId, int newPosition, String toRegionId, String fromRegionId);

    /**
     * Moves a RegionWidget from one page to another
     *
     * @param regionWidgetId the RegionWidget to move
     * @param toPageId       the new page to move the regionWidgetTo
     * @return the updated RegionWidget object
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'update') and " +
            "hasPermission(#toPageId, 'org.apache.rave.model.Page', 'update')")
    RegionWidget moveRegionWidgetToPage(String regionWidgetId, String toPageId);

    /**
     * Creates a new instance of a widget and adds it to the first position of the first region on the page
     *
     * @param pageId   the id of the page to add the widget to
     * @param widgetId the {@link org.apache.rave.model.Widget} id to add
     * @return a valid widget instance
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update') and " +
            "hasPermission(#widgetId, 'org.apache.rave.model.Widget', 'read')")
    RegionWidget addWidgetToPage(String pageId, String widgetId);

    /**
     * Creates a new instance of a widget and adds it to the first position of the identified region on the page
     *
     * @param pageId   the id of the page to add the widget to
     * @param widgetId the {@link org.apache.rave.model.Widget} id to add
     * @return a valid widget instance
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update') and " +
            "hasPermission(#widgetId, 'org.apache.rave.model.Widget', 'read')")
    RegionWidget addWidgetToPageRegion(String pageId, String widgetId, String regionId);

    /**
     * Deletes the specified widget from the page.
     *
     * @param regionWidgetId the id of the region widget to delete.\
     * @return the region from which the widget was deleted
     */
    @PreAuthorize("hasPermission(#regionWidgetId, 'org.apache.rave.model.RegionWidget', 'delete')")
    Region removeWidgetFromPage(String regionWidgetId);

    /**
     * Updates the page properties.
     *
     * @param pageId         the id of the page to update
     * @param name           the new name for the page
     * @param pageLayoutCode the new layout for the page
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update')")
    Page updatePage(String pageId, String name, String pageLayoutCode);

    /**
     * Moves a page to be rendered after another page in order for a user
     *
     * @param pageId          the pageId of the page to move
     * @param moveAfterPageId the pageId of the page you want to move after or
     *                        -1 if you want this to be the first page
     * @return the updated Page object containing its new render sequence
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'read') and " +
            "hasPermission(#moveAfterPageId, 'org.apache.rave.model.Page', 'read')")
    Page movePage(String pageId, String moveAfterPageId);

    /**
     * Moves a page to be rendered as the first page for a user
     *
     * @param pageId the pageId of the page to move to the default position
     * @return the updated Page object containing its new render sequence
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'read')")
    Page movePageToDefault(String pageId);

    /**
     * Clone the page and then share it with another user (Page is detached from original)
     *
     * @param pageId   - the id of the page in question
     * @param userId   - the userid to add
     * @param pageName = name of the page
     * @return true or false whether the user was added
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update')")
    Boolean clonePageForUser(String pageId, String userId, String pageName);

    /**
     * Add another user to share this page with
     *
     * @param pageId - the id of the page in question
     * @param userId - the userid to add
     * @return true or false whether the user was added
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update')")
    Boolean addMemberToPage(String pageId, String userId);

    /**
     * Remove an existing user from the page share
     *
     * @param pageId - the id of the page in question
     * @param userId - the userid to add
     * @return -  whether the user was successfully removed or not
     *         Note: this is read access because a page shared to a non page owner means they have 'read' access
     *         unless they are set as an 'editor' of the page, which means 'update' access.
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'read')")
    Boolean removeMemberFromPage(String pageId, String userId);

    /**
     * Allows a user to accept or decline a page share
     *
     * @param pageId      - the id of the page in question
     * @param shareStatus - a string value defined in PageStatus
     * @return
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'read')")
    Boolean updateSharedPageStatus(String pageId, String shareStatus);

    /**
     * Allows a user to accept or decline a page share
     *
     * @param pageId      - the id of the page in question
     * @param shareStatus - a string value defined in PageStatus
     * @return
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'read')")
    Boolean updateSharedPageStatus(String pageId, String userId, String shareStatus);

    /**
     * Update a user who has shared access to a given page, so that they can edit the page (true)
     * or can only view it (false)
     *
     * @param pageId   - the id of the page in question
     * @param userId   - the userId of the user to assign the permission to
     * @param isEditor - value to denote if the user can edit or not
     * @return
     */
    @PreAuthorize("hasPermission(#pageId, 'org.apache.rave.model.Page', 'update')")
    Boolean updatePageEditingStatus(String pageId, String userId, boolean isEditor);

}