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

package org.apache.rave.portal.repository;

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class JpaRegionWidgetRepositoryTest {
    @PersistenceContext
    private EntityManager manager;

    private static final Long VALID_REGION_WIDGET_ID = 1L;
    private static final Long INVALID_REGION_WIDGET_ID = -1L;
    private static final String VALID_PREFERENCE_NAME = "color";
    private static final String VALID_PREFERENCE_VALUE = "blue";

    @Autowired
    private RegionWidgetRepository repository;

    @Test
    public void getById_validId() {
        RegionWidget regionWidget = repository.getById(VALID_REGION_WIDGET_ID);
        assertThat(regionWidget, is(notNullValue()));
        assertThat(regionWidget.getId(), is(equalTo(VALID_REGION_WIDGET_ID)));
    }

    @Test
    public void getById_invalidId() {
        RegionWidget regionWidget = repository.getById(INVALID_REGION_WIDGET_ID);
        assertThat(regionWidget, is(nullValue()));
    }

    @Test
    @Rollback(true)
    public void save_newEntity() {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setPreferences(new ArrayList<RegionWidgetPreference>());

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();
        assertThat(saved, is(sameInstance(regionWidget)));
        assertThat(saved.getId(), is(notNullValue()));
    }

    @Test
    @Rollback(true)
    public void save_existingEntity() {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setId(VALID_REGION_WIDGET_ID);
        regionWidget.setPreferences(new ArrayList<RegionWidgetPreference>());

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();
        assertThat(saved, is(not(sameInstance(regionWidget))));
        assertThat(saved.getId(), is(equalTo(regionWidget.getId())));
    }

    @Test
    @Rollback(true)
    public void save_cascadePersist() {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setPreferences(new ArrayList<RegionWidgetPreference>());
        RegionWidgetPreference regionWidgetPreference = new RegionWidgetPreference(null, null, VALID_PREFERENCE_NAME,
                VALID_PREFERENCE_VALUE);
        regionWidget.getPreferences().add(regionWidgetPreference);

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();

        assertThat(saved.getPreferences().size(), is(equalTo(1)));
        RegionWidgetPreference actual = saved.getPreferences().get(0);

        assertThat(actual, is(sameInstance(regionWidgetPreference)));
        assertThat(actual.getId(), is(notNullValue()));
    }

    @Test
    @Rollback(true)
    public void save_cascadeMerge() {
        long VALID_PREFERENCE_ID = addPreferenceToRegionWidget(VALID_REGION_WIDGET_ID);

        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setId(VALID_REGION_WIDGET_ID);
        regionWidget.setPreferences(new ArrayList<RegionWidgetPreference>());
        RegionWidgetPreference regionWidgetPreference = new RegionWidgetPreference(VALID_PREFERENCE_ID,
                VALID_REGION_WIDGET_ID, VALID_PREFERENCE_NAME, VALID_PREFERENCE_VALUE);
        regionWidget.getPreferences().add(regionWidgetPreference);

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();

        assertThat(saved.getPreferences().size(), is(equalTo(1)));
        RegionWidgetPreference actual = saved.getPreferences().get(0);

        assertThat(actual, is(not(sameInstance(regionWidgetPreference))));
        assertThat(actual.getId(), is(equalTo(regionWidgetPreference.getId())));
    }

    @Test
    @Rollback(true)
    public void save_cascadeOrphan() {
        long VALID_PREFERENCE_ID = addPreferenceToRegionWidget(VALID_REGION_WIDGET_ID);

        RegionWidget regionWidget = repository.getById(VALID_REGION_WIDGET_ID);
        regionWidget.getPreferences().remove(0);

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();
        RegionWidgetPreference preference = manager.find(RegionWidgetPreference.class, VALID_PREFERENCE_ID);

        assertThat(saved.getPreferences().size(), is(equalTo(0)));
        assertThat(preference, is(nullValue()));
    }

    private long addPreferenceToRegionWidget(long validRegionWidgetId) {
        RegionWidget regionWidget = repository.getById(validRegionWidgetId);
        RegionWidgetPreference regionWidgetPreference = new RegionWidgetPreference(null, validRegionWidgetId,
                VALID_PREFERENCE_NAME, VALID_PREFERENCE_VALUE);
        regionWidget.getPreferences().add(regionWidgetPreference);

        RegionWidget saved = repository.save(regionWidget);
        manager.flush();
        return saved.getPreferences().get(0).getId();
    }
}