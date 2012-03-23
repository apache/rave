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

package org.apache.rave.opensocial.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.rave.portal.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:rave-shindig-test-applicationContext.xml",
        "classpath:rave-shindig-test-dataContext.xml"})
public class JpaPersonRepositoryTest {

    private static final String VALID_USER = "canonical";
    private static final String VALID_USER2 = "john.doe";
    private static final String VALID_USER3 = "jane.doe";
    private static final String VALID_USER4 = "george.doe";
    private static final String VALID_USER5 = "mario.rossi";
    private static final String INVALID_USERNAME = "INVALID_USERNAME";
    private static final String FEMALE = "female";

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PersonRepository repository;

    @Test
    public void findByUsername_valid() {
        Person person = repository.findByUsername(VALID_USER);
        assertThat(person, is(not(nullValue())));
        assertThat(person.getUsername(), is(equalTo(VALID_USER)));
    }
    @Test
    public void findByUsername_null() {
        Person person = repository.findByUsername(INVALID_USERNAME);
        assertThat(person, is(nullValue()));
    }

    @Test
    public void findFriends_valid() {
        List<Person> connected = repository.findFriends(VALID_USER);
        assertThat(connected.size(), is(equalTo(2)));
        assertThat(connected.get(0).getUsername(), is(equalTo(VALID_USER2)));
        assertThat(connected.get(1).getUsername(), is(equalTo(VALID_USER3)));
    }
    @Test
    public void findFriends_invalid() {
        List<Person> connected = repository.findFriends(INVALID_USERNAME);
        assertThat(connected.isEmpty(), is(true));
    }

    @Test
    public void findConnected_valid() {
        List<Person> connected = repository.findAllConnectedPeople(VALID_USER);
        assertThat(connected.size(), is(equalTo(3)));
        assertThat(connected.get(0).getUsername(), is(equalTo(VALID_USER2)));
        assertThat(connected.get(1).getUsername(), is(equalTo(VALID_USER3)));
        assertThat(connected.get(2).getUsername(), is(equalTo(VALID_USER5)));
    }

    @Test
    public void findConnected_invalid() {
        List<Person> connected = repository.findFriends(INVALID_USERNAME);
        assertThat(connected.isEmpty(), is(true));
    }

    @Test
    public void findByGroup_valid() {
        List<Person> connected = repository.findByGroup("Party");
        assertThat(connected.size(), is(equalTo(2)));
        assertThat(connected.get(0).getUsername(), is(equalTo(VALID_USER)));
        assertThat(connected.get(1).getUsername(), is(equalTo(VALID_USER5)));
    }

    @Test
    public void findByGroup_invalid() {
        List<Person> connected = repository.findByGroup(INVALID_USERNAME);
        assertThat(connected.isEmpty(), is(true));
    }


}
