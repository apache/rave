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

import org.apache.rave.model.*;
import org.apache.rave.portal.service.PageLayoutService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.controller.util.ControllerUtils;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Controller
 *
 * @author carlucci
 */
@Controller
public class PageController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PageService pageService;
    private UserService userService;
    private PageLayoutService pageLayoutService;

    @Autowired
    public PageController(PageService pageService, UserService userService, PageLayoutService pageLayoutService) {
        this.pageService = pageService;
        this.userService = userService;
        this.pageLayoutService = pageLayoutService;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return ViewNames.LOGIN_PAGE;
    }


    @RequestMapping(value = {"/page/view", "/index.html"}, method = RequestMethod.GET)
    public String viewDefault(Model model, HttpServletRequest request) {
        List<Page> pages = getAllPagesForAuthenticatedUser();
        Page page = pageService.getDefaultPageFromList(pages);
        PageUser currentPageUser = null;
        User thisUser = userService.getAuthenticatedUser();
        for(PageUser pageUser : page.getMembers()){
            if(pageUser.getUserId().equals(thisUser.getId())){
                currentPageUser = pageUser;
            }
        }
        List<PageLayout> pageLayouts = pageLayoutService.getAllUserSelectable();
        addAttributesToModel(model, page, currentPageUser, pages, pageLayouts);
        String view = ControllerUtils.getDeviceAppropriateView(request, ViewNames.getPageView(page.getPageLayout().getCode()), ViewNames.MOBILE_HOME);
        ControllerUtils.addNavItemsToModel(view, model, page.getId(), userService.getAuthenticatedUser(), currentPageUser.isEditor());
        return view;
    }

    @RequestMapping(value = "/page/view/{pageId}", method = RequestMethod.GET)
    public String view(@PathVariable String pageId, Model model, HttpServletRequest request) {
        try {
            List<Page> pages = getAllPagesForAuthenticatedUser();
            Page page = pageService.getPageFromList(pageId, pages);
            PageUser currentPageUser = null;
            User thisUser = userService.getAuthenticatedUser();
            for(PageUser pageUser : page.getMembers()){
                if(pageUser.getUserId().equals(thisUser.getId())){
                    currentPageUser = pageUser;
                }
            }
            List<PageLayout> pageLayouts = pageLayoutService.getAllUserSelectable();
            addAttributesToModel(model, page, currentPageUser, pages, pageLayouts);
            String view = ControllerUtils.getDeviceAppropriateView(request, ViewNames.getPageView(page.getPageLayout().getCode()), ViewNames.MOBILE_HOME);
            ControllerUtils.addNavItemsToModel(view, model, page.getId(), thisUser, currentPageUser.isEditor());
            return view;
        } catch (Exception e) {
            logger.info("unable to get page - possibly because a shared page was revoked by its owner");
        }
        // Page could not be found or a shared page was removed, in which case return to default view
        return viewDefault(model, request);
    }

    private List<Page> getAllPagesForAuthenticatedUser() {
        User user = userService.getAuthenticatedUser();
        String userId = user.getId();
        List<Page> pages = pageService.getAllUserPages(userId);
        // we add pages to this list which the corresponding pageUser object is not set to "refused"
        List<Page> viewablePages = new ArrayList<Page>();
        for(Page page : pages){
            for(PageUser pageUser : page.getMembers()){
                if(pageUser != null && pageUser.getUserId().equals(user.getId()) && !pageUser.getPageStatus().equals(PageInvitationStatus.REFUSED)){
                    viewablePages.add(page);
                }
            }
        }
        if (viewablePages.isEmpty()) {
            // create a new default page for the user
            logger.info("User {} does not have any pages - creating default page", user.getUsername());
            pageService.addNewDefaultUserPage(userId);
            // refresh the pages list which will now have the new page
            viewablePages = pageService.getAllUserPages(userId);
        }
        return viewablePages;
    }

    private void addAttributesToModel(Model model, Page page, PageUser pageUser, List<Page> pages, List<PageLayout> pageLayouts) {
        model.addAttribute(ModelKeys.PAGE, page);
        model.addAttribute(ModelKeys.PAGES, pages);
        model.addAttribute(ModelKeys.PAGE_LAYOUTS, pageLayouts);
        model.addAttribute(ModelKeys.PAGE_USER, pageUser);
    }
}
