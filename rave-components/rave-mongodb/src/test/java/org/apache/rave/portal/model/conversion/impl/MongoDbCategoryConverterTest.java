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

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.model.MongoDbCategory;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.not;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * This is a test class for MongoDb Category Converter
 */
public class MongoDbCategoryConverterTest {

    public static final long USER1ID = 1234L;
    public static final long USER2ID = 1333L;
    public static final Date DATE1 = new Date();
    public static final Date DATE2 = new Date();
    private MongoDbCategoryConverter converter;
    private UserRepository userRepository;
    private MongoWidgetOperations mongoWidgetOperations;


    @Before
    public void setup(){
        converter = new MongoDbCategoryConverter();
        userRepository = createNiceMock(UserRepository.class);
        mongoWidgetOperations = createNiceMock(MongoWidgetOperations.class);
        converter.setUserRepository(userRepository);
        converter.setMongoWidgetOperations(mongoWidgetOperations);
    }


    @Test
    public void hydrate_valid(){
        MongoDbCategory category = new MongoDbCategory();

        converter.hydrate(category);

        assertThat(category.getUserRepository(),is(sameInstance(userRepository)));
        assertThat(category.getWidgetRepository(),is(sameInstance(mongoWidgetOperations)));

    }

    @Test
    public void hydrate_null(){
        converter.hydrate(null);
        assertThat(true, is(true));
    }


    @Test
    public void convert_valid(){
        Category c = new CategoryImpl();
        User user1 = new UserImpl(USER1ID);
        User user2 = new UserImpl(USER2ID);
        c.setCreatedDate(DATE1);
        c.setCreatedUser(user1);
        c.setId(USER1ID);
        c.setLastModifiedUser(user2);
        c.setText("asdf");
        c.setWidgets(Lists.<Widget>newArrayList());
        c.setLastModifiedDate(DATE2);

        MongoDbCategory mongoC = converter.convert(c);
        assertThat(mongoC.getCreatedDate(), is(equalTo(DATE1)));
        assertThat(mongoC.getCreatedUser(), is(nullValue()));
        assertThat(mongoC.getCreatedUserId(), is(equalTo(USER1ID)));
        assertThat(mongoC.getId(), is(equalTo(USER1ID)));
        assertThat(mongoC.getLastModifiedUserId(), is(equalTo(USER2ID)));
        assertThat(mongoC.getLastModifiedUser(), is(nullValue()));
        assertThat(mongoC.getWidgetRepository(), is(nullValue()));
        assertThat(mongoC.getUserRepository(), is(nullValue()));
        assertThat(mongoC.getText(), is("asdf"));
        assertThat(c.getWidgets().size(), is(0));

    }//end convert_valid

    @Test
    public void convert_Null(){
        Category source = new CategoryImpl();

        MongoDbCategory converted = converter.convert(source);

        assertNull(converted.getLastModifiedUserId());
        assertNull(converted.getCreatedUserId());
        assertNotNull(converted.getId());
    }
}
