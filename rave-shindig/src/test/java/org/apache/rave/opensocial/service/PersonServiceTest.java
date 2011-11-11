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

package org.apache.rave.opensocial.service;

import com.google.common.collect.Lists;
import org.apache.rave.opensocial.repository.PersonRepository;
import org.apache.rave.opensocial.service.impl.DefaultPersonService;
import org.apache.rave.opensocial.service.impl.FieldRestrictingPerson;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.protocol.model.FilterOperation;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PersonServiceTest {

    private static final String DISPLAY_NAME = "ABCDE";
    private static final String HAPPIEST_WHEN = "Sleeping";
    private static final String ID_2 = "1235";
    private static final String ID_3 = "1236";
    private static final String ID_1 = "1234";
    private static final String GROUP_ID = "BOO";
    private PersonService service;
    private PersonRepository repository;
    private SecurityToken token;

    @Before
    public void setup() {
        token = createNiceMock(SecurityToken.class);
        repository = createNiceMock(PersonRepository.class);
        service = new DefaultPersonService(repository);
    }

    @Test
    @Ignore
    public void getPerson_allFields() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);
        Set<String> fields = new HashSet<String>();

        org.apache.rave.portal.model.Person dbPerson = getDbPerson();
        expect(repository.get(Long.parseLong(ID_1))).andReturn(dbPerson);
        replay(repository);

        Future<Person> personFuture = service.getPerson(id, fields, token);
        assertThat(personFuture, is(not(nullValue())));
        Person person = personFuture.get();
        assertThat(person, is(not(nullValue())));
        assertThat(person, is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(person.getId(), is(equalTo(ID_1)));
        assertThat(person.getHappiestWhen(), is(equalTo(HAPPIEST_WHEN)));
        assertThat(person.getDisplayName(), is(equalTo(DISPLAY_NAME)));
    }

    @Test
    public void getPerson_restrictedFields() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);
        Set<String> fields = new HashSet<String>();
        fields.add(Person.Field.DISPLAY_NAME.toString());
        fields.add(Person.Field.ID.toString());

        org.apache.rave.portal.model.Person dbPerson = getDbPerson();
        expect(repository.get(Long.parseLong(ID_1))).andReturn(dbPerson);
        replay(repository);

        Future<Person> personFuture = service.getPerson(id, fields, token);
        assertThat(personFuture, is(not(nullValue())));
        Person person = personFuture.get();
        assertThat(person, is(not(nullValue())));
        assertThat(person, is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(person.getId(), is(equalTo(ID_1)));
        assertThat(person.getHappiestWhen(), is(nullValue()));
        assertThat(person.getDisplayName(), is(equalTo(DISPLAY_NAME)));
    }

    @Test
    @Ignore
    public void getPerson_nullFields() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);

        org.apache.rave.portal.model.Person dbPerson = getDbPerson();
        expect(repository.get(Long.parseLong(ID_1))).andReturn(dbPerson);
        replay(repository);

        Future<Person> personFuture = service.getPerson(id, null, token);
        assertThat(personFuture, is(not(nullValue())));
        Person person = personFuture.get();
        assertThat(person, is(not(nullValue())));
        assertThat(person, is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(person.getId(), is(equalTo(ID_1)));
        assertThat(person.getHappiestWhen(), is(equalTo(HAPPIEST_WHEN)));
        assertThat(person.getDisplayName(), is(equalTo(DISPLAY_NAME)));
    }

    @Test
    @Ignore
    public void getPerson_viewer() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.viewer, ID_2);

        expect(token.getViewerId()).andReturn(ID_1);
        replay(token);

        org.apache.rave.portal.model.Person dbPerson = getDbPerson();
        expect(repository.get(Long.parseLong(ID_1))).andReturn(dbPerson);
        replay(repository);

        Future<Person> personFuture = service.getPerson(id, null, token);
        assertThat(personFuture, is(not(nullValue())));
        Person person = personFuture.get();
        assertThat(person, is(not(nullValue())));
        assertThat(person, is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(person.getId(), is(equalTo(ID_1)));
        assertThat(person.getHappiestWhen(), is(equalTo(HAPPIEST_WHEN)));
        assertThat(person.getDisplayName(), is(equalTo(DISPLAY_NAME)));
    }

    @Test(expected = ProtocolException.class)
    public void getPerson_nullValue() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_2);

        expect(repository.get(Long.parseLong(ID_1))).andReturn(null);
        replay(repository);

        service.getPerson(id, null, token);
    }

    @Test
    public void getPeople_self() throws ExecutionException, InterruptedException {
        String self = ID_1;
        expect(token.getViewerId()).andReturn(self);
        replay(token);

        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);

        expect(repository.get(Long.parseLong(self))).andReturn(getDbPerson());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, null, null, token);
        assertThat(people, is(not(nullValue())));
        assertThat(people.get().getTotalResults(), is(equalTo(1)));
        assertThat(people.get().getEntry().get(0).getId(), is(equalTo(self)));
    }

    @Test
    @Ignore
    public void getPeople_all() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.all, GROUP_ID);
        replay(token);

        expect(repository.findAllConnectedPeople(ID_2)).andReturn(getDbPersonList());
        expect(repository.findAllConnectedPeople(ID_3)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, null, null, token);
        assertThat(people, is(not(nullValue())));
        assertThat(people.get().getEntry().get(0), is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(hasUniqueValues(people), is(true));
        verify(repository);
    }

    @Test
    @Ignore
    public void getPeople_friends() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.friends, GROUP_ID);
        replay(token);

        expect(repository.findFriends(ID_2)).andReturn(getDbPersonList());
        expect(repository.findFriends(ID_3)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, null, null, token);
        assertThat(people, is(not(nullValue())));
        assertThat(people.get().getEntry().get(0), is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(hasUniqueValues(people), is(true));
        verify(repository);
    }

    @Test
    public void getPeople_groupId() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.groupId, GROUP_ID);

        expect(repository.findByGroup(GROUP_ID)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, null, null, token);
        assertThat(people, is(not(nullValue())));
        assertThat(people.get().getEntry().get(0), is(instanceOf(FieldRestrictingPerson.class)));
        assertThat(hasUniqueValues(people), is(true));
        verify(repository);
    }

    @Test
    public void getPeople_GroupFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.groupId, GROUP_ID);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = Person.Field.ABOUT_ME.toString();
        String value = "AAA";
        options.setFilter(field);
        options.setFilterOperation(FilterOperation.contains);
        options.setFilterValue(value);

        expect(repository.findByGroup(GROUP_ID, field, FilterOperation.contains, value)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_AllFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.all, GROUP_ID);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = Person.Field.ABOUT_ME.toString();
        String value = "AAA";
        options.setFilter(field);
        options.setFilterOperation(FilterOperation.contains);
        options.setFilterValue(value);

        expect(repository.findAllConnectedPeople(ID_2, field, FilterOperation.contains, value)).andReturn(getDbPersonList());
        expect(repository.findAllConnectedPeople(ID_3, field, FilterOperation.contains, value)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_FriendsFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.friends, GROUP_ID);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = Person.Field.ABOUT_ME.toString();
        String value = "AAA";
        options.setFilter(field);
        options.setFilterOperation(FilterOperation.contains);
        options.setFilterValue(value);

        expect(repository.findFriends(ID_2, field, FilterOperation.contains, value)).andReturn(getDbPersonList());
        expect(repository.findFriends(ID_3, field, FilterOperation.contains, value)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_FriendsAllFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.friends, GROUP_ID);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.ALL_FILTER;
        String value = "AAA";
        options.setFilter(field);
        options.setFilterOperation(FilterOperation.contains);
        options.setFilterValue(value);

        expect(repository.findFriends(ID_2)).andReturn(getDbPersonList());
        expect(repository.findFriends(ID_3)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_friendHasAppFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.friends, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId).anyTimes();
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.HAS_APP_FILTER;
        options.setFilter(field);

        expect(repository.findFriends(ID_2, appId)).andReturn(getDbPersonList());
        expect(repository.findFriends(ID_3, appId)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_allHasAppFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.all, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId).anyTimes();
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.HAS_APP_FILTER;
        options.setFilter(field);

        expect(repository.findAllConnectedPeople(ID_2, appId)).andReturn(getDbPersonList());
        expect(repository.findAllConnectedPeople(ID_3, appId)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_groupHasAppFilterField() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.groupId, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId).anyTimes();
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.HAS_APP_FILTER;
        options.setFilter(field);

        expect(repository.findByGroup(GROUP_ID, appId)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_friendIsFriendsWith() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.friends, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.IS_WITH_FRIENDS_FILTER;
        options.setFilter(field);
        options.setFilterValue(ID_1);

        expect(repository.findFriendsWithFriend(ID_2, ID_1)).andReturn(getDbPersonList());
        expect(repository.findFriendsWithFriend(ID_3, ID_1)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_allIsFriendsWith() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.all, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId);
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.IS_WITH_FRIENDS_FILTER;
        options.setFilter(field);
        options.setFilterValue(ID_1);

        expect(repository.findAllConnectedPeopleWithFriend(ID_2, ID_1)).andReturn(getDbPersonList());
        expect(repository.findAllConnectedPeopleWithFriend(ID_3, ID_1)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test
    public void getPeople_groupIsFriendsWith() throws ExecutionException, InterruptedException {
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.groupId, GROUP_ID);
        String appId = "5";
        expect(token.getAppId()).andReturn(appId).anyTimes();
        replay(token);

        CollectionOptions options = new CollectionOptions();
        String field = PersonService.IS_WITH_FRIENDS_FILTER;
        options.setFilter(field);
        options.setFilterValue(ID_1);

        expect(repository.findByGroupWithFriend(GROUP_ID, ID_1)).andReturn(getDbPersonList());
        replay(repository);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, options, null, token);
        assertThat(people, is(not(nullValue())));
        verify(repository);
    }

    @Test(expected = ProtocolException.class)
    public void getPeople_deleted() throws ExecutionException, InterruptedException {
        String self = ID_1;
        expect(token.getViewerId()).andReturn(self);
        replay(token);
        Set<UserId> ids = getUserIdSet();
        GroupId groupId = new GroupId(GroupId.Type.deleted, GROUP_ID);

        Future<RestfulCollection<Person>> people = service.getPeople(ids, groupId, null, null, token);
    }

    private List<org.apache.rave.portal.model.Person> getDbPersonList() {
        return Lists.asList(getDbPerson(), new org.apache.rave.portal.model.Person[]{});
    }

    private Set<UserId> getUserIdSet() {
        Set<UserId> ids = new HashSet<UserId>();
        ids.add(new UserId(UserId.Type.userId, ID_2));
        ids.add(new UserId(UserId.Type.userId, ID_3));
        return ids;
    }

    private org.apache.rave.portal.model.Person getDbPerson() {
        org.apache.rave.portal.model.Person dbPerson = new org.apache.rave.portal.model.Person();
        dbPerson.setEntityId(Long.parseLong(ID_1));
        dbPerson.setUsername(ID_1);
        dbPerson.setDisplayName(DISPLAY_NAME);
        return dbPerson;
    }

    private static boolean hasUniqueValues(Future<RestfulCollection<Person>> people) throws ExecutionException, InterruptedException {
        List<Person> persons = people.get().getEntry();
        Set<String> idSet = new HashSet<String>();
        for(Person p : persons) {
            if(idSet.contains(p.getId())) {
                return false;
            }
            idSet.add(p.getId());
        }
        return true;
    }

}
