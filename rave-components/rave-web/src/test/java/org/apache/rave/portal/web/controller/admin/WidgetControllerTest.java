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

package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test for {@link WidgetController}
 */
public class WidgetControllerTest {

    private static final String TABS = "tabs";
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_PAGESIZE = AdminControllerUtil.DEFAULT_PAGE_SIZE;

    private WidgetController controller;
    private WidgetService service;
    private NewWidgetValidator validator;

    @Test
    public void adminWidgets() throws Exception {
        Model model = new ExtendedModelMap();

        SearchResult<Widget> widgetSearchResult = populateWidgetSearchResult();
        expect(service.getLimitedListOfWidgets(DEFAULT_OFFSET, DEFAULT_PAGESIZE)).andReturn(widgetSearchResult);
        replay(service);
        String adminWidgetsView = controller.viewWidgets(DEFAULT_OFFSET, model);
        verify(service);
        assertEquals(ViewNames.ADMIN_WIDGETS, adminWidgetsView);
        assertEquals(widgetSearchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertTrue(model.containsAttribute(TABS));
    }

    @Test
    public void viewAdminWidgetDetail() throws Exception {
        Model model = new ExtendedModelMap();
        Widget widget = new Widget();
        final long entityId = 123L;
        widget.setEntityId(entityId);
        widget.setTitle("My widget");

        expect(service.getWidget(entityId)).andReturn(widget);
        replay(service);
        String adminWidgetDetailView = controller.viewWidgetDetail(entityId, model);
        verify(service);

        assertEquals(ViewNames.ADMIN_WIDGETDETAIL, adminWidgetDetailView);
        assertTrue(model.containsAttribute(TABS));
        assertEquals(widget, model.asMap().get("widget"));
    }

    @Test
    public void updateWidget_valid() {
        Widget widget = new Widget(123L, "http://example.com/widget");
        widget.setTitle("Widget title");
        widget.setType("OpenSocial");
        BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        service.updateWidget(widget);
        expectLastCall();
        replay(service);
        String view = controller.updateWidgetDetail(widget, errors);
        verify(service);

        assertFalse("No errors", errors.hasErrors());
        assertEquals("redirect:123", view);

    }

    @Test
    public void updateWidget_invalid() {
        Widget widget = new Widget(123L, "http://broken/url");
        BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        String view = controller.updateWidgetDetail(widget, errors);

        assertTrue("Errors", errors.hasErrors());
        assertEquals(ViewNames.ADMIN_WIDGETDETAIL, view);

    }

    @Before
    public void setUp() throws Exception {
        controller = new WidgetController();
        service = createMock(WidgetService.class);
        controller.setWidgetService(service);
        validator = new NewWidgetValidator();
        controller.setWidgetValidator(validator);
    }


    private static SearchResult<Widget> populateWidgetSearchResult() {
        List<Widget> widgetList = new ArrayList<Widget>();
        for (int i = 0; i < DEFAULT_PAGESIZE; i++) {
            Widget widget = new Widget();
            widget.setTitle("Widget " + i);
            widgetList.add(widget);
        }
        return new SearchResult<Widget>(widgetList, 25);
    }
}
