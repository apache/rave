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


import org.apache.commons.lang.StringUtils;
import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Repository
public class JpaWidgetRepository extends AbstractJpaRepository<Widget> implements WidgetRepository {

    private final Logger log = LoggerFactory.getLogger(JpaWidgetRepository.class);

    public JpaWidgetRepository() {
        super(Widget.class);
    }

    @Override
    public List<Widget> getAll() {
        log.warn("Requesting potentially large resultset of Widget. No pagesize set.");
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_ALL, Widget.class);
        return query.getResultList();
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_ALL, Widget.class);
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(Widget.WIDGET_COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_BY_FREE_TEXT,
                Widget.class);
        setFreeTextSearchTerm(query, searchTerm);
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        Query query = manager.createNamedQuery(Widget.WIDGET_COUNT_BY_FREE_TEXT);
        setFreeTextSearchTerm(query, searchTerm);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_BY_STATUS,
                Widget.class);
        query.setParameter(Widget.PARAM_STATUS, widgetStatus);
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        Query query = manager.createNamedQuery(Widget.WIDGET_COUNT_BY_STATUS);
        query.setParameter(Widget.PARAM_STATUS, widgetStatus);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm,
                                                            int offset, int pageSize) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<Widget> query = cb.createQuery(Widget.class);
        final Root<Widget> widgetType = query.from(Widget.class);
        query.where(getStatusAndTypeAndFreeTextPredicates(cb, widgetType, widgetStatus, type, searchTerm));
        query.orderBy(getOrderByTitleAsc(cb, widgetType));

        return getPagedResultList(manager.createQuery(query), offset, pageSize);
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<Widget> widgetType = query.from(Widget.class);
        query.select(cb.count(widgetType));
        query.where(getStatusAndTypeAndFreeTextPredicates(cb, widgetType, widgetStatus, type, searchTerm));

        final Long countResult = manager.createQuery(query).getSingleResult();
        return countResult.intValue();
    }

    private Predicate[] getStatusAndTypeAndFreeTextPredicates(CriteriaBuilder cb, Root<Widget> widgetType,
                                                              WidgetStatus widgetStatus, String type,
                                                              String searchTerm) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (StringUtils.isNotBlank(searchTerm)) {
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(getTitleField(widgetType)), getLowercaseWildcardSearchTerm(searchTerm)),
                            cb.like(cb.lower(getDescriptionField(widgetType)), getLowercaseWildcardSearchTerm(searchTerm))
                    )
            );
        }
        if (StringUtils.isNotBlank(type)) {
            predicates.add(cb.and(cb.equal(getTypeField(widgetType), type)));
        }
        if (widgetStatus != null) {
            predicates.add(cb.and(cb.equal(getWidgetStatusField(widgetType), widgetStatus)));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Order getOrderByTitleAsc(CriteriaBuilder cb, Root<Widget> widgetType) {
        return cb.asc(getTitleField(widgetType));
    }

    private Path<String> getTitleField(Root<Widget> widgetType) {
        return widgetType.get("title");
    }

    private Path<String> getDescriptionField(Root<Widget> widgetType) {
        return widgetType.get("description");
    }

    private Path<String> getTypeField(Root<Widget> widgetType) {
        return widgetType.get("type");
    }

    private Path<WidgetStatus> getWidgetStatusField(Root<Widget> widgetType) {
        return widgetType.get("widgetStatus");
    }


    @Override
    public Widget getByUrl(String widgetUrl) {
        if (StringUtils.isBlank(widgetUrl)) {
            throw new IllegalArgumentException("Widget URL must not be empty");
        }
        
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_BY_URL, Widget.class);
        // url is a unique field, so no paging needed
        query.setParameter(Widget.PARAM_URL, widgetUrl);
        final List<Widget> resultList = query.getResultList();
        return getSingleResult(resultList);
    }

    /**
     * Sets input as free text search term to a query
     *
     * @param query      {@link javax.persistence.Query}
     * @param searchTerm free text
     */
    protected void setFreeTextSearchTerm(Query query, final String searchTerm) {
        query.setParameter(Widget.PARAM_SEARCH_TERM, getLowercaseWildcardSearchTerm(searchTerm));
    }
    
    private String getLowercaseWildcardSearchTerm(String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            return searchTerm;
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }
}
