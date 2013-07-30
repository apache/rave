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
import org.apache.commons.lang3.StringUtils;
import org.apache.rave.exception.DataSerializationException;
import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.JpaApplicationData;
import org.apache.rave.portal.model.conversion.JpaApplicationDataConverter;
import org.apache.rave.portal.repository.ApplicationDataRepository;
import org.apache.rave.util.CollectionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaApplicationDataRepository implements ApplicationDataRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaApplicationDataConverter converter;

    @Override
    public Class<? extends ApplicationData> getType() {
        return JpaApplicationData.class;
    }

    @Override
    public ApplicationData get(String id) {
        JpaSerializableApplicationData applicationData = (JpaSerializableApplicationData) manager.find(JpaApplicationData.class, Long.parseLong(id));
        if (applicationData != null) {
            applicationData.deserializeData();
        }
        return applicationData;
    }

    @Override
    @Transactional
    public JpaApplicationData save(ApplicationData item) {
        JpaApplicationData jpaAppData = converter.convert(item);
        JpaSerializableApplicationData jpaSerializableApplicationData = getJpaSerializableApplicationData(jpaAppData);
        jpaSerializableApplicationData.serializeData();
        return saveOrUpdate(jpaSerializableApplicationData.getEntityId(), manager, jpaSerializableApplicationData);
    }

    @Override
    public void delete(ApplicationData item) {
        manager.remove(item instanceof JpaApplicationData ? item : get(item.getId()));
    }

    @Override
    public List<ApplicationData> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<ApplicationData> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
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

        TypedQuery<JpaSerializableApplicationData> query = manager.createNamedQuery(JpaApplicationData.FIND_BY_USER_IDS_AND_APP_ID,
                JpaSerializableApplicationData.class);
        query.setParameter(JpaApplicationData.USER_IDS_PARAM, userIds);
        query.setParameter(JpaApplicationData.APP_URL_PARAM, appId);
        List<JpaSerializableApplicationData> results = query.getResultList();
        for (JpaSerializableApplicationData applicationData : results) {
            applicationData.deserializeData();
        }
        return CollectionUtils.<ApplicationData>toBaseTypedList(results);
    }

    @Override
    public JpaApplicationData getApplicationData(String personId, String appId) {
        TypedQuery<JpaSerializableApplicationData> query = manager.createNamedQuery(JpaApplicationData.FIND_BY_USER_ID_AND_APP_ID,
                JpaSerializableApplicationData.class);
        query.setParameter(JpaApplicationData.USER_ID_PARAM, personId);
        query.setParameter(JpaApplicationData.APP_URL_PARAM, appId);
        JpaSerializableApplicationData applicationData = getSingleResult(query.getResultList());
        if (applicationData != null) {
            applicationData.deserializeData();
        }
        return applicationData;
    }

    private JpaSerializableApplicationData getJpaSerializableApplicationData(JpaApplicationData applicationData) {
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
    public static class JpaSerializableApplicationData extends JpaApplicationData {
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
