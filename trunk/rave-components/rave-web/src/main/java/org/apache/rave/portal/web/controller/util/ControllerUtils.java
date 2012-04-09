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

package org.apache.rave.portal.web.controller.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.DeviceUtils;

public class ControllerUtils {
    private static final Logger log = LoggerFactory.getLogger(ControllerUtils.class);

    /**
     * Utility function to determine if this HttpServletRequest 
     * is coming from a mobile client 
     * 
     * @param request the HttpServletRequest from the client
     * @return true if the client is a mobile device, false if not mobile
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        return DeviceUtils.getCurrentDevice(request).isMobile();
    }

    public static String getDeviceAppropriateView(HttpServletRequest request, String defaultView, String mobileView) {
        // return the appropriate View name based on the request.  It
        // checks to see if the user is on a mobile device or not
        String viewName = null;
        if (ControllerUtils.isMobileDevice(request)) {
            log.debug("mobile device detected - viewing default mobile page template");
            viewName = mobileView;
        } else {
            log.debug("non-mobile device detected - viewing regular page layout");
            viewName = defaultView;
        }
        log.debug("viewName: " + viewName);
        return viewName;
    }
}