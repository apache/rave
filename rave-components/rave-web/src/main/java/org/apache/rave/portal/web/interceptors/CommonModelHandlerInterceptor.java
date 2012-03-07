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

package org.apache.rave.portal.web.interceptors;

import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.service.StaticContentFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds attributes to the ModelMap that are needed in every view
 */
public class CommonModelHandlerInterceptor extends HandlerInterceptorAdapter {

    private final PortalPreferenceService preferenceService;
    private final StaticContentFetcherService staticContentFetcherService;

    @Autowired
    public CommonModelHandlerInterceptor(PortalPreferenceService preferenceService, StaticContentFetcherService staticContentFetcherService) {
        this.preferenceService = preferenceService;
        this.staticContentFetcherService = staticContentFetcherService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            ModelMap map = modelAndView.getModelMap();
            map.addAttribute(ModelKeys.PORTAL_SETTINGS, preferenceService.getPreferencesAsMap());
            map.addAttribute(ModelKeys.STATIC_CONTENT_CACHE, staticContentFetcherService);
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
