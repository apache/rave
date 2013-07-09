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

import org.apache.rave.portal.model.JpaPersonProperty;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.portal.model.impl.PersonPropertyImpl;
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
public class JpaPersonPropertyConverterTest {

    @Autowired
    JpaPersonPropertyConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPersonProperty template = new JpaPersonProperty();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        PersonProperty template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        PersonProperty template = new PersonPropertyImpl("42");
        template.setType("TEST_A");
        template.setValue("TEST_B");
        template.setQualifier("TEST_C");
        template.setExtendedValue("TEST_D");
        template.setPrimary(true);

        JpaPersonProperty jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPersonProperty.class)));
        assertThat(jpaTemplate.getId(), is(equalTo(template.getId())));
        assertThat(jpaTemplate.getType(), is(equalTo(template.getType())));
        assertThat(jpaTemplate.getValue(), is(equalTo(template.getValue())));
        assertThat(jpaTemplate.getQualifier(), is(equalTo(template.getQualifier())));
        assertThat(jpaTemplate.getExtendedValue(), is(equalTo(template.getExtendedValue())));
        assertThat(jpaTemplate.getPrimary(), is(equalTo(template.getPrimary())));
        
    }

}
