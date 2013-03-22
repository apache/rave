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
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetMarketplaceService;
import org.apache.rave.portal.web.controller.util.ControllerUtils;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = { "/marketplace/*", "/marketplace" })
public class WidgetMarketplaceController {

	private static final int MAXIMUM_WIDGETS_PER_PAGE = 10;

	private static final Logger logger = LoggerFactory.getLogger(WidgetMarketplaceController.class);

	private final WidgetMarketplaceService marketplaceService;
	private final UserService userService;
	private final PortalPreferenceService preferenceService;

	@Autowired
	public WidgetMarketplaceController(
			WidgetMarketplaceService marketplaceService,
			PortalPreferenceService preferenceService, UserService userService) {
		this.marketplaceService = marketplaceService;
		this.preferenceService = preferenceService;
		this.userService = userService;
	}

	/**
	 * Views the marketplace tab of the Add New Widget page
	 * @param model
	 * @param referringPageId
	 * @return the view name of the marketplace tab
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String viewSearchResult(Model model,
			@RequestParam String referringPageId,
            @RequestParam(required = false, defaultValue = "0") int offset) {

		final String view = ViewNames.ADD_WIDGET_MARKETPLACE;

		User user = userService.getAuthenticatedUser();
		widgetStoreModelHelper(model, referringPageId, user, view);

		try {
			model.addAttribute(ModelKeys.WIDGETS, marketplaceService
					.getWidgetsByFreeTextSearch("", offset, getPageSize()));
			model.addAttribute(ModelKeys.CATEGORIES, marketplaceService.getCategories());
		} catch (Exception e) {
			model.addAttribute(ModelKeys.ERROR_MESSAGE, "Marketplace not available");
		}
		model.addAttribute(ModelKeys.OFFSET, offset);
		model.addAttribute(ModelKeys.SEARCH_TERM, "");
		return view;
	}

	/**
	 * Views the marketplace tab with search results 
	 * @param model
	 * @param referringPageId
	 * @param searchTerm
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = { "marketplace/search" }, method = RequestMethod.GET)
	public String viewSearchResult(Model model,
			@RequestParam String referringPageId,
			@RequestParam String searchTerm,
			@RequestParam(required = false, defaultValue = "0") int offset) {

		final String view = ViewNames.ADD_WIDGET_MARKETPLACE;

		User user = userService.getAuthenticatedUser();
		widgetStoreModelHelper(model, referringPageId, user, view);

		try {
			model.addAttribute(ModelKeys.WIDGETS, marketplaceService
					.getWidgetsByFreeTextSearch(searchTerm, offset, getPageSize()));
			model.addAttribute(ModelKeys.CATEGORIES, marketplaceService.getCategories());
		} catch (Exception e) {
			model.addAttribute(ModelKeys.ERROR_MESSAGE, "Marketplace not available");
		}
		model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
		model.addAttribute(ModelKeys.OFFSET, offset);
		return view;
	}
	
	/**
	 * Views the marketplace tab with category results 
	 * @param model
	 * @param referringPageId
	 * @param category
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = { "category/{category}" }, method = RequestMethod.GET)
	public String viewCategory(Model model,
			@RequestParam String referringPageId,
			@PathVariable String category,
			@RequestParam(required = false, defaultValue = "0") int offset) {

		final String view = ViewNames.ADD_WIDGET_MARKETPLACE;

		User user = userService.getAuthenticatedUser();
		widgetStoreModelHelper(model, referringPageId, user, view);

        try {
            if(category.equals("0")){
                model.addAttribute(ModelKeys.WIDGETS, marketplaceService
                    .getWidgetsByFreeTextSearch("", offset, getPageSize()));
                model.addAttribute(ModelKeys.CATEGORIES, marketplaceService.getCategories());
            }else{
                model.addAttribute(ModelKeys.WIDGETS, marketplaceService.getWidgetsByCategory(category, offset, getPageSize()));
                model.addAttribute(ModelKeys.CATEGORIES, marketplaceService.getCategories());
                model.addAttribute(ModelKeys.SELECTED_CATEGORY, category);
            }
		} catch (Exception e) {
			model.addAttribute(ModelKeys.ERROR_MESSAGE, "Marketplace not available");
		}
		model.addAttribute(ModelKeys.CATEGORY, category);
		model.addAttribute(ModelKeys.OFFSET, offset);
		return view;
	}
	
	/**
	 * View the marketplace tab showing a single widget detail
	 * @return the view
	 */
	@RequestMapping(value = {"widget/{widget}"}, method=RequestMethod.GET)
	public String viewWidgetDetail(Model model,
			@RequestParam String referringPageId,
			@RequestParam String externalId,
			@PathVariable String widget){
		
		final String view = ViewNames.WIDGET_MARKETPLACE;
		User user = userService.getAuthenticatedUser();
		widgetStoreModelHelper(model, referringPageId, user, view);
		try {
			model.addAttribute(ModelKeys.WIDGET, marketplaceService.getWidget(externalId));
		} catch (Exception e) {
			model.addAttribute(ModelKeys.ERROR_MESSAGE, "Marketplace not available");
		}
		return view;
	}

	private int getPageSize() {
		final PortalPreference pageSizePref = preferenceService
				.getPreference(PortalPreferenceKeys.PAGE_SIZE);
		if (pageSizePref == null) {
			return MAXIMUM_WIDGETS_PER_PAGE;
		}
		try {
			return Integer.parseInt(pageSizePref.getValue());
		} catch (NumberFormatException e) {
			return MAXIMUM_WIDGETS_PER_PAGE;
		}
	}

	/*
	 * Add common model attributes to the model
	 * 
	 * @param model
	 *            Model to add to
	 * @param referringPageId
	 *            Page to refer back to
	 * @param user
	 *            Current authenticated User
	 */
	private void widgetStoreModelHelper(Model model, String referringPageId,
			User user, String view) {
		model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
		ControllerUtils.addNavItemsToModel(view, model, referringPageId, user);
	}

}
