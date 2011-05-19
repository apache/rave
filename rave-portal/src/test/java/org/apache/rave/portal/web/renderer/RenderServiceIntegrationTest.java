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
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class RenderServiceIntegrationTest {

    @Autowired
    private RenderService service;

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
        w.setUrl("http://example.com/gadgets/1");

        RegionWidget rw = new RegionWidget();
        rw.setId(2L);
        rw.setWidget(w);

        String rendered = service.render(rw);
        assertThat(rendered, is(notNullValue()));
        assertThat(rendered.contains("widgets.push({"), is(true));
    }
}
