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

import org.apache.rave.model.PageLayout;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents an organization of regions within a page that is supported by the rendering engine
 */
@Entity
@Access(AccessType.FIELD)
@Table(name="page_layout")
@NamedQueries({
    @NamedQuery(name= JpaPageLayout.PAGELAYOUT_GET_BY_LAYOUT_CODE, query = "select pl from JpaPageLayout pl where pl.code = :code"),
    @NamedQuery(name= JpaPageLayout.PAGELAYOUT_GET_ALL, query="select pl from JpaPageLayout pl order by pl.renderSequence"),
    @NamedQuery(name= JpaPageLayout.PAGELAYOUT_GET_ALL_USER_SELECTABLE, query="select pl from JpaPageLayout pl where pl.userSelectable = true order by pl.renderSequence")
})

public class JpaPageLayout implements BasicEntity, Serializable, PageLayout {
    private static final long serialVersionUID = 1L;

    // static string identifiers for JPA queries
    public static final String PAGELAYOUT_GET_BY_LAYOUT_CODE = "PageLayout.getByLayoutCode";
    public static final String PAGELAYOUT_GET_ALL = "PageLayout.getAll";
    public static final String PAGELAYOUT_GET_ALL_USER_SELECTABLE = "PageLayout.getAllUserSelectable";
    public static final String CODE_PARAM = "code";
    
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

    @Basic
    @Column(name = "user_selectable")
    private boolean userSelectable;

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
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Long getNumberOfRegions() {
        return numberOfRegions;
    }

    @Override
    public void setNumberOfRegions(Long numberOfRegions) {
        this.numberOfRegions = numberOfRegions;
    }

    @Override
    public Long getRenderSequence() {
        return renderSequence;
    }

    @Override
    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    @Override
    public boolean isUserSelectable() {
        return userSelectable;
    }

    @Override
    public void setUserSelectable(boolean userSelectable) {
        this.userSelectable = userSelectable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaPageLayout other = (JpaPageLayout) obj;
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