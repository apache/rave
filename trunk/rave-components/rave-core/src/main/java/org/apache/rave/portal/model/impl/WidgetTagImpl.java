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

import org.apache.rave.model.Tag;
import org.apache.rave.model.User;
import org.apache.rave.model.WidgetTag;

import java.util.Date;

public class WidgetTagImpl implements WidgetTag {

    private String userId;
    private String tagId;
    private Date createdDate;

    public WidgetTagImpl() {
    }

    public WidgetTagImpl(User user, Date createdDate, Tag tag) {
        this.userId = user.getId();
        this.tagId = tag.getId();
        this.createdDate = createdDate;
    }

    public WidgetTagImpl(String userId, Date createdDate, String tagId) {
        this.userId = userId;
        this.tagId = tagId;
        this.createdDate = createdDate;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getTagId() {
        return this.tagId;
    }

    @Override
    public Date getCreatedDate() {
       return this.createdDate;
    }
}
