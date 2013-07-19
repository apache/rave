#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package ${package};

import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.portal.repository.PersonRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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

/**
 * Test class for {@link CustomUserService}
 */
public class CustomUserServiceTest {

    private UserRepository repository;
    private CustomUserService userService;

    @Before
    public void setUp() throws Exception {
        repository = createMock(UserRepository.class);
        userService = new CustomUserService(repository,
                createMock(PageRepository.class),
                createMock(WidgetRepository.class),
                createMock(PageTemplateRepository.class),
                createMock(CategoryRepository.class),
                createMock(PersonRepository.class));
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        final String username = "canonical";
        User user = new UserImpl("1", username);
        expect(repository.getByUsername(username)).andReturn(user);
        replay(repository);

        UserDetails returnedUser = userService.loadUserByUsername(username);
        verify(repository);
        assertEquals(returnedUser, user);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_fail() throws Exception {
        final String username = "missing";

        expect(repository.getByUsername(username)).andReturn(null);
        expectLastCall();
        replay(repository);

        userService.loadUserByUsername(username);

        assertFalse("Method should throw Exception", true);
    }

    @Test
    public void testGetAuthenticatedUser() throws Exception {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new UserImpl("1", "canonical");
        Authentication authentication = new TestingAuthenticationToken(userDetails, "canonical", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User returnedUser = userService.getAuthenticatedUser();
        assertEquals(returnedUser, userDetails);
    }

    @Test(expected = SecurityException.class)
    public void testGetAuthenticatedUser_fail() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(null);

        userService.getAuthenticatedUser();

        assertFalse("Method should throw Exception", true);
    }
}
