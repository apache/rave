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
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts from a {@link org.apache.rave.model.User} to a {@link org.apache.rave.portal.model.JpaUser}
 */
@Component
public class JpaUserConverter implements ModelConverter<User, JpaUser> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaUser convert(User source) {
        return source instanceof JpaUser ? (JpaUser) source : createEntity(source);
    }

    @Override
    public Class<User> getSourceType() {
        return User.class;
    }

    private JpaUser createEntity(User source) {
        JpaUser converted = null;
        if (source != null) {
            converted = new JpaUser();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(User source, JpaUser converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
        converted.setUsername(source.getUsername());
        converted.setEmail(source.getEmail());
        converted.setDisplayName(source.getDisplayName());
        converted.setAdditionalName(source.getUsername());
        converted.setFamilyName(source.getFamilyName());
        converted.setGivenName(source.getGivenName());
        converted.setHonorificPrefix(source.getHonorificPrefix());
        converted.setHonorificSuffix(source.getHonorificSuffix());
        converted.setPreferredName(source.getPreferredName());
        converted.setAboutMe(source.getAboutMe());
        converted.setStatus(source.getStatus());
        converted.setAddresses(source.getAddresses());
        converted.setOrganizations(source.getOrganizations());
        converted.setProperties(source.getProperties());
        converted.setPassword(source.getPassword());
        converted.setConfirmPassword(source.getConfirmPassword());
        converted.setDefaultPageLayout(source.getDefaultPageLayout());
        converted.setDefaultPageLayoutCode(source.getDefaultPageLayoutCode());
        converted.setEnabled(source.isEnabled());
        converted.setExpired(source.isExpired());
        converted.setLocked(source.isLocked());
        converted.setOpenId(source.getOpenId());
        converted.setForgotPasswordHash(source.getForgotPasswordHash());
        converted.setForgotPasswordTime(source.getForgotPasswordTime());
        updateAuthorities(source, converted);
    }

    private void updateAuthorities(User source, JpaUser converted) {
        converted.getAuthorities().clear();
        for(GrantedAuthority grantedAuthority : source.getAuthorities()) {
            converted.addAuthority(grantedAuthority instanceof Authority ? (Authority)grantedAuthority : new AuthorityImpl(grantedAuthority));
        }
    }
}
