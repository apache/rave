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
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbCategoryRepository implements CategoryRepository {

    public static final String COLLECTION = "category";
    public static final Class<CategoryImpl> CLASS = CategoryImpl.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public List<Category> getAll() {
        return null;
    }

    @Override
    public int removeFromCreatedOrModifiedFields(long userId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Category> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Category get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Category save(Category item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Category item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
