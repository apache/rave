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

        Map<Long, Long> userRatings = getRatingsMap();
        Map<Long, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult(24L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult(25L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult(26L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L))
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult(24L, users),
                new WidgetUsersMapReduceResult(25L, users),
                new WidgetUsersMapReduceResult(26L, users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<Long, WidgetStatistics> stats = aggregator.getAllWidgetStatistics(1L);
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(2)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(2)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_noUserRating() {

        Map<Long, Long> userRatings = getRatingsMap();
        Map<Long, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult(24L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult(25L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L)),
                new WidgetRatingsMapReduceResult(26L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 2L, 0L))
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult(24L, users),
                new WidgetUsersMapReduceResult(25L, users),
                new WidgetUsersMapReduceResult(26L, users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<Long, WidgetStatistics> stats = aggregator.getAllWidgetStatistics(5L);
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(2)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(2)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_nullStats() {

        Map<Long, Long> userRatings = getRatingsMap();
        Map<Long, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult(24L, null),
                new WidgetRatingsMapReduceResult(25L, null),
                new WidgetRatingsMapReduceResult(26L, null)
        );

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult(24L, users),
                new WidgetUsersMapReduceResult(25L, users),
                new WidgetUsersMapReduceResult(26L, users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<Long, WidgetStatistics> stats = aggregator.getAllWidgetStatistics(5L);
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(0)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(0)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_noRatings() {

        Map<Long, Long> users = getUsersMap();

        List<WidgetRatingsMapReduceResult> ratings = Lists.newArrayList();

        List<WidgetUsersMapReduceResult> usersMapReduceResults = Arrays.asList(
                new WidgetUsersMapReduceResult(24L, users),
                new WidgetUsersMapReduceResult(25L, users),
                new WidgetUsersMapReduceResult(26L, users)
        );

        expect(mongoOperations.findAll(WidgetRatingsMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(ratings);
        expect(mongoOperations.findAll(WidgetUsersMapReduceResult.class, CollectionNames.STATS_COLLECTION)).andReturn(usersMapReduceResults);
        replay(mongoOperations);

        Map<Long, WidgetStatistics> stats = aggregator.getAllWidgetStatistics(5L);
        assertThat(stats.size(), is(equalTo(3)));
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(0)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(0)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(-1)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void init_existing() {
        expect(mongoOperations.findById(ID, RunStatistics.class, STATS_COLLECTION)).andReturn(new RunStatistics(ID, System.currentTimeMillis() - DEFAULT_RESULT_VALIDITY));
        setMapReduceExpectations();
        mongoOperations.save(isA(RunStatistics.class), eq(STATS_COLLECTION));
        expectLastCall();
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    @Test
    public void init_empty() {
        expect(mongoOperations.findById(ID, RunStatistics.class, STATS_COLLECTION)).andReturn(null);
        setMapReduceExpectations();
        mongoOperations.save(isA(RunStatistics.class), eq(STATS_COLLECTION));
        expectLastCall();
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    @Test
    public void init_tooEarly() {
        expect(mongoOperations.findById(ID, RunStatistics.class, STATS_COLLECTION)).andReturn(new RunStatistics(ID, System.currentTimeMillis() - 1000L));
        replay(mongoOperations);

        ((MongoDbMapReduceStatisticsAggregator)aggregator).buildStats();
        verify(mongoOperations);
    }

    private void setMapReduceExpectations() {
        expect(mongoOperations.mapReduce(eq(WIDGET_COLLECTION), eq(RATINGS_MAP), eq(RATINGS_REDUCE), isA(MapReduceOptions.class), eq(WidgetRatingsMapReduceResult.class))).andReturn(null);
        expect(mongoOperations.mapReduce(eq(PAGE_COLLECTION), eq(USERS_MAP), eq(USERS_REDUCE), isA(MapReduceOptions.class), eq(WidgetUsersMapReduceResult.class))).andReturn(null);
    }

    private Map<Long, Long> getRatingsMap() {
        Map<Long, Long> userRatings = Maps.newHashMap();
        userRatings.put(1L, 10L);
        userRatings.put(2L, 10L);
        return userRatings;
    }

    private Map<Long, Long> getUsersMap() {
        Map<Long, Long> users = Maps.newHashMap();
        users.put(1L, 1L);
        users.put(2L, 1L);
        users.put(3L, 1L);
        users.put(4L, 1L);
        users.put(5L, 1L);
        users.put(6L, 1L);
        return users;
    }
}
