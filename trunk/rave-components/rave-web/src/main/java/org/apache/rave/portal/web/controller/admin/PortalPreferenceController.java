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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.model.PortalPreferenceForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.PortalPreferenceFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;
import java.util.Set;

import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.addNavigationMenusToModel;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.checkTokens;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.isDeleteOrUpdate;

/**
 * Controller for portal preferences
 */
@Controller
@SessionAttributes({"preferenceForm", ModelKeys.TOKENCHECK})
public class PortalPreferenceController {
    private static final String SELECTED_ITEM = "preferences";

    @Autowired
    private PortalPreferenceService preferenceService;

    @Autowired
    private PortalPreferenceFormValidator formValidator;

    @RequestMapping(value = {"/admin/preferences", "/admin/preferences/"}, method = RequestMethod.GET)
    public String viewPreferences(@RequestParam(required = false) final String action,
                                  @RequestParam(required = false) String referringPageId,Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);

        final Map<String, PortalPreference> preferenceMap = preferenceService.getPreferencesAsMap();

        model.addAttribute("preferenceMap", preferenceMap);

        if (isDeleteOrUpdate(action)) {
            model.addAttribute("actionresult", action);
        }

        return ViewNames.ADMIN_PREFERENCES;
    }

    @RequestMapping(value = "/admin/preferencedetail/edit", method = RequestMethod.GET)
    public String editPreferences(Model model, @RequestParam(required = false) String referringPageId) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        final Map<String, PortalPreference> preferenceMap = preferenceService.getPreferencesAsMap();

        PortalPreferenceForm form = new PortalPreferenceForm(preferenceMap);
        model.addAttribute("preferenceForm", form);
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());

        return ViewNames.ADMIN_PREFERENCE_DETAIL;
    }

    @RequestMapping(value = "/admin/preferencedetail/update", method = RequestMethod.POST)
    public String updatePreferences(@ModelAttribute("preferenceForm") PortalPreferenceForm form, BindingResult result,
                                    @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                    @RequestParam String token,
                                    @RequestParam(required = false) String referringPageId,
                                    ModelMap modelMap,
                                    SessionStatus status) {
        checkTokens(sessionToken, token, status);

        formValidator.validate(form, result);
        if (result.hasErrors()) {
            modelMap.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap, referringPageId);
            return ViewNames.ADMIN_PREFERENCE_DETAIL;
        }

        final Set<Map.Entry<String, PortalPreference>> entries = form.getPreferenceMap().entrySet();

        for (Map.Entry<String, PortalPreference> entry : entries) {
            preferenceService.savePreference(entry.getValue());
        }

        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/preferences?action=update&referringPageId=" + referringPageId;
    }

    void setFormValidator(PortalPreferenceFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    public void setPreferenceService(PortalPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }
}
