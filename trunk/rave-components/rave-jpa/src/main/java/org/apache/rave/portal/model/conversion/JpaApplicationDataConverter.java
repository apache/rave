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
import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.JpaApplicationData;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a ApplicationData to a JpaApplicationData
 */
@Component
public class JpaApplicationDataConverter implements ModelConverter<ApplicationData, JpaApplicationData> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<ApplicationData> getSourceType() {
        return ApplicationData.class;
    }

    @Override
    public JpaApplicationData convert(ApplicationData source) {
        return source instanceof JpaApplicationData ? (JpaApplicationData) source : createEntity(source);
    }

    private JpaApplicationData createEntity(ApplicationData source) {
        JpaApplicationData converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaApplicationData() : manager.find(JpaApplicationData.class, Long.parseLong(source.getId()));
            if(converted == null) {
                converted = new JpaApplicationData();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(ApplicationData source, JpaApplicationData converted) {
        converted.setId(source.getId());
        converted.setAppUrl(source.getAppUrl());
        converted.setUserId(source.getUserId());
        converted.setData(source.getData());
    }
}
