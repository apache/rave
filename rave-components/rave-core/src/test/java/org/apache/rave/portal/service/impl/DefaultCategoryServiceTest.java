/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.service.CategoryService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test for {@link org.apache.rave.portal.service.impl.DefaultCategoryService}
 */
public class DefaultCategoryServiceTest {
    private CategoryService service;
    private CategoryRepository repository;
    
    private final Long VALID_ID = 4L;
    private final String VALID_TEXT = "category1";
    private final Date VALID_CREATED_DATE = new Date(66666666);
    private final Date VALID_LAST_MODIFIED_DATE = new Date(77777777);
    private final Long VALID_CREATED_USER_ID = 77L;
    private final Long VALID_LAST_MODIFIED_USER_ID = 88L;
    private User validCreatedUser;
    private User validLastModifiedUser;    
    private Category validCategory;

    private final Long INVALID_ID = -999L;

    @Before
    public void setup() {
        repository = createMock(CategoryRepository.class);
        service = new DefaultCategoryService(repository);

        validCreatedUser = new User(VALID_CREATED_USER_ID);
        validLastModifiedUser = new User(VALID_LAST_MODIFIED_USER_ID);
        
        validCategory = new Category();
        validCategory.setEntityId(VALID_ID);
        validCategory.setText(VALID_TEXT);
        validCategory.setCreatedUser(validCreatedUser);
        validCategory.setCreatedDate(VALID_CREATED_DATE);
        validCategory.setLastModifiedUser(validLastModifiedUser);
        validCategory.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);
    }

    @Test
    public void get_validCategoryId() {
        expect(repository.get(VALID_ID)).andReturn(validCategory);
        replay(repository);
        assertThat(service.get(VALID_ID), is(validCategory));
        verify(repository);
    }

    @Test
    public void get_invalidCategoryId() {
        expect(repository.get(INVALID_ID)).andReturn(null);
        replay(repository);
        assertThat(service.get(INVALID_ID), is(nullValue(Category.class)));
        verify(repository);
    }

    @Test
    public void getAll() {
        List<Category> list = new ArrayList<Category>();
        list.add(validCategory);
        list.add(new Category());
        list.add(new Category());

        expect(repository.getAll()).andReturn(list);
        replay(repository);
        assertThat(service.getAll(), is(list));
        verify(repository);        
    }

    @Test
    public void create() {
        final String NEW_CATEGORY_TEXT = "new category";
        Category expectedCategory = new Category();
        expectedCategory.setText(NEW_CATEGORY_TEXT);

        expect(repository.save(expectedCategory)).andReturn(expectedCategory);
        replay(repository);

        Category wc = service.create(NEW_CATEGORY_TEXT, validCreatedUser);
        assertThat(wc.getText(), is(NEW_CATEGORY_TEXT));
        assertThat(wc.getCreatedDate(), is(notNullValue(Date.class)));
        assertThat(wc.getCreatedDate(), is(wc.getLastModifiedDate()));
        assertThat(wc.getCreatedUser(), is(validCreatedUser));
        assertThat(wc.getLastModifiedUser(), is(validCreatedUser));
        
        verify(repository);
    }

    @Test
    public void update() {
        final String UPDATED_TEXT = "modified category";

        Category expectedSaveCategory = new Category();
        expectedSaveCategory.setEntityId(VALID_ID);
        expectedSaveCategory.setText(UPDATED_TEXT);
        expectedSaveCategory.setCreatedUser(validCreatedUser);
        expectedSaveCategory.setLastModifiedUser(validLastModifiedUser);
        expectedSaveCategory.setCreatedDate(VALID_CREATED_DATE);
        expectedSaveCategory.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);

        expect(repository.get(VALID_ID)).andReturn(validCategory);
        expect(repository.save(expectedSaveCategory)).andReturn(expectedSaveCategory);
        replay(repository);
        
        Category updatedCategory = service.update(VALID_ID, UPDATED_TEXT, validLastModifiedUser);
        assertThat(updatedCategory.getEntityId(), is(VALID_ID));
        assertThat(updatedCategory.getText(), is(UPDATED_TEXT));
        assertThat(updatedCategory.getCreatedUser(), is(validCreatedUser));
        assertThat(updatedCategory.getLastModifiedUser(), is(validLastModifiedUser));
        assertThat(updatedCategory.getLastModifiedDate().after(updatedCategory.getCreatedDate()), is(true));

        verify(repository);
    }

    @Test
    public void delete() {
        expect(repository.get(validCategory.getEntityId())).andReturn(validCategory);
        repository.delete(validCategory);
        expectLastCall();
        replay(repository);
        service.delete(validCategory);
        verify(repository);
    }
}
