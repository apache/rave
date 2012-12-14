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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceCounts;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.apache.rave.portal.repository.impl.MongoDbMapReduceStatisticsAggregator.*;
import static org.apache.rave.portal.repository.util.CollectionNames.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MonogoDbMapReduceStatisticsAggregatorTest {

    private MongoOperations mongoOperations;
    private StatisticsAggregator aggregator;

    @Before
    public void setup() {
        mongoOperations = createMock(MongoOperations.class);
        aggregator = new MongoDbMapReduceStatisticsAggregator(mongoOperations);
    }

    @Test
    public void getStatistics_Case0(){
        long widget_id = 123;
        long user_id = 321;
        Query statsQuery = query(where("widgetId").is(widget_id));
        Query pageQuery = query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widget_id))));
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = createMock(MapReduceResults.class);
        MapReduceCounts mapReduceCounts = createMock(MapReduceCounts.class);
        List<MongoDbPage> pages = new ArrayList<MongoDbPage>();

        expect(mongoOperations.mapReduce(statsQuery, WIDGET_COLLECTION, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class)).andReturn(widgetStats);
        expect(mongoOperations.find(pageQuery, MongoDbPage.class, PAGE_COLLECTION)).andReturn(pages);
        expect(widgetStats.getCounts()).andReturn(mapReduceCounts);
        expect(mapReduceCounts.getOutputCount()).andReturn(0);
        replay(mongoOperations, widgetStats, mapReduceCounts);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, user_id);

        assertTrue(result.getTotalUserCount() == 0);
    }

    @Test
    public void getStatistics_Case1(){
        long widget_id = 123;
        long user_id = 321;
        Query statsQuery = query(where("widgetId").is(widget_id));
        Query pageQuery = query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widget_id))));
        MapReduceResults<WidgetRatingsMapReduceResult> widgetStats = createMock(MapReduceResults.class);
        MapReduceCounts mapReduceCounts = createMock(MapReduceCounts.class);
        WidgetRatingsMapReduceResult statsResult = new WidgetRatingsMapReduceResult();
        List<MongoDbPage> pages = new ArrayList<MongoDbPage>();
        Iterator iter = createMock(Iterator.class);

        //add page to pages
        Page page = new MongoDbPage();
        User owner = new UserImpl();
        ((UserImpl)owner).setId(234L);
        page.setOwner(owner);
        pages.add((MongoDbPage)page);

        //add same id to cover branches
        Page page_2 = new MongoDbPage();
        User owner_2 = new UserImpl();
        ((UserImpl)owner_2).setId(234L);
        page_2.setOwner(owner_2);
        pages.add((MongoDbPage)page_2);

        expect(mongoOperations.mapReduce(statsQuery, WIDGET_COLLECTION, RATINGS_MAP, RATINGS_REDUCE, WidgetRatingsMapReduceResult.class)).andReturn(widgetStats);
        expect(mongoOperations.find(pageQuery, MongoDbPage.class, PAGE_COLLECTION)).andReturn(pages);
        expect(widgetStats.getCounts()).andReturn(mapReduceCounts);
        expect(mapReduceCounts.getOutputCount()).andReturn(1);
        expect(widgetStats.iterator()).andReturn(iter);
        expect(iter.next()).andReturn(statsResult);
        replay(mongoOperations, widgetStats, mapReduceCounts, iter);

        WidgetStatistics result = aggregator.getWidgetStatistics(widget_id, user_id);

        assertThat(result.getTotalUserCount(), is(equalTo(1)));
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
    @Ignore
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
