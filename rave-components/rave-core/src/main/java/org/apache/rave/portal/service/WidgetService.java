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

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;

import java.util.Map;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

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
    SearchResult<Widget> getAllWidgets();


    /**
     * Gets a limited {@link SearchResult} for {@link Widget}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getLimitedListOfWidgets(int offset, int pageSize);

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
    @PostAuthorize("hasPermission(returnObject, 'read')")  
    Widget getWidget(long id);

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
     * @param widgetStatus status of the Widget, should match a value in {@link org.apache.rave.portal.model.WidgetStatus}
     * @param offset       start point within the resultset (for paging)
     * @param pageSize     maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsBySearchCriteria(String searchTerm, String widgetType, String widgetStatus,
                                                    int offset, int pageSize);

    /**
     * Gets a Widget by its (unique) url
     *
     * @param widgetUrl url of the Widget
     * @return {@link Widget} if it exists, otherwise {@literal null}
     */
    @PostAuthorize("hasPermission(returnObject, 'read')")
    Widget getWidgetByUrl(String widgetUrl);


    /**
     * Persists a new {@link Widget} if it is not already present in the store
     *
     * @param widget new Widget to store
     * @return Widget if it is new and can be stored, otherwise {@literal null}
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#widget.owner.entityId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Widget', 'create')")    
    Widget registerNewWidget(Widget widget);
    
    /**
     * Generates the widget statistics for a gadget including the user's specific information.
     * 
     * @param widgetId id of the widget
     * @param userId id of the user
     * @return {@link WidgetStatistics} with the rating information
     */
    WidgetStatistics getWidgetStatistics(long widgetId, long userId);

    /**
     * Generates the mapping of widget statistics for the user.
     * 
     * @param userId id of the user
     * @return Mapping of {@link WidgetStatistics} objects keyed off of the widget's entityId
     */
    Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId);
    
    /**
     * Updates {@link Widget}
     *
     * @param widget to save
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#widget.owner.entityId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.Widget', 'update')")        
    void updateWidget(Widget widget);
}
