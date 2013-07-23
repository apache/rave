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


import com.google.common.collect.Lists;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.renderer.RenderService;
import org.apache.rave.rest.PagesResource;
import org.apache.rave.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class DefaultPageResource implements PagesResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;

    @Override
    public Response getPages() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response createPage(Page page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePage(String id) {
        logger.debug("Deleting page " + id);
        pageService.deletePage(id);
        return Response.noContent().build();
    }

    @Override
    public Response getPage(String id) {
        logger.debug("Retrieving page for export: " + id);
        org.apache.rave.model.Page fromDb = pageService.getPage(id);
        if(fromDb == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            Page responsePage =  new Page(fromDb);

            return Response.ok(new JsonResponseWrapper(responsePage)).build();
        }
    }

    @Override
    public Response updatePage(String id, Page page) {
        org.apache.rave.model.Page fromDb = pageService.updatePage(id, page.getName(), page.getPageLayoutCode());
        Page responsePage =  new Page(fromDb);

        return Response.ok(new JsonResponseWrapper(responsePage)).build();
    }

    @Override
    public Response getPageOmdl(String id) {
        return null;
    }

    @Override
    public Response getPageRegions(String pageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response createPageRegion(String pageId, Region region) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getPageRegion(String id, String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageRegion(String pageId, String regionId, Region region) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePageRegion(String pageId, String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response createPageRegionRegionWidget(String pageId, String regionId, RegionWidget regionWidget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getPageRegionRegionWidgets(String id, String regionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getPageRegionRegionWidget(String id, String regionId, String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageRegionRegionWidget(String pageId, String regionId, String regionWidgetId, RegionWidget regionWidget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePageRegionRegionWidget(String pageId, String regionId, String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }
}
