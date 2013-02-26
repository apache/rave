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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;


/**
 * {@inheritDoc}
 * <p/>
 * Creates a LocalContainerEntityManagerFactoryBean that calls a DataSourcePopulator bean to insert data into the
 * DataSource immediately after the factory is initialized
 */
public class PopulatedLocalContainerEntityManagerFactory extends LocalContainerEntityManagerFactoryBean {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PopulatedLocalContainerEntityManagerFactory.class);

    private DataSourcePopulator populator;

    public PopulatedLocalContainerEntityManagerFactory() {
        super();
    }

    public void setPopulator(DataSourcePopulator populator) {
        this.populator = populator;
    }

    /**
     * Called after the EntityManagerFactory is initialized and processed.  Delegates to the data populator
     * any insertion of data into the DataSource
     *
     * @param emf the newly created EntityManagerFactory
     * @param pui the PersistenceUnit that the EntityManagerFactory was initialized for
     */
    @Override
    protected void postProcessEntityManagerFactory(EntityManagerFactory emf, PersistenceUnitInfo pui) {
        if (populator != null) {
            //Create an entity manager to force initialization of the context and then populate
            emf.createEntityManager().close();
            try {
                populator.initialize(this.getDataSource());
            } catch (RuntimeException e) {
                logger.error("Database population has failed. It will be empty.", e);
            }
        }
        super.postProcessEntityManagerFactory(emf, pui);
    }
}
