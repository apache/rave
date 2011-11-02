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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.portal.repository.WidgetRatingRepository;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Test for {@link org.apache.rave.portal.repository.impl.JpaWidgetRatingRepository}
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetRatingRepositoryTest {

    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private WidgetRatingRepository repository;

    @Test
    public void getByWidgetIdAndUserId_found() {
        Long widgetId = 1L;
        Long userId = 1L;
        WidgetRating widgetRating = repository.get(1L);
        WidgetRating byWidgetAndUser = repository.getByWidgetIdAndUserId(widgetId, userId);
        assertEquals(widgetRating, byWidgetAndUser);
    }

    @Test
    public void getByWidgetIdAndUserId_missing() {
        Long widgetId = 123L;
        Long userId = 234L;
        WidgetRating byWidgetAndUser = repository.getByWidgetIdAndUserId(widgetId, userId);
        assertNull("Don't expect a WidgetRating for widgetId 123 and userId 234", byWidgetAndUser);
    }
}
