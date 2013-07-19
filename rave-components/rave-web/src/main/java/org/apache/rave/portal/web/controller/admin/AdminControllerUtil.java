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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rave.portal.web.model.NavigationItem;
import org.apache.rave.portal.web.model.NavigationMenu;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Util class for the admin controllers
 */
public final class AdminControllerUtil {

    public static final int DEFAULT_PAGE_SIZE = 10;
    private static final int TOKEN_LENGTH = 256;

    private AdminControllerUtil() {
    }

    static String generateSessionToken() {
        return RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
    }

    public static void checkTokens(String sessionToken, String token, SessionStatus status) {
        if (StringUtils.length(sessionToken) != TOKEN_LENGTH || !(sessionToken.equals(token))) {
            status.setComplete();
            throw new SecurityException("Token does not match");
        }
    }

    static boolean isDeleteOrUpdate(String action) {
        return "update".equals(action) || "delete".equals(action);
    }

    static boolean isCreateDeleteOrUpdate(String action){
        return "create".equals(action) || isDeleteOrUpdate(action);
    }

    static void addNavigationMenusToModel(String selectedItem, Model model,  String referringPageId) {
        final NavigationMenu topMenu = getTopMenu(referringPageId);
        model.addAttribute(topMenu.getName(), topMenu);
        final NavigationMenu tabMenu = getTabMenu(selectedItem, referringPageId);
        model.addAttribute(tabMenu.getName(), tabMenu);
    }

    // For the time being hard coded NavigationMenu's
    private static NavigationMenu getTopMenu(String referringPageId) {
        NavigationMenu menu = new NavigationMenu("topnav");

        NavigationItem logout = new NavigationItem("page.general.logout", null, "/j_spring_security_logout");
        menu.addNavigationItem(logout);

        NavigationItem raveHome = new NavigationItem();
        raveHome.setName("page.general.back");

        if (referringPageId != null && !referringPageId.isEmpty()) {
            raveHome.setUrl("/app/page/view/" + referringPageId);
        } else {
            raveHome.setUrl("/");
        }
        menu.addNavigationItem(raveHome);

        return menu;
    }

    private static NavigationMenu getTabMenu(String selectedItem, String referringPageId) {
        NavigationMenu menu = new NavigationMenu("tabs");

        NavigationItem home = new NavigationItem("admin.home.shorttitle", null, null);
        home.setSelected("home".equals(selectedItem));


        NavigationItem users = new NavigationItem("admin.users.shorttitle", null, null);
        users.setSelected("users".equals(selectedItem));


        NavigationItem widgets = new NavigationItem("admin.widgets.shorttitle", null, null);
        widgets.setSelected("widgets".equals(selectedItem));


        NavigationItem preferences = new NavigationItem("admin.preferences.shorttitle", null, null);
        preferences.setSelected("preferences".equals(selectedItem));


        NavigationItem categories = new NavigationItem("admin.category.shortTitle", null, null);
        categories.setSelected("categories".equals(selectedItem));

        // set url of nav items with or without the referring page id
        if (referringPageId != null && !referringPageId.isEmpty()) {
            home.setUrl("/app/admin?referringPageId=" + referringPageId);
            users.setUrl("/app/admin/users?referringPageId=" + referringPageId);
            widgets.setUrl("/app/admin/widgets?referringPageId=" + referringPageId);
            preferences.setUrl("/app/admin/preferences?referringPageId=" + referringPageId);
            categories.setUrl("/app/admin/categories?referringPageId=" + referringPageId);
        } else {
            home.setUrl("/app/admin");
            users.setUrl("/app/admin/users");
            widgets.setUrl("/app/admin/widgets");
            preferences.setUrl("/app/admin/preferences");
            categories.setUrl("/app/admin/categories");
        }

        // add nav items to menu
        menu.addNavigationItem(home);
        menu.addNavigationItem(users);
        menu.addNavigationItem(widgets);
        menu.addNavigationItem(preferences);
        menu.addNavigationItem(categories);

        return menu;
    }

}
