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


import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.provider.opensocial.repository.impl.ShindigGadgetMetadataRepository;
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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class RenderServiceIntegrationTest {

    @Autowired
    private RenderService service;

    @Autowired
    private ShindigGadgetMetadataRepository metadataRepository;

    private RestOperations restOperations;

    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\"}}}]";

    private static final Long VALID_USER_ID = 1234L;
    private static final String VALID_USER_NAME = "jdoe";

    @Before
    public void setup() {
        restOperations = createNiceMock(RestOperations.class);
        expect(restOperations.postForObject(anyObject(String.class), anyObject(String.class), anyObject(Class.class)))
                .andReturn(VALID_METADATA);
        replay(restOperations);

        //Replace the real restOperations instance with a mock -- otherwise the call for gadget metadata would fail since
        //we don't have a shindig server available to hit.
        ReflectionTestUtils.setField(metadataRepository, "restOperations", restOperations);

        //Setup a mock authenticated user
        final User authUser = new User(VALID_USER_ID, VALID_USER_NAME);
        AbstractAuthenticationToken auth = createNiceMock(AbstractAuthenticationToken.class);
        expect(auth.getPrincipal()).andReturn(authUser).anyTimes();
        replay(auth);

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

        Widget w = new Widget();
        w.setType("OpenSocial");
        w.setEntityId(1L);
        w.setTitle("Gadget Title");
        w.setUrl("http://www.example.com/gadget.xml");

        RegionWidget rw = new RegionWidget(1L, w, region);
        region.setRegionWidgets(Arrays.asList(rw));

        String rendered = service.render(rw);
        assertThat(rendered, is(notNullValue()));
        assertThat(rendered.contains("widgets.push({"), is(true));
    }
}
