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

import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.TagService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/store/*", "/store"})
public class WidgetStoreController {

    private static final int MAXIMUM_WIDGETS_PER_PAGE = 10;

    private final WidgetService widgetService;

    private final NewWidgetValidator widgetValidator;

    private final UserService userService;

    private final PortalPreferenceService preferenceService;

    private final TagService tagService;


    @Autowired
    public WidgetStoreController(WidgetService widgetService, NewWidgetValidator validator,
                                 UserService userService, PortalPreferenceService preferenceService,
                                 TagService tagService) {
        this.widgetService = widgetService;
        this.widgetValidator = validator;
        this.userService = userService;
        this.preferenceService = preferenceService;
        this.tagService=tagService;

    }

    /**
     * Views the main page of the widget store
     *
     * @param model           model map
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @param offset          offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model, @RequestParam long referringPageId,
                       @RequestParam(required = false, defaultValue = "0") int offset) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getPublishedWidgets(offset, getPageSize()));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.WIDGETS_STATISTICS, widgetService.getAllWidgetStatistics(user.getEntityId()));
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTags());
        return ViewNames.STORE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "mine")
    public String viewMine(Model model, @RequestParam long referringPageId,
                           @RequestParam(required = false, defaultValue = "0") int offset) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getWidgetsByOwner(user.getEntityId(), offset, getPageSize()));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.WIDGETS_STATISTICS, widgetService.getAllWidgetStatistics(user.getEntityId()));
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTags());
        return ViewNames.STORE;
    }

    /**
     * Views details of the specified widget
     *
     * @param model           model map
     * @param widgetId        ID of the {@link org.apache.rave.portal.model.Widget } to view
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @return the view name of the widget detail page
     */
    @RequestMapping(method = RequestMethod.GET, value = "widget/{widgetId}")
    public String viewWidget(Model model, @PathVariable long widgetId, @RequestParam long referringPageId) {
        model.addAttribute(ModelKeys.WIDGET, widgetService.getWidget(widgetId));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        final User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.WIDGET_STATISTICS, widgetService.getWidgetStatistics(widgetId, user.getEntityId()));
        model.addAttribute(ModelKeys.USER_PROFILE, user);
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTags());
        return ViewNames.WIDGET;
    }

    /**
     * Performs a search in the widget store
     *
     * @param model           {@link Model} map
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @param searchTerm      free text searchTerm query
     * @param offset          offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public String viewSearchResult(Model model, @RequestParam long referringPageId,
                                   @RequestParam String searchTerm,
                                   @RequestParam(required = false, defaultValue = "0") int offset) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm, offset, getPageSize()));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.OFFSET, offset);
        model.addAttribute(ModelKeys.WIDGETS_STATISTICS, widgetService.getAllWidgetStatistics(user.getEntityId()));
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTags());
       return ViewNames.STORE;
    }

    /**
     * Performs a search in the widget store by tag keyword
     *
     * @param model           {@link Model} map
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @param keyword     free text tag keyword
     * @param offset          offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET, value = "tag")
    public String viewTagResult(Model model, @RequestParam long referringPageId,
                                   @RequestParam String keyword,
                                   @RequestParam(required = false, defaultValue = "0") int offset) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getWidgetsByTag(keyword, offset, getPageSize()));
         model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.OFFSET, offset);
        model.addAttribute(ModelKeys.WIDGETS_STATISTICS, widgetService.getAllWidgetStatistics(user.getEntityId()));
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTags());
        model.addAttribute(ModelKeys.SELECTED_TAG, keyword);
        return ViewNames.STORE;
    }

    /**
     * Shows the Add new Widget form
     *
     * @param model           {@link Model}
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @return the view name of the Add new Widget form
     */
    @RequestMapping(method = RequestMethod.GET, value = "widget/add")
    public String viewAddWidgetForm(Model model, @RequestParam long referringPageId) {
        final Widget widget = new Widget();
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.WIDGET, widget);
        return ViewNames.ADD_WIDGET_FORM;
    }

    /**
     * Validates the form input, if valid, tries to store the Widget data
     *
     * @param widget          {@link Widget} as submitted by the user
     * @param results         {@link BindingResult}
     * @param model           {@link Model}
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @return if successful the view name of the widget, otherwise the form
     */
    @RequestMapping(method = RequestMethod.POST, value = "widget/add")
    public String viewAddWidgetResult(@ModelAttribute Widget widget, BindingResult results,
                                      Model model, @RequestParam long referringPageId) {
        User user = userService.getAuthenticatedUser();
        widgetValidator.validate(widget, results);
        if (results.hasErrors()) {
            model.addAttribute(ModelKeys.WIDGET, widget);
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            return ViewNames.ADD_WIDGET_FORM;
        }
        widget.setWidgetStatus(WidgetStatus.PREVIEW);
        widget.setOwner(user);

        final Widget storedWidget = widgetService.registerNewWidget(widget);
        return "redirect:/app/store/widget/" + storedWidget.getEntityId() + "?referringPageId=" + referringPageId;
    }

    public int getPageSize() {
        final PortalPreference pageSizePref = preferenceService.getPreference(PortalPreferenceKeys.PAGE_SIZE);
        if (pageSizePref == null) {
            return MAXIMUM_WIDGETS_PER_PAGE;
        }
        try {
            return Integer.parseInt(pageSizePref.getValue());
        } catch (NumberFormatException e) {
            return MAXIMUM_WIDGETS_PER_PAGE;
        }
    }


}
