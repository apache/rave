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

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * MessageBundle Controller
 */
@Controller
@RequestMapping("/messagebundle/*")
public class MessageBundleController {
    private static final String CLIENT_MESSAGE_IDENTIFIER = "_rave_client.";
    private static final String CLIENT_MESSAGES_BUNDLE_NAME = "messages";
    private static final String JAVASCRIPT_CONTENT_TYPE = "text/javascript";
    private static final Integer CLIENT_MESSAGE_BUNDLE_CACHE_CONTROL_MAX_AGE = 60 * 60 * 24;   // 24 hours
    private Map<Locale, String> clientMessagesCache;
    private AcceptHeaderLocaleResolver acceptHeaderLocaleResolver;
    private HttpHeaders clientMessagesResponseHeaders;

    public MessageBundleController() {
        clientMessagesCache = new HashMap<Locale, String>();
        acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        clientMessagesResponseHeaders = new HttpHeaders();

        // set the common response headers that will be used by the getClientMessages response
        clientMessagesResponseHeaders.setCacheControl("max-age=" + CLIENT_MESSAGE_BUNDLE_CACHE_CONTROL_MAX_AGE);
        clientMessagesResponseHeaders.setContentType(MediaType.parseMediaType(JAVASCRIPT_CONTENT_TYPE));
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Returns a javascript initialization script which adds language specific client-side messages needed for use
     * in the Rave javascript namespace.  It uses the Locale specified by the client's browser to determine
     * which message bundle language to use.
     *
     * @param request The incoming HttpServletRequest
     * @return the JavaScript content to load from the client
     */
    @ResponseBody
    @RequestMapping(value = {"/rave_client_messages.js"}, method = RequestMethod.GET)
    public ResponseEntity<String> getClientMessages(HttpServletRequest request) {
        return new ResponseEntity<String>(getClientMessagesJSForLocale(acceptHeaderLocaleResolver.resolveLocale(request)), clientMessagesResponseHeaders, HttpStatus.OK);
    }

    private Map<String, String> convertResourceBundleToClientMessagesMap(ResourceBundle resourceBundle) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            // only load the messages that are specifically used by the client code for performance reasons
            // strip off the _rave_client. part of the key
            if (key.startsWith(CLIENT_MESSAGE_IDENTIFIER)) {
                map.put(key.replaceFirst(CLIENT_MESSAGE_IDENTIFIER, ""), StringEscapeUtils.escapeEcmaScript(resourceBundle.getString(key)));
            }
        }
        return map;
    }

    private String convertClientMessagesMapToJavaScriptOutput(Map<String, String> clientMessagesMap) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        final String add_client_message = "\"";
        final String key_value_separator = "\":\"";
        final String message_suffix = "\"";
        final String message_newline = ",";
        for (Map.Entry<String, String> mapEntry : clientMessagesMap.entrySet()) {
            i++;
            sb.append(add_client_message).append(mapEntry.getKey()).append(key_value_separator);
            sb.append(mapEntry.getValue()).append(message_suffix);
            if (i<clientMessagesMap.size()) {
                sb.append(message_newline);
            }
        }
        return "define([], function(){ return {" + sb.toString() + "}; })";
    }

    private String getClientMessagesJSForLocale(Locale locale) {
        String javascriptOutput = getClientMessagesJSFromCache(locale);
        if (javascriptOutput == null) {
            javascriptOutput = getClientMessagesJSFromBundle(locale);
        }
        return javascriptOutput;
    }

    private String getClientMessagesJSFromCache(Locale locale) {
        return clientMessagesCache.get(locale);
    }

    private String getClientMessagesJSFromBundle(Locale locale) {
        String javascriptOutput = convertClientMessagesMapToJavaScriptOutput(
                convertResourceBundleToClientMessagesMap(
                        ResourceBundle.getBundle(CLIENT_MESSAGES_BUNDLE_NAME, locale)));

        cacheClientMessages(locale, javascriptOutput);
        return javascriptOutput;
    }

    private void cacheClientMessages(Locale locale, String javascriptOutput) {
        clientMessagesCache.put(locale, javascriptOutput);
    }
}
