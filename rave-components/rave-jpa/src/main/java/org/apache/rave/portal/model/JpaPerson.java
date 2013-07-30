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

import org.apache.rave.model.Address;
import org.apache.rave.model.Organization;
import org.apache.rave.model.Person;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a person in the persistence context
 */
@Entity
@Table(name = "person")
@Access(AccessType.FIELD)
@NamedQueries(value = {
    @NamedQuery(name = JpaPerson.GET_ALL, query = "select p from JpaPerson p"),
    @NamedQuery(name = JpaPerson.GET_COUNT, query = "select count(p) from JpaPerson p"),
    @NamedQuery(name = JpaPerson.FIND_BY_USERNAME, query = "select p from JpaPerson p where p.username like :username"),
    @NamedQuery(name = JpaPerson.FIND_FRIENDS_BY_USERNAME, query = "select a.followedby from JpaPersonAssociation a where a.follower.username = :username and a.status = :status")
})
@DiscriminatorValue("Person")
public class JpaPerson implements BasicEntity, Person, Serializable {

    public static final String FIND_BY_USERNAME = "Person.findByUsername";
    public static final String FIND_FRIENDS_BY_USERNAME = "Person.findFriendsByUsername";
    public static final String USERNAME_PARAM = "username";
    public static final String STATUS_PARAM = "status";
    public static final String GET_ALL = "Person.getAll";
    public static final String GET_COUNT = "Person.getCount";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personIdGenerator")
    @TableGenerator(name = "personIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "person", allocationSize = 1, initialValue = 1)
    protected Long entityId;

    @Basic
    @Column(name = "username", unique = true)
    protected String username;

    @Basic
    @Column(name = "email", unique = true)
    protected String email;

    @Basic
    @Column(name = "display_name", length = 255)
    protected String displayName;

    @Basic
    @Column(name = "additional_name", length = 255)
    protected String additionalName;

    @Basic
    @Column(name = "family_name", length = 255)
    protected String familyName;

    @Basic
    @Column(name = "given_name", length = 255)
    protected String givenName;

    @Basic
    @Column(name = "honorific_prefix", length = 255)
    protected String honorificPrefix;

    @Basic
    @Column(name = "honorific_suffix", length = 255)
    protected String honorificSuffix;

    @Basic
    @Column(name = "preferred_name")
    protected String preferredName;

    @Basic
    @Column(name = "about_me")
    protected String aboutMe;

    @Basic
    @Column(name = "status")
    protected String status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "person_address_jn",
            joinColumns = @JoinColumn(name = "address_id", referencedColumnName = "entity_id"),
            inverseJoinColumns = @JoinColumn(name="person_id", referencedColumnName = "entity_id"))
    protected List<JpaAddress> addresses;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="person_id", referencedColumnName = "entity_id")
    protected List<JpaOrganization> organizations;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = JpaPersonProperty.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "entity_id")
    protected List<JpaPersonProperty> properties;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    @Override
    public void setId(String userId) {
        this.setEntityId(userId == null ? null : Long.parseLong(userId));
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
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getAboutMe() {
        return aboutMe;
    }

    @Override
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public String getPreferredName() {
        return preferredName;
    }

    @Override
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getAdditionalName() {
        return additionalName;
    }

    @Override
    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    @Override
    public String getFamilyName() {
        return familyName;
    }

    @Override
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public String getGivenName() {
        return givenName;
    }

    @Override
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @Override
    public String getHonorificPrefix() {
        return honorificPrefix;
    }

    @Override
    public void setHonorificPrefix(String honorificPrefix) {
        this.honorificPrefix = honorificPrefix;
    }

    @Override
    public String getHonorificSuffix() {
        return honorificSuffix;
    }

    @Override
    public void setHonorificSuffix(String honorificSuffix) {
        this.honorificSuffix = honorificSuffix;
    }

    @Override
    public List<Address> getAddresses() {
        return ConvertingListProxyFactory.createProxyList(Address.class, addresses);
    }

    @Override
    public void setAddresses(List<Address> addresses) {
        if(this.addresses == null) {
            this.addresses = new ArrayList<JpaAddress>();
        }
        this.getAddresses().clear();
        if(addresses != null) {
            this.getAddresses().addAll(addresses);
        }
    }

    @Override
    public List<PersonProperty> getProperties() {
        return ConvertingListProxyFactory.createProxyList(PersonProperty.class, this.properties);
    }

    @Override
    public void setProperties(List<PersonProperty> properties) {
        if(this.properties == null) {
            this.properties = new ArrayList<JpaPersonProperty>();
        }
        this.getProperties().clear();
        if(properties != null) {
            this.getProperties().addAll(properties);
        }
    }

    @Override
    public List<Organization> getOrganizations() {
        return ConvertingListProxyFactory.createProxyList(Organization.class, organizations);
    }

    @Override
    public void setOrganizations(List<Organization> organizations) {
        if(this.organizations == null) {
            this.organizations = new ArrayList<JpaOrganization>();
        }
        this.getOrganizations().clear();
        if(organizations != null) {
            this.getOrganizations().addAll(organizations);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaPerson person = (JpaPerson) o;

        if (entityId != null ? !entityId.equals(person.entityId) : person.entityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}

