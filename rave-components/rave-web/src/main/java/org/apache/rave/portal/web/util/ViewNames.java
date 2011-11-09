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
public class ViewNames {
    private ViewNames() {}
    private static final String USER_PREFIX = "templates.user.";

    public static final String HOME = USER_PREFIX + "home";
    public static final String MOBILE_HOME = USER_PREFIX + "mobile_home";    
    public static final String STORE = "store";
    public static final String WIDGET = "widget";
    public static final String ADD_WIDGET_FORM = "addwidget";
    public static final String NEW_ACCOUNT = "newaccount";
    public static final String USER_PROFILE = "userProfile";

    public static final String ADMIN_HOME = "admin/home";
    public static final String ADMIN_USERS = "admin/users";
    public static final String ADMIN_USERDETAIL = "admin/userdetail";
    public static final String ADMIN_WIDGETS = "admin/widgets";
    public static final String ADMIN_WIDGETDETAIL = "admin/widgetdetail";

    public static final String REDIRECT = "redirect:/";
}
