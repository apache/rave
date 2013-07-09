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
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.repository.WidgetRepository;
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

public class DefaultWidgetPermissionEvaluatorTest {
    private DefaultWidgetPermissionEvaluator defaultWidgetPermissionEvaluator;
    private WidgetRepository mockWidgetRepository;
    private TagRepository mockTagRepository;
    private Page page;
    private Widget widget, widget2;
    private UserImpl user, user2;
    private Authentication mockAuthentication;
    private List<GrantedAuthority> grantedAuthoritiesList;

    private final String VALID_WIDGET_ID = "1";
    private final String VALID_PAGE_ID = "3";
    private final String VALID_USER_ID = "99";
    private final String VALID_USER_ID2 = "66";
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";

    @Before
    public void setUp() {
        mockWidgetRepository = createMock(WidgetRepository.class);
        mockTagRepository = createMock(TagRepository.class);
        defaultWidgetPermissionEvaluator = new DefaultWidgetPermissionEvaluator(mockWidgetRepository, mockTagRepository);
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
        widget = new WidgetImpl(VALID_WIDGET_ID);
        widget.setOwnerId(VALID_USER_ID);
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultWidgetPermissionEvaluator.getType().getName(), is(Widget.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_create_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_update_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_update_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_read_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_read_isNotWidgetOwner_isPublishedGadget() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_3args_read_isNotWidgetOwner_isNotPublishedGadget() {
        widget.setWidgetStatus(WidgetStatus.PREVIEW);

        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, widget, ModelPermissionEvaluator.Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_create_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_read_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_read_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isNotWidgetOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRepository.get(VALID_WIDGET_ID)).andReturn(widget);
        replay(mockAuthentication);
        replay(mockWidgetRepository);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_ID, Widget.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRepository);
    }

    @Test
    public void testHasPermission_4args_update_isWidgetOwner_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Widget.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isWidgetOwner_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Widget.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isWidgetOwner_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultWidgetPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Widget.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

}
