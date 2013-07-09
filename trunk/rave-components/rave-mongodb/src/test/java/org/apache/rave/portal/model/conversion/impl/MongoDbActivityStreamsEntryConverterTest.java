/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class MongoDbActivityStreamsEntryConverterTest {

    private static final String VALUE = "VALUE";
    private static final Date DATE_VALUE = new Date();
    private MongoDbActivityStreamsEntryConverter converter;

    @Before
    public void setup() {
        converter = new MongoDbActivityStreamsEntryConverter();
    }

    @Test
    public void checkAllNull() {
        ActivityStreamsEntry converted = converter.convert((ActivityStreamsEntry)null);
        assertThat(converted, is(nullValue()));
    }

    @Test
    public void checkNullProperties() {
        ActivityStreamsEntry source = new ActivityStreamsEntryImpl();
        ActivityStreamsEntry converted = converter.convert(source);
        assertThat(converted, is(not(sameInstance(source))));
    }

    @Test
    public void checkPopulatedProperties() {
        ActivityStreamsEntry source = createSource(ActivityStreamsEntry.class, true);
        ActivityStreamsEntry converted = converter.convert(source);
        assertThat(converted, is(not(sameInstance(source))));
        assertThat(converted.getId(), is(not(nullValue())));
        assertThat(converted.getVerb(), is(equalTo(VALUE)));
        assertThat(converted.getContent(), is(equalTo(VALUE)));
        assertThat(converted.getActor().getId(), is(not(nullValue())));
        assertThat(converted.getObject().getId(), is(not(nullValue())));
        assertThat(converted.getTarget().getId(), is(not(nullValue())));
    }

    private static <T> T createSource(Class<T> clazz, final boolean createId) {
        return (T)Proxy.newProxyInstance(MongoDbActivityStreamsEntryConverterTest.class.getClassLoader(), new Class[]{clazz}, getInvocationHandler(createId));
    }

    private static InvocationHandler getInvocationHandler(final boolean createId) {
        return new ActivityTestInvocationHandler(createId);
    }

    private static class ActivityTestInvocationHandler implements InvocationHandler {

        private final boolean nullId;

        public ActivityTestInvocationHandler(boolean nullId) {
            this.nullId = nullId;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] params) throws Throwable {
            Object returnValue = null;
            if(method.getName().startsWith("get")) {
                if(params != null) {
                    throw new IllegalArgumentException("POJO get cannot have params");
                }
                Class<?> returnType = method.getReturnType();
                if(returnType.equals(ActivityStreamsObject.class)) {
                    returnValue = method.getName().equals("getAuthor") ? null : createSource(returnType, nullId);
                } else if(returnType.equals(ActivityStreamsMediaLink.class)){
                    returnValue = createSource(returnType, nullId);
                } else if(returnType.equals(String.class)) {
                    returnValue = method.getName().equals("getId") ? nullId ? null : VALUE : VALUE;
                } else if(returnType.equals(Date.class)) {
                    returnValue = DATE_VALUE;
                }
            }
            return returnValue;
        }


    }
}
