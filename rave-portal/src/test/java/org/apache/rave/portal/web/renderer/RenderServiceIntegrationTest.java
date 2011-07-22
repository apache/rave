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


import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.provider.opensocial.repository.impl.ShindigGadgetMetadataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestOperations;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class RenderServiceIntegrationTest {

    @Autowired
    private RenderService service;

    @Autowired
    private ShindigGadgetMetadataRepository metadataRepository;

    private RestOperations restOperations;

    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\"}}}]";

    @Before
    public void setup() {
        restOperations = createNiceMock(RestOperations.class);
        expect(restOperations.postForObject(anyObject(String.class), anyObject(String.class), anyObject(Class.class)))
                .andReturn(VALID_METADATA);
        replay(restOperations);

        //Replace the real restOperations instance with a mock -- otherwise the call for gadget metadata would fail since
        //we don't have a shindig server available to hit.
        ReflectionTestUtils.setField(metadataRepository, "restOperations", restOperations);
    }

    @Test
    public void supportedWidgets() {
        assertThat(service.getSupportedWidgetTypes().size(), is(equalTo(2)));
        assertThat(service.getSupportedWidgetTypes().contains("OpenSocial"), is(true));
        assertThat(service.getSupportedWidgetTypes().contains("W3C"), is(true));
    }

    @Test
    public void renderOpenSocial() {
        Widget w = new Widget();
        w.setType("OpenSocial");
        w.setId(1L);
        w.setTitle("Gadget Title");
        w.setUrl("http://www.example.com/gadget.xml");

        RegionWidget rw = new RegionWidget();
        rw.setId(2L);
        rw.setWidget(w);

        String rendered = service.render(rw);
        assertThat(rendered, is(notNullValue()));
        assertThat(rendered.contains("widgets.push({"), is(true));
    }
}
