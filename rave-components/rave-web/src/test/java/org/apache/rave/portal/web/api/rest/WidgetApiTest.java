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

package org.apache.rave.portal.web.api.rest;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class WidgetApiTest {
    private WidgetApi widgetApi;
    private WidgetService widgetService;
    private UserService userService;
    private MockHttpServletResponse response;

    @Before
    public void setup() {
        widgetService = createMock(WidgetService.class);
        userService = createMock(UserService.class);
        response = createMock(MockHttpServletResponse.class);
        widgetApi = new WidgetApi(widgetService, userService);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAllWidgets() {
        widgetApi.getAllWidgets();
    }
    
    @Test
    public void deleteWidgetRating() {
        widgetService.removeWidgetRating(1L, 2L);
        expectLastCall();
        replay(widgetService);
        
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(response);

        User user = new User(2L);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService);
        widgetApi.deleteWidgetRating(1L, response);

        verify(widgetService, userService);
        verify(response);
    }
    
    @Test
    public void updateWidgetRating() {
        WidgetRating widgetRating = new WidgetRating();
        widgetRating.setScore(5);
        widgetRating.setUserId(2L);
        widgetRating.setWidgetId(1L);
        widgetService.saveWidgetRating(1L, widgetRating);
        expectLastCall();
        replay(widgetService);
        
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(response);

        User user = new User(2L);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService);

        widgetApi.setWidgetRating(1L, 5, response);
        
        verify(widgetService, userService);
        verify(response);
    }
}