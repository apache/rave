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

package org.apache.rave.portal.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean to build up a navigation menu
 */
public class NavigationItem {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_NAME_PARAM = "name_param";
    private static final String PARAM_URL = "url";

    private Map<String, String> parameters;
    private List<NavigationItem> childNavigationItems;
    private boolean selected;
    private boolean expanded;

    public NavigationItem() {
        this(null, null, null);
    }

    public NavigationItem(String name, String nameParam, String url) {
        super();

        this.parameters = new HashMap<String, String>();
        this.childNavigationItems = new ArrayList<NavigationItem>();

        this.setName(name);
        this.setUrl(url);
        this.setNameParam(nameParam);
    }

    /**
     * @return name of the navigation item, can be a translation key
     */
    public String getName() {
        return parameters.get(PARAM_NAME);
    }

    public void setName(String name) {
        this.parameters.put(PARAM_NAME, name);
    }

    /**
     * @return parameter value
     */
    public String getNameParam() {
        return parameters.get(PARAM_NAME_PARAM);
    }

    /**
     * Sets the parameter value used to plug into the name field
     *
     * @param nameParam
     */
    public void setNameParam(String nameParam) {
        this.parameters.put(PARAM_NAME_PARAM, nameParam);
    }

    /**
     * @return url the navigation item should link to
     */
    public String getUrl() {
        return parameters.get(PARAM_URL);
    }

    public void setUrl(String url) {
        this.parameters.put(PARAM_URL, url);
    }


    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Flag that defines if this navigation item is equal to the requested page
     *
     * @return {@literal true} if this navigation item is pointing to the current page
     */
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Flag that defines if a descendant navigation item is equal to the current request
     *
     * @return {@literal true} if this navigation item is an ancestor of the current requested page
     */
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * @return a List of Navigation items that are a level deeper (recursive)
     */
    public List<NavigationItem> getChildNavigationItems() {
        return childNavigationItems;
    }

    public void setChildNavigationItems(List<NavigationItem> childNavigationItems) {
        this.childNavigationItems = childNavigationItems;
    }

    /**
     * Adds a single NavigationItem to the List of child navigation items
     *
     * @param navigationItem child node/tree of the current NavigationItem
     */
    public void addChildNavigationItem(NavigationItem navigationItem) {
        this.childNavigationItems.add(navigationItem);
    }

    public boolean isHasChildren() {
        return !this.childNavigationItems.isEmpty();
    }

}
