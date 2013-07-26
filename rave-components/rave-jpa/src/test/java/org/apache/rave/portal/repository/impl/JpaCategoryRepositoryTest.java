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

package org.apache.rave.portal.repository.impl;

import org.apache.openjpa.persistence.PersistenceException;
import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})

public class JpaCategoryRepositoryTest {

    private final Long VALID_ENTITY_ID = 1L;
    private final Long INVALID_ENTITY_ID = -12345L;
    private final Long VALID_USER_ID = 1L;
    private final Long VALID_WIDGET_ID = 1L;

    private User validUser;
    private JpaWidget validWidget;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private CategoryRepository repository;

    private static final String DUPLICATE_TEXT_VALUE = "Sample Category";

    @Before
    public void setup() {
        validUser = new JpaUser(VALID_USER_ID);
        validWidget = new JpaWidget();
        validWidget.setEntityId(VALID_WIDGET_ID);
    }

    @Test
    public void getById_validId() {
        JpaCategory category = (JpaCategory) repository.get(VALID_ENTITY_ID.toString());
        assertThat(category.getEntityId(), is(equalTo(VALID_ENTITY_ID)));
    }

    @Test
    public void getById_invalidId() {
        assertThat(repository.get(INVALID_ENTITY_ID.toString()), is(nullValue()));
    }

    @Test
    @Rollback(true)
    public void save_newEntity() throws Exception {
        final String NEW_TEXT = "My New Category";
        Date now = new Date();
        JpaCategory category = new JpaCategory();
        category.setLastModifiedDate(now);
        category.setLastModifiedUserId(VALID_USER_ID.toString());
        category.setText(NEW_TEXT);
        category.setCreatedDate(now);
        category.setCreatedUserId(VALID_USER_ID.toString());

        assertThat(category.getEntityId(), is(nullValue()));
        repository.save(category);

        Long newEntityId = category.getEntityId();
        assertThat(newEntityId, is(notNullValue()));
        // verify that it persisted ok
        assertThat((JpaCategory)repository.get(newEntityId.toString()), is(category));
    }

    @Test
    @Rollback(true)
    public void save_existingEntity() {
        final String UPDATED_TEXT = "changed the text";
        Category category = repository.get(VALID_ENTITY_ID.toString());
        assertThat(category.getText(), is(not(UPDATED_TEXT)));
        category.setText(UPDATED_TEXT);
        repository.save(category);
        // fetch again and verify update
        Category modCategory = repository.get(VALID_ENTITY_ID.toString());
        assertThat(modCategory.getText(), is(UPDATED_TEXT));
    }

    @Test
    @Rollback(true)
    public void delete() {
        Category entity = repository.get(VALID_ENTITY_ID.toString());
        assertThat(entity, is(notNullValue()));
        repository.delete(entity);
        assertThat(repository.get(VALID_ENTITY_ID.toString()), is(nullValue()));
    }

    @Test
    public void getAll() {
        List<Category> list = repository.getAll();
        assertThat(list.size(), is(6));
        // verify proper sorting alphabetical by text attribute
        String lastText = "";
        for (Category wc : list) {
            String currentText = wc.getText();
            assertThat(currentText.compareTo(lastText) > 0, is(true));
            lastText = currentText;
        }
    }
    @Test
    public void getLimitedList() {
        List<Category> list = repository.getLimitedList(1, 2);
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getText(), is("Cat99"));
        assertThat(list.get(1).getText(), is("News Category"));
    }

    @Test
    public void getCountAll(){
        int count = repository.getCountAll();
        assertThat(count, is(6));
    }

    /**
     * Verify that a unique constraint exception is thrown if a duplicate text value is attempted to be added
     */
    @Test
    @Ignore //This should just merge in rather than throw an exception, which it looks like it does correctly
    public void save_duplicateText_exception() {
        Date now = new Date();
        User user = new JpaUser(1L);

        JpaCategory wc = new JpaCategory();
        wc.setText(DUPLICATE_TEXT_VALUE);
        wc.setCreatedDate(now);
        wc.setCreatedUserId(user.getId());
        wc.setLastModifiedDate(now);
        wc.setLastModifiedUserId(user.getId());

        boolean gotExpectedException = false;
        try {
            repository.save(wc);
            manager.flush();
        } catch (PersistenceException e) {
            assertThat(e.getCause().toString().contains("Unique"), is(true));
            gotExpectedException = true;
        } finally {
            if (!gotExpectedException) {
                fail("Expected to get a PersistenceException due to Unique Constraint Violation");
            }
        }
    }

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaCategory.class);
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_jpaObject() {
        Category category = repository.get(VALID_ENTITY_ID.toString());
        assertThat(category, is(notNullValue()));
        repository.delete(category);
        category = repository.get(VALID_ENTITY_ID.toString());
        assertThat(category, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_implObject() {
        Category category = repository.get(VALID_ENTITY_ID.toString());
        assertThat(category, is(notNullValue()));
        CategoryImpl impl = new CategoryImpl(category.getId());
        repository.delete(impl);
        category = repository.get(VALID_ENTITY_ID.toString());
        assertThat(category, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void removeFromCreatedOrModifiedFields() {
        assertThat(repository.removeFromCreatedOrModifiedFields(VALID_USER_ID.toString()), is(5));
    }
}
