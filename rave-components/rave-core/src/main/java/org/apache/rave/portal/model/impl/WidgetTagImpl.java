/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.WidgetTag;

import java.util.Date;

public class WidgetTagImpl implements WidgetTag {

    private Long widgetId;
    private User user;
    private Tag tag;
    private Date createdDate;

    public WidgetTagImpl() {
    }

    public WidgetTagImpl(Long widgetId, User user, Date createdDate, Tag tag) {
        this.widgetId = widgetId;
        this.user = user;
        this.tag = tag;
        this.createdDate = createdDate;
    }

    @Override
    public Long getWidgetId() {
        return this.widgetId;
    }

    @Override
    public void setWidgetId(Long id) {
        this.widgetId = id;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
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
