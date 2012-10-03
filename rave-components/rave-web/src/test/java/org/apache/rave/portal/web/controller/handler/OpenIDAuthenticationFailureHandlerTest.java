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

package org.apache.rave.portal.web.controller.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;

public class OpenIDAuthenticationFailureHandlerTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private OpenIDAuthenticationFailureHandler handler;
    private OpenIDAuthenticationToken postAuthToken;
    private AuthenticationException authException;

    private final String MESSAGE = "Successfully authenticated";
    private final String NON_REGISTERED_OPENID_USER = "http://someone.myopenid.com/";
    private final String REDIRECT_URL = "/app/openidregister";
    
    private final Logger logger = LoggerFactory.getLogger(OpenIDAuthenticationFailureHandlerTest.class);
    

    @Before
    public void setup() {        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new OpenIDAuthenticationFailureHandler();
        postAuthToken = new OpenIDAuthenticationToken(OpenIDAuthenticationStatus.SUCCESS,NON_REGISTERED_OPENID_USER, 
        		MESSAGE, new ArrayList<OpenIDAttribute>());
        authException = new UsernameNotFoundException("");
}

    
    @SuppressWarnings("deprecation")
	@Test
    public void authenticationFailureRedirectTest() {        
        try {
        	authException.setAuthentication(postAuthToken);
			handler.onAuthenticationFailure(request, response, authException);
		} catch (IOException e) {
			logger.debug("IOException on Authentication Failure");
		} catch (ServletException e) {
			logger.debug("ServletException on Authentication Failure");
		}
        assertTrue(authException.getAuthentication() instanceof OpenIDAuthenticationToken);
        assertTrue(((OpenIDAuthenticationToken)authException.getAuthentication()).getStatus().equals(OpenIDAuthenticationStatus.SUCCESS));
        assertEquals(((OpenIDAuthenticationToken)authException.getAuthentication()).getMessage(), MESSAGE);
        assertEquals(response.getRedirectedUrl(),REDIRECT_URL);
    }
}