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

import org.apache.rave.model.Authority;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.controller.util.ModelUtils;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewAccountValidator;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;

import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.*;

/**
 * Admin controller to manipulate User data
 */
@Controller
@SessionAttributes({"user", ModelKeys.TOKENCHECK})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String SELECTED_ITEM = "users";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserProfileValidator userProfileValidator;

    @Autowired
    private PortalPreferenceService preferenceService;

    @Autowired
    private NewAccountValidator newAccountValidator;

    @Autowired
    private NewAccountService newAccountService;

    @Value("#{messages['page.newaccount.message.created']}")
    private String messageSuccess;


    @InitBinder(value = {"user"})
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(GrantedAuthority.class, new AuthorityEditor());
        dataBinder.registerCustomEditor(Authority.class, new AuthorityEditor());
        dataBinder.setDisallowedFields("entityId", "username", "password", "confirmPassword");
    }


    @RequestMapping(value = {"/admin/users"}, method = RequestMethod.GET)
    public String viewUsers(@RequestParam(required = false, defaultValue = "0") int offset,
                            @RequestParam(required = false) final String action,
                            @RequestParam(required = false) String referringPageId,
                            Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        final SearchResult<User> users = userService.getLimitedList(offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCHRESULT, users);

        if (isDeleteOrUpdate(action)) {
            model.addAttribute("actionresult", action);
        }

        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/users/search", method = RequestMethod.GET)
    public String searchUsers(@RequestParam(required = true) String searchTerm,
                              @RequestParam(required = false, defaultValue = "0") int offset,
                              @RequestParam(required = false) String referringPageId,Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
        final SearchResult<User> users = userService.getUsersByFreeTextSearch(
                searchTerm, offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.SEARCHRESULT, users);
        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/userdetail/{userid}", method = RequestMethod.GET)
    public String viewUserDetail(@PathVariable("userid") String userid,
                                 @RequestParam(required = false) String referringPageId,
                                 Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model,referringPageId );
        model.addAttribute(ModelKeys.USER, userService.getUserById(userid));
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        return ViewNames.ADMIN_USERDETAIL;
    }

    @RequestMapping(value = "/admin/userdetail/update", method = RequestMethod.POST)
    public String updateUserDetail(@ModelAttribute User user, BindingResult result,
                                   @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                   @RequestParam() String token,
                                   @RequestParam(required = false) String referringPageId,
                                   ModelMap modelMap,
                                   SessionStatus status) {
        checkTokens(sessionToken, token, status);
        user.setConfirmPassword(user.getPassword());
        userProfileValidator.validate(user, result);
        if (result.hasErrors()) {
            modelMap.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap, referringPageId);
            return ViewNames.ADMIN_USERDETAIL;
        }
        userService.updateUserProfile(user);
        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/users?action=update&referringPageId=" + referringPageId;
    }

    @RequestMapping(value = "/admin/userdetail/delete", method = RequestMethod.POST)
    public String deleteUserDetail(@ModelAttribute User user,
                                   @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                   @RequestParam String token,
                                   @RequestParam(required = false) String confirmdelete,
                                   @RequestParam(required = false) String referringPageId,
                                   ModelMap modelMap,
                                   SessionStatus status) {
        checkTokens(sessionToken, token, status);
        if (!Boolean.parseBoolean(confirmdelete)) {
            modelMap.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap, referringPageId);
            modelMap.addAttribute("missingConfirm", true);
            return ViewNames.ADMIN_USERDETAIL;
        }
        userService.deleteUser(user.getId());
        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/users?action=delete&referringPageId=" + referringPageId;
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/admin/adduser"})
    public String setUpForm(ModelMap model, @RequestParam(required = false) String referringPageId) {
        logger.debug("Initializing new account form");
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) model, referringPageId);
        model.addAttribute(ModelKeys.NEW_USER, new UserImpl());
        return ViewNames.ADMIN_NEW_ACCOUNT;

    }

    @RequestMapping(value = {"/admin/newaccount", "/admin/newaccount/*"}, method = RequestMethod.POST)
    public String create(@ModelAttribute(value = "newUser") UserForm newUser, BindingResult results, Model model,
                         @RequestParam(required = false) String referringPageId,
                         RedirectAttributes redirectAttributes) {
        logger.debug("Creating a new user account");
        model.addAttribute(ModelKeys.NEW_USER, newUser);
        newAccountValidator.validate(newUser, results);
        if (results.hasErrors()) {
            logger.info("newaccount.jsp: shows validation errors");
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            return ViewNames.ADMIN_NEW_ACCOUNT;
        }
        try {
            logger.debug("newaccount.jsp: passed form validation");
            newAccountService.createNewAccount(ModelUtils.convert(newUser));
            redirectAttributes.addFlashAttribute(ModelKeys.REDIRECT_MESSAGE, messageSuccess);
            return "redirect:/app/admin/users?referringPageId=" +referringPageId;
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            logger.info("Account creation failed: ", ex);
            results.reject("Account already exists", "Unable to create account");
            return ViewNames.ADMIN_NEW_ACCOUNT;

        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.error("Account creation failed: ", ex);
            } else {
                logger.error("Account creation failed: {}", ex.getMessage());
            }
            results.reject("Unable to create account:" + ex.getMessage(), "Unable to create account");
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            return ViewNames.ADMIN_NEW_ACCOUNT;
        }

    }


    @ModelAttribute("authorities")
    public SearchResult<Authority> populateAuthorityList() {
        return authorityService.getAllAuthorities();
    }

    @ModelAttribute("loggedInUser")
    public String populateLoggedInUsername() {
        return userService.getAuthenticatedUser().getUsername();
    }

    // setters for unit tests
    void setUserService(UserService userService) {
        this.userService = userService;
    }

    void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    void setUserProfileValidator(UserProfileValidator userProfileValidator) {
        this.userProfileValidator = userProfileValidator;
    }

    void setPreferenceService(PortalPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    void setNewAccountValidator(NewAccountValidator newAccountValidator) {
        this.newAccountValidator = newAccountValidator;
    }

    void setNewAccountService(NewAccountService newAccountService) {
        this.newAccountService = newAccountService;
    }


    /**
     * Mapping between the submitted form value and an {@link org.apache.rave.model.Authority}
     */
    private class AuthorityEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Authority authority = authorityService.getAuthorityByName(text);
            setValue(authority);
        }

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
