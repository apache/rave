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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.JpaPerson;
import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.apache.rave.portal.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml",
        "classpath:test-dataContext.xml"})
public class JpaPersonRepositoryTest {

    private static final Long VALID_ID = 1L;
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
    public void getType() {
        assertEquals(repository.getType(), JpaPerson.class);
    }

    @Test
    public void get() {
        JpaPerson p = (JpaPerson) repository.get(VALID_ID);
        assertThat(p.getEntityId(), is(VALID_ID));
        assertThat(p.getUsername(), is(VALID_USER));
    }

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
        assertThat(connected.size(), is(equalTo(3)));
        assertThat(connected.get(0).getUsername(), is(equalTo(VALID_USER2)));
        assertThat(connected.get(1).getUsername(), is(equalTo(VALID_USER3)));
        assertThat(connected.get(2).getUsername(), is(equalTo(VALID_USER4)));
    }
    @Test
    public void findFriends_invalid() {
        List<Person> connected = repository.findFriends(INVALID_USERNAME);
        assertThat(connected.isEmpty(), is(true));
    }

    @Test
    public void findConnected_valid() {
        List<Person> connected = repository.findAllConnectedPeople(VALID_USER);
        assertThat(connected.size(), is(equalTo(4)));
        assertThat(connected.get(0).getUsername(), is(equalTo(VALID_USER2)));
        assertThat(connected.get(1).getUsername(), is(equalTo(VALID_USER3)));
        assertThat(connected.get(2).getUsername(), is(equalTo(VALID_USER4)));
        assertThat(connected.get(3).getUsername(), is(equalTo(VALID_USER5)));
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

    @Test(expected = NotSupportedException.class)
    public void findAllConnectedPeople_2param() {
        repository.findAllConnectedPeople("asdf", "asdf");
    }

    @Test(expected = NotSupportedException.class)
    public void findAllConnectedPeopleWithFriend() {
        repository.findAllConnectedPeopleWithFriend("asdf", "asdf");
    }

    @Test(expected = NotSupportedException.class)
    public void findFriends() {
        repository.findFriends("asdf", "asdf");
    }

    @Test
    public void findFriendsWithFriend_valid() {
        List<Person> friendsWithFriend = repository.findFriendsWithFriend(VALID_USER, VALID_USER2);
        assertThat(friendsWithFriend.size(), is(equalTo(2)));
        assertThat(friendsWithFriend.get(0).getUsername(), is(equalTo(VALID_USER3)));
        assertThat(friendsWithFriend.get(1).getUsername(), is(equalTo(VALID_USER4)));
    }

    @Test
    public void findFriendsWithFriend_invalid() {
        List<Person> friendsWithFriend = repository.findFriends(INVALID_USERNAME);
        assertThat(friendsWithFriend.isEmpty(), is(true));
    }

    @Test(expected = NotSupportedException.class)
    public void findByGroup() {
        repository.findByGroup("asdf", "asdf");
    }

    @Test(expected = NotSupportedException.class)
    public void findByGroupWithFriend() {
        repository.findByGroupWithFriend("asdf", "asdf");
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        final String NEW_USERNAME = "test123";
        final String NEW_ABOUT_ME = "about me blah blah";
        JpaPerson person = new JpaPerson();
        person.setUsername(NEW_USERNAME);
        person.setAboutMe(NEW_ABOUT_ME);

        assertThat(person.getEntityId(), is(nullValue()));
        repository.save(person);
        long newId = person.getEntityId();
        assertThat(newId > 0, is(true));
        JpaPerson newPerson = (JpaPerson) repository.get(newId);
        assertThat(newPerson.getAboutMe(), is(NEW_ABOUT_ME));
        assertThat(newPerson.getUsername(), is(NEW_USERNAME));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        final String UPDATED_ABOUT_ME = "updated about me";
        Person person = repository.get(VALID_ID);
        assertThat(person.getAboutMe(), is(not(UPDATED_ABOUT_ME)));
        person.setAboutMe(UPDATED_ABOUT_ME);
        repository.save(person);
        assertThat(repository.get(VALID_ID).getAboutMe(), is(UPDATED_ABOUT_ME));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_jpaObject() {
        Person person = repository.get(VALID_ID);
        assertThat(person, is(notNullValue()));
        repository.delete(person);
        person = repository.get(VALID_ID);
        assertThat(person, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_implObject() {
        Person person = repository.get(VALID_ID);
        assertThat(person, is(notNullValue()));
        PersonImpl impl = new PersonImpl();
        impl.setUsername(person.getUsername());
        repository.delete(impl);
        person = repository.get(VALID_ID);
        assertThat(person, is(nullValue()));
    }
    
    @Test
    public void read_properties() {
        Person person = repository.get(VALID_ID);
    	assertThat(person.getProperties().size(), is(1)); 
    }

}
