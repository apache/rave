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
package org.apache.rave.opensocial.repository.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.rave.exception.DataSerializationException;
import org.apache.rave.opensocial.model.ApplicationData;
import org.apache.rave.opensocial.repository.ApplicationDataRepository;
import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.TypedQuery;
import java.util.*;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Repository
public class JpaApplicationDataRepository extends AbstractJpaRepository<ApplicationData>
        implements ApplicationDataRepository {

    public JpaApplicationDataRepository() {
        super(JpaSerializableApplicationData.class);
    }

    @Override
    public List<ApplicationData> getApplicationData(List<String> userIds, String appId) {
        //if the call is only looking for data for a single user use the more efficient single user variant transparently
        if (userIds.size() == 1) {
            List<ApplicationData> data = new ArrayList<ApplicationData>();
            ApplicationData applicationData = getApplicationData(userIds.get(0), appId);
            if (applicationData != null) {
                data.add(applicationData);
            }
            return data;
        }

        TypedQuery<JpaSerializableApplicationData> query = manager.createNamedQuery(ApplicationData.FIND_BY_USER_IDS_AND_APP_ID,
                JpaSerializableApplicationData.class);
        query.setParameter(ApplicationData.USER_IDS_PARAM, userIds);
        query.setParameter(ApplicationData.APP_URL_PARAM, appId);
        List<JpaSerializableApplicationData> results = query.getResultList();
        for (JpaSerializableApplicationData applicationData : results) {
            applicationData.deserializeData();
        }
        return new ArrayList<ApplicationData>(results);
    }

    @Override
    public ApplicationData getApplicationData(String personId, String appId) {
        TypedQuery<JpaSerializableApplicationData> query = manager.createNamedQuery(ApplicationData.FIND_BY_USER_ID_AND_APP_ID,
                JpaSerializableApplicationData.class);
        query.setParameter(ApplicationData.USER_ID_PARAM, personId);
        query.setParameter(ApplicationData.APP_URL_PARAM, appId);
        JpaSerializableApplicationData applicationData = getSingleResult(query.getResultList());
        if (applicationData != null) {
            applicationData.deserializeData();
        }
        return applicationData;
    }

    @Override
    public ApplicationData get(long id) {
        JpaSerializableApplicationData applicationData = (JpaSerializableApplicationData) super.get(id);
        if (applicationData != null) {
            applicationData.deserializeData();
        }
        return applicationData;
    }

    @Override
    @Transactional
    public ApplicationData save(ApplicationData applicationData) {
        JpaSerializableApplicationData jpaSerializableApplicationData = getJpaSerializableApplicationData(applicationData);
        jpaSerializableApplicationData.serializeData();
        return super.save(jpaSerializableApplicationData);
    }

    private JpaSerializableApplicationData getJpaSerializableApplicationData(ApplicationData applicationData) {
        if (applicationData instanceof JpaSerializableApplicationData) {
            return (JpaSerializableApplicationData) applicationData;
        }

        return new JpaSerializableApplicationData(applicationData.getEntityId(), applicationData.getUserId(),
                applicationData.getAppUrl(), applicationData.getData());
    }

    /**
     * This class is here so that the details of the persistence strategy in use for serializing the appdata map to a
     * JSON string doesnt end up being reflected in any public API of the ApplicationData object itself.
     * <p/>
     * This allows the public API of this repository to deal in clean ApplicationData models, but under the covers it
     * uses this model for the actual persistence to the database.
     */
    @Entity
    public static class JpaSerializableApplicationData extends ApplicationData {
        @Lob
        @Column(name = "serialized_data")
        private String serializedData;

        public JpaSerializableApplicationData() {
            super();
        }

        public JpaSerializableApplicationData(Long entityId, String userId, String appUrl, Map<String, String> data) {
            super(entityId, userId, appUrl, data);
        }

        public void serializeData() {
            serializedData = new JSONObject(this.getData()).toString();
        }

        public void deserializeData() {
            try {
                Map<String, String> data = new HashMap<String, String>();
                if (StringUtils.isNotBlank(serializedData)) {
                    JSONObject jsonObject = new JSONObject(serializedData);
                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        data.put(key, (String) jsonObject.get(key));
                    }
                }
                this.setData(data);
            } catch (JSONException e) {
                throw new DataSerializationException("Exception caught while deserializing data: " + serializedData, e);
            }
        }
    }
}