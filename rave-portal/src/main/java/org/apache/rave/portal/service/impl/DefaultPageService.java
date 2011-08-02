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

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultPageService implements PageService {
    private final PageRepository pageRepository;
    private final RegionRepository regionRepository;
    private final RegionWidgetRepository regionWidgetRepository;
    private final WidgetRepository widgetRepository;

    @Autowired
    public DefaultPageService(PageRepository pageRepository, RegionRepository regionRepository, WidgetRepository widgetRepository, RegionWidgetRepository regionWidgetRepository) {
        this.pageRepository = pageRepository;
        this.regionRepository = regionRepository;
        this.regionWidgetRepository = regionWidgetRepository;
        this.widgetRepository = widgetRepository;
    }

    @Override
    public List<Page> getAllPages(long userId) {
        return pageRepository.getAllPages(userId);
    }

    @Override
    @Transactional
    public RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegion, long fromRegion) {
        Region target = getFromRepository(toRegion, regionRepository);
        if (toRegion == fromRegion) {
            moveWithinRegion(regionWidgetId, newPosition, target);
        } else {
            moveBetweenRegions(regionWidgetId, newPosition, fromRegion, target);
        }
        target = regionRepository.save(target);
        return findRegionWidgetById(regionWidgetId, target.getRegionWidgets());
    }

    @Override
    @Transactional
    public Region removeWidgetFromPage(long regionWidgetId) {
        RegionWidget widget = getFromRepository(regionWidgetId, regionWidgetRepository);
        regionWidgetRepository.delete(widget);
        return getFromRepository(widget.getRegion().getId(), regionRepository);
    }

    @Override
    @Transactional
    public RegionWidget addWidgetToPage(long pageId, long widgetId) {
        Page page = getFromRepository(pageId, pageRepository);
        Widget widget = getFromRepository(widgetId, widgetRepository);
        Region region = page.getRegions().get(0);
        return createWidgetInstance(widget, region, 0);
    }

    @Override
    @Transactional
    public void registerNewPage(Page page) {
        pageRepository.save(page);
    }

    private RegionWidget createWidgetInstance(Widget widget, Region region, int position) {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setRenderOrder(position);
        regionWidget.setWidget(widget);
        region.getRegionWidgets().add(position, regionWidget);
        updateRenderSequences(region.getRegionWidgets());
        Region persistedRegion = regionRepository.save(region);
        return persistedRegion.getRegionWidgets().get(position);
    }

    private void moveWithinRegion(long regionWidgetId, int newPosition, Region target) {
        replaceRegionWidget(regionWidgetId, newPosition, target, target);
        updateRenderSequences(target.getRegionWidgets());
    }

    private void moveBetweenRegions(long regionWidgetId, int newPosition, long fromRegion, Region target) {
        Region source = getFromRepository(fromRegion, regionRepository);
        replaceRegionWidget(regionWidgetId, newPosition, target, source);
        updateRenderSequences(source.getRegionWidgets());
        updateRenderSequences(target.getRegionWidgets());
        regionRepository.save(source);
    }

    private void replaceRegionWidget(long regionWidgetId, int newPosition, Region target, Region source) {
        RegionWidget widget = findRegionWidgetById(regionWidgetId, source.getRegionWidgets());
        source.getRegionWidgets().remove(widget);
        target.getRegionWidgets().add(newPosition, widget);
    }

    private static <T> T getFromRepository(long id, Repository<T> repo) {
        T object = repo.get(id);
        if (object == null) {
            throw new IllegalArgumentException("Could not find object of given id in " + repo.getClass().getSimpleName());
        }
        return object;
    }

    private static void updateRenderSequences(List<RegionWidget> regionWidgets) {
        int count = 0;
        for (RegionWidget widget : regionWidgets) {
            widget.setRenderOrder(count);
            count++;
        }
    }

    private static RegionWidget findRegionWidgetById(Long id, List<RegionWidget> regionWidgets) {
        for (RegionWidget widget : regionWidgets) {
            if (widget.getId().equals(id)) {
                return widget;
            }
        }
        throw new IllegalArgumentException("Invalid RegionWidget ID");
    }

}