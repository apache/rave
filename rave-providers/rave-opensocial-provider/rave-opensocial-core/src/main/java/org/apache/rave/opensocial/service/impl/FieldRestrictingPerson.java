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
import org.apache.rave.model.Person;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.portal.model.util.ModelUtils;
import org.apache.rave.util.CollectionUtils;
import org.apache.shindig.protocol.model.Enum;
import org.apache.shindig.protocol.model.EnumImpl;
import org.apache.shindig.social.core.model.AccountImpl;
import org.apache.shindig.social.core.model.AddressImpl;
import org.apache.shindig.social.core.model.BodyTypeImpl;
import org.apache.shindig.social.core.model.ListFieldImpl;
import org.apache.shindig.social.core.model.NameImpl;
import org.apache.shindig.social.core.model.UrlImpl;
import org.apache.shindig.social.opensocial.model.Account;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.BodyType;
import org.apache.shindig.social.opensocial.model.Drinker;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.LookingFor;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.NetworkPresence;
import org.apache.shindig.social.opensocial.model.Organization;
import org.apache.shindig.social.opensocial.model.Smoker;
import org.apache.shindig.social.opensocial.model.Url;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.rave.util.CollectionUtils.getSingleValue;

/**
 * Wraps a {@link org.apache.rave.model.Person} model object and returns values only if
 * the field set contains the requested field
 * <p/>
 * Usage of this wrapper is made possible by Shindig's use of a getter based serialization model
 * <p/>
 * NOTE: Setters will throw a {@link NotSupportedException} as Shindig's SPI has no method for persisting changes to
 * a person.
 */
public class FieldRestrictingPerson implements org.apache.shindig.social.opensocial.model.Person, Serializable {

    private Person internal;
    private Set<String> fields;
    private Map<String, ?> appData;
    private boolean isOwner;
    private boolean isViewer;
    private Map<String, List<PersonProperty>> propertyMap;

    public FieldRestrictingPerson(Person internal, Set<String> fields) {
        this.internal = internal;
        this.fields = fields;
        this.propertyMap = internal != null && internal.getProperties() != null ?
                createPropertyMap(internal.getProperties()) :
                new HashMap<String, List<PersonProperty>>();
    }

    //REQUIRED FIELD
    @Override
    public String getDisplayName() {
        return internal.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        throw new NotSupportedException();
    }

    @Override
    public String getAboutMe() {
        return displayField(Field.ABOUT_ME) ? internal.getAboutMe() : null;
    }

    @Override
    public void setAboutMe(String aboutMe) {
        throw new NotSupportedException();
    }

    @Override
    public List<Account> getAccounts() {
        if (displayField(Field.ACCOUNTS)) {
            List<PersonProperty> properties = getFromProperties(Field.ACCOUNTS);
            return convertAccounts(properties);
        }
        return null;
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
        throw new NotSupportedException();
    }

    @Override
    public List<Address> getAddresses() {
        return displayField(Field.ADDRESSES) ? convertAddresses(internal.getAddresses()) : null;
    }

    @Override
    public void setAddresses(List<Address> addresses) {
        throw new NotSupportedException();
    }

    @Override
    public Integer getAge() {
        String value = getSingleValueFromProperties(Field.AGE);
        return value == null ? null : Integer.parseInt(value);
    }

    @Override
    public void setAge(Integer age) {
        throw new NotSupportedException();
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
        String value = getSingleValueFromProperties(Field.BIRTHDAY);
        return value == null ? null : tryParseDate(value);
    }

    @Override
    public void setBirthday(Date birthday) {
        throw new NotSupportedException();
    }

    @Override
    public BodyType getBodyType() {
        BodyType type = null;
        if (displayField(Field.BODY_TYPE)) {
            Map<String, String> map = mapValuesByQualifier(getFromProperties(Field.BODY_TYPE));
            type = new BodyTypeImpl();
            type.setBuild(map.get("build"));
            type.setEyeColor(map.get("eyeColor"));
            type.setHairColor(map.get("hairColor"));
            type.setHeight(map.containsKey("height") ? Float.parseFloat(map.get("height")) : null);
            type.setWeight(map.containsKey("weight") ? Float.parseFloat(map.get("weight")) : null);
        }
        return type;
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
        return getSingleValueFromProperties(Field.CHILDREN);
    }

    @Override
    public void setChildren(String children) {
        throw new NotSupportedException();
    }

    /**
     * Since addresses are stored as a first class relation to the Rave person, the simplest way to set current location
     * is to store a pointer in the properties to the qualifier for the address in the list.
     * <p/>
     * NOTE: This requires that the qualifier be set on all addresses
     *
     * @return a valid address if the qualifier is found; null otherwise
     */
    @Override
    public Address getCurrentLocation() {
        String qualifier = getSingleValueFromProperties(Field.CURRENT_LOCATION);
        if (qualifier != null) {
            for (org.apache.rave.model.Address address : internal.getAddresses()) {
                if (qualifier.equals(address.getQualifier())) {
                    return convertAddress(address);
                }
            }
        }
        return null;
    }

    @Override
    public void setCurrentLocation(Address currentLocation) {
        throw new NotSupportedException();
    }

    @Override
    public org.apache.shindig.protocol.model.Enum<Drinker> getDrinker() {
        String value = getSingleValueFromProperties(Field.DRINKER);
        return value == null ? null : new EnumImpl<Drinker>(Drinker.valueOf(value));
    }

    @Override
    public void setDrinker(org.apache.shindig.protocol.model.Enum<Drinker> newDrinker) {
        throw new NotSupportedException();
    }

    @Override
    public List<ListField> getEmails() {
        if (!displayField(Field.EMAILS)) {
            return null;
        }
        List<ListField> fields = getListFromProperties(Field.EMAILS);
        //Override primary value as we will set a new primary with the registered address
        for (ListField field : fields) {
            field.setPrimary(false);
        }
        //Set the e-mail used to register with Rave as the "primary" address
        ListFieldImpl listField = new ListFieldImpl("Registered", internal.getEmail());
        listField.setPrimary(true);
        fields.add(listField);

        return fields;
    }

    @Override
    public void setEmails(List<ListField> emails) {
        throw new NotSupportedException();
    }

    @Override
    public String getEthnicity() {
        return getSingleValueFromProperties(Field.ETHNICITY);
    }

    @Override
    public void setEthnicity(String ethnicity) {
        throw new NotSupportedException();
    }

    @Override
    public String getFashion() {
        return getSingleValueFromProperties(Field.FASHION);
    }

    @Override
    public void setFashion(String fashion) {
        throw new NotSupportedException();
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
        String value = getSingleValueFromProperties(Field.GENDER);
        return value == null ? null : Gender.valueOf(value);
    }

    @Override
    public void setGender(Gender newGender) {
        throw new NotSupportedException();
    }

    @Override
    public String getHappiestWhen() {
        return getSingleValueFromProperties(Field.HAPPIEST_WHEN);
    }

    @Override
    public void setHappiestWhen(String happiestWhen) {
        throw new NotSupportedException();
    }

    @Override
    public Boolean getHasApp() {
        return null;
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
        return getSingleValueFromProperties(Field.HUMOR);
    }

    @Override
    public void setHumor(String humor) {
        throw new NotSupportedException();
    }

    //REQUIRED FIELD
    @Override
    public String getId() {
        return internal.getUsername();
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
        return getSingleValueFromProperties(Field.JOB_INTERESTS);
    }

    @Override
    public void setJobInterests(String jobInterests) {
        throw new NotSupportedException();
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
        String value = getSingleValueFromProperties(Field.LAST_UPDATED);
        return value == null ? null : tryParseDate(value);
    }

    @Override
    public void setUpdated(Date updated) {
        throw new NotSupportedException();
    }

    @Override
    public String getLivingArrangement() {
        return getSingleValueFromProperties(Field.LIVING_ARRANGEMENT);
    }

    @Override
    public void setLivingArrangement(String livingArrangement) {
        throw new NotSupportedException();
    }

    @Override
    public List<org.apache.shindig.protocol.model.Enum<LookingFor>> getLookingFor() {
        return displayField(Field.LOOKING_FOR) ? getEnumsFromValues(getValuesFromProperties(Field.LOOKING_FOR)) : null;
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
        if (displayField(Field.NAME)) {
            Name name = new NameImpl();
            name.setGivenName(internal.getGivenName());
            name.setFamilyName(internal.getFamilyName());
            name.setAdditionalName(internal.getAdditionalName());
            name.setFormatted(internal.getDisplayName());
            name.setHonorificPrefix(internal.getHonorificPrefix());
            return name;
        }
        return null;
    }

    @Override
    public void setName(Name name) {
        throw new NotSupportedException();
    }

    @Override
    public Enum<NetworkPresence> getNetworkPresence() {
        String value = getSingleValueFromProperties(Field.NETWORKPRESENCE);
        return value == null ? null : new EnumImpl<NetworkPresence>(NetworkPresence.valueOf(value));
    }

    @Override
    public void setNetworkPresence(Enum<NetworkPresence> networkPresence) {
        throw new NotSupportedException();
    }

    @Override
    public String getNickname() {
        return displayField(Field.NICKNAME) ? internal.getPreferredName() : null;
    }

    @Override
    public void setNickname(String nickname) {
        throw new NotSupportedException();
    }

    @Override
    public List<Organization> getOrganizations() {
        return displayField(Field.ORGANIZATIONS) ? convertOrganizations(internal.getOrganizations()) : null;
    }

    @Override
    public void setOrganizations(List<Organization> organizations) {
        throw new NotSupportedException();
    }

    @Override
    public String getPets() {
        return getSingleValueFromProperties(Field.PETS);
    }

    @Override
    public void setPets(String pets) {
        throw new NotSupportedException();
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
        return getSingleValueFromProperties(Field.POLITICAL_VIEWS);
    }

    @Override
    public void setPoliticalViews(String politicalViews) {
        throw new NotSupportedException();
    }

    @Override
    public String getPreferredUsername() {
        return displayField(Field.PREFERRED_USERNAME) ? internal.getUsername() : null;
    }

    @Override
    public void setPreferredUsername(String preferredString) {
        throw new NotSupportedException();
    }

    @Override
    public Url getProfileSong() {
        return displayField(Field.PROFILE_SONG) ? convertToUrl(getSingleValue(getFromProperties(Field.PROFILE_SONG))) : null;
    }

    @Override
    public void setProfileSong(Url profileSong) {
        throw new NotSupportedException();
    }

    @Override
    public Url getProfileVideo() {
        return displayField(Field.PROFILE_SONG) ? convertToUrl(getSingleValue(getFromProperties(Field.PROFILE_VIDEO))) : null;
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
        return getSingleValueFromProperties(Field.RELATIONSHIP_STATUS);
    }

    @Override
    public void setRelationshipStatus(String relationshipStatus) {
        throw new NotSupportedException();
    }

    @Override
    public String getReligion() {
        return getSingleValueFromProperties(Field.RELIGION);
    }

    @Override
    public void setReligion(String religion) {
        throw new NotSupportedException();
    }

    @Override
    public String getRomance() {
        return getSingleValueFromProperties(Field.ROMANCE);
    }

    @Override
    public void setRomance(String romance) {
        throw new NotSupportedException();
    }

    @Override
    public String getScaredOf() {
        return getSingleValueFromProperties(Field.SCARED_OF);
    }

    @Override
    public void setScaredOf(String scaredOf) {
        throw new NotSupportedException();
    }

    @Override
    public String getSexualOrientation() {
        return getSingleValueFromProperties(Field.SEXUAL_ORIENTATION);
    }

    @Override
    public void setSexualOrientation(String sexualOrientation) {
        throw new NotSupportedException();
    }

    @Override
    public Enum<Smoker> getSmoker() {
        String value = getSingleValueFromProperties(Field.SMOKER);
        return value == null ? null : new EnumImpl<Smoker>(Smoker.valueOf(value));
    }

    @Override
    public void setSmoker(Enum<Smoker> newSmoker) {
        throw new NotSupportedException();
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
        throw new NotSupportedException();
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
        String value = getSingleValueFromProperties(Field.UTC_OFFSET);
        return value == null ? null : Long.parseLong(value);
    }

    @Override
    public void setUtcOffset(Long utcOffset) {
        throw new NotSupportedException();
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
        if (!displayField(Field.URLS)) {
            return null;
        }
        List<PersonProperty> properties = getFromProperties(Field.URLS);
        List<Url> urls = new ArrayList<Url>();
        for (PersonProperty property : properties) {
            urls.add(convertToUrl(property));
        }
        return urls;
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
        return getSingleValueFromProperties(Field.PROFILE_URL);
    }

    @Override
    public void setProfileUrl(String profileUrl) {
        throw new NotSupportedException();
    }

    @Override
    public String getThumbnailUrl() {
        return getSingleValueFromProperties(Field.THUMBNAIL_URL);
    }

    @Override
    public void setThumbnailUrl(String thumbnailUrl) {
        throw new NotSupportedException();
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

    private String getSingleValueFromProperties(Field field) {
        List<String> values = getValuesFromProperties(field);
        return values == null ? null : CollectionUtils.getSingleValue(values);
    }

    private boolean displayField(Field field) {
        return fields == null || fields.isEmpty() || fields.contains(field.toString());
    }

    private List<PersonProperty> getFromProperties(Field field) {
        return propertyMap.containsKey(field.toString()) ?
                propertyMap.get(field.toString()) :
                new ArrayList<PersonProperty>();
    }

    private List<String> getValuesFromProperties(Field field) {
        return displayField(field) ? toValueList(getFromProperties(field)) : null;
    }

    private List<ListField> getListFromProperties(Field field) {
        return displayField(field) ? convertFromProperties(getFromProperties(field)) : null;
    }

    private static List<ListField> convertFromProperties(List<PersonProperty> properties) {
        List<ListField> fieldList = new ArrayList<ListField>();
        for (PersonProperty property : properties) {
            ListField field = new ListFieldImpl(property.getQualifier(), property.getValue());
            field.setPrimary(property.getPrimary());
            fieldList.add(field);
        }
        return fieldList;
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

    private static Date tryParseDate(String value) {
        try {
            return new SimpleDateFormat(ModelUtils.STANDARD_DATE_FORMAT).parse(value);
        } catch (ParseException e) {
            throw new IllegalStateException("Invalid Date found:   " + value);
        }
    }

    private static List<String> toValueList(List<PersonProperty> properties) {
        List<String> values = new ArrayList<String>();
        for (PersonProperty property : properties) {
            values.add(property.getValue());
        }
        return values;
    }

    private static Map<String, String> mapValuesByQualifier(List<PersonProperty> properties) {
        Map<String, String> propertyMap = new HashMap<String, String>();
        for (PersonProperty property : properties) {
            propertyMap.put(property.getQualifier(), property.getValue());
        }
        return propertyMap;
    }

    private static Url convertToUrl(PersonProperty property) {
        return new UrlImpl(property.getValue(), property.getExtendedValue(), property.getQualifier());
    }

    private List<Address> convertAddresses(List<org.apache.rave.model.Address> addresses) {
        List<Address> converted = new ArrayList<Address>();
        if (addresses != null) {
            for (org.apache.rave.model.Address address : addresses) {
                converted.add(convertAddress(address));
            }
        }
        return converted;
    }

    private Address convertAddress(org.apache.rave.model.Address address) {
        Address converted = new AddressImpl(address.getFormatted());
        converted.setCountry(address.getCountry());
        converted.setLatitude(address.getLatitude());
        converted.setLocality(address.getLocality());
        converted.setLongitude(address.getLongitude());
        converted.setPostalCode(address.getPostalCode());
        converted.setRegion(address.getRegion());
        converted.setStreetAddress(address.getStreetAddress());
        converted.setType(address.getQualifier());
        converted.setPrimary(address.getPrimary());
        return converted;
    }


    private static Map<String, List<PersonProperty>> createPropertyMap(List<PersonProperty> properties) {
        Map<String, List<PersonProperty>> map = new HashMap<String, List<PersonProperty>>();
        for (PersonProperty property : properties) {
            List<PersonProperty> propertyList;
            String fieldType = property.getType();
            if (map.containsKey(fieldType)) {
                propertyList = map.get(fieldType);
            } else {
                propertyList = new ArrayList<PersonProperty>();
                map.put(fieldType, propertyList);
            }
            propertyList.add(property);
        }
        return map;
    }

    private static List<Account> convertAccounts(List<PersonProperty> properties) {
        List<Account> accounts = new ArrayList<Account>();
        for (PersonProperty property : properties) {
            Account account = convertToAccount(property);
            accounts.add(account);
        }
        return accounts;
    }

    private static Account convertToAccount(PersonProperty property) {
        Account account = new AccountImpl();
        account.setUsername(property.getValue());
        account.setUserId(property.getExtendedValue());
        account.setDomain(property.getQualifier());
        return account;
    }

    private List<Organization> convertOrganizations(List<org.apache.rave.model.Organization> organizations) {
        List<Organization> converted = new ArrayList<Organization>();
        if (organizations != null) {
            for (org.apache.rave.model.Organization org : organizations) {
                converted.add(convertOrganization(org));
            }
        }
        return converted;
    }

    private Organization convertOrganization(org.apache.rave.model.Organization org) {
        Organization converted = new org.apache.shindig.social.core.model.OrganizationImpl();
        converted.setAddress(convertAddress(org.getAddress()));
        converted.setDescription(org.getDescription());
        converted.setStartDate(org.getStartDate());
        converted.setEndDate(org.getEndDate());
        converted.setField(org.getField());
        converted.setName(org.getName());
        converted.setSubField(org.getSubField());
        converted.setType(org.getQualifier());
        converted.setTitle(org.getTitle());
        converted.setWebpage(org.getWebpage());
        return converted;
    }
}
