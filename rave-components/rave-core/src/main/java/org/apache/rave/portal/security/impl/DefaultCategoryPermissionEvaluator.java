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

import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.portal.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of the ModelPermissionEvaluator for Category objects
 */
@Component
public class DefaultCategoryPermissionEvaluator extends AbstractModelPermissionEvaluator<Category> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private CategoryRepository categoryRepository;

    @Autowired
    public DefaultCategoryPermissionEvaluator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Class<Category> getType() {
        return Category.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied Category object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the
     * actual entity in the database.
     *
     * @param authentication the current Authentication object
     * @param category the Category model object
     * @param permission the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Category category, Permission permission) {
        return hasPermission(authentication, category, permission, false);
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
            hasPermission = hasPermission(authentication, categoryRepository.get((String)targetId), permission, true);
        }
        return hasPermission;
    }

    private boolean hasPermission(Authentication authentication, Category category, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted category objects that can be re-used
        // in this method so that the same trusted category object doesn't have to
        // be looked up in the repository multiple times
        List<Category> trustedCategoryContainer = new ArrayList<Category>();

        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic
        if (super.hasPermission(authentication, category, permission)) {
            return true;
        }

        // perform the security logic depending on the Permission type
        boolean hasPermission = false;
        switch (permission) {
            case READ:
                // all users can read any Category
                hasPermission = true;
                break;
            // if you are here, you are not an administrator, and thus can't
            // administer, create, update, or delete a Category
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

    // returns a trusted Category object, either from the CategoryRepository, or the
    // cached container list
    private Category getTrustedCategory(String categoryId, List<Category> trustedCategoryContainer) {
        Category p = null;
        if (trustedCategoryContainer.isEmpty()) {
            p = categoryRepository.get(categoryId);
            trustedCategoryContainer.add(p);
        } else {
            p = trustedCategoryContainer.get(0);
        }
        return p;
    }

    // checks to see if the Authentication object principal is the owner of the supplied category object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isCategoryCreatedUser(Authentication authentication, Category category, List<Category> trustedCategoryContainer, boolean trustedDomainObject) {
        Category trustedCategory = null;
        if (trustedDomainObject) {
            trustedCategory = category;
        } else {
            trustedCategory = getTrustedCategory(category.getId(), trustedCategoryContainer);
        }

        return isCategoryCreatedUserByUsername(authentication, trustedCategory.getCreatedUserId());
    }

    private boolean isCategoryCreatedUserByUsername(Authentication authentication, String id) {
        return ((User)authentication.getPrincipal()).getId().equals(id);
    }

    private boolean isCategoryCreatedUserById(Authentication authentication, String userId) {
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
            return isCategoryCreatedUserById(authentication, (String) raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }
}