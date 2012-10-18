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

import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.model.MongoDbCategory;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbCategoryRepository implements CategoryRepository {

    public static final String COLLECTION = "category";
    public static final Class<MongoDbCategory> CLASS = MongoDbCategory.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public List<Category> getAll() {
        return CollectionUtils.<Category>toBaseTypedList(template.findAll(CLASS, COLLECTION));
    }

    @Override
    public int removeFromCreatedOrModifiedFields(long userId) {
        List<MongoDbCategory> categories = template.find(query(where("lastModifiedUserId").is(userId).orOperator(where("createdUserId").is(userId))), CLASS, COLLECTION);
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
    public Category get(long id) {
        return hydrate(template.findById(id, CLASS, COLLECTION));
    }

    @Override
    public Category save(Category item) {
        MongoDbCategory converted = converter.convert(item, Category.class);
        template.save(converted, COLLECTION);
        return hydrate(converted);
    }

    @Override
    public void delete(Category item) {
        template.remove(get(item.getId()));
    }

    private Category hydrate(MongoDbCategory category) {
        converter.hydrate(category, Category.class);
        return category;
    }

    private boolean updateCategory(long userId, MongoDbCategory category) {
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
}
