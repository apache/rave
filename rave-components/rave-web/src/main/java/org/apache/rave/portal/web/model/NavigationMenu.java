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
import java.util.List;

/**
 * Wrapper around {@link NavigationItem}'s
 */
public class NavigationMenu {

    private String name;

    public NavigationMenu(String name) {
        super();
        this.name = name;
        this.navigationItems = new ArrayList<NavigationItem>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<NavigationItem> navigationItems;


    /**
     * @return List of {@link NavigationItem}'s for this menu, can be empty, not null
     */
    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public void setNavigationItems(List<NavigationItem> navigationItems) {
        this.navigationItems = navigationItems;
    }

    /**
     * Adds a single {@link NavigationItem} to this menu
     *
     * @param navigationItem to add
     */
    public void addNavigationItem(NavigationItem navigationItem) {
        this.navigationItems.add(navigationItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NavigationMenu that = (NavigationMenu) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (navigationItems != null ? !navigationItems.equals(that.navigationItems) : that.navigationItems != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (navigationItems != null ? navigationItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("NavigationMenu");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
