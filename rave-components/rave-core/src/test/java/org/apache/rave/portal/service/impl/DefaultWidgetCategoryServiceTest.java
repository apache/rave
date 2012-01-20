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

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultText;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetCategory;
import org.apache.rave.portal.repository.WidgetCategoryRepository;
import org.apache.rave.portal.service.WidgetCategoryService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test for {@link org.apache.rave.portal.service.impl.DefaultWidgetCategoryService}
 */
public class DefaultWidgetCategoryServiceTest {

    private WidgetCategoryService service;
    private WidgetCategoryRepository repository;
    
    private final Long VALID_ID = 4L;
    private final String VALID_TEXT = "category1";
    private final Date VALID_CREATED_DATE = new Date(66666666);
    private final Date VALID_LAST_MODIFIED_DATE = new Date(77777777);
    private final Long VALID_CREATED_USER_ID = 77L;
    private final Long VALID_LAST_MODIFIED_USER_ID = 88L;
    private User validCreatedUser;
    private User validLastModifiedUser;    
    private WidgetCategory validWidgetCategory;

    private final Long INVALID_ID = -999L;

    @Before
    public void setup() {
        repository = createMock(WidgetCategoryRepository.class);
        service = new DefaultWidgetCategoryService(repository);

        validCreatedUser = new User(VALID_CREATED_USER_ID);
        validLastModifiedUser = new User(VALID_LAST_MODIFIED_USER_ID);
        
        validWidgetCategory = new WidgetCategory();
        validWidgetCategory.setEntityId(VALID_ID);
        validWidgetCategory.setText(VALID_TEXT);
        validWidgetCategory.setCreatedUser(validCreatedUser);
        validWidgetCategory.setCreatedDate(VALID_CREATED_DATE);
        validWidgetCategory.setLastModifiedUser(validLastModifiedUser);
        validWidgetCategory.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);
    }

    @Test
    public void get_validWidgetCategoryId() {
        expect(repository.get(VALID_ID)).andReturn(validWidgetCategory);
        replay(repository);
        assertThat(service.get(VALID_ID), is(validWidgetCategory));
        verify(repository);
    }

    @Test
    public void get_invalidWidgetCategoryId() {
        expect(repository.get(INVALID_ID)).andReturn(null);
        replay(repository);
        assertThat(service.get(INVALID_ID), is(nullValue(WidgetCategory.class)));
        verify(repository);
    }

    @Test
    public void getAll() {
        List<WidgetCategory> list = new ArrayList<WidgetCategory>();
        list.add(validWidgetCategory);
        list.add(new WidgetCategory());
        list.add(new WidgetCategory());

        expect(repository.getAll()).andReturn(list);
        replay(repository);
        assertThat(service.getAll(), is(list));
        verify(repository);        
    }

    @Test
    public void create() {
        final String NEW_CATEGORY_TEXT = "new category";
        WidgetCategory expectedWidgetCategory = new WidgetCategory();
        expectedWidgetCategory.setText(NEW_CATEGORY_TEXT);

        expect(repository.save(expectedWidgetCategory)).andReturn(expectedWidgetCategory);
        replay(repository);

        WidgetCategory wc = service.create(NEW_CATEGORY_TEXT, validCreatedUser);
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

        WidgetCategory expectedSaveWidgetCategory = new WidgetCategory();
        expectedSaveWidgetCategory.setEntityId(VALID_ID);
        expectedSaveWidgetCategory.setText(UPDATED_TEXT);
        expectedSaveWidgetCategory.setCreatedUser(validCreatedUser);
        expectedSaveWidgetCategory.setLastModifiedUser(validLastModifiedUser);
        expectedSaveWidgetCategory.setCreatedDate(VALID_CREATED_DATE);
        expectedSaveWidgetCategory.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);

        expect(repository.get(VALID_ID)).andReturn(validWidgetCategory);
        expect(repository.save(expectedSaveWidgetCategory)).andReturn(expectedSaveWidgetCategory);
        replay(repository);
        
        WidgetCategory updatedWidgetCategory = service.update(VALID_ID, UPDATED_TEXT, validLastModifiedUser);
        assertThat(updatedWidgetCategory.getEntityId(), is(VALID_ID));
        assertThat(updatedWidgetCategory.getText(), is(UPDATED_TEXT));
        assertThat(updatedWidgetCategory.getCreatedUser(), is(validCreatedUser));
        assertThat(updatedWidgetCategory.getLastModifiedUser(), is(validLastModifiedUser));
        assertThat(updatedWidgetCategory.getLastModifiedDate().after(updatedWidgetCategory.getCreatedDate()), is(true));

        verify(repository);
    }

    @Test
    public void delete() {
        repository.delete(validWidgetCategory);
        expectLastCall();
        replay(repository);
        service.delete(validWidgetCategory);
        verify(repository);
    }
}
