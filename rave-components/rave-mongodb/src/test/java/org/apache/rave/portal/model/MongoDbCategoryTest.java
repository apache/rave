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

package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 12:03 PM
 */
public class MongoDbCategoryTest {
    private MongoDbCategory category;
    private UserRepository userRepository;
    private MongoWidgetOperations widgetOperations;

    @Before
    public void setup() {
        category = new MongoDbCategory();
        userRepository = createMock(UserRepository.class);
        widgetOperations = createMock(MongoWidgetOperations.class);
        category.setUserRepository(userRepository);
        category.setWidgetRepository(widgetOperations);
    }

    @Test
    public void testCategory() {

        Long lastModifiedUserId = (long) 123;
        Long createdUserId = (long) 321;
        category.setLastModifiedUserId(lastModifiedUserId);
        category.setCreatedUserId(createdUserId);

        assertThat(category.getLastModifiedUserId(), is(equalTo((long) 123)));
        assertThat(category.getCreatedUserId(), is(equalTo((long) 321)));
        assertThat(category.getUserRepository(), is(sameInstance(userRepository)));
        assertThat(category.getWidgetRepository(), is(sameInstance(widgetOperations)));
    }

    @Test
    public void getCreatedUser_Creator_Set() {
        User user = new UserImpl();
        category.setCreatedUser(user);
        assertThat(category.getCreatedUser(), is(sameInstance(user)));
    }

    @Test
    public void getCreatedUser_CreatedUserId_Null() {
        category.setCreatedUser(null);
        category.setCreatedUserId(null);

        assertNull(category.getCreatedUser());
    }

    @Test
    public void getCreatedUser_UserRepository_Null(){
        category.setCreatedUser(null);
        category.setCreatedUserId((long)123);
        category.setUserRepository(null);

        assertNull(category.getCreatedUser());
    }

    @Test
    public void getCreatedUser_All_True() {
        category.setCreatedUserId((long) 321);
        User user = new UserImpl();
        expect(userRepository.get((long) 321)).andReturn(user);
        replay(userRepository);

        assertThat(category.getCreatedUser(), is(sameInstance(user)));
    }

    @Test
    public void getLastModifiedUser_LastModifier_Set(){
        User user = new UserImpl();
        category.setLastModifiedUser(user);

        assertThat(category.getLastModifiedUser(), is(sameInstance(user)));
    }

    @Test
    public void getLastModifiedUser_LastModifiedUserId_Null(){
        category.setLastModifiedUser(null);
        category.setLastModifiedUserId(null);
        assertNull(category.getLastModifiedUser());
    }

    @Test
    public void getLastModifiedUser_UserRepository_Null(){
        category.setLastModifiedUser(null);
        category.setLastModifiedUserId((long)123);
        category.setUserRepository(null);

        assertNull(category.getLastModifiedUser());
    }

    @Test
    public void getLastModifiedUser_AllTrue() {
        category.setLastModifiedUserId((long) 321);
        User user = new UserImpl();
        expect(userRepository.get((long) 321)).andReturn(user);
        replay(userRepository);

        assertThat(category.getLastModifiedUser(), is(sameInstance(user)));
    }

    @Test
    public void getWidgets_Widgets_Null() {
        List<Widget> widgets = new ArrayList<Widget>();
        expect(widgetOperations.find(query(where("categoryIds").is(category.getId())))).andReturn(widgets);
        replay(widgetOperations);

        assertThat(category.getWidgets(), is(sameInstance(widgets)));
    }

    @Test
    public void getWidgets_Widgets_Set() {
        List<Widget> widgets = new ArrayList<Widget>();
        category.setWidgets(widgets);

        assertThat(category.getWidgets(), is(sameInstance(widgets)));
    }
}
