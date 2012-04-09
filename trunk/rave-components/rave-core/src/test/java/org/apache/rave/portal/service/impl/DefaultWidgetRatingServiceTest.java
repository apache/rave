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

package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.apache.rave.portal.service.WidgetRatingService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test for {@link org.apache.rave.portal.service.impl.DefaultWidgetRatingService}
 */
public class DefaultWidgetRatingServiceTest {

    private WidgetRatingService service;
    private WidgetRatingRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = createMock(WidgetRatingRepository.class);
        service = new DefaultWidgetRatingService(repository);
    }

    @Test
    public void testGetByWidgetIdAndUserId() {
        WidgetRating widgetRating = new WidgetRating(1L, 2L, 3L, 5);
        expect(repository.getByWidgetIdAndUserId(2L, 3L)).andReturn(widgetRating);
        replay(repository);
        final WidgetRating rating = service.getByWidgetIdAndUserId(2L, 3L);
        assertEquals("Score is 5", Integer.valueOf(5), rating.getScore());
        verify(repository);
    }

    @Test
    public void updateScore() {
        WidgetRating widgetRating = createMock(WidgetRating.class);
        widgetRating.setScore(10);

        expectLastCall().once();
        expect(repository.save(widgetRating)).andReturn(widgetRating);
        replay(repository, widgetRating);
        service.updateScore(widgetRating, 10);

        verify(repository, widgetRating);
    }

    @Test
    public void saveWidgetRating_new() {
        WidgetRating newRating = new WidgetRating();
        newRating.setWidgetId(2L);
        newRating.setUserId(1L);
        newRating.setScore(10);

        expect(repository.getByWidgetIdAndUserId(2L, 1L)).andReturn(null);
        expect(repository.save(newRating)).andReturn(newRating);
        replay(repository);

        service.saveWidgetRating(newRating);
        verify(repository);
    }

    @Test
    public void saveWidgetRating_existing() {
        WidgetRating existingRating = new WidgetRating(1L, 1L, 1L, 5);
        WidgetRating newRating = new WidgetRating();
        newRating.setWidgetId(1L);
        newRating.setUserId(1L);
        newRating.setScore(10);

        expect(repository.getByWidgetIdAndUserId(1L, 1L)).andReturn(existingRating);
        expect(repository.save(existingRating)).andReturn(existingRating);
        replay(repository);

        service.saveWidgetRating(newRating);
        verify(repository);

        assertEquals("Updated score", Integer.valueOf(10), existingRating.getScore());
    }

    @Test
    public void removeWidgetRating_existingRating() {
        final WidgetRating widgetRating = new WidgetRating(1L, 1L, 1L, 5);

        expect(repository.getByWidgetIdAndUserId(1L, 1L)).andReturn(widgetRating);
        repository.delete(widgetRating);
        expectLastCall();
        replay(repository);

        service.removeWidgetRating(1L, 1L);
        verify(repository);
    }

    @Test
    public void removeWidgetRating_notExisting() {
        expect(repository.getByWidgetIdAndUserId(1L, 2L)).andReturn(null);
        expectLastCall();
        replay(repository);
        service.removeWidgetRating(1L, 2L);
        verify(repository);
    }
    
    @Test
    public void deleteAll() {
        final Long USER_ID = 33L;
        final int EXPECTED_COUNT = 43;
        
        expect(repository.deleteAll(USER_ID)).andReturn(EXPECTED_COUNT);
        replay(repository);
        assertThat(service.removeAllWidgetRatings(USER_ID), is(EXPECTED_COUNT));
        verify(repository);
    }
}
