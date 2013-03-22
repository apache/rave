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
import org.apache.rave.portal.model.JpaPersonProperty;
import org.apache.rave.model.PersonProperty;
import org.springframework.stereotype.Component;

/**
 * Converts an Address to a JpaAddress
 */
@Component
public class JpaPersonPropertyConverter implements ModelConverter<PersonProperty, JpaPersonProperty> {

    @Override
    public Class<PersonProperty> getSourceType() {
        return PersonProperty.class;
    }

    @Override
    public JpaPersonProperty convert(PersonProperty source) {
        return source instanceof JpaPersonProperty ? (JpaPersonProperty) source : createEntity(source);
    }

    private JpaPersonProperty createEntity(PersonProperty source) {
        JpaPersonProperty converted = null;
        if (source != null) {
            converted  = new JpaPersonProperty();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PersonProperty source, JpaPersonProperty converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setQualifier(source.getQualifier());
        converted.setPrimary(source.getPrimary());
        converted.setType(source.getType());
        converted.setValue(source.getValue());
        converted.setExtendedValue(source.getExtendedValue());
    }
}
