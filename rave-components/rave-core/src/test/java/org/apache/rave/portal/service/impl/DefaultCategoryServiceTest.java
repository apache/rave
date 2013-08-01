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

import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.impl.mock.MockCategoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link org.apache.rave.portal.service.impl.DefaultCategoryService}
 */
public class DefaultCategoryServiceTest {
    private CategoryService service;
    private CategoryRepository repository;

    private final String VALID_ID = "4";
    private final String VALID_TEXT = "category1";
    private final Date VALID_CREATED_DATE = new Date(66666666);
    private final Date VALID_LAST_MODIFIED_DATE = new Date(77777777);
    private final String VALID_CREATED_USER_ID = "77";
    private final String VALID_LAST_MODIFIED_USER_ID = "88";
    private User validCreatedUser;
    private User validLastModifiedUser;
    private Category validCategory;

    private final String INVALID_ID = "-999";

    @Before
    public void setup() {
        repository = createMock(CategoryRepository.class);
        service = new DefaultCategoryService(repository);

        validCreatedUser = new UserImpl(VALID_CREATED_USER_ID);
        validLastModifiedUser = new UserImpl(VALID_LAST_MODIFIED_USER_ID);

        validCategory = new CategoryImpl();
        validCategory.setId(VALID_ID);
        validCategory.setText(VALID_TEXT);
        validCategory.setCreatedUserId(VALID_CREATED_USER_ID);
        validCategory.setCreatedDate(VALID_CREATED_DATE);
        validCategory.setLastModifiedUserId(VALID_LAST_MODIFIED_USER_ID);
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
    public void getAllList() {
        List<Category> list = new ArrayList<Category>();
        list.add(validCategory);
        list.add(new CategoryImpl());
        list.add(new CategoryImpl());

        expect(repository.getAll()).andReturn(list);
        replay(repository);
        assertThat(service.getAllList(), is(list));
        verify(repository);
    }

    @Test
    public void getAll(){
        List<Category> categories = new ArrayList<Category>();
        expect(repository.getAll()).andReturn(categories);
        expect(repository.getCountAll()).andReturn(1);
        replay(repository);

        List<Category> result = service.getAll().getResultSet();
        assertThat(result, is(sameInstance(categories)));
    }

    @Test
    public void getLimitedList(){
        Category cat1 = new CategoryImpl("2", "Fake Category");
        Category cat2 = new CategoryImpl("3", "Another Fake Category");
        List<Category> categories = new ArrayList<Category>();
        categories.add(cat1);
        categories.add(cat2);
        final int pageSize = 10;
        expect(repository.getCountAll()).andReturn(2);
        expect(repository.getLimitedList(0, pageSize)).andReturn(categories);
        replay(repository);

        SearchResult<Category> result = service.getLimitedList(0, pageSize);
        assertEquals(pageSize, result.getPageSize());
        assertSame(categories, result.getResultSet());
        verify(repository);
    }

    @Test
    public void create() {
        final String NEW_CATEGORY_TEXT = "new category";
        final String NEW_ID = "1";
        Category expectedCategory = new CategoryImpl();
        expectedCategory.setText(NEW_CATEGORY_TEXT);

        expect(repository.save(expectedCategory)).andDelegateTo(new MockCategoryRepository() {
            @Override
            public Category save(Category item) {
                item.setId(NEW_ID);
                return item;
            }
        });
        replay(repository);

        Category wc = service.create(NEW_CATEGORY_TEXT, validCreatedUser);
        assertThat(wc.getText(), is(NEW_CATEGORY_TEXT));
        assertThat(wc.getCreatedDate(), is(notNullValue(Date.class)));
        assertThat(wc.getCreatedDate(), is(wc.getLastModifiedDate()));
        assertThat(wc.getCreatedUserId(), is(VALID_CREATED_USER_ID));
        assertThat(wc.getLastModifiedUserId(), is(VALID_CREATED_USER_ID));
        assertThat(wc.getId(), is(NEW_ID));

        verify(repository);
    }

    @Test
    public void update() {
        final String UPDATED_TEXT = "modified category";

        Category expectedSaveCategory = new CategoryImpl();
        expectedSaveCategory.setId(VALID_ID);
        expectedSaveCategory.setText(UPDATED_TEXT);
        expectedSaveCategory.setCreatedUserId(VALID_CREATED_USER_ID);
        expectedSaveCategory.setLastModifiedUserId(VALID_LAST_MODIFIED_USER_ID);
        expectedSaveCategory.setCreatedDate(VALID_CREATED_DATE);
        expectedSaveCategory.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);

        expect(repository.get(VALID_ID)).andReturn(validCategory);
        expect(repository.save(expectedSaveCategory)).andReturn(expectedSaveCategory);
        replay(repository);

        Category updatedCategory = service.update(VALID_ID, UPDATED_TEXT, validLastModifiedUser);
        assertThat(updatedCategory.getId(), is(VALID_ID));
        assertThat(updatedCategory.getText(), is(UPDATED_TEXT));
        assertThat(updatedCategory.getCreatedUserId(), is(VALID_CREATED_USER_ID));
        assertThat(updatedCategory.getLastModifiedUserId(), is(VALID_LAST_MODIFIED_USER_ID));
        assertThat(updatedCategory.getLastModifiedDate().after(updatedCategory.getCreatedDate()), is(true));

        verify(repository);
    }

    @Test
    public void delete() {
        expect(repository.get(validCategory.getId())).andReturn(validCategory);
        repository.delete(validCategory);
        expectLastCall();
        replay(repository);
        service.delete(validCategory);
        verify(repository);
    }
}
