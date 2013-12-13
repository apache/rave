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
import org.apache.commons.lang3.StringUtils;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.rest.PageUsersResource;
import org.apache.rave.rest.exception.ResourceNotFoundException;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.rest.PagesResource;
import org.apache.rave.rest.RegionsResource;
import org.apache.rave.rest.exception.BadRequestException;
import org.apache.rave.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultPageResource implements PagesResource {

    public static final String SELF = "@self";

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;

    private DefaultRegionsResource regionsResouce;
    private DefaultPageUsersResource pageUsersResource;
    private UserService userService;

    @Override
    public SearchResult<Page> getPages() {
        SearchResult<org.apache.rave.model.Page> fromDb = pageService.getAll();
        return convert(fromDb.getResultSet(), fromDb.getTotalResults());
    }

    @Override
    public SearchResult<Page> getContextPages(String context, String identifier) {
        String contextId = identifier.equals(SELF) ? userService.getAuthenticatedUser().getId() : identifier;
        //TODO Replace when handling supports page member handling as a special case RAVE-1044
        List<org.apache.rave.model.Page> pages;
        try {
            if ("portal".equals(context)) {
                pages = pageService.getAllUserPages(contextId);
            } else if ("profile".equals(context)) {
                pages = Arrays.asList(pageService.getPersonProfilePage(contextId));
            } else {
                pages = pageService.getPages(context, contextId);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(contextId);
        }
        if (pages == null) {
            throw new ResourceNotFoundException(contextId);
        }
        return convert(pages, pages.size());
    }

    @Override
    public Page getPage(String id) {
        logger.debug("Retrieving page for export: " + id);
        org.apache.rave.model.Page fromDb = pageService.getPage(id);
        if (fromDb == null) {
            throw new ResourceNotFoundException(id);
        }

        return new Page(fromDb);
    }


    @Override
    public Page createPage(String pageTemplateId, Page page) {
        if (page.getName() == null) {
            throw new BadRequestException("Page name property must be defined.");
        }
        org.apache.rave.model.Page fromDb;
        if(StringUtils.isNotBlank(pageTemplateId)) {
            fromDb = pageService.addNewPage(page.getName(), null, pageTemplateId);
        //TODO: RAVE-977 - when Page type enum is deprecated escape from this logic
        } else if (page.getPageType().equals("user")) {
            if (page.getPageLayoutCode() == null) {
                throw new BadRequestException("Page pageLayoutCode property must be defined.");
            }
            fromDb = pageService.addNewUserPage(page.getName(), page.getPageLayoutCode());
        } else {
            //TODO: RAVE-977 this will change
            throw new BadRequestException("Page pageType property must equal 'user'.");
        }

        return new Page(fromDb);
    }

    @Override
    public Page updatePage(String id, Page page) {
        if (page.getName() == null) {
            throw new BadRequestException("Page name property must be defined.");
        }
        if (page.getPageLayoutCode() == null) {
            throw new BadRequestException("Page pageLayoutCode property must be defined.");
        }
        //TODO: a bad page layout code can corrupt the data
        //As part of the model refactor, this needs to be made more concise and clear what properties of a page are mutable and why
        org.apache.rave.model.Page fromDb = pageService.updatePage(id, page.getName(), page.getPageLayoutCode(), page.getProperties());
        Page responsePage = new Page(fromDb);

        return responsePage;
    }

    @Override
    public Page deletePage(String id) {
        //TODO: this cannot return a 404
        logger.debug("Deleting page " + id);
        pageService.deletePage(id);
        return null;
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

    @Override
    public PageUsersResource getPageMembersResource(String pageId) {
        Page page = getPage(pageId);
        pageUsersResource.setPage(page);

        return pageUsersResource;
    }

    private SearchResult<Page> convert(List<org.apache.rave.model.Page> fromDb, int total) {
        List<Page> pages = Lists.newArrayList();
        for (org.apache.rave.model.Page page : fromDb) {
            pages.add(new Page(page));
        }
        return new SearchResult<Page>(pages, total);
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @Inject
    public void setRegionsResouce(DefaultRegionsResource regionsResouce) {
        this.regionsResouce = regionsResouce;
    }

    @Inject
    public void setPageUsersResource(DefaultPageUsersResource pageUsersResource) {
        this.pageUsersResource = pageUsersResource;
    }
    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
