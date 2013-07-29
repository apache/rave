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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.PortalPreference;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Bean to manage portal preferences like title suffix, logo, number of items per page etc
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "portal_preference")
@NamedQueries({
        @NamedQuery(name = JpaPortalPreference.GET_ALL, query = "SELECT pp FROM JpaPortalPreference pp"),
        @NamedQuery(name = JpaPortalPreference.COUNT_ALL, query = "SELECT count(pp) FROM JpaPortalPreference pp"),
        @NamedQuery(name = JpaPortalPreference.GET_BY_KEY,
                query = "SELECT pp FROM JpaPortalPreference pp WHERE pp.key = :" + JpaPortalPreference.PARAM_KEY)
})
public class JpaPortalPreference implements BasicEntity, Serializable, PortalPreference {

    private static final long serialVersionUID = 1L;

    public static final String GET_ALL = "PortalPreference.getAll";
    public static final String COUNT_ALL = "PortalPreference.countAll";
    public static final String GET_BY_KEY = "PortalPreference.getByKey";
    public static final String PARAM_KEY = "key";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "portalPreferenceIdGenerator")
    @TableGenerator(name = "portalPreferenceIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "portal_preference", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "preference_key", unique = true)
    private String key;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "portal_preference_values")
    @Column(name="element")
    private List<String> values = new LinkedList<String>();

    public JpaPortalPreference() {
        super();
    }

    public JpaPortalPreference(String key, String value) {
        super();
        this.key = key;
        this.values.add(value);
    }

    public JpaPortalPreference(String key, List<String> values) {
        super();
        this.key = key;
        this.values = values;
    }

    @Override
    public Long getEntityId() {
        return this.entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String getValue() {
        if (values.isEmpty()) {
            return null;
        } else if (values.size() == 1) {
            return values.get(0);
        }
        throw new NotSupportedException("Cannot return single value for a List of size " + values.size());
    }

    @Override
    public void setValue(String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        this.values = values;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaPortalPreference other = (JpaPortalPreference) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("PortalPreference");
        sb.append("{entityId=").append(entityId);
        sb.append(", key='").append(key).append('\'');
        sb.append(", values={");
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('\'').append(values.get(i)).append('\'');
            }
            sb.append('}');
        }
        sb.append('}');
        return sb.toString();
    }
}
