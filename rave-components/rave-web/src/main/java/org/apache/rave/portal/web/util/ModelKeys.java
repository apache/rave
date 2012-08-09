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
 * Defines constants representing the available keys used for inserting and retrieving objects from the {@link org.springframework.ui.Model}
 */
public class ModelKeys {
    private ModelKeys() {
    }

    public static final String PAGE = "page"; // the current page object
    public static final String PAGES = "pages"; // a list of pages available for the current user
    public static final String PAGE_USER = "pageUser"; // a single user who have shared access to a given page (other users are obtained from page.getMembers())
    public static final String PAGE_LAYOUTS = "pageLayouts"; // a list of all the possible page layouts
    public static final String ERROR_MESSAGE = "errorMessage"; // an error message to be reported to the user
    public static final String WIDGETS = "widgets"; // a list of widget objects
    public static final String WIDGET = "widget";
    public static final String REGION_WIDGET = "regionWidget"; 
    public static final String MARKETPLACE = "marketplace"; // whether there is an external widget marketplace configured
    public static final String WIDGET_STATISTICS = "widgetStatistics"; //statistics for a single widget
    public static final String WIDGETS_STATISTICS = "widgetsStatistics"; //list of statistics for a list of widgets
    public static final String CATEGORY = "category"; //category
    public static final String REFERRING_PAGE_ID = "referringPageId";
    public static final String OPENSOCIAL_ENVIRONMENT = "openSocialEnv";
    public static final String NEW_USER = "newUser"; //a new user instance created
    public static final String USER = "user"; //a new user instance created
    public static final String USER_PROFILE = "userProfile";
    public static final String SEARCH_TERM = "searchTerm";
    public static final String OFFSET = "offset";
    public static final String SEARCHRESULT = "searchResult";
    public static final String TOKENCHECK = "tokencheck";
    public static final String USER_MAP = "userMap";
    public static final String PORTAL_SETTINGS = "portalSettings";
    public static final String TAGS = "tags";
    public static final String SELECTED_TAG = "selectedTag";
    public static final String CATEGORIES = "categories";
    public static final String SELECTED_CATEGORY = "selectedCategory";
    public static final String DEFAULT_TAG_PAGE = "defaultTagPage";
    public static final String CAPTCHA_HTML = "captchaHtml";
    public static final String REDIRECT_MESSAGE = "message";
    public static final String STATIC_CONTENT_CACHE = "staticContentCache";
    public static final String BEFORE_RAVE_INIT_SCRIPT = "beforeRaveInitScript";
    public static final String AFTER_RAVE_INIT_SCRIPT = "afterRaveInitScript";

}