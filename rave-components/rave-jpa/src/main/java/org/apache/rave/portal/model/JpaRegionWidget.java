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

import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A widget within a region
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "region_widget")
@NamedQueries({
        @NamedQuery(name = JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS,
                    query = "select rw.widgetId, count(distinct rw.region.page.ownerId) from JpaRegionWidget rw group by rw.widgetId"),
        @NamedQuery(name = JpaRegionWidget.REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET,
                    query = "select count(distinct rw.region.page.ownerId) from JpaRegionWidget rw where rw.widgetId = :widgetId"),
        @NamedQuery(name = JpaRegionWidget.FIND_BY_ID,
                    query = "select rw from JpaRegionWidget rw where rw.entityId = :widgetId"),
        @NamedQuery(name = JpaRegionWidget.REGION_WIDGET_GET_ALL, query = JpaRegionWidget.SELECT_R_FROM_REGION_WIDGET_R),
        @NamedQuery(name = JpaRegionWidget.REGION_WIDGET_COUNT_ALL, query = JpaRegionWidget.SELECT_COUNT_R_FROM_REGION_WIDGET_R)
})
public class JpaRegionWidget implements BasicEntity, Serializable, RegionWidget {
    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_ID = "RegionWidget.findById";
    public static final String REGION_WIDGET_GET_DISTINCT_USER_COUNT_ALL_WIDGETS = "JpaRegionWidget.getDistinctUserCountForAllWidgets";
    public static final String REGION_WIDGET_GET_DISTINCT_USER_COUNT_SINGLE_WIDGET = "JpaRegionWidget.getDistinctUserCount";
    public static final String REGION_WIDGET_GET_ALL = "JpaRegionWidget.getAll";
    public static final String REGION_WIDGET_COUNT_ALL = "JpaRegionWidget.countAll";


    public static final String PARAM_WIDGET_ID = "widgetId";

    static final String SELECT_R_FROM_REGION_WIDGET_R = "SELECT r FROM JpaRegionWidget r order by r.entityId";
    static final String SELECT_COUNT_R_FROM_REGION_WIDGET_R = "SELECT count(r) FROM JpaRegionWidget r ";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionWidgetIdGenerator")
    @TableGenerator(name = "regionWidgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region_widget", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "widget_id")
    private Long widgetId;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private JpaRegion region;

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
    private List<JpaRegionWidgetPreference> preferences;

    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

    @Basic(optional = false)
    @Column(name = "hide_chrome")
    private boolean hideChrome;

    public JpaRegionWidget() {
    }

    public JpaRegionWidget(Long entityId) {
        this.entityId = entityId;
    }

    public JpaRegionWidget(Long entityId, Long widgetId, JpaRegion region, int renderOrder) {
        this.entityId = entityId;
        this.widgetId = widgetId;
        this.region = region;
        this.renderOrder = renderOrder;
    }

    public JpaRegionWidget(Long entityId, Long widgetId, JpaRegion region) {
        this.entityId = entityId;
        this.widgetId = widgetId;
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

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    /**
     * Gets the object that represents the metadata about the widget
     *
     * @return valid widget
     */
    @Override
    public String getWidgetId() {
        return widgetId.toString();
    }

    @Override
    public void setWidgetId(String widgetId) {
        this.widgetId = Long.parseLong(widgetId);
    }

    /**
     * Gets the associated region
     *
     * @return the region
     */
    @Override
    @JsonBackReference
    public Region getRegion() {
        return region;
    }

    @Override
    public void setRegion(Region region) {
        this.region = JpaConverter.getInstance().convert(region, Region.class);
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
    @Override
    public String getRenderPosition() {
        return renderPosition;
    }

    @Override
    public void setRenderPosition(String renderPosition) {
        this.renderPosition = renderPosition;
    }

    /**
     * Gets the order relative to other gadgets in the region
     *
     * @return the order of the gadget
     */
    @Override
    public int getRenderOrder() {
        return renderOrder;
    }

    @Override
    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    /**
     * Gets whether or not to render the gadget in collapsed mode
     *
     * @return true if render collapsed; false otherwise
     */
    @Override
    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * Gets the collection of user defined preferences for this RegionWidget.
     *
     * @return The user defined preferences for this RegionWidget.
     */
    @Override
    public List<RegionWidgetPreference> getPreferences() {
        return ConvertingListProxyFactory.createProxyList(RegionWidgetPreference.class, preferences);
    }

    @Override
    public void setPreferences(List<RegionWidgetPreference> preferences) {
        if(this.preferences == null) {
            this.preferences = new ArrayList<JpaRegionWidgetPreference>();
        }
        this.getPreferences().clear();
        if(preferences != null) {
            this.getPreferences().addAll(preferences);
        }
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaRegionWidget other = (JpaRegionWidget) obj;
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
        sb.append("JpaRegionWidget{");
        sb.append("entityId=");
        sb.append(entityId);
        if (widgetId != null) {
            sb.append(",widget=");
            sb.append(widgetId);
        } else {
            sb.append(", Widget Null");
        }
        if (region != null) {
            sb.append(",regionId=");
            sb.append(region.getEntityId());
        } else {
            sb.append(", Region Null");
        }
        sb.append("}");
        return sb.toString();
    }
}
