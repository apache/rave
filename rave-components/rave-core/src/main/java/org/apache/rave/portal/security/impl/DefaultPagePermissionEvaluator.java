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
import java.util.ArrayList;
import java.util.List;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.PageTypeRepository;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * The default implementation of the ModelPermissionEvaluator for Page objects
 * 
 * @author carlucci
 */
@Component
public class DefaultPagePermissionEvaluator extends AbstractModelPermissionEvaluator<Page> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private PageRepository pageRepository;
    
    private final String PERSON_PROFILE_PAGE_TYPE_CODE;
    private final String USER_PAGE_TYPE_CODE;
    
    @Autowired
    public DefaultPagePermissionEvaluator(PageRepository pageRepository, PageTypeRepository pageTypeRepository) {       
        this.pageRepository = pageRepository;

        PERSON_PROFILE_PAGE_TYPE_CODE = pageTypeRepository.getPersonProfilePageType().getCode();
        USER_PAGE_TYPE_CODE = pageTypeRepository.getUserPageType().getCode();
    }
   
    @Override
    public Class<Page> getType() {
        return Page.class;
    }
    
    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied Page object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the 
     * actual entity in the database.
     * 
     * @param authentication the current Authentication object
     * @param page the Page model object
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Page page, Permission permission) {      
        return hasPermission(authentication, page, permission, false);
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
            hasPermission = hasPermission(authentication, pageRepository.get((Long)targetId), permission, true);
        }
        return hasPermission;
    }  
        
    private boolean hasPermission(Authentication authentication, Page page, Permission permission, boolean trustedDomainObject) {       
        // this is our container of trusted page objects that can be re-used 
        // in this method so that the same trusted page object doesn't have to
        // be looked up in the repository multiple times
        List<Page> trustedPageContainer = new ArrayList<Page>();                           
        
        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic                
        if (super.hasPermission(authentication, page, permission)) {
            return true;
        }
        
        // perform the security logic depending on the Permission type
        boolean hasPermission = false;                       
        switch (permission) { 
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer pages              
                break;
            case CREATE:                                                                          
            case DELETE:            
            case UPDATE:
                // anyone can create, delete, or update a page that they own                  
                hasPermission = isPageOwner(authentication, page, trustedPageContainer, trustedDomainObject);     
                break;
            case READ:
                // anyone can read a USER page they own or anyone's PERSON_PROFILE page
                hasPermission = isReadablePage(authentication, page, trustedPageContainer, trustedDomainObject);
                break;
            default:
                log.warn("unknown permission: " + permission);
                break;
        }
        
        return hasPermission;
    }       
    
    // returns a trusted Page object, either from the PageRepository, or the
    // cached container list
    private Page getTrustedPage(long pageId, List<Page> trustedPageContainer) {       
        Page p = null;
        if (trustedPageContainer.isEmpty()) {           
            p = pageRepository.get(pageId);
            trustedPageContainer.add(p);
        } else {
            p = trustedPageContainer.get(0);
        }
        return p;       
    }     
   
    // checks to see if the Authentication object principal is the owner of the supplied page object 
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isPageOwner(Authentication authentication, Page page, List<Page> trustedPageContainer, boolean trustedDomainObject) {        
        Page trustedPage = null;
        if (trustedDomainObject) {
            trustedPage = page;
        } else {
            trustedPage = getTrustedPage(page.getEntityId(), trustedPageContainer);
        }                  
        
        return isPageOwnerByUsername(authentication, trustedPage.getOwner().getUsername());
    }             

    private boolean isPageOwnerByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }
    
    private boolean isPageOwnerById(Authentication authentication, Long userId) {
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
            return isPageOwnerById(authentication, (Long)raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }

    // anyone can read a USER page they own or anyone's PERSON_PROFILE page
    private boolean isReadablePage(Authentication authentication, Page page, List<Page> trustedPageContainer, boolean trustedDomainObject) {
        String pageTypeCode = (page.getPageType() == null) ? "" : page.getPageType().getCode();
        return PERSON_PROFILE_PAGE_TYPE_CODE.equals(pageTypeCode) ||
               isPageOwner(authentication, page, trustedPageContainer, trustedDomainObject);

    }
}