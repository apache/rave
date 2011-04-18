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

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;

public class HomeControllerTest {
    private UserService userService;
    private PageService pageService;
    private HomeController homeController;

    @Before
    public void setup() {
        userService = createNiceMock(UserService.class);
        pageService = createNiceMock(PageService.class);
        homeController = new HomeController(pageService, userService);
    }

    @Test
    public void getHome() {
        final String VALID_USER_ID = "jcian";
        final String VALID_VIEW = "home";
        final Model MODEL = new ExtendedModelMap();
        final List<Page> VALID_PAGES = new ArrayList<Page>();

        expect(userService.getAuthenticatedUser()).andReturn(new Person(VALID_USER_ID));
        replay(userService);

        expect(pageService.getAllPages(VALID_USER_ID)).andReturn(VALID_PAGES);
        replay(pageService);

        String results = homeController.getHome(MODEL);
        assertThat(results, CoreMatchers.equalTo(VALID_VIEW));
        assertThat((List<Page>) MODEL.asMap().get(ModelKeys.PAGES), CoreMatchers.sameInstance(VALID_PAGES));
    }
}