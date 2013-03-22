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

import org.apache.rave.model.Authority;
import org.apache.rave.model.PageLayout;
import org.apache.rave.model.Person;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.conversion.JpaConverter;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.apache.rave.util.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;

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
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = JpaUser.USER_GET_BY_USERNAME, query = "select u from JpaUser u where u.username = :"+ JpaUser.PARAM_USERNAME),
        @NamedQuery(name = JpaUser.USER_GET_BY_USER_EMAIL, query = "select u from JpaUser u where u.email = :"+ JpaUser.PARAM_EMAIL),
        @NamedQuery(name = JpaUser.USER_GET_BY_OPENID, query = "select u from JpaUser u where u.openId = :"+ JpaUser.PARAM_OPENID),
        @NamedQuery(name = JpaUser.USER_GET_ALL, query = "select u from JpaUser u order by u.username asc"),
        @NamedQuery(name = JpaUser.USER_GET_BY_FORGOT_PASSWORD_HASH, query = "select u from JpaUser u where u.forgotPasswordHash = :" + JpaUser.PARAM_FORGOT_PASSWORD_HASH),
        @NamedQuery(name = JpaUser.USER_COUNT_ALL, query = "select count(u) from JpaUser u"),
        @NamedQuery(name = JpaUser.USER_FIND_BY_USERNAME_OR_EMAIL, query = "select u from JpaUser u " +
                "where lower(u.username) like :"+ JpaUser.PARAM_SEARCHTERM+" or lower(u.email) like :"+ JpaUser.PARAM_SEARCHTERM+" order by u.username asc"),
        @NamedQuery(name = JpaUser.USER_COUNT_FIND_BY_USERNAME_OR_EMAIL, query = "select count(u) from JpaUser u " +
                "where lower(u.username) like :"+ JpaUser.PARAM_SEARCHTERM+" or lower(u.email) like :"+ JpaUser.PARAM_SEARCHTERM),
        @NamedQuery(name = JpaUser.USER_GET_ALL_FOR_ADDED_WIDGET, query = "select distinct(u) from JpaUser u, JpaRegionWidget rw where rw.region.page.ownerId = CONCAT(u.entityId,'') and rw.widgetId = :widgetId")
})
@DiscriminatorValue("User")
public class JpaUser extends JpaPerson implements BasicEntity, Serializable, User {
    private static final long serialVersionUID = 1L;

    public static final String USER_GET_BY_USERNAME = "User.getByUsername";
    public static final String USER_GET_BY_USER_EMAIL = "User.getByUserEmail";
    public static final String USER_GET_BY_OPENID = "User.getByOpenId";
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
    public static final String PARAM_OPENID = "openId";
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
    private JpaPageLayout defaultPageLayout;

    @Transient
    private String confirmPassword;

    @Transient
    private String defaultPageLayoutCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "entity_id"),
            inverseJoinColumns =
            @JoinColumn(name = "authority_id", referencedColumnName = "entity_id"))
    private Collection<JpaAuthority> authorities;


    public JpaUser() {
        this(null, null);
    }

    public JpaUser(Long entityId) {
        this(entityId, null);
    }

    public JpaUser(Long entityId, String username) {
        super();
        this.entityId = entityId;
        this.username = username;
        this.authorities = new ArrayList<JpaAuthority>();
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
        return CollectionUtils.<GrantedAuthority>toBaseTypedCollection(authorities);
    }

    @Override
    public void addAuthority(Authority authority) {
        JpaAuthority converted = JpaConverter.getInstance().convert(authority, Authority.class);
        if (!authorities.contains(converted)) {
            authorities.add(converted);
        }
        if (!authority.getUsers().contains(this)) {
            authority.addUser(this);
        }
    }

    @Override
    public void removeAuthority(Authority authority) {
        JpaAuthority converted = JpaConverter.getInstance().convert(authority, Authority.class);
        if (authorities.contains(converted)) {
            authorities.remove(converted);
        }
    }

    @Override
    public void setAuthorities(Collection<Authority> newAuthorities) {
        this.getAuthorities().clear();
        if(newAuthorities != null) {
            this.getAuthorities().addAll(newAuthorities);
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !expired;
    }

    //REVIEW NOTE: Conflating Account and Credential (non)expiration seems likely to cause confusion at some point.
    @Override
    public boolean isAccountNonExpired() {
        return isCredentialsNonExpired();
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //The following properties are specific to the user profile.
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getOpenId() {
        return openId;
    }

    @Override
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String getForgotPasswordHash() {
        return forgotPasswordHash;
    }

    @Override
    public void setForgotPasswordHash(String forgotPasswordHash) {
        this.forgotPasswordHash = forgotPasswordHash;
    }

    @Override
    public Date getForgotPasswordTime() {
        return forgotPasswordTime;
    }

    @Override
    public void setForgotPasswordTime(Date forgotPasswordTime) {
        this.forgotPasswordTime = forgotPasswordTime;
    }

    @Override
    public PageLayout getDefaultPageLayout() {
        return defaultPageLayout;
    }

    @Override
    public void setDefaultPageLayout(PageLayout defaultPageLayout) {
        this.defaultPageLayout = JpaConverter.getInstance().convert(defaultPageLayout, PageLayout.class);
    }

    @Override
    public String getConfirmPassword() {
		return confirmPassword;
	}

	@Override
    public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

    @PreRemove
    public void preRemove() {
        for (JpaAuthority authority : authorities) {
            authority.removeUser(this);
        }
        this.authorities = Collections.emptyList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        final JpaUser other = (JpaUser) obj;
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
        for (JpaAuthority a : authorities) {
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

    @Override
    public Person toPerson() {
        PersonImpl p = new PersonImpl();
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

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    public String getDefaultPageLayoutCode() {
		return defaultPageLayoutCode;
	}

	public void setDefaultPageLayoutCode(String defaultPageLayoutCode) {
		this.defaultPageLayoutCode = defaultPageLayoutCode;
	}
}
