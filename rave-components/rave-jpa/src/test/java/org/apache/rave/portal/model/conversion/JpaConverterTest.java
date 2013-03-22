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

import org.apache.rave.model.ModelConverter;
import org.apache.rave.model.Address;
import org.apache.rave.model.Group;
import org.apache.rave.portal.model.JpaAddress;
import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.portal.model.JpaPerson;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.Person;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.apache.rave.portal.model.impl.GroupImpl;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaConverterTest {

    private List<ModelConverter> converters;

    //Inject the list of converters from the context to work around any issues that arise during testing
    @Autowired
    private List<ModelConverter> originalConverters;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        ModelConverter personConverter = createMock(ModelConverter.class);
        expect(personConverter.getSourceType()).andReturn(Person.class).anyTimes();
        expect(personConverter.convert(isA(PersonImpl.class))).andReturn(new JpaPerson());
        replay(personConverter);

        ModelConverter addressConverter = createMock(ModelConverter.class);
        expect(addressConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(addressConverter.convert(isA(AddressImpl.class))).andReturn(new JpaAddress());
        replay(addressConverter);

        ModelConverter pageLayoutConverter = createMock(ModelConverter.class);
        expect(pageLayoutConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(pageLayoutConverter.convert(isA(PageLayout.class))).andReturn(new JpaPageLayout());
        replay(pageLayoutConverter);

        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        converters.add(personConverter);
        converters.add(addressConverter);
        this.converters = converters;
        Field instance = JpaConverter.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @After
    public void tearDown() {
        //Replace the instance of converters with the one from the context
        new JpaConverter(originalConverters);
    }

    @Test(expected=IllegalStateException.class)
    public void noInstance() {
        JpaConverter.getInstance();
    }

    @Test
    public void converts() {
        new JpaConverter(converters);
        assertThat(JpaConverter.getInstance().convert(new AddressImpl(), Address.class), is(instanceOf(JpaAddress.class)));
        assertThat(JpaConverter.getInstance().convert(new PersonImpl(), Person.class), is(instanceOf(JpaPerson.class)));

    }

    @Test
    public void getMap() {
        new JpaConverter(converters);
        ModelConverter<Address, JpaAddress> converter = JpaConverter.getInstance().getConverter(Address.class);
        assertThat(converter, is(not(nullValue())));
        assertThat(converter.getSourceType(), is(equalTo(Address.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void notFound() {
        new JpaConverter(converters);
        JpaConverter.getInstance().convert(new GroupImpl(), Group.class);
    }
}
