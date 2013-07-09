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

import org.apache.rave.model.User;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.portal.repository.WidgetRepository;
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
public class DefaultWidgetCommentPermissionEvaluator extends AbstractModelPermissionEvaluator<WidgetComment> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private WidgetRepository widgetRepository;

    @Autowired
    public DefaultWidgetCommentPermissionEvaluator(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Class<WidgetComment> getType() {
        return WidgetComment.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied Page object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the
     * actual entity in the database.
     *
     * @param authentication the current Authentication object
     * @param widgetComment the WidgetComment model object
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, WidgetComment widgetComment, Permission permission) {
        return hasPermission(authentication, widgetComment, permission, false);
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
            hasPermission = hasPermission(authentication, widgetRepository.getCommentById(null, (String)targetId), permission, true);
        }
        return hasPermission;
    }

    private boolean hasPermission(Authentication authentication, WidgetComment widgetComment, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted page objects that can be re-used
        // in this method so that the same trusted page object doesn't have to
        // be looked up in the repository multiple times
        List<WidgetComment> trustedWidgetCommentContainer = new ArrayList<WidgetComment>();

        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic
        if (super.hasPermission(authentication, widgetComment, permission)) {
            return true;
        }

        // perform the security logic depending on the Permission type
        boolean hasPermission = false;
        switch (permission) {
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer pages
                break;
            case READ:
                hasPermission =  true;
                break;
            case CREATE:
                hasPermission = true;
                break;
            case CREATE_OR_UPDATE:
            case DELETE:
            case UPDATE:
                // anyone can create, delete, read, or update a page that they own
                hasPermission = isWidgetCommentOwner(authentication, widgetComment, trustedWidgetCommentContainer, trustedDomainObject);
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
        if (WidgetComment.class == clazz) {
            // perform the security logic depending on the Permission type
            boolean hasPermission = false;
            switch (permission) {
                case ADMINISTER:
                    // if you are here, you are not an administrator, so you can't administer pages
                    break;
                case READ:
                    hasPermission = true;
                    break;
                case CREATE:
                case DELETE:
                case UPDATE:
                case CREATE_OR_UPDATE:
                    // anyone can create, delete, read, or update a page that they own
                    hasPermission = isWidgetCommentOwnerById(authentication, (String)raveSecurityContext.getId());
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

    // checks to see if the Authentication object principal is the owner of the supplied widgetComment object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isWidgetCommentOwner(Authentication authentication, WidgetComment widgetComment, List<WidgetComment> trustedPageContainer, boolean trustedDomainObject) {
        WidgetComment trustedWidgetComment = null;
        if (trustedDomainObject) {
            trustedWidgetComment = widgetComment;
        } else {
            trustedWidgetComment = getTrustedWidgetComment(widgetComment.getId(), trustedPageContainer);
        }

        return isWidgetCommentOwnerById(authentication, trustedWidgetComment.getUserId());
    }

    // returns a trusted Page object, either from the PageRepository, or the
    // cached container list
    private WidgetComment getTrustedWidgetComment(String widgetCommentId, List<WidgetComment> trustedWidgetCommentContainer) {
        WidgetComment p = null;
        if (trustedWidgetCommentContainer.isEmpty()) {
            p = widgetRepository.getCommentById(null, widgetCommentId);
            trustedWidgetCommentContainer.add(p);
        } else {
            p = trustedWidgetCommentContainer.get(0);
        }
        return p;
    }

    private boolean isWidgetCommentOwnerByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }
    private boolean isWidgetCommentOwnerById(Authentication authentication, String userId) {
        return ((User)authentication.getPrincipal()).getId().equals(userId);
    }
}
