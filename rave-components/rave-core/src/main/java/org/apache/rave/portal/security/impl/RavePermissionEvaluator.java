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
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * Custom PermissionEvaluator for Rave that stores a map of ModelPermissionEvaluators
 * each of which is responsible for handling Domain Object Security for the Rave Model
 * objects
 *
 * @author carlucci
 */
@Component
public class RavePermissionEvaluator implements PermissionEvaluator {
    private Map<Class, ModelPermissionEvaluator<?>> modelPermissionEvaluatorMap;

    /**
     * Constructor which will take in a component-scanned list of all ModelPermissionEvaluator
     * classes found by Spring component scanner.  The constructor builds the
     * internal Map by using the Model type (Model Class) as the key, thus ensuring
     * only one ModelPermissionEvaluator class exists for each Model object.  The
     * constructor first sorts the injected list of ModelPermissionEvaluator objects
     * by the loadOrder field to allow overrides of the default ModelPermissionEvaluators.
     *
     * @param modelPermissionEvaluatorList autowired injected list of all ModelPermissionEvaluator classes found
     *                                     by the component scanner
     */
    @Autowired
    public RavePermissionEvaluator(List<ModelPermissionEvaluator<?>> modelPermissionEvaluatorList) {
        // order all of the component scanned ModelPermissionEvaluators by their loadOrder value
        // to allow overrides of the default ModelPermissionEvaluator implementations, since
        // we are storing them all in a map the higher order implementations will replace the
        // default lower ordered ones
        Collections.sort(modelPermissionEvaluatorList, new Comparator<ModelPermissionEvaluator>(){
            @Override
            public int compare(ModelPermissionEvaluator o1, ModelPermissionEvaluator o2) {
                return new Integer(o1.getLoadOrder()).compareTo(new Integer(o2.getLoadOrder()));
            }
        });

        // build the map using the model type/class as the key
        modelPermissionEvaluatorMap = new HashMap<Class, ModelPermissionEvaluator<?>>();
        for (ModelPermissionEvaluator<?> mpe : modelPermissionEvaluatorList) {
            modelPermissionEvaluatorMap.put(mpe.getType(), mpe);
        }
    }

    /**
     * Checks to see if the Authentication object has the supplied permission
     * on the supplied domain object
     *
     * @param authentication the Authentication object
     * @param targetDomainObject the domain object needing permission check
     * @param permissionString the permission to check
     * @return true if passes the permission check, false otherwise
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permissionString) {
        if (targetDomainObject == null) {
            return false;
        }
        // find the appropriate ModelPermissionEvaluator from the map based on
        // the targetDomainObject's class and invoke the hasPermission function
        return getEvaluator(targetDomainObject.getClass()).hasPermission(authentication, targetDomainObject,
                getPermission(targetDomainObject, (String) permissionString));
    }

    /**
     * Checks to see if the Authentication object has the supplied permission
     * on the supplied targetType (model class name) and targetId (entityId).
     * This method can be used when a permission check is needed and the method
     * does not currently have the domain object, only its entityId
     *
     * @param authentication the Authentication object
     * @param targetId the entityId of the targetType class
     * @param targetType the class name of the domain object
     * @param permissionString  permission the permission to check
     * @return true if passes the permission check, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permissionString) {
        // find the appropriate ModelPermissionEvaluator from the map based on
        // the targetType and invoke the hasPermission function
        Permission permission = Permission.fromString((String) permissionString);
        if (permission == Permission.CREATE_OR_UPDATE) {
            throw new IllegalArgumentException("CREATE_OR_UPDATE not supported in this context.");
        }

        // The targetType comes in as a String representing the Class (from the Spring annotations)
        // so we need to convert it to a Class
        Class clazz = null;
        try {
            clazz = Class.forName(targetType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class " + targetType + " not found", e);
        }

        return getEvaluator(clazz).hasPermission(authentication, targetId, targetType, permission);
    }

    private ModelPermissionEvaluator getEvaluator(Class targetType) throws IllegalArgumentException {
        ModelPermissionEvaluator mpe = modelPermissionEvaluatorMap.get(targetType);
        if (mpe == null) {
            // search for and register a compatible MPE
            mpe = findAndRegisterCompatibleModelPermissionEvaluator(targetType);
            // at this point, if we still haven't found a compatible MPE, throw exception
            if (mpe == null) {
                throw new IllegalArgumentException("ModelPermissionEvaluator not found for type " + targetType);
            }
        }
        return mpe;
    }

    private ModelPermissionEvaluator findAndRegisterCompatibleModelPermissionEvaluator(Class modelClass) {
        // look to see if this model class implements one of the types of the registered MPE's
        // and add an entry into the map for it.  This will allow, for example, a JpaPage class
        // to use the registered MPE for the Page interface
        for (Map.Entry<Class, ModelPermissionEvaluator<?>> classModelPermissionEvaluatorEntry : modelPermissionEvaluatorMap.entrySet()) {
            Class registeredModelClass = classModelPermissionEvaluatorEntry.getKey();
            ModelPermissionEvaluator<?> registeredMpe = classModelPermissionEvaluatorEntry.getValue();
            if (registeredModelClass.isAssignableFrom(modelClass)) {
                // register this new mapping of model class to mpe class
                modelPermissionEvaluatorMap.put(modelClass, registeredMpe);
                return registeredMpe;
            }
        }
        // we didn't find a compatible ModelPermissionEvaluator...
        return null;
    }

    private Permission getPermission(Object targetDomainObject, String permissionString) {
        Permission permission = Permission.fromString((String) permissionString);
  /*      if (permission.equals(Permission.CREATE_OR_UPDATE)) {
            if (targetDomainObject instanceof BasicEntity) {
                Long id = ((BasicEntity) targetDomainObject).getEntityId();
                if (id == null) {
                    permission = Permission.CREATE;
                } else {
                    permission = Permission.UPDATE;
                }
            } else {
                throw new IllegalArgumentException("CREATE_OR_UPDATE is currently only supported for BasicEntity types");
            }
        }*/
        return permission;
    }
}
