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
package org.apache.rave.portal.security.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.apache.rave.portal.security.util.AuthenticationUtils;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author carlucci
 */
public class AbstractModelPermissionEvaluatorTest {
    private FooModelPermissionEvaluator fooModelPermissionEvaluator;
    private Authentication authentication;
    private FooModel fooModel;    
    
    @Before
    public void setUp() {
        authentication = createMock(Authentication.class);
        fooModel = new FooModel();
        fooModelPermissionEvaluator = new FooModelPermissionEvaluator();
    }
         
    @Test
    public void testGetLoadOrder() {
        // test that the default loadOrder value is 1
        assertThat(fooModelPermissionEvaluator.getLoadOrder(), is(1));
    }

    @Test
    public void testHasPermission_authenticationUserIsAdmin() {        
        List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(authentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(authentication);
        assertThat(fooModelPermissionEvaluator.hasPermission(authentication, fooModel, Permission.READ), is(true));
        verify(authentication);
    }

    @Test
    public void testHasPermission_authenticationUserIsNotAdmin() {        
        List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_THAT_IS_NOT_ADMIN"));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(authentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(authentication);
        assertThat(fooModelPermissionEvaluator.hasPermission(authentication, fooModel, Permission.READ), is(false));
        verify(authentication);
    }    
        
    @Test
    public void testHasPermission_nullAuthorities() {                        
        expect(authentication.getAuthorities()).andReturn(null);
        replay(authentication);
        assertThat(fooModelPermissionEvaluator.hasPermission(authentication, fooModel, Permission.READ), is(false));
        verify(authentication);
    }
    
    class FooModel {
        public FooModel() {
            
        }
    }
            
    class FooModelPermissionEvaluator extends AbstractModelPermissionEvaluator<FooModel> {
        @Override
        public Class<FooModel> getType() {
            return FooModel.class;
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
            return true;
        }
                
    }       
}
