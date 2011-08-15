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

import org.apache.rave.persistence.BasicEntity;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.*;

/**
 * Represents a person in the persistence context
 */
@Entity
@Table(name = "person")
@SequenceGenerator(name="personIdSeq", sequenceName = "person_id_seq")
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personIdSeq")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "about_me")
    private String aboutMe;

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
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private List<Account> accounts;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "person_address_join",
            joinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="person_id", referencedColumnName = "id"))
    private List<Address> addresses;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private List<Organization> organizations;

    @OneToMany(targetEntity = PersonProperty.class)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private List<PersonProperty> properties;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_friends_jn",
            joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id", referencedColumnName = "id"))
    private List<Person> friends;

    @Transient
    private Url profileSong;

    @Transient
    private Address currentLocation;

    @Transient
    private Url profileVideo;

    @Transient
    private List<Url> urls;

    @Transient
    private Map<String, List<PersonProperty>> mappedProperties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Url getProfileSong() {
        return profileSong;
    }

    public void setProfileSong(Url profileSong) {
        this.profileSong = profileSong;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Address getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Address currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Url getProfileVideo() {
        return profileVideo;
    }

    public void setProfileVideo(Url profileVideo) {
        this.profileVideo = profileVideo;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public Map<String, List<PersonProperty>> getProperties() {
        if(mappedProperties == null) {
            mappedProperties = createPropertyMap(properties);
        }
        return mappedProperties;
    }

    public void setProperties(Map<String, List<PersonProperty>> properties) {
        //this.properties = properties;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    @Override
    @Generated("IDE Generated Method")
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (aboutMe != null ? aboutMe.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (ethnicity != null ? ethnicity.hashCode() : 0);
        result = 31 * result + (fashion != null ? fashion.hashCode() : 0);
        result = 31 * result + (happiestWhen != null ? happiestWhen.hashCode() : 0);
        result = 31 * result + (humor != null ? humor.hashCode() : 0);
        result = 31 * result + (jobInterests != null ? jobInterests.hashCode() : 0);
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        result = 31 * result + (livingArrangement != null ? livingArrangement.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (pets != null ? pets.hashCode() : 0);
        result = 31 * result + (politicalViews != null ? politicalViews.hashCode() : 0);
        result = 31 * result + (preferredUsername != null ? preferredUsername.hashCode() : 0);
        result = 31 * result + (relationshipStatus != null ? relationshipStatus.hashCode() : 0);
        result = 31 * result + (religion != null ? religion.hashCode() : 0);
        result = 31 * result + (romance != null ? romance.hashCode() : 0);
        result = 31 * result + (scaredOf != null ? scaredOf.hashCode() : 0);
        result = 31 * result + (sexualOrientation != null ? sexualOrientation.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (utcOffset != null ? utcOffset.hashCode() : 0);
        result = 31 * result + (profileUrl != null ? profileUrl.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        result = 31 * result + (drinker != null ? drinker.hashCode() : 0);
        result = 31 * result + (smoker != null ? smoker.hashCode() : 0);
        result = 31 * result + (networkPresence != null ? networkPresence.hashCode() : 0);
        result = 31 * result + (bodyType != null ? bodyType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (profileSong != null ? profileSong.hashCode() : 0);
        result = 31 * result + (currentLocation != null ? currentLocation.hashCode() : 0);
        result = 31 * result + (profileVideo != null ? profileVideo.hashCode() : 0);
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + (organizations != null ? organizations.hashCode() : 0);
        result = 31 * result + (urls != null ? urls.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    @Generated("IDE Generated Method")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (aboutMe != null ? !aboutMe.equals(person.aboutMe) : person.aboutMe != null) return false;
        if (accounts != null ? !accounts.equals(person.accounts) : person.accounts != null) return false;
        if (addresses != null ? !addresses.equals(person.addresses) : person.addresses != null) return false;
        if (age != null ? !age.equals(person.age) : person.age != null) return false;
        if (birthday != null ? !birthday.equals(person.birthday) : person.birthday != null) return false;
        if (bodyType != null ? !bodyType.equals(person.bodyType) : person.bodyType != null) return false;
        if (children != null ? !children.equals(person.children) : person.children != null) return false;
        if (currentLocation != null ? !currentLocation.equals(person.currentLocation) : person.currentLocation != null)
            return false;
        if (displayName != null ? !displayName.equals(person.displayName) : person.displayName != null) return false;
        if (drinker != null ? !drinker.equals(person.drinker) : person.drinker != null) return false;
        if (ethnicity != null ? !ethnicity.equals(person.ethnicity) : person.ethnicity != null) return false;
        if (fashion != null ? !fashion.equals(person.fashion) : person.fashion != null) return false;
        if (gender != person.gender) return false;
        if (happiestWhen != null ? !happiestWhen.equals(person.happiestWhen) : person.happiestWhen != null)
            return false;
        if (humor != null ? !humor.equals(person.humor) : person.humor != null) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (jobInterests != null ? !jobInterests.equals(person.jobInterests) : person.jobInterests != null)
            return false;
        if (lastUpdated != null ? !lastUpdated.equals(person.lastUpdated) : person.lastUpdated != null) return false;
        if (livingArrangement != null ? !livingArrangement.equals(person.livingArrangement) : person.livingArrangement != null)
            return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (networkPresence != null ? !networkPresence.equals(person.networkPresence) : person.networkPresence != null)
            return false;
        if (nickname != null ? !nickname.equals(person.nickname) : person.nickname != null) return false;
        if (organizations != null ? !organizations.equals(person.organizations) : person.organizations != null)
            return false;
        if (pets != null ? !pets.equals(person.pets) : person.pets != null) return false;
        if (politicalViews != null ? !politicalViews.equals(person.politicalViews) : person.politicalViews != null)
            return false;
        if (preferredUsername != null ? !preferredUsername.equals(person.preferredUsername) : person.preferredUsername != null)
            return false;
        if (profileSong != null ? !profileSong.equals(person.profileSong) : person.profileSong != null) return false;
        if (profileUrl != null ? !profileUrl.equals(person.profileUrl) : person.profileUrl != null) return false;
        if (profileVideo != null ? !profileVideo.equals(person.profileVideo) : person.profileVideo != null)
            return false;
        if (properties != null ? !properties.equals(person.properties) : person.properties != null) return false;
        if (relationshipStatus != null ? !relationshipStatus.equals(person.relationshipStatus) : person.relationshipStatus != null)
            return false;
        if (religion != null ? !religion.equals(person.religion) : person.religion != null) return false;
        if (romance != null ? !romance.equals(person.romance) : person.romance != null) return false;
        if (scaredOf != null ? !scaredOf.equals(person.scaredOf) : person.scaredOf != null) return false;
        if (sexualOrientation != null ? !sexualOrientation.equals(person.sexualOrientation) : person.sexualOrientation != null)
            return false;
        if (smoker != null ? !smoker.equals(person.smoker) : person.smoker != null) return false;
        if (status != null ? !status.equals(person.status) : person.status != null) return false;
        if (thumbnailUrl != null ? !thumbnailUrl.equals(person.thumbnailUrl) : person.thumbnailUrl != null)
            return false;
        if (urls != null ? !urls.equals(person.urls) : person.urls != null) return false;
        if (username != null ? !username.equals(person.username) : person.username != null) return false;
        if (utcOffset != null ? !utcOffset.equals(person.utcOffset) : person.utcOffset != null) return false;

        return true;
    }

    private static Map<String, List<PersonProperty>> createPropertyMap(List<PersonProperty> properties) {
        Map<String, List<PersonProperty>> map = new HashMap<String, List<PersonProperty>>();
        for(PersonProperty property : properties) {
            List<PersonProperty> propertyList;
            String fieldType = property.getFieldType();
            if(map.containsKey(fieldType)) {
                propertyList = map.get(fieldType);
            } else {
                propertyList = new ArrayList<PersonProperty>();
                map.put(fieldType, propertyList);
            }
            propertyList.add(property);
        }
        return map;
    }

}