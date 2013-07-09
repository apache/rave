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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.JpaRegion;
import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaRegionWidgetConverterTest {

    @Autowired
    private JpaRegionWidgetConverter regionWidgetConverter;

    @Test
    public void noConversion() {
        RegionWidget rw = new JpaRegionWidget();
        assertThat(regionWidgetConverter.convert(rw), is(sameInstance(rw)));
    }

    @Test
    public void nullConversion() {
        RegionWidget template = null;
        assertThat(regionWidgetConverter.convert(template), is(nullValue()));
    }


    @Test
    public void newRegion() {
        RegionWidget rw = new RegionWidgetImpl("9");
        rw.setLocked(false);
        rw.setCollapsed(false);
        rw.setHideChrome(true);
        rw.setRenderOrder(9);
        rw.setPreferences(new ArrayList<RegionWidgetPreference>());
        rw.setRegion(new RegionImpl("1"));
        rw.setRenderPosition("last");
        rw.setWidgetId("1");

        JpaRegionWidget converted = regionWidgetConverter.convert(rw);
        assertThat(converted, is(not(sameInstance(rw))));
        assertThat(converted, is(instanceOf(JpaRegionWidget.class)));
        assertThat(converted.getPreferences(), is(equalTo(rw.getPreferences())));
        assertThat(converted.getEntityId().toString(), is(equalTo(rw.getId())));
        assertThat(converted.getId(), is(equalTo(rw.getId())));
        assertThat(converted.isCollapsed(), is(equalTo(rw.isCollapsed())));
        assertThat(converted.getRenderOrder(), is(equalTo(rw.getRenderOrder())));
        assertThat(converted.isLocked(), is(equalTo(rw.isLocked())));
        assertThat(converted.isHideChrome(), is(equalTo(rw.isHideChrome())));
        assertThat(converted.getRegion(), is(instanceOf(JpaRegion.class)));
        assertThat(converted.getRenderPosition(), is(equalTo(rw.getRenderPosition())));
        assertThat(converted.getWidgetId(), is(instanceOf(String.class)));
    }
}
