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

package org.apache.rave.portal.repository;

import org.apache.rave.persistence.BasicEntity;
import org.apache.rave.persistence.Repository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 */

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
@SuppressWarnings("unchecked")
@Ignore
//By iterating over all repositories in the context, we can be sure that basic repository functionality isn't broken by any overriding methods
public class AbstractJpaRepositoryTest {
    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private List<Repository> repositories;
    //NOTE: In order for tests to succeed, there must be an object with id of 1 in the store for every repository
    private static final Long VALID_ENTITY_ID = 1L;
    private static final Long INVALID_ENTITY_ID = -1L;

    @Test
    public void getById_validId() {
        for (Repository repository : repositories) {
            BasicEntity entity = (BasicEntity)repository.get(VALID_ENTITY_ID);
            assertThat(entity, is(notNullValue()));
            assertThat(entity.getEntityId(), is(equalTo(VALID_ENTITY_ID)));
        }
    }

    @Test
    public void getById_invalidId() {
        for (Repository repository : repositories) {
            BasicEntity entity = (BasicEntity)repository.get(INVALID_ENTITY_ID);
            assertThat(entity, is(nullValue()));
        }
    }

    @Test
    @Rollback(true)
    public void save_newEntity() throws Exception {
        for (Repository repository : repositories) {
            BasicEntity entity = constructNewEntityForRepository(repository);
            RepositoryTestUtils.populateAllRequiredFieldsInEntity(sharedManager, entity);
            BasicEntity saved = (BasicEntity)repository.save(entity);
            sharedManager.flush();
            assertThat(saved, is(sameInstance(entity)));
            assertThat(saved.getEntityId(), is(notNullValue()));
        }
    }

    @Test
    @Rollback(true)
    public void save_existingEntity() {
        for (Repository repository : repositories) {
            BasicEntity entity = constructNewEntityForRepository(repository);
            entity.setEntityId(VALID_ENTITY_ID);
            BasicEntity saved = (BasicEntity)repository.save(entity);
            sharedManager.flush();
            assertThat(saved, is(not(sameInstance(entity))));
            assertThat(saved.getEntityId(), is(equalTo(entity.getEntityId())));
        }

    }

    @Test
    @Rollback(true)
    public void delete() {
        for(Repository repository : repositories) {
            Object entity = repository.get(VALID_ENTITY_ID);
            repository.delete(entity);
            sharedManager.flush();
            assertThat(repository.get(VALID_ENTITY_ID), is(nullValue()));
        }
    }

    private BasicEntity constructNewEntityForRepository(Repository repository) {
        try {
            return (BasicEntity)repository.getType().newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
