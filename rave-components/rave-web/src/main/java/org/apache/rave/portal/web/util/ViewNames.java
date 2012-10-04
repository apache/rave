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

package org.apache.rave.portal.web.util;

/**
 * Defines constants representing the available view names in the system
 */
public final class ViewNames {
    private ViewNames() {}
    private static final String USER_PREFIX = "templates.user.";
    private static final String ADMIN_PREFIX = "templates.admin.";
    private static final String PARTIAL_PREFIX = "templates.partial.";
    
    public static final String PAGE = USER_PREFIX + "page";
    public static final String MOBILE_HOME = USER_PREFIX + "mobile_home";
    public static final String STORE = USER_PREFIX + "store";
    public static final String WIDGET = USER_PREFIX + "widget";
    public static final String WIDGET_MARKETPLACE = WIDGET + ".marketplace";
    public static final String REGION_WIDGET = PARTIAL_PREFIX + "regionwidget";
    public static final String ADD_WIDGET_FORM = USER_PREFIX + "addwidget";
    public static final String NEW_ACCOUNT = USER_PREFIX + "newaccount";
    public static final String USER_PROFILE = USER_PREFIX + "userProfile";
    public static final String PERSON_PROFILE = USER_PREFIX + "personProfile";
    public static final String USER_NOT_FOUND = USER_PREFIX + "usernotfound";
    public static final String ADD_WIDGET_MARKETPLACE = ADD_WIDGET_FORM + ".marketplace";
    public static final String ADD_WIDGET_W3C = ADD_WIDGET_FORM + ".w3c";

    public static final String ADMIN_HOME = ADMIN_PREFIX + "home";
    public static final String ADMIN_NEW_ACCOUNT = ADMIN_PREFIX + "newaccount";
    public static final String ADMIN_PREFERENCES = ADMIN_PREFIX + "preferences";
    public static final String ADMIN_PREFERENCE_DETAIL = ADMIN_PREFIX + "preferencedetail";
    public static final String ADMIN_USERS = ADMIN_PREFIX + "users";
    public static final String ADMIN_USERDETAIL = ADMIN_PREFIX + "userdetail";
    public static final String ADMIN_WIDGETS = ADMIN_PREFIX + "widgets";
    public static final String ADMIN_WIDGETDETAIL = ADMIN_PREFIX + "widgetdetail";
    public static final String ADMIN_CATEGORIES = ADMIN_PREFIX + "categories";
    public static final String ADMIN_CATEGORY_DETAIL = ADMIN_PREFIX + "categoryDetail";

    // password reminder / changing
    public static final String NEW_PASSWORD_REQUEST = USER_PREFIX + "newpassword";
    public static final String USERNAME_REQUEST = USER_PREFIX + "retrieveusername";
    public static final String PASSWORD_CHANGE = USER_PREFIX + "changepassword";



    public static final String REDIRECT = "redirect:/";

    public static final String LOGIN_PAGE = "login";
    public static final String CREATE_ACCOUNT_PAGE = "app/openidregister";
    public static final String REDIRECT_LOGIN = REDIRECT +LOGIN_PAGE;

    public static final String POSTS_TAG_PAGE = "postsTagPage";
    public static final String ABOUT_TAG_PAGE = "aboutTagPage";

    public static final String REDIRECT_NEW_PASSWORD = REDIRECT + "app/newpassword";
    public static final String REDIRECT_RETRIEVE_USERNAME = REDIRECT + "app/retrieveusername";


    public static String getPageView(String layoutName) {
        return new StringBuilder(PAGE).append('.').append(layoutName).toString();
    }

    public static String getPersonPageView(String layoutName) {
        return new StringBuilder(PERSON_PROFILE).append('.').append(layoutName).toString();
    }
}
