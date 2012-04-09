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
package org.apache.rave.portal.web.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Abstract class containing common functionality that should be shared across
 * all Rest Api classes
 *  
 * @author carlucci
 */
public abstract class AbstractRestApi {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
     /**
     * Return a 403 response code when any org.springframework.security.access.AccessDeniedException
     * is thrown from any of the API methods due to security restrictions     
     * 
     * @param ex the AccessDeniedException
     * @param request the http request
     * @param response the http response
     */
    @ExceptionHandler(AccessDeniedException.class) 
    public void handleAccessDeniedException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        logger.info("AccessDeniedException: " + request.getUserPrincipal().getName() + " attempted to access resource " + request.getRequestURL(), ex);
        response.setStatus(HttpStatus.FORBIDDEN.value());    
    }
    
    // TODO RAVE-240 - when we implement security we can implement different exception
    //        handlers for different errors (unauthorized, resource not found, etc)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        // RAVE-240 lowered the log level to info
        logger.info("Error occured while accessing " + request.getRequestURL(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ClassUtils.getShortName(ex.getClass());
    }    
}