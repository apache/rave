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

import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.MongoDbAuthority;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * A test for the MongoDbAuthorityCoverter.java
 * To change this template use File | Settings | File Templates.
 */
public class MongoDbAuthorityConverterTest {
    private MongoDbAuthorityConverter authorityConverter;

    @Before
    public void setup(){
        authorityConverter = new MongoDbAuthorityConverter();
    }

    @Test
    public void hydrate_Valid(){
        MongoDbAuthority authority = new MongoDbAuthority();
        authorityConverter.hydrate(authority);
        assertNotNull(authority);
    }

    @Test
    public void convert_Valid(){
        Authority authority = new AuthorityImpl();
        authority.setAuthority("asd;lkfjlkj");
        authority.setDefaultForNewUser(true);

        MongoDbAuthority converted = authorityConverter.convert(authority);
        assertNotNull(converted.getAuthority());
        //assertNotNull(converted.getId());
        assertThat(converted.getAuthority(), is(sameInstance(authority.getAuthority())));
        assertThat(converted.isDefaultForNewUser(), is(sameInstance(authority.isDefaultForNewUser())));

        Authority authorityMongo = new MongoDbAuthority();
        authorityMongo.setAuthority("authority");
        authorityMongo.setDefaultForNewUser(true);
        MongoDbAuthority mongoConverted = authorityConverter.convert(authorityMongo);
        assertThat(mongoConverted, is(sameInstance(authorityMongo)));
        assertThat(mongoConverted.getAuthority(), is(sameInstance(authorityMongo.getAuthority())));
        assertThat(mongoConverted.isDefaultForNewUser(), is(sameInstance(authorityMongo.isDefaultForNewUser())));
    }

    @Test
    public void getSourceType_Valid(){
        assertNotNull(authorityConverter.getSourceType());
    }

}
