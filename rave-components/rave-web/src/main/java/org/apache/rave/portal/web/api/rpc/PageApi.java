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
package org.apache.rave.portal.web.api.rpc;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Defines RPC operations for a Page or its components
 */
@Controller(value="rpcPageApi")
@RequestMapping(value = "/api/rpc/page/*")
public class PageApi {

    private final PageService pageService;

    @Autowired
    public PageApi(PageService pageService) {
        this.pageService = pageService;
    }

    /**
     * Adds a widget to the given page
     * 
     * @param pageId
     *            the ID of the {@link org.apache.rave.portal.model.Page} to add
     *            the widget to
     * @param widgetId
     *            the ID of the {@link org.apache.rave.portal.model.Widget} to
     *            add do the page
     * @return a {@link RpcOperation} containing the new widget instance (
     *         {@link org.apache.rave.portal.model.RegionWidget }) or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{pageId}/widget/add")
    public RpcResult<RegionWidget> addWidgetToPage(
            @PathVariable final long pageId, @RequestParam final long widgetId) {

        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.addWidgetToPage(pageId, widgetId);
            }
        }.getResult();
    }

    /**
     * Moves a widget to a new location
     * <p/>
     * Moves can take place within a region, region to region, or between pages
     * 
     * @param regionWidgetId
     *            the ID of the
     *            {@link org.apache.rave.portal.model.RegionWidget} to move
     * @param newPosition
     *            the 0 based index within the region where the RegionWidget
     *            will now be located
     * @param toRegion
     *            the Id of the {@link org.apache.rave.portal.model.Region }
     *            where the widget will now be located
     * @param fromRegion
     *            the Id of the {@link org.apache.rave.portal.model.Region }
     *            where the widget is currently located
     * @return a {@link RpcOperation} containing the updated widget instance (
     *         {@link org.apache.rave.portal.model.RegionWidget }) or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "regionWidget/{regionWidgetId}/move")
    public RpcResult<RegionWidget> moveWidgetOnPage(
            @PathVariable final long regionWidgetId,
            @RequestParam final int newPosition,
            @RequestParam final long toRegion,
            @RequestParam final long fromRegion) {

        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.moveRegionWidget(regionWidgetId,
                        newPosition, toRegion, fromRegion);
            }
        }.getResult();
    }

    /**
     * Deletes a widget
     *
     * @param regionWidgetId
     *            the ID of the {@link org.apache.rave.portal.model.Widget} to
     *            delete
     * @return an {@link RpcOperation} containing the updated region or any
     *         errors encountered.
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "regionWidget/{regionWidgetId}/delete")
    public RpcResult<Region> removeWidgetFromPage(@PathVariable final long regionWidgetId) {
        return new RpcOperation<Region>() {
            @Override
            public Region execute() {
                return pageService.removeWidgetFromPage(regionWidgetId);
            }
        }.getResult();
    }
    

    /**
     * Adds a new page
     * 
     * @param pageName the new page name
     * @param pageLayoutCode the layout code for this new page
     * @return an {@link RpcOperation} containing the new page or any
     *         errors encountered.
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "add")
    public RpcResult<Page> addPage(@RequestParam final String pageName,
                                   @RequestParam final String pageLayoutCode) {
        return new RpcOperation<Page>() {
             @Override
             public Page execute() {
                 return pageService.addNewUserPage(pageName, pageLayoutCode);
             }
        }.getResult();        
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="get")
    public RpcResult<Page> getPage(@RequestParam final long pageId) {
        return new RpcOperation<Page>() {
            @Override
            public Page execute() {
                return pageService.getPage(pageId);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/update", method = RequestMethod.POST)
    public RpcResult<Page> updatePageProperties(@PathVariable final long pageId,
                                        @RequestParam final String name,
                                        @RequestParam final String layout) {
        return new RpcOperation<Page>() {
            @Override
            public Page execute() {
                return pageService.updatePage(pageId, name, layout);
            }
        }.getResult();
    }
    
    /**
     * Moves a page to a new render position
     * 
     * @param pageId the pageId to move
     * @param moveAfterPageId the pageId to move after in render order
     * @return an {@link RpcOperation} containing the updated page or any
     *         errors encountered.
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{pageId}/move")
    public RpcResult<Page> movePage(@PathVariable final long pageId, 
                                    @RequestParam(required=false) final Long moveAfterPageId) {
        return new RpcOperation<Page>() {
            @Override
            public Page execute() {
                Page page = null;
                if (moveAfterPageId == null) {
                    page = pageService.movePageToDefault(pageId);
                } else {
                    page = pageService.movePage(pageId, moveAfterPageId);
                }
                return page;
            }
        }.getResult();        
    }

    /**
     * Moves a region widget to a new page
     *
     * @param toPageId the pageId to move the region widget to
     * @param regionWidgetId the regionWidgetId that needs to be moved
     * @return an {@link RpcOperation} containing the updated RegionWidget or any
     *         errors encountered.
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{toPageId}/moveWidget")
    public RpcResult<RegionWidget> moveWidgetToPage(@PathVariable final long toPageId,
                                                    @RequestParam(required=false) final long regionWidgetId) {
        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.moveRegionWidgetToPage(regionWidgetId, toPageId);
            }
        }.getResult();
    }
}