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

package org.apache.rave.opensocial.model;

import org.apache.rave.persistence.BasicEntity;
import org.apache.shindig.social.opensocial.model.ListField;

import javax.lang.model.element.TypeElement;
import javax.persistence.*;

/**
 * Defines a property of a person
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="property_type")
@DiscriminatorValue("basic")
@Table(name = "person_property")
@SequenceGenerator(name="personPropertyIdSeq", sequenceName = "person_property_id_seq")
public class PersonProperty implements BasicEntity, ListField {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personPropertyIdSeq")
    private Long id;

    @Basic
    @Column(name = "field_type")
    private String fieldType;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "value")
    private String value;

    @Basic
    @Column(name = "primary_value")
    private Boolean primary;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Boolean getPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonProperty that = (PersonProperty) o;

        if (fieldType != null ? !fieldType.equals(that.fieldType) : that.fieldType != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (primary != null ? !primary.equals(that.primary) : that.primary != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fieldType != null ? fieldType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (primary != null ? primary.hashCode() : 0);
        return result;
    }
}
