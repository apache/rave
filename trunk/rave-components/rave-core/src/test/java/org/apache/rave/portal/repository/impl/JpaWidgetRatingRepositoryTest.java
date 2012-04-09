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

import org.apache.rave.portal.model.WidgetRating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.portal.repository.WidgetRatingRepository;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test for {@link org.apache.rave.portal.repository.impl.JpaWidgetRatingRepository}
 */
@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetRatingRepositoryTest {
    private Long VALID_WIDGET_ID = 1L;
    private Long VALID_USER_ID = 1L;
    private Long VALID_WIDGET_RATING_ID = 1L;

    private Long INVALID_WIDGET_ID = 123L;
    private Long INVALID_USER_ID = 234L;
    
    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private WidgetRatingRepository repository;

    @Test
    public void getByWidgetIdAndUserId_found() {
        assertThat(repository.get(VALID_WIDGET_RATING_ID), is(repository.getByWidgetIdAndUserId(VALID_WIDGET_ID, VALID_USER_ID)));
    }

    @Test
    public void getByWidgetIdAndUserId_missing() {
        assertThat(repository.getByWidgetIdAndUserId(INVALID_WIDGET_ID, INVALID_USER_ID), is(nullValue(WidgetRating.class)));       
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deleteAll() {
        assertThat(repository.deleteAll(VALID_USER_ID), is(2));
    }
}
