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

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageUser;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRegionPermissionEvaluator extends AbstractModelPermissionEvaluator<Region>{
    private Logger log = LoggerFactory.getLogger(getClass());
    private RegionRepository regionRepository;

    @Autowired
    public DefaultRegionPermissionEvaluator(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Class<Region> getType() {
        return Region.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied Region object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the
     * actual entity in the database.
     *
     * @param authentication the current Authentication object
     * @param region   the Region model object
     * @param permission     the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Region region, Permission permission) {
        return hasPermission(authentication, region, permission, false);
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
     * @param targetId       the entityId of the model to check, or a RaveSecurityContext object
     * @param targetType     the class of the model to check
     * @param permission     the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
        boolean hasPermission = false;
        if (targetId instanceof RaveSecurityContext) {
            hasPermission = verifyRaveSecurityContext(authentication, (RaveSecurityContext) targetId);
        } else {
            hasPermission = hasPermission(authentication, regionRepository.get((Long) targetId), permission, true);
        }
        return hasPermission;
    }

    private boolean hasPermission(Authentication authentication, Region region, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted region objects that can be re-used
        // in this method so that the same trusted region object doesn't have to
        // be looked up in the repository multiple times
        List<Region> trustedRegionContainer = new ArrayList<Region>();

        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic
        if (super.hasPermission(authentication, region, permission)) {
            return true;
        }

        // perform the security logic depending on the Permission type
        boolean hasPermission = false;

        switch (permission) {
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer Region
                break;
            case CREATE:
            case DELETE:
            case UPDATE:
                // anyone can create, delete, read, or update a region that they own
                hasPermission = isRegionOwner(authentication, region, trustedRegionContainer, trustedDomainObject)
                || isRegionMember(authentication, region, trustedRegionContainer, trustedDomainObject, true);
                break;
            case READ:
                hasPermission = isRegionOwner(authentication, region, trustedRegionContainer, trustedDomainObject)
                || isRegionMember(authentication, region, trustedRegionContainer, trustedDomainObject, false);
            default:
                log.warn("unknown permission: " + permission);
                break;
        }

        return hasPermission;
    }

    // returns a trusted Region object, either from the RegionRepository, or the
    // cached container list
    private Region getTrustedRegion(long regionId, List<Region> trustedRegionContainer) {
        Region region = null;
        if (trustedRegionContainer.isEmpty()) {
            region = regionRepository.get(regionId);
            trustedRegionContainer.add(region);
        } else {
            region = trustedRegionContainer.get(0);
        }
        return region;
    }

    // checks to see if the Authentication object principal is the owner of the supplied region object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isRegionOwner(Authentication authentication, Region region, List<Region> trustedRegionContainer, boolean trustedDomainObject) {
        Region trustedRegion = null;
        if (trustedDomainObject) {
            trustedRegion = region;
        } else {
            trustedRegion = getTrustedRegion(region.getEntityId(), trustedRegionContainer);
        }
        return isRegionOwnerByUsername(authentication, trustedRegion.getPage().getOwner().getUsername());
    }

    private boolean isRegionOwnerByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }

    private boolean isRegionOwnerById(Authentication authentication, Long userId) {
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
            return isRegionOwnerById(authentication, (Long) raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }
    
    private boolean isRegionMember(Authentication authentication, Region region, List<Region> trustedRegionContainer, boolean trustedDomainObject, boolean checkEditorStatus) {
        Region trustedRegion = null;
        if (trustedDomainObject) {
            trustedRegion = region;
        } else {
            trustedRegion = getTrustedRegion(region.getEntityId(), trustedRegionContainer);
        }
        
        Page containerPage = trustedRegion.getPage();
        
        
        if (containerPage.getMembers() == null){
            return false;
        }
        //
        // Check that the viewer is a member
        //
        String viewer = ((User)authentication.getPrincipal()).getUsername();
        for (PageUser pageUser:containerPage.getMembers()){
            if (pageUser.getUser().getUsername().equals(viewer)){
                log.info("User "+viewer+" is a member of page "+containerPage.getEntityId());
                if(checkEditorStatus){
                    return pageUser.isEditor();
                }
                return true;
            }
        }
        return false;
    }

}