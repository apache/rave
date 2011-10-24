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

package org.apache.rave.portal.web.controller;


import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetRatingService;
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
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;


/**
 * Test class for {@link WidgetStoreController}
 */
public class WidgetStoreControllerTest {

    private static final long WIDGET_ID = 1L;
    private static final long REFERRER_ID = 35L;
    private WidgetStoreController controller;
    private WidgetService widgetService;
    private WidgetRatingService widgetRatingService;
    private UserService userService;

    @Before
    public void setup() {
        widgetService = createNiceMock(WidgetService.class);
        userService = createNiceMock(UserService.class);
        NewWidgetValidator widgetValidator = new NewWidgetValidator();
        widgetRatingService = createMock(WidgetRatingService.class);
        controller = new WidgetStoreController(widgetService, widgetValidator, widgetRatingService, userService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void view() {
        Model model = new ExtendedModelMap();
        List<Widget> widgets = new ArrayList<Widget>();
        SearchResult<Widget> emptyResult = new SearchResult<Widget>(widgets, 0);

        expect(widgetService.getPublishedWidgets(0, 10)).andReturn(emptyResult);
        replay(widgetService);

        String view = controller.view(model, REFERRER_ID, 0);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.STORE)));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS), is(true));
        assertThat((Long) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
        assertThat(widgets, is(sameInstance(emptyResult.getResultSet())));
    }

    @Test
    public void viewWidget() {
        Model model = new ExtendedModelMap();
        Widget w = new Widget(1L, "http://example.com/widget.xml");
        User user = new User(1L, "john.doe");

        expect(widgetService.getWidget(WIDGET_ID)).andReturn(w);
        expect(userService.getAuthenticatedUser()).andReturn(user);
        expect(widgetRatingService.getByWidgetIdAndUserId(1L, 1L)).andReturn(null);
        replay(widgetService, userService, widgetRatingService);

        String view = controller.viewWidget(model, WIDGET_ID, REFERRER_ID);

        verify(widgetService, userService, widgetRatingService);
        assertThat(view, is(equalTo(ViewNames.WIDGET)));
        assertThat(model.containsAttribute(ModelKeys.WIDGET), is(true));
        assertThat(((Widget) model.asMap().get(ModelKeys.WIDGET)), is(sameInstance(w)));
        assertNull(model.asMap().get("widgetRating"));
    }

    @Test
    public void searchWidgets() {
        Model model = new ExtendedModelMap();

        String searchTerm = "gAdGet";

        int offset = 0;
        int pagesize = 10;
        int totalResults = 2;
        Widget widget = new Widget();
        widget.setEntityId(1L);
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);
        SearchResult<Widget> result = new SearchResult<Widget>(widgets, totalResults);
        result.setPageSize(pagesize);

        expect(widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm, offset, pagesize))
                .andReturn(result);
        replay(widgetService);

        String view = controller.viewSearchResult(model, REFERRER_ID, searchTerm, offset);
        verify(widgetService);

        assertEquals(ViewNames.STORE, view);
        final Map<String, Object> modelMap = model.asMap();
        assertEquals(searchTerm, modelMap.get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(ModelKeys.WIDGETS));
        assertEquals(offset, modelMap.get(ModelKeys.OFFSET));
        assertEquals(result, modelMap.get(ModelKeys.WIDGETS));
    }

    @Test
    public void startAddWidget() {
        final Model model = new ExtendedModelMap();
        final String view = controller.viewAddWidgetForm(model);
        assertEquals("View for add widget form", ViewNames.ADD_WIDGET_FORM, view);
        final Widget widget = (Widget) model.asMap().get(ModelKeys.WIDGET);
        assertNotNull("New widget in Model", widget);
    }

    @Test
    public void doAddWidget() {
        final String widgetUrl = "http://example.com/newwidget.xml";
        final Model model = new ExtendedModelMap();
        final Widget widget = new Widget();
        widget.setTitle("Widget title");
        widget.setUrl(widgetUrl);
        widget.setType("OpenSocial");
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        expect(widgetService.registerNewWidget(widget)).andReturn(widget);
        replay(widgetService);
        String view = controller.viewAddWidgetResult(widget, errors, model);
        verify(widgetService);

        assertEquals(ViewNames.WIDGET, view);
        assertFalse("Valid widget data", errors.hasErrors());
        final Widget fromModel = (Widget) model.asMap().get(ModelKeys.WIDGET);
        assertEquals(widget, fromModel);
        assertEquals("New widget has state preview", WidgetStatus.PREVIEW, fromModel.getWidgetStatus());
    }

    @Test
    public void doAddWidget_existing() {
        final String widgetUrl = "http://example.com/existingwidget.xml";
        final Model model = new ExtendedModelMap();
        final Widget widget = new Widget();
        widget.setTitle("Widget title");
        widget.setUrl(widgetUrl);
        widget.setType("OpenSocial");
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        expect(widgetService.registerNewWidget(widget)).andReturn(null);
        replay(widgetService);
        String view = controller.viewAddWidgetResult(widget, errors, model);
        verify(widgetService);

        assertEquals(ViewNames.ADD_WIDGET_FORM, view);
        assertTrue("Valid widget data", errors.hasErrors());
        assertNotNull(model.asMap().get(ModelKeys.WIDGET));
    }

    @Test
    public void doAddWidget_invalid() {
        final Widget widget = new Widget();
        widget.setTitle("Not enough data");
        final Model model = new ExtendedModelMap();
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");
        String view = controller.viewAddWidgetResult(widget, errors, model);
        assertTrue("Invalid widget data", errors.hasErrors());
        assertEquals(ViewNames.ADD_WIDGET_FORM, view);
        assertEquals(widget, model.asMap().get(ModelKeys.WIDGET));
    }
}
