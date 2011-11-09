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

import org.springframework.mock.web.MockHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

public class ControllerUtilsTest {
    private MockHttpServletRequest request;    
    
    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
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
}