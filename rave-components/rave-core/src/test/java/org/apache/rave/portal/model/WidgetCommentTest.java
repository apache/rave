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

import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author ISIS
 */
public class WidgetCommentTest {
    
    private WidgetComment widgetComment;
    
    private static final Long VALID_ENTITY_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_WIDGET_ID = 3L;
    private static final String VALID_COMMENT = "comment";
    private static final Date VALID_CREATED_DATE = new Date();
    private static final Date VALID_LAST_MODIFIED_DATE = new Date();
    
    @Before
    public void setUp() {
        widgetComment = new WidgetComment();
        widgetComment.setEntityId(VALID_ENTITY_ID);
        widgetComment.setWidgetId(VALID_WIDGET_ID);
        widgetComment.setUser(new User(1L, "John.Doe"));
        widgetComment.setText(VALID_COMMENT);
        widgetComment.setCreatedDate(VALID_CREATED_DATE);
        widgetComment.setLastModifiedDate(VALID_LAST_MODIFIED_DATE);
    }
    
    @Test
    public void getters() {
        assertEquals(VALID_ENTITY_ID, widgetComment.getEntityId());
        assertEquals(VALID_WIDGET_ID, widgetComment.getWidgetId());
        assertEquals(VALID_USER_ID, widgetComment.getUser().getEntityId());
        assertEquals(VALID_CREATED_DATE, widgetComment.getCreatedDate());
        assertEquals(VALID_LAST_MODIFIED_DATE, widgetComment.getLastModifiedDate());
    }
    
    @Test
    public void utility() {
        String toString = widgetComment.toString();
        assertNotNull(toString);
        assertTrue(toString instanceof String);
        
        int hashCode = widgetComment.hashCode();
        assertEquals(hashCode, widgetComment.hashCode());
        
        assertFalse(widgetComment.equals(null));
        assertFalse(widgetComment.equals(new WidgetComment()));
        assertFalse(widgetComment.equals(new String()));
        assertFalse(new WidgetComment().equals(widgetComment));
        WidgetComment testWidgetComment = new WidgetComment();
        testWidgetComment.setEntityId(VALID_ENTITY_ID);
        assertTrue(widgetComment.equals(testWidgetComment));
    }
}
