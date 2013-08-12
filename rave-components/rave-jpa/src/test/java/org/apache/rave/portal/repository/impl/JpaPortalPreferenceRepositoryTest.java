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

import org.apache.rave.portal.model.JpaPortalPreference;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test class for {@link JpaPortalPreferenceRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPortalPreferenceRepositoryTest {

    private final Long VALID_ID = 1L;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PortalPreferenceRepository repository;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaPortalPreference.class);
    }

    @Test
    public void get() {
        JpaPortalPreference p = (JpaPortalPreference) repository.get(VALID_ID.toString());
        assertThat(p.getEntityId(), is(VALID_ID));
        assertThat(p.getKey(), is("color"));
        assertThat(p.getValues().size(), is(3));
    }

    @Test
    public void testGetAll() {
        final List<PortalPreference> preferences = repository.getAll();
        assertTrue(preferences.size() == 2);
    }

    @Test
    public void testGetLimitedList(){
        final List<PortalPreference> preferences = repository.getLimitedList(1, 1);
        assertTrue(preferences.size() == 1);
    }

    @Test
    public void testCountAll(){
        int count = repository.getCountAll();
        assertTrue(count == 2);
    }

    @Test
    public void testGetByKey() {
        PortalPreference preference = repository.getByKey("color");
        assertNotNull(preference);
        assertTrue("Expecting preference with one of the values to be red", preference.getValues().contains("red"));
        assertTrue("Expecting preference with one of the values to be blue", preference.getValues().contains("blue"));
    }

    @Test
    public void testGetByKey_notExisting() {
        PortalPreference preference = repository.getByKey("foo");
        assertNull(preference);
    }

    @Test
    public void testValuesAreOverwritten() throws Exception {
        PortalPreference preference = repository.getByKey("color");
        List<String> newColors = new ArrayList<String>();
        newColors.add("purple");
        newColors.add("green");
        preference.setValues(newColors);
        final PortalPreference saved = repository.save(preference);
        assertEquals(2, saved.getValues().size());
    }

    @Test
    @Rollback(true)
    public void testNoPreferences() {
        final List<PortalPreference> preferences = repository.getAll();
        for (PortalPreference preference : preferences) {
            repository.delete(preference);
        }

        final List<PortalPreference> empty = repository.getAll();
        assertTrue(empty.isEmpty());
    }
}
