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
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
* Test for MongoDb Widget Repository class
*/

public class MongoDbWidgetRepositoryTest {

    private MongoWidgetOperations template;
    private StatisticsAggregator statsAggregator;
    private MongoDbWidgetRepository repo;



    @Before
    public void setUp(){
        template = createMock(MongoWidgetOperations.class);
        statsAggregator = createMock(StatisticsAggregator.class);
        repo = new MongoDbWidgetRepository();
        repo.setTemplate(template);
        repo.setStatsAggregator(statsAggregator);

    }

    @Test
    public void getAll(){
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setTitle("B");
        Widget w2 = new WidgetImpl();
        w.setTitle("A");
        widgets.add(w);
        widgets.add(w2);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getAll();
        assertThat(result.size(), is(equalTo(2)));
        assertThat(result.get(0), is(equalTo(w)));

    }

    @Test
    public void getLimitedList(){
        int offset = 2;
        int pageSize = 10;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setTitle("B");
        Widget w2 = new WidgetImpl();
        w.setTitle("A");
        widgets.add(w);
        widgets.add(w2);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getLimitedList(offset, pageSize);
        assertThat(result.size(), is(equalTo(2)));

    }

    @Test
    public void getCountAll(){
        long count = 0;

        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        count = repo.getCountAll();
        assertNotNull(count);
    }

    @Test
    public void getByFreeTextSearch(){
        int offset = 2;
        int pageSize = 10;
        String searchTerm = "test";
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setTitle("A");
        w.setDescription("test");
        widgets.add(w);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getByFreeTextSearch(searchTerm, offset, pageSize);
        assertNotNull(result);
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void getCountFreeTextSearch(){
        long count = 0;
        String searchTerm = "test";
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        Widget w2 = new WidgetImpl();
        w.setTitle("A");
        w.setDescription("test");
        widgets.add(w);
        widgets.add(w2);

        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        count = repo.getCountFreeTextSearch(searchTerm);
        assertNotNull(count);

    }

    @Test
    public void getByStatus(){
        int offset = 2;
        int pageSize = 10;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget w2 = new WidgetImpl();
        w2.setWidgetStatus(WidgetStatus.PREVIEW);
        w.setTitle("A");
        w.setDescription("test");
        widgets.add(w);
        widgets.add(w2);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getByStatus(WidgetStatus.PUBLISHED, offset, pageSize);
        assertNotNull(result);

    }

    @Test
    public void getCountByStatus(){
        long count = 0;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget w2 = new WidgetImpl();
        w2.setWidgetStatus(WidgetStatus.PREVIEW);
        w.setTitle("A");
        w.setDescription("test");
        widgets.add(w);
        widgets.add(w2);

        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        count = repo.getCountByStatus(WidgetStatus.PUBLISHED);
        assertNotNull(count);

    }

    @Test
    public void getByStatusAndTypeAndFreeTextSearch(){
        int offset = 2;
        int pageSize = 10;
        String type = "type";
        String searchTerm = "test" ;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget w2 = new WidgetImpl();
        w2.setWidgetStatus(WidgetStatus.PREVIEW);
        w.setTitle("A");
        w.setDescription("test");
        w.setType(type);
        widgets.add(w);
        widgets.add(w2);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, type, searchTerm, offset, pageSize);
        assertNotNull(result);

    }

    @Test
    public void getByStatusAndTypeAndFreeTextSearch_null(){
        int offset = 2;
        int pageSize = 10;
        String type = "type";
        String searchTerm = "test" ;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        Widget w2 = new WidgetImpl();
        widgets.add(w);
        widgets.add(w2);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getByStatusAndTypeAndFreeTextSearch(WidgetStatus.PUBLISHED, type, searchTerm, offset, pageSize);
        assertNotNull(result);

    }

    @Test
    public void getCountByStatusAndTypeAndFreeText(){
        long count = 0;
        String type = "type";
        String searchTerm = "test" ;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setWidgetStatus(WidgetStatus.PUBLISHED);
        Widget w2 = new WidgetImpl();
        w2.setWidgetStatus(WidgetStatus.PREVIEW);
        w.setTitle("A");
        w.setDescription("test");
        widgets.add(w);
        widgets.add(w2);

        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        count = repo.getCountByStatusAndTypeAndFreeText(WidgetStatus.PUBLISHED, type, searchTerm);
        assertNotNull(count);

    }

    @Test
    public void getByOwmer(){
        int offset = 2;
        int pageSize = 10;
        User owner = new UserImpl("1234L");
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setOwnerId(owner.getId());
        widgets.add(w);

        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);

        List<Widget> result = repo.getByOwner(owner, offset, pageSize);
        assertNotNull(result);
    }

    @Test
    public void getcountByOwner(){
        long count = 0;
        int offset = 2;
        int pageSize = 10;
        String id = "1234L";
        User owner = new UserImpl(id);
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        w.setOwnerId(id);
        widgets.add(w);

        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        count = repo.getCountByOwner(owner, offset, pageSize);
        assertNotNull(count);
    }

    @Test
    public void getByUrl(){
        String widgetUrl = "www.test.com";
        Widget widget = new WidgetImpl();
        widget.setUrl(widgetUrl);

        expect(template.findOne(new Query(where("url").is(widgetUrl)))).andReturn(widget);
        replay(template);

        Widget result = repo.getByUrl(widgetUrl);
        assertThat(result, is(equalTo(widget)));
        assertThat(result.getUrl(), is(equalTo(widgetUrl)));

    }

    @Test
    public void getWidgetStatistics(){
        String widget_id = "1111L";
        String user_id = "2222L";

        WidgetStatistics ws = new WidgetStatistics();

        expect(statsAggregator.getWidgetStatistics(widget_id, user_id)).andReturn(ws);
        replay(statsAggregator);

        ws = repo.getWidgetStatistics(widget_id, user_id);
        assertNotNull(ws);

    }

    @Test
    public void getAllWidgetStatistics(){
        String user_id = "2222L";
        Map<String, WidgetStatistics> ws = Maps.newHashMap();

        expect(statsAggregator.getAllWidgetStatistics(user_id)).andReturn(ws);
        replay(statsAggregator);

        ws = repo.getAllWidgetStatistics(user_id);
        assertNotNull(ws);

    }

    @Test
    public void getUserWidgetRatings(){
        String userId = "1234L";
        Map<String, WidgetRating> wr = Maps.newHashMap();
        List<Widget> widgets = Lists.newArrayList();
        List<WidgetRating> widget_ratings = Lists.newArrayList();
        Widget widget = new WidgetImpl("1111L");
        WidgetRating rating1 = new WidgetRatingImpl();
        WidgetRating rating2 = new WidgetRatingImpl();
        rating1.setUserId(userId);
        rating2.setUserId("5555L");
        widget_ratings.add(rating1);
        widget_ratings.add(rating2);
        widget.setRatings(widget_ratings);
        widgets.add(widget);
        Query q = query(where("ratings").elemMatch(where("userId").is(userId)));

        expect(template.find(q)).andReturn(widgets);
        replay(template);

        wr = repo.getUsersWidgetRatings(userId);
        assertNotNull(wr);
    }

    @Test
    public void getWidgetByTag(){
        int offset = 2;
        int pagesize = 10;
        String tagKeyword = "test";
        List<Widget> widgets = Lists.newArrayList();
        Widget widget = new WidgetImpl();
        Tag tag = new TagImpl();
        expect(template.find(isA(Query.class))).andReturn(widgets);
        replay(template);
    }

    public void setTemplate(MongoWidgetOperations template) {
        this.template = template;
    }

    public void setStatsAggregator(StatisticsAggregator statsAggregator) {
        this.statsAggregator = statsAggregator;
    }

}
