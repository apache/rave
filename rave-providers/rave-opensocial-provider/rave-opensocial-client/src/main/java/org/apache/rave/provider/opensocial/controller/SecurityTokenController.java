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

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.provider.opensocial.exception.SecurityTokenException;
import org.apache.rave.provider.opensocial.service.impl.EncryptedBlobSecurityTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Admin controller to manipulate Widget data
 */
@Controller
public class SecurityTokenController{

    @Autowired
    private WidgetService widgetService;
    
    @Autowired
    private PageService pageService;
    
    @Autowired
    private EncryptedBlobSecurityTokenService securityTokenService;

    @RequestMapping(value = "/api/rest/st", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Map<String, Object> getSecurityToken(@RequestParam("url") final String url, @RequestParam("pageid") final String pageId) {
    	Widget widget = widgetService.getWidgetByUrl(url);
    	Page page = pageService.getPage(pageId);
    	
    	// Use a dummy RegionWidget to generate the security token
    	RegionWidget regionWidget = new RegionWidgetImpl(String.valueOf(System.currentTimeMillis()),"-1",
    			new RegionImpl("-1", page, -1));
    	
    	Map<String, Object> jsonResponse = new HashMap<String, Object>();
    	Map<String, String> errorMessage = new HashMap<String, String>();
    	
    	String securityToken = "";
    	if (widget != null && widget.getWidgetStatus() == WidgetStatus.PUBLISHED) {
    		try {
    			securityToken = securityTokenService.getEncryptedSecurityToken(regionWidget, widget);
    		} catch (SecurityTokenException e) {
    			errorMessage.put("message", "There was a problem creating the security token.");
    		}
    	} else if (widget != null && widget.getWidgetStatus() == WidgetStatus.PREVIEW) {
    		errorMessage.put("message", "The requested gadget exists in the gadget store but is not published.");
    	} else {
    		errorMessage.put("message", "The requested gadget does not exist in the gadget store.");
    	}
    	
    	jsonResponse.put("securityToken", securityToken);
    	if (!errorMessage.isEmpty()) {
    		jsonResponse.put("error", errorMessage);
    	}
    	
    	return jsonResponse;
    }
}
