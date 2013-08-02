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
import org.apache.rave.model.PageType;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.rest.PagesResource;
import org.apache.rave.rest.RegionsResource;
import org.apache.rave.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class DefaultPageResource implements PagesResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;

    private DefaultRegionsResource regionsResouce;

    @Override
    public SearchResult<Page> getPages() {
        SearchResult<org.apache.rave.model.Page> fromDb = pageService.getAll();
        List<Page> pages = new ArrayList<Page>();

        for(org.apache.rave.model.Page page : fromDb.getResultSet()) {
            pages.add(new Page(page));
        }

        SearchResult<Page> returnPages = new SearchResult<Page>(pages, fromDb.getTotalResults());
        return returnPages;
    }

    @Override
    public Page createPage(Page page) {
        //TODO: RAVE-977 - when Page type enum is deprecated escape from this logic
        if(page.getPageType() == "user") {
            org.apache.rave.model.Page fromDb = pageService.addNewUserPage(page.getName(), page.getPageLayoutCode());
            Page responsePage =  new Page(fromDb);

            return responsePage;
        } else {
            //TODO: throw 400 exception
            return null;
        }
    }

    @Override
    public Page deletePage(String id) {
        logger.debug("Deleting page " + id);
        pageService.deletePage(id);
        return null;
    }

    @Override
    public Page getPage(String id) {
        logger.debug("Retrieving page for export: " + id);
        org.apache.rave.model.Page fromDb = pageService.getPage(id);
        //TODO: with a bad ID a 403 gets thrown before I hit this block. Why?
        if(fromDb == null) {
            throw new ResourceNotFoundException(id);
        }
        Page responsePage =  new Page(fromDb);

        return responsePage;
    }

    @Override
    public Page updatePage(String id, Page page) {
        org.apache.rave.model.Page fromDb = pageService.updatePage(id, page.getName(), page.getPageLayoutCode());
        Page responsePage =  new Page(fromDb);

        return responsePage;
    }

    @Override
    public Response getPageOmdl(String id) {
        return null;
    }

    @Override
    public RegionsResource getRegionsResource(String pageId) {
        Page page = getPage(pageId);
        regionsResouce.setPage(page);

        return regionsResouce;
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @Inject
    public void setRegionsResouce(DefaultRegionsResource regionsResouce) {
        this.regionsResouce = regionsResouce;
    }
}
