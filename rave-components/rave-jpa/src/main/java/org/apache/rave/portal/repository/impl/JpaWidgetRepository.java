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


import org.apache.commons.lang3.StringUtils;
import org.apache.rave.model.Tag;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.model.WidgetTag;
import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.portal.model.JpaWidget;
import org.apache.rave.portal.model.JpaWidgetComment;
import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.portal.model.JpaWidgetTag;
import org.apache.rave.portal.model.conversion.JpaWidgetCommentConverter;
import org.apache.rave.portal.model.conversion.JpaWidgetConverter;
import org.apache.rave.portal.model.conversion.JpaWidgetRatingConverter;
import org.apache.rave.portal.model.conversion.JpaWidgetTagConverter;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaWidgetRepository implements WidgetRepository {

    private final Logger log = LoggerFactory.getLogger(JpaWidgetRepository.class);

    @Autowired
    private JpaWidgetConverter converter;

    @Autowired
    private JpaWidgetTagConverter tagConverter;

    @Autowired
    private JpaWidgetCommentConverter commentConverter;

    @Autowired
    JpaWidgetRatingConverter ratingConverter;

    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Widget> getAll() {
        log.warn("Requesting potentially large resultset of Widget. No pagesize set.");
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.GET_ALL, JpaWidget.class);
        return CollectionUtils.<Widget>toBaseTypedList(query.getResultList());
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.GET_ALL, JpaWidget.class);
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaWidget.GET_COUNT);
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
        query.setParameter(JpaWidget.PARAM_OWNER, owner.getId());
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_OWNER);
        query.setParameter(JpaWidget.PARAM_OWNER, owner.getId());
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
    public WidgetStatistics getWidgetStatistics(String widget_id, String user_id) {
        WidgetStatistics widgetStatistics = new WidgetStatistics();

        Query query = manager.createNamedQuery(JpaWidgetRating.WIDGET_TOTAL_LIKES);
        query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, Long.parseLong(widget_id));
        widgetStatistics.setTotalLike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(JpaWidgetRating.WIDGET_TOTAL_DISLIKES);
        query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, Long.parseLong(widget_id));
        widgetStatistics.setTotalDislike(((Number) query.getSingleResult()).intValue());

        query = manager.createNamedQuery(JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET);
        query.setParameter(JpaRegionWidget.PARAM_WIDGET_ID, Long.parseLong(widget_id));
        widgetStatistics.setTotalUserCount(((Number) query.getSingleResult()).intValue());

        try {
            query = manager.createNamedQuery(JpaWidgetRating.WIDGET_USER_RATING);
            query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, Long.parseLong(widget_id));
            query.setParameter(JpaWidgetRating.PARAM_USER_ID, Long.parseLong(user_id));
            widgetStatistics.setUserRating(((Number) query.getSingleResult()).intValue());
        } catch (NoResultException e) {
            widgetStatistics.setUserRating(JpaWidgetRating.UNSET);
        }

        return widgetStatistics;
    }

    @Override
    public Map<String, WidgetRating> getUsersWidgetRatings(String user_id) {
        TypedQuery<JpaWidgetRating> query =
                manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_USER_RATINGS, JpaWidgetRating.class);
        query.setParameter(JpaWidgetRating.PARAM_USER_ID, user_id == null ? null : Long.parseLong(user_id));

        Map<String, WidgetRating> map = new HashMap<String, WidgetRating>();
        for (JpaWidgetRating widgetRating : query.getResultList()) {
            map.put(widgetRating.getWidgetId(), widgetRating);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, WidgetStatistics> getAllWidgetStatistics(String userId) {
        HashMap<String, WidgetStatistics> map = new HashMap<String, WidgetStatistics>();

        //Generate the mapping of all likes done for the widgets
        Query query = manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_TOTAL_LIKES);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long totalLikes = (Long) result[0];
            Long widgetId = (Long) result[1];
            WidgetStatistics widgetStatistics = new WidgetStatistics();
            widgetStatistics.setTotalLike(totalLikes.intValue());
            map.put(widgetId.toString(), widgetStatistics);
        }

        //Add the mapping of all dislikes done for the widgets
        query = manager.createNamedQuery(JpaWidgetRating.WIDGET_ALL_TOTAL_DISLIKES);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long totalDislikes = (Long) result[0];
            Long widgetId = (Long) result[1];
            WidgetStatistics widgetStatistics = map.get(widgetId.toString());
            if (widgetStatistics == null) {
                widgetStatistics = new WidgetStatistics();
                map.put(widgetId.toString(), widgetStatistics);
            }
            widgetStatistics.setTotalDislike(totalDislikes.intValue());
        }

        //get the total user count for widgets
        query = manager.createNamedQuery(JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS);
        for (Object[] result : (List<Object[]>) query.getResultList()) {
            Long widgetId = (Long) result[0];
            Long totalUserCount = (Long) result[1];
            WidgetStatistics widgetStatistics = map.get(widgetId.toString());
            if (widgetStatistics == null) {
                widgetStatistics = new WidgetStatistics();
                map.put(widgetId.toString(), widgetStatistics);
            }
            widgetStatistics.setTotalUserCount(totalUserCount.intValue());
        }

        //Add the current user's current rating of the widget
        Map<String, WidgetRating> userRatings = getUsersWidgetRatings(userId);
        for (Map.Entry<String, WidgetStatistics> entry : map.entrySet()) {
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
        Tag tag = tagRepository.getByKeyword(tagKeyword);
        TypedQuery<JpaWidget> query = manager.createNamedQuery(JpaWidget.WIDGET_GET_BY_TAG, JpaWidget.class);
        query.setParameter(JpaWidget.PARAM_TAG_ID, tag == null ? null : Long.parseLong(tag.getId()));
        return CollectionUtils.<Widget>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        if (tagKeyword != null) {
            tagKeyword = tagKeyword.toLowerCase();
        }
        Tag tag = tagRepository.getByKeyword(tagKeyword);
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_COUNT_BY_TAG);
        query.setParameter(JpaWidget.PARAM_TAG_ID, tag == null ? null : Long.parseLong(tag.getId()));
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public int unassignWidgetOwner(String userId) {
        Query query = manager.createNamedQuery(JpaWidget.WIDGET_UNASSIGN_OWNER);
        query.setParameter(JpaWidget.PARAM_OWNER, userId);
        return query.executeUpdate();
    }

    @Override
    public Class<? extends Widget> getType() {
        return JpaWidget.class;
    }

    @Override
    public Widget get(String id) {
        return manager.find(JpaWidget.class, Long.parseLong(id));
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

    // ***************************************************************************************************************
    // Widget Tag Methods
    // ***************************************************************************************************************


    @Override
    public WidgetTag getTagByWidgetIdAndKeyword(String widgetId, String keyword) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        Tag tag = tagRepository.getByKeyword(keyword);
        TypedQuery<JpaWidgetTag> query = manager.createNamedQuery(JpaWidgetTag.FIND_BY_WIDGETID_AND_TAGID, JpaWidgetTag.class);
        query.setParameter(JpaWidgetTag.TAG_ID_PARAM, tag == null ? null : Long.parseLong(tag.getId()));
        query.setParameter(JpaWidgetTag.WIDGET_ID_PARAM, widgetId == null ? null : Long.parseLong(widgetId));
        return getSingleResult(query.getResultList());
    }

    @Override
    public WidgetTag getTagById(String widgetTagId) {
        return manager.find(JpaWidgetTag.class, Long.parseLong(widgetTagId));
    }

    @Override
    public WidgetTag saveWidgetTag(String widgetId, WidgetTag tag) {
        JpaWidgetTag jpaWidgetTag = tagConverter.convert(tag, widgetId);

        return saveOrUpdate(jpaWidgetTag.getId(), manager, jpaWidgetTag);
    }

    @Override
    public void deleteWidgetTag(WidgetTag tag) {
        manager.remove(tag instanceof JpaWidgetTag ? tag: manager.find(Tag.class, Long.parseLong(tag.getTagId())));
    }

    // ***************************************************************************************************************
    // Widget Comment Methods
    // ***************************************************************************************************************

    @Override
    public WidgetComment getCommentById(String widgetId, String widgetCommentId) {
        // widgetId ignored in JPA
        return manager.find(JpaWidgetComment.class, Long.parseLong(widgetCommentId));
    }

    @Override
    public WidgetComment createWidgetComment(String widgetId, WidgetComment comment) {
        JpaWidgetComment category = commentConverter.convert(comment, widgetId);
        return saveOrUpdate(category.getEntityId(), manager, category);
    }

    @Override
    public WidgetComment updateWidgetComment(String widgetId, WidgetComment comment) {
        JpaWidgetComment category = commentConverter.convert(comment, widgetId);
        return saveOrUpdate(category.getEntityId(), manager, category);
    }

    @Override
    public void deleteWidgetComment(String widgetId, WidgetComment comment) {
        // widgetId ignored in JPA
        manager.remove(comment instanceof JpaWidgetComment ? comment : getCommentById(widgetId, comment.getId()));
    }

    /**
     * Delete all Widget Comments for a userId
     *
     * @param userId
     * @return count of comments deleted
     */
    @Override
    public int deleteAllWidgetComments(String userId) {
        TypedQuery<JpaWidgetComment> query = manager.createNamedQuery(JpaWidgetComment.DELETE_ALL_BY_USER, JpaWidgetComment.class);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    @Override
    public WidgetRating getRatingById(String widgetId, String widgetRatingId) {
        return manager.find(JpaWidgetRating.class, Long.parseLong(widgetRatingId));
    }

    @Override
    public WidgetRating createWidgetRating(String widgetId, WidgetRating rating) {
        JpaWidgetRating jpaItem = ratingConverter.convert(rating, widgetId);
        return saveOrUpdate(jpaItem.getEntityId(), manager, jpaItem);
    }

    @Override
    public WidgetRating updateWidgetRating(String widgetId, WidgetRating rating) {
        JpaWidgetRating jpaItem = ratingConverter.convert(rating, widgetId);
        return saveOrUpdate(jpaItem.getEntityId(), manager, jpaItem);
    }

    @Override
    public void deleteWidgetRating(String widgetId, WidgetRating rating) {
        manager.remove(ratingConverter.convert(rating, widgetId));
    }

    @Override
    public WidgetRating getWidgetRatingsByWidgetIdAndUserId(String widgetId, String userId) {
        TypedQuery<JpaWidgetRating> query =
                manager.createNamedQuery(JpaWidgetRating.WIDGET_RATING_BY_WIDGET_AND_USER, JpaWidgetRating.class);
        query.setParameter(JpaWidgetRating.PARAM_WIDGET_ID, Long.parseLong(widgetId));
        query.setParameter(JpaWidgetRating.PARAM_USER_ID, Long.parseLong(userId));
        final List<JpaWidgetRating> resultList = query.getResultList();
        return getSingleResult(resultList);
    }

    @Override
    public int deleteAllWidgetRatings(String userId) {
        TypedQuery<JpaWidgetRating> query = manager.createNamedQuery(JpaWidgetRating.DELETE_ALL_BY_USER, JpaWidgetRating.class);
        query.setParameter("userId", userId == null ? null : Long.parseLong(userId));
        return query.executeUpdate();
    }
}
