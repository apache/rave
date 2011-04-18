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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.InternationalString;
import org.apache.rave.portal.model.util.LocalizedString;
import org.apache.rave.portal.repository.PageRepository;
import org.springframework.stereotype.Repository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class JpaPageRepository implements PageRepository {

    @Override
    public List<Page> getAllPages(String userId) {
        /**************************************************************************************************************
         ***************************************************************************************************************
         TODO: Returning a static set of mock data here while we decide if this model looks right - if it does we can go
         ahead and add JPA annotations to our models and start using real persistence.
         ***************************************************************************************************************
         **************************************************************************************************************/

        long widgetId = 1;
        Region region1 = new Region();
        region1.setId(1L);
        ArrayList<RegionWidget> region1Widgets = new ArrayList<RegionWidget>();
        region1Widgets.add(getRegionWidget("http://www.labpixies.com/campaigns/todo/todo.xml", "LabPixies Todo",
                widgetId++));
        region1Widgets.add(getRegionWidget("http://widgets.nytimes.com/packages/html/igoogle/topstories.xml",
                "The New York Times", widgetId++));
        region1.setRegionWidgets(region1Widgets);

        Region region2 = new Region();
        region2.setId(2L);
        ArrayList<RegionWidget> region2Widgets = new ArrayList<RegionWidget>();
        region2Widgets.add(getRegionWidget(
                "http://www.flightstats.com/go/FlightStatus/AddGoogleHomePageFlightStatus.module", "Flight Status",
                widgetId++));
        region2Widgets.add(getRegionWidget("http://hosting.gmodules.com/ig/gadgets/file/100674619146546250953/wsj.xml",
                "The Wall Street Journal", widgetId++));
        region2.setRegionWidgets(region2Widgets);

        PageLayout pageLayout = new PageLayout();
        pageLayout.setId(1L);
        pageLayout.setCode("one-column");
        pageLayout.setNumberOfRegions(1L);

        Person person = new Person();
        person.setUserId(userId);

        Page page = new Page();
        page.setId(1L);
        page.setName("Default Page");
        page.setOwner(person);
        page.setRenderSequence(1L);
        page.setPageLayout(pageLayout);
        page.setRegions(Arrays.asList(region1, region2));

        return Arrays.asList(page);
    }

    private RegionWidget getRegionWidget(String specificationUrl, String title, Long id) {
        Widget widget = new Widget();
        InternationalString widgetTitle = new InternationalString();
        widgetTitle.setLocalizedString(new LocalizedString(title, "en"));
        widget.setTitle(widgetTitle);
        widget.setId(id);
        try {
            widget.setUrl(new URL(specificationUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setId(id);
        regionWidget.setWidget(widget);
        regionWidget.setRenderPosition(String.valueOf(id));
        regionWidget.setCollapsed(false);
        return regionWidget;
    }
}