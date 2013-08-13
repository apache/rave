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
import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.w3c.Constants;
import org.apache.rave.provider.w3c.service.impl.W3CWidget;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    private Renderer<RegionWidgetWrapper> renderer;
    private WidgetProviderService wookieService;
    private UserService userService;
    private RenderContext renderContext;
    private ScriptManager scriptManager;

    @Before
    public void setup() {

        renderContext = new RenderContext();
        wookieService = createNiceMock(WidgetProviderService.class);
        userService = createNiceMock(UserService.class);
        scriptManager = createNiceMock(ScriptManager.class);
        renderer = new W3cWidgetWrapperRenderer(wookieService, userService, scriptManager);
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() {
        final String REGION_ID = "222";
        final String REGION_WIDGET_ID = "444";
        final String VALID_SUBPAGE_ID = "778899";
        final String VALID_SUBPAGE_NAME = "My Activity";
        final boolean VALID_IS_DEFAULT_SUBPAGE = true;

        User user = new UserImpl("9999", "testUser");
        expect(userService.getAuthenticatedUser()).andReturn(user);
        replay(userService);

        Page page = new PageImpl();
        page.setSubPages(new ArrayList<Page>());
        Page subPage = new PageImpl();
        subPage.setId(VALID_SUBPAGE_ID);
        subPage.setName(VALID_SUBPAGE_NAME);
        subPage.setParentPage(page);
        subPage.setPageType(PageType.SUB_PAGE.toString());
        page.getSubPages().add(subPage);

        W3CWidget w = new W3CWidget("1");
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl("http://example.com/widgets/1");
        Region region = new RegionImpl(REGION_ID);
        region.setPage(subPage);
        RegionWidget rw = new RegionWidgetImpl(REGION_WIDGET_ID);
        rw.setWidgetId(w.getId());
        rw.setRegion(region);

        W3CWidget wookieWidget = new W3CWidget();
        wookieWidget.setUrl(VALID_WIDGET_INSTANCE_URL);

        RegionWidgetWrapper wrapper = new RegionWidgetWrapper(w, rw);

        expect(wookieService.getWidget(eq(user), eq(rw.getId()), isA(Widget.class))).andReturn(wookieWidget);
        replay(wookieService);

        String placeholder = renderer.render(wrapper, renderContext);
        assertEquals("Script block for widget is incorrect", "<!-- RegionWidget " + REGION_WIDGET_ID + " placeholder -->", placeholder);
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        Widget w = new WidgetImpl("1");
        w.setType("NONE");
        w.setUrl(VALID_WIDGET_URL);
        RegionWidget rw = new RegionWidgetImpl("1");
        rw.setWidgetId(w.getId());

        RegionWidgetWrapper wrapper = new RegionWidgetWrapper(w, rw);

        RenderContext renderContext = createNiceMock(RenderContext.class);
        renderer.render(wrapper, renderContext);
    }
}
