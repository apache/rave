/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateLongId;
import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbPageConverter implements HydratingModelConverter<Page, MongoDbPage> {

    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Override
    public Class<Page> getSourceType() {
        return Page.class;
    }

    @Override
    public MongoDbPage convert(Page sourcePage) {
        MongoDbPage page = new MongoDbPage();
        page.setId(sourcePage.getId() == null ? generateId() : sourcePage.getId());
        page.setOwnerId(sourcePage.getOwnerId());
        page.setContextId(sourcePage.getContextId());
        page.setPageLayoutCode(sourcePage.getPageLayout().getCode());
        page.setName(sourcePage.getName());
        page.setRegions(sourcePage.getRegions());
        //Enforce consistent casing for page types
        page.setPageType(sourcePage.getPageType() == null ? null : sourcePage.getPageType().toUpperCase());

        page.setPageLayout(null);
        page.setParentPage(null);

        List<PageUser> convertedMembers = Lists.newArrayList();
        for (PageUser user : sourcePage.getMembers()) {
            convertedMembers.add(convert(user));
        }
        page.setMembers(convertedMembers);

        List<Region> convertedRegions = Lists.newArrayList();
        for (Region region : page.getRegions()) {
            convertedRegions.add(convert(region));
        }
        page.setRegions(convertedRegions);
        if (sourcePage.getSubPages() != null) {
            List<Page> convertedPages = Lists.newArrayList();
            for (Page subPage : sourcePage.getSubPages()) {
                if(subPage.getId() == null) {
                    subPage.setId(generateId());
                }
                convertedPages.add(convert(subPage));
            }
            page.setSubPages(convertedPages);
        }
        return page;
    }

    public PageUserImpl convert(PageUser sourceUser) {
        PageUserImpl user = sourceUser instanceof PageUserImpl ? (PageUserImpl) sourceUser : new PageUserImpl();
        user.setId(sourceUser.getId() == null ? generateId() : sourceUser.getId());
        user.setUserId(sourceUser.getUserId());
        user.setEditor(sourceUser.isEditor());
        user.setPageStatus(sourceUser.getPageStatus());
        user.setRenderSequence(sourceUser.getRenderSequence());
        user.setPage(null);
        return user;
    }

    @Override
    public void hydrate(MongoDbPage page) {
        if (page == null) {
            return;
        }
        page.setPageLayout(pageLayoutRepository.getByPageLayoutCode(page.getPageLayoutCode()));

        for (PageUser user : page.getMembers()) {
            user.setPage(page);
        }
        for (Region region : page.getRegions()) {
            region.setPage(page);
            hydrate(region);
        }
        if (page.getSubPages() != null) {
            for (Page subPage : page.getSubPages()) {
                subPage.setParentPage(page);
                if (subPage instanceof MongoDbPage) {
                    hydrate((MongoDbPage) subPage);
                }
            }
        }
    }

    public void hydrate(RegionWidgetImpl widget, Region region) {
        widget.setRegion(region);
    }

    public RegionWidgetImpl convert(RegionWidget sourceRegionWidget) {
        RegionWidgetImpl regionWidget = sourceRegionWidget instanceof RegionWidgetImpl ? (RegionWidgetImpl) sourceRegionWidget : new RegionWidgetImpl();
        //RegionWidgetIds MUST be a Long due to the mapping of ModuleID in Shindig.
        regionWidget.setId(sourceRegionWidget.getId() == null ? generateLongId().toString() : sourceRegionWidget.getId());
        regionWidget.setWidgetId(sourceRegionWidget.getWidgetId());
        regionWidget.setRegion(null);
        regionWidget.setPreferences(sourceRegionWidget.getPreferences());
        updatePreferences(regionWidget);
        updateProperties(sourceRegionWidget, regionWidget);
        return regionWidget;
    }

    private void updatePreferences(RegionWidgetImpl regionWidget) {
        List<RegionWidgetPreference> converted = Lists.newArrayList();
        if (regionWidget.getPreferences() != null) {
            for (RegionWidgetPreference preference : regionWidget.getPreferences()) {
                converted.add(convert(preference));
            }
        }
        regionWidget.setPreferences(converted);
    }

    private RegionWidgetPreference convert(RegionWidgetPreference preference) {
        RegionWidgetPreference converted = new RegionWidgetPreferenceImpl();
        converted.setName(preference.getName());
        converted.setValue(preference.getValue());
        return converted;
    }

    private void hydrate(Region region) {
        if (region.getRegionWidgets() == null) {
            region.setRegionWidgets(Lists.<RegionWidget>newArrayList());
        } else {
            for (RegionWidget regionWidget : region.getRegionWidgets()) {
                hydrate((RegionWidgetImpl) regionWidget, region);
            }
        }
    }

    private Region convert(Region region) {
        String regionId = region.getId() == null ? generateId() : region.getId();
        Region converted = new RegionImpl(regionId, null, region.getRenderOrder());
        converted.setLocked(region.isLocked());
        if (region.getRegionWidgets() != null) {
            List<RegionWidget> convertedWidgets = Lists.newArrayList();
            for (RegionWidget widget : region.getRegionWidgets()) {
                convertedWidgets.add(convert(widget));
            }
            converted.setRegionWidgets(convertedWidgets);
        }
        return converted;
    }

    private void updateProperties(RegionWidget source, RegionWidget converted) {
        converted.setLocked(source.isLocked());
        converted.setCollapsed(source.isCollapsed());
        converted.setHideChrome(source.isHideChrome());
        converted.setRenderPosition(source.getRenderPosition());
        converted.setRenderOrder(source.getRenderOrder());
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }
}
