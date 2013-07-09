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

import org.apache.rave.model.Authority;
import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:49 PM
 */
public class MongoDbUserTest {

    private MongoDbUser user;
    private PageLayoutRepository pageLayoutRepository;

    @Before
    public void setup(){
        user = new MongoDbUser();
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        user.setPageLayoutRepository(pageLayoutRepository);
    }

    @Test
    public void setAuthorities_Null(){
        ArrayList<String> array = new ArrayList<String>();
        array.add("string");
        user.setAuthorityCodes(array);
           user.setAuthorities(null);
        assertTrue(user.getAuthorityCodes().isEmpty());
    }

    @Test
    public void setAuthorities_Valid(){
        Authority auth = new AuthorityImpl();
        auth.setAuthority("auth");
        user.setAuthorities(Arrays.asList(auth));

        assertNotNull(user.getAuthorityCodes());
        assertThat(user.getAuthorityCodes().get(0), is(sameInstance(auth.getAuthority())));
    }

    @Test
    public void equals_Same(){
        assertTrue(user.equals(user));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(user.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        user.setId("123");
        MongoDbUser r = new MongoDbUser();
        assertFalse(user.equals(r));
        assertFalse(r.equals(user));

    }

    @Test
    public void equals_Valid(){
        user.setId("123");
        MongoDbUser r = new MongoDbUser();
        r.setId("123");
        assertTrue(user.equals(r));
    }

    @Test
    public void equals_Same_Null(){
        user.setId(null);
        MongoDbUser r = new MongoDbUser();
        assertTrue(user.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        user.setId("123");
        assertNotNull(user.hashCode());
    }

    @Test
    public void hashCode_Null(){
        assertNotNull(user.hashCode());
    }

    @Test
    public void getAuthorities_Valid(){
        ArrayList<String> array = new ArrayList<String>();
        array.add("string");
        user.setAuthorityCodes(array);

        Collection<GrantedAuthority> granted = user.getAuthorities();

        assertTrue(granted.size() == 1);
    }

    @Test
    public void addAuthority_Valid(){
        Authority authority = new AuthorityImpl();
        authority.setAuthority("auth");
        user.addAuthority(authority);
        assertTrue(user.getAuthorityCodes().contains(authority.getAuthority()));
    }

    @Test
    public void addAuthority_Contains(){
        Authority authority = new AuthorityImpl();
        authority.setAuthority("auth");
        List<String> authorityCodes = Arrays.asList(authority.getAuthority());
        user.setAuthorityCodes(authorityCodes);
        user.addAuthority(authority);
    }

    @Test
    public void removeAuthority_Valid(){
        Authority auth = new AuthorityImpl();
        auth.setAuthority("stinky");
        user.setAuthorityCodes(new ArrayList<String>());
        user.getAuthorityCodes().add("stinky");

        user.removeAuthority(auth);

        assertFalse(user.getAuthorityCodes().contains("stinky"));
    }

    @Test
    public void removeAuthority_NotContain(){
        Authority auth = new AuthorityImpl();
        user.removeAuthority(auth);
        assertNotNull(user.getAuthorityCodes());
    }

    @Test
    public void getDefaultPageLayout_Valid(){
        PageLayout layout = new PageLayoutImpl();
        user.setDefaultPageLayoutCode("dingus");
        expect(pageLayoutRepository.getByPageLayoutCode("dingus")).andReturn(layout);
        replay(pageLayoutRepository);

        assertThat(user.getDefaultPageLayout(), is(sameInstance(layout)));
    }

    @Test
    public void getDefaultPageLayout_Null(){
        PageLayout layout = new PageLayoutImpl();
        user.setDefaultPageLayout(layout);
        assertThat(layout, is(sameInstance(user.getDefaultPageLayout())));
    }

}
