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

import org.apache.rave.repository.Repository;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;


import java.util.List;
import java.util.Map;


public interface WidgetRepository extends Repository<Widget> {
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
    WidgetStatistics getWidgetStatistics(String widget_id, String user_id);

    /**
     * Generates the mapping of widget statistics for the user.
     *
     * @param userId id of the user
     * @return Mapping of {@link WidgetStatistics} objects keyed off of the widget's entityId
     */
    Map<String, WidgetStatistics> getAllWidgetStatistics(String userId);

    /**
     * Generates the mapping of widget ratings for the user.
     *
     * @param userId id of the user
     * @return Mapping of {@link org.apache.rave.model.WidgetRating} objects keyed off of the widget's entityId
     */
    Map<String, WidgetRating> getUsersWidgetRatings(String userId);

    /**
	  * Gets a List of {@link Widget}'s by performing a tag search
	  *
     * @param tagKeyWord free text tag keyword
     * @param offset     start point within the resultSet (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @return valid list of widgets, can be empty
     */
     List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize);

    /**
     * Counts the total number of {@link Widget}'s that match tag keyword. Useful for paging.
     *
     * @param  tagKeyword  tag keyword text
     * @return total number of {@link Widget}'s that match tag key word
     */
     int getCountByTag(String tagKeyword);

    /**
     * Sets the widget owner to null for any widgets owned by the supplied userId
     *
     * @param userId the widget owner to unassign
     * @return the number of widgets updated
     */
     int unassignWidgetOwner(String userId);

    // ***************************************************************************************************************
    // Widget Tag Methods
    // ***************************************************************************************************************

    /**
     * Tries to find a {@link org.apache.rave.model.WidgetTag} by the id's of a Widget and Tag keyword
     *
     * @param widgetId unique identifier of a Widget
     * @param keyword   tag's keyword
     * @return {@link org.apache.rave.model.WidgetTag} if it exists, otherwise {@literal null}
     */
     WidgetTag getTagByWidgetIdAndKeyword(String widgetId, String keyword);

    /**
     * Tries to find a {@link org.apache.rave.model.WidgetTag} by the id of the Tag
     *
     * @param widgetTagId   tag's id
     * @return {@link org.apache.rave.model.WidgetTag} if it exists, otherwise {@literal null}
     */
    WidgetTag getTagById(String widgetTagId);

    WidgetTag saveWidgetTag(String widgetId, WidgetTag tag);

    void deleteWidgetTag(WidgetTag tag);

    // ***************************************************************************************************************
    // Widget Comment Methods
    // ***************************************************************************************************************

    WidgetComment getCommentById(String widgetId, String widgetCommentId);

    WidgetComment createWidgetComment(String widgetId, WidgetComment comment);

    WidgetComment updateWidgetComment(String widgetId, WidgetComment comment);

    void deleteWidgetComment(String widgetId, WidgetComment comment);

    /**
     * Delete all Widget Comments for a userId
     *
     * @param userId
     * @return count of comments deleted
     */
    int deleteAllWidgetComments(String userId);

    // ***************************************************************************************************************
    // Widget Ratings Methods
    // ***************************************************************************************************************

    WidgetRating getRatingById(String widgetId, String widgetRatingId);

    WidgetRating createWidgetRating(String widgetId, WidgetRating rating);

    WidgetRating updateWidgetRating(String widgetId, WidgetRating rating);

    void deleteWidgetRating(String widgetId, WidgetRating rating);

    /**
     * Tries to find a {@link org.apache.rave.model.WidgetRating} by the id's of a Widget and USer
     *
     * @param widgetId unique identifier of a Widget
     * @param userId   unique identifier of a User
     * @return {@link org.apache.rave.model.WidgetRating} if it exists, otherwise {@literal null}
     */
    WidgetRating getWidgetRatingsByWidgetIdAndUserId(String widgetId, String userId);

    /**
     * Delete all Widget Ratings for a userId
     *
     * @param userId
     * @return count of ratings deleted
     */
    int deleteAllWidgetRatings(String userId);
}
