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
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.JpaActivityStreamsObject;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.UUID;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;


@Component
public class JpaActivityStreamsObjectConverter implements ModelConverter<ActivityStreamsObject, JpaActivityStreamsObject> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaActivityStreamsObject convert(ActivityStreamsObject source) {
        if(source != null && source.getId() == null) {
            source.setId(UUID.randomUUID().toString());
        }
        return source instanceof JpaActivityStreamsObject ? (JpaActivityStreamsObject) source : createEntity(source);
    }

    @Override
    public Class<ActivityStreamsObject> getSourceType() {
        return ActivityStreamsObject.class;
    }

    private JpaActivityStreamsObject createEntity(ActivityStreamsObject source) {
        JpaActivityStreamsObject converted = null;
        if (source != null) {
            TypedQuery<JpaActivityStreamsObject> query = manager.createNamedQuery(JpaActivityStreamsObject.FIND_BY_ID, JpaActivityStreamsObject.class);
            query.setParameter("id", source.getId());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaActivityStreamsObject();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(ActivityStreamsObject source, JpaActivityStreamsObject converted) {
        converted.setId(source.getId());
        converted.setOpenSocial(source.getOpenSocial());
        converted.setObjectType(source.getObjectType());
        converted.setExtensions(source.getExtensions());
        converted.setDisplayName(source.getDisplayName());
        converted.setAlias(source.getAlias());
        converted.setAttachments(source.getAttachments());
        converted.setAttendedBy(source.getAttendedBy());
        converted.setAttending(source.getAttending());
        converted.setAuthor(source.getAuthor());
        converted.setDownstreamDuplicates(source.getDownstreamDuplicates());
        converted.setFollowers(source.getFollowers());
        converted.setFollowing(source.getFollowing());
        converted.setFriend_requests(source.getFriend_requests());
        converted.setImage(source.getImage());
        converted.setInvited(source.getInvited());
        converted.setLikes(source.getLikes());
        converted.setMaybeAttending(source.getMaybeAttending());
        converted.setNotAttendedBy(source.getNotAttendedBy());
        converted.setNotAttending(source.getNotAttending());
        converted.setSaves(source.getSaves());
        converted.setShares(source.getShares());
        converted.setReviews(source.getReviews());
        converted.setSummary(source.getSummary());
        converted.setUrl(source.getUrl());
        converted.setReplies(source.getReplies());
        converted.setContent(source.getContent());
        converted.setDc(source.getDc());
        converted.setGeojson(source.getGeojson());
        converted.setLd(source.getLd());
        converted.setLinks(source.getLinks());
        converted.setEndTime(source.getEndTime());
        converted.setLocation(source.getLocation());
        converted.setMood(source.getMood());
        converted.setOdata(source.getOdata());
        converted.setPublished(source.getPublished());
        converted.setOpengraph(source.getOpengraph());
        converted.setRating(source.getRating());
        converted.setSchema_org(source.getSchema_org());
        converted.setSource(source.getSource());
        converted.setUpdated(source.getUpdated());
        converted.setUrl(source.getUrl());
    }
}
