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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.*;
import org.apache.rave.portal.web.controller.util.ControllerUtils;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = { "/store/*", "/store" })
public class WidgetStoreController {

    private static final int MAXIMUM_WIDGETS_PER_PAGE = 10;

    private final WidgetService widgetService;

    private final NewWidgetValidator widgetValidator;

    private final UserService userService;

    private final PortalPreferenceService preferenceService;

    private final TagService tagService;

    private final CategoryService categoryService;

    @Autowired
    public WidgetStoreController(WidgetService widgetService, NewWidgetValidator validator, UserService userService,
            PortalPreferenceService preferenceService, TagService tagService, CategoryService categoryService) {
        this.widgetService = widgetService;
        this.widgetValidator = validator;
        this.userService = userService;
        this.preferenceService = preferenceService;
        this.tagService = tagService;
        this.categoryService = categoryService;
    }

    /**
     * Views the main page of the widget store
     *
     * @param model
     *            model map
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @param offset
     *            offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model, @RequestParam String referringPageId,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        final String view = ViewNames.STORE;
        User user = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, user, view);
        model.addAttribute(ModelKeys.WIDGETS, widgetService.getPublishedWidgets(offset, getPageSize()));
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "mine")
    public String viewMine(Model model, @RequestParam String referringPageId,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        final String view = ViewNames.STORE;
        User user = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, user, view);
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getWidgetsByOwner(user.getId(), offset, getPageSize()));
        return view;
    }

    /**
     * Views details of the specified widget
     *
     * @param model
     *            model map
     * @param widgetId
     *            ID of the {@link org.apache.rave.model.Widget } to view
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @return the view name of the widget detail page
     */
    @RequestMapping(method = RequestMethod.GET, value = "widget/{widgetId}")
    public String viewWidget(Model model, @PathVariable String widgetId, @RequestParam String referringPageId) {
        final String view = ViewNames.WIDGET;
        final User user = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, user, view);
        model.addAttribute(ModelKeys.WIDGET, widgetService.getWidget(widgetId));
        model.addAttribute(ModelKeys.WIDGET_STATISTICS, widgetService.getWidgetStatistics(widgetId, user.getId()));
        model.addAttribute(ModelKeys.USER_PROFILE, user);
        return view;
    }

    /**
     * Performs a search in the widget store
     *
     * @param model
     *            {@link Model} map
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @param searchTerm
     *            free text searchTerm query
     * @param offset
     *            offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public String viewSearchResult(Model model, @RequestParam String referringPageId, @RequestParam String searchTerm,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        final String view = ViewNames.STORE;
        User user = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, user, view);
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm, offset, getPageSize()));
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.OFFSET, offset);
        return view;
    }

    /**
     * Performs a search in the widget store by tag keyword
     *
     * @param model
     *            {@link Model} map
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @param keyword
     *            free text tag keyword
     * @param offset
     *            offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET, value = "tag")
    public String viewTagResult(Model model, @RequestParam String referringPageId, @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        final String view = ViewNames.STORE;
        User user = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, user, view);
        model.addAttribute(ModelKeys.WIDGETS, widgetService.getWidgetsByTag(keyword, offset, getPageSize()));
        model.addAttribute(ModelKeys.OFFSET, offset);
        model.addAttribute(ModelKeys.SELECTED_TAG, keyword);
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "category")
    public String viewCategoryResult(@RequestParam(required = true) String referringPageId,
            @RequestParam(required = true) String categoryId,
            @RequestParam(required = false, defaultValue = "0") int offset, Model model) {
        final String view = ViewNames.STORE;
        User authenticatedUser = userService.getAuthenticatedUser();
        widgetStoreModelHelper(model, referringPageId, authenticatedUser, view);
        if (categoryId != null && !categoryId.isEmpty()) {
            model.addAttribute(ModelKeys.WIDGETS, widgetService.getWidgetsByCategory(categoryId, offset, getPageSize()));
        } else {
            model.addAttribute(ModelKeys.WIDGETS, widgetService.getPublishedWidgets(offset, getPageSize()));
        }
        model.addAttribute(ModelKeys.OFFSET, offset);
        model.addAttribute(ModelKeys.SELECTED_CATEGORY, categoryId);
        return view;
    }

    /**
     * Shows the Add new Widget form
     *
     * @param model
     *            {@link Model}
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @return the view name of the Add new Widget form
     */
    @RequestMapping(method = RequestMethod.GET, value = "widget/add")
    public String viewAddWidgetForm(Model model, @RequestParam String referringPageId) {
        final Widget widget = new WidgetImpl();
        final String view = ViewNames.ADD_WIDGET_FORM;
        model.addAttribute(ModelKeys.MARKETPLACE, this.preferenceService.getPreference(PortalPreferenceKeys.EXTERNAL_MARKETPLACE_URL));
        model.addAttribute(ModelKeys.WIDGET, widget);
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        ControllerUtils.addNavItemsToModel(view, model, referringPageId, userService.getAuthenticatedUser());
        return view;
    }
    
    /**
     * Shows the Add new Widget form
     *
     * @param model
     *            {@link Model}
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @param type
     *            the type of widget add form to display, e.g. W3C or OpenSocial (default)
     * @return the view name of the Add new Widget form
     */
    @RequestMapping(method = RequestMethod.GET, value = "widget/add/{type}")
    public String viewAddWidgetFormByType(Model model, @RequestParam String referringPageId, @PathVariable String type) {
        final Widget widget = new WidgetImpl();
        String view;
        if (type != null && type.equalsIgnoreCase("w3c")){
        	view = ViewNames.ADD_WIDGET_W3C;
        } else {
        	view = ViewNames.ADD_WIDGET_FORM;
        }
        model.addAttribute(ModelKeys.MARKETPLACE, this.preferenceService.getPreference(PortalPreferenceKeys.EXTERNAL_MARKETPLACE_URL));
        model.addAttribute(ModelKeys.WIDGET, widget);
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        ControllerUtils.addNavItemsToModel(view, model, referringPageId, userService.getAuthenticatedUser());
        return view;
    }
    
    

    /**
     * Validates the form input, if valid, tries to store the Widget data
     *
     * @param widget
     *            {@link org.apache.rave.model.Widget} as submitted by the user
     * @param results
     *            {@link BindingResult}
     * @param model
     *            {@link Model}
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @return if successful the view name of the widget, otherwise the form
     */
    @RequestMapping(method = RequestMethod.POST, value = "widget/add")
    public String viewAddWidgetResult(@ModelAttribute WidgetImpl widget, BindingResult results, Model model,
            @RequestParam String referringPageId) {
        User user = userService.getAuthenticatedUser();
        widgetValidator.validate(widget, results);
        if (results.hasErrors()) {
        	final String view = ViewNames.ADD_WIDGET_FORM;
            model.addAttribute(ModelKeys.WIDGET, widget);
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            ControllerUtils.addNavItemsToModel(view, model, referringPageId, user);
            return view;
        }
        return finalizeNewWidget(widget,user, referringPageId);
    }
    
    /**
     * Validates the form input, if valid, tries to store the Widget data
     *
     * @param widget
     *            {@link org.apache.rave.model.Widget} as submitted by the user
     * @param results
     *            {@link BindingResult}
     * @param model
     *            {@link Model}
     * @param referringPageId
     *            the source {@link org.apache.rave.model.Page } ID
     * @return if successful the view name of the widget, otherwise the form
     */
    @RequestMapping(method = RequestMethod.POST, value = "widget/add/w3c")
    public String viewAddWidgetResultW3c(@ModelAttribute WidgetImpl widget, BindingResult results, Model model,
            @RequestParam String referringPageId) {
        User user = userService.getAuthenticatedUser();
        widgetValidator.validate(widget, results);
        if (results.hasErrors()) {
        	final String view = ViewNames.ADD_WIDGET_W3C;
            model.addAttribute(ModelKeys.WIDGET, widget);
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            ControllerUtils.addNavItemsToModel(view, model, referringPageId, user);
            return view;
        }
        
        return finalizeNewWidget(widget,user, referringPageId);
    }
    
    /**
     * Finalize adding a new widget created from validated form data, and redirect to its store detail page
     * @param widget
     *            {@link org.apache.rave.model.Widget} as created from form input
     * @param user
     *            the user submitting the new widget
     * @param referringPageId
     *            the source page ID
     * @return a redirection string for the store detail page.
     */
    private String finalizeNewWidget(WidgetImpl widget, User user, String referringPageId){
        /*
         * By default, a new widget has a status of "PREVIEW", however this can be overridden in portal preferences,
         * skipping the need for an admin to approve a new widget.
         */
        PortalPreference status = preferenceService.getPreference(PortalPreferenceKeys.INITIAL_WIDGET_STATUS);
        if (status != null && status.getValue().equals("PUBLISHED")){
			widget.setWidgetStatus(WidgetStatus.PUBLISHED);
		} else {
	        widget.setWidgetStatus(WidgetStatus.PREVIEW);
		}
        
        widget.setOwnerId(user.getId());

        final Widget storedWidget = widgetService.registerNewWidget(widget);
        return "redirect:/app/store/widget/" + storedWidget.getId() + "?referringPageId=" + referringPageId;
    }

    /**
     * Add common model attributes to the model
     *
     * @param model
     *            Model to add to
     * @param referringPageId
     *            Page to refer back to
     * @param user
     *            Current authenticated User
     */
    private void widgetStoreModelHelper(Model model, String referringPageId, User user, String view) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.WIDGETS_STATISTICS, widgetService.getAllWidgetStatistics(user.getId()));
        model.addAttribute(ModelKeys.TAGS, tagService.getAllTagsList());
        model.addAttribute(ModelKeys.CATEGORIES, categoryService.getAllList());
        ControllerUtils.addNavItemsToModel(view, model, referringPageId, user);
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
