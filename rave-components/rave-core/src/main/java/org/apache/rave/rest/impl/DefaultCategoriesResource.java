/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.rest.impl;

import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.model.User;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.rest.CategoriesResource;
import org.apache.rave.rest.model.Category;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class DefaultCategoriesResource implements CategoriesResource {
    private CategoryService categoryService;
    private UserService userService;

    @Override
    public SearchResult<Category> getCategories() {

        SearchResult<org.apache.rave.model.Category> fromDb = categoryService.getAll();
        List<Category> categories = new ArrayList<Category>();

        for (org.apache.rave.model.Category category : fromDb.getResultSet()) {
            categories.add(new Category((category)));
        }

        return new SearchResult<Category>(categories, fromDb.getTotalResults());
    }

    @Override
    public Category getCategory(String id) {

        org.apache.rave.model.Category fromDb = categoryService.get(id);
        if(fromDb == null) {
            throw new ResourceNotFoundException(id);
        }

        return new Category(fromDb);
    }

    @Override
    public Category updateCategory(String id, Category category, UriInfo uri) {

        User user = userService.getAuthenticatedUser();
        org.apache.rave.model.Category updatedCategory = categoryService.update(id, category.getText(), user);

        return new Category(updatedCategory);
    }

    @Override
    public Category createCategory(Category category) {

        User user = userService.getAuthenticatedUser();
        org.apache.rave.model.Category newCategory = categoryService.create(category.getText(), user);

        return new Category(newCategory);
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
