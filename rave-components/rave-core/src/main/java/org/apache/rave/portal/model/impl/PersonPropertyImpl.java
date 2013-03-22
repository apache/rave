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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.PersonProperty;

/** **/
public class PersonPropertyImpl implements PersonProperty {

    private String id;
    private String type;
    private String value;
    private String qualifier;
    private String extendedValue;
    private Boolean primary;

    public PersonPropertyImpl() {}

    public PersonPropertyImpl(String id) {
        this.id = id;
    }

    public PersonPropertyImpl(String id, String type, String value, String extendedValue, String qualifier, Boolean primary) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.qualifier = qualifier;
        this.primary = primary;
        this.extendedValue = extendedValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getExtendedValue() {
        return extendedValue;
    }

    public void setExtendedValue(String extendedValue) {
        this.extendedValue = extendedValue;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonPropertyImpl)) return false;

        PersonPropertyImpl that = (PersonPropertyImpl) o;

        if (extendedValue != null ? !extendedValue.equals(that.extendedValue) : that.extendedValue != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (primary != null ? !primary.equals(that.primary) : that.primary != null) return false;
        if (qualifier != null ? !qualifier.equals(that.qualifier) : that.qualifier != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = 31 * result + (extendedValue != null ? extendedValue.hashCode() : 0);
        result = 31 * result + (primary != null ? primary.hashCode() : 0);
        return result;
    }
}
