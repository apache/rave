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
package org.apache.rave.opensocial.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a person in the persistence context
 */

public class Person {

    public static final String FIND_FRIENDS_BY_USERNAME = "Person.findFriendsByUsername";
    public static final String FIND_BY_GROUP_MEMBERSHIP = "Person.findByGroupMembership";
    public static final String FIND_BY_USERNAME = org.apache.rave.portal.model.Person.FIND_BY_USERNAME;
    public static final String USERNAME_PARAM = org.apache.rave.portal.model.Person.USERNAME_PARAM;

    @OneToOne
    private org.apache.rave.portal.model.Person internalPerson;

    @Basic
    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private org.apache.shindig.social.opensocial.model.Person.Gender gender;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Basic
    @Column(name = "children")
    private String children;

    @Basic
    @Column(name = "ethnicity")
    private String ethnicity;

    @Basic
    @Column(name = "fashion")
    private String fashion;

    @Basic
    @Column(name = "happiest_when")
    private String happiestWhen;

    @Basic
    @Column(name = "humor")
    private String humor;

    @Basic
    @Column(name = "job_interests")
    private String jobInterests;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last-updated")
    private Date lastUpdated;

    @Basic
    @Column(name = "living_arrangement")
    private String livingArrangement;

    @Basic
    @Column(name = "nickname")
    private String nickname;

    @Basic
    @Column(name = "pets")
    private String pets;

    @Basic
    @Column(name = "political_views")
    private String politicalViews;

    @Basic
    @Column(name = "preferred_username")
    private String preferredUsername;

    @Basic
    @Column(name = "relationship_status")
    private String relationshipStatus;

    @Basic
    @Column(name = "religion")
    private String religion;

    @Basic
    @Column(name = "romance")
    private String romance;

    @Basic
    @Column(name = "scared_of")
    private String scaredOf;

    @Basic
    @Column(name = "sexual_orientation")
    private String sexualOrientation;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "utc_offset")
    private Long utcOffset;

    @Basic
    @Column(name = "profile_url")
    private String profileUrl;

    @Basic
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Basic
    @Column(name = "drinker")
    private String drinker;

    @Basic
    @Column(name = "smoker")
    private String smoker;

    @Basic
    @Column(name = "network_presence")
    private String networkPresence;

    @Embedded
    private BodyType bodyType;

    @Embedded
    private Name name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "entity_id")
    private List<Account> accounts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "entity_id")
    private List<Organization> organizations;



    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public org.apache.shindig.social.opensocial.model.Person.Gender getGender() {
        return gender;
    }

    public void setGender(org.apache.shindig.social.opensocial.model.Person.Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getFashion() {
        return fashion;
    }

    public void setFashion(String fashion) {
        this.fashion = fashion;
    }

    public String getHappiestWhen() {
        return happiestWhen;
    }

    public void setHappiestWhen(String happiestWhen) {
        this.happiestWhen = happiestWhen;
    }

    public String getHumor() {
        return humor;
    }

    public void setHumor(String humor) {
        this.humor = humor;
    }

    public String getJobInterests() {
        return jobInterests;
    }

    public void setJobInterests(String jobInterests) {
        this.jobInterests = jobInterests;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLivingArrangement() {
        return livingArrangement;
    }

    public void setLivingArrangement(String livingArrangement) {
        this.livingArrangement = livingArrangement;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getPoliticalViews() {
        return politicalViews;
    }

    public void setPoliticalViews(String politicalViews) {
        this.politicalViews = politicalViews;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getRomance() {
        return romance;
    }

    public void setRomance(String romance) {
        this.romance = romance;
    }

    public String getScaredOf() {
        return scaredOf;
    }

    public void setScaredOf(String scaredOf) {
        this.scaredOf = scaredOf;
    }

    public String getSexualOrientation() {
        return sexualOrientation;
    }

    public void setSexualOrientation(String sexualOrientation) {
        this.sexualOrientation = sexualOrientation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Long utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDrinker() {
        return drinker;
    }

    public void setDrinker(String drinker) {
        this.drinker = drinker;
    }

    public String getSmoker() {
        return smoker;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public String getNetworkPresence() {
        return networkPresence;
    }

    public void setNetworkPresence(String networkPresence) {
        this.networkPresence = networkPresence;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }



}