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

import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageType;
import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.portal.model.JpaPageTemplate;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-dataContext.xml"})
public class JpaPageTemplateConverterTest {

    @Autowired
    JpaPageTemplateConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPageTemplate template = new JpaPageTemplate();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        PageTemplate template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        PageTemplate template = new PageTemplateImpl("1");
        template.setName("name");
        template.setSubPageTemplates(new ArrayList<PageTemplate>());
        template.setDefaultTemplate(true);
        template.setRenderSequence(1);
        template.setPageTemplateRegions(new ArrayList<PageTemplateRegion>());
        template.setPageLayout(new PageLayoutImpl());
        template.setParentPageTemplate(new PageTemplateImpl("1"));
        template.setDescription("Description");
        template.setPageType(PageType.USER.toString());

        JpaPageTemplate jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPageTemplate.class)));
        assertThat(jpaTemplate.getId().toString(), is(equalTo(template.getId())));
        assertThat(jpaTemplate.getPageType(), is(equalTo(template.getPageType())));
        assertThat(jpaTemplate.getName(), is(equalTo(template.getName())));
        assertThat(jpaTemplate.getDescription(), is(equalTo(template.getDescription())));
        assertThat(jpaTemplate.getParentPageTemplate(), is(instanceOf(JpaPageTemplate.class)));
        assertThat(jpaTemplate.getPageLayout(), is(instanceOf(JpaPageLayout.class)));
        assertThat(jpaTemplate.getPageTemplateRegions(), is(equalTo(template.getPageTemplateRegions())));
        assertThat(jpaTemplate.getRenderSequence(), is(equalTo(template.getRenderSequence())));
        assertThat(jpaTemplate.isDefaultTemplate(), is(equalTo(template.isDefaultTemplate())));
        assertThat(jpaTemplate.getSubPageTemplates(), is(equalTo(template.getSubPageTemplates())));
    }

}
