/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@Ignore
public class WidgetTagTest {
    
    private JpaWidgetTag widgetTag;
    
    private static final Long VALID_ENTITY_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_WIDGET_ID = 3L;
    private static final Date VALID_CREATED_DATE = new Date();
    
    @Before
    public void setUp() {
        widgetTag = new JpaWidgetTag(VALID_WIDGET_ID);
        widgetTag.setEntityId(VALID_ENTITY_ID);
        widgetTag.setUserEntityId(1L);
        widgetTag.setCreatedDate(VALID_CREATED_DATE);
        widgetTag.setTagEntityId(1L);
    }
    
    @Test
    public void getters() {
        assertEquals(VALID_ENTITY_ID, widgetTag.getEntityId());
        assertEquals(VALID_WIDGET_ID, widgetTag.getWidgetId());
        assertEquals(VALID_USER_ID, widgetTag.getUserId());
        assertEquals(VALID_CREATED_DATE, widgetTag.getCreatedDate());
    }
    
}
