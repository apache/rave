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
import org.apache.rave.model.User;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The {@link GrantedAuthority} a {@link JpaUser} can have
 */
@Entity
@Table(name = "granted_authority")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = JpaAuthority.GET_BY_AUTHORITY_NAME, query = "SELECT a FROM JpaAuthority a WHERE a.authority = :authority"),
        @NamedQuery(name = JpaAuthority.GET_ALL, query = "SELECT a FROM JpaAuthority a"),
        @NamedQuery(name = JpaAuthority.GET_ALL_DEFAULT, query = "SELECT a FROM JpaAuthority a WHERE a.defaultForNewUser = true"),
        @NamedQuery(name = JpaAuthority.COUNT_ALL, query = "SELECT COUNT(a) FROM JpaAuthority a")
})
public class JpaAuthority implements BasicEntity, Serializable, Authority {

    private static final long serialVersionUID = 463209366149842862L;

    public static final String PARAM_AUTHORITY_NAME = "authority";
    public static final String GET_BY_AUTHORITY_NAME = "Authority.GetByAuthorityName";
    public static final String GET_ALL = "Authority.GetAll";
    public static final String GET_ALL_DEFAULT = "Authority.GetAllDefault";
    public static final String COUNT_ALL = "Authority.CountAll";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "grantedAuthorityIdGenerator")
    @TableGenerator(name = "grantedAuthorityIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "granted_authority", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "authority", unique = true)
    private String authority;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<JpaUser> users;
    
    @Basic
    @Column(name = "default_for_new_user")
    private boolean defaultForNewUser;

    /**
     * Default constructor, needed for JPA
     */
    public JpaAuthority() {
        this.users = new ArrayList<JpaUser>();
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
    public boolean isDefaultForNewUser() {
        return defaultForNewUser;
    }    
    
    @Override
    public void setDefaultForNewUser(boolean defaultForNewUser) {
        this.defaultForNewUser = defaultForNewUser;
    }    

    @Override
    public Collection<User> getUsers() {
        return ConvertingListProxyFactory.createProxyList(User.class, users);
    }

    @Override
    public void addUser(User user) {
        if (!this.getUsers().contains(user)) {
            this.getUsers().add(user);
        }
        if (!user.getAuthorities().contains(this)) {
            user.addAuthority(this);
        }
    }

    @Override
    public void removeUser(User user) {
        if (this.getUsers().contains(user)) {
            this.getUsers().remove(user);
        }
    }

    @PreRemove
    public void preRemove() {
        for (JpaUser user : users) {
            user.removeAuthority(this);
        }
        this.users = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JpaAuthority authority = (JpaAuthority) o;

        if (entityId != null ? !entityId.equals(authority.entityId) : authority.entityId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}
