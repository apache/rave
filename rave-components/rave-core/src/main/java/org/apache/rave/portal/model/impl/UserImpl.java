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

import org.apache.rave.model.Authority;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.util.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserImpl extends PersonImpl implements User, Serializable {
    private String id;
    private String password;
    private boolean expired;
    private boolean credsExpired;
    private boolean locked;
    private boolean enabled;
    private String openId;
    private String forgotPasswordHash;
    private Date forgotPasswordTime;
    private PageLayout defaultPageLayout;
    private String confirmPassword;
    private String defaultPageLayoutCode;
    private List<Authority> authorities = new ArrayList<Authority>();

    public UserImpl() {}

    public UserImpl(String id) {
        this.id = id;
    }

    public UserImpl(String userid, String username) {
        this.id = userid;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonLocked() {
        return !locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialsNonExpired() {
        return !credsExpired;
    }

    public void setCredsExpired(boolean credsExpired) {
        this.credsExpired = credsExpired;
    }
    public boolean isAccountNonExpired() {
        return !expired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getForgotPasswordHash() {
        return forgotPasswordHash;
    }

    public void setForgotPasswordHash(String forgotPasswordHash) {
        this.forgotPasswordHash = forgotPasswordHash;
    }

    public Date getForgotPasswordTime() {
        return forgotPasswordTime;
    }

    public void setForgotPasswordTime(Date forgotPasswordTime) {
        this.forgotPasswordTime = forgotPasswordTime;
    }

    public PageLayout getDefaultPageLayout() {
        return defaultPageLayout;
    }

    public void setDefaultPageLayout(PageLayout defaultPageLayout) {
        this.defaultPageLayout = defaultPageLayout;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Person toPerson() {
        PersonImpl p = new PersonImpl();
        p.setId(this.getId());
        p.setAboutMe(this.getAboutMe());
        p.setAdditionalName(this.getAdditionalName());
        p.setAddresses(this.getAddresses());
        p.setDisplayName(this.getDisplayName());
        p.setEmail(this.getEmail());
        p.setFamilyName(this.getFamilyName());
        p.setGivenName(this.getGivenName());
        p.setHonorificPrefix(this.getHonorificPrefix());
        p.setHonorificSuffix(this.getHonorificSuffix());
        p.setOrganizations(this.getOrganizations());
        p.setPreferredName(this.getPreferredName());
        p.setProperties(this.getProperties());
        p.setStatus(this.getStatus());
        p.setUsername(this.getUsername());
        return p;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDefaultPageLayoutCode() {
        return defaultPageLayoutCode;
    }

    public void setDefaultPageLayoutCode(String defaultPageLayoutCode) {
        this.defaultPageLayoutCode = defaultPageLayoutCode;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return CollectionUtils.<GrantedAuthority>toBaseTypedList(authorities);
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
       this.authorities.remove(authority);
    }

    public void setAuthorities(Collection<Authority> authorities) {
        if(this.authorities == null) {
            this.authorities = new ArrayList<Authority>();
        }
        this.authorities.clear();
        if(authorities != null) {
            this.authorities.addAll(authorities);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        if (!super.equals(o)) return false;

        UserImpl user = (UserImpl) o;

        if (credsExpired != user.credsExpired) return false;
        if (enabled != user.enabled) return false;
        if (expired != user.expired) return false;
        if (locked != user.locked) return false;
        if (authorities != null ? !authorities.equals(user.authorities) : user.authorities != null) return false;
        if (confirmPassword != null ? !confirmPassword.equals(user.confirmPassword) : user.confirmPassword != null)
            return false;
        if (defaultPageLayout != null ? !defaultPageLayout.equals(user.defaultPageLayout) : user.defaultPageLayout != null)
            return false;
        if (defaultPageLayoutCode != null ? !defaultPageLayoutCode.equals(user.defaultPageLayoutCode) : user.defaultPageLayoutCode != null)
            return false;
        if (forgotPasswordHash != null ? !forgotPasswordHash.equals(user.forgotPasswordHash) : user.forgotPasswordHash != null)
            return false;
        if (forgotPasswordTime != null ? !forgotPasswordTime.equals(user.forgotPasswordTime) : user.forgotPasswordTime != null)
            return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (openId != null ? !openId.equals(user.openId) : user.openId != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (expired ? 1 : 0);
        result = 31 * result + (credsExpired ? 1 : 0);
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (openId != null ? openId.hashCode() : 0);
        result = 31 * result + (forgotPasswordHash != null ? forgotPasswordHash.hashCode() : 0);
        result = 31 * result + (forgotPasswordTime != null ? forgotPasswordTime.hashCode() : 0);
        result = 31 * result + (defaultPageLayout != null ? defaultPageLayout.hashCode() : 0);
        result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
        result = 31 * result + (defaultPageLayoutCode != null ? defaultPageLayoutCode.hashCode() : 0);
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        return result;
    }
}
