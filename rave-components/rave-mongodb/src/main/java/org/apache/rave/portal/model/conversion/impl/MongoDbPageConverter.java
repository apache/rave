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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbPageConverter implements HydratingModelConverter<Page, MongoDbPage> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Override
    public Class<Page> getSourceType() {
        return Page.class;
    }

    @Override
    public MongoDbPage convert(Page sourcePage) {
        MongoDbPage page = new MongoDbPage();
        page.setId(page.getId() == null ? generateId() : sourcePage.getId());
        page.setOwnerId(sourcePage.getOwner().getId());
        page.setPageLayoutCode(sourcePage.getPageLayout().getCode());
        page.setName(sourcePage.getName());
        page.setRegions(sourcePage.getRegions());
        page.setPageType(sourcePage.getPageType());

        page.setOwner(null);
        page.setPageLayout(null);
        page.setParentPage(null);
        page.setUserRepository(null);

        List<PageUser> convertedMembers = Lists.newArrayList();
        for (PageUser user : sourcePage.getMembers()) {
            convertedMembers.add(convert(user));
        }
        page.setMembers(convertedMembers);

        //No need to convert regions to anything special at this time
        for (Region region : page.getRegions()) {
            if (region.getId() == null) {
                region.setId(generateId());
            }
            region.setPage(null);
            convert(region);
        }
        if (sourcePage.getSubPages() != null) {
            List<Page> convertedPages = Lists.newArrayList();
            for (Page subPage : sourcePage.getSubPages()) {
                convertedPages.add(convert(subPage));
            }
            page.setSubPages(convertedPages);
        }
        return page;
    }

    public MongoDbPageUser convert(PageUser sourceUser) {
        MongoDbPageUser user = sourceUser instanceof MongoDbPageUser ? (MongoDbPageUser) sourceUser : new MongoDbPageUser();
        user.setId(sourceUser.getId() == null ? generateId() : sourceUser.getId());
        user.setUserId(sourceUser.getUser().getId());
        user.setEditor(sourceUser.isEditor());
        user.setPageStatus(sourceUser.getPageStatus());
        user.setRenderSequence(sourceUser.getRenderSequence());
        user.setPage(null);
        user.setUser(null);
        user.setUserRepository(null);
        return user;
    }

    @Override
    public void hydrate(MongoDbPage page) {
        if (page == null) {
            return;
        }
        page.setPageLayout(pageLayoutRepository.getByPageLayoutCode(page.getPageLayoutCode()));
        page.setUserRepository(userRepository);

        for (PageUser user : page.getMembers()) {
            user.setPage(page);
            if (user instanceof MongoDbPageUser) {
                hydrate((MongoDbPageUser) user);
            }
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


    public void hydrate(MongoDbPageUser user) {
        user.setUserRepository(userRepository);
    }

    public void hydrate(MongoDbRegionWidget widget, Region region) {
        widget.setRegion(region);
        widget.setWidgetRepository(widgetRepository);
    }

    public MongoDbRegionWidget convert(RegionWidget sourceRegionWidget) {
        MongoDbRegionWidget regionWidget = sourceRegionWidget instanceof MongoDbRegionWidget ? (MongoDbRegionWidget) sourceRegionWidget : new MongoDbRegionWidget();
        regionWidget.setId(sourceRegionWidget.getId() == null ? generateId() : sourceRegionWidget.getId());
        regionWidget.setWidgetId(sourceRegionWidget.getWidget().getId());
        regionWidget.setWidget(null);
        regionWidget.setWidgetRepository(null);
        regionWidget.setRegion(null);
        regionWidget.setPreferences(sourceRegionWidget.getPreferences());
        updatePreferences(regionWidget);
        updateProperties(sourceRegionWidget, regionWidget);
        return regionWidget;
    }

    private void updatePreferences(MongoDbRegionWidget regionWidget) {
        List<RegionWidgetPreference> converted = Lists.newArrayList();
        if(regionWidget.getPreferences() != null) {
            for(RegionWidgetPreference preference : regionWidget.getPreferences()) {
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
        for (RegionWidget regionWidget : region.getRegionWidgets()) {
            hydrate((MongoDbRegionWidget) regionWidget, region);
        }
    }

    private void convert(Region region) {
        List<RegionWidget> convertedWidgets = Lists.newArrayList();
        if (region.getRegionWidgets() != null) {
            for (RegionWidget widget : region.getRegionWidgets()) {
                convertedWidgets.add(convert(widget));
            }
        }
        region.setRegionWidgets(convertedWidgets);
    }

    private void updateProperties(RegionWidget source, RegionWidget converted) {
        converted.setLocked(source.isLocked());
        converted.setCollapsed(source.isCollapsed());
        converted.setHideChrome(source.isHideChrome());
        converted.setRenderPosition(source.getRenderPosition());
        converted.setRenderOrder(source.getRenderOrder());
    }

}
