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
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.repository.PageRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpaPageRepository extends AbstractJpaRepository<Page> implements PageRepository{

    public JpaPageRepository() {
        super(Page.class);
    }

    @Override
    public List<Page> getAllPages(Long userId, PageType pageType) {
        TypedQuery<Page> query = manager.createNamedQuery(Page.GET_BY_USER_ID_AND_PAGE_TYPE, Page.class);
        query.setParameter("userId", userId);
        query.setParameter("pageType", pageType);
        return query.getResultList();
    }

    @Override
    public int deletePages(Long userId, PageType pageType) {
        TypedQuery<Page> query = manager.createNamedQuery(Page.DELETE_BY_USER_ID_AND_PAGE_TYPE, Page.class);
        query.setParameter("userId", userId);
        query.setParameter("pageType", pageType);
        return query.executeUpdate();
    }
}