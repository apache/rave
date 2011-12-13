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

import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetCommentService;
import org.apache.rave.portal.service.WidgetRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Handler for all services exposed under the /api/widgets path.
 */
@Controller
@RequestMapping("/api/rest/widgets")
public class WidgetApi extends AbstractRestApi {
    private static Logger logger = LoggerFactory.getLogger(WidgetApi.class);
    private final WidgetCommentService widgetCommentService;
    private final WidgetRatingService widgetRatingService;
    private final UserService userService;
    
    @Autowired
    public WidgetApi(WidgetRatingService widgetRatingService, WidgetCommentService widgetCommentService, UserService userService) {
        this.widgetCommentService = widgetCommentService;
        this.widgetRatingService = widgetRatingService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void getAllWidgets() {
        logger.debug("GET received for all widgets");
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/{widgetId}/comments")
    public void createWidgetComment(@PathVariable long widgetId,
                                 @RequestParam String text,
                                 HttpServletResponse response) {
        WidgetComment widgetComment = new WidgetComment();
        widgetComment.setWidgetId(widgetId);
        widgetComment.setUser(userService.getAuthenticatedUser());
        widgetComment.setText(text);
        widgetComment.setCreatedDate(new Date());
        widgetComment.setLastModifiedDate(new Date());
        
        widgetCommentService.saveWidgetComment(widgetComment);
        
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/{widgetId}/comments/{widgetCommentId}")
    public WidgetComment getWidgetComment(@PathVariable long widgetId,
                                          @PathVariable long widgetCommentId) {
        return widgetCommentService.getWidgetComment(widgetCommentId);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/{widgetId}/comments/{widgetCommentId}")
    public void updateWidgetComment(@PathVariable long widgetId,
                                    @PathVariable long widgetCommentId,
                                    @RequestParam String text,
                                    HttpServletResponse response) {
        
        WidgetComment widgetComment = widgetCommentService.getWidgetComment(widgetCommentId);
        if (widgetComment == null) {
            widgetComment = new WidgetComment();
            widgetComment.setWidgetId(widgetId);
            widgetComment.setUser(userService.getAuthenticatedUser());
            widgetComment.setCreatedDate(new Date());
            widgetComment.setLastModifiedDate(new Date());
        }
        widgetComment.setText(text);
        
        widgetCommentService.saveWidgetComment(widgetComment);
        
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{widgetId}/comments/{widgetCommentId}")
    public void deleteWidgetComment(@PathVariable long widgetId,
                                    @PathVariable long widgetCommentId,
                                    HttpServletResponse response) {
        widgetCommentService.removeWidgetComment(widgetCommentId);
        
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
    
    
    @RequestMapping(value = "/{widgetId}/rating", method = RequestMethod.DELETE)
    public void deleteWidgetRating(@PathVariable long widgetId, 
                            HttpServletResponse response) {
        logger.debug("DELETE WidgetRating received for /api/rest/widgets/{}", widgetId);

        widgetRatingService.removeWidgetRating(widgetId, userService.getAuthenticatedUser().getEntityId());
        
        // send a 204 back for success since there is no content being returned
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
    
    @RequestMapping(value = "/{widgetId}/rating", method = RequestMethod.POST)
    public void setWidgetRating(@PathVariable long widgetId,
                                    @RequestParam(value = "score") Integer score,
                                    HttpServletResponse response) {
        logger.debug("POST WidgetRating received for /api/rest/widgets/{} score: {}", widgetId, score);

        WidgetRating widgetRating = new WidgetRating();
        widgetRating.setScore(score);
        widgetRating.setUserId(userService.getAuthenticatedUser().getEntityId());
        widgetRating.setWidgetId(widgetId);
        widgetRatingService.saveWidgetRating(widgetRating);
        
        // send a 204 back for success since there is no content being returned
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
