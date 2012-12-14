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

import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import sun.security.acl.OwnerImpl;

import java.security.acl.Owner;
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

    private MongoDbWidget widget;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Before
    public void setup(){
        widget = new MongoDbWidget();
        categoryRepository = createMock(CategoryRepository.class);
        userRepository = createMock(UserRepository.class);
        widget.setCategoryRepository(categoryRepository);
        widget.setUserRepository(userRepository);
    }

    @Test
    public void getOwner_Null(){
        widget.setOwnerId((long)123);
        User user = new UserImpl();
        expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);
        assertThat(widget.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void getOwner_Set(){
        User owner = new UserImpl();
        widget.setOwner(owner);
        assertThat(widget.getOwner(), is(sameInstance(owner)));

    }

    @Test
    public void getOwner_Both_Null(){
        assertNull(widget.getOwner());
    }

    @Test
    public void getCategories_Valid(){
        List<Long> categoryIds = Arrays.asList((long)123, (long)321);
        expect(categoryRepository.get((long)123)).andReturn(null);
        Category category = new CategoryImpl();
        expect(categoryRepository.get((long)321)).andReturn(category);
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
        widget.setId((long)123);
        Widget r = new MongoDbWidget();
        assertFalse(widget.equals(r));
        assertFalse(r.equals(widget));

    }

    @Test
    public void equals_Valid(){
        widget.setId((long)123);
        Widget r = new MongoDbWidget();
        ((MongoDbWidget)r).setId((long) 123);
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
        widget.setId((long)123);
        assertNotNull(widget.hashCode());
    }

}
