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
import org.apache.rave.model.Person;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 */
public class ConvertingListProxyFactoryTest {

    // TODO: RAVE-689 temporary fix/workaround 
    private static class StaticConvertersAccessor extends JpaConverter {
        private StaticConvertersAccessor(List<ModelConverter> converters) {
            super(converters);
        }
        public static Map<Class<?>, ModelConverter> getConverters() {        
            return JpaConverter.isInstanceSet() ? JpaConverter.getInstance().converterMap : null;
        }
        public static void setConverters(Map<Class<?>, ModelConverter> converters) {
           if (JpaConverter.isInstanceSet()) {
               JpaConverter.getInstance().converterMap = converters;
           }
        }
    }
    
    private Map<Class<?>, ModelConverter> savedConverters;
    
    @Before
    public void setup() {
        savedConverters = StaticConvertersAccessor.getConverters();
    }
    
    @After
    public void teardown() {
        StaticConvertersAccessor.setConverters(savedConverters);
    }
    // end TODO: RAVE-689 temporary fix/workaround 

    @Test
    public void createProxy() {
        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        ModelConverter converterMock = createMock(ModelConverter.class);
        expect(converterMock.getSourceType()).andReturn(Person.class).anyTimes();
        converters.add(converterMock);
        replay(converterMock);
        new JpaConverter(converters);
        List<PersonImpl> underlying = new ArrayList<PersonImpl>();

        List<Person> personProxy = ConvertingListProxyFactory.createProxyList(Person.class, underlying);
        assertThat(Proxy.isProxyClass(personProxy.getClass()), is(true));
    }

    @Test
    public void proxyAdd() {
        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        ModelConverter converterMock = createMock(ModelConverter.class);
        expect(converterMock.getSourceType()).andReturn(Person.class).anyTimes();
        converters.add(converterMock);
        Person personImpl1 = new PersonImpl();
        PersonImpl personImpl2 = new PersonImpl();
        expect(converterMock.convert(personImpl1)).andReturn(personImpl2);
        replay(converterMock);
        List<PersonImpl> underlying = createMock(List.class);
        expect(underlying.add(personImpl2)).andReturn(true);
        replay(underlying);
        new JpaConverter(converters);


        List<Person> personProxy = ConvertingListProxyFactory.createProxyList(Person.class, underlying);
        Boolean good = personProxy.add(personImpl1);
        assertThat(good, is(true));
        verify(converterMock);
        verify(underlying);
    }

    @Test
    public void proxySet() {
        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        ModelConverter converterMock = createMock(ModelConverter.class);
        expect(converterMock.getSourceType()).andReturn(Person.class).anyTimes();
        converters.add(converterMock);
        Person personImpl1 = new PersonImpl();
        Person personImpl2 = new PersonImpl();
        expect(converterMock.convert(personImpl1)).andReturn(personImpl2);
        replay(converterMock);
        List<PersonImpl> underlying = createMock(List.class);
        expect(underlying.set(0, (PersonImpl)personImpl2)).andReturn((PersonImpl) personImpl2);
        replay(underlying);
        new JpaConverter(converters);

        List<Person> personProxy = ConvertingListProxyFactory.createProxyList(Person.class, underlying);
        Person good = personProxy.set(0, personImpl1);
        assertThat(good, is(sameInstance(personImpl2)));
        verify(converterMock);
        verify(underlying);
    }

    @Test
    public void proxyAddAll() {

        Person personImpl1 = new PersonImpl();
        Person personImpl2 = new PersonImpl();
        Person personImpl3 = new PersonImpl();
        Person personImpl4 = new PersonImpl();

        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        ModelConverter converterMock = createMock(ModelConverter.class);
        expect(converterMock.getSourceType()).andReturn(Person.class).anyTimes();
        converters.add(converterMock);
        expect(converterMock.convert(personImpl1)).andReturn(personImpl2);
        expect(converterMock.convert(personImpl3)).andReturn(personImpl4);
        replay(converterMock);

        List<Person> toAdd = new ArrayList<Person>();
        toAdd.add(personImpl1);
        toAdd.add(personImpl3);

        List<PersonImpl> underlying = createMock(List.class);
        expect(underlying.addAll(isA(List.class))).andReturn(true);
        replay(underlying);
        new JpaConverter(converters);

        List<Person> personProxy = ConvertingListProxyFactory.createProxyList(Person.class, underlying);
        Boolean good = personProxy.addAll(toAdd);
        assertThat(good, is(true));
        verify(converterMock);
        verify(underlying);
    }
}
