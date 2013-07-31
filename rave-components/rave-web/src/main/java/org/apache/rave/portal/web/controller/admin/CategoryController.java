/*
 * Copyright 2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

import static org.apache.rave.portal.web.controller.admin.AdminControllerUtil.*;

@Controller
@SessionAttributes({ModelKeys.CATEGORY, ModelKeys.TOKENCHECK})
public class CategoryController {

    private static final String SELECTED_ITEM = "categories";

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = {"/admin/categories", "/admin/categories/"}, method = RequestMethod.GET)
    public String getCategories(@RequestParam(required = false) final String action,
                                @RequestParam(required = false) String referringPageId,Model model){
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);

        List<Category> categories = categoryService.getAllList();

        model.addAttribute("categories", categories);
        // put category object in the model to allow creating categories from view
        model.addAttribute(ModelKeys.CATEGORY, new CategoryImpl());
        // add tokencheck attribute for creating new category
        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);

        if (isCreateDeleteOrUpdate(action)) {
            model.addAttribute("actionresult", action);
        }

        return ViewNames.ADMIN_CATEGORIES;
    }

    @RequestMapping(value = {"/admin/category/create"}, method = RequestMethod.POST)
    public String createCategory(@ModelAttribute CategoryImpl category,
                                 @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                 @RequestParam String token,
                                 @RequestParam(required = false) String referringPageId,
                                 Model model,
                                 SessionStatus status) {
        checkTokens(sessionToken, token, status);
        User creator = userService.getAuthenticatedUser();
        boolean isValidRequest = validateRequest(category.getText(), creator);
        if (isValidRequest) {
            categoryService.create(category.getText(), creator);
        } else {
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            return ViewNames.ADMIN_CATEGORIES;
        }
        status.setComplete();
        return "redirect:/app/admin/categories?action=create&referringPageId=" + referringPageId;
    }

    @RequestMapping(value = {"/admin/category/update"}, method = RequestMethod.POST)
    public String updateCategory(@ModelAttribute(ModelKeys.CATEGORY) Category category,
                                 @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                 @RequestParam String token,
                                 @RequestParam(required = false) String referringPageId,
                                 Model model,
                                 SessionStatus status){
        checkTokens(sessionToken, token, status);
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        User currentUser = userService.getAuthenticatedUser();
        boolean isValidRequest = validateRequest(category, currentUser);
        if (isValidRequest) {
            categoryService.update(category.getId(), category.getText(), currentUser);
        } else {
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            return ViewNames.ADMIN_CATEGORY_DETAIL;
        }
        status.setComplete();
        model.asMap().clear();
        return "redirect:/app/admin/categories?action=update&referringPageId=" + referringPageId;
    }

    @RequestMapping(value = {"/admin/category/delete"}, method = RequestMethod.POST)
    public String deleteCategory(@ModelAttribute(ModelKeys.CATEGORY) Category category,
                                 @ModelAttribute(ModelKeys.TOKENCHECK) String sessionToken,
                                 @RequestParam String token,
                                 @RequestParam(required = false) String confirmdelete,
                                 @RequestParam(required = false) String referringPageId,
                                 Model model,
                                 SessionStatus status){
        checkTokens(sessionToken, token, status);
        User creator = userService.getAuthenticatedUser();
        if (!Boolean.parseBoolean(confirmdelete)) {
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            AdminControllerUtil.addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            model.addAttribute("missingConfirm", true);
            return ViewNames.ADMIN_CATEGORY_DETAIL;
        }
        boolean isValidRequest = validateRequest(category, creator);
        if (isValidRequest) {
            categoryService.delete(category);
        } else {
            model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
            addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);
            return ViewNames.ADMIN_CATEGORY_DETAIL;
        }
        status.setComplete();
        return "redirect:/app/admin/categories?action=delete&referringPageId=" + referringPageId;
    }

    @RequestMapping(value = "/admin/category/edit", method = RequestMethod.GET)
    public String editCategory(@RequestParam(required = true) String id,
                               @RequestParam(required = false) String referringPageId,Model model) {
        model.addAttribute(ModelKeys.REFERRING_PAGE_ID, referringPageId);
        addNavigationMenusToModel(SELECTED_ITEM, model, referringPageId);

        model.addAttribute(ModelKeys.TOKENCHECK, AdminControllerUtil.generateSessionToken());
        model.addAttribute(ModelKeys.CATEGORY, categoryService.get(id));

        return ViewNames.ADMIN_CATEGORY_DETAIL;
    }

    private boolean validateRequest(String text, User creator) {
        return (text != null && creator != null && !text.equals(""));
    }

    private boolean validateRequest(Category category, User modifier){
        return (validateRequest(category.getText(),modifier) && (categoryService.get(category.getId()) != null));
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
