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

import org.apache.rave.model.Address;

/**
 */
public class AddressImpl implements Address {

    private String country;
    private Float latitude;
    private Float longitude;
    private String locality;
    private String postalCode;
    private String region;
    private String streetAddress;
    private String qualifier;
    private String formatted;
    private Boolean primary;

    public AddressImpl() {  }

    public AddressImpl(String street) {
        this.streetAddress = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
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
        if (!(o instanceof AddressImpl)) return false;

        AddressImpl address = (AddressImpl) o;

        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (formatted != null ? !formatted.equals(address.formatted) : address.formatted != null) return false;
        if (latitude != null ? !latitude.equals(address.latitude) : address.latitude != null) return false;
        if (locality != null ? !locality.equals(address.locality) : address.locality != null) return false;
        if (longitude != null ? !longitude.equals(address.longitude) : address.longitude != null) return false;
        if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null) return false;
        if (primary != null ? !primary.equals(address.primary) : address.primary != null) return false;
        if (qualifier != null ? !qualifier.equals(address.qualifier) : address.qualifier != null) return false;
        if (region != null ? !region.equals(address.region) : address.region != null) return false;
        if (streetAddress != null ? !streetAddress.equals(address.streetAddress) : address.streetAddress != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (locality != null ? locality.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (streetAddress != null ? streetAddress.hashCode() : 0);
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = 31 * result + (formatted != null ? formatted.hashCode() : 0);
        result = 31 * result + (primary != null ? primary.hashCode() : 0);
        return result;
    }
}
