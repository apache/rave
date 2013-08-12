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
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
@Repository
public class MongoDbRegionRepository implements RegionRepository {

    @Autowired
    private MongoPageOperations template;


    @Override
    public Class<? extends Region> getType() {
        return RegionImpl.class;
    }

    @Override
    public Region get(String  id) {
        Page page = getPageByRegionId(id);
        for(Region region : page.getRegions()) {
            if(region.getId().equals(id)) {
                return region;
            }
        }
        return null;
    }

    @Override
    public Region save(Region item) {
        Page page;
        int index;

        if(item.getId() == null) {
            page = getPageFromRepository(item);
            page.getRegions().add(item);
            index = page.getRegions().size()-1;
        } else {
            page = getPageByRegionId(item.getId());
            index = replaceRegion(page, item);
        }
        Page saved = template.save(page);
        return saved.getRegions().get(index);
    }

    @Override
    public void delete(Region item) {
        Page page;

        if(item.getId() == null) {
            throw new IllegalStateException("Unidentifiable region (null id)");
        } else {
            page = getPageByRegionId(item.getId());
            removeRegion(page, item);
        }
        template.save(page);
    }

    @Override
    public List<Region> getAll(){

        Query q = new Query();

        List<Page> allPages = template.find(q);

        List<Region> regions = new ArrayList<Region>();

        for(Page page: allPages){
            List<Region> rgns = page.getRegions();
            if(rgns != null){
                for(Region region : rgns) {
                    regions.add(region);
                }
            }

        }

        return regions;
    }

    @Override
    public List<Region> getLimitedList(int offset, int pageSize){
        List<Region> regions = this.getAll();
        int end = regions.size() < offset + pageSize ? regions.size() : offset + pageSize;

        return regions.subList(offset, end);
    }

    @Override
    public int getCountAll() {
        return this.getAll().size();
    }


    private void removeRegion(Page page, Region item) {
        Iterator<Region> iterator = page.getRegions().iterator();
        while(iterator.hasNext()) {
            Region region = iterator.next();
            if(region.getId().equals(item.getId())) {
                iterator.remove();
                return;
            }
        }
    }

    private int replaceRegion(Page page, Region item) {
        List<Region> regions = page.getRegions();
        for(int i=0; i < regions.size(); i++) {
            if(regions.get(i).getId().equals(item.getId())) {
                regions.remove(i);
                regions.add(i, item);
                return i;
            }
        }
        return 0;
    }

    private Page getPageFromRepository(Region item) {
        if(item.getPage() != null && item.getPage().getId() != null) {
            return template.get(item.getPage().getId());
        }
        else {
            throw new IllegalStateException("Unable to find page for region");
        }
    }

    private Page getPageByRegionId(String  id) {
        return template.findOne(new Query(Criteria.where("regions").elemMatch(Criteria.where("_id").is(id))));
    }


    public MongoPageOperations getTemplate() {
        return template;
    }

    public void setTemplate(MongoPageOperations template) {
        this.template = template;
    }

}
