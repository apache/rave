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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;


/**
 */
@Repository
public class MongoDbWidgetRepository implements WidgetRepository {

    public static final String RATINGS_MAP = "classpath*:WidgetRatingsMap.js";
    public static final String RATINGS_REDUCE = "classpath*:WidgetRatingsReduce.js";
    public static final String USERS_MAP = "classpath*:WidgetUsersMap.js";
    public static final String USERS_REDUCE = "classpath*:WidgetUsersReduce.js";
    @Autowired
    private MongoWidgetOperations template;

    @Override
    public List<Widget> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return template.find(new Query().skip(offset).limit(pageSize));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query());
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        Query query = new Query(getFreeTextClause(searchTerm)).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return (int)template.count(new Query(getFreeTextClause(searchTerm)));
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        Query query = new Query(where("widgetStatus").is(widgetStatus)).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return (int)template.count(new Query(where("widgetStatus").is(widgetStatus)));
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        Query query = getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm).limit(pageSize).skip(offset);
        query.sort().on("title", Order.ASCENDING);
        return template.find(query);
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return (int)template.count(getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm));
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        Query query = getQueryByOwner(owner).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return (int)template.count(getQueryByOwner(owner));
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return template.findOne(new Query(where("url").is(widgetUrl)));
    }

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        Query query = query(where("widgetId").is(widget_id));
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = template.mapReduce(query, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
        MapReduceResults<WidgetUsersMapReduceResult> widgetUsers = template.mapReduce(query, USERS_MAP, USERS_REDUCE, WidgetUsersMapReduceResult.class);
        if(widgetStats.getCounts().getOutputCount() != 1 || widgetUsers.getCounts().getOutputCount() != 1) {
            throw new IllegalStateException("Invalid results returned from Map/Reduce");
        }
        WidgetRatingsMapReduceResult statsResult = widgetStats.iterator().next();
        WidgetUsersMapReduceResult userResult = widgetUsers.iterator().next();

        return createWidgetStatisticsFromResults(user_id, statsResult, userResult.getUsers());
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = template.mapReduce(RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
        Map<Long, Long> usersMap = mapUsersResults(template.mapReduce(USERS_MAP, USERS_REDUCE, WidgetUsersMapReduceResult.class));
        Map<Long, WidgetStatistics> stats = Maps.newHashMap();
        for(WidgetRatingsMapReduceResult result : widgetStats) {
            stats.put(result.getWidgetId(), createWidgetStatisticsFromResults(userId, result, usersMap.get(userId)));
        }
        return stats;
    }

    private Map<Long, Long> mapUsersResults(MapReduceResults<WidgetUsersMapReduceResult> widgetUsersMapReduceResults) {
        Map<Long, Long> map = Maps.newHashMap();
        for(WidgetUsersMapReduceResult result : widgetUsersMapReduceResults) {
            map.put(result.getWidgetId(), result.getUsers());
        }
        return map;
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {
        return template.find(getTagQuery(tagKeyWord).limit(pageSize).skip(offset));
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        return (int)template.count(getTagQuery(tagKeyword));
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

    private Query getWidgetStatusFreeTextQuery(WidgetStatus widgetStatus, String type, String searchTerm) {
        return new Query(where("widgetStatus").is(widgetStatus)
                .andOperator(where("type").is(type))
                .andOperator(getFreeTextClause(searchTerm))
        );
    }

    private Criteria getFreeTextClause(String searchTerm) {
        String regex = "/" + searchTerm + "/";
        return where("title").is(regex).orOperator(where("title").is(regex));
    }

    private Query getQueryByOwner(User owner) {
        return query(where("ownerId").is(owner.getId()));
    }

    private Query getTagQuery(String tagKeyWord) {
        return query(where("tags").elemMatch(where("tag.keyword").is(tagKeyWord)));
    }

    private WidgetStatistics createWidgetStatisticsFromResults(long user_id, WidgetRatingsMapReduceResult statsResult, Long userResult) {
        WidgetStatistics statistics = new WidgetStatistics();
        statistics.setTotalDislike(statsResult.getStatistics().getDislike().intValue());
        statistics.setTotalLike(statsResult.getStatistics().getLike().intValue());
        statistics.setUserRating(statsResult.getStatistics().getUserRatings().containsKey(user_id) ? statsResult.getStatistics().getUserRatings().get(user_id).intValue() : -1);
        statistics.setTotalUserCount(userResult == null ? 0 : userResult.intValue());
        return statistics;
    }

    public static class WidgetUsersMapReduceResult {
        private Long widgetId;
        private Long users;

        public Long getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(Long widgetId) {
            this.widgetId = widgetId;
        }

        public Long getUsers() {
            return users;
        }

        public void setUsers(Long users) {
            this.users = users;
        }
    }

    public static class WidgetRatingsMapReduceResult {
        private Long widgetId;
        private WidgetStatisticsMapReduceResult statistics;

        public Long getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(Long widgetId) {
            this.widgetId = widgetId;
        }

        public WidgetStatisticsMapReduceResult getStatistics() {
            return statistics;
        }

        public void setStatistics(WidgetStatisticsMapReduceResult statistics) {
            this.statistics = statistics;
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
