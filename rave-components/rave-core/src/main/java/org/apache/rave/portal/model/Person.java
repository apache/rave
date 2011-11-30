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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a person in the persistence context
 */
@Entity
@Table(name = "person")
@NamedQueries(value = {
    @NamedQuery(name = Person.FIND_BY_USERNAME, query = "select p from Person p where p.username like :username"),
    @NamedQuery(name = Person.FIND_FRIENDS_BY_USERNAME, query = "select a.followed from PersonAssociation a where a.follower.username = :username"),
    @NamedQuery(name = Person.FIND_BY_GROUP_MEMBERSHIP, query = "select m from Group g join g.members m where exists " +
            "(select 'found' from g.members b where b.username = :username) and m.username <> :username")
})
public class Person implements BasicEntity {

    public static final String FIND_BY_USERNAME = "Person.findByUsername";
    public static final String FIND_FRIENDS_BY_USERNAME = "Person.findFriendsByUsername";
    public static final String FIND_BY_GROUP_MEMBERSHIP = "Person.findByGroupMembership";
    public static final String USERNAME_PARAM = "username";

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
    protected List<Address> addresses;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="person_id", referencedColumnName = "entity_id")
    protected List<Organization> organizations;

    @OneToMany(targetEntity = PersonProperty.class)
    @JoinColumn(name = "person_id", referencedColumnName = "entity_id")
    protected List<PersonProperty> properties;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_association",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "entity_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "entity_id"))
    protected List<Person> friends;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getHonorificPrefix() {
        return honorificPrefix;
    }

    public void setHonorificPrefix(String honorificPrefix) {
        this.honorificPrefix = honorificPrefix;
    }

    public String getHonorificSuffix() {
        return honorificSuffix;
    }

    public void setHonorificSuffix(String honorificSuffix) {
        this.honorificSuffix = honorificSuffix;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<PersonProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<PersonProperty> properties) {
        this.properties = properties;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (entityId != null ? !entityId.equals(person.entityId) : person.entityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}

