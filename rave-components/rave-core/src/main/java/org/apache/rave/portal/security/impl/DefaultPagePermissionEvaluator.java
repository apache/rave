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

import java.io.Serializable;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * The default implementation of the ModelPermissionEvaluator for Page objects
 * 
 * NOTE: this is temporarily a stub placeholder to allow the security framework 
 * code to be checked in and not break the autowiring code
 * 
 * TODO: implement this class
 * 
 * @author carlucci
 */
@Component
public class DefaultPagePermissionEvaluator extends AbstractModelPermissionEvaluator<Page> {

    @Override
    public Class<Page> getType() {
        return Page.class;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Page page, Permission permission) {       
        return true;
    }    

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
        return true;
    }
    
}
