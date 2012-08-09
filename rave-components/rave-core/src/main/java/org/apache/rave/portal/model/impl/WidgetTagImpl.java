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

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.WidgetTag;

import java.util.Date;

public class WidgetTagImpl implements WidgetTag {

    private String id;
    private String userId;
    private String widgetId;
    private Tag tag;
    private Date createdDate;

    public WidgetTagImpl(String widgetId) {
        this.widgetId = widgetId;
    }

    public WidgetTagImpl(String widgetId, String userId, Date createdDate, Tag tag) {
        this.widgetId = widgetId;
        this.userId = userId;
        this.tag = tag;
        this.createdDate = createdDate;
    }

    public WidgetTagImpl(String id, String widgetId, String userId, Date createdDate, Tag tag) {
        this.id = id;
        this.widgetId = widgetId;
        this.userId = userId;
        this.tag = tag;
        this.createdDate = createdDate;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getWidgetId() {
        return widgetId;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Tag getTag() {
        return this.tag;
    }

    @Override
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public Date getCreatedDate() {
       return this.createdDate;
    }

    @Override
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }
}
