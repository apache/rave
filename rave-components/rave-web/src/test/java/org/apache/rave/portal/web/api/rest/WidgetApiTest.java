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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetCommentService;
import org.apache.rave.portal.service.WidgetRatingService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class WidgetApiTest {
    private WidgetApi widgetApi;
    private WidgetCommentService widgetCommentService;
    private WidgetRatingService widgetRatingService;
    private UserService userService;
    private MockHttpServletResponse response;

    private final Long VALID_USER_ID = 5L;
    private final Long VALID_WIDGET_ID = 10L;
    
    private User user;

    @Before
    public void setup() {
        widgetCommentService = createMock(WidgetCommentService.class);
        widgetRatingService = createMock(WidgetRatingService.class);
        userService = createMock(UserService.class);

        user = new User();
        user.setEntityId(VALID_USER_ID);

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
        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService, httpServletResponse);

        widgetApi.createWidgetComment(1L, comment, httpServletResponse);

        verify(userService, httpServletResponse);
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

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(widgetCommentService.getWidgetComment(3L)).andReturn(null);
        widgetCommentService.saveWidgetComment(widgetComment);
        replay(userService, widgetCommentService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.updateWidgetComment(2L, 3L, message, httpServletResponse);

        verify(userService, widgetCommentService, httpServletResponse);
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

        verify(widgetCommentService, httpServletResponse);
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
        expect(userService.getAuthenticatedUser()).andReturn(user);
        widgetRatingService.removeWidgetRating(1L, VALID_USER_ID);
        expectLastCall();
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(userService, widgetRatingService, response);
        widgetApi.deleteWidgetRating(1L, response);
        verify(widgetRatingService, userService, response);
    }

    @Test
    public void updateWidgetRating() {
        WidgetRating widgetRating = new WidgetRating();
        widgetRating.setScore(5);
        widgetRating.setUserId(2L);
        widgetRating.setWidgetId(1L);
        expect(userService.getAuthenticatedUser()).andReturn(user);
        widgetRatingService.saveWidgetRating(widgetRating);
        expectLastCall();
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(userService, widgetRatingService, response);

        User user = new User(2L);
        widgetApi.setWidgetRating(1L, 5, response);

        verify(widgetRatingService, userService, response);
    }

    @Test
    public void getAllUsers() {
        List<Person> persons = new ArrayList<Person>();
        persons.add(new Person());
        persons.add(new Person());
        
        expect(userService.getAllByAddedWidget(VALID_WIDGET_ID)).andReturn(persons);
        replay(userService);
        assertThat(widgetApi.getAllUsers(VALID_WIDGET_ID), sameInstance(persons));

        verify(userService);
    }
}
