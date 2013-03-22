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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.service.OmdlService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Defines RPC operations for a Page or its components
 */
@Controller(value="rpcPageApi")
@RequestMapping(value = "/api/rpc/page/*")
public class PageApi {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final PageService pageService;
    private OmdlService omdlService;

    @Autowired
    public PageApi(PageService pageService, OmdlService omdlService) {
        this.pageService = pageService;
        this.omdlService = omdlService;
    }

    /**
     * Adds a widget to the given page
     *
     * @param pageId
     *            the ID of the {@link org.apache.rave.model.Page} to add
     *            the widget to
     * @param widgetId
     *            the ID of the {@link org.apache.rave.model.Widget} to
     *            add do the page
     * @return a {@link RpcOperation} containing the new widget instance (
     *         {@link org.apache.rave.model.RegionWidget }) or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{pageId}/widget/add")
    public RpcResult<RegionWidget> addWidgetToPage(
            @PathVariable final String pageId, @RequestParam final String widgetId) {

        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.addWidgetToPage(pageId, widgetId);
            }
        }.getResult();
    }

    /**
     * Adds a widget to the given page region
     *
     * @param pageId
     *            the ID of the {@link org.apache.rave.model.Page} to add
     *            the widget to
     * @param widgetId
     *            the ID of the {@link org.apache.rave.model.Widget} to
     *            add do the page
     * @param regionId
     *            the ID of the {@link org.apache.rave.model.Region} to
     *            add the the widget to
     * @return a {@link RpcOperation} containing the new widget instance (
     *         {@link org.apache.rave.model.RegionWidget }) or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{pageId}/widget/add/region/{regionId}")
    public RpcResult<RegionWidget> addWidgetToPageRegion(
            @PathVariable final String pageId, @RequestParam final String widgetId, @PathVariable final String regionId) {

        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.addWidgetToPageRegion(pageId, widgetId, regionId);
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
     *            {@link org.apache.rave.model.RegionWidget} to move
     * @param newPosition
     *            the 0 based index within the region where the RegionWidget
     *            will now be located
     * @param toRegion
     *            the Id of the {@link org.apache.rave.model.Region }
     *            where the widget will now be located
     * @param fromRegion
     *            the Id of the {@link org.apache.rave.model.Region }
     *            where the widget is currently located
     * @return a {@link RpcOperation} containing the updated widget instance (
     *         {@link org.apache.rave.model.RegionWidget }) or any errors
     *         encountered while performing the RPC operation
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "regionWidget/{regionWidgetId}/move")
    public RpcResult<RegionWidget> moveWidgetOnPage(
            @PathVariable final String regionWidgetId,
            @RequestParam final int newPosition,
            @RequestParam final String toRegion,
            @RequestParam final String fromRegion) {

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
     *            the ID of the {@link org.apache.rave.model.Widget} to
     *            delete
     * @return an {@link RpcOperation} containing the updated region or any
     *         errors encountered.
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "regionWidget/{regionWidgetId}/delete")
    public RpcResult<Region> removeWidgetFromPage(@PathVariable final String regionWidgetId) {
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
    public RpcResult<Page> getPage(@RequestParam final String pageId) {
        return new RpcOperation<Page>() {
            @Override
            public Page execute() {
                return pageService.getPage(pageId);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/update", method = RequestMethod.POST)
    public RpcResult<Page> updatePageProperties(@PathVariable final String pageId,
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
    public RpcResult<Page> movePage(@PathVariable final String pageId,
                                    @RequestParam(required=false) final String moveAfterPageId) {
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
    public RpcResult<RegionWidget> moveWidgetToPage(@PathVariable final String toPageId,
                                                    @RequestParam(required=false) final String regionWidgetId) {
        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.moveRegionWidgetToPage(regionWidgetId, toPageId);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/clone", method = RequestMethod.POST)
    public RpcResult<Boolean> clonePageForUser(@PathVariable final String pageId, @RequestParam final String userId, @RequestParam final String pageName) {
        return new RpcOperation<Boolean>() {
             @Override
             public Boolean execute() {
               return pageService.clonePageForUser(pageId, userId, pageName);
             }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/addmember", method = RequestMethod.POST)
    public RpcResult<Boolean> addMemberToPage(@PathVariable final String pageId, @RequestParam final String userId) {
        return new RpcOperation<Boolean>() {
             @Override
             public Boolean execute() {
               return pageService.addMemberToPage(pageId, userId);
             }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/removemember", method = RequestMethod.POST)
    public RpcResult<Boolean> removeMemberFromPage(@PathVariable final String pageId, @RequestParam final String userId) {
        return new RpcOperation<Boolean>() {
             @Override
             public Boolean execute() {
               return pageService.removeMemberFromPage(pageId, userId);
             }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/sharestatus", method = RequestMethod.POST)
    public RpcResult<Boolean> updateSharedPageStatus(@PathVariable final String pageId, @RequestParam final String shareStatus) {
        return new RpcOperation<Boolean>() {
             @Override
             public Boolean execute() {
               return pageService.updateSharedPageStatus(pageId, shareStatus);
             }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/editstatus", method = RequestMethod.POST)
    public RpcResult<Boolean> updatePageEditingStatus(@PathVariable final String pageId, @RequestParam final String userId, @RequestParam final boolean isEditor) {
        return new RpcOperation<Boolean>() {
             @Override
             public Boolean execute() {
               return pageService.updatePageEditingStatus(pageId, userId, isEditor);
             }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "import/omdl")
    public RpcResult<Page> importPage(HttpServletRequest request, final HttpServletResponse response,
            @RequestParam final String pageName) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        final MultipartFile multipartFile = multipartRequest.getFile("omdlFile");
        return new RpcOperation<Page>() {
             @Override
             public Page execute() {
               return omdlService.importOmdl(multipartFile, pageName);
             }
        }.getResult();
    }
}