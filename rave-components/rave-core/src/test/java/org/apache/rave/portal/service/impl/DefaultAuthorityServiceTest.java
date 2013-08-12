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

package org.apache.rave.portal.service.impl;

import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.service.AuthorityService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
Test for {@link org.apache.rave.portal.service.impl.DefaultAuthorityService}
 */
public class DefaultAuthorityServiceTest {

    private AuthorityService service;
    private AuthorityRepository repository;

    @Before
    public void setup() {
        repository = createMock(AuthorityRepository.class);
        service = new DefaultAuthorityService(repository);
    }

    @Test
    public void getAuthorityById() {
        Authority authority = createAuthority();

        expect(repository.get("123")).andReturn(authority);
        replay(repository);
        Authority sAuthority = service.getAuthorityById("123");
        assertEquals(sAuthority, authority);

        verify(repository);
    }

    @Test
    public void getAuthorityByName() {
        Authority authority = createAuthority();

        expect(repository.getByAuthority("FOO")).andReturn(authority);
        replay(repository);
        Authority sAuthority = service.getAuthorityByName("FOO");
        assertEquals(sAuthority, authority);

        verify(repository);

    }

    private static Authority createAuthority() {
        AuthorityImpl authority = new AuthorityImpl();
        authority.setAuthority("FOO");
        final long entityId = 123L;
        return authority;
    }

    @Test
    public void getAuthorityById_NotFound() {
        AuthorityImpl authority = new AuthorityImpl();
        authority.setAuthority("BAR");
        final String entityId = "456";

        expect(repository.get(entityId)).andReturn(null);
        replay(repository);
        Authority sAuthority = service.getAuthorityById(entityId);
        assertNull(sAuthority);

        verify(repository);
    }

    @Test
    public void allAuthorities() {
        List<Authority> authorities = new ArrayList<Authority>();
        Authority foo = new AuthorityImpl();
        foo.setAuthority("FOO");
        Authority bar = new AuthorityImpl();
        bar.setAuthority("BAR");
        authorities.add(foo);
        authorities.add(bar);

        expect(repository.getAll()).andReturn(authorities);
        expect(repository.getCountAll()).andReturn(authorities.size());
        replay(repository);
        SearchResult<Authority> allAuthorities = service.getAllAuthorities();
        verify(repository);

        assertEquals(2, allAuthorities.getTotalResults());
        assertEquals(2, allAuthorities.getResultSet().size());
    }
    
    @Test
    public void test_getAllDefault() {
        List<Authority> authorities = new ArrayList<Authority>();
        Authority foo = new AuthorityImpl();
        foo.setAuthority("FOO");
        foo.setDefaultForNewUser(true);
        Authority bar = new AuthorityImpl();
        bar.setAuthority("BAR");
        bar.setDefaultForNewUser(true);
        
        authorities.add(foo);
        authorities.add(bar);

        expect(repository.getAllDefault()).andReturn(authorities);
        replay(repository);
        SearchResult<Authority> defaultAuthorities = service.getDefaultAuthorities();
        verify(repository);

        assertThat(defaultAuthorities.getTotalResults(), is(2));
        assertThat(defaultAuthorities.getResultSet().size(), is(2));
    }
}
