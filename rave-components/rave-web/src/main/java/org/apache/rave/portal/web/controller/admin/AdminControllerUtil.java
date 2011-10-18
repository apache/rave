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

import org.apache.rave.portal.web.model.NavigationItem;
import org.apache.rave.portal.web.model.NavigationMenu;
import org.springframework.ui.Model;

/**
 * Util class for the admin controllers
 */
public final class AdminControllerUtil {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private AdminControllerUtil() {
    }

    static void addNavigationMenusToModel(String selectedItem, Model model) {
        final NavigationMenu topMenu = getTopMenu();
        model.addAttribute(topMenu.getName(), topMenu);
        final NavigationMenu tabMenu = getTabMenu(selectedItem);
        model.addAttribute(tabMenu.getName(), tabMenu);
    }

    // For the time being hard coded NavigationMenu's
    private static NavigationMenu getTopMenu() {
        NavigationMenu menu = new NavigationMenu("topnav");

        NavigationItem raveHome = new NavigationItem("page.general.back", "/");
        menu.addNavigationItem(raveHome);

        NavigationItem logout = new NavigationItem("page.general.logout", "/j_spring_security_logout");
        menu.addNavigationItem(logout);

        return menu;
    }

    private static NavigationMenu getTabMenu(String selectedItem) {
        NavigationMenu menu = new NavigationMenu("tabs");

        NavigationItem home = new NavigationItem("admin.home.shorttitle", "/app/admin/");
        home.setSelected("home".equals(selectedItem));
        menu.addNavigationItem(home);

        NavigationItem users = new NavigationItem("admin.users.shorttitle", "/app/admin/users");
        users.setSelected("users".equals(selectedItem));
        menu.addNavigationItem(users);

        NavigationItem widgets = new NavigationItem("admin.widgets.shorttitle", "/app/admin/widgets");
        widgets.setSelected("widgets".equals(selectedItem));
        menu.addNavigationItem(widgets);

        return menu;
    }

}
