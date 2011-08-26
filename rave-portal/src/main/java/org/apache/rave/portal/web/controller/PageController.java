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
package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.provider.opensocial.config.OpenSocialEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Page Controller
 * 
 * @author carlucci
 */
@Controller
//@RequestMapping(value={"/page/*","/index.html"})
public class PageController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
         
    private PageService pageService;
    private UserService userService;

    //TODO (RAVE-99) Figure out a better way to register script blocks so that we don't have to have this cross package dep
    @Autowired
    private OpenSocialEnvironment openSocialEnvironment;

    @Autowired
    public PageController(PageService pageService, UserService userService) {
        this.pageService = pageService;
        this.userService = userService;
    }
 
    @RequestMapping(value = {"/page/view", "/index.html"}, method = RequestMethod.GET)
    public String viewDefault(Model model) {
        User user = userService.getAuthenticatedUser();
        List<Page> pages = pageService.getAllPages(user.getId());
        model.addAttribute(ModelKeys.PAGE, pages.get(0));
        model.addAttribute(ModelKeys.PAGES, pages);
        model.addAttribute(ModelKeys.OPENSOCIAL_ENVIRONMENT, openSocialEnvironment);
        return ViewNames.HOME;
    }          
    
    @RequestMapping(value = "/page/view/{pageId}", method = RequestMethod.GET)
    public String view(@PathVariable Long pageId, Model model) {
        User user = userService.getAuthenticatedUser();
        logger.debug("attempting to get pageId " + pageId + " for " + user);
        
        List<Page> pages = pageService.getAllPages(user.getId());
        Page page = pageService.getPageFromList(pageId, pages);
               
        model.addAttribute(ModelKeys.PAGE, page);
        model.addAttribute(ModelKeys.PAGES, pages);
        model.addAttribute(ModelKeys.OPENSOCIAL_ENVIRONMENT, openSocialEnvironment);
        return ViewNames.HOME;
    }
}