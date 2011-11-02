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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetCommentService;
import org.apache.rave.portal.service.WidgetRatingService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

public class WidgetApiTest {
    private WidgetApi widgetApi;
    private WidgetCommentService widgetCommentService;
    private WidgetRatingService widgetRatingService;
    private UserService userService;
    private MockHttpServletResponse response;

    private final Long VALID_USER_ID = 5L;

    @Before
    public void setup() {
        widgetCommentService = createMock(WidgetCommentService.class);
        widgetRatingService = createMock(WidgetRatingService.class);
        userService = createMock(UserService.class);

        User user = new User();
        user.setEntityId(VALID_USER_ID);
        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService);

        response = createMock(MockHttpServletResponse.class);
        widgetApi = new WidgetApi(widgetRatingService, widgetCommentService, userService);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAllWidgets() {
        widgetApi.getAllWidgets();
    }

    @Test
    public void createWidgetComment() {
        String comment = "new comment";

        Widget widget = new Widget();
        widget.setEntityId(1L);
        widget.setComments(new ArrayList<WidgetComment>());

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.createWidgetComment(1L, comment, httpServletResponse);

        verify(userService);
        verify(httpServletResponse);
    }

    @Test
    public void getWidgetComment() {
        WidgetComment widgetComment = new WidgetComment();
        widgetComment.setEntityId(2L);
        expect(widgetCommentService.getWidgetComment(2L)).andReturn(widgetComment);
        replay(widgetCommentService);

        WidgetComment foundComment = widgetApi.getWidgetComment(1L, 2L);
        assertEquals(widgetComment.getEntityId(), foundComment.getEntityId());

        verify(widgetCommentService);
    }

    @Test
    public void updateNonExistentWidgetComment() {
        String message = "message";
        WidgetComment widgetComment = new WidgetComment();
        widgetComment.setWidgetId(2L);
        widgetComment.setText(message);
        widgetComment.setUser(new User(VALID_USER_ID, "John.Doe"));

        expect(widgetCommentService.getWidgetComment(3L)).andReturn(null);
        widgetCommentService.saveWidgetComment(widgetComment);
        replay(widgetCommentService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.updateWidgetComment(2L, 3L, message, httpServletResponse);

        verify(widgetCommentService);
        verify(httpServletResponse);
    }

    @Test
    public void updateWidgetComment() {
        String message = "updated comment";
        WidgetComment widgetComment = new WidgetComment();
        widgetComment.setEntityId(2L);

        expect(widgetCommentService.getWidgetComment(2L)).andReturn(widgetComment);
        widgetCommentService.saveWidgetComment(widgetComment);
        replay(widgetCommentService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.updateWidgetComment(1L, 2L, message, httpServletResponse);

        assertEquals(message, widgetComment.getText());

        verify(widgetCommentService);
        verify(httpServletResponse);
    }

    @Test
    public void deleteWidgetComment() {
        widgetCommentService.removeWidgetComment(1L);
        replay(widgetCommentService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.deleteWidgetComment(2L, 1L, httpServletResponse);

        verify(widgetCommentService);
        verify(httpServletResponse);
    }

    @Test
    public void deleteWidgetRating() {
        widgetRatingService.removeWidgetRating(1L, VALID_USER_ID);
        expectLastCall();
        replay(widgetRatingService);

        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(response);

        widgetApi.deleteWidgetRating(1L, response);

        verify(widgetRatingService, userService);
        verify(response);
    }

    @Test
    public void updateWidgetRating() {
        widgetRatingService.saveWidgetRating(1L, 5, VALID_USER_ID);
        expectLastCall();
        replay(widgetRatingService);

        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(response);

        widgetApi.setWidgetRating(1L, 5, response);

        verify(widgetRatingService, userService);
        verify(response);
    }
}
