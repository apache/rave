/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.web.controller.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller which processes openid authentication failure. If the user record for particular
 * openid user is not found in the DB, it redirects to the account creation page. Otherwise
 * it redirects to the default authentication failure page.
 * 
 */
@Controller
public class OpenIDAuthenticationFailureHandler extends	SimpleUrlAuthenticationFailureHandler {

	private static Logger log = LoggerFactory.getLogger(OpenIDAuthenticationFailureHandler.class);

	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		if(exception instanceof UsernameNotFoundException
			&& exception.getAuthentication() instanceof OpenIDAuthenticationToken
            && ((OpenIDAuthenticationToken)exception.getAuthentication()).getStatus().equals(OpenIDAuthenticationStatus.SUCCESS)) {
			
			OpenIDAuthenticationToken token = (OpenIDAuthenticationToken)exception.getAuthentication();
			String url = token.getIdentityUrl();
			User user = createTemporaryUser(token, url);
			request.getSession(true).setAttribute(ModelKeys.NEW_USER, user);

			DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
			log.info("Redirecting to new user account creation page");
			super.setRedirectStrategy(redirectStrategy);
			redirectStrategy.sendRedirect(request, response, "/"+ViewNames.CREATE_ACCOUNT_PAGE);
			return;
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}

	private User createTemporaryUser(OpenIDAuthenticationToken token,
			final String openId) {
		final List<OpenIDAttribute> attributes = token.getAttributes();
		String email = null;
		String firstName = null;
		String lastName = null;
		String displayName = null;
		for (OpenIDAttribute attribute : attributes) {
			if ("email".equals(attribute.getName())
					&& !attribute.getValues().isEmpty()) {
				email = attribute.getValues().get(0);
			} else if ("firstname".equals(attribute.getName())
					&& !attribute.getValues().isEmpty()) {
				firstName = attribute.getValues().get(0);
			} else if ("lastname".equals(attribute.getName())
					&& !attribute.getValues().isEmpty()) {
				lastName = attribute.getValues().get(0);
			} else if ("fullname".equals(attribute.getName())
					&& !attribute.getValues().isEmpty()) {
				displayName = attribute.getValues().get(0);
			}
		}
		User user = new UserImpl();
		String username = StringUtils.substringAfter(openId, "://").replace("/", "");
		if (username.length() > 35) {
			username = username.substring(0, 35);
		}
		if (displayName == null && firstName != null && lastName != null) {
			displayName = firstName + " " + lastName;
		}
		user.setUsername(username);
		user.setEmail(email);
		user.setGivenName(firstName);
		user.setFamilyName(lastName);
		user.setDisplayName(displayName);
		user.setOpenId(openId);

		return user;
	}
}
