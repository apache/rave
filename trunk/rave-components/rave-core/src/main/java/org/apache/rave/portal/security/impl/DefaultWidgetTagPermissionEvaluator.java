/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.security.impl;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Component
public class DefaultWidgetTagPermissionEvaluator extends AbstractModelPermissionEvaluator<WidgetTag> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private WidgetTagRepository widgetTagRepository;

    @Autowired
    public DefaultWidgetTagPermissionEvaluator(WidgetTagRepository widgetTagRepository) {
        this.widgetTagRepository = widgetTagRepository;
    }
    
    @Override
    public Class<WidgetTag> getType() {
        return WidgetTag.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied WidgetTag object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the 
     * actual entity in the database.
     * 
     * @param authentication the current Authentication object
     * @param widgetTag the WidgetTag model object
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, WidgetTag widgetTag, Permission permission) {
        return hasPermission(authentication, widgetTag, permission, false);
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
            hasPermission = verifyRaveSecurityContext(authentication, (RaveSecurityContext)targetId, permission);           
        } else {
            hasPermission = hasPermission(authentication, widgetTagRepository.get((Long)targetId), permission, true);
        }
        return hasPermission;
    }  
    
    private boolean hasPermission(Authentication authentication, WidgetTag widgetTag, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted WidgetTag objects that can be re-used
        // in this method so that the same trusted WidgetTag object doesn't have to
        // be looked up in the repository multiple times
        List<WidgetTag> trustedWidgetTagContainer = new ArrayList<WidgetTag>();
        
        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic                
        if (super.hasPermission(authentication, widgetTag, permission)) {
            return true;
        }
        
        // perform the security logic depending on the Permission type
        boolean hasPermission = false;                       
        switch (permission) { 
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer WidgetTags
                break;
            case READ:
                hasPermission =  true;
                break;
            case CREATE:
                hasPermission = isWidgetTagOwnerById(authentication, widgetTag.getUser().getEntityId());
                break;
            case DELETE:
            case UPDATE:
                // anyone can create, delete, read, or update a WidgetTag that they own
                hasPermission = isWidgetTagOwner(authentication, widgetTag, trustedWidgetTagContainer, trustedDomainObject);
                break;   
            default:
                log.warn("unknown permission: " + permission);
                break;
        }
        
        return hasPermission;
    }
    
    private boolean verifyRaveSecurityContext(Authentication authentication, RaveSecurityContext raveSecurityContext, Permission permission) {
        Class<?> clazz = null;
        try {
           clazz = Class.forName(raveSecurityContext.getType());
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("unknown class specified in RaveSecurityContext: ", ex);
        }

        // perform the permissions check based on the class supplied to the RaveSecurityContext object
        if (WidgetTag.class == clazz) {
            // perform the security logic depending on the Permission type
            boolean hasPermission = false;
            switch (permission) {
                case ADMINISTER:
                    // if you are here, you are not an administrator, so you can't administer WidgetTags
                    break;
                case READ:
                    hasPermission = true;
                    break;
                case CREATE:
                case DELETE:
                case UPDATE:
                    // anyone can create, delete, read, or update a WidgetTag that they own
                    hasPermission = isWidgetTagOwnerById(authentication, (Long)raveSecurityContext.getId());
                    break;
                default:
                    log.warn("unknown permission: " + permission);
                    break;
            }

            return hasPermission;
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    } 
    
    
    // checks to see if the Authentication object principal is the owner of the supplied WidgetTag object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isWidgetTagOwner(Authentication authentication, WidgetTag widgetTag, List<WidgetTag> trustedWidgetTagContainer, boolean trustedDomainObject) {
        WidgetTag trustedWidgetTag = null;
        if (trustedDomainObject) {
            trustedWidgetTag = widgetTag;
        } else {
            trustedWidgetTag = getTrustedWidgetTag(widgetTag.getEntityId(), trustedWidgetTagContainer);
        }                  
        
        return isWidgetTagOwnerByUsername(authentication, trustedWidgetTag.getUser().getUsername());
    }            
    
    // returns a trusted WidgetTag object, either from the WidgetTagRepository, or the
    // cached container list
    private WidgetTag getTrustedWidgetTag(long widgetTagId, List<WidgetTag> trustedWidgetTagContainer) {
        WidgetTag p = null;
        if (trustedWidgetTagContainer.isEmpty()) {
            p = widgetTagRepository.get(widgetTagId);
            trustedWidgetTagContainer.add(p);
        } else {
            p = trustedWidgetTagContainer.get(0);
        }
        return p;       
    }     

    private boolean isWidgetTagOwnerByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }
    private boolean isWidgetTagOwnerById(Authentication authentication, Long userId) {
        return ((User)authentication.getPrincipal()).getEntityId().equals(userId);
    }
}
