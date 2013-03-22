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

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.Category;
import org.apache.rave.portal.model.MongoDbCategory;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbCategoryConverter implements HydratingModelConverter<Category, MongoDbCategory> {

    @Autowired
    private MongoWidgetOperations widgetOperations;


    @Override
    public void hydrate(MongoDbCategory dehydrated) {
        if(dehydrated == null) {
            return;
        }
        dehydrated.setWidgetRepository(widgetOperations);
    }

    @Override
    public Class<Category> getSourceType() {
        return Category.class;
    }

    @Override
    public MongoDbCategory convert(Category source) {
        MongoDbCategory category = new MongoDbCategory();
        category.setId(source.getId() == null ? generateId() : source.getId());
        category.setCreatedDate(source.getCreatedDate());
        category.setCreatedUserId(source.getCreatedUserId());
        category.setLastModifiedUserId(source.getLastModifiedUserId());
        category.setWidgetRepository(null);
        category.setText(source.getText());
        category.setWidgets(null);
        return category;
    }

    public void setMongoWidgetOperations(MongoWidgetOperations mongoWidgetOperations) {
        this.widgetOperations = mongoWidgetOperations;
    }
}
