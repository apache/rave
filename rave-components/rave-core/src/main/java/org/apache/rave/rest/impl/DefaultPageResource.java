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
import org.apache.rave.rest.PagesResource;
import org.apache.rave.rest.RegionsResource;
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
        return Response.ok(pageService.getAll()).build();
    }

    @Override
    public Response createPage(Page page) {
        //pageService.createPage(page)
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

        return Response.ok(new Page(fromDb)).build();
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
    public RegionsResource getRegionsResource(String pageId) {
        org.apache.rave.model.Page fromDb = pageService.getPage(pageId);
        Page page = new Page(fromDb);

        return new DefaultRegionsResource(page);
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }
}
