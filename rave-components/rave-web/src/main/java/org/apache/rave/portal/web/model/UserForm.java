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
package org.apache.rave.portal.web.model;

import org.apache.rave.portal.model.impl.AuthorityImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Form backing object similar to User with concrete object references
 */
public class UserForm {

    private String id;
    private Collection<AuthorityImpl> authorities;
    private String password;
    private String username;
    private String confirmPassword;
    private String forgotPasswordHash;
    private String email;
    private String openId;
    private String defaultPageLayoutCode;
    private String givenName;
    private String familyName;
    private String displayName;
    private String status;
    private String aboutMe;
    private boolean locked;
    private boolean credsExpired;
    private boolean expired;
    private boolean enabled;

    public UserForm(String userid, String username) {
        this();
        this.id = userid;
        this.username = username;
    }

    public UserForm() {
        this.authorities = new ArrayList<AuthorityImpl>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<AuthorityImpl> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<AuthorityImpl> authorities) {
        this.authorities = authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getForgotPasswordHash() {
        return forgotPasswordHash;
    }

    public void setForgotPasswordHash(String forgotPasswordHash) {
        this.forgotPasswordHash = forgotPasswordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDefaultPageLayoutCode() {
        return defaultPageLayoutCode;
    }

    public void setDefaultPageLayoutCode(String defaultPageLayoutCode) {
        this.defaultPageLayoutCode = defaultPageLayoutCode;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredsExpired() {
        return credsExpired;
    }

    public void setCredsExpired(boolean credsExpired) {
        this.credsExpired = credsExpired;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserForm)) return false;

        UserForm userForm = (UserForm) o;

        if (credsExpired != userForm.credsExpired) return false;
        if (enabled != userForm.enabled) return false;
        if (expired != userForm.expired) return false;
        if (locked != userForm.locked) return false;
        if (aboutMe != null ? !aboutMe.equals(userForm.aboutMe) : userForm.aboutMe != null) return false;
        if (authorities != null ? !authorities.equals(userForm.authorities) : userForm.authorities != null)
            return false;
        if (confirmPassword != null ? !confirmPassword.equals(userForm.confirmPassword) : userForm.confirmPassword != null)
            return false;
        if (defaultPageLayoutCode != null ? !defaultPageLayoutCode.equals(userForm.defaultPageLayoutCode) : userForm.defaultPageLayoutCode != null)
            return false;
        if (displayName != null ? !displayName.equals(userForm.displayName) : userForm.displayName != null)
            return false;
        if (email != null ? !email.equals(userForm.email) : userForm.email != null) return false;
        if (familyName != null ? !familyName.equals(userForm.familyName) : userForm.familyName != null) return false;
        if (forgotPasswordHash != null ? !forgotPasswordHash.equals(userForm.forgotPasswordHash) : userForm.forgotPasswordHash != null)
            return false;
        if (givenName != null ? !givenName.equals(userForm.givenName) : userForm.givenName != null) return false;
        if (id != null ? !id.equals(userForm.id) : userForm.id != null) return false;
        if (openId != null ? !openId.equals(userForm.openId) : userForm.openId != null) return false;
        if (password != null ? !password.equals(userForm.password) : userForm.password != null) return false;
        if (status != null ? !status.equals(userForm.status) : userForm.status != null) return false;
        if (username != null ? !username.equals(userForm.username) : userForm.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (confirmPassword != null ? confirmPassword.hashCode() : 0);
        result = 31 * result + (forgotPasswordHash != null ? forgotPasswordHash.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (openId != null ? openId.hashCode() : 0);
        result = 31 * result + (defaultPageLayoutCode != null ? defaultPageLayoutCode.hashCode() : 0);
        result = 31 * result + (givenName != null ? givenName.hashCode() : 0);
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (aboutMe != null ? aboutMe.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + (credsExpired ? 1 : 0);
        result = 31 * result + (expired ? 1 : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }
}
