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

import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.model.Tag;
import org.apache.rave.portal.model.conversion.JpaTagConverter;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.AuthorityRepository}
 */
@Repository
public class JpaTagRepository implements TagRepository {

    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
    private JpaTagConverter converter;


    @Override
    public List<Tag> getAll() {
        TypedQuery<JpaTag> query = manager.createNamedQuery(JpaTag.GET_ALL, JpaTag.class);
        return CollectionUtils.<Tag>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Tag> getLimitedList(int offset, int limit) {
        TypedQuery<JpaTag> query = manager.createNamedQuery(JpaTag.GET_ALL, JpaTag.class);
        return CollectionUtils.<Tag>toBaseTypedList(getPagedResultList(query, offset, limit));
    }

    @Override
    public Class<? extends Tag> getType(){
        return JpaTag.class;
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaTag.COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public JpaTag getByKeyword(String keyword) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        TypedQuery<JpaTag> query = manager.createNamedQuery(JpaTag.FIND_BY_KEYWORD, JpaTag.class);
        query.setParameter("keyword", keyword);
        return getSingleResult(query.getResultList());
    }

    @Override
    public Tag get(String id) {
        return manager.find(JpaTag.class, Long.parseLong(id));
    }

    @Override
    public Tag save(Tag item) {
        JpaTag tag = converter.convert(item);
        return saveOrUpdate(tag.getEntityId(), manager, tag);
    }

    @Override
    public void delete(Tag item) {
        manager.remove(item instanceof JpaTag ? item : getByKeyword(item.getKeyword()));
    }
}
