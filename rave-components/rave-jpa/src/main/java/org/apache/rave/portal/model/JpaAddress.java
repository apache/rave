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

import org.apache.rave.model.Address;

import javax.persistence.*;

/**
 */
@Entity
@Access(AccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery(name = JpaAddress.FIND_BY_STREET_CITY_COUNTRY, query = "select a from JpaAddress a where a.streetAddress=:street and a.locality=:city and a.country=:country")
})
@Table(name = "address")
public class JpaAddress implements Address {

    public static final String FIND_BY_STREET_CITY_COUNTRY = "findByStreetCityCountry";
    public static final String STREET_PARAM = "street";
    public static final String CITY_PARAM = "city";
    public static final String COUNTRY_PARAM = "country";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "addressIdGenerator")
    @TableGenerator(name = "addressIdGenerator", table = "RAVE_SHINDIG_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "address", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "country", length = 255)
    private String country;

    @Basic
    @Column(name = "latitude")
    private Float latitude;

    @Basic
    @Column(name = "longitude")
    private Float longitude;

    @Basic
    @Column(name = "locality", length = 255)
    private String locality;

    @Basic
    @Column(name = "postal_code", length = 255)
    private String postalCode;

    @Basic
    @Column(name = "region", length = 255)
    private String region;

    @Basic
    @Column(name = "street_address", length = 255)
    private String streetAddress;

    @Basic
    @Column(name = "qualifier", length = 255)
    private String qualifier;

    @Basic
    @Column(name = "formatted", length = 255)
    private String formatted;

    @Basic
    @Column(name = "primary_address")
    private Boolean primary;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Float getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public Float getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getLocality() {
        return locality;
    }

    @Override
    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getStreetAddress() {
        return streetAddress;
    }

    @Override
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String getFormatted() {
        return formatted;
    }

    @Override
    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public Boolean getPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}
