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
import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.JpaAuthority;
import org.apache.rave.model.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaAuthorityConverter implements ModelConverter<Authority, JpaAuthority> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Authority> getSourceType() {
        return Authority.class;
    }

    @Override
    public JpaAuthority convert(Authority source) {
        return source instanceof JpaAuthority ? (JpaAuthority) source : createEntity(source);
    }

    private JpaAuthority createEntity(Authority source) {
        JpaAuthority converted = null;
        if (source != null) {
            TypedQuery<JpaAuthority> query = manager.createNamedQuery(JpaAuthority.GET_BY_AUTHORITY_NAME, JpaAuthority.class);
            query.setParameter(JpaAuthority.PARAM_AUTHORITY_NAME, source.getAuthority());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaAuthority();
                updateProperties(source, converted);
            }
        }
        return converted;
    }

    private void updateProperties(Authority source, JpaAuthority converted) {
        converted.setDefaultForNewUser(source.isDefaultForNewUser());
        converted.setAuthority(source.getAuthority());
        for(User user : source.getUsers()) {
            converted.addUser(user);
        }
    }
}
