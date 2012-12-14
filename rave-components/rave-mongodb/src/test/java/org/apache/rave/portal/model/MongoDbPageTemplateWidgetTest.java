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
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:28 PM
 */
public class MongoDbPageTemplateWidgetTest {
    private WidgetRepository widgetRepository;
    private MongoDbPageTemplateWidget templateWidget;

    @Before
    public void setup() {
        widgetRepository = createMock(WidgetRepository.class);
        templateWidget = new MongoDbPageTemplateWidget();
        templateWidget.setWidgetRepository(widgetRepository);
        templateWidget.setWidgetId((long) 123);
    }

    @Test
    public void getWidget_Null() {
        Widget widget = new WidgetImpl();
        expect(widgetRepository.get((long) 123)).andReturn(widget);
        replay(widgetRepository);

        assertThat(templateWidget.getWidget(), is(sameInstance(widget)));
    }

    @Test
    public void getWudget_Not_Null(){
        Widget widget = new WidgetImpl();
        templateWidget.setWidget(widget);
        assertThat(widget, is(sameInstance(templateWidget.getWidget())));
    }
}
