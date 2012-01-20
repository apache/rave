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
import org.apache.openjpa.jdbc.kernel.exps.ToLowerCase;
import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_BY_OWNER, Widget.class);
        query.setParameter(Widget.PARAM_OWNER, owner);
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        Query query = manager.createNamedQuery(Widget.WIDGET_COUNT_BY_OWNER);
        query.setParameter(Widget.PARAM_OWNER, owner);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
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

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        WidgetStatistics widgetStatistics = new WidgetStatistics();

        Query query = manager.createNamedQuery(WidgetRating.WIDGET_TOTAL_LIKES);
        query.setParameter(WidgetRating.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalLike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(WidgetRating.WIDGET_TOTAL_DISLIKES);
        query.setParameter(WidgetRating.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalDislike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(RegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET);
        query.setParameter(RegionWidget.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalUserCount(((Number) query.getSingleResult()).intValue());

        try {
            query = manager.createNamedQuery(WidgetRating.WIDGET_USER_RATING);
            query.setParameter(WidgetRating.PARAM_WIDGET_ID, widget_id);
            query.setParameter(WidgetRating.PARAM_USER_ID, user_id);
            widgetStatistics.setUserRating(((Number) query.getSingleResult()).intValue());
        } catch (NoResultException e) {
            widgetStatistics.setUserRating(WidgetRating.UNSET);
        }

        return widgetStatistics;
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long user_id) {
        TypedQuery<WidgetRating> query =
                manager.createNamedQuery(WidgetRating.WIDGET_ALL_USER_RATINGS, WidgetRating.class);
        query.setParameter(WidgetRating.PARAM_USER_ID, user_id);

        Map<Long, WidgetRating> map = new HashMap<Long, WidgetRating>();
        for (WidgetRating widgetRating : query.getResultList()) {
            map.put(widgetRating.getWidgetId(), widgetRating);
        }

        return map;
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        HashMap<Long, WidgetStatistics> map = new HashMap<Long, WidgetStatistics>();

        //Generate the mapping of all likes done for the widgets
        Query query = manager.createNamedQuery(WidgetRating.WIDGET_ALL_TOTAL_LIKES);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long totalLikes = (Long) result[0];
            Long widgetId = (Long) result[1];
            WidgetStatistics widgetStatistics = new WidgetStatistics();
            widgetStatistics.setTotalLike(totalLikes.intValue());
            map.put(widgetId, widgetStatistics);
        }

        //Add the mapping of all dislikes done for the widgets
        query = manager.createNamedQuery(WidgetRating.WIDGET_ALL_TOTAL_DISLIKES);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long totalDislikes = (Long) result[0];
            Long widgetId = (Long) result[1];
            WidgetStatistics widgetStatistics = map.get(widgetId);
            if (widgetStatistics == null) {
                widgetStatistics = new WidgetStatistics();
                map.put(widgetId, widgetStatistics);
            }
            widgetStatistics.setTotalDislike(totalDislikes.intValue());
        }

        //get the total user count for widgets
        query = manager.createNamedQuery(RegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long widgetId = (Long) result[0];
            Long totalUserCount = (Long) result[1];
            WidgetStatistics widgetStatistics = map.get(widgetId);
            if (widgetStatistics == null) {
                widgetStatistics = new WidgetStatistics();
                map.put(widgetId, widgetStatistics);
            }
            widgetStatistics.setTotalUserCount(totalUserCount.intValue());
        }

        //Add the current user's current rating of the widget
        Map<Long, WidgetRating> userRatings = getUsersWidgetRatings(userId);
        for (Map.Entry<Long, WidgetStatistics> entry : map.entrySet()) {
            //If the user has a widget rating then record it
            if (userRatings.containsKey(entry.getKey())) {
                entry.getValue().setUserRating(userRatings.get(entry.getKey()).getScore());
            }
            //otherwise set the rating to UNSET
            else {
                entry.getValue().setUserRating(WidgetRating.UNSET);
            }
        }

        return map;
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyword, int offset, int pageSize) {
        if (tagKeyword!=null) tagKeyword =tagKeyword.toLowerCase();
        TypedQuery<Widget> query = manager.createNamedQuery(Widget.WIDGET_GET_BY_TAG, Widget.class);
        query.setParameter(Widget.PARAM_TAG, tagKeyword.toLowerCase());
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        if (tagKeyword!=null) tagKeyword =tagKeyword.toLowerCase();
        Query query = manager.createNamedQuery(Widget.WIDGET_COUNT_BY_TAG);
        query.setParameter(Widget.PARAM_TAG,tagKeyword);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
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

    private String getLowercaseWildcardSearchTerm(String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            return searchTerm;
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }
}
