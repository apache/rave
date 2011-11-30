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

import org.apache.commons.lang.StringUtils;
import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DefaultWidgetService implements WidgetService {

    private final WidgetRepository widgetRepository;
    private final UserRepository userRepository;

    @Autowired
    public DefaultWidgetService(WidgetRepository widgetRepository, UserRepository userRepository) {
        this.widgetRepository = widgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SearchResult<Widget> getAllWidgets() {
        final int count = widgetRepository.getCountAll();
        final List<Widget> widgets = widgetRepository.getAll();
        return new SearchResult<Widget>(widgets, count);
    }

    @Override
    public SearchResult<Widget> getLimitedListOfWidgets(int offset, int pageSize) {
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
            return getLimitedListOfWidgets(offset, pageSize);
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
    public Widget getWidget(long id) {
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
    public SearchResult<Widget> getWidgetsByOwner(Long ownerId, int offset, int pageSize) {
        final User user = userRepository.get(ownerId);
        final int count = widgetRepository.getCountByOwner(user, offset, pageSize);
        final List<Widget> widgets = widgetRepository.getByOwner(user, offset, pageSize);
        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets,  count);
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
    public WidgetStatistics getWidgetStatistics(long widgetId, long userId) {
        return widgetRepository.getWidgetStatistics(widgetId, userId);
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        return widgetRepository.getAllWidgetStatistics(userId);
    }

    @Override
    @Transactional
    public void updateWidget(Widget widget) {
        widgetRepository.save(widget);
    }

}
