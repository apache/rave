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
import org.apache.rave.portal.service.WidgetMarketplaceService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.api.rpc.model.RpcOperation;
import org.apache.rave.portal.web.api.rpc.model.RpcResult;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Defines RPC operations for the marketplace or its components
 */
@Controller(value="rpcMarketplaceApi")
@RequestMapping(value = "/api/rpc/marketplace/*")
public class MarketplaceAPI {

    private static final Logger logger = LoggerFactory.getLogger(MarketplaceAPI.class);
    private final WidgetMarketplaceService marketplaceService;
    private final NewWidgetValidator widgetValidator;
    private final WidgetService widgetService;

    @Autowired
    public MarketplaceAPI(WidgetMarketplaceService marketplaceService, NewWidgetValidator validator, WidgetService widgetService) {
        this.marketplaceService = marketplaceService;
        this.widgetValidator = validator;
        this.widgetService = widgetService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "add")
    public RpcResult<Widget> add(@RequestParam final String url,
                                   @RequestParam final String providerType) {
        return new RpcOperation<Widget>() {
             @Override
             public Widget execute() {
                 Widget widget = null;
                 // TODO - improve info sent back to marketplace, rather then just writing to the console
                 try {
                    widget =  marketplaceService.resolveWidgetMetadata(url, providerType);
                    if(widget == null){
                        return null;
                    }
                    BeanPropertyBindingResult results = new BeanPropertyBindingResult(widget, "widget");
                    widgetValidator.validate(widget, results);
                    if (results.hasErrors()) {
                        if(results.hasFieldErrors("url") && results.getFieldError("url").toString().contains("widget.url.exists")){
                            return widgetService.getWidgetByUrl(widget.getUrl());
                        }
                        return null;
                    }
                    return marketplaceService.addWidget(widget);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return null;
                }
             }
        }.getResult();
    }

}
