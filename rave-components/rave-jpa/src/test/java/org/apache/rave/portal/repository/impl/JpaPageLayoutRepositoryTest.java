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

import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.Region;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageLayoutRepositoryTest {

    private static final Long VALID_ID = 2L;
    private static final String VALID_LAYOUT_CODE = "columns_2";
    private static final String INVALID_LAYOUT_CODE = "invalid_layout";

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PageLayoutRepository repository;

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaPageLayout.class);
    }

    @Test
    public void get() {
        JpaPageLayout p = (JpaPageLayout) repository.get(VALID_ID.toString());
        assertThat(p.getEntityId(), is(VALID_ID));
        assertThat(p.getCode(), is(VALID_LAYOUT_CODE));
    }

    @Test
    public void getAll() {
        List<PageLayout> pageLayouts = repository.getAll();
        assertThat(pageLayouts.size(), is(8));

        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (PageLayout pl : pageLayouts) {
            Long currentRenderSequence = pl.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
        }
    }

    @Test
    public void getAllUserSelectable() {
        List<PageLayout> pageLayouts = repository.getAllUserSelectable();
        assertThat(pageLayouts.size(), is(7));
        // test that the query returns the pages in proper render sequence order
        Long lastRenderSequence = -1L;
        for (PageLayout pl : pageLayouts) {
            Long currentRenderSequence = pl.getRenderSequence();
            assertThat(currentRenderSequence > lastRenderSequence, is(true));
            lastRenderSequence = currentRenderSequence;
            assertThat(pl.isUserSelectable(), is(true));
        }
    }

    @Test
    public void getByPageLayoutCode() {
        JpaPageLayout pageLayout = (JpaPageLayout)repository.getByPageLayoutCode(VALID_LAYOUT_CODE);
        assertThat(pageLayout.getCode(), is(VALID_LAYOUT_CODE));
        assertThat(pageLayout.getNumberOfRegions(), is(2L));
        assertThat(pageLayout.getEntityId(), is(notNullValue(Long.class)));
    }

    @Test
    public void getByPageLayoutCode_invalidCode() {
        assertThat((JpaPageLayout)repository.getByPageLayoutCode(INVALID_LAYOUT_CODE), is(nullValue(JpaPageLayout.class)));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        final Long newNumRegions = 3L;
        final String newCode = "new_code";
        final Long newRenderSeq = 22L;
        final boolean newUserSelectable = true;

        JpaPageLayout pageLayout = new JpaPageLayout();
        pageLayout.setNumberOfRegions(newNumRegions);
        pageLayout.setCode(newCode);
        pageLayout.setRenderSequence(newRenderSeq);
        pageLayout.setUserSelectable(newUserSelectable);

        assertThat(pageLayout.getEntityId(), is(nullValue()));
        repository.save(pageLayout);
        Long newId = pageLayout.getEntityId();
        assertThat(newId > 0, is(true));
        PageLayout newLayout = repository.get(newId.toString());
        assertThat(newLayout.getCode(), is(newCode));
        assertThat(newLayout.getRenderSequence(), is(newRenderSeq));
        assertThat(newLayout.getNumberOfRegions(), is(newNumRegions));
        assertThat(newLayout.isUserSelectable(), is(newUserSelectable));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        final String UPDATED_CODE = "updated code";
        PageLayout pageLayout = repository.get(VALID_ID.toString());
        assertThat(pageLayout.getCode(), is(not(UPDATED_CODE)));
        pageLayout.setCode(UPDATED_CODE);
        repository.save(pageLayout);
        PageLayout updatedPageLayout = repository.get(VALID_ID.toString());
        assertThat(updatedPageLayout.getCode(), is(UPDATED_CODE));
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback(true)
    public void delete() {
        PageLayout pl = repository.get(VALID_ID.toString());
        assertThat(pl, is(notNullValue()));
        repository.delete(pl);
        pl = repository.get(VALID_ID.toString());
        assertThat(pl, is(nullValue()));
    }
}
