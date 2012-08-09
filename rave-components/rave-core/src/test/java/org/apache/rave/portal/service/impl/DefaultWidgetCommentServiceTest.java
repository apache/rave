/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.apache.rave.portal.service.WidgetCommentService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DefaultWidgetCommentServiceTest {

    private WidgetCommentRepository widgetCommentRepository;
    private WidgetCommentService widgetCommentService;


    @Before
    public void setup() {
        widgetCommentRepository = createMock(WidgetCommentRepository.class);
        widgetCommentService = new DefaultWidgetCommentService(widgetCommentRepository);
    }

    @Test
    public void getWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetCommentRepository.get("1")).andReturn(comment);
        replay(widgetCommentRepository);

        assertEquals(comment, widgetCommentService.getWidgetComment("1"));
        verify(widgetCommentRepository);
    }

    @Test
    public void saveWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetCommentRepository.save(comment)).andReturn(comment);
        replay(widgetCommentRepository);

        widgetCommentService.saveWidgetComment(comment);
        verify(widgetCommentRepository);
    }

    @Test
    public void deleteWidgetComment() {
        WidgetComment comment = new WidgetCommentImpl("1");
        expect(widgetCommentRepository.get("1")).andReturn(comment);
        widgetCommentRepository.delete(comment);
        replay(widgetCommentRepository);

        widgetCommentService.removeWidgetComment("1");
        verify(widgetCommentRepository);
    }

    @Test
    public void deleteAll() {
        final String USER_ID = "33";
        final int EXPECTED_COUNT = 43;

        expect(widgetCommentRepository.deleteAll(USER_ID)).andReturn(EXPECTED_COUNT);
        replay(widgetCommentRepository);
        assertThat(widgetCommentService.deleteAll(USER_ID), is(EXPECTED_COUNT));
        verify(widgetCommentRepository);
    }
}