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

import org.apache.rave.model.Page;
import org.apache.rave.model.PageUser;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.User;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRegionWidgetPermissionEvaluator extends AbstractModelPermissionEvaluator<RegionWidget> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private RegionWidgetRepository regionWidgetRepository;
    private UserRepository userRepository;

    @Autowired
    public DefaultRegionWidgetPermissionEvaluator(RegionWidgetRepository regionWidgetRepository, UserRepository userRepository) {
        this.regionWidgetRepository = regionWidgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Class<RegionWidget> getType() {
        return RegionWidget.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied RegionWidget object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the
     * actual entity in the database.
     *
     * @param authentication the current Authentication object
     * @param regionWidget   the RegionWidget model object
     * @param permission     the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, RegionWidget regionWidget, Permission permission) {
        return hasPermission(authentication, regionWidget, permission, false);
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
            hasPermission = hasPermission(authentication, regionWidgetRepository.get((String) targetId), permission, true);
        }
        return hasPermission;
    }

    private boolean hasPermission(Authentication authentication, RegionWidget regionWidget, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted regionWidget objects that can be re-used
        // in this method so that the same trusted regionWidget object doesn't have to
        // be looked up in the repository multiple times
        List<RegionWidget> trustedRegionWidgetContainer = new ArrayList<RegionWidget>();

        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic
        if (super.hasPermission(authentication, regionWidget, permission)) {
            return true;
        }

        // perform the security logic depending on the Permission type
        boolean hasPermission = false;

        switch (permission) {
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer RegionWidgets
                break;
            case CREATE:
            case DELETE:
            case UPDATE:
                // anyone can create, delete, read, or update a regionWidget that they own
                hasPermission = isRegionWidgetOwner(authentication, regionWidget, trustedRegionWidgetContainer, trustedDomainObject)
                || isRegionWidgetMember(authentication, regionWidget, trustedRegionWidgetContainer, trustedDomainObject, true);
                break;
            case READ:
                hasPermission = isRegionWidgetOwner(authentication, regionWidget, trustedRegionWidgetContainer, trustedDomainObject)
                || isRegionWidgetMember(authentication, regionWidget, trustedRegionWidgetContainer, trustedDomainObject, false);
            default:
                log.warn("unknown permission: " + permission);
                break;
        }

        return hasPermission;
    }

    // returns a trusted RegionWidget object, either from the RegionWidgetRepository, or the
    // cached container list
    private RegionWidget getTrustedRegionWidget(String regionWidgetId, List<RegionWidget> trustedRegionWidgetContainer) {
        RegionWidget regionWidget = null;
        if (trustedRegionWidgetContainer.isEmpty()) {
            regionWidget = regionWidgetRepository.get(regionWidgetId);
            trustedRegionWidgetContainer.add(regionWidget);
        } else {
            regionWidget = trustedRegionWidgetContainer.get(0);
        }
        return regionWidget;
    }

    // checks to see if the Authentication object principal is the owner of the supplied regionWidget object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isRegionWidgetOwner(Authentication authentication, RegionWidget regionWidget, List<RegionWidget> trustedRegionWidgetContainer, boolean trustedDomainObject) {
        RegionWidget trustedRegionWidget = null;
        if (trustedDomainObject) {
            trustedRegionWidget = regionWidget;
        } else {
            trustedRegionWidget = getTrustedRegionWidget(regionWidget.getId(), trustedRegionWidgetContainer);
        }
        return isRegionWidgetOwnerByUsername(authentication, getUserIdFromRegionWidget(trustedRegionWidget));
    }

    private boolean isRegionWidgetOwnerByUsername(Authentication authentication, String userId) {
        return ((User)authentication.getPrincipal()).getId().equals(userId);
    }

    private boolean isRegionWidgetOwnerById(Authentication authentication, String userId) {
        return ((User)authentication.getPrincipal()).getId().equals(userId);
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
            return isRegionWidgetOwnerById(authentication, (String) raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }

    private String getUserIdFromRegionWidget(RegionWidget regionWidget) {
        return regionWidget.getRegion().getPage().getOwnerId();
    }

    private boolean isRegionWidgetMember(Authentication authentication,
            RegionWidget regionWidget, List<RegionWidget> trustedRegionWidgetContainer, boolean trustedDomainObject, boolean checkEditorStatus) {
        RegionWidget trustedRegionWidget = null;
        if (trustedDomainObject) {
            trustedRegionWidget = regionWidget;
        } else {
            trustedRegionWidget = getTrustedRegionWidget(regionWidget.getId(), trustedRegionWidgetContainer);
        }

        Page containerPage = trustedRegionWidget.getRegion().getPage();


        if (containerPage.getMembers() == null){
            return false;
        }
        //
        // Check that the viewer is a member
        //
        String viewer = ((User)authentication.getPrincipal()).getUsername();
        for (PageUser pageUser:containerPage.getMembers()){
            if (userRepository.get(pageUser.getUserId()).getUsername().equals(viewer)){
                log.info("User "+viewer+" is a member of page "+containerPage.getId());
                if(checkEditorStatus){
                    return pageUser.isEditor();
                }
                return true;
            }
        }
        return false;
    }
}
