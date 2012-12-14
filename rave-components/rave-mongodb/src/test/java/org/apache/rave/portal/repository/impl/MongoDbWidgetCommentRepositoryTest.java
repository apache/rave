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
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.impl.UserImpl;
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
    private MongoDbWidgetCommentRepository repo;

    @Before
    public void setUp(){
        template = createMock(MongoWidgetOperations.class);
        repo = new MongoDbWidgetCommentRepository();
        repo.setTemplate(template);

    }

    @Test
    public void deleteAll(){
        Long userId = 1234L;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        List<WidgetComment> comments = Lists.newArrayList();
        WidgetComment wc1 = new WidgetCommentImpl();
        WidgetComment wc2 = new WidgetCommentImpl();
        User user = new UserImpl(1234L);
        wc1.setUser(user);
        wc2.setUser(user);
        comments.add(wc1);
        comments.add(wc2);
        w.setComments(comments);
        widgets.add(w);

        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(isA(Widget.class))).andReturn(w);
        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        expect(template.save(isA(Widget.class))).andReturn(w);
        replay(template);

        int count = repo.deleteAll(userId);
        assertThat(count, is(equalTo(2)));
    }

    @Test
    public void deleteAll_zero(){
        Long userId = 1234L;
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        List<WidgetComment> comments = Lists.newArrayList();
        WidgetComment wc1 = new WidgetCommentImpl();
        WidgetComment wc2 = new WidgetCommentImpl();
        User user = new UserImpl(1111L);
        wc1.setUser(user);
        wc2.setUser(user);
        comments.add(wc1);
        comments.add(wc2);
        w.setComments(comments);
        widgets.add(w);

        expect(template.find(query(where("comments").elemMatch(where("userId").is(userId))))).andReturn(widgets);
        replay(template);

        int count = repo.deleteAll(userId);
        assertThat(count, is(equalTo(0)));

    }

    @Test
    public void get(){
        Long id = 1234L;
        List<WidgetComment> comments = Lists.newArrayList();
        Widget widget = new WidgetImpl();
        WidgetComment wc = new WidgetCommentImpl(id);
        comments.add(wc);
        widget.setComments(comments);

        expect(template.findOne(query(where("comments").elemMatch(where("_id").is(id))))).andReturn(widget);
        replay(template);

        WidgetComment result = repo.get(id);
        assertThat(result.getId(), is(equalTo(id)));
    }

    @Test
    public void get_null(){
        Long id = 1234L;
        List<WidgetComment> comments = Lists.newArrayList();
        Widget widget = new WidgetImpl();
        WidgetComment wc = new WidgetCommentImpl(1111L);
        comments.add(wc);
        widget.setComments(comments);

        expect(template.findOne(query(where("comments").elemMatch(where("_id").is(id))))).andReturn(null);
        replay(template);

        WidgetComment result = repo.get(id);
        assertNull(result);

    }

}
