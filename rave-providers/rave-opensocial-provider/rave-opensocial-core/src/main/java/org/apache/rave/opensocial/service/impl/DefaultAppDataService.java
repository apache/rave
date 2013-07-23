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

package org.apache.rave.opensocial.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.ApplicationData;
import org.apache.rave.model.Person;
import org.apache.rave.opensocial.service.SimplePersonService;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.apache.rave.portal.repository.ApplicationDataRepository;
import org.apache.rave.service.LockService;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.DataCollection;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

/**
 * Implementation of the {@link AppDataService} SPI.
 */
@Service
public class DefaultAppDataService implements AppDataService {
    private final SimplePersonService personService;
    private final LockService lockService;
    private final ApplicationDataRepository applicationDataRepository;

    /**
     * These are the only visibility rules I can find in the OpenSocial specification regarding visibility of appdata:
     * <p/>
     * "This data store can be read by anyone who can see the gadget, but only the VIEWER's data is writable."
     * <p/>
     * So those are the only rules that this implementation currently enforces.
     */

    @Autowired
    public DefaultAppDataService(SimplePersonService personService, LockService lockService,
                                 ApplicationDataRepository applicationDataRepository) {
        this.personService = personService;
        this.lockService = lockService;
        this.applicationDataRepository = applicationDataRepository;
    }

    /**
     * Retrieves app data for the specified user list and group.
     *
     * @param userIds A set of UserIds
     * @param groupId The group
     * @param appId   The application ID
     * @param fields  The fields to filter the data by - empty set implies no filter
     * @param token   The security token
     * @return The data fetched
     */
    @Override
    public Future<DataCollection> getPersonData(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields,
                                                SecurityToken token) throws ProtocolException {
        //make sure the request conforms to the OpenSocial visibility rules
        List<String> personIds = validateReadRequest(userIds, groupId, appId, token);

        //fetch their appdata, convert it to a DataCollection and return it
        List<ApplicationData> applicationData = applicationDataRepository.getApplicationData(personIds, appId);
        DataCollection dataCollection = convertAppDataMapToDataCollection(personIds, applicationData, fields);
        return ImmediateFuture.newInstance(dataCollection);
    }

    /**
     * Deletes data for the specified user and group.
     *
     * @param userId  The user
     * @param groupId The group
     * @param appId   The application ID
     * @param fields  The fields to delete - empty set implies all fields
     * @param token   The security token
     * @return an error if one occurs
     */
    @Override
    public Future<Void> deletePersonData(UserId userId, GroupId groupId, String appId, Set<String> fields,
                                         SecurityToken token) throws ProtocolException {
        //make sure the request conforms to the OpenSocial visibility rules
        String personId = validateWriteRequest(userId, groupId, appId, token);

        //lock on this user and this application to avoid any potential concurrency issues
        Lock lock = getApplicationDataLock(personId, appId);
        try {
            lock.lock();

            //get the application data for this user and application
            ApplicationData applicationData = applicationDataRepository.getApplicationData(personId, appId);

            //if there is no data, there's nothing to delete, so we're done...
            if (applicationData == null || applicationData.getData() == null) {
                return ImmediateFuture.newInstance(null);
            }

            //remove the fields specified -- empty field set implies remove all, otherwise remove just the fields specified
            Map<String, String> data = applicationData.getData();
            if (fields == null || fields.size() == 0) {
                data.clear();
            } else {
                data.keySet().removeAll(fields);
            }

            //save our changes and return
            applicationDataRepository.save(applicationData);
        } finally {
            lock.unlock();
            lockService.returnLock(lock);
        }
        return ImmediateFuture.newInstance(null);
    }

    /**
     * Updates app data for the specified user and group with the new values.
     *
     * @param userId  The user
     * @param groupId The group
     * @param appId   The application ID
     * @param fields  The fields to update.  Empty set implies that all fields that should be persisted have been
     *                provided in the values map (completely replace current appData with new data).  A key in the
     *                fields set without a corresponding key in the values map implies a delete of that field.
     *                A key in the values map not present in the fields set is a bad request.
     * @param values  The values to set
     * @param token   The security token
     * @return an error if one occurs
     */
    @Override
    public Future<Void> updatePersonData(UserId userId, GroupId groupId, String appId, Set<String>
            fields, Map<String, String> values, SecurityToken token) throws ProtocolException {
        //make sure the request conforms to the OpenSocial visibility rules
        String personId = validateWriteRequest(userId, groupId, appId, token);

        //lock on this user and this application to avoid any potential concurrency issues
        Lock lock = getApplicationDataLock(personId, appId);
        try {
            lock.lock();
            //get the application data for this user and application
            ApplicationData applicationData = applicationDataRepository.getApplicationData(personId, appId);

            //if there is no data, create an empty object to store the data in that we'll save when we're done
            if (applicationData == null) {
                applicationData = new ApplicationDataImpl(null, personId, appId, new HashMap<String, String>());
            }

            //if the fields parameter is empty, we can just use the values map directly since this is a full update
            if (fields == null || fields.size() == 0) {
                applicationData.setData(values);
            }
            //if there are keys in the values map that aren't in the fields set, its a bad request
            else if (!fields.containsAll(values.keySet())) {
                throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Fields parameter must either be empty or contain keys " +
                        "for all name value pairs sent in request.");
            }
            //we have a partial update - we know that the fields set contains keys for all the entries in the values
            //map (due to the check above), so we can just enumerate over it now to finish our work.  So we want to remove
            //any fields found in the fields set that are not found in the values map and update the rest.
            else {
                Map<String, String> data = applicationData.getData();
                for (String field : fields) {
                    //if this field is not in the values map, its a delete
                    if (!values.containsKey(field)) {
                        data.remove(field);
                    } else {
                        //its an update
                        data.put(field, values.get(field));
                    }
                }
            }

            //save our changes and return
            applicationDataRepository.save(applicationData);
        } finally {
            lock.unlock();
            lockService.returnLock(lock);
        }
        return ImmediateFuture.newInstance(null);
    }

    private List<String> validateReadRequest(Set<UserId> userIds, GroupId groupId, String appId, SecurityToken token) {
        //if the appId in the token matches the appId parameter, then we know the user "can see the gadget"
        validateAppIdMatches(appId, token);

        //get the people we're supposed to be fetching data for
        List<Person> people = personService.getPeople(userIds, groupId, null, token);
        return convertPeopleToUserIds(people);
    }

    private String validateWriteRequest(UserId userId, GroupId groupId, String appId, SecurityToken token) {
        //do the read level validation first
        Set<UserId> userIds = new HashSet<UserId>(Arrays.asList(userId));
        List<String> personIds = validateReadRequest(userIds, groupId, appId, token);

        //and now check the write level validation which is "only the VIEWER's data is writable"
        if (personIds.size() != 1 || !personIds.get(0).equalsIgnoreCase(token.getViewerId())) {
            throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Writing appdata for anyone but the " +
                    "current viewer is forbidden.");
        }

        return personIds.get(0);
    }

    private void validateAppIdMatches(String appId, SecurityToken token) {
        if (StringUtils.isBlank(appId) || !appId.equalsIgnoreCase(token.getAppId())) {
            throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Requesting appdata for a different " +
                    "application is forbidden.");
        }
    }

    private List<String> convertPeopleToUserIds(List<Person> people) {
        List<String> ids = new ArrayList<String>(people.size());
        for (Person person : people) {
            ids.add(String.valueOf(person.getUsername()));
        }
        return ids;
    }

    private Lock getApplicationDataLock(String personId, String appId) {
        return lockService.borrowLock("ApplicationData", new StringBuilder(personId).append("-").append(appId).toString());
    }

    private DataCollection convertAppDataMapToDataCollection(List<String> personIds, List<ApplicationData> applicationData,
                                                             Set<String> fields) {
        //create the map that we'll use to associate users with their appdata
        Map<String, Map<String, String>> dataCollectionMap = new HashMap<String, Map<String, String>>();

        //enumerate the data we have mapping it back to the owner
        for (ApplicationData data : applicationData) {
            //create a map for our return values
            Map<String, String> returnData = new HashMap<String, String>();
            //if there isn't a set of fields to filter on return all user data, otherwise filter to the specified fields
            if (fields == null || fields.size() == 0) {
                returnData.putAll(data.getData());
            } else {
                //otherwise filter the values
                for (Map.Entry<String, String> userDataEntry : data.getData().entrySet()) {
                    if (fields.contains(userDataEntry.getKey())) {
                        returnData.put(userDataEntry.getKey(), userDataEntry.getValue());
                    }
                }
            }

            //put an entry in the data collection mapping the user and their appdata
            dataCollectionMap.put(data.getUserId(), returnData);
        }

        //now enumerate all of the personIds to be sure we have some data in the map for them, and if not, add empty data
        for (String personId : personIds) {
            if (!dataCollectionMap.containsKey(personId)) {
                dataCollectionMap.put(personId, new HashMap<String, String>());
            }
        }

        return new DataCollection(dataCollectionMap);
    }
}