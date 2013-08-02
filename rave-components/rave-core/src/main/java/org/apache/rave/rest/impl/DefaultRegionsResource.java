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


import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.rest.RegionWidgetsResource;
import org.apache.rave.rest.RegionsResource;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

public class DefaultRegionsResource implements RegionsResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Page page;

    private DefaultRegionWidgetsResource regionWidgetsResource;

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public SearchResult<Region> getPageRegions() {
        List<Region> regions = page.getRegions();
        SearchResult<Region> results = new SearchResult<Region>(regions, regions.size());

        return results;
    }

    @Override
    public Region createPageRegion(Region region) {
        //TODO: this method does not make sense since regions are changed via a page's pageLayoutCode
        return null;
    }

    @Override
    public Region getPageRegion(String regionId) {
        List<Region> regions = page.getRegions();
        Region match = null;
        for(Region region: regions) {
            if(region.getId().equals(regionId)) {
                match = region;
                break;
            }
        }

        if(match == null) {
            throw new ResourceNotFoundException(regionId);
        }

        return match;
    }

    @Override
    public Region updatePageRegion(String regionId, Region region) {
        //TODO: this method does not make sense since regions are changed via a page's pageLayoutCode
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Region deletePageRegion(String regionId) {
        //TODO: this method does not make sense since regions are changed via a page's pageLayoutCode
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RegionWidgetsResource getRegionWidgetsResource(String regionId) {
        Region region = getPageRegion(regionId);
        regionWidgetsResource.setPage(page);
        regionWidgetsResource.setRegion(region);

        return regionWidgetsResource;
    }

    @Inject
    public void setRegionWidgetsResource(DefaultRegionWidgetsResource regionWidgetsResource) {
        this.regionWidgetsResource = regionWidgetsResource;
    }
}
