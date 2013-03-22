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

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.JpaPage;
import org.apache.rave.portal.model.JpaRegion;
import org.apache.rave.portal.model.impl.RegionImpl;
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
public class JpaRegionConverterTest {

    @Autowired
    private JpaRegionConverter regionConverter;

    @Test
    public void noConversion() {
        Region region = new JpaRegion();
        assertThat(regionConverter.convert(region), is(sameInstance(region)));
    }

    @Test
    public void nullConversion() {
        Region template = null;
        assertThat(regionConverter.convert(template), is(nullValue()));
    }


    @Test
    public void newRegion() {
        Region region = new RegionImpl("9");
        region.setLocked(false);
        region.setPage(new JpaPage());
        region.setRegionWidgets(new ArrayList<RegionWidget>());
        region.setRenderOrder(9);

        JpaRegion converted = regionConverter.convert(region);
        assertThat(converted, is(not(sameInstance(region))));
        assertThat(converted, is(instanceOf(JpaRegion.class)));
        assertThat(converted.getRegionWidgets(), is(equalTo(region.getRegionWidgets())));
        assertThat(converted.getEntityId().toString(), is(equalTo(region.getId())));
        assertThat(converted.getId(), is(equalTo(region.getId())));
        assertThat(converted.getPage(), is(instanceOf(Page.class)));
        assertThat(converted.getRenderOrder(), is(equalTo(region.getRenderOrder())));
        assertThat(converted.isLocked(), is(equalTo(region.isLocked())));
    }
}
