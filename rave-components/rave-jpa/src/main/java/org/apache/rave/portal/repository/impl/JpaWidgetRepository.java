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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.JpaWidgetConverter;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.rave.persistence.jpa.util.JpaUtil.*;

@Repository
public class JpaWidgetRepository implements WidgetRepository {

    private final Logger log = LoggerFactory.getLogger(JpaWidgetRepository.class);

    @Autowired
    private JpaWidgetConverter converter;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Widget> getAll() {
        log.warn("Requesting potentially large resultset of Widget. No pagesize set.");
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_ALL, JpaWidget.class);
        return CollectionUtils.<Widget>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_ALL, JpaWidget.class);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_FREE_TEXT,
                JpaWidget.class);
        setFreeTextSearchTerm(query, searchTerm);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_FREE_TEXT);
        setFreeTextSearchTerm(query, searchTerm);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_STATUS,
                JpaWidget.class);
        query.setParameter(JpaWidget.PARAM_STATUS, widgetStatus);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_STATUS);
        query.setParameter(JpaWidget.PARAM_STATUS, widgetStatus);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm,
                                                            int offset, int pageSize) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<JpaWidget> query = cb.createQuery(JpaWidget.class);
        final Root<JpaWidget> widgetType = query.from(JpaWidget.class);
        query.where(getStatusAndTypeAndFreeTextPredicates(cb, widgetType, widgetStatus, type, searchTerm));
        query.orderBy(getOrderByTitleAsc(cb, widgetType));

        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(manager.createQuery(query), offset, pageSize));
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<JpaWidget> widgetType = query.from(JpaWidget.class);
        query.select(cb.count(widgetType));
        query.where(getStatusAndTypeAndFreeTextPredicates(cb, widgetType, widgetStatus, type, searchTerm));

        final Long countResult = manager.createQuery(query).getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_OWNER, JpaWidget.class);
        query.setParameter(JpaWidget.PARAM_OWNER, owner);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_OWNER);
        query.setParameter(JpaWidget.PARAM_OWNER, owner);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public JpaWidget getByUrl(String widgetUrl) {
        if (StringUtils.isBlank(widgetUrl)) {
            throw new IllegalArgumentException("Widget URL must not be empty");
        }

        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_URL, JpaWidget.class);
        // url is a unique field, so no paging needed
        query.setParameter(JpaWidget.PARAM_URL, widgetUrl);
        final List<JpaWidget> resultList = query.getResultList();
        return getSingleResult(resultList);
    }

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        WidgetStatistics widgetStatistics = new WidgetStatistics();

        Query query = manager.createNamedQuery(JpaWidgetRating.WIDGET_TOTAL_LIKES);
        query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalLike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(JpaWidgetRating.WIDGET_TOTAL_DISLIKES);
        query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalDislike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET);
        query.setParameter(JpaRegionWidget.PARAM_WIDGET_ID, widget_id);
        widgetStatistics.setTotalUserCount(((Number) query.getSingleResult()).intValue());

        try {
            query = manager.createNamedQuery(JpaWidgetRating.WIDGET_USER_RATING);
            query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, widget_id);
            query.setParameter(JpaWidgetRating.PARAM_USER_ID, user_id);
            widgetStatistics.setUserRating(((Number) query.getSingleResult()).intValue());
        } catch (NoResultException e) {
            widgetStatistics.setUserRating(JpaWidgetRating.UNSET);
        }

        return widgetStatistics;
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long user_id) {
        TypedQuery<JpaWidgetRating> query =
                manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_USER_RATINGS, JpaWidgetRating.class);
        query.setParameter(JpaWidgetRating.PARAM_USER_ID, user_id);

        Map<Long, WidgetRating> map = new HashMap<Long, WidgetRating>();
        for (WidgetRating widgetRating : query.getResultList()) {
            map.put(widgetRating.getWidgetId(), widgetRating);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        HashMap<Long, WidgetStatistics> map = new HashMap<Long, WidgetStatistics>();

        //Generate the mapping of all likes done for the widgets
        Query query = manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_TOTAL_LIKES);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long totalLikes = (Long) result[0];
            Long widgetId = (Long) result[1];
            WidgetStatistics widgetStatistics = new WidgetStatistics();
            widgetStatistics.setTotalLike(totalLikes.intValue());
            map.put(widgetId, widgetStatistics);
        }

        //Add the mapping of all dislikes done for the widgets
        query = manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_TOTAL_DISLIKES);
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
        query = manager.createNamedQuery(JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS);
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
                entry.getValue().setUserRating(JpaWidgetRating.UNSET);
            }
        }

        return map;
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyword, int offset, int pageSize) {
        if (tagKeyword != null) {
            tagKeyword = tagKeyword.toLowerCase();
        }
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_TAG, JpaWidget.class);
        query.setParameter(JpaWidget.PARAM_TAG, tagKeyword);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        if (tagKeyword != null) {
            tagKeyword = tagKeyword.toLowerCase();
        }
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_TAG);
        query.setParameter(JpaWidget.PARAM_TAG, tagKeyword);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public int unassignWidgetOwner(long userId) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_UNASSIGN_OWNER);
        query.setParameter(JpaWidget.PARAM_OWNER, userId);
        return query.executeUpdate();
    }

    @Override
    public Class<? extends Widget> getType() {
        return JpaWidget.class;
    }

    @Override
    public Widget get(long id) {
        return manager.find(JpaWidget.class, id);
    }

    @Override
    public Widget save(Widget item) {
        return saveOrUpdate(item.getId(), manager, converter.convert(item));
    }

    @Override
    public void delete(Widget item) {
        manager.remove(converter.convert(item));
    }

    /**
     * Sets input as free text search term to a query
     *
     * @param query      {@link javax.persistence.Query}
     * @param searchTerm free text
     */
    protected void setFreeTextSearchTerm(Query query, final String searchTerm) {
        query.setParameter(JpaWidget.PARAM_SEARCH_TERM, getLowercaseWildcardSearchTerm(searchTerm));
    }

    private Predicate[] getStatusAndTypeAndFreeTextPredicates(CriteriaBuilder cb, Root<JpaWidget> widgetType,
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

    private Order getOrderByTitleAsc(CriteriaBuilder cb, Root<JpaWidget> widgetType) {
        return cb.asc(getTitleField(widgetType));
    }

    private Path<String> getTitleField(Root<JpaWidget> widgetType) {
        return widgetType.get("title");
    }

    private Path<String> getDescriptionField(Root<JpaWidget> widgetType) {
        return widgetType.get("description");
    }

    private Path<String> getTypeField(Root<JpaWidget> widgetType) {
        return widgetType.get("type");
    }

    private Path<WidgetStatus> getWidgetStatusField(Root<JpaWidget> widgetType) {
        return widgetType.get("widgetStatus");
    }

    private String getLowercaseWildcardSearchTerm(String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            return searchTerm;
        }
        return "%" + searchTerm.toLowerCase() + "%";
    }
}
