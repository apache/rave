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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.rave.portal.model.WidgetRatingsMapReduceResult;
import org.apache.rave.portal.model.WidgetUsersMapReduceResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.rave.portal.repository.impl.MongoDbMapReduceStatisticsAggregator.*;
import static org.apache.rave.portal.repository.util.CollectionNames.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MonogoDbMapReduceStatisticsAggregatorTest {

    private MongoOperations mongoOperations;
    private StatisticsAggregator aggregator;

    @Before
    public void setup() {
        mongoOperations = createMock(MongoOperations.class);
        aggregator = new MongoDbMapReduceStatisticsAggregator(mongoOperations);
    }

    @Test
    public void getAllStatistics_valid() {

        Map<String, Long> userRatings = getRatingsMap();
        Map<String, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult("24L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult("25L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult("26L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L))
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult("24L", users),
                new WidgetUsersMapReduceResult("25L", users),
                new WidgetUsersMapReduceResult("26L", users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.WIDGET_RATINGS)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.WIDGET_USERS)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<String, WidgetStatistics> stats = aggregator.getAllWidgetStatistics("1L");
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get("24L").getTotalLike(), is(equalTo(2)));
        assertThat(stats.get("24L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("24L").getUserRating(), is(equalTo(10)));
        assertThat(stats.get("24L").getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get("26L").getTotalLike(), is(equalTo(2)));
        assertThat(stats.get("26L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("26L").getUserRating(), is(equalTo(10)));
        assertThat(stats.get("26L").getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_noUserRating() {

        Map<String, Long> userRatings = getRatingsMap();
        Map<String, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult("24L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult("25L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult("26L", new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L))
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult("24L", users),
                new WidgetUsersMapReduceResult("25L", users),
                new WidgetUsersMapReduceResult("26L", users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.WIDGET_RATINGS)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.WIDGET_USERS)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<String, WidgetStatistics> stats = aggregator.getAllWidgetStatistics("5L");
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get("24L").getTotalLike(), is(equalTo(2)));
        assertThat(stats.get("24L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("24L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("24L").getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get("26L").getTotalLike(), is(equalTo(2)));
        assertThat(stats.get("26L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("26L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("26L").getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_nullStats() {

        Map<String, Long> userRatings = getRatingsMap();
        Map<String, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult("24L", null),
                new WidgetRatingsMapReduceResult("25L", null),
                new WidgetRatingsMapReduceResult("26L", null)
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult("24L", users),
                new WidgetUsersMapReduceResult("25L", users),
                new WidgetUsersMapReduceResult("26L", users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.WIDGET_RATINGS)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.WIDGET_USERS)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<String, WidgetStatistics> stats = aggregator.getAllWidgetStatistics("5L");
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get("24L").getTotalLike(), is(equalTo(0)));
        assertThat(stats.get("24L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("24L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("24L").getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get("26L").getTotalLike(), is(equalTo(0)));
        assertThat(stats.get("26L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("26L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("26L").getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_noRatings() {

        Map<String, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Lists.newArrayList();

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult("24L", users),
                new WidgetUsersMapReduceResult("25L", users),
                new WidgetUsersMapReduceResult("26L", users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.WIDGET_RATINGS)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.WIDGET_USERS)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<String, WidgetStatistics> stats = aggregator.getAllWidgetStatistics("5L");
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get("24L").getTotalLike(), is(equalTo(0)));
        assertThat(stats.get("24L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("24L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("24L").getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get("26L").getTotalLike(), is(equalTo(0)));
        assertThat(stats.get("26L").getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get("26L").getUserRating(), is(equalTo(-1)));
        assertThat(stats.get("26L").getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getWidgetStatistics_valid() {
        String widget_id = "1L";
        Map<String, Long> userMap = Maps.newHashMap();
        userMap.put("20L", 10L);
        userMap.put("21L", 10L);
        WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult stats = new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userMap, 20L, 0L);
        WidgetUsersMapReduceResult usersResult = new WidgetUsersMapReduceResult(widget_id, userMap);
        WidgetRatingsMapReduceResult ratingsResult = new WidgetRatingsMapReduceResult(widget_id, stats);

        expect(mongoOperations.findById(widget_id, WidgetRatingsMapReduceResult.class, WIDGET_RATINGS)).andReturn(ratingsResult);
        expect(mongoOperations.findById(widget_id, WidgetUsersMapReduceResult.class, WIDGET_USERS)).andReturn(usersResult);
        replay(mongoOperations);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, "21L");

        assertThat(result.getTotalDislike(), is(equalTo(0)));
        assertThat(result.getTotalLike(), is(equalTo(20)));
        assertThat(result.getUserRating(), is(equalTo(10)));
        assertThat(result.getTotalUserCount(), is(equalTo(2)));
    }

    @Test
    public void getWidgetStatistics_noUser() {
        String widget_id = "1L";
        Map<String, Long> userMap = Maps.newHashMap();
        userMap.put("20L", 10L);
        userMap.put("21L", 10L);
        WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult stats = new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userMap, 20L, 0L);
        WidgetUsersMapReduceResult usersResult = new WidgetUsersMapReduceResult(widget_id, userMap);
        WidgetRatingsMapReduceResult ratingsResult = new WidgetRatingsMapReduceResult(widget_id, stats);

        expect(mongoOperations.findById(widget_id, WidgetRatingsMapReduceResult.class, WIDGET_RATINGS)).andReturn(ratingsResult);
        expect(mongoOperations.findById(widget_id, WidgetUsersMapReduceResult.class, WIDGET_USERS)).andReturn(usersResult);
        replay(mongoOperations);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, "23L");

        assertThat(result.getTotalDislike(), is(equalTo(0)));
        assertThat(result.getTotalLike(), is(equalTo(20)));
        assertThat(result.getUserRating(), is(equalTo(-1)));
        assertThat(result.getTotalUserCount(), is(equalTo(2)));
    }

    @Test
    public void getWidgetStatistics_nullRatings() {
        String widget_id = "1L";
        Map<String, Long> userMap = Maps.newHashMap();
        userMap.put("20L", 10L);
        userMap.put("21L", 10L);
        WidgetUsersMapReduceResult usersResult = new WidgetUsersMapReduceResult(widget_id, userMap);

        expect(mongoOperations.findById(widget_id, WidgetRatingsMapReduceResult.class, WIDGET_RATINGS)).andReturn(null);
        expect(mongoOperations.findById(widget_id, WidgetUsersMapReduceResult.class, WIDGET_USERS)).andReturn(usersResult);
        replay(mongoOperations);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, "21L");

        assertThat(result.getTotalDislike(), is(equalTo(0)));
        assertThat(result.getTotalLike(), is(equalTo(0)));
        assertThat(result.getUserRating(), is(equalTo(-1)));
        assertThat(result.getTotalUserCount(), is(equalTo(2)));
    }

    @Test
    public void getWidgetStatistics_nullUsers() {
        String widget_id = "1L";
        Map<String, Long> userMap = Maps.newHashMap();
        userMap.put("20L", 10L);
        userMap.put("21L", 10L);
        WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult stats = new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userMap, 20L, 0L);
        WidgetRatingsMapReduceResult ratingsResult = new WidgetRatingsMapReduceResult(widget_id, stats);

        expect(mongoOperations.findById(widget_id, WidgetRatingsMapReduceResult.class, WIDGET_RATINGS)).andReturn(ratingsResult);
        expect(mongoOperations.findById(widget_id, WidgetUsersMapReduceResult.class, WIDGET_USERS)).andReturn(null);
        replay(mongoOperations);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, "21L");

        assertThat(result.getTotalDislike(), is(equalTo(0)));
        assertThat(result.getTotalLike(), is(equalTo(20)));
        assertThat(result.getUserRating(), is(equalTo(10)));
        assertThat(result.getTotalUserCount(), is(equalTo(0)));
    }

    @Test @Ignore
    public void init_existing() {
        expect(mongoOperations.findById(eq(ID), eq(RunStatistics.class), eq(OPERATIONS))).andReturn(new RunStatistics(ID, System.currentTimeMillis() - (DEFAULT_RESULT_VALIDITY * 1000)));
        setMapReduceExpectations();
        mongoOperations.save(isA(RunStatistics.class), eq(OPERATIONS));
        expectLastCall();
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    @Test
    public void init_empty() {
        expect(mongoOperations.findById(ID, RunStatistics.class, OPERATIONS)).andReturn(null);
        setMapReduceExpectations();
        mongoOperations.save(isA(RunStatistics.class), eq(OPERATIONS));
        expectLastCall();
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    @Test
    public void init_tooEarly() {
        expect(mongoOperations.findById(ID, RunStatistics.class, OPERATIONS)).andReturn(new RunStatistics(ID, System.currentTimeMillis() - 1000L));
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    @Test
    public void runStats() {
        String id = "BOO";
        long timestamp = 1234L;
        RunStatistics stats = new RunStatistics();
        stats.setId(id);
        stats.setRefreshedTimeStamp(timestamp);

        assertThat(stats.getId(), is(equalTo(id)));
        assertThat(stats.getRefreshedTimeStamp(), is(equalTo(timestamp)));
    }

    private void setMapReduceExpectations() {
        expect(mongoOperations.mapReduce(eq(WIDGET_COLLECTION), eq(RATINGS_MAP), eq(RATINGS_REDUCE), anyObject(MapReduceOptions.class), eq(WidgetRatingsMapReduceResult.class))).andReturn(null);
        expect(mongoOperations.mapReduce(eq(PAGE_COLLECTION), eq(USERS_MAP), eq(USERS_REDUCE),  anyObject(MapReduceOptions.class), eq(WidgetUsersMapReduceResult.class))).andReturn(null);
    }

    private Map<String, Long> getRatingsMap() {
        Map<String, Long> userRatings = Maps.newHashMap();
        userRatings.put("1L", 10L);
        userRatings.put("2L", 10L);
        return userRatings;
    }

    private Map<String, Long> getUsersMap() {
        Map<String, Long> users = Maps.newHashMap();
        users.put("1L", 1L);
        users.put("2L", 1L);
        users.put("3L", 1L);
        users.put("4L", 1L);
        users.put("5L", 1L);
        users.put("6L", 1L);
        return users;
    }
}
