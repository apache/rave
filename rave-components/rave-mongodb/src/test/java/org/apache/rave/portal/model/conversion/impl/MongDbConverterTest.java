/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;


/**
 * Created with IntelliJ IDEA.
 * User: dsullivan
 * Date: 11/16/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongDbConverterTest {
    private MongoDbConverter converter;
    private ModelConverter mock1;
    private ModelConverter mock2;
    private List<HydratingModelConverter> converterList;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup(){
        converter = new MongoDbConverter();
        mock1 = createMock(HydratingModelConverter.class);
        mock2 = createMock(HydratingModelConverter.class);
        converterList = Arrays.asList(
                (HydratingModelConverter)mock1,
                (HydratingModelConverter)mock2
        );

        expect(mock1.getSourceType()).andReturn(String.class);
        expect(mock2.getSourceType()).andReturn(Integer.class);
    }

    @Test
    public void hydrate_Valid(){
        String source = "blah";
        Class<String> clazz = String.class;
        ((HydratingModelConverter)mock1).hydrate(source);
        expectLastCall();
        replay(mock1);
        converter.setConverters(converterList);

        converter.hydrate(source, clazz);
        verify(mock1);


    }

    @Test
    public void hydrate_Null(){
        converter.setConverters(converterList);
        long exceptionSource = 432;
        Class<Long> exceptionClass = Long.class;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No ModelConverter found for type "+exceptionClass);

        converter.hydrate(exceptionSource, exceptionClass);

    }

    @Test
    public void convert_Valid(){
        String source = "blah";
        Class<String> clazz = String.class;
        long returned = 123;

        expect(mock1.convert(source)).andReturn(returned);
        replay(mock1);
        converter.setConverters(converterList);

        assertThat(converter.<String, Long>convert(source, clazz),is(sameInstance(returned)));
    }

    @Test
    public void convert_Exception(){
        long exceptionSource = 432;
        Class<Long> exceptionClass = Long.class;

        converter.setConverters(converterList);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No ModelConverter found for type "+exceptionClass);

        converter.convert(exceptionSource, exceptionClass);
    }

    @Test
    public void getConverter_Valid(){
        replay(mock1,mock2);
        converter.setConverters(converterList);
        assertThat(converter.getConverter(String.class), is(sameInstance(mock1)));
        assertThat(converter.getConverter(Integer.class), is(sameInstance(mock2)));
    }

}
