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

import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.portal.model.JpaActivityStreamsMediaLink;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;
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
public class JpaActivityStreamsMediaLinkConverterTest {

    @Autowired
    JpaActivityStreamsMediaLinkConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaActivityStreamsMediaLink template = new JpaActivityStreamsMediaLink();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        ActivityStreamsMediaLink template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        ActivityStreamsMediaLink template = new ActivityStreamsMediaLinkImpl();
        template.setDuration(1);
        template.setHeight(1);
        template.setUrl("TEST_C");
        template.setWidth(1);



        JpaActivityStreamsMediaLink jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaActivityStreamsMediaLink.class)));
        assertThat(jpaTemplate.getDuration(), is(equalTo(template.getDuration())));
        assertThat(jpaTemplate.getHeight(), is(equalTo(template.getHeight())));
        assertThat(jpaTemplate.getWidth(), is(equalTo(template.getWidth())));
        assertThat(jpaTemplate.getUrl(), is(equalTo(template.getUrl())));


    }

}
