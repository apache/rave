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
package org.apache.rave.portal.model;

import org.apache.rave.persistence.BasicEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * {@inheritDoc}
 * <p/>
 * A user of the system
 */
@Entity
// user can be a restricted table name
@Table(name = "raveuser")
@NamedQueries({
        @NamedQuery(name = User.USER_GET_BY_USERNAME, query = "select u from User u where u.username = :username"),
        @NamedQuery(name = User.USER_GET_BY_USER_EMAIL, query = "select u from User u where u.email = :email"),
        @NamedQuery(name = User.USER_GET_ALL, query = "select u from User u order by u.username asc"),
        @NamedQuery(name = User.USER_COUNT_ALL, query = "select count(u) from User u"),
        @NamedQuery(name = User.USER_FIND_BY_USERNAME_OR_EMAIL, query = "select u from User u " +
                "where lower(u.username) like :searchTerm or lower(u.email) like :searchTerm order by u.username asc"),
        @NamedQuery(name = User.USER_COUNT_FIND_BY_USERNAME_OR_EMAIL, query = "select count(u) from User u " +
                "where lower(u.username) like :searchTerm or lower(u.email) like :searchTerm")

})
public class User implements UserDetails, BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String USER_GET_BY_USERNAME = "User.getByUsername";
    public static final String USER_GET_BY_USER_EMAIL = "User.getByUserEmail";
    public static final String USER_GET_ALL = "User.getAll";
    public static final String USER_COUNT_ALL = "User.countAll";
    public static final String USER_FIND_BY_USERNAME_OR_EMAIL = "User.findByUsernameOrEmail";
    public static final String USER_COUNT_FIND_BY_USERNAME_OR_EMAIL = "User.countFindByUsernameOrEmail";

    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_SEARCHTERM = "searchTerm";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "raveuserIdGenerator")
    @TableGenerator(name = "raveuserIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "raveuser", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "username", unique = true)
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "expired")
    private boolean expired;

    @Basic
    @Column(name = "locked")
    private boolean locked;

    @Basic
    @Column(name = "enabled")
    private boolean enabled;

    @Basic
    @Column(name = "email", unique = true)
    private String email;

    @Basic
    @Column(name = "openid")
    private String openId;

    @Transient
    private String confirmPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "entity_id"),
            inverseJoinColumns =
            @JoinColumn(name = "authority_id", referencedColumnName = "entity_id"))
    private Collection<Authority> authorities;

    public User() {
        this(null, null);
    }

    public User(Long entityId) {
        this(entityId, null);
    }

    public User(Long entityId, String username) {
        super();
        this.entityId = entityId;
        this.username = username;
        this.authorities = new ArrayList<Authority>();
    }

    /**
     * Gets the unique identifier for this user.
     *
     * @return The unique identifier for this user.
     */
    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.addAll(authorities);
        return grantedAuthorities;
    }

    public void addAuthority(Authority authority) {
        if (!authorities.contains(authority)) {
            authorities.add(authority);
        }
        if (!authority.getUsers().contains(this)) {
            authority.addUser(this);
        }
    }

    public void removeAuthority(Authority authority) {
        if (authorities.contains(authority)) {
            authorities.remove(authority);
        }
    }


    //TODO RAVE-233:Setup Hashing and Salting of Passwords

    /**
     * Gets the password stored in the database
     *
     * @return password as String
     */
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !expired;
    }

    /**
     * Synchronized with password expiration {@see isCredentialsNonExpired}
     *
     * @return <code>true</code> if the user's is not expired valid (ie non-expired), <code>false</code> if no longer valid
     */
    //REVIEW NOTE: Conflating Account and Credential (non)expiration seems likely to cause confusion at some point. 
    @Override
    public boolean isAccountNonExpired() {
        return isCredentialsNonExpired();
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //The following properties are specific to the user profile.
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

    public String getConfirmPassword() {
        //confirmPassword is not stored persistently, so if the value is not set,
        //return the password instead. This will need to be as secure as the password
        //field itself.
        if (confirmPassword != null && confirmPassword.length() > 0) {
            return confirmPassword;
        } else {
            return password;
        }
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @PreRemove
    public void preRemove() {
        for(Authority authority : authorities) {
            authority.removeUser(this);
        }
        this.authorities = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "User{" + "entityId=" + entityId + ", username=" + username + ", expired=" + expired + ", locked=" + locked + ", enabled=" + enabled + ", email=" + email + ", openId=" + openId + '}';
    }
}
