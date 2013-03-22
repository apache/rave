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

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
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

public class DefaultRegionWidgetPermissionEvaluatorTest {
    private DefaultRegionWidgetPermissionEvaluator defaultRegionWidgetPermissionEvaluator;
    private RegionWidgetRepository mockRegionWidgetRepository;
    private Authentication mockAuthentication;
    private Page page;
    private RegionWidget regionWidget;
    private Region region;
    private UserImpl user, user2;
    private List<GrantedAuthority> grantedAuthoritiesList;

    private final String VALID_REGION_ID = "1";
    private final String VALID_REGION_WIDGET_ID = "1";
    private final String VALID_PAGE_ID = "3";
    private final String VALID_USER_ID = "99";
    private final String VALID_USER_ID2 = "66";
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";

    @Before
    public void setUp() {
        mockRegionWidgetRepository = createMock(RegionWidgetRepository.class);
        defaultRegionWidgetPermissionEvaluator = new DefaultRegionWidgetPermissionEvaluator(mockRegionWidgetRepository, null);
        mockAuthentication = createMock(Authentication.class);

        user = new UserImpl();
        user.setUsername(VALID_USERNAME);
        user.setId(VALID_USER_ID);
        user2 = new UserImpl();
        user2.setUsername(VALID_USERNAME2);
        user2.setId(VALID_USER_ID2);
        page = new PageImpl();
        page.setId(VALID_PAGE_ID);
        page.setOwnerId(user.getId());
        region = new RegionImpl(VALID_REGION_ID);
        region.setPage(page);
        regionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID);
        regionWidget.setRegion(region);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultRegionWidgetPermissionEvaluator.getType().getName(), is(RegionWidget.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_create_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_update_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>> expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_update_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_read_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_read_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, regionWidget, Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_create_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_read_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_read_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isNotRegionWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockRegionWidgetRepository.get(VALID_REGION_WIDGET_ID)).andReturn(regionWidget);
        replay(mockAuthentication);
        replay(mockRegionWidgetRepository);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_REGION_WIDGET_ID, RegionWidget.class.getName(), Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockRegionWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isRegionWidgetOwner_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, RegionWidget.class.getName(), Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isRegionWidgetOwner_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, RegionWidget.class.getName(), Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isRegionWidgetOwner_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultRegionWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, RegionWidget.class.getName(), Permission.UPDATE);
        verify(mockAuthentication);
    }
}
