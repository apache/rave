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
package org.apache.rave.portal.security.util;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Utility function to hold common Authentication related helper functions
 * 
 * @author carlucci
 */
public class AuthenticationUtils {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    /**
     * Checks to see if an Authentication object has a given role
     * 
     * @param authentication the Authentication object containing a list of 
     *                       GrantedAuthority objects of which to check against
     * @param role the role to check
     * @return true if the role is found, false otherwise
     */
    public static boolean hasRole(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        if (grantedAuthorities == null || role == null || role.isEmpty()) {
            return false;
        }

        for (GrantedAuthority auth : grantedAuthorities) {
            if (role.equalsIgnoreCase(auth.getAuthority())) {           
                return true;
            }
        }

        return false;
    }
    
    /**
     * Checks to see if the user has the super user admin role
     * 
     * @param authentication the Authentication object containing a list of 
     *                       GrantedAuthority objects of which to check against
     * @return true if the admin role is found, false otherwise
     */
    public static boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, ROLE_ADMIN);
    }
}
