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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.rave.portal.security.ModelPermissionEvaluator;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Custom PermissionEvaluator for Rave that stores a map of ModelPermissionEvaluators
 * each of which is responsible for handling Domain Object Security for the Rave Model
 * objects
 * 
 * @author carlucci
 */
@Component
public class RavePermissionEvaluator implements PermissionEvaluator {
    private Map<String, ModelPermissionEvaluator> modelPermissionEvaluatorMap;
    
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
    public RavePermissionEvaluator(List<ModelPermissionEvaluator> modelPermissionEvaluatorList) {
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
        modelPermissionEvaluatorMap = new HashMap<String, ModelPermissionEvaluator>();
        for (ModelPermissionEvaluator mpe : modelPermissionEvaluatorList) {
            modelPermissionEvaluatorMap.put(mpe.getType().getName(), mpe);
        }
    }
    
    /**
     * Checks to see if the Authentication object has the supplied permission  
     * on the supplied domain object
     * 
     * @param authentication the Authentication object
     * @param targetDomainObject the domain object needing permission check
     * @param permission the permission to check
     * @return true if passes the permission check, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {      
        // find the appropriate ModelPermissionEvaluator from the map based on 
        // the targetDomainObject's class and invoke the hasPermission function
        return getEvaluator(targetDomainObject.getClass().getName()).hasPermission(authentication, targetDomainObject, Permission.fromString((String)permission));
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
     * @param permission  permission the permission to check
     * @return true if passes the permission check, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {  
        // find the appropriate ModelPermissionEvaluator from the map based on 
        // the targetType and invoke the hasPermission function
        return getEvaluator(targetType).hasPermission(authentication, targetId, targetType, Permission.fromString((String)permission));
    }    
     
    private ModelPermissionEvaluator getEvaluator(String targetType) throws IllegalArgumentException {        
        ModelPermissionEvaluator mpe = modelPermissionEvaluatorMap.get(targetType);
        if (mpe == null) {
            throw new IllegalArgumentException("ModelPermissionEvaluator not found for type " + targetType);
        }
        return mpe;
    }
}