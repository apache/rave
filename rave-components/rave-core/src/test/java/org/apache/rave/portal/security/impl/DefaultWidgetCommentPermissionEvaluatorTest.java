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

import org.apache.rave.portal.security.util.AuthenticationUtils;
import org.apache.rave.portal.model.WidgetComment;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import org.apache.rave.portal.model.User;
import org.springframework.security.core.Authentication;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 */
public class DefaultWidgetCommentPermissionEvaluatorTest {
    
    private DefaultWidgetCommentPermissionEvaluator defaultWidgetCommentPermissionEvaluator;
    private WidgetCommentRepository mockWidgetCommentRepository;
    private Authentication mockAuthentication;
    private WidgetComment widgetComment;
    private User user, user2;    
    private List<GrantedAuthority> grantedAuthoritiesList;
    
    private final Long VALID_COMMENT_ID = 3L;
    private final Long VALID_USER_ID = 99L;
    private final Long INVALID_USER_ID = VALID_USER_ID + 1;
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";
    @Before
    public void setUp() {               
        mockWidgetCommentRepository = createMock(WidgetCommentRepository.class);
        mockAuthentication = createMock(Authentication.class);
        defaultWidgetCommentPermissionEvaluator = new DefaultWidgetCommentPermissionEvaluator(mockWidgetCommentRepository);
        
        user = new User();
        user.setUsername(VALID_USERNAME);
        user.setEntityId(VALID_USER_ID);
        user2 = new User();
        user2.setUsername(VALID_USERNAME2);
        user2.setEntityId(INVALID_USER_ID);
        widgetComment = new WidgetComment();
        widgetComment.setEntityId(VALID_COMMENT_ID);
        widgetComment.setUser(user);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new GrantedAuthorityImpl("ROLE_USER"));
    }
    
    @Test
    public void testGetType() throws ClassNotFoundException {            
        assertThat(defaultWidgetCommentPermissionEvaluator.getType().getName(), is(WidgetComment.class.getName()));
    }
    
    @Test
    public void verifyNotAdministrator() {
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, null, Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }
    
    
    @Test
    public void verifyAdministrator() {
        List myGrantedAuthoritiesList = new ArrayList();
        myGrantedAuthoritiesList.addAll(grantedAuthoritiesList);
        myGrantedAuthoritiesList.add(new GrantedAuthorityImpl(AuthenticationUtils.ROLE_ADMIN));
        
        expect(mockAuthentication.getAuthorities()).andReturn(myGrantedAuthoritiesList);
        replay(mockAuthentication);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, null, Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }
    
    @Test 
    public void testOwnerPermissions() {
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);
        expect(mockWidgetCommentRepository.get(VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(mockWidgetCommentRepository);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.DELETE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.UPDATE), is(true));
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetCommentRepository);
    }
    
    @Test
    public void testNonOwnerPermissions() {
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        replay(mockAuthentication);
        expect(mockWidgetCommentRepository.get(VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(mockWidgetCommentRepository);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.DELETE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.UPDATE), is(false));
        
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, widgetComment, Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetCommentRepository);
    }
    
    @Test
    public void testOwnerPermissionsById() {
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);
        expect(mockWidgetCommentRepository.get(VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(mockWidgetCommentRepository);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.DELETE), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.UPDATE), is(true));
        
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetCommentRepository);
    }
    
    @Test
    public void testNonOwnerPermissionsById() {
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        replay(mockAuthentication);
        expect(mockWidgetCommentRepository.get(VALID_COMMENT_ID)).andReturn(widgetComment).anyTimes();
        replay(mockWidgetCommentRepository);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.ADMINISTER), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.READ), is(true));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.DELETE), is(false));
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.UPDATE), is(false));
        
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, VALID_COMMENT_ID, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockWidgetCommentRepository);
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
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, WidgetComment.class.getName(), Permission.CREATE_OR_UPDATE), is(false));
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
    public void testCreationPremission() {
        //Widget.entityId is not set in this case
        
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList).anyTimes();
        expect(mockAuthentication.getPrincipal()).andReturn(user).anyTimes();
        replay(mockAuthentication);
        
        WidgetComment localWidgetComment = new WidgetComment();
        User localUser = new User();
        localUser.setEntityId(VALID_USER_ID);
        localWidgetComment.setUser(localUser);
        expect(mockWidgetCommentRepository.get(VALID_COMMENT_ID)).andReturn(localWidgetComment).anyTimes();
        replay(mockWidgetCommentRepository);
        
        assertThat(defaultWidgetCommentPermissionEvaluator.hasPermission(mockAuthentication, localWidgetComment, Permission.CREATE), is(true));
        
        verify(mockAuthentication);
        verify(mockWidgetCommentRepository);
    }
}