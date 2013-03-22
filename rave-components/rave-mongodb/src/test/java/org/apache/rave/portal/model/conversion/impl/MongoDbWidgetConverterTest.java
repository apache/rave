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

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.easymock.EasyMock.createNiceMock;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * This is a test for the MongoDb Widget Converter
 */
public class MongoDbWidgetConverterTest {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private MongoDbWidgetConverter converter;
    private static final String ID = "1234L";
    public static final Date DATE = new Date();

    @Before
    public void setUp() {

        userRepository = createNiceMock(UserRepository.class);
        categoryRepository = createNiceMock(CategoryRepository.class);
        converter = new MongoDbWidgetConverter();
        converter.setCategoryRepository(categoryRepository);
    }

    @Test
    public void hydrate_valid() {

        MongoDbWidget dehydrated = new MongoDbWidget();

        dehydrated.setComments(Lists.<WidgetComment>newLinkedList());
        WidgetCommentImpl wc = new WidgetCommentImpl();
        dehydrated.getComments().add(wc);

        dehydrated.setTags(Lists.<WidgetTag>newLinkedList());
        WidgetTagImpl wt = new WidgetTagImpl();
        dehydrated.getTags().add(wt);

        converter.hydrate(dehydrated);

        assertThat(dehydrated.getCategoryRepository(), is(sameInstance(categoryRepository)));

    }

    @Test
    public void hydrate_Tags_Null_Comments_Null() {
        MongoDbWidget dehydrated = new MongoDbWidget();
        converter.hydrate(dehydrated);
        assertThat(dehydrated.getCategoryRepository(), is(sameInstance(categoryRepository)));
    }

    @Test
    public void hydrate_null() {
        converter.hydrate(null);
        assertThat(true, is(true));
    }

    @Test
    public void hydrate_NotMongoWidget_NotMongoComments() {
        WidgetTag widgetTag = new WidgetTagImpl();
        WidgetComment widgetComment = new WidgetCommentImpl();

        MongoDbWidget widget = new MongoDbWidget();
        widget.setComments(Arrays.asList(widgetComment));
        widget.setTags(Arrays.asList(widgetTag));

        converter.hydrate(widget);

        assertTrue(widgetTag instanceof WidgetTagImpl);
        assertTrue(widgetComment instanceof WidgetCommentImpl);
    }

    @Test
    public void convert_widget() {

        MongoDbWidget converted;
        Widget source = new WidgetImpl(ID);
        source.setOwnerId(ID);
        source.setUrl("http://mitre.org");
        source.setType("type");
        source.setTitle("title");
        source.setTitleUrl("http://title.org");
        source.setThumbnailUrl("http://thumbnail.org");
        source.setScreenshotUrl("http://screenshot.org");
        source.setAuthor("author");
        source.setAuthorEmail("authorEmail");
        source.setDescription("description");
        source.setWidgetStatus(WidgetStatus.PREVIEW);
        source.setComments(Lists.<WidgetComment>newLinkedList());
        source.setDisableRendering(true);
        source.setFeatured(true);

        source.setCategories(Lists.<Category>newLinkedList());
        Category c = new CategoryImpl(ID);
        source.getCategories().add(c);

        source.setComments(Lists.<WidgetComment>newLinkedList());
        WidgetComment wc = new WidgetCommentImpl(ID);
        wc.setUserId(ID);
        wc.setCreatedDate(DATE);
        wc.setLastModifiedDate(DATE);
        wc.setText("text");
        source.getComments().add(wc);

        source.setTags(Lists.<WidgetTag>newLinkedList());
        WidgetTagImpl wt = new WidgetTagImpl(ID, DATE, ID);
        source.getTags().add(wt);

        source.setRatings(Lists.<WidgetRating>newLinkedList());
        WidgetRating wr = new WidgetRatingImpl();
        source.getRatings().add(wr);

        converted = converter.convert(source);

        assertThat(converted.getUrl(), is(equalTo("http://mitre.org")));
        assertThat(converted.getType(), is(equalTo("type")));
        assertThat(converted.getTitle(), is(equalTo("title")));
        assertThat(converted.getTitleUrl(), is(equalTo("http://title.org")));
        assertThat(converted.getThumbnailUrl(), is(equalTo("http://thumbnail.org")));
        assertThat(converted.getScreenshotUrl(), is(equalTo("http://screenshot.org")));
        assertThat(converted.getAuthor(), is(equalTo("author")));
        assertThat(converted.getAuthorEmail(), is(equalTo("authorEmail")));
        assertThat(converted.getDescription(), is(equalTo("description")));
        assertThat(converted.getWidgetStatus(), is(WidgetStatus.PREVIEW));
        assertNotNull(converted.getComments());

        //Test convertCategories method
        assertNotNull(converted.getCategoryIds());
        assertThat(converted.getCategoryIds().get(0), is(equalTo(ID)));
        assertNull(converted.getCategoryRepository());

        //Test convertComments method
        assertNotNull(converted.getComments());
        assertThat(converted.getComments().get(0), is(instanceOf(WidgetCommentImpl.class)));
        assertThat(((WidgetCommentImpl) (converted.getComments().get(0))).getUserId(), is(equalTo(ID)));
        assertThat(converted.getComments().get(0).getId(), is(equalTo(ID)));
        assertThat(converted.getComments().get(0).getCreatedDate(), is(equalTo(DATE)));
        assertThat(converted.getComments().get(0).getLastModifiedDate(), is(equalTo(DATE)));
        assertThat(converted.getComments().get(0).getText(), is(equalTo("text")));

        //Test convertTags method
        assertNotNull(converted.getTags());
        assertThat(converted.getTags().get(0), is(instanceOf(WidgetTagImpl.class)));
        assertNotNull(converted.getTags().get(0).getTagId());
        assertThat(converted.getTags().get(0).getTagId(), is(equalTo(ID)));
        assertThat(converted.getTags().get(0).getCreatedDate(), is(equalTo(DATE)));
        assertThat(((WidgetTagImpl) (converted.getTags().get(0))).getUserId(), is(equalTo(ID)));

        //Test convertRatings method
        assertNotNull(converted.getRatings());
        assertNotNull(converted.getRatings().get(0).getId());
    }

    @Test
    public void convert_Null() {
        Widget source = new WidgetImpl();
        MongoDbWidget converted = converter.convert(source);

        assertNull(converted.getOwnerId());
        assertNotNull(converted.getCategories());//the getter sets this field
        assertNotNull(converted.getCategoryIds());//the getter sets this field
        assertNotNull(converted.getComments());
        assertNotNull(converted.getTags());
        assertNotNull(converted.getRatings());
    }

    @Test
    public void convert_MongoInstance() {
        Widget source = new MongoDbWidget();
        WidgetComment comment = new WidgetCommentImpl();
        comment.setUserId(ID);
        source.setComments(Arrays.asList(comment));

        WidgetTag tag = new WidgetTagImpl(ID, DATE, ID);
        source.setTags(Arrays.asList(tag));

        WidgetRatingImpl rating = new WidgetRatingImpl();
        rating.setId(ID);
        source.setRatings(Arrays.<WidgetRating>asList(rating));

        Widget converted = converter.convert(source);

        assertTrue(converted.getComments().get(0) instanceof WidgetCommentImpl);
        assertTrue(converted.getTags().get(0) instanceof WidgetTagImpl);
        assertThat(converted.getRatings().get(0).getId(), is(equalTo(ID)));
    }
}
