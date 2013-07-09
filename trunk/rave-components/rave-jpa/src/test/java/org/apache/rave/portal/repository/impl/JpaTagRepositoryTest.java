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

import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.model.Tag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.model.WidgetTag;
import org.apache.rave.portal.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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
    public void getType() {
        assertEquals(repository.getType(), JpaTag.class);
    }

    @Test
    public void getById_validId() {
        final Tag tag = repository.get(VALID_ID.toString());
        assertNotNull(tag);
        assertEquals(VALID_ID, ((JpaTag)tag).getEntityId());
        assertEquals(tag.getKeyword(), "news");
    }

    @Test
    public void getList() {
        List<Tag> list = repository.getAll();
        assertTrue(list.size() == 3);
        assertEquals(list.iterator().next().getKeyword(), "misc");
    }

    @Test
    public void countAll() {
        int count = repository.getCountAll();
        assertTrue("Found at least 1 tag", count == 3);
    }

    @Test
    public void getByKeyword() {
        Tag tag = repository.getByKeyword("news");
        assertNotNull(tag);
        assertTrue(((JpaTag)tag).getEntityId() == 1);
        tag = repository.getByKeyword("NEWS");
        assertNotNull(tag);
        assertTrue(((JpaTag)tag).getEntityId() == 1);
        tag = repository.getByKeyword("news  ");
        assertNotNull(tag);
        assertTrue(((JpaTag)tag).getEntityId() == 1);
        tag = repository.getByKeyword("   news  ");
        assertNotNull(tag);
        assertTrue(((JpaTag)tag).getEntityId() == 1);
        tag = repository.getByKeyword(null);
        assertNull(tag);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save_valid(){
        JpaTag tag = new JpaTag();
        String ordnance = "ordnance";
        tag.setKeyword(ordnance);
        repository.save(tag);
        Tag foundTag = repository.getByKeyword(ordnance);
        assertNotNull(foundTag);
        assertEquals(tag.getKeyword(), foundTag.getKeyword());
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    @Rollback(true)
    public void save_null(){
        Tag tag = null;
        tag = repository.save(tag);
        assertNull(tag);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void delete_valid_jpaTag(){
        String keyword = "misc";
        JpaTag jpaTag = (JpaTag)repository.getByKeyword(keyword);
        assertNotNull(jpaTag);
        repository.delete(jpaTag);
        assertNull(repository.getByKeyword(keyword));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void delete_valid_tagImpl(){
        String keyword = "misc";
        // make sure we do have a tag with the keyword in the db
        JpaTag control = (JpaTag)repository.getByKeyword(keyword);
        assertNotNull(control);
        // create a tag with the keyword not of JpaTag.class for branch coverage
        TagImpl tag = new TagImpl(keyword);
        assertNotNull(tag);
        repository.delete(tag);
        assertNull(repository.getByKeyword(keyword));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void delete_invalid(){
        String keyword = "abdhjdhlnews";
        TagImpl tag = new TagImpl(keyword);
        assertNotNull(tag);
        repository.delete(tag);
    }

//    @Test
//    public void getAvailableTagsByWidgetId() {
//        final Long WIDGET_ID = 3L;
//        List<Tag> list = repository.getAvailableTagsByWidgetId(WIDGET_ID.toString());
//        assertTrue(list.size() == 2);
//    }
}
