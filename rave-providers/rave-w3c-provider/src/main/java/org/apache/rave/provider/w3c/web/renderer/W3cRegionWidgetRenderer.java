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
package org.apache.rave.provider.w3c.web.renderer;


import org.apache.rave.model.User;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.w3c.service.impl.W3CWidget;
import org.apache.rave.rest.model.RegionWidget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.apache.rave.provider.w3c.Constants.WIDGET_TYPE;

@Component
public class W3cRegionWidgetRenderer implements RegionWidgetRenderer {

    private final WidgetProviderService widgetService;
    private final UserService userService;
    private final WidgetService coreWidgetService;

    @Autowired
    public W3cRegionWidgetRenderer(@Qualifier("wookieWidgetService") WidgetProviderService widgetService,
                                    UserService userService, WidgetService coreWidgetService) {
        this.widgetService = widgetService;
        this.userService = userService;
        this.coreWidgetService = coreWidgetService;
    }

    @Override
    public String getSupportedContext() {
        return WIDGET_TYPE;
    }

    @Override
    public String render(RegionWidget item, RenderContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegionWidget prepareForRender(RegionWidget item) {
        User user = userService.getAuthenticatedUser();
        String sharedDataKey = String.valueOf(item.getId());
        W3CWidget contextualizedWidget = (W3CWidget) widgetService.getWidget(user, sharedDataKey, coreWidgetService.getWidget(item.getWidgetId()));
        item.setWidgetUrl(contextualizedWidget.getUrl());
        return item;
    }
}
