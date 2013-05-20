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
package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.Group;
import org.apache.rave.portal.model.JpaGroup;
import org.apache.rave.repository.GroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaGroupRepositoryTest {

    private static final String GROUP_ID = "1";
    private static final String GROUP_TITLE = "Party";

    private static final String INVALID_GROUP_ID = "99";
    private static final String INVALID_GROUP_TITLE = "bogus";

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private GroupRepository repository;

    @Test
    public void getById_validId() {
        Group group = repository.get(GROUP_ID.toString());
        assertThat(group, notNullValue());
        assertThat(group.getTitle(), is(equalTo(GROUP_TITLE)));
        assertThat(group.getMemberIds().get(0), equalTo("1"));
        assertThat(group.getMemberIds().get(1), equalTo("5"));
    }

    @Test
    public void getById_invalidId() {
        Group group = repository.get(INVALID_GROUP_ID);
        assertThat(group, is(nullValue()));
    }

    @Test
    public void getType() {
        assertEquals(repository.getType(), JpaGroup.class);
    }

    @Test
    public void getList() {
        List<Group> list = repository.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void createGroup() {
        JpaGroup newGroup = new JpaGroup();
        newGroup.setTitle("TEST GROUP");
        newGroup.setDescription("TEST GROUP");
        newGroup.setOwnerId("1");
        newGroup.setMemberIds(Arrays.asList(new String[] {"1", "5"}));

        Group savedGroup = repository.save(newGroup);

        assertThat(savedGroup.getId(), notNullValue());
        assertThat(savedGroup.getTitle(), equalTo("TEST GROUP"));
        assertThat(savedGroup.getMemberIds().size(), equalTo(2));
    }

    @Test
    public void deleteGroup() {
        JpaGroup newGroup = new JpaGroup();
        newGroup.setTitle("TEST GROUP");
        newGroup.setDescription("TEST GROUP");
        newGroup.setOwnerId("1");
        newGroup.setMemberIds(Arrays.asList(new String[] {"1", "5"}));

        Group savedGroup = repository.save(newGroup);

        String id = savedGroup.getId();

        assertThat(savedGroup.getId(), notNullValue());

        Group foundGroup = repository.get(id);

        assertThat(foundGroup, notNullValue());

        repository.delete(savedGroup);

        foundGroup = repository.get(id);

        assertThat(foundGroup, nullValue());
    }

    @Test
    public void findByTitle() {
        Group group = repository.findByTitle(GROUP_TITLE);

        assertThat(group, notNullValue());
        assertThat(group.getTitle(), equalTo(GROUP_TITLE));
        assertThat(group.getId(), equalTo(GROUP_ID));
    }

    @Test
    public void findByTitle_Invalid() {
        Group group = repository.findByTitle(INVALID_GROUP_TITLE);

        assertThat(group, nullValue());
    }
}
