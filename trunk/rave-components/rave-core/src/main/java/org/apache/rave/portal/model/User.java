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

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * {@inheritDoc}
 * <p/>
 * A user of the system
 */
@Entity
@NamedQueries({
        @NamedQuery(name = User.USER_GET_BY_USERNAME, query = "select u from User u where u.username = :"+User.PARAM_USERNAME),
        @NamedQuery(name = User.USER_GET_BY_USER_EMAIL, query = "select u from User u where u.email = :"+User.PARAM_EMAIL),
        @NamedQuery(name = User.USER_GET_ALL, query = "select u from User u order by u.username asc"),
        @NamedQuery(name = User.USER_GET_BY_FORGOT_PASSWORD_HASH, query = "select u from User u where u.forgotPasswordHash = :" + User.PARAM_FORGOT_PASSWORD_HASH),
        @NamedQuery(name = User.USER_COUNT_ALL, query = "select count(u) from User u"),
        @NamedQuery(name = User.USER_FIND_BY_USERNAME_OR_EMAIL, query = "select u from User u " +
                "where lower(u.username) like :"+User.PARAM_SEARCHTERM+" or lower(u.email) like :"+User.PARAM_SEARCHTERM+" order by u.username asc"),
        @NamedQuery(name = User.USER_COUNT_FIND_BY_USERNAME_OR_EMAIL, query = "select count(u) from User u " +
                "where lower(u.username) like :"+User.PARAM_SEARCHTERM+" or lower(u.email) like :"+User.PARAM_SEARCHTERM),
        @NamedQuery(name = User.USER_GET_ALL_FOR_ADDED_WIDGET, query = "select distinct(rw.region.page.owner) from RegionWidget rw where rw.widget.entityId = :widgetId order by rw.region.page.owner.familyName, rw.region.page.owner.givenName")
})
public class User extends Person implements UserDetails, BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String USER_GET_BY_USERNAME = "User.getByUsername";
    public static final String USER_GET_BY_USER_EMAIL = "User.getByUserEmail";
    public static final String USER_GET_ALL = "User.getAll";
    public static final String USER_COUNT_ALL = "User.countAll";
    public static final String USER_FIND_BY_USERNAME_OR_EMAIL = "User.findByUsernameOrEmail";
    public static final String USER_COUNT_FIND_BY_USERNAME_OR_EMAIL = "User.countFindByUsernameOrEmail";
    public static final String USER_GET_COMMENTERS = "User.getCommenters";
    public static final String USER_GET_ALL_FOR_ADDED_WIDGET = "User.getAllForAddedWidget";
    public static final String USER_GET_BY_FORGOT_PASSWORD_HASH = "User.getByForgotPasswordHash";

    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_FORGOT_PASSWORD_HASH = "forgotPasswordHash";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_SEARCHTERM = "searchTerm";
    public static final String PARAM_WIDGET_ID = "widgetId";


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
    @Column(name = "openid")
    private String openId;

    @Basic
    @Column(name = "forgotPasswordHash", unique = true)
    private String forgotPasswordHash;

    @Basic
    @Column(name = "password_hash_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date forgotPasswordTime;

    @ManyToOne
    @JoinColumn(name="default_page_layout_id")
    private PageLayout defaultPageLayout;    

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

    public void setAuthorities(Collection<Authority> newAuthorities) {
        this.authorities = newAuthorities;
    }

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

    public boolean isLocked() {
        return locked;
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

    public boolean isExpired() {
        return expired;
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
        for (Authority authority : authorities) {
            authority.removeUser(this);
        }
        this.authorities = Collections.emptyList();
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
        final StringBuffer sb = new StringBuffer();
        sb.append("User");
        sb.append("{entityId=").append(entityId);
        sb.append(", username='").append(username).append('\'');
        sb.append(", expired=").append(expired);
        sb.append(", locked=").append(locked);
        sb.append(", enabled=").append(enabled);
        sb.append(", email='").append(email).append('\'');
        sb.append(", openId='").append(openId).append('\'');
        sb.append(", authorities=[");
        boolean first=true;
        for (Authority a : authorities) {
            if (!first) {
                sb.append(',');
            }
            sb.append('\'').append(a.getAuthority()).append('\'');
            first = false;
        }
        sb.append(']');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Conversion function to create a new Person object based off of this User class.  This is useful for when you
     * have a User object and want to pass on the Person data without the User class data (like password, etc)
     * 
     * @return a Person object representing the data contained in this class
     */
    public Person toPerson() {
        Person p = new Person();
        p.setAboutMe(this.getAboutMe());
        p.setAdditionalName(this.getAdditionalName());
        p.setAddresses(this.getAddresses());
        p.setDisplayName(this.getDisplayName());
        p.setEmail(this.getEmail());
        p.setEntityId(this.getEntityId());
        p.setFamilyName(this.getFamilyName());
        p.setFriends(this.getFriends());
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
}
