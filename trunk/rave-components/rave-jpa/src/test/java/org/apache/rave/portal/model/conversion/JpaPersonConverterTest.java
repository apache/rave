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

import org.apache.rave.model.Address;
import org.apache.rave.model.Organization;
import org.apache.rave.model.Person;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.portal.model.JpaPerson;
import org.apache.rave.portal.model.impl.PersonImpl;
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
public class JpaPersonConverterTest {

    @Autowired
    JpaPersonConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPerson template = new JpaPerson();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        Person template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        Person template = new PersonImpl();
        template.setUsername("TEST_A");
        template.setEmail("TEST_B");
        template.setDisplayName("TEST_C");
        template.setAdditionalName("TEST_D");
        template.setFamilyName("TEST_E");
        template.setGivenName("TEST_F");
        template.setHonorificPrefix("TEST_G");
        template.setHonorificSuffix("TEST_H");
        template.setPreferredName("TEST_I");
        template.setAboutMe("TEST_J");
        template.setStatus("TEST_K");
        template.setAddresses(new ArrayList<Address>());
        template.setOrganizations(new ArrayList<Organization>());
        template.setProperties(new ArrayList<PersonProperty>());

        JpaPerson jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPerson.class)));
        assertThat(jpaTemplate.getUsername(), is(equalTo(template.getUsername())));
        assertThat(jpaTemplate.getEmail(), is(equalTo(template.getEmail())));
        assertThat(jpaTemplate.getDisplayName(), is(equalTo(template.getDisplayName())));
        assertThat(jpaTemplate.getUsername(), is(equalTo(template.getUsername())));
        assertThat(jpaTemplate.getFamilyName(), is(equalTo(template.getFamilyName())));
        assertThat(jpaTemplate.getGivenName(), is(equalTo(template.getGivenName())));
        assertThat(jpaTemplate.getHonorificPrefix(), is(equalTo(template.getHonorificPrefix())));
        assertThat(jpaTemplate.getHonorificSuffix(), is(equalTo(template.getHonorificSuffix())));
        assertThat(jpaTemplate.getPreferredName(), is(equalTo(template.getPreferredName())));
        assertThat(jpaTemplate.getAboutMe(), is(equalTo(template.getAboutMe())));
        assertThat(jpaTemplate.getStatus(), is(equalTo(template.getStatus())));
        assertThat(jpaTemplate.getAddresses(), is(equalTo(template.getAddresses())));
        assertThat(jpaTemplate.getOrganizations(), is(equalTo(template.getOrganizations())));
        assertThat(jpaTemplate.getProperties(), is(equalTo(template.getProperties())));
    }

}
