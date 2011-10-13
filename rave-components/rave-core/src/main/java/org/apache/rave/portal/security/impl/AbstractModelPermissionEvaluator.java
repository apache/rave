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

import org.apache.rave.portal.security.ModelPermissionEvaluator;
import org.apache.rave.portal.security.util.AuthenticationUtils;
import org.springframework.security.core.Authentication;

/**
 * Abstract ModelPermissionEvaluator class that all model permission evaluators 
 * should extend from.  It defines a couple common functions who's logic is 
 * common across all ModelPermissionEvaluator implementations
 * 
 * @author carlucci
 */
public abstract class AbstractModelPermissionEvaluator<T> implements ModelPermissionEvaluator<T> {
        
    /**
     * The default hasPermission function implementation for 
     * ModelPermissionEvaluator classes.  It checks to see if the Authentication
     * supplied is an admin user to the system, which would trump all other
     * fine-grained permission checks.
     *      
     * @param authentication the Authentication object to check
     * @param targetDomainObject the targetDomainObject being checked (unused at this level)
     * @param permission the Permission (unused at this level)
     * @return true if the Authentication object is considered an admin, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, T targetDomainObject, Permission permission) {
        // check for admin role first as it will trump all other permission checks
        return AuthenticationUtils.isAdmin(authentication);
    }
    
    /**
     * Returns the load order of the implemented ModelPermissionEvaluator.  This
     * value is used by the RavePermissionEvaluator class when initializing 
     * the map of Model->ModelPermissionEvaluator objects to be used by Rave.
     * All of the default supplied ModelPermissionEvaluator classes will have a
     * value of 1.  This function can be overridden by anyone who wishes to create
     * their own ModelPermissionEvaluator implementations for specific domain
     * objects.  The overridden function should return a value greater than 1
     * so it is added to the map AFTER the default implementation, and thus
     * replacing it since they use the same key.
     * 
     * @return the default loadOrder which is 1
     */
    @Override
    public int getLoadOrder() {
        // all default RavePermissionEvaluators will have a load order of 1
        // implementers can override implementations by returning a load order
        // greater than 1
        return 1;
    }
}
