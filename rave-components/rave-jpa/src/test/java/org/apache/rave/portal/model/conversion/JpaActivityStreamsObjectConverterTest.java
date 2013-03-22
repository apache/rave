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

import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.JpaActivityStreamsObject;
import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
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
public class JpaActivityStreamsObjectConverterTest {

    @Autowired
    JpaActivityStreamsObjectConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaActivityStreamsObject template = new JpaActivityStreamsObject();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        ActivityStreamsObject template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        ActivityStreamsObject template = new ActivityStreamsObjectImpl();
        template.setContent("TEST_A");
        template.setDisplayName("TEST_B");
        template.setDc("TEST_C");
        template.setLd("TEST_D");
        template.setDc("TEST_E");
        template.setMood("TEST_F");
        template.setLocation("TEST_G");
        template.setRating("TEST_H");
        template.setSource("TEST_I");
        template.setLinks("TEST_J");



        JpaActivityStreamsObject jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaActivityStreamsObject.class)));
        assertThat(jpaTemplate.getContent(), is(equalTo(template.getContent())));
        assertThat(jpaTemplate.getDisplayName(), is(equalTo(template.getDisplayName())));
        assertThat(jpaTemplate.getContent(), is(equalTo(template.getContent())));
        assertThat(jpaTemplate.getDc(), is(equalTo(template.getDc())));
        assertThat(jpaTemplate.getMood(), is(equalTo(template.getMood())));
        assertThat(jpaTemplate.getLocation(), is(equalTo(template.getLocation())));
        assertThat(jpaTemplate.getRating(), is(equalTo(template.getRating())));
        assertThat(jpaTemplate.getSource(), is(equalTo(template.getSource())));
        assertThat(jpaTemplate.getLinks(), is(equalTo(template.getLinks())));

    }

}
