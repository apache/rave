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
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.repository.PageTypeRepository;
import org.apache.rave.portal.repository.RepositoryConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Repository
public class JpaPageTypeRepository extends AbstractJpaRepository<PageType> implements PageTypeRepository {

    public JpaPageTypeRepository() {
        super(PageType.class);
    }

    @Override
    public List<PageType> getAll() {
        return manager.createNamedQuery("PageType.getAll", PageType.class).getResultList();
    }

    @Override
    public PageType getByCode(String code) {
        TypedQuery<PageType> query = manager.createNamedQuery("PageType.getByCode", PageType.class);
        query.setParameter("code", code);
        return getSingleResult(query.getResultList());
    }

    @Override
    public PageType getUserPageType() {
        return getByCode(RepositoryConstants.PageType.USER);
    }

    @Override
    public PageType getPersonProfilePageType() {
        return getByCode(RepositoryConstants.PageType.PERSON_PROFILE);
    }

    @Override
    public PageType getSubPagePageType() {
        return getByCode(RepositoryConstants.PageType.SUB_PAGE);
    }
}