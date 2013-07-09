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
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.portal.model.JpaActivityStreamsMediaLink;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.UUID;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;


@Component
public class JpaActivityStreamsMediaLinkConverter implements ModelConverter<ActivityStreamsMediaLink, JpaActivityStreamsMediaLink> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaActivityStreamsMediaLink convert(ActivityStreamsMediaLink source) {
        if(source != null && source.getId() == null) {
            source.setId(source.getUrl() == null ? UUID.randomUUID().toString() : source.getUrl());
        }
        return source instanceof JpaActivityStreamsMediaLink ? (JpaActivityStreamsMediaLink) source : createEntity(source);
    }

    @Override
    public Class<ActivityStreamsMediaLink> getSourceType() {
        return ActivityStreamsMediaLink.class;
    }

    private JpaActivityStreamsMediaLink createEntity(ActivityStreamsMediaLink source) {
        JpaActivityStreamsMediaLink converted = null;
        if (source != null) {
            TypedQuery<JpaActivityStreamsMediaLink> query = manager.createNamedQuery(JpaActivityStreamsMediaLink.FIND_BY_ID, JpaActivityStreamsMediaLink.class);
            query.setParameter("id", source.getId());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaActivityStreamsMediaLink();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(ActivityStreamsMediaLink source, JpaActivityStreamsMediaLink converted) {
        converted.setDuration(source.getDuration());
        converted.setHeight(source.getHeight());
        converted.setWidth(source.getWidth());
        converted.setUrl(source.getUrl());
    }
}
