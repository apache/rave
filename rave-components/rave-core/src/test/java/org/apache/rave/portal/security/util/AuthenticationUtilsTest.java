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
package org.apache.rave.portal.security.util;

import org.easymock.EasyMock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
/**
 *
 * @author carlucci
 */
public class AuthenticationUtilsTest {
    private Authentication authentication;    
    private final String VALID_ROLE = "MY_ROLE";
    private final String INVALID_ROLE = "UNKNOWN_ROLE";
    
    @Before
    public void setup() {
        authentication = createMock(Authentication.class);
    }
    
    @Test
    public void testHasRole() {    
        List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(VALID_ROLE));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(authentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        replay(authentication);
        
        assertThat(AuthenticationUtils.hasRole(authentication, VALID_ROLE), is(true));
        assertThat(AuthenticationUtils.hasRole(authentication, INVALID_ROLE), is(false));
        assertThat(AuthenticationUtils.hasRole(authentication, null), is(false));
        assertThat(AuthenticationUtils.hasRole(authentication, ""), is(false));
    }


    @Test
    public void testIsAdmin_validAdmin() {
        List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(authentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(authentication);
        
        assertThat(AuthenticationUtils.isAdmin(authentication), is(true));
    }
    
    @Test
    public void testIsAdmin_notValidAdmin() {
        List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(VALID_ROLE));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(authentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(authentication);
        
        assertThat(AuthenticationUtils.isAdmin(authentication), is(false));
    }    
}