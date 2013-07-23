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

package org.apache.rave.portal.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
Test for {@link WidgetRatingTest}
 */
public class WidgetRatingTest {
    JpaWidgetRating widgetRating;
    
    Long id;
    String widgetId;
    String userId;
    Integer score;
    
    @Before
    public void setUp() throws Exception {
        id = 1L;
        widgetId = "1";
        userId = "1";
        score = 1;
        
        widgetRating = new JpaWidgetRating();
        widgetRating.setEntityId(id);
        widgetRating.setScore(score);
        widgetRating.setUserId(userId);
        widgetRating.setWidgetId(widgetId);
    }
    
    @Test
    public void testAccessorMethods() {
        assertEquals(id, widgetRating.getEntityId());
        assertEquals(score, widgetRating.getScore());
        assertEquals(userId, widgetRating.getUserId());
        assertEquals(widgetId, widgetRating.getWidgetId());
    }
    
    @Test
    public void equality() {
        JpaWidgetRating tester = new JpaWidgetRating();
        tester.setEntityId(1L);
        
        assertEquals(widgetRating, tester);
        tester.setEntityId(2L);
        assertFalse(widgetRating.equals(tester));
        assertFalse(widgetRating.equals(null));
        assertFalse(widgetRating.equals(new JpaWidgetRating()));
        assertFalse(widgetRating.equals(new String()));
        
        widgetRating.toString();
        widgetRating.hashCode();
    }
    
    @After
    public void tearDown() throws Exception {
        widgetRating = null;
    }
}
