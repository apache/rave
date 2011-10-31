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

package org.apache.rave.portal.security;

import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 *
 * @author carlucci
 */
public interface ModelPermissionEvaluator<T> {
    
    /**
     * An enum representing all of the possible permissions a user can
     * have on a Model object
     */
    public static enum Permission {
        ADMINISTER,        
        CREATE,        
        DELETE,
        READ,
        UPDATE,
        CREATE_OR_UPDATE;

        /**
         * Returns the equivalent Permission enum from the supplied string
         * 
         * @param value string representing the enum to return
         * @return the enum value
         */
        public static Permission fromString(String value) {
            return Permission.valueOf(value.toUpperCase());
        }
    }
    
    Class<T> getType();
    boolean hasPermission(Authentication authentication, T targetDomainObject, Permission permission);
    boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission);
    int getLoadOrder();
}
