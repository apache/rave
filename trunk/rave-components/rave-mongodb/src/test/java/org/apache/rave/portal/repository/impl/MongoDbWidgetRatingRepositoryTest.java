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

import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetRating;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


/**
 * User: DSULLIVAN
 * Date: 12/7/12
 * Time: 1:24 PM
 */
public class MongoDbWidgetRatingRepositoryTest {
    private MongoDbWidgetRepository ratingRepository;
    private MongoWidgetOperations template;

    @Before
    public void setup() {
        template = createMock(MongoWidgetOperations.class);
        ratingRepository = new MongoDbWidgetRepository();
        ratingRepository.setTemplate(template);
    }

    @Test
    public void getByWidgetIdAndUserId_Valid(){
        String widgetId = "222";
        String userId = "333";
        Widget widget = new WidgetImpl();
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setUserId(userId);
        widget.setRatings(Arrays.asList(widgetRating));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        assertThat(ratingRepository.getWidgetRatingsByWidgetIdAndUserId(widgetId, userId), is(sameInstance(widgetRating)));
    }

    @Test
    public void getByWidgetIdAndUserId_Null(){
        String widgetId = "222";
        String userId = "333";
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        assertNull(ratingRepository.getWidgetRatingsByWidgetIdAndUserId(widgetId, userId));
    }

    @Test
    public void getByWidgetIdAndUserId_Diff_Id(){
        String widgetId = "222";
        String userId = "333";
        Widget widget = new WidgetImpl();
        WidgetRating widgetRating = new WidgetRatingImpl();
        widgetRating.setUserId("444");
        widget.setRatings(Arrays.asList(widgetRating));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        assertNull(ratingRepository.getWidgetRatingsByWidgetIdAndUserId(widgetId, userId));
    }

    @Test
    public void deleteAll_Valid(){
        String userId = "233";
        List<Widget> widgets = new ArrayList<Widget>();
        Widget delete = new WidgetImpl();
        WidgetRating rating = new WidgetRatingImpl();
        List<WidgetRating> ratings= new ArrayList<WidgetRating>();
        ratings.add(rating);
        delete.setRatings(ratings);
        rating.setUserId(userId);
        widgets.add(delete);
        expect(template.find(query(where("ratings").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(delete)).andReturn(null);
        replay(template);

        int count = ratingRepository.deleteAllWidgetRatings(userId);
        assertFalse(ratings.contains(rating));
        assertThat(count, is(1));
    }

    @Test
    public void deleteAll_Diff_Id(){
        String userId = "111";
        Widget widget = new WidgetImpl();
        List<Widget> widgets = Arrays.asList(widget);
        WidgetRating rating = new WidgetRatingImpl();
        rating.setUserId("222");
        widget.setRatings(Arrays.asList(rating));
        expect(template.find(query(where("ratings").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        replay(template);

        int count = ratingRepository.deleteAllWidgetRatings(userId);
        assertThat(count, is(0));
    }

    @Test
    public void get_Valid(){
        String id = "342";
        String widgetId = "243";
        Widget found = new WidgetImpl(widgetId);
        WidgetRatingImpl widgetRating = new WidgetRatingImpl();
        widgetRating.setId(id);
        found.setRatings(Arrays.asList((WidgetRating) widgetRating));
        expect(template.get(widgetId)).andReturn(found);
        replay(template);

        assertThat(ratingRepository.getRatingById(widgetId, id), is(sameInstance((WidgetRating)widgetRating)));
    }

    @Test
    public void get_Null(){
        String id = "123";
        String widgetId = "1234";
        Widget widget = new WidgetImpl();
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);
        widget.setRatings(new ArrayList<WidgetRating>());

        assertNull(ratingRepository.getRatingById(widgetId, id));
    }

    @Test
    public void get_Diff_Id(){
        String widgetId="1234";
        Widget widget = new WidgetImpl(widgetId);
        String id = "555";
        WidgetRatingImpl rating = new WidgetRatingImpl();
        rating.setId("444");
        widget.setRatings(Arrays.asList((WidgetRating) rating));
        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        assertNull(ratingRepository.getRatingById(widgetId, id));
    }

    @Test
    public void save_Id_Valid(){
        WidgetRatingImpl item = new WidgetRatingImpl();
        WidgetRatingImpl lookup = new WidgetRatingImpl();
        String widgetId = "2134";
        String userId = "3245";
        String id = "3245";
        int score = 838;
        item.setUserId(userId);
        item.setScore(score);
        item.setId(id);
        lookup.setId(id);
        Widget widget = new WidgetImpl();
        widget.setRatings(Arrays.asList((WidgetRating)lookup));

        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        WidgetRating widgetRating = ratingRepository.updateWidgetRating(widgetId, item);

        assertThat(item.getScore(), is(equalTo(lookup.getScore())));
        assertThat(item.getUserId(), is(equalTo(lookup.getUserId())));
        assertThat(item.getScore(), is(equalTo(lookup.getScore())));
        assertThat(widgetRating, is(sameInstance((WidgetRating)lookup)));
    }

    @Test
    public void save_Id_Null(){
        WidgetRating item = new WidgetRatingImpl();
        String widgetId = "5544";
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        WidgetRating widgetRating = ratingRepository.createWidgetRating(widgetId, item);
        assertTrue(widget.getRatings().contains(item));
        assertThat(widgetRating, is(sameInstance(item)));
    }

    @Test
    public void save_Null(){
        WidgetRatingImpl item = new WidgetRatingImpl();
        Widget widget = new WidgetImpl();
        widget.setRatings(new ArrayList<WidgetRating>());
        String id = "123";
        String widgetId = "321";
        Widget saved = new WidgetImpl();
        saved.setRatings(new ArrayList<WidgetRating>());
        item.setId(id);
        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(saved);
        replay(template);

        assertNull(ratingRepository.updateWidgetRating(widgetId, item));
    }

    @Test
    public void save_Diff_Id(){
        WidgetRating item = new WidgetRatingImpl();
        String widgetId = "3333";
        String itemId = "123";
        ((WidgetRatingImpl)item).setId(itemId);
        Widget widget = new WidgetImpl();
        WidgetRating exist = new WidgetRatingImpl();
        ((WidgetRatingImpl)exist).setId("4444");
        widget.setRatings(Arrays.asList(exist));

        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(widget);
        replay(template);

        assertNull(ratingRepository.updateWidgetRating(widgetId, item));
    }

    @Test
    public void delete_Valid(){
        WidgetRating item = new WidgetRatingImpl();
        String widgetId = "387383";
        String id = "234";
        ((WidgetRatingImpl)item).setId(id);
        Widget widget = new WidgetImpl();
        ArrayList<WidgetRating> ratings = new ArrayList<WidgetRating>();
        ratings.add(item);
        widget.setRatings(ratings);
        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(null);
        replay(template);

        ratingRepository.deleteWidgetRating(widgetId, item);
        assertFalse(ratings.contains(item));
        verify(template);
    }

    @Test
    public void delete_Id_Not_Equals(){
         WidgetRating item = new WidgetRatingImpl();
        String widgetId = "32323";
        ((WidgetRatingImpl)item).setId("333333");
        Widget widget = new WidgetImpl();
        WidgetRating exist = new WidgetRatingImpl();
        ((WidgetRatingImpl)exist).setId("323");
        widget.setRatings(Arrays.asList(exist));
        expect(template.get(widgetId)).andReturn(widget);
        expect(template.save(widget)).andReturn(null);
        replay(template);

        ratingRepository.deleteWidgetRating(widgetId, item);
        verify(template);
    }
}
