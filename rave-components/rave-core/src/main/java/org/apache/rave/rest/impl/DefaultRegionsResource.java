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
import org.apache.rave.portal.service.RegionService;
import org.apache.rave.rest.RegionWidgetsResource;
import org.apache.rave.rest.RegionsResource;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

public class DefaultRegionsResource implements RegionsResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private RegionService regionService;
    private PageService pageService;
    private Page page;

    public DefaultRegionsResource(Page page) {
        this.page = page;
    }

    @Override
    public Response getPageRegions() {
        return Response.ok(page.getRegions()).build();
    }

    @Override
    public Response createPageRegion(Region region) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getPageRegion(String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageRegion(String regionId, Region region) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePageRegion(String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RegionWidgetsResource getRegionWidgetsResource(String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
