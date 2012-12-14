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

package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 2:37 PM
 */
public class MongoDbPageTest {
    private MongoDbPage page;
    private UserRepository repository;

    @Before
    public void setup() {
        page = new MongoDbPage();
        repository = createMock(UserRepository.class);
        page.setUserRepository(repository);
    }

    @Test
    public void getOwner_Null_Owner(){
        User user = new UserImpl();
        page.setOwnerId((long)123);
        expect(repository.get((long) 123)).andReturn(user);
        replay(repository);

        assertThat(page.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void getOwner_Owner_Set(){
        User user = new UserImpl();
        page.setOwner(user);

        assertThat(page.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void equals_Same(){
        assertTrue(page.equals(page));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(page.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        page.setId((long)123);
        Page r = new MongoDbPage();
        assertFalse(page.equals(r));
        assertFalse(r.equals(page));

    }

    @Test
    public void equals_Valid(){
        page.setId((long)123);
        MongoDbPage r = new MongoDbPage();
        r.setId((long) 123);
        assertTrue(page.equals(r));
    }

    @Test
    public void equals_Valid_Null_Id(){
        page.setId(null);
        MongoDbPage r = new MongoDbPage();
        r.setId(null);
        assertTrue(page.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertTrue(page.hashCode()==0);
    }

    @Test
    public void hashCode_Id_Set(){
          page.setId((long)123);
        assertNotNull(page.hashCode());
    }

}
