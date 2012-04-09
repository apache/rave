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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.security.ModelPermissionEvaluator;
import org.apache.rave.portal.security.util.AuthenticationUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultCategoryPermissionEvaluatorTest {
    private DefaultCategoryPermissionEvaluator defaultCategoryPermissionEvaluator;
    private CategoryRepository mockCategoryRepository;

    private Category category;
    private User user, user2;
    private Authentication mockAuthentication;
    private List<GrantedAuthority> grantedAuthorities;

    private final Long VALID_WIDGET_CATEGORY_ID = 22L;
    private final Long VALID_USER_ID = 99L;
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";
    
    @Before
    public void setUp() {
        mockCategoryRepository = createMock(CategoryRepository.class);
        defaultCategoryPermissionEvaluator = new DefaultCategoryPermissionEvaluator(mockCategoryRepository);
        mockAuthentication = createMock(Authentication.class);

        user = new User();
        user.setUsername(VALID_USERNAME);
        user.setEntityId(VALID_USER_ID);
        user2 = new User();
        user2.setUsername(VALID_USERNAME2);
        
        category = new Category();
        category.setEntityId(VALID_WIDGET_CATEGORY_ID);
        category.setCreatedUser(user);

        grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultCategoryPermissionEvaluator.getType().getName(), is(Category.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockCategoryRepository);
    }

    @Test
    public void testHasPermission_3args_create_hasAdminRole() {
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_3args_delete() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_3args_delete_hasAdminRole() {
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.DELETE), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_3args_update() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_3args_update_hasAdminRole() {
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }     

    @Test
    public void testHasPermission_3args_read() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, category, ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        replay(mockAuthentication);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        expect(mockCategoryRepository.get(VALID_WIDGET_CATEGORY_ID)).andReturn(category);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_delete() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        expect(mockCategoryRepository.get(VALID_WIDGET_CATEGORY_ID)).andReturn(category);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_read_isCreatedUser() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        expect(mockCategoryRepository.get(VALID_WIDGET_CATEGORY_ID)).andReturn(category);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_read_isNotCreatedUser() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        expect(mockCategoryRepository.get(VALID_WIDGET_CATEGORY_ID)).andReturn(category);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_update() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthorities);
        expect(mockCategoryRepository.get(VALID_WIDGET_CATEGORY_ID)).andReturn(category);
        replay(mockAuthentication, mockCategoryRepository);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_CATEGORY_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication, mockCategoryRepository);
    }

    @Test
    public void testHasPermission_4args_update_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.portal.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Category.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Category.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultCategoryPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Category.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

}