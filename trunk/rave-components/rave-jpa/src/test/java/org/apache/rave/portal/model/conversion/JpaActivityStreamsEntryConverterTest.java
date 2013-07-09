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

import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.JpaActivityStreamsEntry;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
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
public class JpaActivityStreamsEntryConverterTest {

    @Autowired
    JpaActivityStreamsEntryConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaActivityStreamsEntry template = new JpaActivityStreamsEntry();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        ActivityStreamsEntry template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        ActivityStreamsEntry template = new ActivityStreamsEntryImpl();
        template.setAppId("TEST_A");
        template.setBcc("TEST_B");
        template.setContent("TEST_C");
        template.setContext("TEST_D");
        template.setDc("TEST_E");
        template.setInReplyTo("TEST_F");
        template.setLocation("TEST_G");
        template.setPriority("TEST_H");
        template.setTitle("TEST_I");
        template.setGroupId("TEST_J");
        template.setSource("TEST_K");
        template.setUserId("TEST_K");


        JpaActivityStreamsEntry jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaActivityStreamsEntry.class)));
        assertThat(jpaTemplate.getAppId(), is(equalTo(template.getAppId())));
        assertThat(jpaTemplate.getBcc(), is(equalTo(template.getBcc())));
        assertThat(jpaTemplate.getContent(), is(equalTo(template.getContent())));
        assertThat(jpaTemplate.getDc(), is(equalTo(template.getDc())));
        assertThat(jpaTemplate.getInReplyTo(), is(equalTo(template.getInReplyTo())));
        assertThat(jpaTemplate.getLocation(), is(equalTo(template.getLocation())));
        assertThat(jpaTemplate.getPriority(), is(equalTo(template.getPriority())));
        assertThat(jpaTemplate.getTitle(), is(equalTo(template.getTitle())));
        assertThat(jpaTemplate.getGroupId(), is(equalTo(template.getGroupId())));
        assertThat(jpaTemplate.getSource(), is(equalTo(template.getSource())));
        assertThat(jpaTemplate.getUserId(), is(equalTo(template.getUserId())));
    }

}
