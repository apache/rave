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
import org.apache.rave.model.PageType;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.PageRepository;
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
 * @author carlucci
 */
public class DefaultPagePermissionEvaluatorTest {
    private DefaultPagePermissionEvaluator defaultPagePermissionEvaluator;
    private PageRepository mockPageRepository;
    private Authentication mockAuthentication;
    private Page page, personProfilePage, pageSubPage, personProfileSubPage;
    private UserImpl user, user2;
    private List<GrantedAuthority> grantedAuthoritiesList;

    private final String VALID_USER_ID = "99";
    private final String VALID_USER_ID2 = "66";
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";
    private final String VALID_PAGE_ID = "3";
    private final String VALID_PAGE_ID2 = "77";
    private final String VALID_PAGE_ID3 = "177";
    private final String VALID_PAGE_ID4 = "665";

    @Before
    public void setUp() {
        mockPageRepository = createMock(PageRepository.class);
        mockAuthentication = createMock(Authentication.class);

        defaultPagePermissionEvaluator = new DefaultPagePermissionEvaluator(mockPageRepository, null);

        user = new UserImpl();
        user.setUsername(VALID_USERNAME);
        user.setId(VALID_USER_ID);
        user2 = new UserImpl();
        user2.setUsername(VALID_USERNAME2);
        user2.setId(VALID_USER_ID2);

        page = new PageImpl();
        page.setId(VALID_PAGE_ID);
        page.setOwnerId(user.getId());
        page.setPageType(PageType.USER.toString());

        pageSubPage = new PageImpl();
        pageSubPage.setId(VALID_PAGE_ID4);
        pageSubPage.setOwnerId(user.getId());
        pageSubPage.setPageType(PageType.SUB_PAGE.toString());
        pageSubPage.setParentPage(page);

        personProfilePage = new PageImpl();
        personProfilePage.setId(VALID_PAGE_ID2);
        personProfilePage.setOwnerId(user.getId());
        personProfilePage.setPageType(PageType.PERSON_PROFILE.toString());
        personProfileSubPage = new PageImpl();
        personProfileSubPage.setId(VALID_PAGE_ID3);
        personProfileSubPage.setOwnerId(user.getId());
        personProfileSubPage.setPageType(PageType.PERSON_PROFILE.toString());
        personProfileSubPage.setParentPage(personProfilePage);

        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
    }


    @Test
    public void testGetType() throws ClassNotFoundException {
        assertThat(defaultPagePermissionEvaluator.getType().getName(), is(Page.class.getName()));
    }

    @Test
    public void testHasPermission_3args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {
        grantedAuthoritiesList.add(new SimpleGrantedAuthority(AuthenticationUtils.ROLE_ADMIN));

        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.ADMINISTER), is(true));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_3args_create_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_create_isNotPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_delete_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_delete_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_isPageOwner_userSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID4)).andReturn(pageSubPage);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, pageSubPage, Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_notPageOwner_pageSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID4)).andReturn(pageSubPage);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, pageSubPage, Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_notPageOwner_personProfilePage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, personProfilePage, Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_read_notPageOwner_personProfileSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, personProfileSubPage, Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_update_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_3args_update_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_administer() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.ADMINISTER), is(false));
        verify(mockAuthentication);
    }

    @Test
    public void testHasPermission_4args_create_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.CREATE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_create_isNotPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.CREATE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_delete_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.DELETE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_delete_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.DELETE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_isPageOwner_userSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID4)).andReturn(pageSubPage);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID4, Page.class.getName(), Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_notPageOwner_pageSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID4)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID4, Page.class.getName(), Permission.READ), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_notPageOwner_personProfilePage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockPageRepository.get(VALID_PAGE_ID2)).andReturn(personProfilePage);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID2, Page.class.getName(), Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_read_notPageOwner_personProfileSubPage() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockPageRepository.get(VALID_PAGE_ID3)).andReturn(personProfileSubPage);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID3, Page.class.getName(), Permission.READ), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_update_isPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.UPDATE), is(true));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_update_notPageOwner() {
        EasyMock.<Collection<? extends GrantedAuthority>>expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2).anyTimes();
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.UPDATE), is(false));
        verify(mockAuthentication);
        verify(mockPageRepository);
    }

    @Test
    public void testHasPermission_4args_update_isPageOwner_withRaveSecurityContextObject() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "org.apache.rave.model.User");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Page.class.getName(), Permission.UPDATE), is(true));
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isPageOwner_withInvalidRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "java.lang.String");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultPagePermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Page.class.getName(), Permission.UPDATE);
        verify(mockAuthentication);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_update_isPageOwner_withUnknownRaveSecurityContextType() {
        RaveSecurityContext raveSecurityContext = new RaveSecurityContext(VALID_USER_ID, "foo.bar.DummyClass");

        expect(mockAuthentication.getPrincipal()).andReturn(user);
        replay(mockAuthentication);
        defaultPagePermissionEvaluator.hasPermission(mockAuthentication, raveSecurityContext, Page.class.getName(), Permission.UPDATE);
        verify(mockAuthentication);
    }
}
