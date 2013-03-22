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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.WidgetTag;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetTagConverterTest {
  
    @Autowired 
    JpaWidgetTagConverter jpaWidgetTagConverter;
    
    @Test
    public void convert_valid_widgetTaq(){
        JpaWidget widget = new JpaWidget(1L, "");
        WidgetTag widgetTag = new WidgetTagImpl(new JpaUser(1L).getId(), new Date(), new JpaTag(1L, "news").getId());
        JpaWidgetTag jpaWidgetTag = jpaWidgetTagConverter.convert(widgetTag, widget.getId());
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetTag.getCreatedDate(), jpaWidgetTag.getCreatedDate());
        assertEquals(widgetTag.getTagId(), jpaWidgetTag.getTagId());
        assertEquals(widgetTag.getUserId(), jpaWidgetTag.getUserId());
        assertEquals(widget.getId(), jpaWidgetTag.getWidgetId());
    }

    @Test
    public void convert_valid_jpaWidgetTaq(){
        JpaWidgetTag widgetTag = new JpaWidgetTag(3);
        widgetTag.setCreatedDate(new Date());
        widgetTag.setUserEntityId(new JpaUser(1L).getEntityId());
        widgetTag.setTagEntityId(new JpaTag(1L, "news").getEntityId());
        JpaWidgetTag jpaWidgetTag = jpaWidgetTagConverter.convert(widgetTag, "3");
        assertNotNull(jpaWidgetTag);
        assertSame(widgetTag, jpaWidgetTag);
    }
}
