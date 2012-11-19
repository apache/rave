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
import org.apache.rave.portal.repository.util.CollectionNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public void getAllStatistics_valid_allPositive() {

        Map<Long, Long> userRatings = Maps.newHashMap();
        userRatings.put(1L, 10L);
        userRatings.put(2L, 10L);

        Map<Long, Long> users = Maps.newHashMap();
        users.put(1L, 1L);
        users.put(2L, 1L);
        users.put(3L, 1L);
        users.put(4L, 1L);
        users.put(5L, 1L);
        users.put(6L, 1L);

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult(24L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 20L, 0L)),
                new WidgetRatingsMapReduceResult(25L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 20L, 0L)),
                new WidgetRatingsMapReduceResult(26L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 10L, 0L))
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
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(20)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(10)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }

    @Test
    public void getAllStatistics_valid_allNegative() {

        Map<Long, Long> userRatings = Maps.newHashMap();
        userRatings.put(1L, 10L);
        userRatings.put(2L, 10L);

        Map<Long, Long> users = Maps.newHashMap();
        users.put(1L, 1L);
        users.put(2L, 1L);
        users.put(3L, 1L);
        users.put(4L, 1L);
        users.put(5L, 1L);
        users.put(6L, 1L);

        List<WidgetRatingsMapReduceResult> ratings = Arrays.asList(
                new WidgetRatingsMapReduceResult(24L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 20L, 0L)),
                new WidgetRatingsMapReduceResult(25L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 20L, 0L)),
                new WidgetRatingsMapReduceResult(26L, new WidgetRatingsMapReduceResult.WidgetStatisticsMapReduceResult(userRatings, 10L, 0L))
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
        assertThat(stats.get(24L).getTotalLike(), is(equalTo(20)));
        assertThat(stats.get(24L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(24L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(24L).getTotalUserCount(), is(equalTo(users.size())));
        assertThat(stats.get(26L).getTotalLike(), is(equalTo(10)));
        assertThat(stats.get(26L).getTotalDislike(), is(equalTo(0)));
        assertThat(stats.get(26L).getUserRating(), is(equalTo(10)));
        assertThat(stats.get(26L).getTotalUserCount(), is(equalTo(users.size())));
    }
}
