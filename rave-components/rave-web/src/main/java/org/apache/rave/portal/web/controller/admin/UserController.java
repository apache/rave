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

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UserProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Admin controller to manipulate User data
 */
@Controller
@SessionAttributes({"user"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileValidator userProfileValidator;

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String viewUsers(@RequestParam(required = false, defaultValue = "0") int offset, Model model) {
        AdminControllerUtil.addNavigationMenusToModel("users", model);
        final SearchResult<User> users = userService.getLimitedListOfUsers(offset, AdminControllerUtil.DEFAULT_PAGE_SIZE);
        model.addAttribute(ModelKeys.SEARCHRESULT, users);
        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/users/search", method = RequestMethod.GET)
    public String searchUsers(@RequestParam(required = true) String searchTerm,
                              @RequestParam(required = false, defaultValue = "0") int offset, Model model) {
        AdminControllerUtil.addNavigationMenusToModel("users", model);
        final SearchResult<User> users = userService.getUsersByFreeTextSearch(
                searchTerm, offset, AdminControllerUtil.DEFAULT_PAGE_SIZE);
        model.addAttribute(ModelKeys.SEARCH_TERM, searchTerm);
        model.addAttribute(ModelKeys.SEARCHRESULT, users);
        return ViewNames.ADMIN_USERS;
    }

    @RequestMapping(value = "/admin/userdetail/{userid}", method = RequestMethod.GET)
    public String viewUserDetail(@PathVariable("userid") Long userid, Model model) {
        AdminControllerUtil.addNavigationMenusToModel("users", model);
        final User user = userService.getUserById(userid);
        model.addAttribute(user);
        return ViewNames.ADMIN_USERDETAIL;
    }

    @RequestMapping(value = "/admin/userdetail/update", method = RequestMethod.POST)
    public String updateUserDetail(@ModelAttribute("user") User user, BindingResult result) {
        userProfileValidator.validate(user, result);
        if (result.hasErrors()) {
            return ViewNames.ADMIN_USERDETAIL;
        }
        userService.updateUserProfile(user);
        return "redirect:" + user.getEntityId();
    }

    void setUserService(UserService userService) {
        this.userService = userService;
    }

    void setUserProfileValidator(UserProfileValidator userProfileValidator) {
        this.userProfileValidator = userProfileValidator;
    }

}
