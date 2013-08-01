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

import org.apache.rave.model.Category;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.controller.util.CategoryEditor;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.UpdateWidgetValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for {@link WidgetController}
 */
public class WidgetControllerTest {

    private static final String TABS = "tabs";
    private static final String REFERRER_ID = "35";
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_PAGESIZE = AdminControllerUtil.DEFAULT_PAGE_SIZE;

    private WidgetController controller;
    private WidgetService service;
    private UpdateWidgetValidator widgetValidator;
    private PortalPreferenceService preferenceService;
    private CategoryService categoryService;
    private String validToken;
    private List<Category> categories;
    private WebDataBinder webDataBinder;
    private CategoryEditor categoryEditor;

    @Before
    public void setUp() throws Exception {
        controller = new WidgetController();
        service = createMock(WidgetService.class);
        controller.setWidgetService(service);
        widgetValidator = new UpdateWidgetValidator(service);
        controller.setWidgetValidator(widgetValidator);
        validToken = AdminControllerUtil.generateSessionToken();
        preferenceService = createMock(PortalPreferenceService.class);
        controller.setPreferenceService(preferenceService);
        categoryService = createMock(CategoryService.class);
        controller.setCategoryService(categoryService);

        categories = new ArrayList<Category>();
        categories.add(new CategoryImpl());
        categories.add(new CategoryImpl());

        webDataBinder = createMock(WebDataBinder.class);
        categoryEditor = new CategoryEditor(categoryService);
        controller.setCategoryEditor(categoryEditor);
    }

    @Test
    public void adminWidgets() throws Exception {
        Model model = new ExtendedModelMap();
        PortalPreferenceService preferenceService = createMock(PortalPreferenceService.class);
        expect(preferenceService.getPreference(PortalPreferenceKeys.PAGE_SIZE)).andReturn(null);
        replay(preferenceService);

        SearchResult<Widget> widgetSearchResult = populateWidgetSearchResult();
        expect(service.getLimitedList(DEFAULT_OFFSET, DEFAULT_PAGESIZE)).andReturn(widgetSearchResult);
        replay(service);


        String adminWidgetsView = controller.viewWidgets(DEFAULT_OFFSET, null,REFERRER_ID, model);
        verify(service);
        assertEquals(ViewNames.ADMIN_WIDGETS, adminWidgetsView);
        assertEquals(widgetSearchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertTrue(model.containsAttribute(TABS));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void searchWidgets() throws Exception {
        Model model = new ExtendedModelMap();
        String searchTerm = "widget";
        String type = "OpenSocial";
        String status = "published";
        PortalPreferenceService preferenceService = createMock(PortalPreferenceService.class);
        expect(preferenceService.getPreference(PortalPreferenceKeys.PAGE_SIZE)).andReturn(null);
        replay(preferenceService);

        SearchResult<Widget> widgetSearchResult = populateWidgetSearchResult();
        expect(service.getWidgetsBySearchCriteria(searchTerm, type, status, DEFAULT_OFFSET, DEFAULT_PAGESIZE)).andReturn(widgetSearchResult);
        replay(service);


        String searchView = controller.searchWidgets(searchTerm, type, status, DEFAULT_OFFSET,REFERRER_ID, model);
        verify(service);

        assertEquals(ViewNames.ADMIN_WIDGETS, searchView);
        assertEquals(searchTerm, model.asMap().get(ModelKeys.SEARCH_TERM));
        assertEquals(type, model.asMap().get("selectedWidgetType"));
        assertEquals(status, model.asMap().get("selectedWidgetStatus"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void viewAdminWidgetDetail() throws Exception {
        Model model = new ExtendedModelMap();
        WidgetImpl widget = new WidgetImpl();
        final String entityId = "123";
        widget.setId(entityId);
        widget.setTitle("My widget");

        expect(service.getWidget(entityId)).andReturn(widget);
        expect(categoryService.getAllList()).andReturn(categories);
        replay(service, categoryService);
        String adminWidgetDetailView = controller.viewWidgetDetail(entityId,REFERRER_ID, model);
        verify(service, categoryService);

        assertEquals(ViewNames.ADMIN_WIDGETDETAIL, adminWidgetDetailView);
        assertTrue(model.containsAttribute(TABS));
        assertEquals(widget, model.asMap().get("widget"));
        assertThat(model.containsAttribute(ModelKeys.CATEGORIES), is(true));
        assertThat((List<Category>) model.asMap().get(ModelKeys.CATEGORIES), is(categories));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void updateWidget_valid() {
        final String widgetUrl = "http://example.com/widget";
        WidgetImpl widget = new WidgetImpl("123", widgetUrl);
        widget.setTitle("WidgetImpl title");
        widget.setType("OpenSocial");
        widget.setDescription("Lorem ipsum");
        BindingResult errors = new BeanPropertyBindingResult(widget, "widget");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        ModelMap modelMap = new ExtendedModelMap();

        expect(service.getWidgetByUrl(widgetUrl)).andReturn(widget);
        service.updateWidget(widget);
        sessionStatus.setComplete();
        expectLastCall();
        replay(service, sessionStatus);
        String view = controller.updateWidgetDetail(widget, errors, validToken, validToken,REFERRER_ID, modelMap, sessionStatus);
        verify(service, sessionStatus);

        assertFalse("No errors", errors.hasErrors());
        assertEquals("redirect:/app/admin/widgets?action=update&referringPageId=" +REFERRER_ID, view);



    }

    @Test(expected = SecurityException.class)
    public void updateWidget_wrongToken() {
        WidgetImpl widget = new WidgetImpl();
        BindingResult errors = new BeanPropertyBindingResult(widget, "widget");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        ModelMap modelMap = new ExtendedModelMap();

        sessionStatus.setComplete();
        expectLastCall();
        replay(sessionStatus);

        String otherToken = AdminControllerUtil.generateSessionToken();

        controller.updateWidgetDetail(widget, errors, "sessionToken", otherToken,REFERRER_ID, modelMap, sessionStatus);

        verify(sessionStatus);
        assertFalse("Can't come here", true);
    }

    @Test
    public void updateWidget_invalid() {
        WidgetImpl widget = new WidgetImpl("123", "http://broken/url");
        BindingResult errors = new BeanPropertyBindingResult(widget, "widget");
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        ModelMap modelMap = new ExtendedModelMap();

        String view = controller.updateWidgetDetail(widget, errors, validToken, validToken,REFERRER_ID, modelMap, sessionStatus);

        assertTrue("Errors", errors.hasErrors());
        assertEquals(ViewNames.ADMIN_WIDGETDETAIL, view);

    }

    @Test
    public void initBinder() {
        webDataBinder.setDisallowedFields("entityId");
        expectLastCall();
        webDataBinder.registerCustomEditor(Category.class, categoryEditor);
        expectLastCall();
        replay(webDataBinder);
        controller.initBinder(webDataBinder);
        verify(webDataBinder);
    }


    private static SearchResult<Widget> populateWidgetSearchResult() {
        List<Widget> widgetList = new ArrayList<Widget>();
        for (int i = 0; i < DEFAULT_PAGESIZE; i++) {
            WidgetImpl widget = new WidgetImpl();
            widget.setTitle("WidgetImpl " + i);
            widgetList.add(widget);
        }
        return new SearchResult<Widget>(widgetList, 25);
    }
}
