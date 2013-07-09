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

import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ControllerUtilsTest {
    private MockHttpServletRequest request;
    private DeviceResolver deviceResolver;
    private User user;
    
    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        deviceResolver = new LiteDeviceResolver();
        user = new UserImpl();
        user.setDisplayName("Test");
    }
    
    @Test
    public void testIsMobileDevice_mobileClient() {
        MockHttpUtil.setupRequestAsMobileUserAgent(request);        
        assertThat(ControllerUtils.isMobileDevice(request), is(true));       
    }
    
    @Test
    public void testIsMobileDevice_nonMobileClient() {
        MockHttpUtil.setupRequestAsNonMobileUserAgent(request);        
        assertThat(ControllerUtils.isMobileDevice(request), is(false));       
    }

    @Test
    public void testGetDeviceAppropriateView_defaultView() {
        String defaultView = "defaultView";
        String mobileView = "mobileView";

        request.addHeader("User-Agent", "MSIE");
        request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, deviceResolver.resolveDevice(request));
        assertThat(ControllerUtils.getDeviceAppropriateView(request, defaultView, mobileView), is(defaultView));
    }

    @Test
    public void testGetDeviceAppropriateView_mobileView() {
        String defaultView = "defaultView";
        String mobileView = "mobileView";

        request.addHeader("User-Agent", "Blackberry");
        request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, deviceResolver.resolveDevice(request));
        assertThat(ControllerUtils.getDeviceAppropriateView(request, defaultView, mobileView), is(mobileView));
    }

    @Test
    public void getDisplayName_withDisplayName(){
        assertEquals(user.getDisplayName(), ControllerUtils.getDisplayName(user));
    }

    @Test
    public void getDisplayName_withoutDisplayName(){
        user.setDisplayName("");
        user.setUsername("username");
        assertEquals(user.getUsername(), ControllerUtils.getDisplayName(user));
    }

    @Test
    public void getDisplayName_withNullDisplayName(){
        user.setDisplayName(null);
        user.setUsername("username");
        assertEquals(user.getUsername(), ControllerUtils.getDisplayName(user));
    }
}