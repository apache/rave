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
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OpenSocialWidgetRendererTest {
    private OpenSocialService openSocialService;
    private SecurityTokenService securityTokenService;
    private ScriptManager scriptManager;
    private Renderer<RegionWidget> renderer;

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
        renderer = new OpenSocialWidgetRenderer(openSocialService, securityTokenService, scriptManager);
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() throws JSONException {
        expect(openSocialService.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(openSocialService);

        WidgetImpl w = new WidgetImpl();
        w.setId(1L);
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl(VALID_GADGET_URL);
        Region region = new RegionImpl(1L);
        RegionWidget rw = new RegionWidgetImpl();
        rw.setId(1L);
        rw.setCollapsed(VALID_COLLAPSED);
        rw.setWidget(w);
        rw.setRegion(region);
        rw.setHideChrome(VALID_HIDE_CHROME);
        rw.setLocked(VALID_LOCKED);
        rw.setPreferences(Arrays.asList((RegionWidgetPreference)new RegionWidgetPreferenceImpl( 1L, "color", "blue"),
                                        new RegionWidgetPreferenceImpl(1L, "speed", "fast"),
                                        new RegionWidgetPreferenceImpl( 1L, null, null)));

        final String markup =
            "<script>rave.registerWidget(1, {type: 'OpenSocial'," +
            " regionWidgetId: 1," +
            " widgetUrl: '" + VALID_GADGET_URL +"', " +
            " securityToken: '" + VALID_SECURITY_TOKEN + "', " +
            " metadata: " + VALID_METADATA + "," +
            " userPrefs: {\"speed\":\"fast\",\"color\":\"blue\"}," +
            " collapsed: " + VALID_COLLAPSED + ", " +
            " widgetId: 1," +
            " locked: " + VALID_LOCKED + "," +
            " hideChrome: " + VALID_HIDE_CHROME +
            "});</script>";

        expect(securityTokenService.getEncryptedSecurityToken(rw)).andReturn(VALID_SECURITY_TOKEN);
        replay(securityTokenService);

        String key = OpenSocialWidgetRenderer.REGISTER_WIDGET_KEY+"-"+w.getId();
        scriptManager.registerScriptBlock(key, markup, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, renderContext);
        expectLastCall();
        replay(scriptManager);

        String result = renderer.render(rw, renderContext);

        assertThat(result, is(equalTo("<!-- RegionWidget 1 placeholder -->")));
        verify(scriptManager);
    }

    @Test
    public void render_null() {
        WidgetImpl w = new WidgetImpl();
        w.setType(Constants.WIDGET_TYPE);
        Region region = new RegionImpl(1L);
        RegionWidget rw = new RegionWidgetImpl();
        rw.setWidget(w);
        rw.setRegion(region);

        final String markup =
            "<script>rave.registerWidget(1, {type: 'OpenSocial'," +
            " regionWidgetId: null," +
            " widgetUrl: 'null', " +
            " securityToken: 'null', " +
            " metadata: null," +
            " userPrefs: {}," +
            " collapsed: false, " +
            " widgetId: null," +
            " locked: false, hideChrome: false});</script>";

        scriptManager.registerScriptBlock(OpenSocialWidgetRenderer.REGISTER_WIDGET_KEY, markup, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, null);
        expectLastCall();
        replay(scriptManager);
        String result = renderer.render(rw, null);
        verify(scriptManager);
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        WidgetImpl w = new WidgetImpl();
        w.setType("NONE");
        w.setUrl("http://www.example.com/gadget.xml");
        RegionWidget rw = new RegionWidgetImpl();
        rw.setId(1L);
        rw.setWidget(w);

        renderer.render(rw, null);
    }
}
