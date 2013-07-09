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

/**
 * Class used by the Permission security annotations in cases where we want
 * to perform permission checks on Model objects that are inputs to service 
 * functions but are not of the same class as the returned object(s).
 *
 * For example, verifying that the supplied userId to a function is the 
 * current authenticated user, when the function returns a List of Page objects.  
 * See org.apache.rave.portal.service.DefaultPageService.getAllUserPages for an
 * example of usage.
 * 
 * @author carlucci
 */
public class RaveSecurityContext implements Serializable {
    // the id of the security context object
    private Object id;
    // the type fully qualified class name of the context object
    private String type;

    public RaveSecurityContext() { }
    public RaveSecurityContext(Object id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}