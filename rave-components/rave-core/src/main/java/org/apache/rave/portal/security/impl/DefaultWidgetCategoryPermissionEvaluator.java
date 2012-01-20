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

import org.apache.rave.portal.model.WidgetCategory;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.WidgetCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of the ModelPermissionEvaluator for WidgetCategory objects
 */
@Component
public class DefaultWidgetCategoryPermissionEvaluator extends AbstractModelPermissionEvaluator<WidgetCategory> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private WidgetCategoryRepository widgetCategoryRepository;

    @Autowired
    public DefaultWidgetCategoryPermissionEvaluator(WidgetCategoryRepository widgetCategoryRepository) {
        this.widgetCategoryRepository = widgetCategoryRepository;
    }
   
    @Override
    public Class<WidgetCategory> getType() {
        return WidgetCategory.class;
    }
    
    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied WidgetCategory object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the 
     * actual entity in the database.
     * 
     * @param authentication the current Authentication object
     * @param widgetCategory the WidgetCategory model object
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, WidgetCategory widgetCategory, Permission permission) {      
        return hasPermission(authentication, widgetCategory, permission, false);
    }    

    /**
     * Checks to see if the Authentication object has the supplied Permission 
     * for the Entity represented by the targetId(entityId) and targetType(model class name).
     * This method invokes the private hasPermission function with the 
     * trustedDomainObject parameter set to true since we must pull the entity
     * from the database and are guaranteed a trusted domain object,
     * before performing our permission checks.
     * 
     * @param authentication the current Authentication object
     * @param targetId the entityId of the model to check, or a RaveSecurityContext object
     * @param targetType the class of the model to check
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
        boolean hasPermission = false;
        if (targetId instanceof RaveSecurityContext) {
            hasPermission = verifyRaveSecurityContext(authentication, (RaveSecurityContext)targetId);           
        } else {
            hasPermission = hasPermission(authentication, widgetCategoryRepository.get((Long)targetId), permission, true);
        }
        return hasPermission;
    }  
        
    private boolean hasPermission(Authentication authentication, WidgetCategory widgetCategory, Permission permission, boolean trustedDomainObject) {       
        // this is our container of trusted widgetCategory objects that can be re-used 
        // in this method so that the same trusted widgetCategory object doesn't have to
        // be looked up in the repository multiple times
        List<WidgetCategory> trustedWidgetCategoryContainer = new ArrayList<WidgetCategory>();                           
        
        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic                
        if (super.hasPermission(authentication, widgetCategory, permission)) {
            return true;
        }
        
        // perform the security logic depending on the Permission type
        boolean hasPermission = false;                       
        switch (permission) {
            case READ:
                // all users can read any WidgetCategory
                hasPermission = true;
                break;
            // if you are here, you are not an administrator, and thus can't
            // administer, create, update, or delete a WidgetCategory
            case ADMINISTER:
            case CREATE:
            case DELETE:
            case UPDATE:
                break;
            default:
                log.warn("unknown permission: " + permission);
                break;
        }
        
        return hasPermission;
    }       
    
    // returns a trusted WidgetCategory object, either from the WidgetCategoryRepository, or the
    // cached container list
    private WidgetCategory getTrustedWidgetCategory(long widgetCategoryId, List<WidgetCategory> trustedWidgetCategoryContainer) {
        WidgetCategory p = null;
        if (trustedWidgetCategoryContainer.isEmpty()) {
            p = widgetCategoryRepository.get(widgetCategoryId);
            trustedWidgetCategoryContainer.add(p);
        } else {
            p = trustedWidgetCategoryContainer.get(0);
        }
        return p;
    }
   
    // checks to see if the Authentication object principal is the owner of the supplied widgetCategory object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isWidgetCategoryCreatedUser(Authentication authentication, WidgetCategory widgetCategory, List<WidgetCategory> trustedWidgetCategoryContainer, boolean trustedDomainObject) {
        WidgetCategory trustedWidgetCategory = null;
        if (trustedDomainObject) {
            trustedWidgetCategory = widgetCategory;
        } else {
            trustedWidgetCategory = getTrustedWidgetCategory(widgetCategory.getEntityId(), trustedWidgetCategoryContainer);
        }

        return isWidgetCategoryCreatedUserByUsername(authentication, trustedWidgetCategory.getCreatedUser().getUsername());
    }

    private boolean isWidgetCategoryCreatedUserByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }

    private boolean isWidgetCategoryCreatedUserById(Authentication authentication, Long userId) {
        return ((User)authentication.getPrincipal()).getEntityId().equals(userId);
    }

    private boolean verifyRaveSecurityContext(Authentication authentication, RaveSecurityContext raveSecurityContext) {
        Class<?> clazz = null;
        try {
           clazz = Class.forName(raveSecurityContext.getType());
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("unknown class specified in RaveSecurityContext: ", ex);
        }

        // perform the permissions check based on the class supplied to the RaveSecurityContext object
        if (User.class == clazz) {
            return isWidgetCategoryCreatedUserById(authentication, (Long) raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }
}