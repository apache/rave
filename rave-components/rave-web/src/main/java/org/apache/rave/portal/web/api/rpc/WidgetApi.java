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

import org.apache.rave.model.Widget;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines RPC operations for a Widget or its components
 */
@Controller(value = "rpcWidgetApi")
@RequestMapping(value = "/api/rpc/widget/*")
public class WidgetApi {

    private Map<String, WidgetMetadataResolver> widgetMetadataResolverMap;
    
    private WidgetService widgetService;

    @Autowired
    public WidgetApi(List<WidgetMetadataResolver> widgetMetadataResolvers, WidgetService widgetService) {
        this.widgetService = widgetService;
        widgetMetadataResolverMap = new HashMap<String, WidgetMetadataResolver>();
        for (WidgetMetadataResolver widgetMetadataResolver : widgetMetadataResolvers) {
            widgetMetadataResolverMap.put(widgetMetadataResolver.getSupportedContext(), widgetMetadataResolver);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "metadata/get")
    public RpcResult<Widget> getWidgetMetadata(@RequestParam final String url,
                                               @RequestParam final String type) {
        return new RpcOperation<Widget>() {
            @Override
            public Widget execute() {
                if (widgetMetadataResolverMap.get(type) == null) {
                    throw new IllegalArgumentException("Get Metadata for provider " + type + " is not implemented");
                }
                return widgetMetadataResolverMap.get(type).getMetadata(url);
            }
        }.getResult();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "metadatagroup/get")
    public RpcResult<Widget[]> getWidgetMetadataGroup(@RequestParam final String url,
                                               @RequestParam final String type) {
        return new RpcOperation<Widget[]>() {
            @Override
            public Widget[] execute() {
                if (widgetMetadataResolverMap.get(type) == null) {
                    throw new IllegalArgumentException("Get Metadata group for provider " + type + " is not implemented");
                }
                return widgetMetadataResolverMap.get(type).getMetadataGroup(url);
            }
        }.getResult();
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "getall")
    public RpcResult<SearchResult<Widget>> getAllWidgets(){
        return new RpcOperation<SearchResult<Widget>>() {
            @Override
            public SearchResult<Widget> execute() {
               SearchResult<Widget> results = widgetService.getAll();
               // strip out the owner and tag info 
               for(Widget widget : results.getResultSet()){
                   widget.setOwnerId(null);
                   widget.setTags(null);
               }
               return results;
            }
        }.getResult();
    }
}
