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

import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetRatingRepositoryTest {
    private String VALID_WIDGET_ID = "1";
    private String VALID_USER_ID = "1";
    private String VALID_WIDGET_RATING_ID = "1";

    private String INVALID_WIDGET_ID = "123";
    private String INVALID_USER_ID = "234";

    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private WidgetRepository repository;

    @Test
    public void getByWidgetIdAndUserId_found() {
        assertThat(repository.getRatingById(VALID_WIDGET_ID, VALID_WIDGET_RATING_ID), is(repository.getWidgetRatingsByWidgetIdAndUserId(VALID_WIDGET_ID, VALID_USER_ID)));
    }

    @Test
    public void getByWidgetIdAndUserId_missing() {
        assertThat(repository.getWidgetRatingsByWidgetIdAndUserId(INVALID_WIDGET_ID, INVALID_USER_ID), is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deleteAll() {
        assertThat(repository.deleteAllWidgetRatings(VALID_USER_ID), is(2));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete() {
        WidgetRating wr = repository.getRatingById(VALID_WIDGET_ID, VALID_WIDGET_RATING_ID);
        assertThat(wr, is(notNullValue()));
        repository.deleteWidgetRating(VALID_WIDGET_ID, wr);
        wr = repository.getRatingById(VALID_WIDGET_ID, VALID_WIDGET_RATING_ID);
        assertThat(wr, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        final int EXPECTED_SCORE = 1;

        WidgetRating wr = new JpaWidgetRating();
        wr.setScore(EXPECTED_SCORE);
        wr.setUserId(VALID_USER_ID);
        assertThat(wr.getId(), is(nullValue()));
        repository.createWidgetRating(VALID_WIDGET_ID, wr);
        String newId = wr.getId();
        assertThat(Long.parseLong(newId) > 0, is(true));
        WidgetRating newRating = repository.getRatingById(VALID_WIDGET_ID, newId);
        assertThat(newRating.getScore(), is(EXPECTED_SCORE));
        assertThat(newRating.getUserId(), is(VALID_USER_ID));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        int EXPECTED_SCORE = 99;
        WidgetRating wr = repository.getRatingById(VALID_WIDGET_ID, VALID_WIDGET_RATING_ID);
        assertThat(wr.getScore(), is(not(EXPECTED_SCORE)));
        wr.setScore(99);
        repository.updateWidgetRating(VALID_WIDGET_ID, wr);
        WidgetRating updatedRating = repository.getRatingById(VALID_WIDGET_ID, VALID_WIDGET_RATING_ID);
        assertThat(updatedRating.getScore(), is(EXPECTED_SCORE));
    }
}
