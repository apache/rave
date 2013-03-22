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

import org.apache.rave.model.User;
import org.apache.rave.model.Authority;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class AuthorityImpl implements Authority {

    private String authority;
    private Collection<User> users;
    private boolean defaultForNewUser;

    public AuthorityImpl(){
        this.users = new ArrayList<User>();
    }

    public AuthorityImpl(String grantedAuthority) {
        this();
        this.authority = grantedAuthority;
    }

    public AuthorityImpl(GrantedAuthority grantedAuthority) {
        this(grantedAuthority.getAuthority());
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public Collection<User> getUsers() {
        return users;
    }

    @Override
    public void addUser(User user) {
        this.users.add(user);
    }

    @Override
    public void removeUser(User user) {
        this.users.remove(user);
    }

    @Override
    public boolean isDefaultForNewUser() {
        return defaultForNewUser;
    }

    @Override
    public void setDefaultForNewUser(boolean defaultForNewUser) {
        this.defaultForNewUser = defaultForNewUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorityImpl)) return false;

        AuthorityImpl authority1 = (AuthorityImpl) o;

        if (defaultForNewUser != authority1.defaultForNewUser) return false;
        if (authority != null ? !authority.equals(authority1.authority) : authority1.authority != null) return false;
        if (users != null ? !users.equals(authority1.users) : authority1.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authority != null ? authority.hashCode() : 0;
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (defaultForNewUser ? 1 : 0);
        return result;
    }
}
