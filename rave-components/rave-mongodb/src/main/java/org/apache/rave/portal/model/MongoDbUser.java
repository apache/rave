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

package org.apache.rave.portal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.rave.model.Authority;
import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.List;

/**
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MongoDbUser extends UserImpl {

    private List<String> authorityCodes;
    private List<MongoDbPersonAssociation> friends;

    @XmlTransient
    @JsonIgnore
    private PageLayoutRepository pageLayoutRepository;

    public MongoDbUser(String id) {
        super(id);
    }

    public MongoDbUser() {
    }

    public List<String> getAuthorityCodes() {
        return authorityCodes;
    }

    public PageLayoutRepository getPageLayoutRepository() {
        return pageLayoutRepository;
    }

    public void setAuthorityCodes(List<String> authorityCodes) {
        this.authorityCodes = authorityCodes;
    }

    public List<MongoDbPersonAssociation> getFriends() {
        return friends;
    }

    public void setFriends(List<MongoDbPersonAssociation> friends) {
        this.friends = friends;
    }

    @Override
    public void setAuthorities(Collection<Authority> authorities) {
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                addAuthorityCode(authority);
            }
        } else {
            authorityCodes.clear();
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        ensureAuthorityCodes();
        Collection<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        for (String code : authorityCodes) {
            grantedAuthorities.add(new SimpleGrantedAuthority(code));
        }
        return grantedAuthorities;
    }

    @Override
    public void addAuthority(Authority authority) {
        addAuthorityCode(authority);
    }

    @Override
    public void removeAuthority(Authority authority) {
        ensureAuthorityCodes();
        if (authorityCodes.contains(authority.getAuthority())) {
            authorityCodes.remove(authority.getAuthority());
        }
    }

    @Override
    public PageLayout getDefaultPageLayout() {
        PageLayout layout = super.getDefaultPageLayout();
        if (layout == null) {
            layout = pageLayoutRepository.getByPageLayoutCode(super.getDefaultPageLayoutCode());
            super.setDefaultPageLayout(layout);
        }
        return layout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoDbUser)) return false;

        MongoDbUser that = (MongoDbUser) o;

        if (this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    private void ensureAuthorityCodes() {
        if (authorityCodes == null) {
            authorityCodes = Lists.newArrayList();
        }
    }

    private void addAuthorityCode(GrantedAuthority authority) {
        ensureAuthorityCodes();
        if (!authorityCodes.contains(authority.getAuthority())) {
            authorityCodes.add(authority.getAuthority());
        }
    }
}
