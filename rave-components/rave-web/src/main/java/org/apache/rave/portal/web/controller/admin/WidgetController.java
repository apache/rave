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

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import static org.apache.rave.portal.model.WidgetStatus.values;

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
    private NewWidgetValidator widgetValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("entityId");
    }

    @RequestMapping(value = "/admin/widgets", method = RequestMethod.GET)
    public String viewWidgets(@RequestParam(required = false, defaultValue = "0") int offset, Model model) {
        AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, model);
        final SearchResult<Widget> widgets =
                widgetService.getLimitedListOfWidgets(offset, AdminControllerUtil.DEFAULT_PAGE_SIZE);
        model.addAttribute(ModelKeys.SEARCHRESULT, widgets);
        return ViewNames.ADMIN_WIDGETS;
    }

    @RequestMapping(value = "/admin/widgetdetail/{widgetid}", method = RequestMethod.GET)
    public String viewWidgetDetail(@PathVariable("widgetid") Long widgetid, Model model) {
        AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, model);
        model.addAttribute(widgetService.getWidget(widgetid));
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        return ViewNames.ADMIN_WIDGETDETAIL;
    }

    @RequestMapping(value = "/admin/widgetdetail/update", method = RequestMethod.POST)
    public String updateWidgetDetail(@ModelAttribute(ModelKeys.WIDGET) Widget widget, BindingResult result,
                                     @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                     @RequestParam() String token,
                                     SessionStatus status) {
        AdminControllerUtil.checkTokens(sessionToken, token, status);
        widgetValidator.validate(widget, result);
        if (result.hasErrors()) {
            return ViewNames.ADMIN_WIDGETDETAIL;
        }
        widgetService.updateWidget(widget);
        status.setComplete();
        return "redirect:" + widget.getEntityId();
    }

    @ModelAttribute("widgetStatus")
    public WidgetStatus[] getWidgetStatusValues() {
        return values();
    }

    // setters for unit tests
    void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    void setWidgetValidator(NewWidgetValidator widgetValidator) {
        this.widgetValidator = widgetValidator;
    }

}
