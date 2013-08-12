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

import org.apache.rave.rest.exception.ResourceNotFoundException;
import org.apache.rave.rest.model.PageLayout;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.rest.PageLayoutResource;
import org.apache.rave.rest.model.SearchResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DefaultPageLayoutResource implements PageLayoutResource {
    private PageLayoutService pageLayoutService;

    @Inject
    public void setPageLayoutService(PageLayoutService pageLayoutService) {
        this.pageLayoutService = pageLayoutService;
    }

    @Override
    public SearchResult<PageLayout> getPageLayouts() {

        List<org.apache.rave.model.PageLayout> fromDb = pageLayoutService.getAll();
        List<PageLayout> pageLayouts = new ArrayList<PageLayout>();

        for (org.apache.rave.model.PageLayout pageLayout : fromDb) {
            pageLayouts.add(new PageLayout(pageLayout));
        }

        return new SearchResult<PageLayout>(pageLayouts, fromDb.size());
    }

    @Override
    public PageLayout getPageLayout(String code) {

        org.apache.rave.model.PageLayout fromDb = pageLayoutService.getPageLayoutByCode(code);

        if(fromDb == null) {
            throw new ResourceNotFoundException(code);
        }

        return new PageLayout(fromDb);
    }

    @Override
    public PageLayout updatePageLayout(String code, PageLayout pageLayout) {
        return null;
    }

    @Override
    public PageLayout createPageLayout(PageLayout pageLayout) {
        return null;
    }
}
