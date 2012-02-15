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

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="page_type")
@NamedQueries({
        @NamedQuery(name = "PageType.getByCode", query="SELECT pt FROM PageType pt WHERE pt.code = :code"),
        @NamedQuery(name = "PageType.getAll", query="SELECT pt FROM PageType pt ORDER BY pt.code")
})
@Access(AccessType.FIELD)
public class PageType implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageTypeIdGenerator")
    @TableGenerator(name = "pageTypeIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_COUNT", pkColumnValue = "page_type", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic(optional=false)
    @Column(name="code", unique=true)
    private String code;

    @Basic(optional=false) @Column(name="description")
    private String description;

    public PageType() {

    }

    public PageType(Long entityId) {
        this.entityId = entityId;
    }

    public PageType(Long entityId, String code, String description) {
        this.entityId = entityId;
        this.code = code;
        this.description = description;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageType pageType = (PageType) o;

        if (!entityId.equals(pageType.entityId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId.hashCode();
    }

    @Override
    public String toString() {
        return "PageType{" +
                "entityId=" + entityId +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
