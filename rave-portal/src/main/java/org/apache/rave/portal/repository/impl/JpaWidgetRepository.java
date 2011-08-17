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


import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JpaWidgetRepository extends AbstractJpaRepository<Widget> implements WidgetRepository {

    private final Logger log = LoggerFactory.getLogger(JpaWidgetRepository.class);

    private int LARGE_PAGESIZE = 1000;

    public JpaWidgetRepository() {
        super(Widget.class);
    }

    @Override
    public List<Widget> getAll() {
        log.warn("Requesting potentially large resultset of Widget. No pagesize set.");
        TypedQuery<Widget> query = manager.createNamedQuery("Widget.getAll", Widget.class);
        return query.getResultList();
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery("Widget.getAll", Widget.class);
        query.setFirstResult(offset);
        if (pageSize >= LARGE_PAGESIZE) {
            log.warn("Requesting potentially large resultset of Widgets. Pagesize is {}", pageSize);
        }
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery("Widget.countAll");
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery("Widget.getByFreeText", Widget.class);
        query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");
        if (pageSize >= LARGE_PAGESIZE) {
            log.warn("Requesting potentially large resultset of Widgets. Pagesize is {}", pageSize);
        }
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        Query query = manager.createNamedQuery("Widget.countByFreeText");
        query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }
}
