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
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * Test for {@link AdminController}
 */
public class AdminControllerTest {

    private static final String TABS = "tabs";
    AdminController controller;
    UserService userService;

    @Test
    public void adminHome() throws Exception {
        Model model = new ExtendedModelMap();
        String homeView = controller.viewDefault(model);
        assertEquals(ViewNames.ADMIN_HOME, homeView);
        assertTrue(model.containsAttribute(TABS));
    }

    @Test
    public void adminUsers() throws Exception {
        Model model = new ExtendedModelMap();
        final int offset = 0;
        final int pageSize = 10;
        SearchResult<User> searchResult = createSearchResultWithTwoUsers();

        expect(userService.getLimitedListOfUsers(offset, pageSize)).andReturn(searchResult);
        replay(userService);

        String adminUsersView = controller.viewUsers(offset,model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertTrue(model.containsAttribute(TABS));
    }

    @Test
    public void searchUsers() throws Exception {
        Model model = new ExtendedModelMap();

        final String searchTerm = "Doe";
        final int offset = 0;
        final int pageSize = 10;
        SearchResult<User> searchResult = createSearchResultWithTwoUsers();

        expect(userService.getUsersByFreeTextSearch(searchTerm, offset, pageSize)).andReturn(searchResult);
        replay(userService);

        String adminUsersView = controller.searchUsers(searchTerm, offset, model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertEquals(searchResult, model.asMap().get(ModelKeys.SEARCHRESULT));
        assertEquals(searchTerm, model.asMap().get(ModelKeys.SEARCH_TERM));
        assertTrue(model.containsAttribute(TABS));
    }


    @Test
    public void adminUserDetail() throws Exception {
        Model model = new ExtendedModelMap();
        String userid = "dummyUserId";
        String adminUserDetailView = controller.viewUserDetail(userid, model);
        assertEquals(ViewNames.ADMIN_USERDETAIL, adminUserDetailView);
        assertTrue(model.containsAttribute(TABS));
        assertEquals(userid, model.asMap().get("userid"));

    }

    @Test
    public void adminWidgets() throws Exception {
        Model model = new ExtendedModelMap();
        String adminWidgetsView = controller.viewWidgets(model);
        assertEquals(ViewNames.ADMIN_WIDGETS, adminWidgetsView);
        assertTrue(model.containsAttribute(TABS));
    }
    

    @Before
    public void setUp() throws Exception {
        controller = new AdminController();
        userService = createNiceMock(UserService.class);
        controller.setUserService(userService);
    }

    private static SearchResult<User> createSearchResultWithTwoUsers() {
        User user1 = new User(123L, "john.doe.sr");
        User user2 = new User(456L, "john.doe.jr");
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        final int totalResult = 12390;
        return new SearchResult<User>(users, totalResult);
    }
}
