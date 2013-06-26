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

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

public class RegionImpl implements Region {
    private String id;
    private Page page;
    private Boolean locked = false;
    private Integer renderOrder = 0;
    private List<RegionWidget> regionWidgets;

    public RegionImpl() {

    }

    public RegionImpl(String id, Page page, int renderOrder) {
        this.id = id;
        this.page = page;
        this.renderOrder = renderOrder;
    }

    public RegionImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(Page page) {
        this.page = page;
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
    public List<RegionWidget> getRegionWidgets() {
        return regionWidgets;
    }

    @Override
    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        this.regionWidgets = regionWidgets;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionImpl)) return false;

        RegionImpl region = (RegionImpl) o;

        if (id != null ? !id.equals(region.id) : region.id != null) return false;
        if (locked != null ? !locked.equals(region.locked) : region.locked != null)
            return false;
        if (renderOrder != null ? !renderOrder.equals(region.renderOrder) : region.renderOrder != null) return false;
        if (regionWidgets != null ? !regionWidgets.equals(region.regionWidgets) : region.regionWidgets != null)
            return false;
        if (page != null ? !page.equals(region.page) : region.page != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (page != null ? page.hashCode() : 0);
        result = 31 * result + (regionWidgets != null ? regionWidgets.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (renderOrder != null ? renderOrder.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegionImpl{" +
                "id=" + id +
                ", page=" + ((page == null) ? null : page.getId()) +
                ", locked=" + locked +
                ", renderOrder=" + renderOrder +
                '}';
    }
}
