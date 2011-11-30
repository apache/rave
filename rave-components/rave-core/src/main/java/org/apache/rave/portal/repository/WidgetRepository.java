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
package org.apache.rave.portal.repository;

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.WidgetStatistics;

import java.util.List;
import java.util.Map;


public interface WidgetRepository extends Repository<Widget> {
    /**
     * Gets a list of <strong>all</strong> widgets in the repository
     *
     * @return a valid List
     */
    List<Widget> getAll();

    /**
     * List of Widgets with the same condition as in {@link #getAll()}
     * but with a limited amount of Widgets.
     *
     * @param offset   start point within the total resultset
     * @param pageSize maximum number of items to be returned (for paging)
     * @return a List of widgets with of at most the number of items in pageSize
     */
    List<Widget> getLimitedList(int offset, int pageSize);

    /**
     * @return the total number of {@link Widget}'s in the repository. Useful for paging.
     */
    int getCountAll();

    /**
     * Gets a List of {@link Widget}'s by performing a free text search
     *
     * @param searchTerm free text search term
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return valid list of widgets, can be empty
     */
    List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize);

    /**
     * Counts the total number of {@link Widget}'s that match the search term. Useful for paging.
     *
     * @param searchTerm free text search term
     * @return total number of {@link Widget}'s that match the search term
     */
    int getCountFreeTextSearch(String searchTerm);

    /**
     * Gets a List of {@link Widget}'s in the repository by {@link WidgetStatus}
     *
     * @param widgetStatus status of the widget (PREVIEW, PUBLISHED etc)
     * @param offset       start point within the total resultset
     * @param pageSize     maximum number of items to be returned (for paging)
     * @return valid list of widgets, can be empty
     */
    List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize);

    /**
     * Counts the total number of {@link Widget}'s filtering on status. Useful for paging.
     *
     * @param widgetStatus status of the widget (PREVIEW, PUBLISHED etc)
     * @return total number of {@link Widget}'s that match the search term
     */
    int getCountByStatus(WidgetStatus widgetStatus);

    /**
     * Gets a List of {@link Widget}'s by performing a free text search filtering on status
     *
     * @param widgetStatus status of the widget (PREVIEW, PUBLISHED etc)
     * @param type         type of Widget (e.g. W3C, OpenSocial)
     * @param searchTerm   free text search term
     * @param offset       start point within the resultset (for paging)
     * @param pageSize     maximum number of items to be returned (for paging)
     * @return valid list of widgets, can be empty
     */
    List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm,
                                                     int offset, int pageSize);


    /**
     * Counts the total number of {@link Widget}'s by performing a free text search filtering
     * on status. Useful for paging.
     *
     * @param widgetStatus status of the widget (PREVIEW, PUBLISHED etc)
     * @param type         type of Widget (e.g. W3C, OpenSocial)
     * @param searchTerm   free text search term
     * @return total number of {@link Widget}'s that match the search criteria
     */
    int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm);

    List<Widget> getByOwner(User owner, int offset, int pageSize);

    int getCountByOwner(User owner, int offset, int pageSize);

    /**
     * Searches for a Widget by its url
     *
     * @param widgetUrl (unique) url of the Widget
     * @return {@link Widget} if it can be found, otherwise {@literal null}
     */
    Widget getByUrl(String widgetUrl);
    
    /**
     * Generates the widget statistics for a gadget including the user's specific information.
     * 
     * @param widget_id id of the widget
     * @param user_id id of the user
     * @return {@link WidgetStatistics} with the rating information
     */
    WidgetStatistics getWidgetStatistics(long widget_id, long user_id);

    /**
     * Generates the mapping of widget statistics for the user.
     *
     * @param userId id of the user
     * @return Mapping of {@link WidgetStatistics} objects keyed off of the widget's entityId
     */
    Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId);

    /**
     * Generates the mapping of widget ratings for the user.
     *
     * @param userId id of the user
     * @return Mapping of {@link WidgetRating} objects keyed off of the widget's entityId
     */
    Map<Long, WidgetRating> getUsersWidgetRatings(long userId);
}