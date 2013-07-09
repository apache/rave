/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.security.impl;

import org.apache.rave.model.WidgetComment;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.repository.WidgetRepository;
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

/**
 *
 */
public class DefaultWidgetCommentPermissionEvaluatorTest {

    private DefaultWidgetCommentPermissionEvaluator defaultWidgetCommentPermissionEvaluator;
    private WidgetRepository widgetRepository;
    private Authentication mockAuthentication;
    private WidgetComment widgetComment;
    private UserImpl user, user2;
    private List<GrantedAuthority> grantedAuthoritiesList;

    private final String VALID_COMMENT_ID = "3";
    private final String VALID_USER_ID = "99";
    private final String INVALID_USER_ID = VALID_USER_ID + 1;
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";

    @Before
    public void setUp() {
        widgetRepository = createMock(WidgetRepository.class);
        mockAuthentication = createMock(Authentication.class);
        defaultWidgetCommentPermissionEvaluator = new DefaultWidgetCommentPermissionEvaluator(widgetRepository);

        user = new UserImpl();
        user.setUsername(VALID_USERNAME);
        user.setId(VALID_USER_ID);
        user2 = new UserImpl();
        user2.setUsername(VALID_USERNAME2);
        user2.setId(INVALID_USER_ID);
        widgetComment = new WidgetCommentImpl(VALID_COMMENT_ID);
        widgetComment.setUserId(VALID_USER_ID);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultWidgetCommentPermissionEvaluator.getType().getName(), is(WidgetComment.class.getName()));
    }

    @Test
    public void verifyNotAdministrator() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, null, Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void verifyAdministrator() {
        List<GrantedAuthority> myGrantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        myGrantedAuthoritiesList.addAll(grantedAuthoritiesList);
        myGrantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));


        expect((List<GrantedAuthority>) mockAuthentication.getAuthorities()).andReturn(myGrantedAuthoritiesList);
        replay(mockAuthentication);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, null, Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testOwnerPermissions() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);
        expect(widgetRepository.getCommentById(null, VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(widgetRepository);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.DELETE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.UPDATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE_OR_UPDATE), is(true));
        verify(mockAuthentication);
        verify(widgetRepository);
    }

    @Test
    public void testNonOwnerPermissions() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        replay(mockAuthentication);
        expect(widgetRepository.getCommentById(null, VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(widgetRepository);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.DELETE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.UPDATE), is(false));

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(widgetRepository);
    }

    @Test
    public void testOwnerPermissionsById() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);
        expect(widgetRepository.getCommentById(null, VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(widgetRepository);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.DELETE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.UPDATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(true));
        verify(mockAuthentication);
        verify(widgetRepository);
    }

    @Test
    public void testNonOwnerPermissionsById() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        replay(mockAuthentication);
        expect(widgetRepository.getCommentById(null, VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(widgetRepository);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.DELETE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.UPDATE), is(false));

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(widgetRepository);
    }

    @Test
    public void testOwnerPremissionBySecurityContext() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, WidgetComment.class.getName());

        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.DELETE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.UPDATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testNonOwnerPremissionBySecurityContext() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(INVALID_USER_ID, WidgetComment.class.getName());

        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.CREATE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.DELETE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.UPDATE), is(false));

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testCreationPermission() {
        //Widget.entityId is not set in this case

        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);

        WidgetComment localWidgetComment = new WidgetCommentImpl();
        UserImpl localUser = new UserImpl();
        localUser.setId(VALID_USER_ID);
        localWidgetComment.setUserId(VALID_USER_ID);
        expect(widgetRepository.getCommentById(null, VALID_COMMENT_ID)).andReturn(localWidgetComment).anyTimes();
        replay(widgetRepository);

        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, localWidgetComment, Permission.CREATE), is(true));

        verify(mockAuthentication);
        verify(widgetRepository);
    }
}
