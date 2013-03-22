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
import org.apache.rave.opensocial.service.impl.DefaultActivityStreamsService;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.apache.rave.util.ActivityConversionUtil;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.core.model.ActivityObjectImpl;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultActivityStreamsServiceTest {

    private static Logger log = Logger.getLogger(DefaultActivityStreamsServiceTest.class.getName());


    private ActivityStreamsRepository repository;
    private PersonService personService;
    private SecurityToken token;
    private DefaultActivityStreamsService service;
    private ActivityStreamsEntryImpl activityStreamsEntry;
    private Collection<? extends ActivityStreamsEntryImpl> activityStreamsEntries;
    private ActivityStreamsObjectImpl activityStreamsObject;
    private ActivityStreamsObjectImpl authorObject;
    private ActivityConversionUtil conversionUtilTest ;
    private static final String ID_1 = "1234";
    private static final String ID_2 = "1235";
    private static final String APP_ID = "testing";
    private static final String GROUP_ID = "self";
    private static final String ACTIVITY_TITLE = "Activity Title";
    private static final String ACTIVITY_ID = "Activity ID";


    @Before
    public void setup() {

        token = createNiceMock(SecurityToken.class);
        repository = createNiceMock(ActivityStreamsRepository.class);
        personService =createNiceMock(PersonService.class);
        service = new DefaultActivityStreamsService(repository,personService);

        conversionUtilTest=new ActivityConversionUtil();

        activityStreamsEntry = new ActivityStreamsEntryImpl();
        activityStreamsObject = new ActivityStreamsObjectImpl();
        authorObject = new ActivityStreamsObjectImpl();
        authorObject.setDisplayName("Test Author");
        activityStreamsObject.setAuthor(authorObject);
        activityStreamsObject.setDisplayName("Test Streams Object");
        activityStreamsObject.setPublished(new Date());
        activityStreamsObject.setId("Test ID");
        activityStreamsEntry.setUserId(ID_1);
        activityStreamsEntry.setActor(activityStreamsObject);
        activityStreamsEntry.setContent("Activity Content");
        activityStreamsEntry.setGenerator(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setIcon(new ActivityStreamsMediaLinkImpl());
        activityStreamsEntry.setId(ACTIVITY_ID);
        activityStreamsEntry.setObject(activityStreamsObject);
        activityStreamsEntry.setPublished(new Date());
        activityStreamsEntry.setProvider(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setTarget(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setTitle(ACTIVITY_TITLE);
        activityStreamsEntry.setUpdated(new Date());
        activityStreamsEntry.setUrl("Activity URL");
        activityStreamsEntry.setVerb("Activity Verb");
        activityStreamsEntry.setOpenSocial(new ActivityObjectImpl());

        activityStreamsEntries = Lists.asList(activityStreamsEntry,new ActivityStreamsEntryImpl[]{});

    }

    @Test
    public void createActivityEntryTest() throws ExecutionException, InterruptedException {

        UserId id = new UserId(UserId.Type.userId, ID_1);
        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);
        Set<String> fields = new HashSet<String>();

        expect(token.getViewerId()).andReturn(ID_1);
        expect(repository.save(EasyMock.isA(ActivityStreamsEntryImpl.class))).andReturn(activityStreamsEntry);
        replay(repository);
        replay(token);

        ActivityEntry shindigActivity = conversionUtilTest.convert(activityStreamsEntry);
        ActivityStreamsEntry raveActivity = conversionUtilTest.convert(shindigActivity);
        Future<ActivityEntry> activityEntry = service.createActivityEntry(id,groupId,APP_ID,fields, shindigActivity,token);

        assertThat(conversionUtilTest.convert(activityEntry.get()).getTitle(), is(activityStreamsEntry.getTitle()));
    }

    @Test
    public void getActivityEntriesTest() throws ExecutionException, InterruptedException {

        UserId id = new UserId(UserId.Type.userId, ID_1);
        UserId id2 = new UserId(UserId.Type.userId, ID_2);
        Set<UserId> users = new HashSet<UserId>();
        users.add(id);
        users.add(id2);

        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);
        Set<String> fields = new HashSet<String>();

        expect(repository.save(activityStreamsEntry)).andReturn(activityStreamsEntry);
        expect(repository.getByUserId(ID_1)).andReturn((List) getActivityList());
        expect(personService.getPeople(users,groupId,null,fields,token)).andReturn(ImmediateFuture.newInstance(new RestfulCollection<Person>(getDbPersonList())));
        replay(repository);
        replay(personService);

        service.createActivityEntry(id,groupId,APP_ID,fields,conversionUtilTest.convert(activityStreamsEntry),token);

        Future<RestfulCollection<ActivityEntry>> result = service.getActivityEntries(users,groupId,APP_ID,fields,null,token);

        assertThat(result, is(notNullValue()));
        assertThat(result.get().getTotalResults(), is(equalTo(1)));



    }

    @Test
    public void getActivityEntriesWithIdsTest() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);
        UserId id2 = new UserId(UserId.Type.userId, ID_2);
        HashSet<UserId> users = new HashSet<UserId>();
        users.add(id);
        users.add(id2);
        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);
        Set<String> fields = new HashSet<String>();

        expect(repository.save(EasyMock.isA(ActivityStreamsEntryImpl.class))).andReturn(activityStreamsEntry);
        expect(repository.get(ACTIVITY_ID)).andReturn(activityStreamsEntry);
        expect(personService.getPeople(users,groupId,null,fields,token)).andReturn(ImmediateFuture.newInstance(new RestfulCollection<Person>(getDbPersonList())));
        replay(repository);
        replay(personService);

        Future<ActivityEntry> entry =  service.createActivityEntry(id,groupId,APP_ID,fields,conversionUtilTest.convert(activityStreamsEntry),token);

        HashSet<String> activityIds = new HashSet<String>();
        activityIds.add(entry.get().getId());
        log.info("getting id: " + entry.get().getId());
        Future<RestfulCollection<ActivityEntry>> activities = service.getActivityEntries(id,groupId,APP_ID,fields,null,activityIds,token);

        assertThat(activities, is(notNullValue()));
        assertThat(activities.get().getTotalResults(), is(equalTo(1)));



    }

    @Test
    public void getActivityEntryWithIdTest() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);

        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);
        Set<String> fields = new HashSet<String>();


        expect(repository.save(EasyMock.isA(ActivityStreamsEntryImpl.class))).andReturn(activityStreamsEntry);
        expect(repository.get(EasyMock.eq(ACTIVITY_ID))).andReturn(activityStreamsEntry);
        replay(repository);


        Future<ActivityEntry> entry =  service.createActivityEntry(id,groupId,APP_ID,fields,conversionUtilTest.convert(activityStreamsEntry),token);

        Future<ActivityEntry> activity = service.getActivityEntry(id,groupId,APP_ID,fields,entry.get().getId(),token);

        assertThat(activity, is(notNullValue()));
        assertThat(activity.get().getTitle(), is(entry.get().getTitle()));
        assertThat(activity.get().getId(), is(entry.get().getId()));



    }

    @Test
    public void deleteActivityEntryWithIdTest() throws ExecutionException, InterruptedException {
        UserId id = new UserId(UserId.Type.userId, ID_1);

        GroupId groupId = new GroupId(GroupId.Type.self, GROUP_ID);
        Set<String> fields = new HashSet<String>();


        expect(repository.save(activityStreamsEntry)).andReturn(activityStreamsEntry);
        expect(repository.get(ACTIVITY_ID)).andReturn(activityStreamsEntry);
        repository.delete(activityStreamsEntry);
        expectLastCall();
        replay(repository);


        service.createActivityEntry(id,groupId,APP_ID,fields,conversionUtilTest.convert(activityStreamsEntry),token);

        HashSet<String> activityIds = new HashSet<String>();
        activityIds.add(ACTIVITY_ID);

        service.deleteActivityEntries(id,groupId,APP_ID,activityIds,token);

        Future<ActivityEntry> activity = service.getActivityEntry(id,groupId,APP_ID,fields,ACTIVITY_ID,token);

        assertThat(activity.get(), is(nullValue()));

    }




    private org.apache.shindig.social.opensocial.model.Person getDbPerson() {
        PersonImpl  dbPerson = new PersonImpl();
        dbPerson.setId(ID_1);

        dbPerson.setDisplayName("Test");
        return dbPerson;
    }

    private org.apache.shindig.social.opensocial.model.Person getDbPerson(String id) {
        PersonImpl dbPerson = new PersonImpl();
        dbPerson.setId(id);
        dbPerson.setDisplayName("Test");

        return dbPerson;
    }
    private List<org.apache.shindig.social.opensocial.model.Person> getDbPersonList() {

        return Lists.asList(getDbPerson(), new org.apache.shindig.social.opensocial.model.Person[]{});
    }

    private List<ActivityStreamsEntryImpl> getActivityList() {

        return Lists.asList(activityStreamsEntry, new ActivityStreamsEntryImpl[]{});
    }



}
