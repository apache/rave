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

import org.apache.rave.model.WidgetTag;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;

import static org.junit.Assert.*;

/**
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetTagRepositoryTest {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private WidgetRepository repository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void getByWidgetIdAndTag_valid(){
        String widgetId = "3";
        String keyword = "news";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagByWidgetIdAndKeyword(widgetId, keyword);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetId, jpaWidgetTag.getWidgetId());
        assertEquals(keyword, tagRepository.get(jpaWidgetTag.getTagId()).getKeyword());
    }

    @Test
    public void getByWidgetIdAndTag_keyword_trim_valid(){
        String widgetId = "3";
        String keyword = "  news    ";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagByWidgetIdAndKeyword(widgetId, keyword);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetId, jpaWidgetTag.getWidgetId());
        assertEquals(keyword.trim(), tagRepository.get(jpaWidgetTag.getTagId()).getKeyword());
    }

    @Test
    public void getByWidgetIdAndTag_invalid(){
        String widgetId = "3";
        String keyword = "saturday";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagByWidgetIdAndKeyword(widgetId, keyword);
        assertNull(jpaWidgetTag);
    }

    @Test
    public void getByWidgetIdAndTag_null(){
        String widgetId = "3";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagByWidgetIdAndKeyword(widgetId, null);
        assertNull(jpaWidgetTag);
    }

    @Test
    public void get_valid(){
        String id = "1";
        JpaWidgetTag widgetTag = (JpaWidgetTag)repository.getTagById(id);
        assertNotNull(widgetTag);
        assertEquals(id, widgetTag.getEntityId().toString());
    }

    @Test
    public void get_invalid(){
        String id = "1000291";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagById(id);
        assertNull(jpaWidgetTag);
    }

    @Test
    @Rollback(true)
    public void save() {
        JpaWidgetTag widgetTag = new JpaWidgetTag();
        JpaTag tag = new JpaTag(2L, "boing");
        widgetTag.setTagEntityId(tag.getEntityId());
        widgetTag.setCreatedDate(new Date());
        widgetTag.setUserEntityId(1L);
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.saveWidgetTag("1", widgetTag);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetTag.getTagId(), jpaWidgetTag.getTagId());
        assertEquals(widgetTag.getWidgetId(), jpaWidgetTag.getWidgetId());
        assertEquals(widgetTag.getUserId(), jpaWidgetTag.getUserId());
        assertEquals(widgetTag.getCreatedDate(), jpaWidgetTag.getCreatedDate());
        assertEquals(JpaWidgetTag.class, jpaWidgetTag.getClass());
    }

    @Test(expected = NullPointerException.class)
    @Rollback(true)
    public void save_null() {
        WidgetTag widgetTag = null;
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.saveWidgetTag("1", widgetTag);
        assertNull(jpaWidgetTag);
    }

    @Test
    @Rollback(true)
    public void delete_valid(){
        String id = "1";
        WidgetTag widgetTag = repository.getTagById(id);
        assertNotNull(widgetTag);
        repository.deleteWidgetTag(widgetTag);
        assertNull(repository.getTagById(id));
    }

    @Test
    @Rollback(true)
    public void delete_jpaWidgetTag_valid(){
        String id = "1";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getTagById(id);
        assertNotNull(jpaWidgetTag);
        repository.deleteWidgetTag(jpaWidgetTag);
        assertNull(repository.getTagById(id));
    }

    @Test(expected = NullPointerException.class)
    @Rollback(true)
    public void delete_invalid(){
        String id = "17827873261";
        WidgetTag widgetTag = repository.getTagById(id);
        assertNull(widgetTag);
        repository.deleteWidgetTag(widgetTag);
        assertNull(repository.get(id));
    }
}
