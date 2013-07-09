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

import org.apache.rave.model.PageTemplateWidget;
import org.apache.rave.portal.model.JpaPageTemplateRegion;
import org.apache.rave.portal.model.JpaPageTemplateWidget;
import org.apache.rave.portal.model.JpaWidget;
import org.apache.rave.portal.model.impl.PageTemplateRegionImpl;
import org.apache.rave.portal.model.impl.PageTemplateWidgetImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-dataContext.xml"})
public class JpaPageTemplateWidgetConverterTest {

    @Autowired
    JpaPageTemplateWidgetConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPageTemplateWidget template = new JpaPageTemplateWidget();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        PageTemplateWidget template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        JpaWidget widget = new JpaWidget();
        widget.setEntityId(1L);
        PageTemplateWidget template = new PageTemplateWidgetImpl("1");
        template.setLocked(true);
        template.setHideChrome(true);
        template.setPageTemplateRegion(new PageTemplateRegionImpl("1"));
        template.setRenderSeq(1);
        template.setWidgetId(widget.getId());

        JpaPageTemplateWidget jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPageTemplateWidget.class)));
        assertThat(jpaTemplate.isLocked(), is(equalTo(template.isLocked())));
        assertThat(jpaTemplate.getPageTemplateRegion(), is(instanceOf(JpaPageTemplateRegion.class)));
        assertThat(jpaTemplate.getRenderSeq(), is(equalTo(template.getRenderSeq())));
        assertThat(jpaTemplate.getWidgetId(), is(equalTo(template.getWidgetId())));
        assertThat(jpaTemplate.isLocked(), is(equalTo(template.isLocked())));
        assertThat(jpaTemplate.isHideChrome(), is(equalTo(template.isHideChrome())));
    }

}
