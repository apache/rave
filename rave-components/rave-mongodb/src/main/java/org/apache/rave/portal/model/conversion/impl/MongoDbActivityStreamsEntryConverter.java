/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.MongoDbActivityStreamsEntry;
import org.apache.rave.portal.model.MongoDbActivityStreamsMediaLink;
import org.apache.rave.portal.model.MongoDbActivityStreamsObject;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbActivityStreamsEntryConverter implements HydratingModelConverter<ActivityStreamsEntry, MongoDbActivityStreamsEntry> {

    @Override
    public void hydrate(MongoDbActivityStreamsEntry dehydrated) {
    }

    @Override
    public Class<ActivityStreamsEntry> getSourceType() {
        return ActivityStreamsEntry.class;
    }

    @Override
    public MongoDbActivityStreamsEntry convert(ActivityStreamsEntry source) {
        if (source != null) {
            MongoDbActivityStreamsEntry converted = new MongoDbActivityStreamsEntry();
            converted.setActor(convert(source.getActor()));
            converted.setObject(convert(source.getObject()));
            converted.setGenerator(convert(source.getGenerator()));
            converted.setIcon(convert(source.getIcon()));
            converted.setProvider(convert(source.getProvider()));
            converted.setProvider(convert(source.getProvider()));
            converted.setTarget(convert(source.getTarget()));
            updateSimpleProperties(source, converted);

            return converted;
        }
        return null;
    }

    private ActivityStreamsMediaLink convert(ActivityStreamsMediaLink source) {
        if (source != null) {
            MongoDbActivityStreamsMediaLink converted = new MongoDbActivityStreamsMediaLink();
            converted.setId(source.getId() == null ? generateId() : source.getId());
            converted.setDuration(source.getDuration());
            converted.setHeight(source.getHeight());
            converted.setWidth(source.getWidth());
            converted.setUrl(source.getUrl());
            converted.setOpenSocial(source.getOpenSocial());
            return converted;
        }
        return null;
    }

    public ActivityStreamsObject convert(ActivityStreamsObject source) {
        if (source != null) {
            MongoDbActivityStreamsObject converted = new MongoDbActivityStreamsObject();
            converted.setAuthor(convert(source.getAuthor()));
            converted.setAttachments(convert(source.getAttachments()));
            converted.setImage(convert(source.getImage()));
            updateSimpleProperties(source, converted);
            return converted;
        }
        return null;
    }

    private List<ActivityStreamsObject> convert(List<ActivityStreamsObject> sourceObjects) {
        if (sourceObjects != null) {
            List<ActivityStreamsObject> converted = Lists.newArrayList();
            for (ActivityStreamsObject source : sourceObjects) {
                converted.add(convert(source));
            }
        }
        return null;
    }

    private void updateSimpleProperties(ActivityStreamsObject source, ActivityStreamsObjectImpl converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setOpenSocial(source.getOpenSocial());
        converted.setObjectType(source.getObjectType());
        converted.setExtensions(source.getExtensions());
        converted.setDisplayName(source.getDisplayName());
        converted.setAlias(source.getAlias());
        converted.setAttendedBy(source.getAttendedBy());
        converted.setAttending(source.getAttending());
        converted.setDownstreamDuplicates(source.getDownstreamDuplicates());
        converted.setFollowers(source.getFollowers());
        converted.setFollowing(source.getFollowing());
        converted.setFriend_requests(source.getFriend_requests());
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

    private void updateSimpleProperties(ActivityStreamsEntry source, ActivityStreamsEntry converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setOpenSocial(source.getOpenSocial());
        converted.setObjectType(source.getObjectType());
        converted.setExtensions(source.getExtensions());
        converted.setUrl(source.getUrl());
        converted.setAppId(source.getAppId());
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
        converted.setOpengraph(source.getOpengraph());
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
