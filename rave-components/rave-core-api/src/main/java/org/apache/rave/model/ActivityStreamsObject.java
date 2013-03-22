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

package org.apache.rave.model;

import java.util.Date;
import java.util.List;


public interface ActivityStreamsObject extends ActivityStreamsItem {

    public List<ActivityStreamsObject> getAttachments() ;

    public void setAttachments(List<ActivityStreamsObject> attachments) ;

    public ActivityStreamsObject getAuthor() ;

    public void setAuthor(ActivityStreamsObject author) ;

    public String getContent() ;

    public void setContent(String content) ;

    public String getDisplayName() ;

    public void setDisplayName(String displayName) ;

    public List<String> getDownstreamDuplicates() ;

    public void setDownstreamDuplicates(List<String> downstreamDuplicates) ;

    public ActivityStreamsMediaLink getImage() ;

    public void setImage(ActivityStreamsMediaLink image) ;

    public String getSummary() ;

    public void setSummary(String summary) ;

    public List<String> getUpstreamDuplicates() ;

    public void setUpstreamDuplicates(List<String> upstreamDuplicates) ;

    public String getAlias() ;

    public void setAlias(String alias) ;

    public String getAttendedBy() ;

    public void setAttendedBy(String attendedBy) ;

    public String getAttending() ;

    public void setAttending(String attending) ;

    public String getDc() ;

    public void setDc(String dc) ;

    public Date getEndTime() ;

    public void setEndTime(Date endTime) ;

    public String getFollowers() ;

    public void setFollowers(String followers) ;

    public String getFollowing() ;

    public void setFollowing(String following) ;

    public String getFriend_requests() ;

    public void setFriend_requests(String friend_requests) ;

    public String getFriends() ;

    public void setFriends(String friends) ;

    public String getGeojson() ;

    public void setGeojson(String geojson) ;

    public String getInvited() ;

    public void setInvited(String invited) ;

    public String getLikes() ;

    public void setLikes(String likes) ;

    public String getLd() ;

    public void setLd(String ld) ;

    public String getLinks() ;

    public void setLinks(String links) ;

    public String getLocation() ;

    public void setLocation(String location) ;

    public String getMaybeAttending() ;

    public void setMaybeAttending(String maybeAttending) ;

    public String getMembers() ;

    public void setMembers(String members) ;

    public String getNotAttendedBy() ;

    public void setNotAttendedBy(String notAttendedBy) ;

    public String getMood() ;

    public void setMood(String mood) ;

    public String getNotAttending() ;

    public void setNotAttending(String notAttending) ;

    public String getOdata() ;

    public void setOdata(String odata) ;

    public String getOpengraph() ;

    public void setOpengraph(String opengraph) ;

    public String getRating() ;

    public void setRating(String rating) ;

    public String getReplies() ;

    public void setReplies(String replies) ;

    public String getReviews() ;

    public void setReviews(String reviews) ;

    public String getSaves() ;

    public void setSaves(String saves) ;

    public String getSchema_org() ;

    public void setSchema_org(String schema_org) ;

    public String getShares() ;

    public void setShares(String shares) ;

    public String getSource() ;

    public void setSource(String source) ;

    public Date getStartTime() ;

    public void setStartTime(Date startTime) ;

}
