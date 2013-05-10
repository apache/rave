/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.Group;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.MongoDbGroup;
import org.springframework.stereotype.Component;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbGroupConverter implements HydratingModelConverter<Group, MongoDbGroup> {
    @Override
    public void hydrate(MongoDbGroup dehydrated) {
        // Does nothing
    }

    @Override
    public Class<Group> getSourceType() {
        return Group.class;
    }

    @Override
    public MongoDbGroup convert(Group source) {
        MongoDbGroup group = new MongoDbGroup();
        group.setId(source.getId() == null ? generateId() : source.getId());
        group.setTitle(source.getTitle());
        group.setOwnerId(source.getOwnerId());
        group.setDescription(source.getDescription());
        group.setMemberIds(source.getMemberIds());

        return group;
    }
}
