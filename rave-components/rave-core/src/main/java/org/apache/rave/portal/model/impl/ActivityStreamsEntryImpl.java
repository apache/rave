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


import org.apache.rave.portal.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.ActivityStreamsItem;
import org.apache.rave.portal.model.ActivityStreamsObject;
import org.apache.rave.portal.model.ActivityStreamsMediaLink;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Date;

/**
 * A simple bean implementation of an ActivityStream Entry. See
 * http://docs.oracle.com/javaee/6/tutorial/doc/bnbqa.html to finish annotating
 * properly.
 */

public class ActivityStreamsEntryImpl implements ActivityStreamsEntry {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(ActivityStreamsEntryImpl.class.getName());


    private String id;


    private ActivityStreamsObject actor;


    private String content;


    private ActivityStreamsObject generator;


    private ActivityStreamsMediaLink icon;


    private ActivityStreamsItem object;


    private ActivityStreamsObject provider;


    private ActivityStreamsObject target;


    private String title;


    private String verb;

    //The user who verb'd this activity

    private String userId;

    //If this activity was generated as part of a group, this indicates the group's id

    private String groupId;

    //The id of the application that published this activity

    private String appId;


    private String bcc;


    private String bto;


    private String cc;


    private String context;


    private String dc;


    private Date endTime;


    private String geojson;


    private String inReplyTo;


    private String ld;


    private String links;


    private String location;


    private String mood;


    private String odata;


    private String opengraph;


    private String priority;


    private String rating;


    private String result;


    private String schema_org;


    private String source;


    private Date startTime;


    private String tags;


    private String to;

    private Date published;


    private Date updated;


    private String url;


    private String objectType;


    private HashMap openSocial;


    private HashMap extensions;

    /**
     * Create a new empty DeserializableActivityEntry
     */
    public ActivityStreamsEntryImpl() {
    }

    /** {@inheritDoc} */

    public Date getPublished() {
        return published;
    }

    /** {@inheritDoc} */
    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getUpdated(){
        return updated;
    }

    public void setUpdated(Date updated){
        this.updated=updated;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public String getObjectType(){
        return this.objectType;
    }

    public void setObjectType(String objectType){
        this.objectType=objectType;
    }

    /** {@inheritDoc} */

    public HashMap getOpenSocial() {
        return openSocial;
    }

    /**
     * {@inheritDoc}
     */
    public void setOpenSocial(HashMap openSocial) {

        this.openSocial = openSocial;
    }

    /** {@inheritDoc} */

    public HashMap getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(HashMap extensions) {


        this.extensions = extensions;
    }





    public ActivityStreamsObject getActor() {
        return actor;
    }

    /** {@inheritDoc} */
    public void setActor(ActivityStreamsObject actor) {
        this.actor = actor;
    }

    /** {@inheritDoc} */
    public String getContent() {
        return content;
    }

    /** {@inheritDoc} */
    public void setContent(String content) {
        this.content = content;
    }


    /** {@inheritDoc} */
    public ActivityStreamsObject getGenerator() {
        return generator;
    }

    /** {@inheritDoc} */
    public void setGenerator(ActivityStreamsObject generator) {
        this.generator = generator;
    }

    /** {@inheritDoc} */
    public ActivityStreamsMediaLink getIcon() {
        return icon;
    }

    /** {@inheritDoc} */
    public void setIcon(ActivityStreamsMediaLink icon) {
        this.icon = icon;
    }

    /** {@inheritDoc} */

    public String getId() {
        return id;
    }

    /** {@inheritDoc} */
    public void setId(String id) {
        this.id = id;
    }

    /** {@inheritDoc} */
    public ActivityStreamsItem getObject() {
        return object;
    }

    /** {@inheritDoc} */
    public void setObject(ActivityStreamsObject object) {
        this.object = object;
    }



    /** {@inheritDoc} */
    public ActivityStreamsObject getProvider() {
        return provider;
    }

    /** {@inheritDoc} */
    public void setProvider(ActivityStreamsObject provider) {
        this.provider = provider;
    }

    /** {@inheritDoc} */
    public ActivityStreamsObject getTarget() {
        return target;
    }

    /** {@inheritDoc} */
    public void setTarget(ActivityStreamsObject target) {
        this.target = target;
    }

    /** {@inheritDoc} */

    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    public void setTitle(String title) {
        this.title = title;
    }


    /** {@inheritDoc} */

    public String getVerb() {
        return verb;
    }

    /** {@inheritDoc} */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /** {@inheritDoc} */
    public String getUserId() {
        return userId;
    }

    /** {@inheritDoc} */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** {@inheritDoc} */
    public String getGroupId() {
        return groupId;
    }

    /** {@inheritDoc} */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    /** {@inheritDoc} */
    public String getAppId() {
        return appId;
    }

    /** {@inheritDoc} */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /** {@inheritDoc} */
    public String getBcc() {
        return bcc;
    }

    /** {@inheritDoc} */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /** {@inheritDoc} */
    public String getBto() {
        return bto;
    }

    /** {@inheritDoc} */
    public void setBto(String bto) {
        this.bto = bto;
    }

    /** {@inheritDoc} */
    public String getCc() {
        return cc;
    }

    /** {@inheritDoc} */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /** {@inheritDoc} */
    public String getContext() {
        return context;
    }

    /** {@inheritDoc} */
    public void setContext(String context) {
        this.context = context;
    }

    /** {@inheritDoc} */
    public String getDc() {
        return dc;
    }

    /** {@inheritDoc} */
    public void setDc(String dc) {
        this.dc = dc;
    }

    /** {@inheritDoc} */
    public Date getEndTime() {
        return endTime;
    }

    /** {@inheritDoc} */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /** {@inheritDoc} */
    public String getGeojson() {
        return geojson;
    }

    /** {@inheritDoc} */
    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    /** {@inheritDoc} */
    public String getInReplyTo() {
        return inReplyTo;
    }

    /** {@inheritDoc} */
    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    /** {@inheritDoc} */
    public String getLd() {
        return ld;
    }

    /** {@inheritDoc} */
    public void setLd(String ld) {
        this.ld = ld;
    }

    /** {@inheritDoc} */
    public String getLinks() {
        return links;
    }

    /** {@inheritDoc} */
    public void setLinks(String links) {
        this.links = links;
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return location;
    }

    /** {@inheritDoc} */
    public void setLocation(String location) {
        this.location = location;
    }

    /** {@inheritDoc} */
    public String getMood() {
        return mood;
    }

    /** {@inheritDoc} */
    public void setMood(String mood) {
        this.mood = mood;
    }

    /** {@inheritDoc} */
    public String getOdata() {
        return odata;
    }

    /** {@inheritDoc} */
    public void setOdata(String odata) {
        this.odata = odata;
    }

    /** {@inheritDoc} */
    public String getOpengraph() {
        return opengraph;
    }

    /** {@inheritDoc} */
    public void setOpengraph(String opengraph) {
        this.opengraph = opengraph;
    }

    /** {@inheritDoc} */
    public String getPriority() {
        return priority;
    }

    /** {@inheritDoc} */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /** {@inheritDoc} */
    public String getRating() {
        return rating;
    }

    /** {@inheritDoc} */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /** {@inheritDoc} */
    public String getResult() {
        return result;
    }

    /** {@inheritDoc} */
    public void setResult(String result) {
        this.result = result;
    }

    /** {@inheritDoc} */
    public String getSchema_org() {
        return schema_org;
    }

    /** {@inheritDoc} */
    public void setSchema_org(String schema_org) {
        this.schema_org = schema_org;
    }

    /** {@inheritDoc} */
    public String getSource() {
        return source;
    }

    /** {@inheritDoc} */
    public void setSource(String source) {
        this.source = source;
    }

    /** {@inheritDoc} */
    public Date getStartTime() {
        return startTime;
    }

    /** {@inheritDoc} */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /** {@inheritDoc} */
    public String getTags() {
        return tags;
    }

    /** {@inheritDoc} */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /** {@inheritDoc} */
    public String getTo() {
        return to;
    }

    /** {@inheritDoc} */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Sorts ActivityEntries in ascending order based on publish date.
     *
     * @param that
     *            is the DeserializableActivityEntry to compare to this DeserializableActivityEntry
     *
     * @return int represents how the ActivityEntries compare
     */

    public int compareTo(ActivityStreamsEntry that) {
        if (this.getPublished() == null && that.getPublished() == null) {
            return 0; // both are null, equal
        } else if (this.getPublished() == null) {
            return -1; // this is null, comes before real date
        } else if (that.getPublished() == null) {
            return 1; // that is null, this comes after
        } else { // compare publish dates in lexicographical order
            return this.getPublished().compareTo(that.getPublished());
        }
    }


}
