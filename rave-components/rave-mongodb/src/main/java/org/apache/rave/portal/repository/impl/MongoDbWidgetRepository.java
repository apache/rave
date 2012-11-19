/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;


/**
 */
@Repository
public class MongoDbWidgetRepository implements WidgetRepository {
    ///org/apache/rave/WidgetRatingsMap.js
    public static final String RATINGS_MAP = "classpath:/org/apache/rave/WidgetRatingsMap.js";
    public static final String RATINGS_REDUCE = "classpath:/org/apache/rave/WidgetRatingsReduce.js";
    public static final String USERS_MAP = "classpath:/org/apache/rave/WidgetUsersMap.js";
    public static final String USERS_REDUCE = "classpath:/org/apache/rave/WidgetUsersReduce.js";
    public static final int DEFAULT_RESULT_VALIDITY = 60000;

    private Map<Long, Integer> widgetUsers = Maps.newHashMap();
    private long usersTimestamp = System.currentTimeMillis();

    @Autowired
    private MongoWidgetOperations template;

    @Autowired
    private MongoPageOperations pageTemplate;

    @Override
    public List<Widget> getAll() {
        return template.find(addSort(new Query()));
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return template.find(addSort(new Query().skip(offset).limit(pageSize)));
    }

    @Override
    public int getCountAll() {
        return (int) template.count(new Query());
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        Query query = new Query(addFreeTextClause(searchTerm, new Criteria())).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return (int) template.count(new Query(addFreeTextClause(searchTerm, new Criteria())));
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        Query query = new Query(where("widgetStatus").is(getWidgetStatusString(widgetStatus))).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return (int) template.count(new Query(where("widgetStatus").is(getWidgetStatusString(widgetStatus))));
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        Query query = getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm).limit(pageSize).skip(offset);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return (int) template.count(getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm));
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        Query query = getQueryByOwner(owner).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return (int) template.count(getQueryByOwner(owner));
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return template.findOne(new Query(where("url").is(widgetUrl)));
    }

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        Query statsQuery = query(where("widgetId").is(widget_id));
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = template.mapReduce(statsQuery, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
        List<Page> pages = pageTemplate.find(query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widget_id)))));

        int userCount = getUserCount(pages).size();
        switch (widgetStats.getCounts().getOutputCount()) {
            case 0:
                WidgetStatistics stats = new WidgetStatistics();
                stats.setTotalUserCount(userCount);
                return stats;
            case 1:
                WidgetRatingsMapReduceResult statsResult = widgetStats.iterator().next();
                return createWidgetStatisticsFromResults(user_id, statsResult, userCount);
            default:
                throw new IllegalStateException("Invalid results returned from Map/Reduce");
        }
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = template.mapReduce(RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
        Map<Long, WidgetStatistics> stats = Maps.newHashMap();
        if (widgetStats.getCounts().getOutputCount() > 0) {
            addCombinedStats(userId, widgetStats, widgetUsers, stats);
        } else {
            addUserCount(widgetUsers, stats);
        }
        return stats;
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long userId) {
        Query q = query(where("ratings").elemMatch(where("userId").is(userId)));
        List<Widget> widgets = template.find(q);
        Map<Long, WidgetRating> ratings = Maps.newHashMap();
        for (Widget widget : widgets) {
            for (WidgetRating rating : widget.getRatings()) {
                if (rating.getUserId().equals(userId)) {
                    ratings.put(widget.getId(), rating);
                    break;
                }
            }
        }
        return ratings;
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {
        return template.find(addSort(getTagQuery(tagKeyWord).limit(pageSize).skip(offset)));
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        return (int) template.count(getTagQuery(tagKeyword));
    }

    @Override
    public int unassignWidgetOwner(long userId) {
        Query query = query(where("ownerId").is(userId));
        return template.update(query, update("ownerId", null));
    }

    @Override
    public Class<? extends Widget> getType() {
        return MongoDbWidget.class;
    }

    @Override
    public Widget get(long id) {
        return template.get(id);
    }

    @Override
    public Widget save(Widget item) {
        return template.save(item);
    }

    @Override
    public void delete(Widget item) {
        template.remove(new Query(where("_id").is(item.getId())));
    }

    @PostConstruct
    private void init() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(System.currentTimeMillis() - usersTimestamp > DEFAULT_RESULT_VALIDITY) {
                    queryForUserStats();
                }
            }
        }, 0, DEFAULT_RESULT_VALIDITY, TimeUnit.MILLISECONDS);
    }

    private Map<Long, Integer> queryForUserStats() {
        synchronized (this) {
            if(System.currentTimeMillis() - usersTimestamp > DEFAULT_RESULT_VALIDITY) {
                MapReduceResults<WidgetUsersMapReduceResult> users = pageTemplate.mapReduce(USERS_MAP, USERS_REDUCE, WidgetUsersMapReduceResult.class);
                widgetUsers = mapUsersResults(users);
                usersTimestamp = System.currentTimeMillis();
            }
        }
        return widgetUsers;
    }

    private Map<Long, Integer> mapUsersResults(MapReduceResults<WidgetUsersMapReduceResult> widgetUsersMapReduceResults) {
        Map<Long, Integer> map = Maps.newHashMap();
        for (WidgetUsersMapReduceResult result : widgetUsersMapReduceResults) {
            if (result.getId() != null) {
                map.put(result.getId(), result.getValue().size());
            }
        }
        return map;
    }

    private Query getWidgetStatusFreeTextQuery(WidgetStatus widgetStatus, String type, String searchTerm) {
        Criteria criteria = addFreeTextClause(searchTerm, new Criteria());
        if (type != null && !type.isEmpty()) {
            criteria.and("type").is(type);
        }
        if (widgetStatus != null) {
            criteria.and("widgetStatus").is(getWidgetStatusString(widgetStatus));
        }
        return query(criteria);
    }

    private Criteria addFreeTextClause(String searchTerm, Criteria criteria) {
        Pattern p = Pattern.compile(".*" + searchTerm + ".*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        criteria.orOperator(where("title").regex(p), (where("description").regex(p)));
        return criteria;
    }

    private Query getQueryByOwner(User owner) {
        return query(where("ownerId").is(owner.getId()));
    }

    private Query getTagQuery(String tagKeyWord) {
        return query(where("tags").elemMatch(where("tag.keyword").is(tagKeyWord)));
    }

    private void addUserCount(Map<Long, Integer> users, Map<Long, WidgetStatistics> stats) {
        for (Map.Entry<Long, Integer> result : users.entrySet()) {
            WidgetStatistics widgetStatistics = new WidgetStatistics();
            widgetStatistics.setTotalUserCount(result.getValue());
            widgetStatistics.setUserRating(-1);
            stats.put(result.getKey(), widgetStatistics);
        }
    }

    private void addCombinedStats(long userId, MapReduceResults<WidgetRatingsMapReduceResult> widgetStats, Map<Long, Integer> usersMap, Map<Long, WidgetStatistics> stats) {
        for (WidgetRatingsMapReduceResult result : widgetStats) {
            stats.put(result.getId(), createWidgetStatisticsFromResults(userId, result, usersMap.get(result.getId())));
        }
    }

    private WidgetStatistics createWidgetStatisticsFromResults(long user_id, WidgetRatingsMapReduceResult statsResult, Integer userResult) {
        WidgetStatistics statistics = new WidgetStatistics();
        WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult result = statsResult.getValue();
        if (result != null) {
            statistics.setTotalDislike(result.getDislike().intValue());
            statistics.setTotalLike(result.getLike().intValue());
            statistics.setUserRating(result.getUserRatings().containsKey(user_id) ? result.getUserRatings().get(user_id).intValue() : -1);
        }
        statistics.setTotalUserCount(userResult == null ? 0 : userResult);
        return statistics;
    }

    private String getWidgetStatusString(WidgetStatus widgetStatus) {
        return widgetStatus.getWidgetStatus().toUpperCase();
    }

    private Query addSort(Query query) {
        query.sort().on("featured", Order.DESCENDING).on("title", Order.ASCENDING);
        return query;
    }

    private Set<Long> getUserCount(List<Page> pages) {
        Set<Long> set = Sets.newHashSet();
        for (Page page : pages) {
            Long id = page.getOwner().getId();
            if (!set.contains(id)) {
                set.add(id);
            }
        }
        return set;
    }

    public static class WidgetUsersMapReduceResult {
        private Long id;
        private Map<Long, Long> value;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Map<Long, Long> getValue() {
            return value;
        }

        public void setValue(Map<Long, Long> value) {
            this.value = value;
        }
    }

    public static class WidgetRatingsMapReduceResult {
        private Long id;
        private WidgetStatisticsMapReduceResult value;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public WidgetStatisticsMapReduceResult getValue() {
            return value;
        }

        public void setValue(WidgetStatisticsMapReduceResult value) {
            this.value = value;
        }

        public static class WidgetStatisticsMapReduceResult {
            private Map<Long, Long> userRatings;
            private Long like;
            private Long dislike;

            public Map<Long, Long> getUserRatings() {
                return userRatings;
            }

            public void setUserRatings(Map<Long, Long> userRatings) {
                this.userRatings = userRatings;
            }

            public Long getLike() {
                return like;
            }

            public void setLike(Long like) {
                this.like = like;
            }

            public Long getDislike() {
                return dislike;
            }

            public void setDislike(Long dislike) {
                this.dislike = dislike;
            }
        }

    }
}
