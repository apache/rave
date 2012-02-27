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
import org.apache.rave.portal.model.PageTemplate;
import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpaPageTemplateRepository extends AbstractJpaRepository<PageTemplate> implements PageTemplateRepository {

    public JpaPageTemplateRepository() {
        super(PageTemplate.class);
    }

    @Override
    public List<PageTemplate> getAll() {
        TypedQuery<PageTemplate> query = manager.createNamedQuery(PageTemplate.PAGE_TEMPLATE_GET_ALL, PageTemplate.class);
        return query.getResultList();
    }

    @Override
    public PageTemplate getDefaultPersonPage() {
        TypedQuery<PageTemplate> query = manager.createNamedQuery(PageTemplate.PAGE_TEMPLATE_GET_DEFAULT_PERSON_PAGE, PageTemplate.class);
        query.setParameter("pageType", PageType.PERSON_PROFILE);
        return query.getSingleResult();
    }
}