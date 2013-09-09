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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.*;

import java.util.List;

public class PageImpl implements Page {
    private String id;
    private String name;
    private String ownerId;
    private String contextId;
    private Page parentPage;
    private List<Page> subPages;
    private PageLayout pageLayout;
    private List<Region> regions;
    private String pageType;
    private List<PageUser> members;

    public PageImpl() {}

    public PageImpl(String id) {
        this.id = id;
    }

    public PageImpl(String id, String ownerId) {
        this.id = id;
        this.ownerId = ownerId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(String owner) {
        this.ownerId = owner;
    }

    @Override
    public String getContextId() {
        return contextId;
    }

    @Override
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @Override
    public Page getParentPage() {
        return parentPage;
    }

    @Override
    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    @Override
    public List<Page> getSubPages() {
        return subPages;
    }

    @Override
    public void setSubPages(List<Page> subPages) {
        this.subPages = subPages;
    }

    @Override
    public PageLayout getPageLayout() {
        return pageLayout;
    }

    @Override
    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    @Override
    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public String getPageType() {
        return pageType;
    }

    @Override
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    @Override
    public List<PageUser> getMembers() {
        return members;
    }

    @Override
    public void setMembers(List<PageUser> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Page other = (Page) obj;
        if (this.id != other.getId() && (this.id == null || !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }
}
