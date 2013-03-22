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

public interface ActivityStreamsEntry extends ActivityStreamsItem {

    public ActivityStreamsObject getActor() ;


    public void setActor(ActivityStreamsObject actor);


    public String getContent();


    public void setContent(String content) ;

    public ActivityStreamsObject getGenerator() ;


    public void setGenerator(ActivityStreamsObject generator) ;


    public ActivityStreamsMediaLink getIcon();


    public void setIcon(ActivityStreamsMediaLink icon) ;


    public ActivityStreamsObject getObject() ;


    public void setObject(ActivityStreamsObject object) ;


    public ActivityStreamsObject getProvider();


    public void setProvider(ActivityStreamsObject provider) ;


    public ActivityStreamsObject getTarget() ;


    public void setTarget(ActivityStreamsObject target) ;


    public String getTitle() ;

    /** {@inheritDoc} */
    public void setTitle(String title) ;


    public String getVerb() ;


    public void setVerb(String verb) ;


    public String getUserId() ;


    public void setUserId(String userId) ;


    public String getGroupId() ;


    public void setGroupId(String groupId) ;

    public String getAppId() ;


    public void setAppId(String appId) ;


    public String getBcc() ;


    public void setBcc(String bcc) ;


    public String getBto() ;


    public void setBto(String bto) ;


    public String getCc() ;


    public void setCc(String cc) ;


    public String getContext() ;


    public void setContext(String context) ;


    public String getDc() ;


    public void setDc(String dc) ;


    public Date getEndTime() ;


    public void setEndTime(Date endTime) ;


    public String getGeojson() ;


    public void setGeojson(String geojson) ;


    public String getInReplyTo() ;


    public void setInReplyTo(String inReplyTo) ;


    public String getLd() ;


    public void setLd(String ld) ;


    public String getLinks() ;


    public void setLinks(String links) ;


    public String getLocation() ;


    public void setLocation(String location) ;


    public String getMood() ;


    public void setMood(String mood) ;


    public String getOdata() ;


    public void setOdata(String odata) ;


    public String getOpengraph() ;


    public void setOpengraph(String opengraph) ;


    public String getPriority() ;


    public void setPriority(String priority) ;


    public String getRating() ;


    public void setRating(String rating) ;


    public String getResult() ;


    public void setResult(String result) ;


    public String getSchema_org() ;


    public void setSchema_org(String schema_org) ;


    public String getSource() ;


    public void setSource(String source) ;


    public Date getStartTime() ;


    public void setStartTime(Date startTime) ;


    public String getTags() ;


    public void setTags(String tags) ;


    public String getTo() ;


    public void setTo(String to) ;
}
