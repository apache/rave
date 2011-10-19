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

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.AuthorityRepository}
 */
@Repository
public class JpaAuthorityRepository extends AbstractJpaRepository<Authority>
        implements AuthorityRepository {

    public JpaAuthorityRepository() {
        super(Authority.class);
    }

    @Override
    public Authority getByAuthority(String authorityName) {
        TypedQuery<Authority> query = manager.createNamedQuery(Authority.GET_BY_AUTHORITY_NAME, Authority.class);
        query.setParameter(Authority.PARAM_AUTHORITY_NAME, authorityName);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<Authority> getAll() {
        TypedQuery<Authority> query = manager.createNamedQuery(Authority.GET_ALL, Authority.class);
        return query.getResultList();
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(Authority.COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }
}
