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

import org.apache.rave.portal.model.JpaRegion;
import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
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
import java.util.ArrayList;

import org.apache.rave.portal.repository.RegionRepository;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaRegionRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    private static final Long REGION_ID = 1L;
    private static final Long INVALID_REGION_ID = -1L;

    @Autowired
    private RegionRepository repository;

    @Test
    public void getById_validId() {
        JpaRegion region = (JpaRegion)repository.get(REGION_ID);
        assertThat(region, is(notNullValue()));
        assertThat(region.getEntityId(), is(equalTo(1L)));
        assertThat(region.getRegionWidgets().size(), is(equalTo(2)));
    }

    @Test
    public void getById_invalidId() {
        JpaRegion region = (JpaRegion)repository.get(INVALID_REGION_ID);
        assertThat(region, is(nullValue()));
    }

    @Test
    @Rollback(true)
    public void save_newEntity() {
        JpaRegion region = new JpaRegion();
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        JpaRegion saved = (JpaRegion)repository.save(region);
        manager.flush();
        assertThat(saved, is(sameInstance(region)));
        assertThat(saved.getEntityId(), is(notNullValue()));
    }

    @Test
    @Rollback(true)
    public void save_existingEntity() {
        JpaRegion region = new JpaRegion();
        region.setEntityId(1L);
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        JpaRegion saved = (JpaRegion)repository.save(region);
        manager.flush();
        assertThat(saved, is(not(sameInstance(region))));
        assertThat(saved.getEntityId(), is(equalTo(region.getEntityId())));
    }

    @Test
    public void save_cascadePersist() {
        JpaRegion region = new JpaRegion();
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        RegionWidget regionWidget = new JpaRegionWidget();
        region.getRegionWidgets().add(regionWidget);

        JpaRegion saved = (JpaRegion)repository.save(region);
        manager.flush();

        assertThat(saved.getRegionWidgets().size(), is(equalTo(1)));
        RegionWidget actual = saved.getRegionWidgets().get(0);

        assertThat(actual, is(sameInstance(regionWidget)));
        assertThat(actual.getId(), is(notNullValue()));
    }

    @Test
    public void save_cascadeMerge() {

        JpaRegion region = new JpaRegion();
        region.setEntityId(1L);
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        RegionWidget regionWidget = new JpaRegionWidget();
        regionWidget.setId(1L);
        region.getRegionWidgets().add(regionWidget);

        Region saved = repository.save(region);
        manager.flush();

        assertThat(saved.getRegionWidgets().size(), is(equalTo(1)));
        RegionWidget actual = saved.getRegionWidgets().get(0);

        assertThat(actual, is(not(sameInstance(regionWidget))));
        assertThat(actual.getId(), is(equalTo(1L)));
    }

    @Test
    @Ignore //TODO Failed test due to interface migration "No metadata was found for type "interface org.apache.rave.portal.model.RegionWidget". The class is not enhanced."
    public void save_cascadeOrphan() {
        JpaRegion region = (JpaRegion)repository.get(1L);
        long id = region.getRegionWidgets().get(0).getId();
        region.getRegionWidgets().remove(0);

        Region saved = repository.save(region);
        manager.flush();
        RegionWidget widget = manager.find(RegionWidget.class, id);

        assertThat(saved.getRegionWidgets().size(), is(equalTo(1)));
        assertThat(widget, is(nullValue()));
    }

}
