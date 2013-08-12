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

package org.apache.rave.rest.impl;


import org.apache.rave.portal.service.PageService;
import org.apache.rave.rest.exception.BadRequestException;
import org.apache.rave.rest.exception.ResourceNotFoundException;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.RegionWidgetsResource;
import org.apache.rave.rest.model.RegionWidget;
import org.apache.rave.rest.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class DefaultRegionWidgetsResource implements RegionWidgetsResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;

    private Page page;
    private Region region;

    public void setPage(Page page) {
        this.page = page;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public SearchResult<RegionWidget> getPageRegionRegionWidgets() {
        List<RegionWidget> regionWidgets = region.getRegionWidgets();
        SearchResult<RegionWidget> results = new SearchResult<RegionWidget>(regionWidgets, regionWidgets.size());

        return results;
    }

    @Override
    public RegionWidget createPageRegionRegionWidget(RegionWidget regionWidget) {
        if(regionWidget.getWidgetId() == null) {
            throw new BadRequestException("RegionWidget widgetId property must be defined.");
        }
        org.apache.rave.model.RegionWidget fromDb =
                pageService.addWidgetToPageRegion(page.getId(), regionWidget.getWidgetId(), region.getId());

        return new RegionWidget(fromDb);
    }

    @Override
    public RegionWidget getPageRegionRegionWidget(String regionWidgetId) {
        List<RegionWidget> regionWidgets = region.getRegionWidgets();
        RegionWidget match = null;
        for(RegionWidget widget: regionWidgets) {
            if(widget.getId().equals(regionWidgetId)) {
                match = widget;
                break;
            }
        }

        if(match == null) {
            throw new ResourceNotFoundException(regionWidgetId);
        }

        return match;
    }

    @Override
    public RegionWidget updatePageRegionRegionWidget(String regionWidgetId, RegionWidget regionWidget) {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RegionWidget deletePageRegionRegionWidget(String regionWidgetId) {
        pageService.removeWidgetFromPage(regionWidgetId);
        return null;
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }
}
