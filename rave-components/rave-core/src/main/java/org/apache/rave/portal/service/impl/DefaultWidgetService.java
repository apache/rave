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

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.model.WidgetTag;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultWidgetService implements WidgetService {

    private final WidgetRepository widgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DefaultWidgetService(WidgetRepository widgetRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.widgetRepository = widgetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public SearchResult<Widget> getAll() {
        final int count = widgetRepository.getCountAll();
        final List<Widget> widgets = widgetRepository.getAll();
        return new SearchResult<Widget>(widgets, count);
    }

    @Override
    public SearchResult<Widget> getLimitedList(int offset, int pageSize) {
        final int count = widgetRepository.getCountAll();
        final List<Widget> widgets = widgetRepository.getLimitedList(offset, pageSize);
        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<Widget> getWidgetsByFreeTextSearch(String searchTerm,
                                                           int offset, int pageSize) {
        if (StringUtils.isBlank(searchTerm)) {
            return getLimitedList(offset, pageSize);
        }

        final int count = widgetRepository.getCountFreeTextSearch(searchTerm);
        final List<Widget> widgets = widgetRepository.getByFreeTextSearch(searchTerm,
                offset, pageSize);

        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public Widget getWidget(String id) {
        return widgetRepository.get(id);
    }

    @Override
    public SearchResult<Widget> getPublishedWidgets(int offset, int pageSize) {
        final int count = widgetRepository.getCountByStatus(WidgetStatus.PUBLISHED);
        final List<Widget> widgets = widgetRepository.getByStatus(WidgetStatus.PUBLISHED,
                offset, pageSize);
        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<Widget> getPublishedWidgetsByFreeTextSearch(String searchTerm,
                                                                    int offset, int pageSize) {
        return getWidgetsBySearchCriteria(searchTerm, null, WidgetStatus.PUBLISHED.getWidgetStatus(), offset, pageSize);
    }

    @Override
    public SearchResult<Widget> getWidgetsBySearchCriteria(String searchTerm, String widgetType, String widgetStatus,
                                                           int offset, int pageSize) {

        final WidgetStatus status = StringUtils.isBlank(widgetStatus) ? null : WidgetStatus.get(widgetStatus);
        final int count = widgetRepository.getCountByStatusAndTypeAndFreeText(status, widgetType, searchTerm);
        final List<Widget> widgets = widgetRepository.getByStatusAndTypeAndFreeTextSearch(status, widgetType,
                searchTerm, offset, pageSize);
        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<Widget> getWidgetsByOwner(String ownerId, int offset, int pageSize) {
        final User user = userRepository.get(ownerId);
        final int count = widgetRepository.getCountByOwner(user, offset, pageSize);
        final List<Widget> widgets = widgetRepository.getByOwner(user, offset, pageSize);
        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public Widget getWidgetByUrl(String widgetUrl) {
        return widgetRepository.getByUrl(widgetUrl);
    }

    @Override
    public boolean isRegisteredUrl(String widgetUrl) {
        return widgetRepository.getByUrl(widgetUrl) != null;
    }

    @Override
    @Transactional
    public Widget registerNewWidget(Widget widget) {
        if (getWidgetByUrl(widget.getUrl()) != null) {
            throw new DuplicateItemException("Trying to add an existing widget for url " + widget.getUrl());
        }
        return widgetRepository.save(widget);
    }

    @Override
    public WidgetStatistics getWidgetStatistics(String widgetId, String userId) {
        return widgetRepository.getWidgetStatistics(widgetId, userId);
    }

    @Override
    public Map<String, WidgetStatistics> getAllWidgetStatistics(String userId) {
        return widgetRepository.getAllWidgetStatistics(userId);
    }

    @Override
    @Transactional
    public void updateWidget(Widget widget) {
        widgetRepository.save(widget);
    }

    @Override
    public SearchResult<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {

        int count = widgetRepository.getCountByTag(tagKeyWord);
        List<Widget> widgets = widgetRepository.getWidgetsByTag(tagKeyWord, offset, pageSize);
        SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<Widget> getWidgetsByCategory(String categoryId, int offset, int pageSize) {
        Category category = categoryRepository.get(categoryId);
        if (category == null) {
            return new SearchResult<Widget>(new ArrayList<Widget>(), 0);
        }

        List<Widget> widgets = category.getWidgets();
        SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, widgets.size());
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public WidgetTag getWidgetTag(String id) {
        return widgetRepository.getTagById(id);
    }


    @Override
    @Transactional
    public WidgetTag createWidgetTag(String widgetId, WidgetTag widgetTag) {
        return widgetRepository.saveWidgetTag(widgetId, widgetTag);
    }

    @Override
    public WidgetTag getWidgetTagByWidgetIdAndKeyword(String widgetId, String keyword) {
        return widgetRepository.getTagByWidgetIdAndKeyword(widgetId, keyword);
    }

    // ***************************************************************************************************************
    // Widget Comment Methods
    // ***************************************************************************************************************

    @Override
    public WidgetComment getWidgetComment(String widgetId, String commentId) {
        return widgetRepository.getCommentById(widgetId, commentId);
    }

    @Override
    @Transactional
    public void createWidgetComment(String widgetId, WidgetComment widgetComment) {
        widgetRepository.createWidgetComment(widgetId, widgetComment);
    }

    @Override
    @Transactional
    public void updateWidgetComment(String widgetId, WidgetComment widgetComment) {
        widgetRepository.updateWidgetComment(widgetId, widgetComment);
    }

    @Override
    @Transactional
    public void removeWidgetComment(String widgetId, String commentId) {
        widgetRepository.deleteWidgetComment(widgetId, getWidgetComment(widgetId, commentId));
    }

    @Override
    @Transactional
    public int deleteAllWidgetComments(String userId) {
        return widgetRepository.deleteAllWidgetComments(userId);
    }

    @Override
    public WidgetRating getWidgetRatingByWidgetIdAndUserId(String widgetId, String userId) {
        return widgetRepository.getWidgetRatingsByWidgetIdAndUserId(widgetId, userId);
    }

    @Override
    @Transactional
    public void updateWidgetRatingScore(String widgetId, WidgetRating widgetRating, Integer score) {
        widgetRating.setScore(score);
        widgetRepository.updateWidgetRating(widgetId, widgetRating);
    }

    @Override
    @Transactional
    public void saveWidgetRating(String widgetId, WidgetRating rating) {
        WidgetRating existingRating = getWidgetRatingByWidgetIdAndUserId(widgetId, rating.getUserId());
        if (existingRating == null) {
            widgetRepository.createWidgetRating(widgetId, rating);
        } else {
            updateWidgetRatingScore(widgetId, existingRating, rating.getScore());
        }
    }

    @Override
    @Transactional
    public void removeWidgetRating(String widgetId, String userId) {
        WidgetRating widgetRating = widgetRepository.getWidgetRatingsByWidgetIdAndUserId(widgetId, userId);
        if (widgetRating == null) {
            return;
        }
        widgetRepository.deleteWidgetRating(widgetId, widgetRating);
    }

    @Override
    @Transactional
    public int removeAllWidgetRatings(String userId) {
        return widgetRepository.deleteAllWidgetRatings(userId);
    }
}
