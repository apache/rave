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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.rave.integrationtests.webservice.StateManager;
import org.apache.rave.rest.model.JsonResponseWrapper;
import org.apache.rave.rest.model.Person;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PeopleSteps {

    private static final String URL_PEOPLE = "/people/";

    private static final String KEY_ACTIVE_PERSON = "active_person";

    private static ObjectMapper mapper = new ObjectMapper();

    @Given("the system contains \"$count\" people")
    @Then("there should be \"$count\" people in the system")
    public void countPeople(int count) throws Exception {
        assertThat("Wrong number of people", getAllPeople().size(), equalTo(count));
    }

    @When("the user retrieves the full person record for the display name of \"$displayName\"")
    public void getUserByDisplayName(String displayName) throws Exception {
        for (Person person : getAllPeople()) {
            if (person.getDisplayName().equals(displayName)) {
                getState().put(KEY_ACTIVE_PERSON, getPerson(person.getId()));
                return;
            }
        }

        assert false;
    }

    @Then("the \"$field\" field on the active person object is \"$value\"")
    public void validateFieldOnActivePersonWithValue(String field, String value) {
        validateFieldOnActivePerson(field, value);
    }

    @Then("the \"$field\" field on the active person object is null")
    public void validateFieldOnActivePersonIsNull(String field) {
        validateFieldOnActivePerson(field, null);
    }

    private void validateFieldOnActivePerson(String field, String value) {
        Person person = (Person)getState().get(KEY_ACTIVE_PERSON);
        assertThat("Active Person is Null", person, notNullValue());

        for (Method method : person.getClass().getMethods()) {
            if (method.getName().toLowerCase().equals("get" + field.toLowerCase())) {
                try {
                    String val = (String)method.invoke(person);
                    assertThat("Value doesn't match", val, equalTo(value));
                    return;
                } catch (Exception ex) {
                    fail("Exception thrown");
                }
            }
        }

        fail("Method not found");
    }

    private List<Person> getAllPeople() throws Exception {
        WebClient webClient = getClient();
        List<Person> people = new ArrayList<Person>();
        for (Object obj : (List<Object>)webClient.path(URL_PEOPLE).get(JsonResponseWrapper.class).getData()) {
            people.add(mapper.readValue(mapper.writeValueAsString(obj), Person.class));
        }
        return people;
    }

    private Person getPerson(String id) throws Exception {
        WebClient webClient = getClient();
        Object person = webClient.path(URL_PEOPLE + id).get(JsonResponseWrapper.class).getData();
        return mapper.readValue(mapper.writeValueAsString(person), Person.class);
    }

    private WebClient getClient() {
        WebClient client = (WebClient) getState().get(CommonSteps.KEY_WEB_CLIENT);
        client.back(true);
        client.resetQuery();
        return client;
    }

    private HashMap<String, Object> getState() {
        return StateManager.getStateStore(new String(Long.toString(Thread.currentThread().getId())));
    }
}
