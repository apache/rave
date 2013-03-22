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

import org.apache.rave.model.Category;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:49 PM
 */
public class MongoDbWidgetTest {

    public static final String ID_1 = "123";
    public static final String ID_2 = "321";
    private MongoDbWidget widget;
    private CategoryRepository categoryRepository;

    @Before
    public void setup(){
        widget = new MongoDbWidget();
        categoryRepository = createMock(CategoryRepository.class);
        widget.setCategoryRepository(categoryRepository);
    }

    @Test
    public void getCategories_Valid(){
        List<String> categoryIds = Arrays.asList(ID_1, ID_2);
        expect(categoryRepository.get(ID_1)).andReturn(null);
        Category category = new CategoryImpl();
        expect(categoryRepository.get(ID_2)).andReturn(category);
        replay(categoryRepository);
        widget.setCategoryIds(categoryIds);
        List<Category> result = widget.getCategories();
        assertNotNull(result);
        assertTrue(result.contains(category));
    }

    @Test
    public void getCategories_Null(){
        List<Category> categoryList = new ArrayList<Category>();
        widget.setCategories(categoryList);
        assertNotNull(widget.getCategories());
    }

    @Test
    public void getCategories_Null_Categories_Full(){
        List<Category> categoryList = new ArrayList<Category>();
        categoryList.add(new CategoryImpl());
        widget.setCategories(categoryList);
        assertThat(categoryList, is(sameInstance(widget.getCategories())));
    }

    @Test
    public void equals_Same(){
        assertTrue(widget.equals(widget));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(widget.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        widget.setId(ID_1);
        Widget r = new MongoDbWidget();
        assertFalse(widget.equals(r));
        assertFalse(r.equals(widget));

    }

    @Test
    public void equals_Valid(){
        widget.setId(ID_1);
        Widget r = new MongoDbWidget();
        ((MongoDbWidget)r).setId(ID_1);
        assertTrue(widget.equals(r));
    }

    @Test
    public void equals_Null(){
        widget.setId(null);
        Widget r = new MongoDbWidget();
        ((MongoDbWidget)r).setId(null);
        assertTrue(widget.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(widget.hashCode());
    }

    @Test
    public void hashCode_Null(){
        widget.setId(ID_1);
        assertNotNull(widget.hashCode());
    }

}
