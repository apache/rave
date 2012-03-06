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

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.AuthorityService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;

import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.DEFAULT_PAGE_SIZE;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.addNavigationMenusToModel;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.checkTokens;
import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.isDeleteOrUpdate;

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
                            Model model) {
        addNavigationMenusToModel(SELECTED_ITEM, model);
        final SearchResult<User> users = userService.getLimitedListOfUsers(offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCHRESULT, users);

        if (isDeleteOrUpdate(action)) {
            model.addAttribute("actionresult", action);
        }

        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/users/search", method = RequestMethod.GET)
    public String searchUsers(@RequestParam(required = true) String searchTerm,
                              @RequestParam(required = false, defaultValue = "0") int offset, Model model) {
        addNavigationMenusToModel(SELECTED_ITEM, model);
        final SearchResult<User> users = userService.getUsersByFreeTextSearch(
                searchTerm, offset, getPageSize());
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.SEARCHRESULT, users);
        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/userdetail/{userid}", method = RequestMethod.GET)
    public String viewUserDetail(@PathVariable("userid") Long userid, Model model) {
        addNavigationMenusToModel(SELECTED_ITEM, model);
        model.addAttribute(userService.getUserById(userid));
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        return ViewNames.ADMIN_USERDETAIL;
    }

    @RequestMapping(value = "/admin/userdetail/update", method = RequestMethod.POST)
    public String updateUserDetail(@ModelAttribute("user") User user, BindingResult result,
                                   @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                   @RequestParam() String token,
                                   ModelMap modelMap,
                                   SessionStatus status) {
        checkTokens(sessionToken, token, status);
        userProfileValidator.validate(user, result);
        if (result.hasErrors()) {
            AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap);
            return ViewNames.ADMIN_USERDETAIL;
        }
        userService.updateUserProfile(user);
        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/users?action=update";
    }

    @RequestMapping(value = "/admin/userdetail/delete", method = RequestMethod.POST)
    public String deleteUserDetail(@ModelAttribute("user") User user,
                                   @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                   @RequestParam String token,
                                   @RequestParam(required = false) String confirmdelete,
                                   ModelMap modelMap,
                                   SessionStatus status) {
        checkTokens(sessionToken, token, status);
        if (!Boolean.parseBoolean(confirmdelete)) {
            AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) modelMap);
            modelMap.addAttribute("missingConfirm", true);
            return ViewNames.ADMIN_USERDETAIL;
        }
        userService.deleteUser(user.getEntityId());
        modelMap.clear();
        status.setComplete();
        return "redirect:/app/admin/users?action=delete";
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/admin/adduser"})
    public String setUpForm(ModelMap model) {
        logger.debug("Initializing new account form");
        AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, (Model) model);
        model.addAttribute(ModelKeys.NEW_USER, new NewUser());
        return ViewNames.ADMIN_NEW_ACCOUNT;

    }

    @RequestMapping(value = {"/admin/newaccount", "/admin/newaccount/*"}, method = RequestMethod.POST)
    public String create(@ModelAttribute NewUser newUser, BindingResult results, Model model,
                         RedirectAttributes redirectAttributes) {
        logger.debug("Creating a new user account");
        model.addAttribute(ModelKeys.NEW_USER, newUser);
        newAccountValidator.validate(newUser, results);
        if (results.hasErrors()) {
            logger.info("newaccount.jsp: shows validation errors");
            addNavigationMenusToModel(SELECTED_ITEM, model);
            return ViewNames.ADMIN_NEW_ACCOUNT;
        }
        try {
            logger.debug("newaccount.jsp: passed form validation");
            newAccountService.createNewAccount(newUser);
            redirectAttributes.addFlashAttribute(ModelKeys.REDIRECT_MESSAGE, messageSuccess);
            return "redirect:/app/admin/users";
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException ex) {
            addNavigationMenusToModel(SELECTED_ITEM, model);
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
            addNavigationMenusToModel(SELECTED_ITEM, model);
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
     * Mapping between the submitted form value and an {@link Authority}
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
