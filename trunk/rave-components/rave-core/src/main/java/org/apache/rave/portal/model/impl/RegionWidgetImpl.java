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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;

import java.util.List;

public class RegionWidgetImpl implements RegionWidget {
    private String id;
    private String widgetId;
    private String renderPosition;
    private Integer renderOrder = 0;
    private Boolean collapsed = false;
    private List<RegionWidgetPreference> preferences;
    private Boolean locked = false;
    private Boolean hideChrome = false;

    @JsonBackReference
    private Region region;

    public RegionWidgetImpl() {

    }

    public RegionWidgetImpl(String id) {
        this.id = id;
    }

    public RegionWidgetImpl(String id, String widgetId, Region region) {
        this.id = id;
        this.widgetId = widgetId;
        this.region = region;
    }

    public RegionWidgetImpl(String id, String widgetId, Region region, int renderOrder) {
        this.id = id;
        this.widgetId = widgetId;
        this.region = region;
        this.renderOrder = renderOrder;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getWidgetId() {
        return widgetId;
    }

    @Override
    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionWidgetImpl)) return false;

        RegionWidgetImpl rw = (RegionWidgetImpl) o;

        if (id != null ? !id.equals(rw.id) : rw.id != null) return false;
        if (locked != null ? !locked.equals(rw.locked) : rw.locked != null)
            return false;
        if (renderOrder != null ? !renderOrder.equals(rw.renderOrder) : rw.renderOrder != null) return false;
        if (widgetId != null ? !widgetId.equals(rw.widgetId) : rw.widgetId != null)
            return false;
        if (region != null ? !region.equals(rw.region) : rw.region != null) return false;
        if (hideChrome != null ? !hideChrome.equals(rw.hideChrome) : rw.hideChrome != null) return false;
        if (preferences != null ? !preferences.equals(rw.preferences) : rw.preferences != null) return false;
        if (collapsed != null ? !collapsed.equals(rw.collapsed) : rw.collapsed != null) return false;
        if (renderPosition != null ? !renderPosition.equals(rw.renderPosition) : rw.renderPosition != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (widgetId != null ? widgetId.hashCode() : 0);
        result = 31 * result + (preferences != null ? preferences.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (renderOrder != null ? renderOrder.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        result = 31 * result + (renderPosition != null ? renderPosition.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (hideChrome != null ? hideChrome.hashCode() : 0);
        result = 31 * result + (collapsed != null ? collapsed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegionWidgetImpl{" +
                "id=" + id +
                ", widget=" + ((widgetId == null) ? null : widgetId)  +
                ", region=" + ((region == null) ? null : region.getId()) +
                ", renderPosition='" + renderPosition + '\'' +
                ", renderOrder=" + renderOrder +
                ", collapsed=" + collapsed +
                ", locked=" + locked +
                ", hideChrome=" + hideChrome +
                '}';
    }
}
