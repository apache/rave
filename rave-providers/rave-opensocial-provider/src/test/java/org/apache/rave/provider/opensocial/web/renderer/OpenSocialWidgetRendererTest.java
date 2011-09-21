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
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OpenSocialWidgetRendererTest {
    private OpenSocialService openSocialService;
    private SecurityTokenService securityTokenService;
    private Renderer<RegionWidget> renderer;

    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_METADATA = "metadata";
    private static final String VALID_SECURITY_TOKEN = "securityToken";
    private static final boolean VALID_COLLAPSED = true;

    @Before
    public void setup() {
        openSocialService = createNiceMock(OpenSocialService.class);
        securityTokenService = createNiceMock(SecurityTokenService.class);
        renderer = new OpenSocialWidgetRenderer(openSocialService, securityTokenService);
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() throws JSONException {
        expect(openSocialService.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(openSocialService);

        Widget w = new Widget();
        w.setEntityId(1L);
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl(VALID_GADGET_URL);
        RegionWidget rw = new RegionWidget();
        rw.setEntityId(1L);
        rw.setCollapsed(VALID_COLLAPSED);
        rw.setWidget(w);
        rw.setPreferences(Arrays.asList(new RegionWidgetPreference(1L, 1L, "color", "blue"),
                                        new RegionWidgetPreference(2L, 1L, "speed", "fast"),
                                        new RegionWidgetPreference(3L, 1L, null, null)));

        expect(securityTokenService.getEncryptedSecurityToken(rw)).andReturn(VALID_SECURITY_TOKEN);
        replay(securityTokenService);

        String result = renderer.render(rw);

        /*
            result should look like:

            widgets.push({type: 'OpenSocial', regionWidgetId: 1, widgetUrl: 'http://www.example.com/gadget.xml',
                securityToken: 'securityToken',  metadata: metadata, userPrefs: {"speed":"fast","color":"blue"}, collapsed: true});
        */

        JSONObject jsonObject = new JSONObject(
                result.substring("widgets.push(".length(), result.length() - ");".length()));

        assertThat((Integer) jsonObject.get("regionWidgetId"), is(equalTo(1)));
        assertThat((String) jsonObject.get("type"), is(equalTo("OpenSocial")));
        assertThat((String) jsonObject.get("widgetUrl"), is(equalTo("http://www.example.com/gadget.xml")));
        assertThat((String) jsonObject.get("securityToken"), is(equalTo(VALID_SECURITY_TOKEN)));
        assertThat((String) jsonObject.get("metadata"), is(equalTo(VALID_METADATA)));
        assertThat((String) ((JSONObject) jsonObject.get("userPrefs")).get("color"), is(equalTo("blue")));
        assertThat((String) ((JSONObject) jsonObject.get("userPrefs")).get("speed"), is(equalTo("fast")));
        assertThat((Boolean) jsonObject.get("collapsed"), is(equalTo(VALID_COLLAPSED)));
    }

    @Test
    public void render_null() {
        Widget w = new Widget();
        w.setType(Constants.WIDGET_TYPE);
        RegionWidget rw = new RegionWidget();
        rw.setWidget(w);

        String result = renderer.render(rw);
        assertThat(result.matches(".*regionWidgetId[ ]*:[ ]*null,.*"), is(true));
        assertThat(result.matches(".*type[ ]*:[ ]*'OpenSocial',.*"), is(true));
        assertThat(result.matches(".*widgetUrl[ ]*:[ ]*'null',.*"), is(true));
        assertThat(result.matches(".*metadata[ ]*:[ ]*null,.*"), is(true));
        assertThat(result.matches(".*collapsed[ ]*:[ ]*false.*"), is(true));
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        Widget w = new Widget();
        w.setType("NONE");
        w.setUrl("http://www.example.com/gadget.xml");
        RegionWidget rw = new RegionWidget();
        rw.setEntityId(1L);
        rw.setWidget(w);

        renderer.render(rw);
    }
}