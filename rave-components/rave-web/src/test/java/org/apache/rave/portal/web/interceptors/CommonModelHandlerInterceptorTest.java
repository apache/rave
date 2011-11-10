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

import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test for {@link CommonModelHandlerInterceptor}
 */
public class CommonModelHandlerInterceptorTest {

    CommonModelHandlerInterceptor interceptor;
    PortalPreferenceService portalPreferenceService;

    @Before
    public void setUp() throws Exception {
        portalPreferenceService = createMock(PortalPreferenceService.class);
        interceptor = new CommonModelHandlerInterceptor(portalPreferenceService);
    }

    @Test
    public void testPostHandle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = null;
        ModelAndView modelAndView = new ModelAndView();

        Map<String, PortalPreference> preferenceMap = new HashMap<String, PortalPreference>();

        expect(portalPreferenceService.getPreferencesAsMap()).andReturn(preferenceMap);
        replay(portalPreferenceService);
        interceptor.postHandle(request, response, handler, modelAndView);
        verify(portalPreferenceService);

        assertTrue(modelAndView.getModelMap().containsKey(ModelKeys.PORTAL_SETTINGS));
    }
    @Test
    public void testPostHandle_noModelAndView() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = null;
        ModelAndView modelAndView = null;

        interceptor.postHandle(request, response, handler, modelAndView);
        assertNull(modelAndView);
    }
}
