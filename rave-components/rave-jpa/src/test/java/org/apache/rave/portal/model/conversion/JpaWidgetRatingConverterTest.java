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

import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
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
public class JpaWidgetRatingConverterTest {

    @Autowired
    JpaWidgetRatingConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaWidgetRating template = new JpaWidgetRating();
        assertThat(converter.convert(template, "1"), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        WidgetRating template = null;
        assertThat(converter.convert(template, "1"), is(nullValue()));
    }


    @Test
    public void convertValid() {
        WidgetRating template = new WidgetRatingImpl("1");
        template.setScore(1);
        template.setUserId("42");
        
        JpaWidgetRating jpaTemplate = converter.convert(template, "24");

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaWidgetRating.class)));
        assertThat(jpaTemplate.getScore(), is(equalTo(template.getScore())));
        assertThat(jpaTemplate.getUserId(), is(equalTo(template.getUserId())));
    }

}
