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

package org.apache.rave.portal.web.api.rest;

import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ClassUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author carlucci
 */
public class AbstractRestApiTest {
    private AbstractRestApiImpl abstractRestApiImpl;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
       
    
    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        abstractRestApiImpl = new AbstractRestApiImpl();
    }
       
    @Test
    public void testHandleException() {
        RuntimeException re = new RuntimeException("error");        
        String value = abstractRestApiImpl.handleException(re, request, response);
        
        assertThat(value, is(ClassUtils.getShortName(re.getClass())));
        assertThat(response.getStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));   
    }
    
    @Test
    public void testHandleAccessDeniedException() {
        Principal principal = createMock(Principal.class);                
        request.setUserPrincipal(principal);
        AccessDeniedException ade = new AccessDeniedException("error");        
        
        expect(principal.getName()).andReturn("theuser");
        replay(principal);        
        abstractRestApiImpl.handleAccessDeniedException(ade, request, response);        
        assertThat(response.getStatus(), is(HttpStatus.FORBIDDEN.value()));   
        verify(principal);
    }    

    class AbstractRestApiImpl extends AbstractRestApi { }
}
