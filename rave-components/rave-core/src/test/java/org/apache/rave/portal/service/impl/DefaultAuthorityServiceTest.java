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

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.service.impl.DefaultAuthorityService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.service.AuthorityService;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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

        expect(repository.get(123L)).andReturn(authority);
        replay(repository);
        Authority sAuthority = service.getAuthorityById(123L);
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
        Authority authority = new Authority();
        authority.setAuthority("FOO");
        final long entityId = 123L;
        authority.setEntityId(entityId);
        return authority;
    }

    @Test
    public void getAuthorityById_NotFound() {
        Authority authority = new Authority();
        authority.setAuthority("BAR");
        final long entityId = 456L;
        authority.setEntityId(entityId);

        expect(repository.get(entityId)).andReturn(null);
        replay(repository);
        Authority sAuthority = service.getAuthorityById(entityId);
        assertNull(sAuthority);

        verify(repository);
    }

    @Test
    public void allAuthorities() {
        List<Authority> authorities = new ArrayList<Authority>();
        Authority foo = new Authority();
        foo.setAuthority("FOO");
        Authority bar = new Authority();
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
}
