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
import org.apache.rave.portal.model.WidgetRatingsMapReduceResult;
import org.apache.rave.portal.model.WidgetUsersMapReduceResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.rave.portal.repository.util.CollectionNames.*;

/**
 * Provides Statistics via MapReduce jobs
 */
@Component
public class MongoDbMapReduceStatisticsAggregator implements StatisticsAggregator {

    private static final Logger log = LoggerFactory.getLogger(MongoDbMapReduceStatisticsAggregator.class);

    public static final String RATINGS_MAP = "classpath:/org/apache/rave/WidgetRatingsMap.js";
    public static final String RATINGS_REDUCE = "classpath:/org/apache/rave/WidgetRatingsReduce.js";
    public static final String USERS_MAP = "classpath:/org/apache/rave/WidgetUsersMap.js";
    public static final String USERS_REDUCE = "classpath:/org/apache/rave/WidgetUsersReduce.js";
    public static final int DEFAULT_RESULT_VALIDITY = 60;
    public static final String ID = "metadata";

    private final MongoOperations mongoOperations;

    @Autowired
    public MongoDbMapReduceStatisticsAggregator(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public WidgetStatistics getWidgetStatistics(String widget_id, String user_id) {
        WidgetUsersMapReduceResult userResult = mongoOperations.findById(widget_id, WidgetUsersMapReduceResult.class, WIDGET_USERS);
        WidgetRatingsMapReduceResult ratingResult = mongoOperations.findById(widget_id, WidgetRatingsMapReduceResult.class, WIDGET_RATINGS);

        int userCount = userResult == null ? 0 : userResult.getValue().size();
        WidgetStatistics stats;
        if(ratingResult == null) {
            stats = new WidgetStatistics();
            stats.setUserRating(-1);
        } else {
            stats = createWidgetStatisticsFromResults(user_id, ratingResult);
        }
        stats.setTotalUserCount(userCount);
        return stats;
    }

    @Override
    public Set<String> getUsersWithWidget(String widgetId) {
        WidgetUsersMapReduceResult result = mongoOperations.findById(widgetId, WidgetUsersMapReduceResult.class, WIDGET_USERS);
        return result.getValue().keySet();
    }

    @Override
    public Map<String , WidgetStatistics> getAllWidgetStatistics(String userId) {
        List<WidgetRatingsMapReduceResult> widgetStats = mongoOperations.findAll(WidgetRatingsMapReduceResult.class, WIDGET_RATINGS);
        List<WidgetUsersMapReduceResult> widgetUsers = mongoOperations.findAll(WidgetUsersMapReduceResult.class, WIDGET_USERS);

        Map<String, WidgetStatistics> stats = Maps.newHashMap();
        Map<String, Integer> userStats = mapUsersResults(widgetUsers);

        if (widgetStats.size() > 0) {
            addCombinedStats(userId, widgetStats, userStats, stats);
        } else {
            addUserCount(userStats, stats);
        }
        return stats;
    }

    @PostConstruct
    public void init() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    log.debug("Executing Map/Reduce Statistics Aggregation");
                    buildStats();
                } catch (Exception e) {
                    log.error("Error executing Map/Reduce Statistics Aggregation!", e);
                }
            }
        }, 0, DEFAULT_RESULT_VALIDITY, TimeUnit.SECONDS);
    }

    public void buildStats() {
        RunStatistics runStats = mongoOperations.findById(ID, RunStatistics.class, OPERATIONS);
        if(runStats == null || (System.currentTimeMillis() - runStats.getRefreshedTimeStamp()) > (DEFAULT_RESULT_VALIDITY * 1000)) {
            queryForUserStats();
        }
    }

    private void queryForUserStats() {
        synchronized (this) {
            executeUsersMapReduce();
            executeRatingsMapReduce();
            mongoOperations.save(new RunStatistics(ID, System.currentTimeMillis()), OPERATIONS);
        }
    }

    private Map<String, Integer> mapUsersResults(List<WidgetUsersMapReduceResult> widgetUsersMapReduceResults) {
        Map<String, Integer> map = Maps.newHashMap();
        for (WidgetUsersMapReduceResult result : widgetUsersMapReduceResults) {
            if (result.getId() != null) {
                map.put(result.getId(), result.getValue().size());
            }
        }
        return map;
    }

    private MapReduceOptions getOptions(String collection) {
        //Currently a bug in javascriptMode prevents maps from being correctly stored by the db
        return MapReduceOptions.options().outputCollection(collection).outputTypeReplace();
    }

    private MapReduceResults<WidgetUsersMapReduceResult> executeUsersMapReduce() {
        return mongoOperations.mapReduce(PAGE_COLLECTION, USERS_MAP, USERS_REDUCE, getOptions(WIDGET_USERS), WidgetUsersMapReduceResult.class);
    }

    private MapReduceResults<WidgetRatingsMapReduceResult> executeRatingsMapReduce() {
        return mongoOperations.mapReduce(WIDGET_COLLECTION, RATINGS_MAP, RATINGS_REDUCE, getOptions(WIDGET_RATINGS), WidgetRatingsMapReduceResult.class);
    }

    private void addUserCount(Map<String, Integer> users, Map<String, WidgetStatistics> stats) {
        for (Map.Entry<String, Integer> result : users.entrySet()) {
            WidgetStatistics widgetStatistics = getTotalUserOnlyWidgetStatistics(result.getValue());
            stats.put(result.getKey(), widgetStatistics);
        }
    }

    private void addCombinedStats(String userId, List<WidgetRatingsMapReduceResult> widgetStats, Map<String, Integer> usersMap, Map<String , WidgetStatistics> stats) {
        for (WidgetRatingsMapReduceResult result : widgetStats) {
            stats.put(result.getId(), createWidgetStatisticsFromResults(userId, result));
        }
        for(Map.Entry<String, Integer> id : usersMap.entrySet()) {
            Integer value = id.getValue();
            if(stats.containsKey(id.getKey())) {
                stats.get(id.getKey()).setTotalUserCount(value);
            } else {
                WidgetStatistics stat = getTotalUserOnlyWidgetStatistics(value);
                stats.put(id.getKey(), stat);
            }
        }
    }

    private WidgetStatistics getTotalUserOnlyWidgetStatistics(Integer value) {
        WidgetStatistics stat = new WidgetStatistics();
        stat.setTotalUserCount(value);
        stat.setUserRating(-1);
        return stat;
    }

    private WidgetStatistics createWidgetStatisticsFromResults(String user_id, WidgetRatingsMapReduceResult statsResult) {
        WidgetStatistics statistics = new WidgetStatistics();
        WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult result = statsResult.getValue();
        if (result != null) {
            statistics.setTotalDislike(result.getDislike().intValue());
            statistics.setTotalLike(result.getLike().intValue());
            statistics.setUserRating(result.getUserRatings().containsKey(user_id) ? result.getUserRatings().get(user_id).intValue() : -1);
        } else {
            statistics.setUserRating(-1);
        }
        return statistics;
    }

    public static class RunStatistics {
        private String id;
        private Long refreshedTimeStamp;

        public RunStatistics() {
        }

        public RunStatistics(String id, long timestamp) {
            this.id = id;
            this.refreshedTimeStamp = timestamp;
        }

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
