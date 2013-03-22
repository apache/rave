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
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Test for MongoDb Widget Comment Repository class
 */
public class MongoDbWidgetCommentRepositoryTest {

    private MongoWidgetOperations template;
    private MongoDbWidgetRepository repo;

    @Before
    public void setUp(){
        template = createMock(MongoWidgetOperations.class);
        repo = new MongoDbWidgetRepository();
        repo.setTemplate(template);

    }

    @Test
    public void deleteAll(){
        String userId = "1234L";
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        List<WidgetComment> comments = Lists.newArrayList();
        WidgetComment wc1 = new WidgetCommentImpl();
        WidgetComment wc2 = new WidgetCommentImpl();
        wc1.setUserId(userId);
        wc2.setUserId(userId);
        comments.add(wc1);
        comments.add(wc2);
        w.setComments(comments);
        widgets.add(w);

        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(isA(Widget.class))).andReturn(w);
        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(isA(Widget.class))).andReturn(w);
        replay(template);

        int count = repo.deleteAllWidgetComments(userId);
        assertThat(count, is(equalTo(2)));
    }

    @Test
    public void deleteAll_zero(){
        String userId = "1234L";
        String userId_2 = "1111L";
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        List<WidgetComment> comments = Lists.newArrayList();
        WidgetComment wc1 = new WidgetCommentImpl();
        WidgetComment wc2 = new WidgetCommentImpl();
        wc1.setUserId(userId_2);
        wc2.setUserId(userId_2);
        comments.add(wc1);
        comments.add(wc2);
        w.setComments(comments);
        widgets.add(w);

        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        replay(template);

        int count = repo.deleteAllWidgetComments(userId);
        assertThat(count, is(equalTo(0)));

    }

    @Test
    public void get(){
        String id = "1234L";
        String widgetId = "321L";
        List<WidgetComment> comments = Lists.newArrayList();
        Widget widget = new WidgetImpl(widgetId);
        WidgetComment wc = new WidgetCommentImpl(id);
        comments.add(wc);
        widget.setComments(comments);

        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        WidgetComment result = repo.getCommentById(widgetId, id);
        assertThat(result.getId(), is(equalTo(id)));
    }

    @Test
    public void get_null(){
        String id = "1234L";
        String widgetId = "321L";
        List<WidgetComment> comments = Lists.newArrayList();
        Widget widget = new WidgetImpl(widgetId);
        WidgetComment wc = new WidgetCommentImpl("1111L");
        comments.add(wc);
        widget.setComments(comments);

        expect(template.get(widgetId)).andReturn(widget);
        replay(template);

        WidgetComment result = repo.getCommentById(widgetId, id);
        assertNull(result);

    }

}
