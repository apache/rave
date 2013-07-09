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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.WidgetImpl;
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
public class JpaWidgetConverterTest {

    @Autowired
    JpaWidgetConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaWidget template = new JpaWidget();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        Widget template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        WidgetImpl template = new WidgetImpl("42");
        template.setUrl("TEST_A");
        template.setType("TEST_B");
        template.setTitle("TEST_C");
        template.setTitleUrl("TEST_D");
        template.setUrl("TEST_E");
        template.setThumbnailUrl("TEST_F");
        template.setScreenshotUrl("TEST_G");
        template.setAuthor("TEST_H");
        template.setAuthorEmail("TEST_I");
        template.setDescription("TEST_J");
        template.setWidgetStatus(WidgetStatus.PUBLISHED);
        template.setComments(new ArrayList<WidgetComment>());
        template.setOwnerId("24");
        template.setDisableRendering(true);
        template.setRatings(new ArrayList<WidgetRating>());
        template.setTags(new ArrayList<WidgetTag>());
        template.setCategories(new ArrayList<Category>());
        template.setFeatured(true);

        Widget jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance((Widget)template))));
        assertThat(jpaTemplate, is(instanceOf(JpaWidget.class)));
        assertThat(jpaTemplate.getId(), is(equalTo(template.getId())));
        assertThat(jpaTemplate.getUrl(), is(equalTo(template.getUrl())));
        assertThat(jpaTemplate.getType(), is(equalTo(template.getType())));
        assertThat(jpaTemplate.getTitle(), is(equalTo(template.getTitle())));
        assertThat(jpaTemplate.getTitleUrl(), is(equalTo(template.getTitleUrl())));
        assertThat(jpaTemplate.getUrl(), is(equalTo(template.getUrl())));
        assertThat(jpaTemplate.getThumbnailUrl(), is(equalTo(template.getThumbnailUrl())));
        assertThat(jpaTemplate.getScreenshotUrl(), is(equalTo(template.getScreenshotUrl())));
        assertThat(jpaTemplate.getAuthor(), is(equalTo(template.getAuthor())));
        assertThat(jpaTemplate.getAuthorEmail(), is(equalTo(template.getAuthorEmail())));
        assertThat(jpaTemplate.getDescription(), is(equalTo(template.getDescription())));
        assertThat(jpaTemplate.getWidgetStatus(), is(equalTo(template.getWidgetStatus())));
        assertThat(jpaTemplate.getComments(), is(equalTo(template.getComments())));
        assertThat(jpaTemplate.getOwnerId(), is(equalTo(template.getOwnerId())));
        assertThat(jpaTemplate.isDisableRendering(), is(equalTo(template.isDisableRendering())));
        assertThat(jpaTemplate.getRatings(), is(equalTo(template.getRatings())));
        assertThat(jpaTemplate.getTags(), is(equalTo(template.getTags())));
        assertThat(jpaTemplate.getCategories(), is(equalTo(template.getCategories())));
        assertThat(jpaTemplate.isFeatured(), is(equalTo(template.isFeatured())));
    }

}
