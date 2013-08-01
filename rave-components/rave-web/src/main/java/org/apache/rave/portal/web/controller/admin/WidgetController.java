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

package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.model.Category;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.controller.util.CategoryEditor;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UpdateWidgetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import static org.apache.rave.model.WidgetStatus.values;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.*;

/**
 * Admin controller to manipulate Widget data
 */
@Controller
@SessionAttributes({ModelKeys.WIDGET, ModelKeys.TOKENCHECK})
public class WidgetController {

    private static final String SELECTED_ITEM = "widgets";

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private UpdateWidgetValidator widgetValidator;

    @Autowired
    private PortalPreferenceService preferenceService;
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryEditor categoryEditor;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("entityId");
        dataBinder.registerCustomEditor(Category.class, categoryEditor);
    }

    @RequestMapping(value = "/admin/widgets", method = RequestMethod.GET)
    public String viewWidgets(@RequestParam(required = false, defaultValue = "0") int offset,
                              @RequestParam(required = false) final String action,
                              @RequestParam(required = false) String referringPageId,
                              Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        final SearchResult<Widget> widgets =
                widgetService.getLimitedList(offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCHRESULT, widgets);

        if (isDeleteOrUpdate(action)) {
            model.addAttribute("actionresult", action);
        }

        return ViewNames.ADMIN_WIDGETS;
    }

    @RequestMapping(value = "/admin/widgets/search", method = RequestMethod.GET)
    public String searchWidgets(@RequestParam(required = false) String searchTerm,
                                @RequestParam(required = false) String widgettype,
                                @RequestParam(required = false) String widgetstatus,
                                @RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false) String referringPageId, Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        final SearchResult<Widget> widgets = widgetService.getWidgetsBySearchCriteria(searchTerm, widgettype,
                widgetstatus, offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCHRESULT, widgets);
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute("selectedWidgetType", widgettype);
        model.addAttribute("selectedWidgetStatus", widgetstatus);
        return ViewNames.ADMIN_WIDGETS;
    }

    @RequestMapping(value = "/admin/widgetdetail/{widgetid}", method = RequestMethod.GET)
    public String viewWidgetDetail(@PathVariable("widgetid") String widgetid,
                                   @RequestParam(required = false) String referringPageId,Model model) {
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.WIDGET, widgetService.getWidget(widgetid));
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        model.addAttribute(ModelKeys.CATEGORIES, categoryService.getAllList());

        return ViewNames.ADMIN_WIDGETDETAIL;
    }

    @RequestMapping(value = "/admin/widgetdetail/update", method = RequestMethod.POST)
    public String updateWidgetDetail(@ModelAttribute(ModelKeys.WIDGET) Widget widget, BindingResult result,
                                     @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                     @RequestParam String token,
                                     @RequestParam(required = false) String referringPageId,
                                     ModelMap modelMap,
                                     SessionStatus status) {
        checkTokens(sessionToken, token, status);
        widgetValidator.validate(widget, result);
        if (result.hasErrors()) {
            addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap, referringPageId);
            modelMap.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            modelMap.addAttribute(ModelKeys.CATEGORIES, categoryService.getAllList());
            return ViewNames.ADMIN_WIDGETDETAIL;
        }
        widgetService.updateWidget(widget);
        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/widgets?action=update&referringPageId=" + referringPageId;
    }

    @ModelAttribute("widgetStatus")
    public WidgetStatus[] getWidgetStatusValues() {
        return values();
    }

    // setters for unit tests
    void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    void setWidgetValidator(UpdateWidgetValidator widgetValidator) {
        this.widgetValidator = widgetValidator;
    }
    // setters for unit tests
    public void setPreferenceService(PortalPreferenceService preferenceService) {
            this.preferenceService = preferenceService;
    }

    // setters for unit tests
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // setters for unit tests
    public void setCategoryEditor(CategoryEditor categoryEditor) {
        this.categoryEditor = categoryEditor;
    }

    public int getPageSize() {
        final PortalPreference pageSizePref = preferenceService.getPreference(PortalPreferenceKeys.PAGE_SIZE);
        if (pageSizePref == null) {
            return DEFAULT_PAGE_SIZE;
        }
        try {
            return Integer.parseInt(pageSizePref.getValue());
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }
}
