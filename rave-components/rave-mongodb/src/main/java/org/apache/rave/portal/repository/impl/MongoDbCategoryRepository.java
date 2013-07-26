/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.Category;
import org.apache.rave.portal.model.MongoDbCategory;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.CATEGORY_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbCategoryRepository implements CategoryRepository {
    public static final Class<MongoDbCategory> CLASS = MongoDbCategory.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public List<Category> getAll() {
        return CollectionUtils.<Category>toBaseTypedList(hydrate(template.findAll(CLASS, CATEGORY_COLLECTION)));
    }

    @Override
    public List<Category> getLimitedList(int offset, int limit) {
        return CollectionUtils.<Category>toBaseTypedList(hydrate(template.find(new Query().skip(offset).limit(limit), CLASS)));
    }

    @Override
    public int getCountAll() {
        return (int) template.count(new Query(), CLASS);
    }

    @Override
    public int removeFromCreatedOrModifiedFields(String userId) {
        List<MongoDbCategory> categories = template.find(query(where("lastModifiedUserId").is(userId).orOperator(where("createdUserId").is(userId))), CLASS, CATEGORY_COLLECTION);
        int count = 0;
        for(MongoDbCategory category : categories) {
            boolean updated = updateCategory(userId, category);
            if(updated) {
                save(category);
                count++;
            }
        }
        return count;
    }

    @Override
    public Class<? extends Category> getType() {
        return CLASS;
    }

    @Override
    public Category get(String id) {
        return hydrate(template.findById(id, CLASS, CATEGORY_COLLECTION));
    }

    @Override
    public Category save(Category item) {
        MongoDbCategory converted = converter.convert(item, Category.class);
        template.save(converted, CATEGORY_COLLECTION);
        return hydrate(converted);
    }

    @Override
    public void delete(Category item) {
        template.remove(get(item.getId()), CATEGORY_COLLECTION);
    }

    private Category hydrate(MongoDbCategory category) {
        converter.hydrate(category, Category.class);
        return category;
    }

    private List<MongoDbCategory> hydrate(List<MongoDbCategory> all) {
        for(MongoDbCategory category : all) {
            hydrate(category);
        }
        return all;
    }

    private boolean updateCategory(String userId, MongoDbCategory category) {
        boolean updated = false;
        if(category.getCreatedUserId().equals(userId)) {
            category.setCreatedUserId(null);
            updated = true;
        }
        if(category.getLastModifiedUserId().equals(userId)) {
            category.setLastModifiedUserId(null);
            updated = true;
        }
        return updated;
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }

    public void setConverter(HydratingConverterFactory converter) {
        this.converter = converter;
    }
}
