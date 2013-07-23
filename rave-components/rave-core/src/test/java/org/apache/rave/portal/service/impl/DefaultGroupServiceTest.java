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
import org.apache.rave.portal.model.impl.GroupImpl;
import org.apache.rave.repository.GroupRepository;
import org.apache.rave.service.GroupService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DefaultGroupServiceTest {

    private GroupService service;
    private GroupRepository repository;

    @Before
    public void setup() {
        repository = createMock(GroupRepository.class);
        service = new DefaultGroupService(repository);
    }

    @Test
    public void getGroupById() {
        Group group = createGroup("test");
        expect(repository.get("1")).andReturn(group);
        replay(repository);
        Group sGroup = service.getGroupByID("1");
        assertEquals(sGroup, group);
        verify(repository);
    }


    private static Group createGroup(String title) {
        GroupImpl group = new GroupImpl();
        group.setTitle(title);
        return group;
    }

    @Test
    public void getGroupById_NotFound() {
        final String entityId = "456";
        expect(repository.get(entityId)).andReturn(null);
        replay(repository);
        Group sGroup = service.getGroupByID(entityId);
        assertNull(sGroup);

        verify(repository);
    }

    @Test
    public void allGroups() {
        List<Group> tags = new ArrayList<Group>();
        Group group = createGroup("test");
        tags.add(group);
        expect(repository.getAll()).andReturn(tags);
        replay(repository);
        List<Group> allGroups = service.getAllGroups();
        verify(repository);
        assertTrue(allGroups.size() > 0);
    }

    @Test
    public void findByTitle() {
        Group group = createGroup("test");
        expect(repository.findByTitle("test")).andReturn(group);
        expect(repository.findByTitle("TEST")).andReturn(group);
        expect(repository.findByTitle(" test")).andReturn(group);

    }
}
