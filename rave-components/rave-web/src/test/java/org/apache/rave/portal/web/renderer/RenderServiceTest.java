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

package org.apache.rave.portal.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.web.renderer.impl.DefaultRenderService;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RenderServiceTest {

    private static final String SUPPORTED_TYPE_1 = "FOO";
    private static final String SUPPORTED_TYPE_2 = "BAR";
    private static final String RENDERED_TYPE_1 = "FOO_RENDERED";
    private static final String RENDERED_TYPE_2 = "BAR_RENDERED";

    private RenderService service;
    private List<RegionWidgetRenderer> widgetRenderers;
    private RegionWidgetRenderer widgetRenderer2;
    private RegionWidgetRenderer widgetRenderer1;
    private RenderContext context;

    @Before
    public void setup() {
        widgetRenderers = new ArrayList<RegionWidgetRenderer>();
        widgetRenderer2 = createNiceMock(RegionWidgetRenderer.class);
        widgetRenderer1 = createNiceMock(RegionWidgetRenderer.class);

        expect(widgetRenderer1.getSupportedContext()).andReturn(SUPPORTED_TYPE_1);
        expect(widgetRenderer2.getSupportedContext()).andReturn(SUPPORTED_TYPE_2);
    }

    @Test
    public void getSupportedWidgetTypes_singleType() {
        replay(widgetRenderer1);
        constructFooRenderService();

        Collection<String> types = service.getSupportedWidgetTypes();
        assertThat(types, is(notNullValue()));
        assertThat(types.size(), is(equalTo(1)));
        assertThat(types.iterator().next(), is(equalTo(SUPPORTED_TYPE_1)));
    }

    @Test
    public void getSupportedWidgetTypes_multipleTypes() {
        replayMocks();

        constructFooBarRenderService();
        Collection<String> types = service.getSupportedWidgetTypes();
        assertThat(types, is(notNullValue()));
        assertThat(types.size(), is(equalTo(2)));
        assertThat(types.contains(SUPPORTED_TYPE_1), is(true));
        assertThat(types.contains(SUPPORTED_TYPE_2), is(true));
    }

    @Test
    public void render_supported_foo() {
        Widget w = new Widget();
        w.setType(SUPPORTED_TYPE_1);

        RegionWidget rw = new RegionWidget();
        rw.setWidget(w);

        expect(widgetRenderer1.render(rw)).andReturn(RENDERED_TYPE_1);
        replayMocks();

        constructFooBarRenderService();
        assertThat(service.render(rw, context), is(equalTo(RENDERED_TYPE_1)));
    }

    @Test
    public void render_supported_bar() {
        Widget w = new Widget();
        w.setType(SUPPORTED_TYPE_2);
        RegionWidget rw = new RegionWidget();
        rw.setWidget(w);

        expect(widgetRenderer2.render(rw)).andReturn(RENDERED_TYPE_2);
        replayMocks();

        constructFooBarRenderService();
        assertThat(service.render(rw, context), is(equalTo(RENDERED_TYPE_2)));
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        Widget w = new Widget();
        w.setType("NONE");

        RegionWidget rw = new RegionWidget();
        rw.setWidget(w);

        replayMocks();

        constructFooBarRenderService();
        service.render(rw, context);
    }


    private void constructFooBarRenderService() {
        widgetRenderers.add(widgetRenderer1);
        widgetRenderers.add(widgetRenderer2);
        service = new DefaultRenderService(widgetRenderers);
    }

    private void constructFooRenderService() {
        widgetRenderers.add(widgetRenderer1);
        service = new DefaultRenderService(widgetRenderers);
    }

    private void replayMocks() {
        replay(widgetRenderer1);
        replay(widgetRenderer2);
    }
}
