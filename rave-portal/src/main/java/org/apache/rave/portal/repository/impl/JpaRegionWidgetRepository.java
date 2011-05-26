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

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.apache.rave.portal.repository.impl.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaRegionWidgetRepository implements RegionWidgetRepository {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public RegionWidget get(long regionWidgetId) {
        return manager.find(RegionWidget.class, regionWidgetId);
    }

    @Override
    @Transactional
    public RegionWidget save(RegionWidget regionWidget) {
        return saveOrUpdate(regionWidget.getId(), manager, regionWidget);
    }


}