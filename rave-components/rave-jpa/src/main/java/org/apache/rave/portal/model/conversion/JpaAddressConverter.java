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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.model.Address;
import org.apache.rave.portal.model.JpaAddress;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts an Address to a JpaAddress
 */
@Component
public class JpaAddressConverter implements ModelConverter<Address, JpaAddress> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Address> getSourceType() {
        return Address.class;
    }

    @Override
    public JpaAddress convert(Address source) {
        return source instanceof JpaAddress ? (JpaAddress) source : createEntity(source);
    }

    private JpaAddress createEntity(Address source) {
        JpaAddress converted = null;
        if (source != null) {
            TypedQuery<JpaAddress> query = manager.createNamedQuery(JpaAddress.FIND_BY_STREET_CITY_COUNTRY, JpaAddress.class);
            query.setParameter(JpaAddress.STREET_PARAM, source.getStreetAddress());
            query.setParameter(JpaAddress.CITY_PARAM, source.getLocality());
            query.setParameter(JpaAddress.COUNTRY_PARAM, source.getCountry());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaAddress();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(Address source, JpaAddress converted) {
        converted.setCountry(source.getCountry());
        converted.setLatitude(source.getLatitude());
        converted.setLongitude(source.getLongitude());
        converted.setLocality(source.getLocality());
        converted.setPostalCode(source.getPostalCode());
        converted.setRegion(source.getRegion());
        converted.setStreetAddress(source.getStreetAddress());
        converted.setQualifier(source.getQualifier());
        converted.setFormatted(source.getFormatted());
        converted.setPrimary(source.getPrimary());
    }
}
