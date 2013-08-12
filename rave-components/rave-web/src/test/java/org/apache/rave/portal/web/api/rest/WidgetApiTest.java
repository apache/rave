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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.service.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class WidgetApiTest {
    private WidgetApi widgetApi;
    private UserService userService;
    private WidgetService widgetService;
    private TagService tagService;
    private MockHttpServletResponse response;

    private final String VALID_USER_ID = "5";
    private final String VALID_WIDGET_ID = "10";

    private UserImpl user;
    private List<Tag> tagList;

    @Before
    public void setup() {
        userService = createMock(UserService.class);
        tagService = createMock(TagService.class);
        widgetService = createMock(WidgetService.class);

        user = new UserImpl();
        user.setId(VALID_USER_ID);

        tagList = new ArrayList<Tag>();
        tagList.add(new TagImpl("1", "tag1"));
        tagList.add(new TagImpl("2", "tag2"));

        response = createMock(MockHttpServletResponse.class);
        widgetApi = new WidgetApi(userService, tagService, widgetService);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAllWidgets() {
        widgetApi.getAllWidgets();
    }

    @Test
    public void createWidgetComment() {
        String comment = "new comment";

        WidgetImpl widget = new WidgetImpl();
        widget.setId("1");
        widget.setComments(new ArrayList<WidgetComment>());

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService, httpServletResponse);

        widgetApi.createWidgetComment("1", comment, httpServletResponse);

        verify(userService, httpServletResponse);
    }

    @Test
    public void getWidgetComment() {
        WidgetComment widgetComment = new WidgetCommentImpl("2");
        expect(widgetService.getWidgetComment("1", "2")).andReturn(widgetComment);
        replay(widgetService);

        WidgetComment foundComment = widgetApi.getWidgetComment("1", "2");
        assertEquals(widgetComment.getId(), foundComment.getId());

        verify(widgetService);
    }

    @Test
    public void updateNonExistentWidgetComment() {
        String message = "message";
        WidgetComment widgetComment = new WidgetCommentImpl();
        widgetComment.setText(message);
        widgetComment.setUserId(VALID_USER_ID);

        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(widgetService.getWidgetComment("2", "3")).andReturn(null);
        widgetService.createWidgetComment("2", widgetComment);
        replay(userService, widgetService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.updateWidgetComment("2", "3", message, httpServletResponse);

        verify(userService, widgetService, httpServletResponse);
    }

    @Test
    public void updateWidgetComment() {
        String message = "updated comment";
        WidgetComment widgetComment = new WidgetCommentImpl("2");

        expect(widgetService.getWidgetComment("2", "2")).andReturn(widgetComment);
        widgetService.updateWidgetComment("2", widgetComment);
        replay(widgetService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.updateWidgetComment("2", "2", message, httpServletResponse);

        assertEquals(message, widgetComment.getText());

        verify(widgetService, httpServletResponse);
    }

    @Test
    public void deleteWidgetComment() {
        widgetService.removeWidgetComment("2", "1");
        replay(widgetService);

        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
        replay(httpServletResponse);

        widgetApi.deleteWidgetComment("2", "1", httpServletResponse);

        verify(widgetService);
        verify(httpServletResponse);
    }

    @Test
    public void deleteWidgetRating() {
        expect(userService.getAuthenticatedUser()).andReturn(user);
        widgetService.removeWidgetRating("1", VALID_USER_ID);
        expectLastCall();
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(userService, widgetService, response);
        widgetApi.deleteWidgetRating("1", response);
        verify(widgetService, userService, response);
    }

    @Test
    public void updateWidgetRating() {
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setScore(5);
        widgetRating.setUserId("5");
        expect(userService.getAuthenticatedUser()).andReturn(user);
        widgetService.saveWidgetRating(eq("1"), eq(widgetRating));
        expectLastCall();
        response.setStatus(HttpStatus.NO_CONTENT.value());
        replay(userService, widgetService, response);

        User user = new UserImpl("2");
        widgetApi.setWidgetRating("1", 5, response);

        verify(widgetService, userService, response);
    }

    @Test
    public void getAllUsers() {
        List<Person> persons = new ArrayList<Person>();
        persons.add(new PersonImpl());
        persons.add(new PersonImpl());

        expect(userService.getAllByAddedWidget(VALID_WIDGET_ID)).andReturn(persons);
        replay(userService);
        assertThat(widgetApi.getAllUsers(VALID_WIDGET_ID), sameInstance(persons));

        verify(userService);
    }
    
    @Test
    public void getTags() {
        WidgetImpl widget = new WidgetImpl(VALID_WIDGET_ID);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tagList.get(0).getId());
        widget.getTags().add(widgetTag);

        expect(widgetService.getWidget(VALID_WIDGET_ID)).andReturn(widget);
        replay(widgetService);
        expect(tagService.getTagById(tagList.get(0).getId())).andReturn(tagList.get(0));
        replay(tagService);

        List<Tag> tags = widgetApi.getTags(VALID_WIDGET_ID, response);
        assertEquals(tags.size(), 1);
        verify(tagService);
    }

    @Test
    public void getAllTags() {
        expect(tagService.getAllTagsList()).andReturn(tagList);
        replay(tagService);

        assertThat(widgetApi.getAllTags(), is(tagList));
        verify(tagService);
    }

    @Test
    public void createWidgetTag_newTag() {
        final String TAG_TEXT = "mytag";
        TagImpl tag = new TagImpl(TAG_TEXT);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tag.getId());
                        
        expect(widgetService.getWidgetTagByWidgetIdAndKeyword(VALID_WIDGET_ID, TAG_TEXT)).andReturn(null);
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(tagService.getTagByKeyword(TAG_TEXT)).andReturn(null);
        expect(tagService.save(isA(TagImpl.class))).andReturn(tag);
        expect(widgetService.createWidgetTag(isA(String.class), isA(WidgetTag.class))).andReturn(widgetTag);
        expectLastCall();
        replay(widgetService, userService, tagService);
        widgetApi.createWidgetTag(VALID_WIDGET_ID, TAG_TEXT, response);
        verify(widgetService, userService, tagService);
    }

    @Test
    public void createWidgetTag_newTag_existsForOtherWidget() {
        final String TAG_TEXT = "mytag";
        TagImpl tag = new TagImpl(TAG_TEXT);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tag.getId());

        expect(widgetService.getWidgetTagByWidgetIdAndKeyword(VALID_WIDGET_ID, TAG_TEXT)).andReturn(null);
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(tagService.getTagByKeyword(TAG_TEXT)).andReturn(tag);
        expect(widgetService.createWidgetTag(isA(String.class), isA(WidgetTag.class))).andReturn(widgetTag);
        expectLastCall();
        replay(widgetService, userService, tagService);
        widgetApi.createWidgetTag(VALID_WIDGET_ID, TAG_TEXT, response);
        verify(widgetService, userService, tagService);
    }

    @Test
    public void createWidgetTag_nullText() {
        final String TAG_TEXT = null;
        TagImpl tag = new TagImpl(TAG_TEXT);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tag.getId());

        widgetApi.createWidgetTag(VALID_WIDGET_ID, TAG_TEXT, response);
    }

    @Test
    public void createWidgetTag_emptyText() {
        final String TAG_TEXT = "      ";
        TagImpl tag = new TagImpl(TAG_TEXT);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tag.getId());

        widgetApi.createWidgetTag(VALID_WIDGET_ID, TAG_TEXT, response);
    }

    @Test
    public void createWidgetTag_existingTag() {
        final String TAG_TEXT = "mytag";
        TagImpl tag = new TagImpl(TAG_TEXT);
        WidgetTagImpl widgetTag = new WidgetTagImpl(VALID_USER_ID, new Date(), tag.getId());

        expect(widgetService.getWidgetTagByWidgetIdAndKeyword(VALID_WIDGET_ID, TAG_TEXT)).andReturn(widgetTag);
        replay(widgetService);
        widgetApi.createWidgetTag(VALID_WIDGET_ID, TAG_TEXT, response);
        verify(widgetService);
    }
}
