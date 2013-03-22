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

package org.apache.rave.portal.model.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.model.ActivityStreamsObject;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ActivityStreamsObjectImpl implements ActivityStreamsObject {

    private static final long serialVersionUID = 1L;


    private List<ActivityStreamsObject> attachments;

    private ActivityStreamsObject author;

    private String content;

    private String displayName;

    private List<String> downstreamDuplicates;

    private String id;

    private ActivityStreamsMediaLink image;

    private String summary;

    private List<String> upstreamDuplicates;

    private String alias;

    private String attendedBy;

    private String attending;

    private String dc;

    private Date endTime;

    private String followers;

    private String following;

    private String friend_requests;

    private String friends;

    private String geojson;

    private String invited;

    private String likes;

    private String ld;

    private String links;

    private String location;

    private String maybeAttending;

    private String members;

    private String notAttendedBy;

    private String mood;

    private String notAttending;

    private String odata;

    private String opengraph;

    private String rating;

    private String replies;

    private String reviews;

    private String saves;

    private String schema_org;

    private String shares;

    private String source;

    private Date startTime;

    private Date published;

    private Date updated;

    private String url;

    private String objectType;

    private Map openSocial;

    private Map extensions;

    /**
     * Constructs an empty ActivityObject.
     */
    public ActivityStreamsObjectImpl() {
    }


    /**
     * {@inheritDoc}
     */

    public Date getPublished() {
        return published;
    }

    /**
     * {@inheritDoc}
     */
    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * {@inheritDoc}
     */

    public Map getOpenSocial() {
        return openSocial;
    }

    /**
     * {@inheritDoc}
     */
    public void setOpenSocial(Map openSocial) {

        this.openSocial = openSocial;
    }

    /**
     * {@inheritDoc}
     */

    public Map getExtensions() {
        return extensions;
    }

    /**
     * {@inheritDoc}
     */
    public void setExtensions(Map extensions) {
        this.extensions = extensions;
    }


    /**
     * {@inheritDoc}
     */

    private List<? extends ActivityStreamsObject> getNativeAttachments() {
        return attachments;
    }

    /**
     * {@inheritDoc}
     */
    private void setNativeAttachments(List<ActivityStreamsObject> attachments) {
        this.attachments = attachments;
    }

    public List<ActivityStreamsObject> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(List<ActivityStreamsObject> attachments) {
        this.attachments = attachments;
    }


    /**
     * {@inheritDoc}
     */

    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */

    public ActivityStreamsObject getAuthor() {
        return author;
    }

    /**
     * {@inheritDoc}
     */
    public void setAuthor(ActivityStreamsObject author) {
        this.author = author;
    }

    /**
     * {@inheritDoc}
     */

    public String getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * {@inheritDoc}
     */

    public String getDisplayName() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     */

    public List<String> getDownstreamDuplicates() {
        return downstreamDuplicates;
    }

    /**
     * {@inheritDoc}
     */
    public void setDownstreamDuplicates(List<String> downstreamDuplicates) {
        this.downstreamDuplicates = downstreamDuplicates;
    }

    /**
     * {@inheritDoc}
     */

    public ActivityStreamsMediaLink getImage() {
        return image;
    }

    /**
     * {@inheritDoc}
     */
    public void setImage(ActivityStreamsMediaLink image) {
        this.image = image;
    }


    /**
     * {@inheritDoc}
     */

    public String getSummary() {
        return summary;
    }

    /**
     * {@inheritDoc}
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * {@inheritDoc}
     */

    public List<String> getUpstreamDuplicates() {
        return upstreamDuplicates;
    }

    /**
     * {@inheritDoc}
     */
    public void setUpstreamDuplicates(List<String> upstreamDuplicates) {
        this.upstreamDuplicates = upstreamDuplicates;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFriend_requests() {
        return friend_requests;
    }

    public void setFriend_requests(String friend_requests) {
        this.friend_requests = friend_requests;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    public String getInvited() {
        return invited;
    }

    public void setInvited(String invited) {
        this.invited = invited;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLd() {
        return ld;
    }

    public void setLd(String ld) {
        this.ld = ld;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaybeAttending() {
        return maybeAttending;
    }

    public void setMaybeAttending(String maybeAttending) {
        this.maybeAttending = maybeAttending;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getNotAttendedBy() {
        return notAttendedBy;
    }

    public void setNotAttendedBy(String notAttendedBy) {
        this.notAttendedBy = notAttendedBy;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getNotAttending() {
        return notAttending;
    }

    public void setNotAttending(String notAttending) {
        this.notAttending = notAttending;
    }

    public String getOdata() {
        return odata;
    }

    public void setOdata(String odata) {
        this.odata = odata;
    }

    public String getOpengraph() {
        return opengraph;
    }

    public void setOpengraph(String opengraph) {
        this.opengraph = opengraph;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getSaves() {
        return saves;
    }

    public void setSaves(String saves) {
        this.saves = saves;
    }

    public String getSchema_org() {
        return schema_org;
    }

    public void setSchema_org(String schema_org) {
        this.schema_org = schema_org;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public List<ActivityStreamsObjectImpl> convertAll(List<ActivityStreamsObjectImpl> objects) {
        List<ActivityStreamsObjectImpl> entity = Lists.newArrayList();
        for (ActivityStreamsObjectImpl object : objects) {
            entity.add(object);
        }
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityStreamsObjectImpl that = (ActivityStreamsObjectImpl) o;

        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (attachments != null ? !attachments.equals(that.attachments) : that.attachments != null) return false;
        if (attendedBy != null ? !attendedBy.equals(that.attendedBy) : that.attendedBy != null) return false;
        if (attending != null ? !attending.equals(that.attending) : that.attending != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (dc != null ? !dc.equals(that.dc) : that.dc != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (downstreamDuplicates != null ? !downstreamDuplicates.equals(that.downstreamDuplicates) : that.downstreamDuplicates != null)
            return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) return false;
        if (followers != null ? !followers.equals(that.followers) : that.followers != null) return false;
        if (following != null ? !following.equals(that.following) : that.following != null) return false;
        if (friend_requests != null ? !friend_requests.equals(that.friend_requests) : that.friend_requests != null)
            return false;
        if (friends != null ? !friends.equals(that.friends) : that.friends != null) return false;
        if (geojson != null ? !geojson.equals(that.geojson) : that.geojson != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (invited != null ? !invited.equals(that.invited) : that.invited != null) return false;
        if (ld != null ? !ld.equals(that.ld) : that.ld != null) return false;
        if (likes != null ? !likes.equals(that.likes) : that.likes != null) return false;
        if (links != null ? !links.equals(that.links) : that.links != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (maybeAttending != null ? !maybeAttending.equals(that.maybeAttending) : that.maybeAttending != null)
            return false;
        if (members != null ? !members.equals(that.members) : that.members != null) return false;
        if (mood != null ? !mood.equals(that.mood) : that.mood != null) return false;
        if (notAttendedBy != null ? !notAttendedBy.equals(that.notAttendedBy) : that.notAttendedBy != null)
            return false;
        if (notAttending != null ? !notAttending.equals(that.notAttending) : that.notAttending != null) return false;
        if (objectType != null ? !objectType.equals(that.objectType) : that.objectType != null) return false;
        if (odata != null ? !odata.equals(that.odata) : that.odata != null) return false;
        if (openSocial != null ? !openSocial.equals(that.openSocial) : that.openSocial != null) return false;
        if (opengraph != null ? !opengraph.equals(that.opengraph) : that.opengraph != null) return false;
        if (published != null ? !published.equals(that.published) : that.published != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        if (replies != null ? !replies.equals(that.replies) : that.replies != null) return false;
        if (reviews != null ? !reviews.equals(that.reviews) : that.reviews != null) return false;
        if (saves != null ? !saves.equals(that.saves) : that.saves != null) return false;
        if (schema_org != null ? !schema_org.equals(that.schema_org) : that.schema_org != null) return false;
        if (shares != null ? !shares.equals(that.shares) : that.shares != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (updated != null ? !updated.equals(that.updated) : that.updated != null) return false;
        if (upstreamDuplicates != null ? !upstreamDuplicates.equals(that.upstreamDuplicates) : that.upstreamDuplicates != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attachments != null ? attachments.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (downstreamDuplicates != null ? downstreamDuplicates.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (upstreamDuplicates != null ? upstreamDuplicates.hashCode() : 0);
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (attendedBy != null ? attendedBy.hashCode() : 0);
        result = 31 * result + (attending != null ? attending.hashCode() : 0);
        result = 31 * result + (dc != null ? dc.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (followers != null ? followers.hashCode() : 0);
        result = 31 * result + (following != null ? following.hashCode() : 0);
        result = 31 * result + (friend_requests != null ? friend_requests.hashCode() : 0);
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        result = 31 * result + (geojson != null ? geojson.hashCode() : 0);
        result = 31 * result + (invited != null ? invited.hashCode() : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        result = 31 * result + (ld != null ? ld.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (maybeAttending != null ? maybeAttending.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (notAttendedBy != null ? notAttendedBy.hashCode() : 0);
        result = 31 * result + (mood != null ? mood.hashCode() : 0);
        result = 31 * result + (notAttending != null ? notAttending.hashCode() : 0);
        result = 31 * result + (odata != null ? odata.hashCode() : 0);
        result = 31 * result + (opengraph != null ? opengraph.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (replies != null ? replies.hashCode() : 0);
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        result = 31 * result + (saves != null ? saves.hashCode() : 0);
        result = 31 * result + (schema_org != null ? schema_org.hashCode() : 0);
        result = 31 * result + (shares != null ? shares.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (published != null ? published.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (objectType != null ? objectType.hashCode() : 0);
        result = 31 * result + (openSocial != null ? openSocial.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        return result;
    }
}
