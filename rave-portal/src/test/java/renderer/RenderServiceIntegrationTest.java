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

package renderer;


import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.web.renderer.RenderService;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.opensocial.repository.impl.ShindigGadgetMetadataRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class RenderServiceIntegrationTest {

    @Autowired
    private RenderService service;

    @Autowired
    private ScriptManager scriptManager;

    @Autowired
    private ShindigGadgetMetadataRepository metadataRepository;

    private RestOperations restOperations;

    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\"}}}]";

    private static final Long VALID_USER_ID = 1234L;
    private static final String VALID_USER_NAME = "jdoe";

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        restOperations = EasyMock.createNiceMock(RestOperations.class);
        EasyMock.expect(restOperations.postForObject(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class), EasyMock.anyObject(Class.class)))
                .andReturn(VALID_METADATA);
        EasyMock.replay(restOperations);

        //Replace the real restOperations instance with a mock -- otherwise the call for gadget metadata would fail since
        //we don't have a shindig server available to hit.
        ReflectionTestUtils.setField(metadataRepository, "restOperations", restOperations);

        //Setup a mock authenticated user
        final User authUser = new User(VALID_USER_ID, VALID_USER_NAME);
        AbstractAuthenticationToken auth = EasyMock.createNiceMock(AbstractAuthenticationToken.class);
        EasyMock.expect(auth.getPrincipal()).andReturn(authUser).anyTimes();
        EasyMock.replay(auth);

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void supportedWidgets() {
        assertThat(service.getSupportedWidgetTypes().size(), is(equalTo(1)));
        assertThat(service.getSupportedWidgetTypes().contains("OpenSocial"), is(true));
    }

    @Test
    public void renderOpenSocial() {
        Page page = new Page(1L, new User(VALID_USER_ID, VALID_USER_NAME));
        Region region = new Region(1L, page, 1);
        page.setRegions(Arrays.asList(region));

        WidgetImpl w = new WidgetImpl();
        w.setType("OpenSocial");
        w.setId(1L);
        w.setTitle("Gadget Title");
        w.setUrl("http://www.example.com/gadget.xml");

        RegionWidget rw = new RegionWidget(1L, w, region);
        region.setRegionWidgets(Arrays.asList(rw));

        RenderContext context = new RenderContext();
        context.setProperties(new HashMap());
        String rendered = service.render(rw, context);
        assertThat(rendered, is(notNullValue()));
        assertThat(rendered.contains("<!--"), is(true));
        assertThat(scriptManager.getScriptBlocks(ScriptLocation.AFTER_RAVE, context).size(), is(equalTo(1)));
    }
}
