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

package org.apache.rave.portal.service;

import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.apache.rave.portal.service.impl.DefaultWidgetRatingService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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
}
