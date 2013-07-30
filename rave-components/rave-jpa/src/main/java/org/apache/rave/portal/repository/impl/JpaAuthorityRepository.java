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
import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.JpaAuthority;
import org.apache.rave.portal.model.conversion.JpaAuthorityConverter;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.AuthorityRepository}
 */
@Repository
public class JpaAuthorityRepository implements AuthorityRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaAuthorityConverter converter;


    @Override
    public Authority getByAuthority(String authorityName) {
        TypedQuery<JpaAuthority> query = manager.createNamedQuery(JpaAuthority.GET_BY_AUTHORITY_NAME, JpaAuthority.class);
        query.setParameter(JpaAuthority.PARAM_AUTHORITY_NAME, authorityName);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<Authority> getAll() {
        TypedQuery<JpaAuthority> query = manager.createNamedQuery(JpaAuthority.GET_ALL, JpaAuthority.class);
        return CollectionUtils.<Authority>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Authority> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<Authority> getAllDefault() {
        TypedQuery<JpaAuthority> query = manager.createNamedQuery(JpaAuthority.GET_ALL_DEFAULT, JpaAuthority.class);
        return CollectionUtils.<Authority>toBaseTypedList(query.getResultList());
    }    

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaAuthority.COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public Class<? extends Authority> getType() {
        return JpaAuthority.class;
    }

    @Override
    public Authority get(String id) {
        return manager.find(JpaAuthority.class, Long.parseLong(id));
    }

    @Override
    public Authority save(Authority item) {
        JpaAuthority authority = converter.convert(item);
        return saveOrUpdate(authority.getEntityId(), manager, authority);
    }

    @Override
    public void delete(Authority item) {
        manager.remove(converter.convert(item));
    }
}
