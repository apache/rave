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

package org.apache.rave.provider.opensocial.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OpenSocialWidgetRendererTest {
    private OpenSocialService openSocialService;
    private SecurityTokenService securityTokenService;
    private ScriptManager scriptManager;
    private Renderer<RegionWidgetWrapper> renderer;

    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_METADATA = "metadata";
    private static final String VALID_SECURITY_TOKEN = "securityToken";
    private static final boolean VALID_COLLAPSED = true;
    private static final boolean VALID_LOCKED = false;
    private static final boolean VALID_HIDE_CHROME = true;
    private RenderContext renderContext;

    @Before
    public void setup() {
        renderContext = new RenderContext();
        scriptManager = createStrictMock(ScriptManager.class);
        openSocialService = createNiceMock(OpenSocialService.class);
        securityTokenService = createNiceMock(SecurityTokenService.class);
        renderer = new OpenSocialWidgetWrapperRenderer(openSocialService, securityTokenService, scriptManager);
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() throws JSONException {
        final String WIDGET_ID = "999";
        final String REGION_WIDGET_ID = "12345";
        final String REGION_ID = "8675309";
        final String VALID_SUBPAGE_ID = "778899";
        final String VALID_SUBPAGE_NAME = "My Activity";
        final boolean VALID_IS_DEFAULT_SUBPAGE = true;

        expect(openSocialService.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(openSocialService);

        Page page = new PageImpl();
        page.setSubPages(new ArrayList<Page>());
        Page subPage = new PageImpl();
        subPage.setId(VALID_SUBPAGE_ID);
        subPage.setName(VALID_SUBPAGE_NAME);
        subPage.setParentPage(page);
        subPage.setPageType(PageType.SUB_PAGE.toString());
        page.getSubPages().add(subPage);

        WidgetImpl w = new WidgetImpl();
        w.setId(WIDGET_ID);
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl(VALID_GADGET_URL);

        Region region = new RegionImpl(REGION_ID);
        region.setPage(subPage);
        RegionWidget rw = new RegionWidgetImpl(REGION_WIDGET_ID);
        rw.setCollapsed(VALID_COLLAPSED);
        rw.setWidgetId(w.getId());
        rw.setRegion(region);
        rw.setHideChrome(VALID_HIDE_CHROME);
        rw.setLocked(VALID_LOCKED);
        rw.setPreferences(Arrays.asList((RegionWidgetPreference) new RegionWidgetPreferenceImpl("1", "color", "blue"),
                new RegionWidgetPreferenceImpl("1", "speed", "fast"),
                new RegionWidgetPreferenceImpl("1", null, null)));

        final String markup =
                "<script>require(['rave'], function(rave){" +
                        "rave.registerWidget('" + REGION_ID + "', {type: 'OpenSocial'," +
                        " regionWidgetId: '" + REGION_WIDGET_ID + "'," +
                        " widgetUrl: '" + VALID_GADGET_URL + "', " +
                        " securityToken: '" + VALID_SECURITY_TOKEN + "', " +
                        " metadata: " + VALID_METADATA + "," +
                        " userPrefs: {\"speed\":\"fast\",\"color\":\"blue\"}," +
                        " collapsed: " + VALID_COLLAPSED + ", " +
                        " widgetId: '" + WIDGET_ID + "'," +
                        " locked: " + VALID_LOCKED + "," +
                        " hideChrome: " + VALID_HIDE_CHROME + "," +
                        " subPage: {id: '" + VALID_SUBPAGE_ID + "', name: '" + VALID_SUBPAGE_NAME + "', isDefault: " + VALID_IS_DEFAULT_SUBPAGE + "}" +
                        "})" +
                        "});</script>";

        expect(securityTokenService.getEncryptedSecurityToken(rw, w)).andReturn(VALID_SECURITY_TOKEN);
        replay(securityTokenService);

        String key = OpenSocialWidgetWrapperRenderer.REGISTER_WIDGET_KEY + "-" + rw.getId();
        scriptManager.registerScriptBlock(key, markup, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, renderContext);
        expectLastCall();
        replay(scriptManager);

        RegionWidgetWrapper wrapper = new RegionWidgetWrapper(w, rw);

        String result = renderer.render(wrapper, renderContext);

        assertThat(result, is(equalTo("<!-- RegionWidget '" + rw.getId() + "' placeholder -->")));
        verify(scriptManager);
    }

    @Test
    public void render_null() {
        final String WIDGET_ID = "999";
        final String REGION_WIDGET_ID = "12345";
        final String REGION_ID = "8675309";

        Page page = new PageImpl();
        page.setPageType(PageType.USER.toString());

        WidgetImpl w = new WidgetImpl();
        w.setType(Constants.WIDGET_TYPE);

        Region region = new RegionImpl(REGION_ID);
        region.setPage(page);
        RegionWidget rw = new RegionWidgetImpl();
        rw.setWidgetId(w.getId());
        rw.setRegion(region);

        final String markup =
                "<script>require(['rave'], function(rave){" +
                        "rave.registerWidget('8675309', {type: 'OpenSocial'," +
                        " regionWidgetId: 'null'," +
                        " widgetUrl: 'null', " +
                        " securityToken: 'null', " +
                        " metadata: null," +
                        " userPrefs: {}," +
                        " collapsed: false, " +
                        " widgetId: 'null'," +
                        " locked: false," +
                        " hideChrome: false," +
                        " subPage: {id: null, name: '', isDefault: false}" +
                        "})" +
                        "});</script>";

        scriptManager.registerScriptBlock(OpenSocialWidgetWrapperRenderer.REGISTER_WIDGET_KEY, markup, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, null);
        expectLastCall();
        replay(scriptManager);

        RegionWidgetWrapper wrapper = new RegionWidgetWrapper(w, rw);

        String result = renderer.render(wrapper, null);
        verify(scriptManager);
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        WidgetImpl w = new WidgetImpl("1");
        w.setType("NONE");
        w.setUrl("http://www.example.com/gadget.xml");

        RegionWidget rw = new RegionWidgetImpl("1");
        rw.setWidgetId(w.getId());

        RegionWidgetWrapper wrapper = new RegionWidgetWrapper(w, rw);

        renderer.render(wrapper, null);
    }
}
