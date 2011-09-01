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

package org.apache.rave.opensocial.service.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.util.CollectionUtils;
import org.apache.shindig.protocol.model.Enum;
import org.apache.shindig.protocol.model.EnumImpl;
import org.apache.shindig.social.opensocial.model.*;

import java.io.Serializable;
import java.util.*;

/**
 * Wraps a {@link org.apache.rave.opensocial.model.Person} model object and returns values only if
 * the field set contains the requested field
 * <p/>
 * Usage of this wrapper is made possible by Shindig's use of a getter based serialization model
 *
 * NOTE: Setters will throw a {@link NotSupportedException} as Shindig's SPI has no method for persisting changes to
 *       a person.
 */
public class FieldRestrictingPerson implements org.apache.shindig.social.opensocial.model.Person, Serializable {

    private org.apache.rave.opensocial.model.Person internal;
    private Set<String> fields;
    private Map<String, ?> appData;
    private boolean isOwner;
    private boolean isViewer;

    public FieldRestrictingPerson(org.apache.rave.opensocial.model.Person internal, Set<String> fields) {
        this.internal = internal;
        this.fields = fields;
    }

    @Override
    public String getDisplayName() {
        return displayField(Field.DISPLAY_NAME) ? internal.getDisplayName() : null;
    }

    @Override
    public void setDisplayName(String displayName) {
        internal.setDisplayName(displayName);
    }

    @Override
    public String getAboutMe() {
        return displayField(Field.ABOUT_ME) ? internal.getAboutMe() : null;
    }

    @Override
    public void setAboutMe(String aboutMe) {
        internal.setAboutMe(aboutMe);
    }

    @Override
    public List<Account> getAccounts() {
        return displayField(Field.ACCOUNTS) ? CollectionUtils.<Account>toBaseTypedList(internal.getAccounts()) : null;
    }

    @Override
    public void setAccounts(List<Account> accounts) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getActivities() {
        return getValuesFromProperties(Field.ACTIVITIES);
    }

    @Override
    public void setActivities(List<String> activities) {

    }

    @Override
    public List<Address> getAddresses() {
        return displayField(Field.ADDRESSES) ? CollectionUtils.<Address>toBaseTypedList(internal.getAddresses()) : null;
    }

    @Override
    public void setAddresses(List<Address> addresses) {
        throw new NotSupportedException();
    }

    @Override
    public Integer getAge() {
        return displayField(Field.AGE) ? internal.getAge() : null;
    }

    @Override
    public void setAge(Integer age) {
        internal.setAge(age);
    }

    @Override
    public Map<String, ?> getAppData() {
        return this.appData;
    }

    @Override
    public void setAppData(Map<String, ?> appData) {
        this.appData = appData;
    }

    @Override
    public Date getBirthday() {
        return displayField(Field.BIRTHDAY) ? internal.getBirthday() : null;
    }

    @Override
    public void setBirthday(Date birthday) {
        throw new NotSupportedException();
    }

    @Override
    public BodyType getBodyType() {
        return displayField(Field.BODY_TYPE) ? internal.getBodyType() : null;
    }

    @Override
    public void setBodyType(BodyType bodyType) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getBooks() {
        return getValuesFromProperties(Field.BOOKS);
    }

    @Override
    public void setBooks(List<String> books) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getCars() {
        return getValuesFromProperties(Field.CARS);
    }

    @Override
    public void setCars(List<String> cars) {
        throw new NotSupportedException();
    }

    @Override
    public String getChildren() {
        return displayField(Field.CHILDREN) ? internal.getChildren() : null;
    }

    @Override
    public void setChildren(String children) {
        internal.setChildren(children);
    }

    @Override
    public Address getCurrentLocation() {
        return displayField(Field.CURRENT_LOCATION) ? internal.getCurrentLocation() : null;
    }

    @Override
    public void setCurrentLocation(Address currentLocation) {
        throw new NotSupportedException();
    }

    @Override
    public org.apache.shindig.protocol.model.Enum<Drinker> getDrinker() {
        return displayField(Field.DRINKER) ? new EnumImpl<Drinker>(Drinker.valueOf(internal.getDrinker())) : null;
    }

    @Override
    public void setDrinker(org.apache.shindig.protocol.model.Enum<Drinker> newDrinker) {
        internal.setDrinker(newDrinker.getValue().toString());
    }

    @Override
    public List<ListField> getEmails() {
        return getListFromProperties(Field.EMAILS);
    }

    @Override
    public void setEmails(List<ListField> emails) {
        throw new NotSupportedException();
    }

    @Override
    public String getEthnicity() {
        return displayField(Field.ETHNICITY) ? internal.getEthnicity() : null;
    }

    @Override
    public void setEthnicity(String ethnicity) {
        internal.setEthnicity(ethnicity);
    }

    @Override
    public String getFashion() {
        return displayField(Field.FASHION) ? internal.getFashion() : null;
    }

    @Override
    public void setFashion(String fashion) {
        internal.setFashion(fashion);
    }

    @Override
    public List<String> getFood() {
        return getValuesFromProperties(Field.FOOD);
    }

    @Override
    public void setFood(List<String> food) {
        throw new NotSupportedException();
    }

    @Override
    public Gender getGender() {
        return displayField(Field.GENDER) ? internal.getGender() : null;
    }

    @Override
    public void setGender(Gender newGender) {
        internal.setGender(newGender);
    }

    @Override
    public String getHappiestWhen() {
        return displayField(Field.HAPPIEST_WHEN) ? internal.getHappiestWhen() : null;
    }

    @Override
    public void setHappiestWhen(String happiestWhen) {
        internal.setHappiestWhen(happiestWhen);
    }

    @Override
    public Boolean getHasApp() {
        return displayField(Field.HAS_APP) ? false : null;
    }

    @Override
    public void setHasApp(Boolean hasApp) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getHeroes() {
        return getValuesFromProperties(Field.HEROES);
    }

    @Override
    public void setHeroes(List<String> heroes) {
        throw new NotSupportedException();
    }

    @Override
    public String getHumor() {
        return displayField(Field.HUMOR) ? internal.getHumor() : null;
    }

    @Override
    public void setHumor(String humor) {
        internal.setHumor(humor);
    }

    @Override
    public String getId() {
        return displayField(Field.ID) ? internal.getUsername() : null;
    }

    @Override
    public void setId(String id) {
        internal.setUsername(id);
    }

    @Override
    public List<ListField> getIms() {
        return getListFromProperties(Field.IMS);
    }

    @Override
    public void setIms(List<ListField> ims) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getInterests() {
        return getValuesFromProperties(Field.INTERESTS);
    }

    @Override
    public void setInterests(List<String> interests) {
        throw new NotSupportedException();
    }

    @Override
    public String getJobInterests() {
        return displayField(Field.JOB_INTERESTS) ? internal.getJobInterests() : null;
    }

    @Override
    public void setJobInterests(String jobInterests) {
        internal.setJobInterests(jobInterests);
    }

    @Override
    public List<String> getLanguagesSpoken() {
        return getValuesFromProperties(Field.LANGUAGES_SPOKEN);
    }

    @Override
    public void setLanguagesSpoken(List<String> languagesSpoken) {
        throw new NotSupportedException();
    }

    @Override
    public Date getUpdated() {
        return internal.getLastUpdated();
    }

    @Override
    public void setUpdated(Date updated) {
        internal.setLastUpdated(updated);
    }

    @Override
    public String getLivingArrangement() {
        return displayField(Field.LIVING_ARRANGEMENT) ? internal.getLivingArrangement() : null;
    }

    @Override
    public void setLivingArrangement(String livingArrangement) {
        internal.setLivingArrangement(livingArrangement);
    }

    @Override
    public List<org.apache.shindig.protocol.model.Enum<LookingFor>> getLookingFor() {
        return getEnumsFromValues(getValuesFromProperties(Field.LOOKING_FOR));
    }

    @Override
    public void setLookingFor(List<Enum<LookingFor>> lookingFor) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getMovies() {
        return getValuesFromProperties(Field.MOVIES);
    }

    @Override
    public void setMovies(List<String> movies) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getMusic() {
        return getValuesFromProperties(Field.MUSIC);
    }

    @Override
    public void setMusic(List<String> music) {
        throw new NotSupportedException();
    }

    @Override
    public Name getName() {
        return displayField(Field.NAME) ? internal.getName() : null;
    }

    @Override
    public void setName(Name name) {
        throw new NotSupportedException();
    }

    @Override
    public Enum<NetworkPresence> getNetworkPresence() {
        return displayField(Field.NETWORKPRESENCE) ? new EnumImpl<NetworkPresence>(NetworkPresence.valueOf(internal.getNetworkPresence())) : null;
    }

    @Override
    public void setNetworkPresence(Enum<NetworkPresence> networkPresence) {
        internal.setNetworkPresence(networkPresence.getValue().toString());
    }

    @Override
    public String getNickname() {
        return displayField(Field.NICKNAME) ? internal.getNickname() : null;
    }

    @Override
    public void setNickname(String nickname) {
        internal.setNickname(nickname);
    }

    @Override
    public List<Organization> getOrganizations() {
        return displayField(Field.ORGANIZATIONS) ? CollectionUtils.<Organization>toBaseTypedList(internal.getOrganizations()) : null;
    }

    @Override
    public void setOrganizations(List<Organization> organizations) {
        throw new NotSupportedException();
    }

    @Override
    public String getPets() {
        return displayField(Field.PETS) ? internal.getPets() : null;
    }

    @Override
    public void setPets(String pets) {
        internal.setPets(pets);
    }

    @Override
    public List<ListField> getPhoneNumbers() {
        return getListFromProperties(Field.PHONE_NUMBERS);
    }

    @Override
    public void setPhoneNumbers(List<ListField> phoneNumbers) {
        throw new NotSupportedException();
    }

    @Override
    public List<ListField> getPhotos() {
        return getListFromProperties(Field.PHOTOS);
    }

    @Override
    public void setPhotos(List<ListField> photos) {
        throw new NotSupportedException();
    }

    @Override
    public String getPoliticalViews() {
        return displayField(Field.POLITICAL_VIEWS) ? internal.getPoliticalViews() : null;
    }

    @Override
    public void setPoliticalViews(String politicalViews) {
        internal.setPoliticalViews(politicalViews);
    }

    @Override
    public String getPreferredUsername() {
        return displayField(Field.PREFERRED_USERNAME) ? internal.getPreferredUsername() : null;
    }

    @Override
    public void setPreferredUsername(String preferredString) {
        internal.setPreferredUsername(preferredString);
    }

    @Override
    public Url getProfileSong() {
        return displayField(Field.PROFILE_SONG) ? internal.getProfileSong() : null;
    }

    @Override
    public void setProfileSong(Url profileSong) {
        throw new NotSupportedException();
    }

    @Override
    public Url getProfileVideo() {
        return displayField(Field.PROFILE_VIDEO) ? internal.getProfileVideo() : null;
    }

    @Override
    public void setProfileVideo(Url profileVideo) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getQuotes() {
        return getValuesFromProperties(Field.QUOTES);
    }

    @Override
    public void setQuotes(List<String> quotes) {
        throw new NotSupportedException();
    }

    @Override
    public String getRelationshipStatus() {
        return displayField(Field.RELATIONSHIP_STATUS) ? internal.getRelationshipStatus() : null;
    }

    @Override
    public void setRelationshipStatus(String relationshipStatus) {
        internal.setRelationshipStatus(relationshipStatus);
    }

    @Override
    public String getReligion() {
        return displayField(Field.RELIGION) ? internal.getReligion() : null;
    }

    @Override
    public void setReligion(String religion) {
        internal.setReligion(religion);
    }

    @Override
    public String getRomance() {
        return displayField(Field.ROMANCE) ? internal.getRomance() : null;
    }

    @Override
    public void setRomance(String romance) {
        internal.setRomance(romance);
    }

    @Override
    public String getScaredOf() {
        return displayField(Field.SCARED_OF) ? internal.getScaredOf() : null;
    }

    @Override
    public void setScaredOf(String scaredOf) {
        internal.setScaredOf(scaredOf);
    }

    @Override
    public String getSexualOrientation() {
        return displayField(Field.SEXUAL_ORIENTATION) ? internal.getSexualOrientation() : null;
    }

    @Override
    public void setSexualOrientation(String sexualOrientation) {
        internal.setSexualOrientation(sexualOrientation);
    }

    @Override
    public Enum<Smoker> getSmoker() {
        return displayField(Field.SMOKER) ? new EnumImpl<Smoker>(Smoker.valueOf(internal.getSmoker())) : null;
    }

    @Override
    public void setSmoker(Enum<Smoker> newSmoker) {
        internal.setSmoker(newSmoker.getValue().toString());
    }

    @Override
    public List<String> getSports() {
        return getValuesFromProperties(Field.SPORTS);
    }

    @Override
    public void setSports(List<String> sports) {
        throw new NotSupportedException();
    }

    @Override
    public String getStatus() {
        return displayField(Field.STATUS) ? internal.getStatus() : null;
    }

    @Override
    public void setStatus(String status) {
        internal.setStatus(status);
    }

    @Override
    public List<String> getTags() {
        return getValuesFromProperties(Field.TAGS);
    }

    @Override
    public void setTags(List<String> tags) {
        throw new NotSupportedException();
    }

    @Override
    public Long getUtcOffset() {
        return displayField(Field.UTC_OFFSET) ? internal.getUtcOffset() : null;
    }

    @Override
    public void setUtcOffset(Long utcOffset) {
        internal.setUtcOffset(utcOffset);
    }

    @Override
    public List<String> getTurnOffs() {
        return getValuesFromProperties(Field.TURN_OFFS);
    }

    @Override
    public void setTurnOffs(List<String> turnOffs) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getTurnOns() {
        return getValuesFromProperties(Field.TURN_ONS);
    }

    @Override
    public void setTurnOns(List<String> turnOns) {
        throw new NotSupportedException();
    }

    @Override
    public List<String> getTvShows() {
        return getValuesFromProperties(Field.TV_SHOWS);
    }

    @Override
    public void setTvShows(List<String> tvShows) {
        throw new NotSupportedException();
    }

    @Override
    public List<Url> getUrls() {
        //TODO RAVE-178:Get URLs
        //return displayField(Field.URLS) ? internal.getUrls() : null;
        return null;
    }

    @Override
    public void setUrls(List<Url> urls) {
        throw new NotSupportedException();
    }

    @Override
    public boolean getIsOwner() {
        return isOwner;
    }

    @Override
    public String getProfileUrl() {
        return displayField(Field.PROFILE_URL) ? internal.getProfileUrl() : null;
    }

    @Override
    public void setProfileUrl(String profileUrl) {
        internal.setProfileUrl(profileUrl);
    }

    @Override
    public String getThumbnailUrl() {
        return displayField(Field.THUMBNAIL_URL) ? internal.getThumbnailUrl() : null;
    }

    @Override
    public void setThumbnailUrl(String thumbnailUrl) {
        internal.setThumbnailUrl(thumbnailUrl);
    }

    @Override
    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Override
    public boolean getIsViewer() {
        return isViewer;
    }

    @Override
    public void setIsViewer(boolean isViewer) {
        this.isViewer = isViewer;
    }

    private boolean displayField(Field field) {
        return fields == null || fields.isEmpty() || fields.contains(field.toString());
    }

    private List<? extends ListField> getFromProperties(Field field) {
        return internal.getProperties().get(field.toString());
    }

    private List<String> getValuesFromProperties(Field field) {
        return displayField(field) ? toValueList(getFromProperties(field)) : null;
    }

    private List<ListField> getListFromProperties(Field field) {
        return displayField(field) ? CollectionUtils.<ListField>toBaseTypedList(getFromProperties(field)) : null;
    }

    private static List<Enum<LookingFor>> getEnumsFromValues(List<String> values) {
        List<Enum<LookingFor>> looking = new ArrayList<Enum<LookingFor>>();
        if (values != null) {
            for (String value : values) {
                looking.add(new EnumImpl<LookingFor>(LookingFor.valueOf(value)));
            }
        }
        return looking;
    }

    private static List<String> toValueList(List<? extends ListField> properties) {
        List<String> values = new ArrayList<String>();
        for (ListField property : properties) {
            values.add(property.getValue());
        }
        return values;
    }
}
