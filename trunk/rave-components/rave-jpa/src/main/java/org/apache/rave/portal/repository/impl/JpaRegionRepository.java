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

import org.apache.rave.portal.model.JpaRegion;
import org.apache.rave.model.Region;
import org.apache.rave.portal.model.conversion.JpaRegionConverter;
import org.apache.rave.portal.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;


@Repository
public class JpaRegionRepository implements RegionRepository {
    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaRegionConverter regionConverter;

    @Override
    public Class<? extends Region> getType() {
        return JpaRegion.class;
    }

    @Override
    public Region get(String id) {
        return manager.find(JpaRegion.class, Long.parseLong(id));
    }

    @Override
    public Region save(Region item) {
        JpaRegion region = regionConverter.convert(item);
        return saveOrUpdate(region.getEntityId(), manager, region);
    }

    @Override
    public void delete(Region item) {
        manager.remove(item instanceof JpaRegion ? item : get(item.getId()));
    }
}
