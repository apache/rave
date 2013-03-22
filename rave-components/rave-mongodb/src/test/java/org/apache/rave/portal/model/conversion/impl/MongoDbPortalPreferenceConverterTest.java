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


import com.google.common.collect.Lists;
import org.apache.rave.portal.model.MongoDbPortalPreference;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.notNull;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Test class for MongoDb Portal Preference Converter
 */

public class MongoDbPortalPreferenceConverterTest {

    private MongoDbPortalPreferenceConverter converter;

    @Before
    public void setUp(){

        converter = new MongoDbPortalPreferenceConverter();
    }

    @Test
    public void convertPreference_valid(){

        PortalPreference pp = new PortalPreferenceImpl();
        pp.setKey("key");
        pp.setValues(Lists.<String>newLinkedList());
        MongoDbPortalPreference converted;
        MongoDbPortalPreference mpp = new MongoDbPortalPreference();
        mpp.setKey("carol");
        mpp.setValues(Lists.<String>newLinkedList());

        converted = converter.convert(pp);

        assertThat(converted.getKey(), is(equalTo("key")));
        assertTrue(converted.getValues().isEmpty());
        //assertNotNull(converted.getId());

        converted = converter.convert(mpp);

        assertThat(converted.getKey(), is(equalTo("carol")));
        assertTrue(converted.getValues().isEmpty());
        assertThat(converted, is(sameInstance(mpp)));
        assertThat(converted.getId(), is(notNull()));

    }

    @Test
    public void hydrate(){
        MongoDbPortalPreference mpp = new MongoDbPortalPreference();
        converter.hydrate(mpp);
        assertNotNull(mpp);
    }

    @Test
    public void getSourceType(){

        assertThat(converter.getSourceType(), is(equalTo(PortalPreference.class)));
    }

}
