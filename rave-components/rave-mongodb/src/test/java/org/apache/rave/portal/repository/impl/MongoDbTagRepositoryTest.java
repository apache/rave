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
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test for MongoDb Tag Repository class
 */
public class MongoDbTagRepositoryTest {

    private MongoWidgetOperations widgetTemplate;
    private MongoDbTagRepository repo;

    @Before
    public void setUp(){
        widgetTemplate = createMock(MongoWidgetOperations.class);
        repo = new MongoDbTagRepository();
        repo.setWidgetTemplate(widgetTemplate);

    }

    @Test
    public void getAll(){
        List<Widget> widgets = Lists.newArrayList();
        List<WidgetTag> widget_tags = Lists.newArrayList();
        WidgetTag wt = new WidgetTagImpl();
        Tag tag = new TagImpl();
        wt.setTag(tag);
        widget_tags.add(wt);
        Widget w = new WidgetImpl();
        w.setTags(widget_tags);
        widgets.add(w);

        expect(widgetTemplate.find(new Query())).andReturn(widgets);
        expect(widgetTemplate.find(new Query())).andReturn(widgets);
        replay(widgetTemplate);

        List<Tag> result = repo.getAll();
        assertNotNull(result);
        assertThat(result.get(0), is(sameInstance(tag)));

        int count = repo.getCountAll();
        assertThat(count, is(equalTo(1)));

    }

    @Test
    public void getAll_null(){
        List<Widget> widgets = Lists.newArrayList();
        Widget w = new WidgetImpl();
        widgets.add(w);

        expect(widgetTemplate.find(new Query())).andReturn(widgets);
        replay(widgetTemplate);

        List<Tag> result = repo.getAll();
        assertThat(result.size(), is(equalTo(0)));

    }

    @Test (expected = NotSupportedException.class)
    public void save(){
        Tag tag = new TagImpl();
        repo.save(tag);

    }

    @Test (expected = NotSupportedException.class)
    public void delete(){
        Tag tag = new TagImpl();
        repo.delete(tag);

    }

//    @Test
//    public void getAvailableTagsByWidgetId(){
//        List<Widget> widgets = Lists.newArrayList();
//        List<WidgetTag> widget_tags = Lists.newArrayList();
//        WidgetTag wt = new WidgetTagImpl();
//        wt.setWidgetId(1111L);
//        Tag tag = new TagImpl();
//        wt.setTag(tag);
//        widget_tags.add(wt);
//        Widget w = new WidgetImpl(1234L);
//        w.setTags(widget_tags);
//        widgets.add(w);
//
//        expect(widgetTemplate.find(new Query())).andReturn(widgets);
//        // The following expect is getting this error...
//        // Method threw 'java.lang.NullPointerException' exception.
//        // Cannot evaluate org.easymock.internal.Invocation.toString()
//        expect(widgetTemplate.get(1234L).getTags()).andReturn(widget_tags);
//        replay(widgetTemplate);
//
//        List<Tag> result = repo.getAvailableTagsByWidgetId(1234L);
//        assertThat(result.size(), is(equalTo(1)));
//    }


}
