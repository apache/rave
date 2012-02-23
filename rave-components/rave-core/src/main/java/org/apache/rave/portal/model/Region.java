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
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * A region of a page, which can contain widget instances {@link RegionWidget}
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="region")
@Access(AccessType.FIELD)
public class Region implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;
     
    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionIdGenerator")
    @TableGenerator(name = "regionIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @Basic
    @Column(name = "render_order")
    private int renderOrder;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderOrder")
    @JoinColumn(name = "region_id")
    private List<RegionWidget> regionWidgets;

    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

    public Region() {
    }

    public Region(Long entityId) {
        this.entityId = entityId;
    }

    public Region(Long entityId, Page page, int renderOrder) {
        this.entityId = entityId;
        this.page = page;
        this.renderOrder = renderOrder;
    }
    
    @SuppressWarnings("unused")
    @XmlElement(name="widget")
    /**
     * Only used for XML serialization, omitting regionwidget
     */
    private List<Widget> getWidgets(){
        ArrayList<Widget> widgets = new ArrayList<Widget>();
        for (RegionWidget rw: regionWidgets){
            widgets.add(rw.getWidget());
        }
        return widgets;
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
     * Gets the associated page
     *
     * @return the associated page
     */
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
    
    /**
     * Gets the order relative to regions on the page
     * 
     * @return the order of the region
     */
    public int getRenderOrder() {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    /**
     * Gets the ordered list of widget instances for the region
     *
     * @return Valid list
     */
    @JsonManagedReference
    public List<RegionWidget> getRegionWidgets() {
        return regionWidgets;
    }

    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        this.regionWidgets = regionWidgets;
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
        final Region other = (Region) obj;
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
        return "Region{" + "entityId=" + entityId + ", pageId=" + ((page == null) ? null : page.getEntityId()) + ", regionWidgets=" + regionWidgets + '}';
    }
}
