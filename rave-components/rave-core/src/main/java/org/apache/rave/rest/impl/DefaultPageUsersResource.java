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
import org.apache.rave.rest.PageUsersResource;
import org.apache.rave.rest.RegionWidgetsResource;
import org.apache.rave.rest.RegionsResource;
import org.apache.rave.rest.exception.BadRequestException;
import org.apache.rave.rest.exception.ResourceNotFoundException;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.PageUser;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.SearchResult;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import java.util.List;

public class DefaultPageUsersResource implements PageUsersResource {

    private Page page;
    private PageService pageService;

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public SearchResult<PageUser> getPageUsers() {
        List<PageUser> members = page.getMembers();
        SearchResult<PageUser> result = new SearchResult<PageUser>(members, members.size());

        return result;
    }

    @Override
    public PageUser createPageUser(PageUser user) {
        if (user.getPersonId() == null) {
            throw new BadRequestException("Page user personId property must be defined.");
        }
        pageService.addMemberToPage(page.getId(), user.getPersonId());
        pageService.updatePageEditingStatus(page.getId(), user.getPersonId(), user.isEditor());
        return user;
    }

    @Override
    public PageUser getPageUser(String userId) {
        List<PageUser> members = page.getMembers();
        PageUser match = null;
        for (PageUser user : members) {
            if (user.getPersonId().equals(userId)) {
                match = user;
                break;
            }
        }

        if (match == null) {
            throw new ResourceNotFoundException(userId);
        }

        return match;
    }

    @Override
    public PageUser updatePageUser(String userId, PageUser user) {
        PageUser oldUser = getPageUser(userId);

        if (user.getPersonId() == null) {
            throw new BadRequestException("Page user status property must be defined.");
        }

        if(oldUser.isEditor() != user.isEditor()) {
            pageService.updatePageEditingStatus(page.getId(), userId, user.isEditor());
        }
        if(!oldUser.getStatus().equals(user.getStatus())) {
            pageService.updateSharedPageStatus(page.getId(), userId, user.getStatus());
        }

        return user;
    }

    @Override
    public PageUser deletePageUser(String userId) {
        PageUser user = getPageUser(userId);

        pageService.removeMemberFromPage(page.getId(), user.getPersonId());
        return null;
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }
}
