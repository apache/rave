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

package org.apache.rave.provider.opensocial.controller;

import com.google.common.collect.Maps;
import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin controller to manipulate Widget data
 */
@Controller @RequestMapping(value = "/api/rest/opensocial")
public class SecurityTokenController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OpenSocialService openSocialService;

    @RequestMapping(value = "/gadget", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Map<String, Object> getUnboundTokenAndMetadata(@RequestParam("url") final String url, @RequestParam("pageid") final String pageId) {
    	Map<String, Object> jsonResponse = new HashMap<String, Object>();
        jsonResponse.put("securityToken", openSocialService.getEncryptedSecurityToken(pageId, url));
        jsonResponse.put("metadata", openSocialService.getGadgetMetadata(url));
    	return jsonResponse;
    }

    /**
     * Return a 404 response code when any ResourceNotFoundException is thrown
     *
     * @param ex the ResourceNotFoundException
     * @param request the http request
     * @param response the http response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public Map<String, String> handleNotFound(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        return setResponse(ex, request, response, HttpStatus.NOT_FOUND);
    }


    /**
     * Return a 400 response code when any IllegalState is thrown
     *
     * @param ex the IllegalStateException
     * @param request the http request
     * @param response the http response
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public Map<String, String> handleIllegalState(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        return setResponse(ex, request, response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Return a 500 response code when any unknown exception is thrown
     *
     * @param ex the exeption
     * @param request the http request
     * @param response the http response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, String> handleALl(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        return setResponse(ex, request, response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> setResponse(Exception ex, HttpServletRequest request, HttpServletResponse response, HttpStatus status) {
        logger.error("Not Found Exception: " + request.getUserPrincipal().getName() + " attempted to access resource " + request.getRequestURI(), ex);
        response.setStatus(status.value());
        Map<String, String> errors = Maps.newHashMap();
        errors.put("error", ex.getMessage());
        return errors;
    }
}
