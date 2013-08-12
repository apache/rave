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


import org.apache.rave.model.Category;
import org.apache.rave.model.Tag;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.TagService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewWidgetValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Test class for {@link WidgetStoreController}
 */
public class WidgetStoreControllerTest {

    private static final String WIDGET_ID = "1";
    private static final String REFERRER_ID = "35";
    private WidgetStoreController controller;
    private WidgetService widgetService;
    private TagService tagService;
    private CategoryService categoryService;
    private UserImpl validUser;
    private WidgetStatistics widgetStatistics;
    private Map<String, WidgetStatistics> allWidgetStatisticsMap;

    @Before
    public void setup() {
        validUser = new UserImpl();
        validUser.setId("1");
        widgetStatistics = new WidgetStatistics();

        allWidgetStatisticsMap = new HashMap<String, WidgetStatistics>();
        allWidgetStatisticsMap.put(WIDGET_ID, widgetStatistics);

        widgetService = createMock(WidgetService.class);
        tagService = createMock(TagService.class);
        categoryService = createMock(CategoryService.class);

        UserService userService = createMock(UserService.class);
        expect(userService.getAuthenticatedUser()).andReturn(validUser);
        replay(userService);

        PortalPreferenceService preferenceService = createMock(PortalPreferenceService.class);
        expect(preferenceService.getPreference(PortalPreferenceKeys.INITIAL_WIDGET_STATUS)).andReturn(null);
        expect(preferenceService.getPreference(PortalPreferenceKeys.PAGE_SIZE)).andReturn(null);
        expect(preferenceService.getPreference(PortalPreferenceKeys.EXTERNAL_MARKETPLACE_URL)).andReturn(null);
        replay(preferenceService);

        NewWidgetValidator widgetValidator = new NewWidgetValidator(widgetService);
        controller = new WidgetStoreController(widgetService, widgetValidator, userService,
                preferenceService, tagService, categoryService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void view() {
        Model model = new ExtendedModelMap();
        List<Widget> widgets = new ArrayList<Widget>();
        SearchResult<Widget> emptyResult = new SearchResult<Widget>(widgets, 0);

        expect(widgetService.getPublishedWidgets(0, 10)).andReturn(emptyResult);
        expect(widgetService.getAllWidgetStatistics(validUser.getId())).andReturn(allWidgetStatisticsMap);
        replay(widgetService);

        String view = controller.view(model, REFERRER_ID, 0);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.STORE)));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS), is(true));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS_STATISTICS), is(true));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
        assertThat(widgets, is(sameInstance(emptyResult.getResultSet())));
        assertThat(model.containsAttribute(ModelKeys.TAGS), is(true));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));

    }

    @Test
    public void viewMine() {
        Model model = new ExtendedModelMap();
        List<Widget> widgets = new ArrayList<Widget>();
        SearchResult<Widget> emptyResult = new SearchResult<Widget>(widgets, 0);

        expect(widgetService.getWidgetsByOwner(validUser.getId(), 0, 10)).andReturn(emptyResult);
        expect(widgetService.getAllWidgetStatistics(validUser.getId())).andReturn(allWidgetStatisticsMap);
        replay(widgetService);

        String view = controller.viewMine(model, REFERRER_ID, 0);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.STORE)));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS), is(true));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS_STATISTICS), is(true));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
        assertThat(widgets, is(sameInstance(emptyResult.getResultSet())));
        assertThat(model.containsAttribute(ModelKeys.TAGS), is(true));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));

    }

    @Test
    public void viewWidget() {
        Model model = new ExtendedModelMap();
        Widget w = new WidgetImpl("1", "http://example.com/widget.xml");

        expect(widgetService.getAllWidgetStatistics(validUser.getId())).andReturn(allWidgetStatisticsMap);
        expect(tagService.getAllTagsList()).andReturn(new ArrayList<Tag>());
        expect(categoryService.getAllList()).andReturn(new ArrayList<Category>());
        expect(widgetService.getWidget(WIDGET_ID)).andReturn(w);
        expect(widgetService.getWidgetStatistics(WIDGET_ID, validUser.getId())).andReturn(widgetStatistics);
        replay(widgetService);

        String view = controller.viewWidget(model, WIDGET_ID, REFERRER_ID);

        verify(widgetService);
        assertThat(view, is(equalTo(ViewNames.WIDGET)));
        assertThat(model.containsAttribute(ModelKeys.WIDGET), is(true));
        assertThat(model.containsAttribute(ModelKeys.WIDGET_STATISTICS), is(true));
        assertThat(model.containsAttribute(ModelKeys.TAGS), is(true));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));
        assertThat(model.containsAttribute(ModelKeys.REFERRING_PAGE_ID), is(true));
        assertThat(((Widget) model.asMap().get(ModelKeys.WIDGET)), is(sameInstance(w)));
        assertNull(model.asMap().get("widgetRating"));

    }

    @Test
    public void viewCategoryResult_valid() {
        Model model = new ExtendedModelMap();
        String categoryId = "1";
        int offset = 0;
        int pageSize = 10;
        SearchResult<Widget> searchResults = new SearchResult<Widget>(new ArrayList<Widget>(),0);
        expect(widgetService.getAllWidgetStatistics(validUser.getId())).andReturn(allWidgetStatisticsMap);
        expect(tagService.getAllTagsList()).andReturn(new ArrayList<Tag>());
        expect(categoryService.getAllList()).andReturn(new ArrayList<Category>());
        expect(widgetService.getWidgetsByCategory(categoryId, offset, pageSize)).andReturn(searchResults);
        replay(widgetService, tagService, categoryService);

        String view = controller.viewCategoryResult(REFERRER_ID, categoryId, offset, model);

        verify(widgetService, tagService, categoryService);
        assertThat(view, is(equalTo(ViewNames.STORE)));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS), is(true));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS_STATISTICS), is(true));
        assertThat(model.containsAttribute(ModelKeys.TAGS), is(true));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));
        assertThat(model.containsAttribute(ModelKeys.REFERRING_PAGE_ID), is(true));
        assertThat(model.containsAttribute(ModelKeys.OFFSET), is(true));
        assertThat(model.containsAttribute(ModelKeys.SELECTED_CATEGORY), is(true));

    }

    @Test
    public void searchWidgets() {
        Model model = new ExtendedModelMap();

        String searchTerm = "gAdGet";

        int offset = 0;
        int pagesize = 10;
        int totalResults = 2;
        WidgetImpl widget = new WidgetImpl();
        widget.setId("1");
        List<Widget> widgets = new ArrayList<Widget>();
        widgets.add(widget);
        SearchResult<Widget> result = new SearchResult<Widget>(widgets, totalResults);
        result.setPageSize(pagesize);

        expect(widgetService.getPublishedWidgetsByFreeTextSearch(searchTerm, offset, pagesize)).andReturn(result);
        expect(widgetService.getAllWidgetStatistics(validUser.getId())).andReturn(allWidgetStatisticsMap);
        replay(widgetService);

        String view = controller.viewSearchResult(model, REFERRER_ID, searchTerm, offset);
        verify(widgetService);

        assertEquals(ViewNames.STORE, view);
        final Map<String, Object> modelMap = model.asMap();
        assertEquals(searchTerm, modelMap.get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(ModelKeys.WIDGETS));
        assertThat(model.containsAttribute(ModelKeys.WIDGETS_STATISTICS), is(true));
        assertEquals(offset, modelMap.get(ModelKeys.OFFSET));
        assertEquals(result, modelMap.get(ModelKeys.WIDGETS));
        assertThat(model.containsAttribute(ModelKeys.TAGS), is(true));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));

    }

    @Test
    public void startAddWidget() {
        final Model model = new ExtendedModelMap();
        final String view = controller.viewAddWidgetForm(model,REFERRER_ID);
        assertEquals("View for add widget form", ViewNames.ADD_WIDGET_FORM, view);
        final Widget widget = (Widget) model.asMap().get(ModelKeys.WIDGET);
        assertNotNull("New widget in Model", widget);
    }

    @Test
    public void doAddWidget() {
        final String widgetUrl = "http://example.com/newwidget.xml";
        final Model model = new ExtendedModelMap();
        final WidgetImpl widget = new WidgetImpl();
        widget.setId("1");
        widget.setTitle("Widget title");
        widget.setUrl(widgetUrl);
        widget.setType("OpenSocial");
        widget.setDescription("Lorem ipsum");
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        expect(widgetService.registerNewWidget(widget)).andReturn(widget);
        expect(widgetService.isRegisteredUrl(widgetUrl)).andReturn(false);
        replay(widgetService);
        String view = controller.viewAddWidgetResult(widget, errors, model,REFERRER_ID);
        verify(widgetService);

        assertEquals("redirect:/app/store/widget/" + widget.getId() +     "?referringPageId=" + REFERRER_ID, view);
        assertFalse("Valid widget data", errors.hasErrors());
    }

    @Test
    public void doAddWidget_existing() {
        final String widgetUrl = "http://example.com/existingwidget.xml";
        final Model model = new ExtendedModelMap();

        final WidgetImpl existingWidget = new WidgetImpl();
        existingWidget.setId("123");
        existingWidget.setTitle("Widget title");
        existingWidget.setUrl(widgetUrl);
        existingWidget.setType("OpenSocial");

        final WidgetImpl widget = new WidgetImpl();
        widget.setTitle("Widget title");
        widget.setUrl(widgetUrl);
        widget.setType("OpenSocial");
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");

        expect(widgetService.isRegisteredUrl(widgetUrl)).andReturn(true);
        replay(widgetService);
        String view = controller.viewAddWidgetResult(widget, errors, model,REFERRER_ID);
        verify(widgetService);

        assertEquals(ViewNames.ADD_WIDGET_FORM, view);
        assertTrue("Valid widget data", errors.hasErrors());
        assertNotNull(model.asMap().get(ModelKeys.WIDGET));
    }

    @Test
    public void doAddWidget_invalid() {
        final WidgetImpl widget = new WidgetImpl();
        widget.setTitle("Not enough data");
        final Model model = new ExtendedModelMap();
        final BindingResult errors = new BeanPropertyBindingResult(widget, "widget");
        String view = controller.viewAddWidgetResult(widget, errors, model,REFERRER_ID);
        assertTrue("Invalid widget data", errors.hasErrors());
        assertEquals(ViewNames.ADD_WIDGET_FORM, view);
        assertEquals(widget, model.asMap().get(ModelKeys.WIDGET));
    }
}
