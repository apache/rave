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

import org.apache.rave.model.User;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.rest.CategoriesResource;
import org.apache.rave.rest.model.Category;
import org.apache.rave.rest.model.CategoryList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class DefaultCategoriesResource implements CategoriesResource {
    private CategoryService categoryService;
    private UserService userService;

    @Override
    public Response getCategories() {
        CategoryList categoryList = new CategoryList();
        for (org.apache.rave.model.Category category : categoryService.getAllList()) {
            categoryList.getCategories().add(new Category((category)));
        }

        return Response.ok(categoryList).build();
    }

    @Override
    public Response getCategory(String id) {
        org.apache.rave.model.Category category = categoryService.get(id);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(new Category(category)).build();
        }
    }

    @Override
    public Response updateCategory(String id, Category category,UriInfo uri) {
        User user = userService.getAuthenticatedUser();

        org.apache.rave.model.Category updatedCategory = categoryService.update(id, category.getText(), user);

        return Response.ok(new Category(updatedCategory)).location(uri.getRequestUri()).build();
    }

    @Override
    public Response createCategory(Category category) {
        User user = userService.getAuthenticatedUser();

        org.apache.rave.model.Category newCategory = categoryService.create(category.getText(), user);

        UriBuilder builder = UriBuilder.fromResource(CategoriesResource.class).path("/{id}");
        return Response.created(builder.build(newCategory.getId())).entity(new Category(newCategory)).build();
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
