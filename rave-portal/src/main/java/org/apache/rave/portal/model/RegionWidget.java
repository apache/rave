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
package org.apache.rave.portal.model;

import org.apache.rave.persistence.BasicEntity;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;
import java.util.List;

/**
 * A widget within a region
 */
@Entity
@Table(name = "region_widget")
public class RegionWidget implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionWidgetIdGenerator")
    @TableGenerator(name = "regionWidgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region_widget", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "widget_id")
    private Widget widget;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Basic
    @Column(name = "render_position")
    private String renderPosition;

    @Basic
    @Column(name = "render_order")
    private int renderOrder;

    @Basic
    @Column(name = "collapsed")
    private boolean collapsed;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "region_widget_id", referencedColumnName = "id")
    private List<RegionWidgetPreference> preferences;

    public RegionWidget() {
    }

    public RegionWidget(Long id) {
        this.id = id;
    }

    public RegionWidget(Long id, Widget widget, Region region, int renderOrder) {
        this.id = id;
        this.widget = widget;
        this.region = region;
        this.renderOrder = renderOrder;
    }

    public RegionWidget(Long id, Widget widget, Region region) {
        this.id = id;
        this.widget = widget;
        this.region = region;
    }

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the object that represents the metadata about the widget
     *
     * @return valid widget
     */
    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    /**
     * Gets the associated region
     *
     * @return the region
     */
    @JsonBackReference
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Gets the render position of this widget.  When the widget instance is being displayed on a "desktop" type view
     * (single region where the user can drag and drop widgets to a particular X and Y coordinate within that region)
     * this value might be an X and Y coordinate of the upper left hand corner of the widget.  It will be up to the
     * rendering engine (based on the type of PageLayout associated with the Page this widget is rendering on) to
     * determine how to use the value from this field.
     *
     * @return The RegionWidgets position within the Region
     */
    public String getRenderPosition() {
        return renderPosition;
    }

    public void setRenderPosition(String renderPosition) {
        this.renderPosition = renderPosition;
    }

    /**
     * Gets the order relative to other gadgets in the region
     *
     * @return the order of the gadget
     */
    public int getRenderOrder() {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    /**
     * Gets whether or not to render the gadget in collapsed mode
     *
     * @return true if render collapsed; false otherwise
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * Gets the collection of user defined preferences for this RegionWidget.
     *
     * @return The user defined preferences for this RegionWidget.
     */
    public List<RegionWidgetPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<RegionWidgetPreference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegionWidget other = (RegionWidget) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RegionWidget{");
        sb.append("id=");
        sb.append(id);
        sb.append(",widget=");
        sb.append(widget);
        sb.append(",region=");
        sb.append(region);
        sb.append("}");
        return sb.toString();
    }
}