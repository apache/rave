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
import org.apache.rave.rest.PagesForRenderResource;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.PageList;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class DefaultPageForRenderResource implements PagesForRenderResource {

    public static final String SELF = "@self";
    private PageService pageService;
    private RenderService renderService;
    private UserService userService;

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
        return null;
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
