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

import org.apache.rave.opensocial.model.ApplicationData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:rave-shindig-test-applicationContext.xml",
        "classpath:rave-shindig-test-dataContext.xml"})
public class JpaApplicationDataRepositoryTest {
    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ApplicationDataRepository repository;

    private static final String VALID_USER_ID = "12345";
    private static final String VALID_APPLICATION_ID = "http://example.com/gadget.xml";
    private static final String SECOND_VALID_APPLICATION_ID = "http://example.com/gadget2.xml";
    private static final Long VALID_APPLICATION_DATA_ID = 1L;

    private Map<String, String> validApplicationDataMap;

    @Before
    public void setup() {
        validApplicationDataMap = new HashMap<String, String>();
        validApplicationDataMap.put("color", "blue");
        validApplicationDataMap.put("speed", "fast");
        validApplicationDataMap.put("state", "MA");
    }

    @Test
    public void get_valid() {
        ApplicationData applicationData = repository.get(VALID_APPLICATION_DATA_ID);
        validateApplicationData(applicationData);
    }

    @Test
    public void get_invalid() {
        ApplicationData applicationData = repository.get(-1L);
        assertThat(applicationData, is(nullValue()));
    }

    @Test
    public void getApplicationData_byUserIdAndApplicationId_valid() {
        ApplicationData applicationData = repository.getApplicationData(VALID_USER_ID, VALID_APPLICATION_ID);
        validateApplicationData(applicationData);
    }

    @Test
    public void getApplicationData_byUserIdAndApplicationId_invalid() {
        ApplicationData applicationData = repository.getApplicationData("-1", VALID_APPLICATION_ID);
        assertThat(applicationData, is(nullValue()));
    }

    @Test
    public void getApplicationData_byUserIdsAndApplicationId_valid() {
        List<ApplicationData> applicationData = repository.getApplicationData(Arrays.asList(VALID_USER_ID),
                VALID_APPLICATION_ID);
        validateApplicationData(applicationData.get(0));
    }

    @Test
    public void getApplicationData_byUserIdsAndApplicationId_invalid() {
        List<ApplicationData> applicationData = repository.getApplicationData(Arrays.asList("-1"),
                VALID_APPLICATION_ID);
        assertThat(applicationData, not(nullValue()));
        assertThat(applicationData.size(), is(equalTo(0)));
    }

    @Test
    public void getApplicationData_byUserIdsAndApplicationId_multipleUserIds_valid() {
        List<ApplicationData> applicationData = repository.getApplicationData(Arrays.asList(VALID_USER_ID, "NO-DATA-USER"),
                VALID_APPLICATION_ID);
        //Since there is no appdata in the database for "NO-DATA-USER" we should only get back one result
        assertThat(applicationData.size(), is(equalTo(1)));
        validateApplicationData(applicationData.get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save_newEntity() {
        ApplicationData applicationData = new ApplicationData(null, VALID_USER_ID, SECOND_VALID_APPLICATION_ID,
                validApplicationDataMap);

        ApplicationData saved = repository.save(applicationData);
        manager.flush();
        assertThat(saved.getEntityId(), is(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save_existingEntity() {
        ApplicationData applicationData = new ApplicationData(VALID_APPLICATION_DATA_ID, VALID_USER_ID,
                VALID_APPLICATION_ID, new HashMap<String, String>());

        ApplicationData saved = repository.save(applicationData);
        manager.flush();
        assertThat(saved, is(not(sameInstance(applicationData))));
        assertThat(saved.getEntityId(), is(equalTo(applicationData.getEntityId())));
    }

    private void validateApplicationData(ApplicationData applicationData) {
        assertThat(applicationData, is(not(nullValue())));
        assertThat(applicationData.getEntityId(), is(equalTo(VALID_APPLICATION_DATA_ID)));
        assertThat(applicationData.getUserId(), is(equalTo(VALID_USER_ID)));
        assertThat(applicationData.getAppUrl(), is(equalTo(VALID_APPLICATION_ID)));

        Map<String, String> actualData = applicationData.getData();
        for (Map.Entry<String, String> entry : validApplicationDataMap.entrySet()) {
            assertEquals(entry.getValue(), actualData.get(entry.getKey()));
        }
    }
}
