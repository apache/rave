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

import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
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
public class JpaRegionRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    private static final Long REGION_ID = 1L;
    private static final Long INVALID_REGION_ID = -1L;

    @Autowired
    private RegionRepository repository;

    @Test
    public void getById_validId() {
        Region region = repository.get(REGION_ID);
        assertThat(region, is(notNullValue()));
        assertThat(region.getId(), is(equalTo(1L)));
        assertThat(region.getRegionWidgets().size(), is(equalTo(2)));
    }

    @Test
    public void getById_invalidId() {
        Region region = repository.get(INVALID_REGION_ID);
        assertThat(region, is(nullValue()));
    }

    @Test
    @Rollback(true)
    public void save_newEntity() {
        Region region = new Region();
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        Region saved = repository.save(region);
        manager.flush();
        assertThat(saved, is(sameInstance(region)));
        assertThat(saved.getId(), is(notNullValue()));
    }

    @Test
    @Rollback(true)
    public void save_existingEntity() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        Region saved = repository.save(region);
        manager.flush();
        assertThat(saved, is(not(sameInstance(region))));
        assertThat(saved.getId(), is(equalTo(region.getId())));
    }

    @Test
    public void save_cascadePersist() {
        Region region = new Region();
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        RegionWidget regionWidget = new RegionWidget();
        region.getRegionWidgets().add(regionWidget);

        Region saved = repository.save(region);
        manager.flush();

        assertThat(saved.getRegionWidgets().size(), is(equalTo(1)));
        RegionWidget actual = saved.getRegionWidgets().get(0);

        assertThat(actual, is(sameInstance(regionWidget)));
        assertThat(actual.getId(), is(notNullValue()));
    }

    @Test
    public void save_cascadeMerge() {

        Region region = new Region();
        region.setId(1L);
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        RegionWidget regionWidget = new RegionWidget();
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
    public void save_cascadeOrphan() {
        Region region = repository.get(1L);
        long id = region.getRegionWidgets().get(0).getId();
        region.getRegionWidgets().remove(0);

        Region saved = repository.save(region);
        manager.flush();
        RegionWidget widget = manager.find(RegionWidget.class, id);

        assertThat(saved.getRegionWidgets().size(), is(equalTo(1)));
        assertThat(widget, is(nullValue()));


    }

}
