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
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.apache.rave.portal.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handler for all services exposed under the /api/widgets path.
 */
@Controller
@RequestMapping("/api/rest/widgets")
public class WidgetApi extends AbstractRestApi {
    private static Logger logger = LoggerFactory.getLogger(WidgetApi.class);
    private final UserService userService;
    private final TagService tagService;
    private final WidgetService widgetService;


    @Autowired
    public WidgetApi(UserService userService,
                     TagService tagService, WidgetService widgetService) {
        this.userService = userService;
        this.tagService = tagService;
        this.widgetService = widgetService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void getAllWidgets() {
        logger.debug("GET received for all widgets");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{widgetId}/comments")
    public void createWidgetComment(@PathVariable String widgetId,
                                    @RequestParam String text,
                                    HttpServletResponse response) {
        WidgetComment widgetComment = new WidgetCommentImpl();
        widgetComment.setUserId(userService.getAuthenticatedUser().getId());
        widgetComment.setText(text);
        widgetComment.setCreatedDate(new Date());
        widgetComment.setLastModifiedDate(new Date());

        widgetService.createWidgetComment(widgetId, widgetComment);

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{widgetId}/comments/{widgetCommentId}")
    public WidgetComment getWidgetComment(@PathVariable String widgetId,
                                          @PathVariable String widgetCommentId) {
        return widgetService.getWidgetComment(widgetId, widgetCommentId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{widgetId}/comments/{widgetCommentId}")
    public void updateWidgetComment(@PathVariable String widgetId,
                                    @PathVariable String widgetCommentId,
                                    @RequestParam String text,
                                    HttpServletResponse response) {

        WidgetComment widgetComment = widgetService.getWidgetComment(widgetId, widgetCommentId);
        if (widgetComment == null) {
            widgetComment = new WidgetCommentImpl();
            widgetComment.setUserId(userService.getAuthenticatedUser().getId());
            widgetComment.setCreatedDate(new Date());
            widgetComment.setLastModifiedDate(new Date());
            widgetComment.setText(text);
            widgetService.createWidgetComment(widgetId, widgetComment);
        } else {
            widgetComment.setText(text);
            widgetService.updateWidgetComment(widgetId, widgetComment);
        }

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{widgetId}/comments/{widgetCommentId}")
    public void deleteWidgetComment(@PathVariable String widgetId,
                                    @PathVariable String widgetCommentId,
                                    HttpServletResponse response) {
        widgetService.removeWidgetComment(widgetId, widgetCommentId);

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }


    @RequestMapping(value = "/{widgetId}/rating", method = RequestMethod.DELETE)
    public void deleteWidgetRating(@PathVariable String widgetId,
                                   HttpServletResponse response) {
        logger.debug("DELETE WidgetRating received for /api/rest/widgets/{}", widgetId);

        widgetService.removeWidgetRating(widgetId, userService.getAuthenticatedUser().getId());

        // send a 204 back for success since there is no content being returned
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @RequestMapping(value = "/{widgetId}/rating", method = RequestMethod.POST)
    public void setWidgetRating(@PathVariable String widgetId,
                                @RequestParam(value = "score") Integer score,
                                HttpServletResponse response) {
        logger.debug("POST WidgetRating received for /api/rest/widgets/{} score: {}", widgetId, score);

        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setScore(score);
        widgetRating.setUserId(userService.getAuthenticatedUser().getId());
        widgetService.saveWidgetRating(widgetId, widgetRating);

        // send a 204 back for success since there is no content being returned
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{widgetId}/users")
    public List<Person> getAllUsers(@PathVariable String widgetId) {
        return userService.getAllByAddedWidget(widgetId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{widgetId}/tags")
    public void createWidgetTag(@PathVariable String widgetId,
                                @RequestParam String tagText,
                                HttpServletResponse response) {
        logger.debug("add tags " + tagText + " to widget " + widgetId);
        if (tagText != null && !tagText.trim().isEmpty()) {
            WidgetTag existed = widgetService.getWidgetTagByWidgetIdAndKeyword(widgetId, tagText);
            if (existed == null) {
                WidgetTag widgetTag = new WidgetTagImpl(userService.getAuthenticatedUser(), new Date(), getTag(tagText));
                widgetService.createWidgetTag(widgetId, widgetTag);
                logger.debug("widget tag is saved.");

            }

        }

        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{widgetId}/tags")
    public List<Tag> getTags(@PathVariable String widgetId, HttpServletResponse response) {
        ArrayList<Tag> tags = new ArrayList<Tag>();

        Widget widget = widgetService.getWidget(widgetId);

        if (widget == null) {
            response.setStatus(404);
            return null;
        }

        for (WidgetTag wt : widget.getTags()) {
            tags.add(tagService.getTagById(wt.getTagId()));
        }

        return tags;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/tags")
    public List<Tag> getAllTags() {
        return tagService.getAllTagsList();
    }

    private Tag getTag(String keyword) {
        Tag tag = tagService.getTagByKeyword(keyword);
        if (tag == null) {
            tag = new TagImpl(keyword);
            tag = tagService.save(tag);
        }
        return tag;
    }
}
