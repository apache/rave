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

import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.JpaApplicationData;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.apache.rave.portal.repository.ApplicationDataRepository;
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
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml",
        "classpath:test-dataContext.xml"})
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
    public void getType() {
        assertEquals(repository.getType(), JpaApplicationData.class);
    }

    @Test
    public void get_valid() {
        JpaApplicationData applicationData = (JpaApplicationData) repository.get(VALID_APPLICATION_DATA_ID.toString());
        validateApplicationData(applicationData);
    }

    @Test
    public void get_invalid() {
        ApplicationData applicationData = repository.get("-1");
        assertThat(applicationData, is(nullValue()));
    }

    @Test
    public void getApplicationData_byUserIdAndApplicationId_valid() {
        JpaApplicationData applicationData = (JpaApplicationData) repository.getApplicationData(VALID_USER_ID, VALID_APPLICATION_ID);
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
        validateApplicationData((JpaApplicationData)applicationData.get(0));
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
        validateApplicationData((JpaApplicationData)applicationData.get(0));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save_newEntity() {
        ApplicationData applicationData = new JpaApplicationData(null, VALID_USER_ID, SECOND_VALID_APPLICATION_ID,
                validApplicationDataMap);

        JpaApplicationData saved = (JpaApplicationData)repository.save(applicationData);
        manager.flush();
        assertThat(saved.getEntityId(), is(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save_existingEntity() {
        JpaApplicationData applicationData = new JpaApplicationData(VALID_APPLICATION_DATA_ID, VALID_USER_ID,
                VALID_APPLICATION_ID, new HashMap<String, String>());

        JpaApplicationData saved = (JpaApplicationData)repository.save(applicationData);
        manager.flush();
        assertThat(saved, is(not(sameInstance(applicationData))));
        assertThat(saved.getEntityId(), is(equalTo(applicationData.getEntityId())));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_jpaObject() {
        ApplicationData applicationData = repository.get(VALID_APPLICATION_DATA_ID.toString());
        assertThat(applicationData, is(notNullValue()));
        repository.delete(applicationData);
        applicationData = repository.get(VALID_APPLICATION_DATA_ID.toString());
        assertThat(applicationData, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_implObject() {
        ApplicationData applicationData = repository.get(VALID_APPLICATION_DATA_ID.toString());
        assertThat(applicationData, is(notNullValue()));
        ApplicationDataImpl impl = new ApplicationDataImpl(applicationData.getId());
        repository.delete(impl);
        applicationData = repository.get(VALID_APPLICATION_DATA_ID.toString());
        assertThat(applicationData, is(nullValue()));
    }

    private void validateApplicationData(JpaApplicationData applicationData) {
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
