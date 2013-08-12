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

import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.model.WidgetTag;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

/**
 * Provides widget operations
 */
public interface WidgetService {

    /**
     * Gets a {@link SearchResult} for {@link Widget}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<Widget> getAll();


    /**
     * Gets a limited {@link SearchResult} for {@link Widget}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getLimitedList(int offset, int pageSize);

    /**
     * Gets a SearchResult for {@link Widget}'s by performing a free text search
     *
     * @param searchTerm free text search term
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsByFreeTextSearch(String searchTerm, int offset, int pageSize);

    /**
     * Gets the detailed metadata for a widget
     *
     * @param id the Id of the widget
     * @return a valid widget if one exists for the given id; null otherwise
     */
    @PostAuthorize("returnObject == null or hasPermission(returnObject, 'read')")
    Widget getWidget(String id);

    /**
     * Gets a {@link SearchResult} for {@link Widget}'s that are published
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getPublishedWidgets(int offset, int pageSize);

    /**
     * Gets a SearchResult for published {@link Widget}'s by performing a free text search
     *
     * @param searchTerm free text search term
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getPublishedWidgetsByFreeTextSearch(String searchTerm,
                                                             int offset, int pageSize);

    /**
     * Gets a SearchResult for {@link Widget}'s that match the free text search, widget type and status
     *
     * @param searchTerm   free text search term
     * @param widgetType   type of Widget
     * @param widgetStatus status of the Widget, should match a value in {@link org.apache.rave.model.WidgetStatus}
     * @param offset       start point within the resultset (for paging)
     * @param pageSize     maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsBySearchCriteria(String searchTerm, String widgetType, String widgetStatus,
                                                    int offset, int pageSize);

    SearchResult<Widget> getWidgetsByOwner(String ownerId, int offset, int pageSize);

    /**
     * Gets a Widget by its (unique) url
     *
     * @param widgetUrl url of the Widget
     * @return {@link Widget} if it exists, otherwise {@literal null}
     */
//    @PostAuthorize("hasPermission(returnObject, 'read')")
    Widget getWidgetByUrl(String widgetUrl);

    /**
     * Checks if there is already a {@link Widget} for the given url. Does not need authorization.
     *
     * @param widgetUrl url of a widget definition
     * @return {@literal true} if it exists, otherwise {@literal false}
     */
    boolean isRegisteredUrl(String widgetUrl);

    /**
     * Persists a new {@link Widget} if it is not already present in the store
     *
     * @param widget new Widget to store
     * @return Widget if it is new and can be stored
     */
    @PostAuthorize("hasPermission(returnObject, 'create')")
    Widget registerNewWidget(Widget widget);

    /**
     * Generates the widget statistics for a gadget including the user's specific information.
     *
     * @param widgetId id of the widget
     * @param userId id of the user
     * @return {@link WidgetStatistics} with the rating information
     */
    WidgetStatistics getWidgetStatistics(String widgetId, String userId);

    /**
     * Generates the mapping of widget statistics for the user.
     *
     * @param userId id of the user
     * @return Mapping of {@link WidgetStatistics} objects keyed off of the widget's entityId
     */
    Map<String, WidgetStatistics> getAllWidgetStatistics(String userId);

    /**
     * Updates {@link Widget}
     *
     * @param widget to save
     */
    @PreAuthorize("hasPermission(#widget.id, 'org.apache.rave.model.Widget', 'update')")
    void updateWidget(Widget widget);

/**
     * Gets a SearchResult for {@link Widget}'s by performing a tag keyword search
     *
     * @param tagKeyWord  tag keyword
     * @param offset     start point within the resultSet (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize);

    /**
     * Get a SearchResult for {@link Widget}s by category
     * 
     * @param categoryId category to search against
     * @param offset     start point within the resultSet (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return           SearchResult
     */
    SearchResult<Widget> getWidgetsByCategory(String categoryId, int offset, int pageSize);

    // ***************************************************************************************************************
    // Widget Tag Methods
    // ***************************************************************************************************************

    WidgetTag getWidgetTag(String id);

    WidgetTag getWidgetTagByWidgetIdAndKeyword(String widgetId, String keyword);

    // No security check required as everyone is allowed to create a tag
    WidgetTag createWidgetTag(String widgetId, WidgetTag widgetTag);

    // ***************************************************************************************************************
    // Widget Comment Methods
    // ***************************************************************************************************************

    @PostAuthorize("hasPermission(returnObject, 'read')")
    WidgetComment getWidgetComment(String widgetId, String id);

    // No security check required as everyone is allowed to create a comment
    void createWidgetComment(String widgetId, WidgetComment widgetComment);

    @PreAuthorize("hasPermission(#widgetComment, 'update')")
    void updateWidgetComment(String widgetId, WidgetComment widgetComment);

    @PreAuthorize("hasPermission(#commentId, 'org.apache.rave.model.WidgetComment', 'delete')")
    void removeWidgetComment(String widgetId, String commentId);

    /**
     * Deletes all Widget Comments for a userId
     *
     * @param userId
     * @return number of comments deleted
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.WidgetComment', 'delete')")
    int deleteAllWidgetComments(String userId);

    // ***************************************************************************************************************
    // Widget Ratings Methods
    // ***************************************************************************************************************

    /**
     * Gets a {@link org.apache.rave.model.WidgetRating} for the widgetId and userId
     *
     * @param widgetId unique identifier for a Widget
     * @param userId   unique identifier for a User
     * @return WidgetRating or {@link null} if there is no such combination
     */
    WidgetRating getWidgetRatingByWidgetIdAndUserId(String widgetId, String userId);

    /**
     * Updates the score of a {@link org.apache.rave.model.WidgetRating}
     *
     * @param widgetId Widget ID
     * @param widgetRating WidgetRating
     * @param score        value of the rating
     */
    @PreAuthorize("hasPermission(#widgetRating.entityId, 'org.apache.rave.model.WidgetRating', 'update')")
    void updateWidgetRatingScore(String widgetId, WidgetRating widgetRating, Integer score);

    /**
     * Saves a {@link org.apache.rave.model.WidgetRating} for a widget
     *
     * @param rating   WidgetRating
     */
    void saveWidgetRating(String widgetId, WidgetRating rating);

    /**
     * Removes the rating of a widget
     *
     * @param widgetId unique identifier of a {@link org.apache.rave.model.Widget}
     * @param userId   unique identifier of a {@link org.apache.rave.model.User}
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.WidgetRating', 'delete')")
    void removeWidgetRating(String widgetId, String userId);

    /**
     * Removes all widget ratings for a userId, for all widgets
     *
     * @param userId
     * @return the number of widget ratings deleted
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.model.User'), 'org.apache.rave.model.WidgetRating', 'delete')")
    int removeAllWidgetRatings(String userId);
}
