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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultWidgetService implements WidgetService {

    private final WidgetRepository widgetRepository;

    @Autowired
    public DefaultWidgetService(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
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
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<Widget> getPublishedWidgetsByFreeTextSearch(String searchTerm,
                                                                    int offset, int pageSize) {

        if (StringUtils.isBlank(searchTerm)) {
            return getPublishedWidgets(offset, pageSize);
        }

        final int count = widgetRepository.getCountByStatusAndFreeText(WidgetStatus.PUBLISHED,
                searchTerm);
        final List<Widget> widgets = widgetRepository.getByStatusAndFreeTextSearch(
                WidgetStatus.PUBLISHED, searchTerm, offset, pageSize);

        final SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets, count);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }
}
