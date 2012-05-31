/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaWidgetComment;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.WidgetCommentImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetCommentConverterTest {

    @Autowired
    private JpaWidgetCommentConverter widgetCommentConverter;

    @Test
    public void noConversion() {
        WidgetComment comment = new JpaWidgetComment();
        assertThat(widgetCommentConverter.convert(comment), is(sameInstance(comment)));
    }

    @Test
    public void newComment() {
        WidgetComment comment = new WidgetCommentImpl();
        comment.setCreatedDate(new Date());
        comment.setId(9L);
        comment.setLastModifiedDate(new Date());
        comment.setText("hello");
        comment.setUser(new User(1L));
        comment.setWidgetId(9L);

        JpaWidgetComment converted = widgetCommentConverter.convert(comment);
        assertThat(converted, is(not(sameInstance(comment))));
        assertThat(converted, is(instanceOf(JpaWidgetComment.class)));
        assertThat(converted.getCreatedDate(), is(equalTo(comment.getCreatedDate())));
        assertThat(converted.getEntityId(), is(equalTo(comment.getId())));
        assertThat(converted.getId(), is(equalTo(comment.getId())));
        assertThat(converted.getLastModifiedDate(), is(equalTo(comment.getLastModifiedDate())));
        assertThat(converted.getText(), is(equalTo(comment.getText())));
        assertThat(converted.getUser(), is(equalTo(comment.getUser())));
        assertThat(converted.getWidgetId(), is(equalTo(comment.getWidgetId())));
    }
}