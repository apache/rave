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

import org.apache.rave.model.Category;
import org.apache.rave.portal.model.JpaCategory;
import org.apache.rave.portal.model.JpaWidget;
import org.apache.rave.portal.model.conversion.JpaCategoryConverter;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.CategoryRepository}
 */
@Repository
public class JpaCategoryRepository implements CategoryRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaCategoryConverter categoryConverter;

    @Override
    public Class<? extends Category> getType() {
        return JpaCategory.class;
    }

    @Override
    public Category get(String id) {
        return manager.find(JpaCategory.class, Long.parseLong(id));
    }

    @Override
    public Category save(Category item) {
        JpaCategory category = categoryConverter.convert(item);
        return saveOrUpdate(category.getEntityId(), manager, category);
    }

    @Override
    public void delete(Category item) {
        manager.remove(item instanceof JpaCategory ? item : get(item.getId()));
    }

    @Override
    public List<Category> getAll() {
        List<JpaCategory> resultList = manager.createNamedQuery(JpaCategory.GET_ALL, JpaCategory.class).getResultList();
        return CollectionUtils.<Category>toBaseTypedList(resultList);
    }

    @Override
    public List<Category> getLimitedList(int offset, int limit) {
        TypedQuery<JpaCategory> query = manager.createNamedQuery(JpaCategory.GET_ALL, JpaCategory.class);
        return CollectionUtils.<Category>toBaseTypedList(getPagedResultList(query, offset, limit));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaCategory.GET_COUNT);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public int removeFromCreatedOrModifiedFields(String userId) {
       List<Category> categories = getAll();
       int numRecordsChanged = 0;
       for (Category category : categories) {
           boolean changed = false;
           String createdUser = category.getCreatedUserId();
           String lastModifiedUser = category.getLastModifiedUserId();
           if (createdUser != null && userId.equals(createdUser)) {
               category.setCreatedUserId(null);
               changed = true;
               }
           if (lastModifiedUser != null && userId.equals(lastModifiedUser)) {
               category.setLastModifiedUserId(null);
               changed = true;
               }
           if (changed) {
               numRecordsChanged++;
               save(category);
               }
           }
       return numRecordsChanged;

    }
}