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

import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.conversion.JpaRegionWidgetConverter;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;


@Repository
public class JpaRegionWidgetRepository implements RegionWidgetRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaRegionWidgetConverter regionWidgetConverter;

    @Override
    public Class<? extends RegionWidget> getType() {
        return JpaRegionWidget.class;
    }

    @Override
    public RegionWidget get(String id) {
        return manager.find(JpaRegionWidget.class, Long.parseLong(id));
    }

    @Override
    public RegionWidget save(RegionWidget item) {
        JpaRegionWidget region = regionWidgetConverter.convert(item);
        return saveOrUpdate(region.getEntityId(), manager, region);
    }

    @Override
    public void delete(RegionWidget item) {
        manager.remove(item instanceof JpaRegionWidget ? item : get(item.getId()));
    }
}