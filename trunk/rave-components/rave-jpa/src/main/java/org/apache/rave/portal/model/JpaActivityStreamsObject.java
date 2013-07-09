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

package org.apache.rave.portal.model;


import com.google.common.collect.Lists;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Object")
@NamedQueries({
        @NamedQuery(name = JpaActivityStreamsObject.FIND_BY_ID, query = "SELECT a FROM JpaActivityStreamsObject a WHERE a.id = :id")
})
public class JpaActivityStreamsObject extends JpaActivityStreamsItem implements ActivityStreamsObject {

    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_ID = "JpaActivityStreamsObject.findById";

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<JpaActivityStreamsObject> attachments;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private JpaActivityStreamsObject author;

    @Basic
    private String content;

    @Basic
    private String displayName;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> downstreamDuplicates;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected JpaActivityStreamsMediaLink image;

    @Basic
    private String summary;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> upstreamDuplicates;

    @Basic
    private String alias;

    @Basic
    private String attendedBy;

    @Basic
    private String attending;

    @Basic
    private String dc;

    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endTime;

    @Basic
    private String followers;

    @Basic
    private String following;

    @Basic
    private String friend_requests;

    @Basic
    private String friends;

    @Basic
    private String geojson;

    @Basic
    private String invited;

    @Basic
    private String likes;

    @Basic
    private String ld;

    @Basic
    private String links;

    @Basic
    private String location;

    @Basic
    private String maybeAttending;

    @Basic
    private String members;

    @Basic
    private String notAttendedBy;

    @Basic
    private String mood;

    @Basic
    private String notAttending;

    @Basic
    private String odata;

    @Basic
    private String opengraph;

    @Basic
    private String rating;

    @Basic
    private String replies;

    @Basic
    private String reviews;

    @Basic
    private String saves;

    @Basic
    private String schema_org;

    @Basic
    private String shares;

    @Basic
    private String source;

    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startTime;

    /**
     * Constructs an empty ActivityObject.
     */
    public JpaActivityStreamsObject() {
    }


    public List<ActivityStreamsObject> getAttachments() {
        return ConvertingListProxyFactory.createProxyList(ActivityStreamsObject.class, this.attachments);
    }

    public void setAttachments(List<ActivityStreamsObject> attachments) {
        if(this.attachments == null) {
            this.attachments = Lists.newArrayList();
        }
        this.attachments.clear();
        if(attachments != null) {
            this.getAttachments().addAll(attachments);
        }
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
        this.author = JpaConverter.getInstance().convert(author, ActivityStreamsObject.class);
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
        this.image = JpaConverter.getInstance().convert(image, ActivityStreamsMediaLink.class);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaActivityStreamsObject other = (JpaActivityStreamsObject) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

}
