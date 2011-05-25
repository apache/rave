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

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Provides RPC functions via the API.
 */
@Controller
@RequestMapping(value = "/api/rpc/page/*")
public class PageApi {

    private final PageService pageService;

    @Autowired
    public PageApi(PageService pageService) {
        this.pageService = pageService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "{pageId}/widget/add")
    public RpcResult<RegionWidget> doPageOperation(@PathVariable final long pageId,
                                                   @RequestParam final long widgetId) {

        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.addWidgetToPage(pageId, widgetId);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "regionWidget/{region_widget_id}/move")
    public RpcResult<RegionWidget> doRegionWidgetOperation(@PathVariable final long region_widget_id,
                                                           @RequestParam final int new_position,
                                                           @RequestParam final long to_region,
                                                           @RequestParam final long from_region) {


        return new RpcOperation<RegionWidget>() {
            @Override
            public RegionWidget execute() {
                return pageService.moveRegionWidget(region_widget_id, new_position, to_region, from_region);
            }
        }.getResult();
    }
}
