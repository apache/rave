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

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * A widget within a region
 */
@Entity
@Table(name = "region_widget")
@NamedQueries({
        @NamedQuery(name = RegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS,
                    query = "select rw.widget.entityId, count(distinct rw.region.page.owner) from RegionWidget rw group by rw.widget.entityId"),
        @NamedQuery(name = RegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET,
                    query = "select count(distinct rw.region.page.owner) from RegionWidget rw where rw.widget.entityId = :widgetId")
})
public class RegionWidget implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS = "RegionWidget.getDistinctUserCountForAllWidgets";
    public static final String REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET = "RegionWidget.getDistinctUserCount";

    public static final String PARAM_WIDGET_ID = "widgetId";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionWidgetIdGenerator")
    @TableGenerator(name = "regionWidgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region_widget", allocationSize = 1, initialValue = 1)
    private Long entityId;

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
    @JoinColumn(name = "region_widget_id", referencedColumnName = "entity_id")
    private List<RegionWidgetPreference> preferences;

    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

    public RegionWidget() {
    }

    public RegionWidget(Long entityId) {
        this.entityId = entityId;
    }

    public RegionWidget(Long entityId, Widget widget, Region region, int renderOrder) {
        this.entityId = entityId;
        this.widget = widget;
        this.region = region;
        this.renderOrder = renderOrder;
    }

    public RegionWidget(Long entityId, Widget widget, Region region) {
        this.entityId = entityId;
        this.widget = widget;
        this.region = region;
    }

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RegionWidget{");
        sb.append("entityId=");
        sb.append(entityId);
        sb.append(",widget=");
        sb.append(widget);
        sb.append(",regionId=");
        sb.append(region.getEntityId());
        sb.append("}");
        return sb.toString();
    }
}