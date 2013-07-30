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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.Group;
import org.apache.rave.portal.model.JpaGroup;
import org.apache.rave.portal.model.conversion.JpaGroupConverter;
import org.apache.rave.repository.GroupRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaGroupRepository implements GroupRepository {

    @Autowired
    private JpaGroupConverter converter;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<? extends Group> getType() {
        return JpaGroup.class;
    }

    @Override
    public Group get(String id) {
        long primaryKey = Long.parseLong(id);
        return manager.find(JpaGroup.class, primaryKey);
    }

    @Override
    public List<Group> getAll() {
        TypedQuery<JpaGroup> query = manager.createNamedQuery(JpaGroup.GET_ALL, JpaGroup.class);
        return CollectionUtils.<Group>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Group> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public Group save(Group item) {
        JpaGroup converted = converter.convert(item);
        return saveOrUpdate(converted.getEntityId(), manager, converted);
    }

    @Override
    public void delete(Group item) {
        manager.remove(converter.convert(item));
    }

    @Override
    public Group findByTitle(String title) {
        TypedQuery<JpaGroup> query = manager.createNamedQuery(JpaGroup.FIND_BY_TITLE, JpaGroup.class);
        query.setParameter(JpaGroup.GROUP_TITLE_PARAM, title);
        return getSingleResult(query.getResultList());
    }
}
