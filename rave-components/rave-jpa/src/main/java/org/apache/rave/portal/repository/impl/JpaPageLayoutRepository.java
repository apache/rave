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
import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.conversion.JpaPageLayoutConverter;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;


/**
 */
@Repository
public class JpaPageLayoutRepository implements PageLayoutRepository{

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaPageLayoutConverter converter;

    @Override
    public JpaPageLayout getByPageLayoutCode(String codename){
        TypedQuery<JpaPageLayout>query = manager.createNamedQuery(JpaPageLayout.PAGELAYOUT_GET_BY_LAYOUT_CODE,JpaPageLayout.class);
        query.setParameter("code",codename);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<PageLayout> getAll() {
        return CollectionUtils.<PageLayout>toBaseTypedList(manager.createNamedQuery(JpaPageLayout.PAGELAYOUT_GET_ALL, JpaPageLayout.class).getResultList());
    }

    @Override
    public List<PageLayout> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<PageLayout> getAllUserSelectable() {
        return CollectionUtils.<PageLayout>toBaseTypedList(manager.createNamedQuery(JpaPageLayout.PAGELAYOUT_GET_ALL_USER_SELECTABLE, JpaPageLayout.class).getResultList());
    }

    @Override
    public Class<? extends PageLayout> getType() {
        return JpaPageLayout.class;
    }

    @Override
    public PageLayout get(String id) {
        return manager.find(JpaPageLayout.class, Long.parseLong(id));
    }

    @Override
    public PageLayout save(PageLayout item) {
        JpaPageLayout layout = converter.convert(item);
        return saveOrUpdate(layout.getEntityId(), manager, layout);
    }

    @Override
    public void delete(PageLayout item) {
        manager.remove(converter.convert(item));
    }
}
