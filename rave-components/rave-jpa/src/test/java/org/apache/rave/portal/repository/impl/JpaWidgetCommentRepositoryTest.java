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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.JpaWidgetComment;
import org.apache.rave.model.User;
import org.apache.rave.model.WidgetComment;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link JpaWidgetRepository}
 */
@Transactional(readOnly=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetCommentRepositoryTest {
    private final Long VALID_USER_ID = 1L;
    private final Long VALID_WIDGET_COMMENT_ID = 2L;
    private final Long VALID_WIDGET_ID = 4L;

    @PersistenceContext
    private EntityManager sharedManager;

    @Autowired
    private WidgetRepository repository;

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void deleteAll() {
        assertThat(repository.deleteAllWidgetComments(VALID_USER_ID.toString()), is(2));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_new() {
        Date createdDate = new Date();
        Date lastModDate = new Date();
        String text = "my comment";
        User user = new UserImpl(VALID_USER_ID.toString());

        JpaWidgetComment wc = new JpaWidgetComment();
        wc.setCreatedDate(createdDate);
        wc.setWidgetId(VALID_WIDGET_ID.toString());
        wc.setLastModifiedDate(lastModDate);
        wc.setText(text);
        wc.setUserId(VALID_USER_ID.toString());
        assertThat(wc.getId(), is(nullValue()));
        repository.createWidgetComment(VALID_WIDGET_ID.toString(), wc);
        String newId = wc.getId();
        assertThat(Long.parseLong(newId) > 0, is(true));
        WidgetComment newComment = repository.getCommentById(VALID_WIDGET_ID.toString(), newId);
        assertThat(newComment.getUserId(), is(VALID_USER_ID.toString()));
        assertThat(newComment.getText(), is(text));
        assertThat(newComment.getCreatedDate(), is(createdDate));
        assertThat(newComment.getLastModifiedDate(), is(lastModDate));
    }


    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void save_existing() {
        final String UPDATED_TEXT = "updated comment";
        WidgetComment widgetComment = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(widgetComment.getText(), is(not(UPDATED_TEXT)));
        widgetComment.setText(UPDATED_TEXT);
        repository.createWidgetComment(VALID_WIDGET_ID.toString(), widgetComment);
        WidgetComment updatedComment = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(updatedComment.getText(), is(UPDATED_TEXT));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_jpaObject() {
        WidgetComment wc = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(wc, is(notNullValue()));
        repository.deleteWidgetComment(VALID_WIDGET_ID.toString(), wc);
        wc = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(wc, is(nullValue()));
    }

    @Test
    @Transactional(readOnly=false)
    @Rollback(true)
    public void delete_implObject() {
        WidgetComment wc = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(wc, is(notNullValue()));
        WidgetComment impl = new WidgetCommentImpl(wc.getId());
        repository.deleteWidgetComment(VALID_WIDGET_ID.toString(), impl);
        wc = repository.getCommentById(VALID_WIDGET_ID.toString(), VALID_WIDGET_COMMENT_ID.toString());
        assertThat(wc, is(nullValue()));
    }
}