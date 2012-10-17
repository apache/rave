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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.MongoDbRegionWidget;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.repository.MongoModelOperations;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class MongoDbRegionWidgetRepository implements RegionWidgetRepository {

    @Autowired
    private MongoModelOperations.MongoPageOperations template;

    @Override
    public Class<? extends RegionWidget> getType() {
        return MongoDbRegionWidget.class;
    }

    @Override
    public RegionWidget get(long id) {
        Page page = getPageByRegionWidgetId(id);
        return getRegionWidgetById(page, id);
    }

    @Override
    public RegionWidget save(RegionWidget item) {
        return item.getId() == null ? addNewRegionWidget(item) : updateRegionWidget(item);
    }

    @Override
    public void delete(RegionWidget item) {
        Page page = getPageByRegionWidgetId(item.getId());
        replaceOrRemoveWidget(page, item, false);
        template.save(page);
    }

    private RegionWidget updateRegionWidget(RegionWidget item) {
        RegionWidget savedWidget;Page page = getPageByRegionWidgetId(item.getId());
        replaceOrRemoveWidget(page, item, true);
        Page saved = template.save(page);
        savedWidget = getRegionWidgetById(saved, item.getId());
        return savedWidget;
    }

    private RegionWidget addNewRegionWidget(RegionWidget item) {
        Page page = getPageFromRepository(item);
        Region parent = getRegionById(item.getRegion().getId(), page.getRegions());
        parent.getRegionWidgets().add(item);
        Page saved = template.save(page);
        return getRegionById(parent.getId(), saved.getRegions()).getRegionWidgets().get(parent.getRegionWidgets().size() -1);
    }

    private RegionWidget getRegionWidgetById(Page page, Long id) {
        for(Region region : page.getRegions()) {
            for(RegionWidget widget : region.getRegionWidgets()) {
                if(widget.getId().equals(id)) {
                    return widget;
                }
            }
        }
        return null;
    }

    private int replaceOrRemoveWidget(Page page, RegionWidget item, boolean replace) {
        for(Region region : page.getRegions()) {
            List<RegionWidget> regionWidgets = region.getRegionWidgets();
            for(int i=0; i< regionWidgets.size(); i++) {
                if(regionWidgets.get(i).getId().equals(item.getId())) {
                    regionWidgets.remove(i);
                    if(replace) {
                        regionWidgets.add(i, item);
                    }
                    return i;
                }
            }
        }
        return -1;
    }

    private Region getRegionById(Long id, List<Region> regions) {
        for(Region region: regions) {
            if(id.equals(region.getId())) {
                return region;
            }
        }
        return null;
    }

    private Page getPageFromRepository(RegionWidget item) {
        if(item.getRegion() != null && item.getRegion().getPage() != null && item.getRegion().getPage().getId() != null) {
            return template.get(item.getRegion().getPage().getId());
        }
        else {
            throw new IllegalStateException("Unable to find page for region");
        }
    }

    private Page getPageByRegionWidgetId(long id) {
        return template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))));
    }
}
