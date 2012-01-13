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
package org.apache.rave.portal.web.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * MessageBundle Controller
 */
@Controller
@RequestMapping("/messagebundle/*")
public class MessageBundleController  {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String CLIENT_MESSAGE_IDENTIFIER = "_rave_client.";
    private final String CLIENT_MESSAGES_BUNDLE_NAME = "messages";

    /**
     * Returns a javascript initialization script which adds language specific client-side messages needed for use
     * in the Rave javascript namespace.  It uses the Locale specified by the client's browser to determine
     * which message bundle language to use.
     *
     * @param request  The incoming HttpServletRequest
     * @param response The outgoing HttpServletResponse
     * @return the JavaScript content to load from the client
     */
    @RequestMapping(value = {"/rave_client_messages.js"}, method = RequestMethod.GET)
    @ResponseBody
    public String getClientMessages(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/javascript");

        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(CLIENT_MESSAGES_BUNDLE_NAME, acceptHeaderLocaleResolver.resolveLocale(request));

        return convertClientMessagesMapToJavaScriptOutput(convertResourceBundleToClientMessagesMap(resourceBundle));
    }

    private Map<String, String> convertResourceBundleToClientMessagesMap(ResourceBundle resourceBundle) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            // only load the messages that are specifically used by the client code for performance reasons
            // strip off the client. part of the key
            if (key.startsWith(CLIENT_MESSAGE_IDENTIFIER)) {
                map.put(key.replaceFirst(CLIENT_MESSAGE_IDENTIFIER,""), StringEscapeUtils.escapeJavaScript(resourceBundle.getString(key)));
            }
        }
        return map;
    }
    
    private String convertClientMessagesMapToJavaScriptOutput(Map<String, String> clientMessagesMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> mapEntry : clientMessagesMap.entrySet()) {
            sb.append("rave.addClientMessage(\"" + mapEntry.getKey() + "\",\"" + mapEntry.getValue() + "\");");
        }
        return sb.toString();
    }
}