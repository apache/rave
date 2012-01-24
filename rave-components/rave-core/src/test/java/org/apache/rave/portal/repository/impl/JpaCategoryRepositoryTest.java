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
import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private CategoryRepository repository;

    private static final String DUPLICATE_TEXT_VALUE = "Sample Category";

    @Test
    public void getAll() {
        List<Category> list = repository.getAll();
        assertThat(list.size(), is(2));
        // verify proper sorting alphabetical by text attribute
        String lastText = "";
        for (Category wc : list) {
            String currentText = wc.getText();
            assertThat(currentText.compareTo(lastText) > 0, is(true));
            lastText = currentText;
        }
    }

    /**
     * Verify that a unique constraint exception is thrown if a duplicate text value is attempted to be added
     */
    @Test
    public void save_duplicateText_exception() {
        Date now = new Date();
        User user = new User(1L);
        
        Category wc = new Category();
        wc.setText(DUPLICATE_TEXT_VALUE);
        wc.setCreatedDate(now);
        wc.setCreatedUser(user);
        wc.setLastModifiedDate(now);
        wc.setLastModifiedUser(user);

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
}
