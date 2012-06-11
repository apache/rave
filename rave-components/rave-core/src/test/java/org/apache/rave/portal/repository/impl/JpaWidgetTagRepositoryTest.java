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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.WidgetTagRepository;
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
    private WidgetTagRepository repository;

    @Test
    public void getByWidgetIdAndTag_valid(){
        Long widgetId = 3L;
        String keyword = "news";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getByWidgetIdAndTag(widgetId, keyword);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetId, jpaWidgetTag.getWidgetId());
        assertEquals(keyword, jpaWidgetTag.getTag().getKeyword());
    }

    @Test
    public void getByWidgetIdAndTag_keyword_trim_valid(){
        Long widgetId = 3L;
        String keyword = "  news    ";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getByWidgetIdAndTag(widgetId, keyword);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetId, jpaWidgetTag.getWidgetId());
        assertEquals(keyword.trim(), jpaWidgetTag.getTag().getKeyword());
    }

    @Test
    public void getByWidgetIdAndTag_invalid(){
        Long widgetId = 3L;
        String keyword = "saturday";
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.getByWidgetIdAndTag(widgetId, keyword);
        assertNull(jpaWidgetTag);
    }
    
    @Test
    public void get_valid(){
        Long id = 1L;
        JpaWidgetTag widgetTag = (JpaWidgetTag)repository.get(id);
        assertNotNull(widgetTag);
        assertEquals(id, widgetTag.getEntityId());
    }
    
    @Test
    public void get_invalid(){
        Long id = 1000291L;
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.get(id);
        assertNull(jpaWidgetTag);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void save() {
        WidgetTag widgetTag = new JpaWidgetTag();
        JpaTag tag = new JpaTag(100L, "boing");
        widgetTag.setTag(tag);
        widgetTag.setWidgetId(2L);
        widgetTag.setCreatedDate(new Date());
        widgetTag.setUser(new JpaUser());
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.save(widgetTag);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetTag.getTag().getKeyword(), jpaWidgetTag.getTag().getKeyword());
        assertEquals(widgetTag.getWidgetId(), jpaWidgetTag.getWidgetId());
        assertEquals(widgetTag.getUser(), jpaWidgetTag.getUser());
        assertEquals(widgetTag.getCreatedDate(), jpaWidgetTag.getCreatedDate());
        assertEquals(JpaWidgetTag.class, jpaWidgetTag.getClass());
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    @Rollback(true)
    public void save_null() {
        WidgetTag widgetTag = null;
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.save(widgetTag);
        assertNull(jpaWidgetTag);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void delete_valid(){
        Long id = 1L;
        WidgetTag widgetTag = repository.get(id);
        assertNotNull(widgetTag);
        repository.delete(widgetTag);
        assertNull(repository.get(id));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void delete_jpaWidgetTag_valid(){
        Long id = 1L;
        JpaWidgetTag jpaWidgetTag = (JpaWidgetTag)repository.get(id);
        assertNotNull(jpaWidgetTag);
        repository.delete(jpaWidgetTag);
        assertNull(repository.get(id));
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    @Rollback(true)
    public void delete_invalid(){
        Long id = 17827873261L;
        WidgetTag widgetTag = repository.get(id);
        assertNull(widgetTag);
        repository.delete(widgetTag);
        assertNull(repository.get(id));
    }
}
