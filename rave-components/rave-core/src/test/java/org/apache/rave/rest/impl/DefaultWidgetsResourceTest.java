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
package org.apache.rave.rest.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.rest.WidgetsResource;
import org.apache.rave.rest.exception.ResourceNotFoundException;
import org.apache.rave.rest.model.SearchResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultWidgetsResourceTest {

    private WidgetService service;
    private WidgetsResource resource;
    private Widget w1, w2, w3;

    @Before
    public void setup() {
        service = createMock(WidgetService.class);
        resource = new DefaultWidgetsResource();
        ((DefaultWidgetsResource)resource).setWidgetService(service);
        w1 = new WidgetImpl("1", "http://example.com/1");
        w1.setTitle("title");
        w1.setTitleUrl("http://example.com/title");
        w1.setThumbnailUrl("http://example.com/thumbnail");
        w1.setScreenshotUrl("http://example.com/screenshot");
        w1.setType("W3C");
        w1.setAuthor("author");
        w1.setAuthorEmail("authorEmail");
        w1.setDescription("Desscription");
        w1.setWidgetStatus(WidgetStatus.PUBLISHED);
        w1.setDisableRendering(true);
        w1.setDisableRenderingMessage("");
        w1.setFeatured(true);
        Map<String, Object> properties = Maps.newHashMap();
        properties.put("foo", "bar");
        w1.setProperties(properties);
        w2 = new WidgetImpl("2", "http://example.com/2");
        w3 = new WidgetImpl("1", "http://example.com/2");
    }

    @Test
    public void getAll() {
        List<Widget> widgets = Lists.newArrayList(w1, w2);
        SearchResult<Widget> result = new SearchResult<Widget>(widgets, widgets.size());
        expect(service.getAll()).andReturn(result);
        replay(service);

        SearchResult<org.apache.rave.rest.model.Widget> resourceResult = resource.getWidgets();
        assertThat(resourceResult.getTotalResults(), is(equalTo(widgets.size())));
        assertThat(resourceResult.getResultSet().get(0).getId(), is(equalTo(w1.getId())));
        assertThat(resourceResult.getResultSet().get(1).getId(), is(equalTo(w2.getId())));
    }
    
    @Test
    public void getById_exists() {
        expect(service.getWidget(w1.getId())).andReturn(w1);
        replay(service);

        org.apache.rave.rest.model.Widget widget = resource.getWidget(w1.getId());
        validate(widget, w1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getById_notExists() {
        expect(service.getWidget(w1.getId())).andReturn(null);
        replay(service);

        org.apache.rave.rest.model.Widget widget = resource.getWidget(w1.getId());
    }

    @Test
    public void update() {
        expect(service.getWidget(w1.getId())).andReturn(w3).times(1);
        service.updateWidget(w1);
        expectLastCall();
        expect(service.getWidget(w1.getId())).andReturn(w1).times(1);
        replay(service);

        org.apache.rave.rest.model.Widget widget = resource.updateWidget(w1.getId(), new org.apache.rave.rest.model.Widget(w1));
        validate(widget, w1);
    }

    @Test
    public void create() {
        expect(service.registerNewWidget(w1)).andReturn(w1).times(1);
        replay(service);

        org.apache.rave.rest.model.Widget widget = resource.createWidget(new org.apache.rave.rest.model.Widget(w1));
        validate(widget, w1);
    }

    private void validate(org.apache.rave.rest.model.Widget widget, Widget source) {
        assertThat(widget.getId(), is(equalTo(source.getId())));
        assertThat(widget.getUrl(), is(equalTo(source.getUrl())));
        assertThat(widget.getTitle(), is(equalTo(source.getTitle())));
        assertThat(widget.getTitleUrl(), is(equalTo(source.getTitleUrl())));
        assertThat(widget.getThumbnailUrl(), is(equalTo(source.getThumbnailUrl())));
        assertThat(widget.getScreenshotUrl(), is(equalTo(source.getScreenshotUrl())));
        assertThat(widget.getType(), is(equalTo(source.getType())));
        assertThat(widget.getAuthor(), is(equalTo(source.getAuthor())));
        assertThat(widget.getAuthorEmail(), is(equalTo(source.getAuthorEmail())));
        assertThat(widget.getDescription(), is(equalTo(source.getDescription())));
        assertThat(widget.getStatus(), is(equalTo(source.getWidgetStatus())));
        assertThat(widget.isDisable(), is(equalTo(source.isDisableRendering())));
        assertThat(widget.getDisabledMessage(), is(equalTo(source.getDisableRenderingMessage())));
        assertThat(widget.isFeatured(), is(equalTo(source.isFeatured())));
        assertThat((String)widget.getProperties().get("foo"), is(equalTo("bar")));
    }
}
