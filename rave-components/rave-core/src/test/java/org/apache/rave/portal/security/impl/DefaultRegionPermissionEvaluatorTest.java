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

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.RegionRepository;
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

public class DefaultRegionPermissionEvaluatorTest {
    private DefaultRegionPermissionEvaluator defaultRegionPermissionEvaluator;
    private RegionRepository mockRegionRepository;
    private Page page;
    private Region region, region2;
    private User user, user2;
    private Authentication mockAuthentication;
    private List<GrantedAuthority> grantedAuthoritiesList;

    private final Long VALID_REGION_ID = 1L;
    private final Long VALID_PAGE_ID = 3L;
    private final Long VALID_USER_ID = 99L;
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";

    @Before
    public void setUp() {
        mockRegionRepository = createMock(RegionRepository.class);
        defaultRegionPermissionEvaluator = new DefaultRegionPermissionEvaluator(mockRegionRepository);
        mockAuthentication = createMock(Authentication.class);

        user = new User();
        user.setUsername(VALID_USERNAME);
        user.setEntityId(VALID_USER_ID);
        user2 = new User();
        user2.setUsername(VALID_USERNAME2);
        page = new Page();
        page.setEntityId(VALID_PAGE_ID);
        page.setOwner(user);
        region = new Region();
        region.setEntityId(VALID_REGION_ID);
        region.setPage(page);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultRegionPermissionEvaluator.getType().getName(), is(Region.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_create_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_update_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_update_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_read_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_3args_read_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, region, ModelPermissionEvaluator.Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_create_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_read_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_read_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_update_isRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_update_isNotRegionOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockRegionRepository.get(VALID_REGION_ID)).andReturn(region);
        replay(mockAuthentication);
        replay(mockRegionRepository);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_ID, Region.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionRepository);
    }

    @Test
    public void testHasPermission_4args_update_isRegionOwner_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.portal.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Region.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isRegionOwner_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Region.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isRegionOwner_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultRegionPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Region.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

}
