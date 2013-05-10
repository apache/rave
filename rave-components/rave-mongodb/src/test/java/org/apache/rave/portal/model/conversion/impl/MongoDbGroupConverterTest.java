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
import org.apache.rave.model.Group;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.MongoDbGroup;
import org.apache.rave.portal.model.impl.GroupImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.easymock.EasyMock.createNiceMock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MongoDbGroupConverterTest {

    public static final String  USER1ID = "1234";
    public static final String USER2ID = "1333";
    public static final Date DATE1 = new Date();
    public static final Date DATE2 = new Date();
    private MongoDbGroupConverter converter;


    @Before
    public void setup(){
        converter = new MongoDbGroupConverter();
    }


    @Test
    public void hydrate_valid(){
        MongoDbGroup category = new MongoDbGroup();

        converter.hydrate(category);
    }

    @Test
    public void hydrate_null(){
        converter.hydrate(null);
    }


    @Test
    public void convert_valid(){
        Group group = new GroupImpl();
        group.setId(USER1ID);
        group.setTitle("asdf");
        group.setDescription("Description");
        group.setMemberIds(Arrays.asList(new String[] {"1", "2", "3"}));
        group.setOwnerId(USER2ID);

        MongoDbGroup mongoGroup = converter.convert(group);
        assertThat(mongoGroup.getId(), is(equalTo(USER1ID)));
        assertThat(mongoGroup.getTitle(), is(equalTo("asdf")));
        assertThat(mongoGroup.getDescription(), is("Description"));
        assertThat(mongoGroup.getMemberIds().size(), is(3));
        assertThat(mongoGroup.getOwnerId(), is(USER2ID));

    }//end convert_valid

    @Test
    public void convert_Null(){
        Group source = new GroupImpl();

        MongoDbGroup converted = converter.convert(source);

        assertNull(converted.getTitle());
        assertNull(converted.getDescription());
        assertNotNull(converted.getId());
    }
}
