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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A region of a page, which can contain widget instances {@link RegionWidget}
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="region")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = JpaRegion.FIND_BY_ENTITY_ID, query="select r from JpaRegion r where r.entityId = :entity_id"),
        @NamedQuery(name = JpaRegion.REGION_GET_ALL, query = JpaRegion.SELECT_R_FROM_REGION_R),
        @NamedQuery(name = JpaRegion.REGION_COUNT_ALL, query = JpaRegion.SELECT_COUNT_R_FROM_REGION_R)
})
public class JpaRegion implements BasicEntity, Serializable, Region {
    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_ENTITY_ID = "Region.findByEntityId";
    public static final String REGION_GET_ALL = "Region.getAll";
    public static final String REGION_COUNT_ALL = "Region.countAll";
    public static final String ENTITY_ID_PARAM = "entity_id";

    static final String SELECT_R_FROM_REGION_R = "SELECT r FROM JpaRegion r order by r.entityId";
    static final String SELECT_COUNT_R_FROM_REGION_R = "SELECT count(r) FROM JpaRegion r ";

    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionIdGenerator")
    @TableGenerator(name = "regionIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private JpaPage page;

    @Basic
    @Column(name = "render_order")
    private int renderOrder;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderOrder")
    @JoinColumn(name = "region_id")
    private List<JpaRegionWidget> regionWidgets;

    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

    public JpaRegion() {
    }

    public JpaRegion(Long entityId) {
        this.entityId = entityId;
    }

    public JpaRegion(Long entityId, JpaPage page, int renderOrder) {
        this.entityId = entityId;
        this.page = page;
        this.renderOrder = renderOrder;
    }

    @SuppressWarnings("unused")
    @XmlElement(name="widget")
    /**
     * Only used for XML serialization, omitting regionwidget
     */
//    private List<Widget> getWidgets(){
//        ArrayList<Widget> widgets = new ArrayList<Widget>();
//        for (RegionWidget rw: regionWidgets){
//            widgets.add(rw.getWidget());
//        }
//        return widgets;
//    }

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
     * Gets the associated page
     *
     * @return the associated page
     */
    @Override
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(Page page) {
        this.page = JpaConverter.getInstance().convert(page, Page.class);
    }

    /**
     * Gets the order relative to regions on the page
     *
     * @return the order of the region
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
     * Gets the ordered list of widget instances for the region
     *
     * @return Valid list
     */
    @Override
    @JsonManagedReference
    public List<RegionWidget> getRegionWidgets() {
        return ConvertingListProxyFactory.createProxyList(RegionWidget.class, regionWidgets);
    }

    @Override
    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        if(this.regionWidgets == null) {
            this.regionWidgets = new ArrayList<JpaRegionWidget>();
        }
        this.getRegionWidgets().clear();
        if(regionWidgets != null) {
            for(RegionWidget widget : regionWidgets) {
                widget.setRegion(this);
                this.getRegionWidgets().add(widget);
            }
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaRegion other = (JpaRegion) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JpaRegion{" + "entityId=" + entityId + ", pageId=" + ((page == null) ? null : page.getEntityId()) + ", regionWidgets=" + regionWidgets + '}';
    }
}
