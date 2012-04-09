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

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaTagRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private TagRepository repository;


    private static final Long VALID_ID = 1L;

    @Test
    public void getById_validId() {
        final Tag tag = repository.get(VALID_ID);
        assertNotNull(tag);
        assertEquals(VALID_ID, tag.getEntityId());
        assertEquals(tag.getKeyword(), "news");
    }

    @Test
    public void getList() {
        List<Tag> list = repository.getAll();
        assertTrue(list.size() == 2);
        assertEquals(list.iterator().next().getKeyword(), "news");
        assertTrue(list.iterator().next().getWidgets().size() == 1);
    }

    @Test
    public void countAll() {
        int count = repository.getCountAll();
        assertTrue("Found at least 1 tag", count == 2);
    }

    @Test
    public void getByKeyword() {
        Tag tag = repository.getByKeyword("news");
        assertNotNull(tag);
        assertTrue(tag.getEntityId() == 1);
        tag = repository.getByKeyword("NEWS");
        assertNotNull(tag);
        assertTrue(tag.getEntityId() == 1);
        tag = repository.getByKeyword("news  ");
        assertNotNull(tag);
        assertTrue(tag.getEntityId() == 1);
        tag = repository.getByKeyword("   news  ");
        assertNotNull(tag);
        assertTrue(tag.getEntityId() == 1);

    }


}
