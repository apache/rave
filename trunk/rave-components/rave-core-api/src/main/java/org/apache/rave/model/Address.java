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
package org.apache.rave.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 */
@XmlTransient
public interface Address {
    String getCountry();

    void setCountry(String country);

    Float getLatitude();

    void setLatitude(Float latitude);

    Float getLongitude();

    void setLongitude(Float longitude);

    String getLocality();

    void setLocality(String locality);

    String getPostalCode();

    void setPostalCode(String postalCode);

    String getRegion();

    void setRegion(String region);

    String getStreetAddress();

    void setStreetAddress(String streetAddress);

    String getQualifier();

    void setQualifier(String qualifier);

    String getFormatted();

    void setFormatted(String formatted);

    Boolean getPrimary();

    void setPrimary(Boolean primary);
}
