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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageBundleControllerTest {
    private MessageBundleController messageBundleController;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setup() {        
        messageBundleController = new MessageBundleController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void getClientMessages_defaultLocale() {
        Locale.setDefault(Locale.US);
        final String EXPECTED_JS = buildExpectedJavaScript("mother", "father", "text with \\\"quotes\\\"");
        assertThat(messageBundleController.getClientMessages(request, response), is(EXPECTED_JS));
        assertThat(response.getContentType(), is("application/javascript"));
    }

    @Test
    public void getClientMessages_nlLocale() {
        Locale nlLocale = new Locale("nl","NL");
        request.addPreferredLocale(nlLocale);

        final String EXPECTED_JS = buildExpectedJavaScript("moeder", "vader", "tekst met \\\"quotes\\\"");
        assertThat(messageBundleController.getClientMessages(request, response), is(EXPECTED_JS));
        assertThat(response.getContentType(), is("application/javascript"));
    }
    
    
    private String buildExpectedJavaScript(String test1, String test2, String test3) {
        StringBuilder sb = new StringBuilder();
        sb.append("rave.addClientMessage(\"test1\",\"");
        sb.append(test1);
        sb.append("\");rave.addClientMessage(\"test2\",\"");
        sb.append(test2);
        sb.append("\");rave.addClientMessage(\"test3\",\"");
        sb.append(test3);
        sb.append("\");");
        return sb.toString();
    }
}