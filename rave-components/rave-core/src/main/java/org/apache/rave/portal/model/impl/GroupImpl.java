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

import org.apache.rave.model.Group;

import java.util.List;

public class GroupImpl implements Group {

    protected String id;
    protected String description;
    protected String owner;
    protected String title;
    protected List<String> members;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getOwnerId() {
        return owner;
    }

    @Override
    public void setOwnerId(String ownerId) {
        this.owner = ownerId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getMemberIds() {
        return members;
    }

    @Override
    public void setMemberIds(List<String> members) {
        this.members = members;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupImpl)) return false;

        GroupImpl group = (GroupImpl) o;

        if (description != null ? !description.equals(group.description) : group.description != null) return false;
        if (owner != null ? !owner.equals(group.owner) : group.owner != null) return false;
        if (members != null ? !members.equals(group.members) : group.members != null) return false;
        if (title != null ? !title.equals(group.title) : group.title != null) return false;

        return true;
    }
}
