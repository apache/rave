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
package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.model.Widget;

import java.util.List;

public class RegionWidgetImpl implements RegionWidget {
    private Long id;
    private Widget widget;
    private Region region;
    private String renderPosition;
    private int renderOrder;
    private boolean collapsed;
    private List<RegionWidgetPreference> preferences;
    private boolean locked;
    private boolean hideChrome;

    public RegionWidgetImpl() {

    }

    public RegionWidgetImpl(Long id) {
        this.id = id;
    }

    public RegionWidgetImpl(Long id, Widget widget, Region region) {
        this.id = id;
        this.widget = widget;
        this.region = region;
    }

    public RegionWidgetImpl(Long id, Widget widget, Region region, int renderOrder) {
        this.id = id;
        this.widget = widget;
        this.region = region;
        this.renderOrder = renderOrder;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Widget getWidget() {
        return widget;
    }

    @Override
    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String getRenderPosition() {
        return renderPosition;
    }

    @Override
    public void setRenderPosition(String renderPosition) {
        this.renderPosition = renderPosition;
    }

    @Override
    public int getRenderOrder() {
        return renderOrder;
    }

    @Override
    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    @Override
    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    @Override
    public List<RegionWidgetPreference> getPreferences() {
        return preferences;
    }

    @Override
    public void setPreferences(List<RegionWidgetPreference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isHideChrome() {
        return hideChrome;
    }

    @Override
    public void setHideChrome(boolean hideChrome) {
        this.hideChrome = hideChrome;
    }
}
