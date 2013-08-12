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
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.apache.rave.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 7:55 AM
 */
public class MongoDbCategoryRepositoryTest {
    private MongoDbCategoryRepository categoryRepository;
    private MongoOperations template;
    private HydratingConverterFactory converter;

    @Before
    public void setup(){
        categoryRepository = new MongoDbCategoryRepository();
        template = createMock(MongoOperations.class);
        converter = createMock(HydratingConverterFactory.class);
        categoryRepository.setConverter(converter);
        categoryRepository.setTemplate(template);
    }

    @Test
    public void getAll_Valid(){
        List<MongoDbCategory> categoryList = new ArrayList<MongoDbCategory>();
        MongoDbCategory category = new MongoDbCategory();
        categoryList.add(category);
        expect(template.findAll(categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(categoryList);
        converter.hydrate(category, Category.class);
        expectLastCall();
        replay(template, converter);

        assertThat(CollectionUtils.<Category>toBaseTypedList(categoryList), is(sameInstance(categoryRepository.getAll())));
    }

    @Test
    public void removeFromCreatedOrModifiedFields_Valid(){
        List<MongoDbCategory> categoryList = new ArrayList<MongoDbCategory>();
        MongoDbCategory category = new MongoDbCategory();
        categoryList.add(category);
        String userId = "123";
        category.setCreatedUserId(userId);
        category.setLastModifiedUserId(userId);
        MongoDbCategory converted = new MongoDbCategory();

        expect(template.find(query(Criteria.where("lastModifiedUserId").is(userId).orOperator(Criteria.where("createdUserId").is(userId))), categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(categoryList);
        expect(converter.convert(category, Category.class)).andReturn(converted);
        template.save(converted, CollectionNames.CATEGORY_COLLECTION);
        expectLastCall();
        converter.hydrate(category, Category.class);
        expectLastCall();
        replay(template, converter);

        int count = categoryRepository.removeFromCreatedOrModifiedFields(userId);
        assertNull(category.getCreatedUserId());
        assertNull(category.getLastModifiedUserId());
        assertThat(count, is(equalTo(1)));

    }

    @Test
    public void removeFromCreatedOrModifiedFields_NotUpdated(){
        String userId = "54321";
        MongoDbCategory category = new MongoDbCategory();
        List<MongoDbCategory> categories = Arrays.asList(category);
        category.setCreatedUserId("234");
        category.setLastModifiedUserId("234");
        expect(template.find(query(Criteria.where("lastModifiedUserId").is(userId).orOperator(Criteria.where("createdUserId").is(userId))), categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(categories);
        replay(template);

        int count = categoryRepository.removeFromCreatedOrModifiedFields(userId);
        assertThat(count, is(equalTo(0)));
    }

    @Test
    public void getType_Valid(){
        assertThat((Class<MongoDbCategory>)categoryRepository.getType(), is(equalTo(categoryRepository.CLASS)));
    }

    @Test
    public void get_Valid(){
        String id = "123";
        MongoDbCategory category = new MongoDbCategory();
        expect(template.findById(id, categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(category);
        converter.hydrate(category, Category.class);
        expectLastCall();
        replay(template, converter);

        assertThat(category, is(sameInstance(categoryRepository.get(id))));

    }

    @Test
    public void save_Valid(){
        MongoDbCategory converted = new MongoDbCategory();
        Category item = new MongoDbCategory();

        expect(converter.convert(item, Category.class)).andReturn(converted);
        template.save(converted, CollectionNames.CATEGORY_COLLECTION);
        expectLastCall();
        converter.hydrate(converted, Category.class);
        expectLastCall();
        replay(converter, template);

        assertThat(converted, is(sameInstance(categoryRepository.save(item))));
    }

    @Test
    public void delete_Valid(){
        Category item = new MongoDbCategory();
        String id = "123";
        item.setId(id);
        MongoDbCategory hydrate = new MongoDbCategory();

        expect(template.findById(id, categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(hydrate);
        template.remove(hydrate, CollectionNames.CATEGORY_COLLECTION);
        expectLastCall();
        converter.hydrate(hydrate, Category.class);
        expectLastCall();
        replay(template, converter);

        categoryRepository.delete(item);

        verify(converter, template);
    }

    @Test
    public void getAll(){
        List<MongoDbCategory> categories = new ArrayList<MongoDbCategory>();
        expect(template.findAll(categoryRepository.CLASS, CollectionNames.CATEGORY_COLLECTION)).andReturn(categories);
        replay(template);

        assertThat(CollectionUtils.<Category>toBaseTypedList(categories), is(sameInstance(categoryRepository.getAll())));
    }

    @Test
    public void getLimitedList(){
        int offset = 234;
        int pageSize = 123;
        List<MongoDbCategory> found = new ArrayList<MongoDbCategory>();
        Query q = new Query().skip(offset).limit(pageSize);
        expect(template.find(q, categoryRepository.CLASS)).andReturn(found);
        replay(template);
        assertThat(CollectionUtils.<Category>toBaseTypedList(found), is(sameInstance(categoryRepository.getLimitedList(offset, pageSize))));
    }

    @Test
    public void getCountAll_Valid(){
        long doubleOseven = 007;
        expect(template.count(new Query(), categoryRepository.CLASS)).andReturn(doubleOseven);
        replay(template);
        assertThat((int)doubleOseven, is(sameInstance(categoryRepository.getCountAll())));
    }

}
