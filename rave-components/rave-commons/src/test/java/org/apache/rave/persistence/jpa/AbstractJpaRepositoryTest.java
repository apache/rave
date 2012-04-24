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

package org.apache.rave.persistence.jpa;

import org.apache.rave.persistence.BasicEntity;
import org.apache.rave.persistence.Repository;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 */
public class AbstractJpaRepositoryTest {

    private static final long ID = 1L;
    private EntityManager manager;
    private Repository<TestEntity> repository;

    @Before
    public void setup() {
        manager = createNiceMock(EntityManager.class);
        repository = new TestJpaRepository(manager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getType() {
        assertThat((Class<TestEntity>) repository.getType(), is(equalTo(TestEntity.class)));
    }

    @Test
    public void get() {
        expect(manager.find(TestEntity.class, ID)).andReturn(new TestEntity(ID));
        replay(manager);

        TestEntity entity = repository.get(ID);
        assertThat(entity.getEntityId(), is(equalTo(ID)));
        verify(manager);
    }

    @Test
    public void save_persist() {
        TestEntity entity = new TestEntity(null);
        manager.persist(entity);
        expectLastCall();
        replay(manager);

        TestEntity result = repository.save(entity);
        assertThat(result, is(sameInstance(entity)));
        verify(manager);
    }


    @Test
    public void save_merge() {
        TestEntity entity = new TestEntity(ID);
        expect(manager.merge(entity)).andReturn(new TestEntity(ID));
        replay(manager);

        TestEntity result = repository.save(entity);
        assertThat(result, is(not(sameInstance(entity))));
        verify(manager);
    }


    @Test
    public void delete() {
        TestEntity entity = new TestEntity(ID);
        manager.remove(entity);
        expectLastCall();
        replay(manager);

        repository.delete(entity);
        verify(manager);
    }


    private class TestJpaRepository extends AbstractJpaRepository<TestEntity> {

        protected TestJpaRepository(EntityManager manager) {
            super(TestEntity.class);
            this.manager = manager;
        }
    }

    private class TestEntity implements BasicEntity{
        private Long entityId;

        private TestEntity(Long entityId) {
            this.entityId = entityId;
        }

        @Override
        public Long getEntityId() {
            return entityId;
        }

        @Override
        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }
    }
}
