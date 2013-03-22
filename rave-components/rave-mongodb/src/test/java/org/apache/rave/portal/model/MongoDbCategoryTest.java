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

import org.apache.rave.model.Widget;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 12:03 PM
 */
public class MongoDbCategoryTest {
    public static final String ID_1 = "123";
    public static final String ID_2 = "321";
    private MongoDbCategory category;
    private UserRepository userRepository;
    private MongoWidgetOperations widgetOperations;

    @Before
    public void setup() {
        category = new MongoDbCategory();
        userRepository = createMock(UserRepository.class);
        widgetOperations = createMock(MongoWidgetOperations.class);
        category.setWidgetRepository(widgetOperations);
    }

    @Test
    public void testCategory() {

        String lastModifiedUserId = ID_1;
        String createdUserId = ID_2;
        category.setLastModifiedUserId(lastModifiedUserId);
        category.setCreatedUserId(createdUserId);

        assertThat(category.getLastModifiedUserId(), is(equalTo(ID_1)));
        assertThat(category.getCreatedUserId(), is(equalTo(ID_2)));
        assertThat(category.getWidgetRepository(), is(sameInstance(widgetOperations)));
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
