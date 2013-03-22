/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.MongoDbPersonAssociation;
import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.PersonPropertyImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbUserConverter implements HydratingModelConverter<User, MongoDbUser> {
    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Override
    public void hydrate(MongoDbUser dehydrated) {
        if(dehydrated == null) {
            return;
        }
        if(dehydrated.getFriends() == null) {
            dehydrated.setFriends(Lists.<MongoDbPersonAssociation>newArrayList());
        }
        if(dehydrated.getAuthorityCodes() == null) {
            dehydrated.setAuthorityCodes(Lists.<String>newArrayList());
        }
        dehydrated.setPageLayoutRepository(pageLayoutRepository);
    }

    @Override
    public Class<User> getSourceType() {
        return User.class;
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    public PageLayoutRepository getPageLayoutRepository() {
        return pageLayoutRepository;
    }

    @Override
    public MongoDbUser convert(User source) {
        MongoDbUser user = source instanceof MongoDbUser ? (MongoDbUser)source : new MongoDbUser();
        List<String> authorityCodes = Lists.newArrayList();
        for(GrantedAuthority authority : source.getAuthorities()) {
            authorityCodes.add(authority.getAuthority());
        }
        user.setAuthorityCodes(authorityCodes);
        updateProperties(source, user);
        return user;
    }

    private void updateProperties(User source, MongoDbUser converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setUsername(source.getUsername());
        converted.setEmail(source.getEmail());
        converted.setDisplayName(source.getDisplayName());
        converted.setAdditionalName(source.getAdditionalName());
        converted.setFamilyName(source.getFamilyName());
        converted.setGivenName(source.getGivenName());
        converted.setHonorificPrefix(source.getHonorificPrefix());
        converted.setHonorificSuffix(source.getHonorificSuffix());
        converted.setPreferredName(source.getPreferredName());
        converted.setAboutMe(source.getAboutMe());
        converted.setStatus(source.getStatus());
        converted.setAddresses(source.getAddresses());
        converted.setOrganizations(source.getOrganizations());
        converted.setProperties(convert(source.getProperties()));
        converted.setPassword(source.getPassword());
        converted.setConfirmPassword(source.getConfirmPassword());
        converted.setDefaultPageLayoutCode(getPageLayoutCode(source));
        converted.setEnabled(source.isEnabled());
        converted.setExpired(source.isExpired());
        converted.setLocked(source.isLocked());
        converted.setOpenId(source.getOpenId());
        converted.setForgotPasswordHash(source.getForgotPasswordHash());
        converted.setForgotPasswordTime(source.getForgotPasswordTime());
        converted.setPageLayoutRepository(null);
    }

    private List<PersonProperty> convert(List<PersonProperty> sources) {
        if(sources != null) {
            List<PersonProperty> converted = Lists.newArrayList();
            for(PersonProperty source : sources) {
                converted.add(convert(source));
            }
            return converted;
        }
        return null;
    }

    private PersonProperty convert(PersonProperty source) {
        PersonProperty converted = new PersonPropertyImpl();
        converted.setType(source.getType());
        converted.setValue(source.getValue());
        converted.setExtendedValue(source.getExtendedValue());
        converted.setQualifier(source.getQualifier());
        converted.setPrimary(source.getPrimary());
        return converted;
    }

    private String getPageLayoutCode(User source) {
        String code = null;
        if (source.getDefaultPageLayout() == null) {
            if(source.getDefaultPageLayoutCode() != null) {
                code = source.getDefaultPageLayoutCode();
            }
        } else {
            code = source.getDefaultPageLayout().getCode();
            source.setDefaultPageLayout(null);
        }
        return code;
    }
}
