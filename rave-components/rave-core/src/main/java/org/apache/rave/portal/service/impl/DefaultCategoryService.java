/*
 * Copyright 2011 The Apache Software Foundation.
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
package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public DefaultCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category get(long entityId) {
        return categoryRepository.get(entityId);
    }



    @Override
    public List<Category> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    @Transactional
    public Category create(String text, User createdUser) {
        Category category = new Category();
        Date now = new Date();
        category.setText(text);
        category.setCreatedDate(now);
        category.setCreatedUser(createdUser);
        category.setLastModifiedDate(now);
        category.setLastModifiedUser(createdUser);
        categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public Category update(long categoryId, String text, User lastModifiedUser) {
        Category category = categoryRepository.get(categoryId);
        category.setText(text);
        category.setLastModifiedDate(new Date());
        category.setLastModifiedUser(lastModifiedUser);
        categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public void delete(Category category) {
        Category categoryToBeDeleted = categoryRepository.get(category.getEntityId());
        categoryRepository.delete(categoryToBeDeleted);
    }
}
