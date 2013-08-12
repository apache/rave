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

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbRegionWidgetRepository implements RegionWidgetRepository {

    @Autowired
    private MongoPageOperations template;

    @Override
    public Class<? extends RegionWidget> getType() {
        return RegionWidgetImpl.class;
    }

    @Override
    public RegionWidget get(String id) {
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

    @Override
    public List<RegionWidget> getAll(){
        Query q = new Query();

        List<Page> allPages = template.find(q);

        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();

        for(Page page: allPages){
            List<Region> regions = page.getRegions();
            if(regions != null){
                for(Region region : regions) {
                    List<RegionWidget> rws = region.getRegionWidgets();
                    if(rws != null){
                        for(RegionWidget rw : rws){
                            regionWidgets.add(rw);
                        }
                    }
                }
            }

        }

        return regionWidgets;
    }

    @Override
    public List<RegionWidget> getLimitedList(int offset, int pageSize) {
        List<RegionWidget> regionWidgets = this.getAll();
        int end = regionWidgets.size() < offset + pageSize ? regionWidgets.size() : offset + pageSize;

        return regionWidgets.subList(offset, end);
    }

    @Override
    public int getCountAll() {
        return this.getAll().size();
    }

    private RegionWidget updateRegionWidget(RegionWidget item) {
        RegionWidget savedWidget;
        Page page = getPageByRegionWidgetId(item.getId());
        replaceOrRemoveWidget(page, item, true);
        Page saved = template.save(page);
        savedWidget = getRegionWidgetById(saved, item.getId());
        return savedWidget;
    }

    private RegionWidget addNewRegionWidget(RegionWidget item) {
        Page page = getPageFromRepository(item);
        Region parent = getRegionById(item.getRegion().getId(), page.getRegions());
        if(parent == null) throw new IllegalStateException("Unable to find parent for page");
        parent.getRegionWidgets().add(item);
        Page saved = template.save(page);
        return getRegionById(parent.getId(), saved.getRegions()).getRegionWidgets().get(parent.getRegionWidgets().size() -1);
    }

    private RegionWidget getRegionWidgetById(Page page, String id) {
        List<Region> regions = page.getRegions();
        RegionWidget regionWidget = getRegionWidget(id, regions);
        if(regionWidget == null && page.getSubPages() != null) {
            for(Page subPage : page.getSubPages()) {
                regionWidget = getRegionWidgetById(subPage, id);
                if(regionWidget != null) break;
            }
        }
        return regionWidget;
    }

    private RegionWidget getRegionWidget(String id, List<Region> regions) {
        for(Region region : regions) {
            for(RegionWidget widget : region.getRegionWidgets()) {
                if(widget.getId().equals(id)) {
                    return widget;
                }
            }
        }
        return null;
    }

    private int replaceOrRemoveWidget(Page page, RegionWidget item, boolean replace) {
        List<Region> regions = page.getRegions();
        int index = replaceOrRemoveWidget(item, replace, regions);
        if(index == -1 && page.getSubPages() != null) {
            for(Page subPage : page.getSubPages()) {
                index = replaceOrRemoveWidget(item, replace, subPage.getRegions());
                if(index != -1) break;
            }
        }
        if(index == -1) throw new IllegalStateException("Widget does not exist in parent page regions");
        return index;
    }

    private int replaceOrRemoveWidget(RegionWidget item, boolean replace, List<Region> regions) {
        for(Region region : regions) {
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

    private Region getRegionById(String id, List<Region> regions) {
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

    private Page getPageByRegionWidgetId(String id) {
        Criteria criteria = getRegionWidgetIdCriteria(id);
        return template.findOne(query(new Criteria().orOperator(criteria, where("subPages").elemMatch(criteria))));
    }

    private Criteria getRegionWidgetIdCriteria(String id) {
        return where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)));
    }

    public void setTemplate(MongoPageOperations template) {
        this.template = template;
    }
}
