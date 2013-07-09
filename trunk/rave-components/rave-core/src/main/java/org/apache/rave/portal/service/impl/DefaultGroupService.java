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

package org.apache.rave.portal.service.impl;

import org.apache.rave.model.Group;
import org.apache.rave.repository.GroupRepository;
import org.apache.rave.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "groupService")
public class DefaultGroupService implements GroupService {
    private static final Logger log = LoggerFactory.getLogger(DefaultGroupService.class);

    private final GroupRepository groupRepository;

    @Autowired
    public DefaultGroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group getGroupByID(String id) {
        return groupRepository.get(id);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.getAll();
    }

    @Override
    public Group getGroupByTitle(String title) {
        return groupRepository.findByTitle(title);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }
}
