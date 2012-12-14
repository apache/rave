/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:38 PM
 */
public class MongoDbRegionWidgetTest {
    private MongoDbRegionWidget regionWidget;
    private WidgetRepository widgetRepository;

    @Before
    public void setup(){
        regionWidget = new MongoDbRegionWidget();
        widgetRepository = createMock(WidgetRepository.class);
        regionWidget.setWidgetRepository(widgetRepository);
    }

    @Test
    public void getWidget_Widget_Null(){
        regionWidget.setWidgetId((long)123);
        Widget widget = new WidgetImpl();
        expect(widgetRepository.get((long)123)).andReturn(widget);
        replay(widgetRepository);

        assertThat(widget, is(sameInstance(regionWidget.getWidget())));

    }

    @Test
    public void getWidget_Widget_Set(){
        Widget widget = new WidgetImpl();
        regionWidget.setWidget(widget);
        assertThat(widget, is(sameInstance(regionWidget.getWidget())));
    }

    @Test
    public void equals_Same(){
        assertTrue(regionWidget.equals(regionWidget));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(regionWidget.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        regionWidget.setId((long)123);
        RegionWidget r = new MongoDbRegionWidget();
        assertFalse(regionWidget.equals(r));
        assertFalse(r.equals(regionWidget));

    }

    @Test
    public void equals_Valid(){
        regionWidget.setId((long)123);
        RegionWidget r = new MongoDbRegionWidget();
        r.setId((long)123);
        assertTrue(regionWidget.equals(r));
    }

    @Test
    public void equals_Same_Null(){
        regionWidget.setId(null);
        RegionWidget r = new MongoDbRegionWidget();
        r.setId(null);
        assertTrue(regionWidget.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        regionWidget.setId((long)123);
        assertNotNull(regionWidget.hashCode());
    }

    @Test
    public void hashCode_Null(){
        regionWidget.setId(null);
        assertTrue(regionWidget.hashCode()==0);
    }
}
