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
import org.apache.rave.portal.model.JpaAddress;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaAddressConverterTest {

    @Autowired
    private JpaAddressConverter addressConverter;

    @Test
    public void noConversion() {
        Address address = new JpaAddress();
        assertThat(addressConverter.convert(address), is(sameInstance(address)));
    }

    @Test
    public void nullConversion() {
        Address address = null;
        assertThat(addressConverter.convert(address), is(nullValue()));
    }

    @Test
    public void newAddress() {
        Address address = new AddressImpl();
        address.setStreetAddress("1600 Pennsylvania Avenue");
        address.setLocality("Washington DC");
        address.setCountry("USA");
        address.setRegion("Washington DC");
        address.setPrimary(true);

        JpaAddress converted = addressConverter.convert(address);
        assertThat(converted, is(not(sameInstance(address))));
        assertThat(converted, is(instanceOf(JpaAddress.class)));
        assertThat(converted.getStreetAddress(), is(equalTo(address.getStreetAddress())));
        assertThat(converted.getLocality(), is(equalTo(address.getLocality())));
        assertThat(converted.getCountry(), is(equalTo(address.getCountry())));
        assertThat(converted.getRegion(), is(equalTo(address.getRegion())));
        assertThat(converted.getPrimary(), is(true));
    }
}
