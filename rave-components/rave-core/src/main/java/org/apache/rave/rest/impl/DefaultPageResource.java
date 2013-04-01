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
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.PageList;
import org.apache.rave.rest.model.RegionWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DefaultPageResource implements PagesResource {

    public static final String SELF = "@self";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;
    private RenderService renderService;
    private UserService userService;

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
        return fromDb == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(new Page(fromDb)).build();
    }

    @Override
    public Response updatePage(String id, Page page) {
        org.apache.rave.model.Page fromDb = pageService.updatePage(id, page.getName(), page.getPageLayoutCode());
        return Response.ok(new Page(fromDb)).build();
    }

    @Override
    public Response getPageOmdl(String id) {
        return null;
    }

    @Override
    public Response getPagesForRender(String context, String identifier) {
        List<org.apache.rave.model.Page> pages;
        if("portal".equals(context)) {
            String userId = SELF.equals(identifier) ? userService.getAuthenticatedUser().getId() : identifier;
            pages = pageService.getAllUserPages(userId);
        } else if("profile".equals(context)) {
            pages = Arrays.asList(pageService.getPersonProfilePage(identifier));
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Page> converted = Lists.newArrayList();
        for(org.apache.rave.model.Page page : pages) {
            Page convert = new Page(page);
            converted.add(renderService.prepareForRender(convert));
        }
        return Response.ok(new PageList(converted)).build();
    }

    @Override
    public Response getPageForRender(String context, String identifier, String id) {
        return getPage(id);
    }

    @Override
    public Response clonePage(String context, String identifier, String id) {
        return null;
    }

    @Override
    public Response importOmdlPage(String context, String identifier, File page) {
        return null;
    }

    @Override
    public Response createPage(String context, String identifier, Page page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePageInContext(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageInContext(String id, Page page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response movePage(String id, String moveAfterPageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addWidgetToPage(String id, RegionWidget widget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response removeWidgetFromPage(String id, String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addWidgetToRegion(String id, String regionId, RegionWidget widget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response moveWidgetOnPage(String id, String toRegionId, String regionWidgetId, int position) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response moveWidgetToPage(String targetPageId, String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addMemberToPage(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response removeMemberFromPage(String id, String userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updateSharedPageStatus(String id, String userId, String status) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageEditingStatus(String id, String userId, boolean editor) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @Inject
    public void setRenderService(RenderService renderService) {
        this.renderService = renderService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
