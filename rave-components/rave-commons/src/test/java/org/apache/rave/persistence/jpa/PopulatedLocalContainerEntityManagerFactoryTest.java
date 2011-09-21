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

import org.apache.rave.jdbc.util.DataSourcePopulator;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import static org.easymock.EasyMock.*;

/**
 */
public class PopulatedLocalContainerEntityManagerFactoryTest {
    private PopulatedLocalContainerEntityManagerFactory factory;
    private DataSourcePopulator populator;
    private EntityManagerFactory innerFactory;
    private EntityManager manager;
    private PersistenceUnitInfo info;
    private DataSource dataSource;

    @Before
    public void setup() {
        factory = new TestPopulatedLocalContainerEntityManagerFactory();
        populator = createNiceMock(DataSourcePopulator.class);
        innerFactory = createNiceMock(EntityManagerFactory.class);
        manager = createNiceMock(EntityManager.class);
        info = createNiceMock(PersistenceUnitInfo.class);
        dataSource = createNiceMock(DataSource.class);
    }

    @Test
    public void postProcess_nullPopulator() {
        replay(info);
        replay(innerFactory);
        factory.postProcessEntityManagerFactory(innerFactory, info);
        verify(info);
        verify(innerFactory);
    }

    @Test
    public void postProcess_validPopulator() {
        populator.initialize(dataSource);
        expectLastCall();
        replay(populator);

        manager.close();
        expectLastCall();
        replay(manager);

        expect(innerFactory.createEntityManager()).andReturn(manager);
        replay(innerFactory);
        replay(info);

        factory.setPopulator(populator);
        factory.setDataSource(dataSource);

        factory.postProcessEntityManagerFactory(innerFactory, info);

        verify(info);
        verify(innerFactory);
        verify(populator);
        verify(manager);
    }

    private class TestPopulatedLocalContainerEntityManagerFactory extends PopulatedLocalContainerEntityManagerFactory {}
}
