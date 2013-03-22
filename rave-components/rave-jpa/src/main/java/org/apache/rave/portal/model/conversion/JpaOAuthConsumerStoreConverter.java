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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaOAuthConsumerStore;
import org.apache.rave.model.OAuthConsumerStore;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a OAuthConsumerStore to a JpaOAuthConsumerStore
 */
@Component
public class JpaOAuthConsumerStoreConverter implements ModelConverter<OAuthConsumerStore, JpaOAuthConsumerStore> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<OAuthConsumerStore> getSourceType() {
        return OAuthConsumerStore.class;
    }

    @Override
    public JpaOAuthConsumerStore convert(OAuthConsumerStore source) {
        return source instanceof JpaOAuthConsumerStore ? (JpaOAuthConsumerStore) source : createEntity(source);
    }

    private JpaOAuthConsumerStore createEntity(OAuthConsumerStore source) {
        JpaOAuthConsumerStore converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaOAuthConsumerStore() : manager.find(JpaOAuthConsumerStore.class, Long.parseLong(source.getId()));
            if(converted == null) {
                converted = new JpaOAuthConsumerStore();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(OAuthConsumerStore source, JpaOAuthConsumerStore converted) {
        converted.setId(source.getId());
        converted.setServiceName(source.getServiceName());
        converted.setCallbackUrl(source.getCallbackUrl());
        converted.setConsumerKey(source.getConsumerKey());
        converted.setConsumerSecret(source.getConsumerSecret());
        converted.setGadgetUri(source.getGadgetUri());
        converted.setKeyName(source.getKeyName());
        converted.setKeyType(source.getKeyType());
    }
}
