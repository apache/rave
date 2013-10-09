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

import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.JpaPageTemplate;
import org.apache.rave.model.PageTemplate;
import org.apache.rave.portal.model.conversion.JpaConverter;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaPageTemplateRepository implements PageTemplateRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<PageTemplate> getAll() {
        TypedQuery<JpaPageTemplate> query = manager.createNamedQuery(JpaPageTemplate.PAGE_TEMPLATE_GET_ALL, JpaPageTemplate.class);
        return CollectionUtils.<PageTemplate>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<PageTemplate> getLimitedList(int offset, int limit) {
        TypedQuery<JpaPageTemplate> query = manager.createNamedQuery(JpaPageTemplate.PAGE_TEMPLATE_GET_ALL, JpaPageTemplate.class);
        return CollectionUtils.<PageTemplate>toBaseTypedList(getPagedResultList(query, offset, limit));
    }

    @Override
    public int getCountAll() {
        return getAll().size();
    }

    @Override
    public List<PageTemplate> getAll(String pageType) {
        TypedQuery<JpaPageTemplate> query = manager.createNamedQuery(JpaPageTemplate.PAGE_TEMPLATE_GET_ALL_FOR_TYPE, JpaPageTemplate.class);
        query.setParameter("pageType", pageType);
        return CollectionUtils.<PageTemplate>toBaseTypedList(query.getResultList());
    }

    @Override
    public JpaPageTemplate getDefaultPage(String pageType) {
        TypedQuery<JpaPageTemplate> query = manager.createNamedQuery(JpaPageTemplate.PAGE_TEMPLATE_GET_DEFAULT_PAGE_BY_TYPE, JpaPageTemplate.class);
        query.setParameter("pageType", pageType.toUpperCase());
        return query.getSingleResult();
    }

    @Override
    public Class<? extends PageTemplate> getType() {
        return PageTemplate.class;
    }

    @Override
    public PageTemplate get(String id) {
        return manager.find(JpaPageTemplate.class, id);
    }

    @Override
    public PageTemplate save(PageTemplate template) {
        return (PageTemplate) saveOrUpdate(template.getId(), manager, JpaConverter.getInstance().convert(template, PageTemplate.class));
    }

    @Override
    public void delete(PageTemplate item) {
        manager.remove(JpaConverter.getInstance().convert(item, PageTemplate.class));
    }
}