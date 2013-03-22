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
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.JpaActivityStreamsEntry;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.UUID;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;


@Component
public class JpaActivityStreamsEntryConverter implements ModelConverter<ActivityStreamsEntry, JpaActivityStreamsEntry> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaActivityStreamsEntry convert(ActivityStreamsEntry source) {
        if(source != null && source.getId() == null) {
            source.setId(UUID.randomUUID().toString());
        }
        return source instanceof JpaActivityStreamsEntry ? (JpaActivityStreamsEntry) source : createEntity(source);
    }

    @Override
    public Class<ActivityStreamsEntry> getSourceType() {
        return ActivityStreamsEntry.class;
    }

    private JpaActivityStreamsEntry createEntity(ActivityStreamsEntry source) {
        JpaActivityStreamsEntry converted = null;
        if (source != null) {
            TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery(JpaActivityStreamsEntry.FIND_BY_ID, JpaActivityStreamsEntry.class);
            query.setParameter("id", source.getId());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaActivityStreamsEntry();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(ActivityStreamsEntry source, JpaActivityStreamsEntry converted) {
        converted.setId(source.getId());
        converted.setActor(source.getActor());
        converted.setOpenSocial(source.getOpenSocial());
        converted.setObject(source.getObject());
        converted.setObjectType(source.getObjectType());
        converted.setExtensions(source.getExtensions());
        converted.setGenerator(source.getGenerator());
        converted.setUrl(source.getUrl());
        converted.setIcon(source.getIcon());
        converted.setAppId(source.getAppId());
        converted.setProvider(source.getProvider());
        converted.setBcc(source.getBcc());
        converted.setBto(source.getBto());
        converted.setCc(source.getCc());
        converted.setContent(source.getContent());
        converted.setDc(source.getDc());
        converted.setContext(source.getContext());
        converted.setGeojson(source.getGeojson());
        converted.setGroupId(source.getGroupId());
        converted.setInReplyTo(source.getInReplyTo());
        converted.setLd(source.getLd());
        converted.setLinks(source.getLinks());
        converted.setEndTime(source.getEndTime());
        converted.setLocation(source.getLocation());
        converted.setMood(source.getMood());
        converted.setOdata(source.getOdata());
        converted.setPriority(source.getPriority());
        converted.setPublished(source.getPublished());
        converted.setTarget(source.getTarget());
        converted.setOpengraph(source.getOpengraph());
        converted.setProvider(source.getProvider());
        converted.setRating(source.getRating());
        converted.setResult(source.getResult());
        converted.setSchema_org(source.getSchema_org());
        converted.setSource(source.getSource());
        converted.setTags(source.getTags());
        converted.setTitle(source.getTitle());
        converted.setSource(source.getSource());
        converted.setTo(source.getTo());
        converted.setUpdated(source.getUpdated());
        converted.setUserId(source.getUserId());
        converted.setVerb(source.getVerb());
        converted.setUrl(source.getUrl());


    }
}
