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

import org.apache.rave.model.Category;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.model.User;
import org.apache.rave.rest.model.SearchResult;
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
    public Category get(String id) {
        return categoryRepository.get(id);
    }



    @Override
    public List<Category> getAllList() {
        return categoryRepository.getAll();
    }

    @Override
    public SearchResult<Category> getAll() {
        final int count = categoryRepository.getCountAll();
        final List<Category> categories = categoryRepository.getAll();
        return new SearchResult<Category>(categories, count);
    }

    @Override
    public SearchResult<Category> getLimitedList(int offset, int pageSize) {
        final int count = categoryRepository.getCountAll();
        final List<Category> categories = categoryRepository.getLimitedList(offset, pageSize);
        final SearchResult<Category> searchResult = new SearchResult<Category>(categories, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    @Transactional
    public Category create(String text, User createdUser) {
        Category category = new CategoryImpl();
        Date now = new Date();
        category.setText(text);
        category.setCreatedDate(now);
        category.setCreatedUserId(createdUser.getId());
        category.setLastModifiedDate(now);
        category.setLastModifiedUserId(createdUser.getId());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(String categoryId, String text, User lastModifiedUser) {
        Category category = categoryRepository.get(categoryId);
        category.setText(text);
        category.setLastModifiedDate(new Date());
        category.setLastModifiedUserId(lastModifiedUser.getId());
        categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public void delete(Category category) {
        Category categoryToBeDeleted = categoryRepository.get(category.getId());
        categoryRepository.delete(categoryToBeDeleted);
    }
}
