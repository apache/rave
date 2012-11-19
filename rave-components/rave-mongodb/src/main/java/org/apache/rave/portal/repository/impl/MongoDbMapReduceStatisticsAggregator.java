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
import org.apache.rave.portal.model.MongoDbPage;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.WidgetRatingsMapReduceResult;
import org.apache.rave.portal.model.WidgetUsersMapReduceResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.rave.portal.repository.util.CollectionNames.PAGE_COLLECTION;
import static org.apache.rave.portal.repository.util.CollectionNames.STATS_COLLECTION;
import static org.apache.rave.portal.repository.util.CollectionNames.WIDGET_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Provides Statistics via MapReduce jobs
 */
@Component
public class MongoDbMapReduceStatisticsAggregator implements StatisticsAggregator {
    public static final String RATINGS_MAP = "classpath:/org/apache/rave/WidgetRatingsMap.js";
    public static final String RATINGS_REDUCE = "classpath:/org/apache/rave/WidgetRatingsReduce.js";
    public static final String USERS_MAP = "classpath:/org/apache/rave/WidgetUsersMap.js";
    public static final String USERS_REDUCE = "classpath:/org/apache/rave/WidgetUsersReduce.js";
    public static final int DEFAULT_RESULT_VALIDITY = 60000;
    public static final String ID = "metadata";

    private final MongoOperations mongoOperations;

    @Autowired
    public MongoDbMapReduceStatisticsAggregator(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        Query statsQuery = query(where("widgetId").is(widget_id));
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = mongoOperations.mapReduce(statsQuery, WIDGET_COLLECTION, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
        List<MongoDbPage> pages = mongoOperations.find(query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widget_id)))), MongoDbPage.class ,PAGE_COLLECTION);

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
        List<WidgetRatingsMapReduceResult> widgetStats = mongoOperations.findAll(WidgetRatingsMapReduceResult.class, STATS_COLLECTION);
        List<WidgetUsersMapReduceResult> widgetUsers = mongoOperations.findAll(WidgetUsersMapReduceResult.class, STATS_COLLECTION);

        Map<Long, WidgetStatistics> stats = Maps.newHashMap();
        Map<Long, Integer> userStats = mapUsersResults(widgetUsers);

        if (widgetStats.size() > 0) {
            addCombinedStats(userId, widgetStats, userStats, stats);
        } else {
            addUserCount(userStats, stats);
        }
        return stats;
    }

    @PostConstruct
    private void init() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                RunStatistics runStats = mongoOperations.findById(ID, RunStatistics.class, STATS_COLLECTION);
                if(System.currentTimeMillis() - runStats.getRefreshedTimeStamp() > DEFAULT_RESULT_VALIDITY) {
                    queryForUserStats(runStats);
                }
            }
        }, 0, DEFAULT_RESULT_VALIDITY, TimeUnit.MILLISECONDS);
    }

    private void queryForUserStats(RunStatistics runStats) {
        synchronized (this) {
            if(System.currentTimeMillis() - runStats.getRefreshedTimeStamp() > DEFAULT_RESULT_VALIDITY) {
                executeUsersMapReduce();
                executeRatingsMapReduce();
                runStats.setRefreshedTimeStamp(System.currentTimeMillis());
                mongoOperations.save(runStats, STATS_COLLECTION);
            }
        }
    }

    private Map<Long, Integer> mapUsersResults(List<WidgetUsersMapReduceResult> widgetUsersMapReduceResults) {
        Map<Long, Integer> map = Maps.newHashMap();
        for (WidgetUsersMapReduceResult result : widgetUsersMapReduceResults) {
            if (result.getId() != null) {
                map.put(result.getId(), result.getValue().size());
            }
        }
        return map;
    }

    private MapReduceResults<WidgetUsersMapReduceResult> executeUsersMapReduce() {
        return mongoOperations.mapReduce(PAGE_COLLECTION, USERS_MAP, USERS_REDUCE, WidgetUsersMapReduceResult.class);
    }

    private MapReduceResults<WidgetRatingsMapReduceResult> executeRatingsMapReduce() {
        return mongoOperations.mapReduce(PAGE_COLLECTION, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class);
    }

    private void addUserCount(Map<Long, Integer> users, Map<Long, WidgetStatistics> stats) {
        for (Map.Entry<Long, Integer> result : users.entrySet()) {
            WidgetStatistics widgetStatistics = new WidgetStatistics();
            widgetStatistics.setTotalUserCount(result.getValue());
            widgetStatistics.setUserRating(-1);
            stats.put(result.getKey(), widgetStatistics);
        }
    }

    private void addCombinedStats(long userId, List<WidgetRatingsMapReduceResult> widgetStats, Map<Long, Integer> usersMap, Map<Long, WidgetStatistics> stats) {
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

    private Set<Long> getUserCount(List<MongoDbPage> pages) {
        Set<Long> set = Sets.newHashSet();
        for (Page page : pages) {
            Long id = page.getOwner().getId();
            if (!set.contains(id)) {
                set.add(id);
            }
        }
        return set;
    }

    public static class RunStatistics {
        private String id;
        private Long refreshedTimeStamp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getRefreshedTimeStamp() {
            return refreshedTimeStamp;
        }

        public void setRefreshedTimeStamp(Long refreshedTimeStamp) {
            this.refreshedTimeStamp = refreshedTimeStamp;
        }
    }

}
