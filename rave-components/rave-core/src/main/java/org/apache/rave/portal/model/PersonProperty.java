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

/**
 * A generic extension model for the {@link Person} that allows implementers to
 * add fields to the Person not initially envisioned
 */
@Entity
@Table(name = "person_property")
public class PersonProperty implements BasicEntity {

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personPropertyIdGenerator")
    @TableGenerator(name = "personPropertyIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "person_property", allocationSize = 1, initialValue = 1)
    private Long entityId;

    /**
     * The property type (IM, PhoneNumber, etc)
     */
    @Basic
    @Column(name = "type")
    private String type;

    /**
     * The value of the field
     */
    @Basic
    @Column(name = "value")
    private String value;

    /**
     * The distinguishing qualifier (Home, Work, etc)
     */
    @Basic
    @Column(name = "qualifier")
    private String qualifier;

    /**
     * Extended information related to the value
     */
    @Basic
    @Column(name = "extended_value")
    private String extendedValue;

    /**
     * Determines whether or not the property is the designated primary for the type
     */
    @Basic
    @Column(name = "primary_value")
    private Boolean primary;

    public PersonProperty() {
    }

    public PersonProperty(Long entityId, String type, String value, String extendedValue, String qualifier, Boolean primary) {
        this.entityId = entityId;
        this.type = type;
        this.value = value;
        this.qualifier = qualifier;
        this.primary = primary;
        this.extendedValue = extendedValue;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getExtendedValue() {
        return extendedValue;
    }

    public void setExtendedValue(String extendedValue) {
        this.extendedValue = extendedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonProperty that = (PersonProperty) o;

        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}