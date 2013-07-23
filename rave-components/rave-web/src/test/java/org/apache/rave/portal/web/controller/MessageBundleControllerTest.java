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

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageBundleControllerTest {
    private MessageBundleController messageBundleController;
    private MockHttpServletRequest request;
    private final Integer EXPECTED_CLIENT_MESSAGE_BUNDLE_MAX_AGE = 60 * 60 * 24;
    private final String EXPECTED_JAVASCRIPT_CONTENT_TYPE = "text/javascript";
    private HttpHeaders expectedHeaders;

    @Before
    public void setup() {        
        messageBundleController = new MessageBundleController();
        request = new MockHttpServletRequest();

        expectedHeaders = new HttpHeaders();
        expectedHeaders.setCacheControl("max-age=" + EXPECTED_CLIENT_MESSAGE_BUNDLE_MAX_AGE);
        expectedHeaders.setContentType(MediaType.parseMediaType(EXPECTED_JAVASCRIPT_CONTENT_TYPE));
    }

    @Test
    public void getClientMessages_defaultLocale() {                               
        final String EXPECTED_JS = buildExpectedJavaScript("mother", "father", "text with \\\"quotes\\\"");
        assertResponseEntity(messageBundleController.getClientMessages(request), EXPECTED_JS, HttpStatus.OK);
    }

    @Test
    public void getClientMessages_nlLocale() {
        Locale nlLocale = new Locale("nl","NL");
        request.addPreferredLocale(nlLocale);

        final String EXPECTED_JS = buildExpectedJavaScript("moeder", "vader", "tekst met \\\"quotes\\\"");
        assertResponseEntity(messageBundleController.getClientMessages(request), EXPECTED_JS, HttpStatus.OK);
    }

    @Test
    public void getClientMessages_cachedRequest() {
        Locale nlLocale = new Locale("nl","NL");
        request.addPreferredLocale(nlLocale);

        final String EXPECTED_UNCACHED_JS = buildExpectedJavaScript("moeder", "vader", "tekst met \\\"quotes\\\"");
        final String EXPECTED_CACHED_JS = buildExpectedJavaScript("moeder", "vader", "tekst met \\\"quotes\\\"");

        assertResponseEntity(messageBundleController.getClientMessages(request), EXPECTED_UNCACHED_JS, HttpStatus.OK);
        assertResponseEntity(messageBundleController.getClientMessages(request), EXPECTED_CACHED_JS, HttpStatus.OK);
    }

    private void assertResponseEntity(ResponseEntity<String> responseEntity, String expectedBody, HttpStatus expectedHttpStatus) {
        assertThat(responseEntity.getBody(), is(expectedBody));
        assertThat(responseEntity.getStatusCode(), is(expectedHttpStatus));
        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.parseMediaType(EXPECTED_JAVASCRIPT_CONTENT_TYPE)));
        assertThat(responseEntity.getHeaders().getCacheControl(), is("max-age=" + EXPECTED_CLIENT_MESSAGE_BUNDLE_MAX_AGE));
    }

    private String buildExpectedJavaScript(String test1, String test2, String test3) {
        StringBuilder sb = new StringBuilder();
        sb.append("define([], function(){ return {\"test1\":\"");
        sb.append(test1);
        sb.append("\",\"test2\":\"");
        sb.append(test2);
        sb.append("\",\"test3\":\"");
        sb.append(test3);
        sb.append("\"}; })");
        return sb.toString();
    }
}