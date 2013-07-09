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

import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

public final class MockHttpUtil {
    public static void setupRequestAsNonMobileUserAgent(MockHttpServletRequest mockHttpServletRequest) {
        mockHttpServletRequest.addHeader("User-Agent", "MSIE");         
        mockHttpServletRequest.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new LiteDeviceResolver().resolveDevice(mockHttpServletRequest));
    }
    
    public static void setupRequestAsMobileUserAgent(MockHttpServletRequest mockHttpServletRequest) {        
        mockHttpServletRequest.addHeader("User-Agent", "Blackberry");         
        mockHttpServletRequest.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new LiteDeviceResolver().resolveDevice(mockHttpServletRequest));
    }    
}

