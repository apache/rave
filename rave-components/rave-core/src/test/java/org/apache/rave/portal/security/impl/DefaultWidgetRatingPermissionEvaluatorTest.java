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

import org.apache.rave.model.WidgetRating;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
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


public class DefaultWidgetRatingPermissionEvaluatorTest {
    private DefaultWidgetRatingPermissionEvaluator defaultWidgetRatingPermissionEvaluator;
    private WidgetRepository mockWidgetRatingRepository;
    private Authentication mockAuthentication;
    private List<GrantedAuthority> grantedAuthoritiesList;
    private WidgetRating widgetRating;
    private UserImpl user, user2;

    private final String VALID_USER_ID = "99";
    private final String VALID_USER_ID2 = "100";
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";
    private final String VALID_WIDGET_ID = "1";
    private final String  VALID_WIDGET_RATING_ID = "1";

    @Before
    public void setUp() {
        mockWidgetRatingRepository = createMock(WidgetRepository.class);
        defaultWidgetRatingPermissionEvaluator = new DefaultWidgetRatingPermissionEvaluator(mockWidgetRatingRepository);

        widgetRating = new WidgetRatingImpl(VALID_WIDGET_ID);
        widgetRating.setUserId(VALID_USER_ID);

        user = new UserImpl();
        user.setUsername(VALID_USERNAME);
        user.setId(VALID_USER_ID);
        user2 = new UserImpl();
        user2.setId(VALID_USER_ID2);
        user2.setUsername(VALID_USERNAME2);

        mockAuthentication = createMock(Authentication.class);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));

    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultWidgetRatingPermissionEvaluator.getType().getName(), is(WidgetRating.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    // -------------------
    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_delete_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_3args_update_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_3args_update_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_3args_read_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_3args_read_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, widgetRating, ModelPermissionEvaluator.Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_create_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_read_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_read_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_update_isWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_update_isNotWidgetRatingOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockWidgetRatingRepository.getRatingById(null, VALID_WIDGET_RATING_ID)).andReturn(widgetRating);
        replay(mockAuthentication);
        replay(mockWidgetRatingRepository);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, VALID_WIDGET_RATING_ID, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetRatingRepository);
    }

    @Test
    public void testHasPermission_4args_update_isWidgetRatingOwner_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isWidgetRatingOwner_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isWidgetRatingOwner_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultWidgetRatingPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetRating.class.getName(), ModelPermissionEvaluator.Permission.UPDATE);
        verify(mockAuthentication);
    }


}
