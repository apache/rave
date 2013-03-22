/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.util.data;

import org.apache.rave.model.*;

import java.util.List;

public class ModelWrapper {
    private List<PageLayout> pageLayouts;
    private List<User> users;
    private List<Group> groups;
    private List<Widget> widgets;
    private List<Page> pages;
    private List<Authority> authorities;
    private List<PortalPreference> portalPreferences;
    private List<Category> categories;
    private List<PageTemplate> pageTemplates;
    private List<ActivityStreamsEntry> activities;

    public List<PageLayout> getPageLayouts() {
        return pageLayouts;
    }

    public void setPageLayouts(List<PageLayout> pageLayouts) {
        this.pageLayouts = pageLayouts;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<PortalPreference> getPortalPreferences() {
        return portalPreferences;
    }

    public void setPortalPreferences(List<PortalPreference> portalPreferences) {
        this.portalPreferences = portalPreferences;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<PageTemplate> getPageTemplates() {
        return pageTemplates;
    }

    public void setPageTemplates(List<PageTemplate> pageTemplates) {
        this.pageTemplates = pageTemplates;
    }

    public List<ActivityStreamsEntry> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityStreamsEntry> activities) {
        this.activities = activities;
    }
}