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

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"/store/*", "/store"})
public class WidgetStoreController {

    // TODO make this value configurable through some management interface
    private static final int MAXIMUM_WIDGETS_PER_PAGE = 10;

    private final WidgetService widgetService;

    private final NewWidgetValidator widgetValidator;

    @Autowired
    public WidgetStoreController(WidgetService widgetService, NewWidgetValidator validator) {
        this.widgetService = widgetService;
        this.widgetValidator = validator;
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
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getPublishedWidgets(offset, MAXIMUM_WIDGETS_PER_PAGE));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
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
        return ViewNames.WIDGET;
    }

    /**
     * Performs a search in the widget store
     *
     * @param model           {@link Model} map
     * @param referringPageId the source {@link org.apache.rave.portal.model.Page } ID
     * @param searchTerm          free text searchTerm query
     * @param offset          offset within the total amount of results (to enable paging)
     * @return the view name of the main store page
     */
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public String viewSearchResult(Model model, @RequestParam long referringPageId,
                                   @RequestParam String searchTerm,
                                   @RequestParam(required = false, defaultValue = "0") int offset) {
        model.addAttribute(ModelKeys.WIDGETS,
                widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm, offset,
                        MAXIMUM_WIDGETS_PER_PAGE));
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.OFFSET, offset);
        return ViewNames.STORE;
    }

    /**
     * Shows the Add new Widget form
     *
     * @param model {@link Model}
     * @return the view name of the Add new Widget form
     */
    //TODO:  Change the value of this request mapping so that it follows the pattern /store/widget/add
    @RequestMapping(method = RequestMethod.GET, value = "addwidget")
    public String viewAddWidgetForm(Model model) {
        final Widget widget = new Widget();
        model.addAttribute(ModelKeys.WIDGET, widget);
        return ViewNames.ADD_WIDGET_FORM;
    }

    /**
     * Validates the form input, if valid, tries to store the Widget data
     *
     * @param widget  {@link Widget} as submitted by the user
     * @param results {@link BindingResult}
     * @param model   {@link Model}
     * @return if successful the view name of the widget, otherwise the form
     */
    /*TODO:  Change the value of this request mapping so that it follows the pattern /store/widget/add
      TODO:  The value can be the same as the GET action as you are mapping based on method & name
     */
    @RequestMapping(method = RequestMethod.POST, value = "doaddwidget")
    public String viewAddWidgetResult(@ModelAttribute Widget widget, BindingResult results,
                                      Model model) {
        widgetValidator.validate(widget, results);
        if (results.hasErrors()) {
            model.addAttribute(ModelKeys.WIDGET, widget);
            return ViewNames.ADD_WIDGET_FORM;
        }
        widget.setWidgetStatus(WidgetStatus.PREVIEW);

        final Widget storedWidget = widgetService.registerNewWidget(widget);
        if (storedWidget == null) {
            results.reject("page.addwidget.result.exists");
            model.addAttribute(ModelKeys.WIDGET, widget);
            return ViewNames.ADD_WIDGET_FORM;
        }

        model.addAttribute(ModelKeys.WIDGET, storedWidget);
        return ViewNames.WIDGET;
    }
}
