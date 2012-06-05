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

package org.apache.rave.provider.w3c.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.conversion.JpaConverter;
import org.apache.rave.portal.model.conversion.JpaWidgetConverter;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.w3c.Constants;
import org.apache.rave.provider.w3c.service.impl.W3CWidget;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/*
*/
public class W3cWidgetRendererTest {

    private static final String VALID_WIDGET_URL = "http://example.com/widgets/1";
    private static final String VALID_WIDGET_INSTANCE_URL = "http://example.com/widgetinstances/1";
    private Renderer<RegionWidget> renderer;
    private WidgetProviderService wookieService;
    private UserService userService;
    private RenderContext renderContext;
    private ScriptManager scriptManager;

    @Before
    public void setup() {
        //TODO:REMOVE WHEN REGION_WIDGET REFACTOR IS COMPLETE
        JpaWidgetConverter converter = new JpaWidgetConverter();
        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        converters.add(converter);
        new JpaConverter(converters);

        renderContext = new RenderContext();
        wookieService = createNiceMock(WidgetProviderService.class);
        userService = createNiceMock(UserService.class);
        scriptManager = createNiceMock(ScriptManager.class);
        renderer = new W3cWidgetRenderer(wookieService, userService, scriptManager);  
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() {
        User user = new User(9999L, "testUser");
        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService);
        
        W3CWidget w = new W3CWidget();
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl("http://example.com/widgets/1");
        Region region = new RegionImpl(1L);
        RegionWidget rw = new RegionWidgetImpl();
        rw.setId(1L);
        rw.setWidget(w);
        rw.setRegion(region);

        W3CWidget wookieWidget = new W3CWidget();
        wookieWidget.setUrl(VALID_WIDGET_INSTANCE_URL);

        expect(wookieService.getWidget(eq(user), eq(rw.getId().toString()), isA(Widget.class))).andReturn(wookieWidget);
        replay(wookieService);

        String placeholder = renderer.render(rw, renderContext);
        assertEquals("Script block for widget is incorrect", "<!-- RegionWidget 1 placeholder -->", placeholder);
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        Widget w = new WidgetImpl();
        w.setType("NONE");
        w.setUrl(VALID_WIDGET_URL);
        RegionWidget rw = new RegionWidgetImpl();
        rw.setId(1L);
        rw.setWidget(w);

        RenderContext renderContext = createNiceMock(RenderContext.class);
        renderer.render(rw, renderContext);
    }
}
