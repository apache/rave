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

package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/store/*")
public class WidgetStoreController {

    private final WidgetService widgetService;

    @Autowired
    public WidgetStoreController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute(ModelKeys.WIDGETS, widgetService.getAllWidgets());
        return ViewNames.STORE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "widget/{widgetId}")
    public String viewWidget(Model model, @PathVariable long widgetId) {
        model.addAttribute(ModelKeys.WIDGET, widgetService.getWidget(widgetId));
        return ViewNames.WIDGET;
    }
}
