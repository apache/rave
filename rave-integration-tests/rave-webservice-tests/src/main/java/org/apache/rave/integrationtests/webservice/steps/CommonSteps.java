/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.integrationtests.webservice.steps;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.rave.integrationtests.webservice.StateManager;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashMap;

public class CommonSteps {
    public static final String KEY_WEB_CLIENT = "webclient";
    private final String baseURL = "http://localhost:8080/portal/api/rest";

    @Given("the user \"$username\" with the password of \"$password\" is logged into the system expecting \"$datatype\"")
    public void userIsLoggedIn(String username, String password, String datatype) {
        WebClient client;

        if (datatype.equals("JSON")) {
            JacksonJsonProvider json = new JacksonJsonProvider();

            client = WebClient.create(baseURL, Collections.singletonList(json), true);
            client.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
        } else {
            client = WebClient.create(baseURL, true);
            client.type(MediaType.APPLICATION_XML_TYPE).accept(MediaType.APPLICATION_XML_TYPE);
        }
        String authorizationHeader = "Basic " +
                org.apache.cxf.common.util.Base64Utility.encode((username + ":" + password).getBytes());
        client.header("Authorization", authorizationHeader);

        getState().put(KEY_WEB_CLIENT, client);
    }

    @When("the user waits \"$count\" seconds")
    public void userWaits(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

    private HashMap<String, Object> getState() {
        return StateManager.getStateStore(new String(Long.toString(Thread.currentThread().getId())));
    }
}
