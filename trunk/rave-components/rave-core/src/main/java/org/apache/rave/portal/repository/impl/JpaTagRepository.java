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
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.AuthorityRepository}
 */
@Repository
public class JpaTagRepository extends AbstractJpaRepository<Tag>
        implements TagRepository {

    public JpaTagRepository() {
        super(Tag.class);
    }


    @Override
    public List<Tag> getAll() {
        TypedQuery<Tag> query = manager.createNamedQuery(Tag.GET_ALL, Tag.class);
        return query.getResultList();
    }


    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(Tag.COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public Tag getByKeyword(String keyword) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        TypedQuery<Tag> query = manager.createNamedQuery(Tag.FIND_BY_KEYWORD, Tag.class);
        query.setParameter("keyword", keyword);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<Tag> getAvailableTagsByWidgetId(Long widgetId) {
        TypedQuery<Tag> query = manager.createNamedQuery(Tag.GET_ALL_NOT_IN_WIDGET, Tag.class);
        query.setParameter("widgetId", widgetId);
        return query.getResultList();
    }

}
