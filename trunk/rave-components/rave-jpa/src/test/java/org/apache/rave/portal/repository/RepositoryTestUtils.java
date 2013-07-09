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
package org.apache.rave.portal.repository;

import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.rave.portal.model.BasicEntity;
import org.apache.rave.model.PageType;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 *
 * @author ACARLUCCI
 */
public class RepositoryTestUtils {
    
    /**
     * Utility function for populating all required fields in model entities
     * by using reflection and JPA entity meta data inspection to determine
     * which fields are NOT NULL allowable.
     * 
     * @param entityManager
     * @param entity
     * @throws Exception 
     */
    public static void populateAllRequiredFieldsInEntity(EntityManager entityManager, BasicEntity entity) throws Exception {
        // loop over all fields in entity and determine which ones are 
        // "required", (optional=false) in JPA config terms, and set a value
        // to those fields            
        FieldMetaData[] declaredFields = JPAFacadeHelper.getMetaData(entityManager, entity.getClass()).getDeclaredFields();
        // for each field int the entity...
        for (FieldMetaData fieldMetaData : declaredFields) {
            // if the field is declared optional=false...
            if (fieldMetaData.getFieldMetaData().getNullValue() == FieldMetaData.NULL_EXCEPTION) {                    
                String fieldName = fieldMetaData.getName();                    
                // using reflection obtain the getter and setter methods for this field...
                for (Method method : entity.getClass().getDeclaredMethods()) {
                    String methodPrefix = method.getName().substring(0,3);
                    String methodName = method.getName().substring(3);
                    if("get".equals(methodPrefix) && fieldName.equalsIgnoreCase(methodName)) {
                        String setterMethodName = "set" + methodName;                           
                        Method setterMethod = entity.getClass().getMethod(setterMethodName, method.getReturnType());                            
                        invokeEntitySetterMethodBasedOnAvailableConstructor(entity, method, setterMethod);                 
                    }
                }                                        
            }
        }
    }
    
    private static void invokeEntitySetterMethodBasedOnAvailableConstructor(BasicEntity entity, Method getterMethod, Method setterMethod) throws Exception {        
        // first see if there is a simple no-arg default constructor for this field type
        Constructor<?> defaultConstructor = null;
        try {
            defaultConstructor = getterMethod.getReturnType().getConstructor();
        } catch (NoSuchMethodException e) { }

        if (defaultConstructor != null) {                                
            setterMethod.invoke(entity, defaultConstructor.newInstance());    
        } else {
            // check to see what the type is and invoke a constructor
            // new classes might need to be added here in the future 
            // based on what fields are required in the models
            if (getterMethod.getReturnType().equals(Long.class)) {
                setterMethod.invoke(entity, new Long(1L)); 
            } else if (getterMethod.getReturnType().equals(Integer.class)) {
                setterMethod.invoke(entity, new Integer(1)); 
            } else if (getterMethod.getReturnType().equals(PageType.class)){
                setterMethod.invoke(entity, PageType.USER);
            }
        }           
    }    
}
