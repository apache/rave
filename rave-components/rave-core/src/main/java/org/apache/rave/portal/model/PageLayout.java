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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;

/**
 * Represents an organization of regions within a page that is supported by the rendering engine
 */
@Entity
@Table(name="page_layout")
@NamedQueries({
    @NamedQuery(name=PageLayout.PAGELAYOUT_GET_BY_LAYOUT_CODE, query = "select pl from PageLayout pl where pl.code = :code"),
    @NamedQuery(name=PageLayout.PAGELAYOUT_GET_ALL, query="select pl from PageLayout pl order by pl.renderSequence")
})

public class PageLayout implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    // static string identifiers for JPA queries
    public static final String PAGELAYOUT_GET_BY_LAYOUT_CODE = "PageLayout.getByLayoutCode";
    public static final String PAGELAYOUT_GET_ALL = "PageLayout.getAll";
    
    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageLayoutIdGenerator")
    @TableGenerator(name = "pageLayoutIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_layout", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic @Column(name="code", unique = true)
    private String code;

    @Basic @Column(name="number_of_regions")
    private Long numberOfRegions;

    @Basic(optional=false) @Column(name="render_sequence")
    private Long renderSequence;

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
     * Gets the code used by the rendering engine to identify the page layout
     *
     * @return Valid code known by rendering engine
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the number of regions supported by this page layout
     *
     * @return Valid number of regions > 0
     */
    public Long getNumberOfRegions() {
        return numberOfRegions;
    }

    public void setNumberOfRegions(Long numberOfRegions) {
        this.numberOfRegions = numberOfRegions;
    }

    public Long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PageLayout other = (PageLayout) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PageLayout{" + "entityId=" + entityId + ", code=" + code + ", numberOfRegions=" + numberOfRegions + '}';
    }
}