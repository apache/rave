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

import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.apache.rave.model.Person;
import org.apache.rave.portal.repository.ApplicationDataRepository;
import org.apache.rave.opensocial.service.impl.DefaultAppDataService;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.apache.rave.service.LockService;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AppDataServiceTest {
    private SimplePersonService personService;
    private LockService lockService;
    private ApplicationDataRepository appDataRepository;
    private AppDataService appDataService;

    private static final String VALID_OWNER_ID = "12345";
    private static final String VALID_VIEWER_ID = "12345";
    private static final String VALID_USER_ID = "12345";
    private static final String VALID_APPLICATION_ID = "http://example.com/gadget.xml";
    private static final String VALID_APPLICATION_DATA_ID = "1";
    private static final Long VALID_MODULE_ID = 1l;

    private Map<String, String> validApplicationDataMap;
    private ApplicationData validApplicationData;

    private Person validPerson;

    @Before
    public void setup() {
        personService = createMock(SimplePersonService.class);
        lockService = createMock(LockService.class);
        appDataRepository = createMock(ApplicationDataRepository.class);
        appDataService = new DefaultAppDataService(personService, lockService, appDataRepository);

        validApplicationDataMap = new HashMap<String, String>();
        validApplicationDataMap.put("color", "blue");
        validApplicationDataMap.put("speed", "fast");
        validApplicationDataMap.put("state", "MA");
        validApplicationData = new ApplicationDataImpl(VALID_APPLICATION_DATA_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                validApplicationDataMap);

        validPerson = new PersonImpl();
        validPerson.setUsername(VALID_VIEWER_ID);
    }

    @Test
    public void getPersonData_validRequest_hasAppData() throws Exception {
        testGetPersonData(validApplicationData.getData().keySet(), VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                validApplicationData, validApplicationData.getData());
    }

    @Test
    public void getPersonData_validRequest_noAppData() throws Exception {
        testGetPersonData(validApplicationData.getData().keySet(), VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                null, new HashMap<String, String>());
    }

    @Test
    public void getPersonData_validRequest_hasAppData_nullFields() throws Exception {
        testGetPersonData(null, VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                validApplicationData, validApplicationData.getData());
    }

    @Test
    public void getPersonData_validRequest_hasAppData_emptyFields() throws Exception {
        testGetPersonData(new HashSet<String>(), VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                validApplicationData, validApplicationData.getData());
    }

    @Test
    public void getPersonData_validRequest_hasAppData_partialFields() throws Exception {
        HashMap<String, String> expectedData = new HashMap<String, String>(validApplicationDataMap);
        expectedData.remove("color");

        testGetPersonData(expectedData.keySet(), VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID,
                validApplicationData, expectedData);
    }

    @Test(expected = ProtocolException.class)
    public void getPersonData_invalidRequest_wrongApplication() throws Exception {
        testGetPersonData(validApplicationData.getData().keySet(), VALID_OWNER_ID, VALID_VIEWER_ID,
                "http://example.com/wrong.xml", validApplicationData, validApplicationData.getData());
    }

    @Test
    public void deletePersonData_validRequest_clearAllDataWithAllFields() throws Exception {
        Set<String> fieldsToDelete = new HashSet<String>(validApplicationData.getData().keySet());

        testDeletePersonData(fieldsToDelete, new HashMap<String, String>());
    }

    @Test
    public void deletePersonData_validRequest_clearAllDataWithNullFields() throws Exception {
        testDeletePersonData(null, new HashMap<String, String>());
    }

    @Test
    public void deletePersonData_validRequest_clearAllDataWithEmptyFields() throws Exception {
        testDeletePersonData(new HashSet<String>(), new HashMap<String, String>());
    }

    @Test
    public void deletePersonData_validRequest_clearSomeData() throws Exception {
        Set<String> fieldsToDelete = new HashSet<String>(validApplicationData.getData().keySet());
        fieldsToDelete.remove("color");
        HashMap<String, String> expectedApplicationDataAfterDelete = new HashMap<String, String>(validApplicationData.getData());
        for (String fieldToDelete : fieldsToDelete) {
            expectedApplicationDataAfterDelete.remove(fieldToDelete);
        }

        testDeletePersonData(fieldsToDelete, expectedApplicationDataAfterDelete);
    }

    @Test(expected = ProtocolException.class)
    public void deletePersonData_invalidRequest_wrongViewer() throws Exception {
        testDeletePersonData("11111", "11111", new HashSet<String>(), new HashMap<String, String>(),
                validApplicationData);
    }

    @Test
    public void deletePersonData_validRequest_nullApplicationData() throws Exception {
        testDeletePersonDataNoAppDataExpected(null);
    }

    @Test
    public void deletePersonData_validRequest_emptyApplicationData() throws Exception {
        ApplicationData applicationData = new ApplicationDataImpl();
        testDeletePersonDataNoAppDataExpected(applicationData);
    }


    @Test
    public void updatePersonData_validRequest_removeAllValues() throws Exception {
        HashMap<String, String> values = new HashMap<String, String>();
        testUpdatePersonData(null, values, values, validApplicationData);
    }

    @Test
    public void updatePersonData_validRequest_replaceAllValues_nullFields() throws Exception {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("newKey1", "newValue1");
        values.put("newKey2", "newValue2");
        testUpdatePersonData(null, values, values, validApplicationData);
    }

    @Test
    public void updatePersonData_validRequest_replaceAllValues_emptyFields() throws Exception {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("newKey1", "newValue1");
        values.put("newKey2", "newValue2");
        testUpdatePersonData(new HashSet<String>(), values, values, validApplicationData);
    }

    @Test
    public void updatePersonData_validRequest_appendNewValues() throws Exception {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("newKey1", "newValue1");
        values.put("newKey2", "newValue2");

        HashMap<String, String> expectedValuesAfterUpdate = new HashMap<String, String>(validApplicationData.getData());
        expectedValuesAfterUpdate.putAll(values);

        testUpdatePersonData(values.keySet(), values, expectedValuesAfterUpdate, validApplicationData);
    }

    @Test
    public void updatePersonData_validRequest_appendNewValues_nullApplicationData() throws Exception {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("newKey1", "newValue1");
        values.put("newKey2", "newValue2");

        testUpdatePersonData(values.keySet(), values, values, null);
    }

    @Test
    public void updatePersonData_validRequest_removeOneField() throws Exception {
        String propertyToRemove = "color";
        Set<String> fields = new HashSet<String>();
        fields.add(propertyToRemove);

        HashMap<String, String> expectedValuesAfterUpdate = new HashMap<String, String>(validApplicationData.getData());
        expectedValuesAfterUpdate.remove(propertyToRemove);

        testUpdatePersonData(fields, new HashMap<String, String>(), expectedValuesAfterUpdate, validApplicationData);
    }

    @Test
    public void updatePersonData_validRequest_updateExistingField() throws Exception {
        String propertyToUpdate = "color";
        String updatedValue = "ZZZZZZZZZZ";

        Set<String> fields = new HashSet<String>();
        fields.add(propertyToUpdate);

        HashMap<String, String> values = new HashMap<String, String>();
        values.put(propertyToUpdate, updatedValue);

        HashMap<String, String> expectedValuesAfterUpdate = new HashMap<String, String>(validApplicationData.getData());
        expectedValuesAfterUpdate.put(propertyToUpdate, updatedValue);

        testUpdatePersonData(fields, values, expectedValuesAfterUpdate, validApplicationData);
    }

    @Test(expected = ProtocolException.class)
    public void updatePersonData_invalidRequest_invalidArguments() throws Exception {
        Set<String> fields = new HashSet<String>();
        fields.add("foo");

        HashMap<String, String> values = new HashMap<String, String>();
        values.put("a key", "that is not present in the fields set");
        testUpdatePersonData(fields, values, values, validApplicationData);
    }

    private void testGetPersonData(Set<String> fields, String ownerId, String viewerId, String applicationId,
                                   ApplicationData applicationData, Map<String, String> expectedData) throws Exception {

        Set<UserId> userIds = new HashSet<UserId>(Arrays.asList(new UserId(UserId.Type.userId, VALID_USER_ID)));

        SecurityToken securityToken = getMockSecurityToken(ownerId, viewerId, applicationId, VALID_MODULE_ID);

        List<Person> users = Arrays.asList(validPerson);
        GroupId groupId = new GroupId(GroupId.Type.self, "@self");
        expect(personService.getPeople(userIds, groupId, null, securityToken)).andReturn(users);
        replay(personService);

        expect(appDataRepository.getApplicationData(convertPeopleToUserIds(users), applicationId)).andReturn(
                applicationData == null ? new ArrayList<ApplicationData>() : Arrays.asList(applicationData));
        replay(appDataRepository);

        Future<DataCollection> result = appDataService.getPersonData(userIds, groupId, VALID_APPLICATION_ID, fields,
                securityToken);
        Map<String, String> actualData = result.get().getEntry().get(viewerId);
        assertThat(actualData, is(not(nullValue())));
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            assertEquals(entry.getValue(), actualData.get(entry.getKey()));
        }
    }

    private void testDeletePersonData(Set<String> fieldsToDelete, HashMap<String, String> expectedApplicationDataAfterDelete) {
        testDeletePersonData(VALID_OWNER_ID, VALID_VIEWER_ID, fieldsToDelete, expectedApplicationDataAfterDelete,
                validApplicationData);
    }

    private void testDeletePersonData(String ownerId, String viewerId, Set<String> fieldsToDelete,
                                      HashMap<String, String> expectedApplicationDataAfterDelete,
                                      ApplicationData applicationData) {

        UserId userId = new UserId(UserId.Type.userId, VALID_USER_ID);
        Set<UserId> userIds = new HashSet<UserId>(Arrays.asList(userId));

        SecurityToken securityToken = getMockSecurityToken(ownerId, viewerId, VALID_APPLICATION_ID, VALID_MODULE_ID);

        List<Person> users = Arrays.asList(validPerson);
        GroupId groupId = new GroupId(GroupId.Type.self, "@self");
        expect(personService.getPeople(userIds, groupId, null, securityToken)).andReturn(users);
        replay(personService);

        expect(appDataRepository.getApplicationData(VALID_USER_ID, VALID_APPLICATION_ID)).andReturn(applicationData);
        Capture<ApplicationData> capturedApplicationData = new Capture<ApplicationData>();
        expect(appDataRepository.save(capture(capturedApplicationData))).andReturn(null);
        replay(appDataRepository);

        ReentrantLock lock = new ReentrantLock();
        expect(lockService.borrowLock(anyObject(String.class), anyObject(String.class))).andReturn(lock);
        lockService.returnLock(lock);
        replay(lockService);

        appDataService.deletePersonData(userId, groupId, VALID_APPLICATION_ID, fieldsToDelete, securityToken);

        ApplicationData expectedApplicationData = new ApplicationDataImpl(applicationData.getId(),
                applicationData.getUserId(), applicationData.getAppUrl(), expectedApplicationDataAfterDelete);

        ApplicationData actualApplicationData = capturedApplicationData.getValue();
        assertEquals(expectedApplicationData.getId(), actualApplicationData.getId());
        assertEquals(expectedApplicationData.getUserId(), actualApplicationData.getUserId());
        assertEquals(expectedApplicationData.getAppUrl(), actualApplicationData.getAppUrl());
        assertEquals(expectedApplicationData.getData(), actualApplicationData.getData());
    }

    private void testDeletePersonDataNoAppDataExpected(ApplicationData applicationData) throws InterruptedException, ExecutionException {

        UserId userId = new UserId(UserId.Type.userId, VALID_USER_ID);
        Set<UserId> userIds = new HashSet<UserId>(Arrays.asList(userId));

        SecurityToken securityToken = getMockSecurityToken(VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID, VALID_MODULE_ID);

        List<Person> users = Arrays.asList(validPerson);
        GroupId groupId = new GroupId(GroupId.Type.self, "@self");
        expect(personService.getPeople(userIds, groupId, null, securityToken)).andReturn(users);
        replay(personService);

        expect(appDataRepository.getApplicationData(VALID_USER_ID, VALID_APPLICATION_ID)).andReturn(applicationData);
        replay(appDataRepository);

        ReentrantLock lock = new ReentrantLock();
        expect(lockService.borrowLock(anyObject(String.class), anyObject(String.class))).andReturn(lock);
        lockService.returnLock(lock);
        replay(lockService);

        Future<Void> result = appDataService.deletePersonData(userId, groupId, VALID_APPLICATION_ID, null, securityToken);
        assertEquals(null, result.get());
    }

    private void testUpdatePersonData(Set<String> fields, Map<String, String> values,
                                      HashMap<String, String> expectedApplicationDataAfterUpdate,
                                      ApplicationData applicationData) {

        UserId userId = new UserId(UserId.Type.userId, VALID_USER_ID);
        Set<UserId> userIds = new HashSet<UserId>(Arrays.asList(userId));

        SecurityToken securityToken = getMockSecurityToken(VALID_OWNER_ID, VALID_VIEWER_ID, VALID_APPLICATION_ID, VALID_MODULE_ID);

        List<Person> users = Arrays.asList(validPerson);
        GroupId groupId = new GroupId(GroupId.Type.self, "@self");
        expect(personService.getPeople(userIds, groupId, null, securityToken)).andReturn(users);
        replay(personService);

        expect(appDataRepository.getApplicationData(VALID_USER_ID, VALID_APPLICATION_ID)).andReturn(applicationData);
        Capture<ApplicationData> capturedApplicationData = new Capture<ApplicationData>();
        expect(appDataRepository.save(capture(capturedApplicationData))).andReturn(null);
        replay(appDataRepository);

        ReentrantLock lock = new ReentrantLock();
        expect(lockService.borrowLock(anyObject(String.class), anyObject(String.class))).andReturn(lock);
        lockService.returnLock(lock);
        replay(lockService);

        appDataService.updatePersonData(userId, groupId, VALID_APPLICATION_ID, fields, values, securityToken);

        ApplicationDataImpl expectedApplicationData = applicationData == null ? new ApplicationDataImpl(null, VALID_USER_ID,
                VALID_APPLICATION_ID, expectedApplicationDataAfterUpdate) :
                new ApplicationDataImpl(applicationData.getId(), applicationData.getUserId(),
                        applicationData.getAppUrl(), expectedApplicationDataAfterUpdate);

        ApplicationData actualApplicationData = capturedApplicationData.getValue();
        assertEquals(expectedApplicationData.getId(), actualApplicationData.getId());
        assertEquals(expectedApplicationData.getUserId(), actualApplicationData.getUserId());
        assertEquals(expectedApplicationData.getAppUrl(), actualApplicationData.getAppUrl());
        assertEquals(expectedApplicationData.getData(), actualApplicationData.getData());
    }

    private SecurityToken getMockSecurityToken(String ownerId, String viewerId, String applicationId, Long moduleID) {
        SecurityToken securityToken;
        securityToken = createNiceMock(SecurityToken.class);
        expect(securityToken.getOwnerId()).andReturn(ownerId);
        expect(securityToken.getViewerId()).andReturn(viewerId);
        expect(securityToken.getAppId()).andReturn(applicationId);
        expect(securityToken.getModuleId()).andReturn(moduleID);
        replay(securityToken);
        return securityToken;
    }

    private List<String> convertPeopleToUserIds(List<Person> people) {
        List<String> ids = new ArrayList<String>(people.size());
        for (Person person : people) {
            ids.add(String.valueOf(person.getUsername()));
        }
        return ids;
    }
}
