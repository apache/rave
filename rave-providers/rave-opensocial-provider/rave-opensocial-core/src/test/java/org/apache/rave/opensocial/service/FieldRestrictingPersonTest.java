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

package org.apache.rave.opensocial.service;


import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.opensocial.service.impl.FieldRestrictingPerson;
import org.apache.rave.model.PersonProperty;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.apache.rave.portal.model.impl.PersonPropertyImpl;
import org.apache.rave.portal.model.util.ModelUtils;
import org.apache.shindig.protocol.model.EnumImpl;
import org.apache.shindig.social.core.model.BodyTypeImpl;
import org.apache.shindig.social.core.model.UrlImpl;
import org.apache.shindig.social.opensocial.model.Account;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.Drinker;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.LookingFor;
import org.apache.shindig.social.opensocial.model.NetworkPresence;
import org.apache.shindig.social.opensocial.model.Person;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 */
public class FieldRestrictingPersonTest {

    private static final String USERNAME = "canonical";
    private static final String ABOUT_ME = "About Me";
    private static final String ADDITIONAL_NAME = "AdditionalName";
    private static final String DISPLAY_NAME = "Display Name";
    private static final String E_MAIL_ADDRESS = "E-mail address";
    private static final String FIRST_NAME = "User";
    private static final String GIVEN_NAME = "Canonical";
    private static final String PREFIX = "Prefix";
    private static final String SUFFIX = "Suffix";
    private static final String PREFERRED_NAME = "PreferredName";
    private static final String STATUS = "Status";
    private static final Integer AGE = 24;
    private static final String BIRTHDAY_STRING = "1997-07-16T19:20:30+0100";
    private static final Date BIRTHDAY = parseDate(BIRTHDAY_STRING);
    private static final String BODY_BUILD = "big";
    private static final String BODY_EYE_COLOR = "red";
    private static final String ACTIVITY_1 = "snowboarding";
    private static final String ACTIVITY_2 = "baseball";
    private static final String E_MAIL_ADDRESS_2 = "ihateyou@h8r.com";
    private static final String E_MAIL_ADDRESS_3 = "boo@yahoo.com";
    private static final String IM_PROVIDER_1 = "aol";
    private static final String IM_PROVIDER_2 = "skype";
    private static final String IM_1 = "aimname";
    private static final String IM_2 = "skypename";
    private static final String LINK_VALUE = "linkValue";
    private static final String LINK_TEXT = "linkText";
    private static final String STREET = "1 Long Road";
    private static final String CITY = "Big City";
    private static final String STATE = "TX";
    private static final String COUNTRY = "USA";
    private static final float LATITUDE = 10234.12F;
    private static final float LONGITUDE = 0.0F;
    private static final String POSTAL_CODE = "00000";
    private static final String QUALIFIER = "Home";
    private static final String ID = "1";


    @Test
    public void getId() {
        Person p = new FieldRestrictingPerson(getTestPerson(), null);
        assertThat(p.getId(), is(equalTo(USERNAME)));
    }
    @Test
    public void getDisplayName() {
        Person p = new FieldRestrictingPerson(getTestPerson(), null);
        assertThat(p.getDisplayName(), is(equalTo(DISPLAY_NAME)));
    }
    @Test
    public void getUsername_null() {
        Person p = new FieldRestrictingPerson(getTestPerson(), null);
        assertThat(p.getPreferredUsername(), is(equalTo(USERNAME)));
    }
    @Test
    public void getUsername_empty() {
        Person p = new FieldRestrictingPerson(getTestPerson(), new HashSet<String>());
        assertThat(p.getPreferredUsername(), is(equalTo(USERNAME)));
    }
    @Test
    public void getUsername_valid() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PREFERRED_USERNAME));
        assertThat(p.getPreferredUsername(), is(equalTo(USERNAME)));
    }

    @Test
    public void getAboutMe_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ABOUT_ME));
        assertThat(p.getAboutMe(), is(equalTo(ABOUT_ME)));
    }

    @Test
    public void getAboutMe_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BIRTHDAY, Person.Field.ACTIVITIES));
        assertThat(p.getAboutMe(), is(nullValue()));
    }

    @Test
    public void getAge_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.AGE));
        assertThat(p.getAge(), is(equalTo(AGE)));
    }

    @Test
    public void getAge_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BIRTHDAY, Person.Field.ACTIVITIES));
        assertThat(p.getAge(), is(nullValue()));
    }

    @Test
    public void getBirthday_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BIRTHDAY));
        assertThat(p.getBirthday(), is(equalTo(BIRTHDAY)));
    }

    @Test
    public void getBirthday_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.AGE, Person.Field.ACTIVITIES));
        assertThat(p.getBirthday(), is(nullValue()));
    }

    @Test
    public void getActvities_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ACTIVITIES));
        List<String> activities = p.getActivities();
        assertThat(activities.size(), is(equalTo(2)));
        assertThat(activities.contains(ACTIVITY_1), is(true));
        assertThat(activities.contains(ACTIVITY_2), is(true));
    }

    @Test
    public void getActivities_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.AGE, Person.Field.BOOKS));
        assertThat(p.getActivities(), is(nullValue()));
    }
    @Test
    public void getBooks_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BOOKS, Person.Field.ACTIVITIES));
        assertThat(p.getBooks().isEmpty(), is(true));
    }

    @Test
    public void getCars_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.CARS, Person.Field.ACTIVITIES));
        assertThat(p.getCars().isEmpty(), is(true));
    }

    @Test
    public void getBodyType_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BODY_TYPE));
        assertThat(p.getBodyType().getBuild(), is(equalTo(BODY_BUILD)));
        assertThat(p.getBodyType().getEyeColor(), is(equalTo(BODY_EYE_COLOR)));
    }

    @Test
    public void getBodyType_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.AGE, Person.Field.ACTIVITIES));
        assertThat(p.getBodyType(), is(nullValue()));
    }

    @Test
    public void getChildren_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ABOUT_ME));
        assertThat(p.getChildren(), is(nullValue()));
    }

    @Test
    public void getDrinker_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.DRINKER));
        assertThat(p.getDrinker().getValue(), is(equalTo(Drinker.HEAVILY)));
    }

    @Test
    public void getDrinker_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ABOUT_ME));
        assertThat(p.getDrinker(), is(nullValue()));
    }

    @Test
    public void getEmails_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.EMAILS));
        assertThat(p.getEmails().size(), is(equalTo(3)));
        int primaryCount = 0;
        for(ListField field : p.getEmails()) {
            assertThat(isValidEmailField(field), is(true));
            primaryCount += field.getPrimary() != null && field.getPrimary() ? 1 : 0;
        }
        assertThat(primaryCount, is(equalTo(1)));
    }

    @Test
    public void getEthnicity_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ETHNICITY, Person.Field.ACTIVITIES));
        assertThat(p.getEthnicity(), is(nullValue()));
    }

    @Test
    public void getFashion_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.FASHION, Person.Field.ACTIVITIES));
        assertThat(p.getFashion(), is(nullValue()));
    }

    @Test
    public void getFood_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.FOOD, Person.Field.ACTIVITIES));
        assertThat(p.getFood().isEmpty(), is(true));
    }

    @Test
    public void getGender_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.GENDER));
        assertThat(p.getGender(), is(equalTo(Person.Gender.female)));
    }

    @Test
    public void getHappiestWhen_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.HAPPIEST_WHEN));
        assertThat(p.getHappiestWhen(), is(nullValue()));
    }

    @Test
    public void getHumor_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.HUMOR));
        assertThat(p.getHumor(), is(nullValue()));
    }

    @Test
    public void getGender_null() {
        org.apache.rave.model.Person testPerson = getTestPerson();
        testPerson.setProperties(new ArrayList<PersonProperty>());
        Person p = new FieldRestrictingPerson(testPerson, getFieldSet(Person.Field.GENDER));
        assertThat(p.getGender(), is(nullValue()));
    }

    @Test
    public void getIms_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.IMS));
        assertThat(p.getIms().size(), is(equalTo(2)));
        for(ListField field : p.getIms()) {
            if(IM_PROVIDER_1.equals(field.getType())) {
                assertThat(field.getValue(), is(equalTo(IM_1)));
            } else if(IM_PROVIDER_2.equals(field.getType())) {
                assertThat(field.getValue(), is(equalTo(IM_2)));
            } else {
                fail();
            }
        }
    }

    @Test
    public void getInterests_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.INTERESTS));
        assertThat(p.getInterests().isEmpty(), is(true));
    }

    @Test
    public void getJobInterests_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.JOB_INTERESTS));
        assertThat(p.getJobInterests(), is(nullValue()));
    }

    @Test
    public void getLanguagesSpoken_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.LANGUAGES_SPOKEN));
        assertThat(p.getLanguagesSpoken().isEmpty(), is(true));
    }

    @Test
    public void getUpdated_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.LAST_UPDATED));
        assertThat(p.getUpdated(), is(nullValue()));
    }

    @Test
    public void getLivingArrangement_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.LIVING_ARRANGEMENT));
        assertThat(p.getLivingArrangement(), is(nullValue()));
    }

    @Test
    public void getLookingFor_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.LOOKING_FOR));
        assertThat(p.getLookingFor().size(), is(equalTo(1)));
    }

    @Test
    public void getMovies_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.MOVIES));
        assertThat(p.getMovies().isEmpty(), is(true));
    }

    @Test
    public void getMusic_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.MUSIC));
        assertThat(p.getMusic().isEmpty(), is(true));
    }

    @Test
    public void getNetworkPresence_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.NETWORKPRESENCE));
        assertThat(p.getNetworkPresence(), is(nullValue()));
    }

    @Test
    public void getNickname_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.NICKNAME));
        assertThat(p.getNickname(), is(equalTo(PREFERRED_NAME)));
    }

    @Test
    public void getPets_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PETS));
        assertThat(p.getPets(), is(nullValue()));
    }

    @Test
    public void getPhoneNumbers_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PHONE_NUMBERS));
        assertThat(p.getPhoneNumbers().isEmpty(), is(true));
    }

    @Test
    public void getPhotos_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PHOTOS));
        assertThat(p.getPhotos().isEmpty(), is(true));
    }

    @Test
    public void getPoliticalViews_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.POLITICAL_VIEWS));
        assertThat(p.getPoliticalViews(), is(nullValue()));
    }

    @Test
    public void getProfileSong_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PROFILE_SONG));
        assertThat(p.getProfileSong().getValue(), is(equalTo(LINK_VALUE)));
        assertThat(p.getProfileSong().getLinkText(), is(equalTo(LINK_TEXT)));
    }

    @Test
    public void getProfileSong_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.BOOKS));
        assertThat(p.getProfileSong(), is(nullValue()));
    }

    @Test
    public void getProfileVideo_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.PROFILE_VIDEO));
        assertThat(p.getProfileVideo(), is(nullValue()));
    }

    @Test
    public void getQuotes_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.QUOTES));
        assertThat(p.getQuotes().isEmpty(), is(true));
    }

    @Test
    public void getRelationshipStatus_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.RELATIONSHIP_STATUS));
        assertThat(p.getRelationshipStatus(), is(nullValue()));
    }

    @Test
    public void getStatus_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.STATUS));
        assertThat(p.getStatus(), is(equalTo(STATUS)));
    }

    @Test
    public void getAddresses_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ADDRESSES));
        assertThat(p.getAddresses().size(), is(equalTo(2)));
        assertThat(p.getAddresses().get(1).getStreetAddress(), is(equalTo(STREET)));
        assertThat(p.getAddresses().get(1).getLocality(), is(equalTo(CITY)));
        assertThat(p.getAddresses().get(1).getRegion(), is(equalTo(STATE)));
        assertThat(p.getAddresses().get(1).getCountry(), is(equalTo(COUNTRY)));
        assertThat(p.getAddresses().get(1).getLatitude(), is(equalTo(LATITUDE)));
        assertThat(p.getAddresses().get(1).getLongitude(), is(equalTo(LONGITUDE)));
        assertThat(p.getAddresses().get(1).getPostalCode(), is(equalTo(POSTAL_CODE)));
        assertThat(p.getAddresses().get(1).getType(), is(equalTo(QUALIFIER)));
    }

    @Test
    public void getCurrentLocation_set() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.CURRENT_LOCATION));
        assertThat(p.getCurrentLocation().getStreetAddress(), is(equalTo(STREET)));
    }

    @Test
    public void getCurrentLocation_notset() {
        Person p = new FieldRestrictingPerson(getTestPerson(), getFieldSet(Person.Field.ADDRESSES));
        assertThat(p.getCurrentLocation(), is(nullValue()));
    }

    @Test(expected = NotSupportedException.class)
    public void setStatus() {
        new FieldRestrictingPerson(null, null).setStatus(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setRelationshipStatus() {
        new FieldRestrictingPerson(null, null).setRelationshipStatus(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setQuotes() {
        new FieldRestrictingPerson(null, null).setQuotes(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setProfileVideo() {
        new FieldRestrictingPerson(null, null).setProfileVideo(new UrlImpl());
    }

    @Test(expected = NotSupportedException.class)
    public void setProfileSong() {
        new FieldRestrictingPerson(null, null).setProfileSong(new UrlImpl());
    }

    @Test(expected = NotSupportedException.class)
    public void setPoliticalViews() {
        new FieldRestrictingPerson(null, null).setPoliticalViews(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setPhotos() {
        new FieldRestrictingPerson(null, null).setPhotos(new ArrayList<ListField>());
    }

    @Test(expected = NotSupportedException.class)
    public void setPhoneNumbers() {
        new FieldRestrictingPerson(null, null).setPhoneNumbers(new ArrayList<ListField>());
    }

    @Test(expected = NotSupportedException.class)
    public void setPets() {
        new FieldRestrictingPerson(null, null).setPets(PREFERRED_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setNickname() {
        new FieldRestrictingPerson(null, null).setNickname(PREFERRED_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setNetworkPresence() {
        new FieldRestrictingPerson(null, null).setNetworkPresence(new EnumImpl<NetworkPresence>(NetworkPresence.AWAY));
    }

    @Test(expected = NotSupportedException.class)
    public void setMusic() {
        new FieldRestrictingPerson(null, null).setMusic(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setMovies() {
        new FieldRestrictingPerson(null, null).setMovies(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setLookingFor() {
        new FieldRestrictingPerson(null, null).setLookingFor(new ArrayList<org.apache.shindig.protocol.model.Enum<LookingFor>>());
    }

    @Test(expected = NotSupportedException.class)
    public void setLivingArrangement() {
        new FieldRestrictingPerson(null, null).setLivingArrangement(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setUpdated() {
        new FieldRestrictingPerson(null, null).setUpdated(new Date());
    }

    @Test(expected = NotSupportedException.class)
    public void setLanguagesSpoken() {
        new FieldRestrictingPerson(null, null).setLanguagesSpoken(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setJobInterests() {
        new FieldRestrictingPerson(null, null).setJobInterests(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setInterests() {
        new FieldRestrictingPerson(null, null).setInterests(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setIms() {
        new FieldRestrictingPerson(null, null).setIms(new ArrayList<ListField>());
    }

    @Test(expected = NotSupportedException.class)
    public void setHumor() {
        new FieldRestrictingPerson(null, null).setHumor(DISPLAY_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setDisplayName() {
        new FieldRestrictingPerson(null, null).setDisplayName(DISPLAY_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setAboutMe() {
        new FieldRestrictingPerson(null, null).setAboutMe(DISPLAY_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setActivities() {
        new FieldRestrictingPerson(null, null).setActivities(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setAge() {
        new FieldRestrictingPerson(null, null).setAge(AGE);
    }

    @Test(expected = NotSupportedException.class)
    public void setAccounts() {
        new FieldRestrictingPerson(null, null).setAccounts(new ArrayList<Account>());
    }

    @Test(expected = NotSupportedException.class)
    public void setAddresses() {
        new FieldRestrictingPerson(null, null).setAddresses(new ArrayList<Address>());
    }

    @Test(expected = NotSupportedException.class)
    public void setBirthday() {
        new FieldRestrictingPerson(null, null).setBirthday(new Date());
    }

    @Test(expected = NotSupportedException.class)
    public void setBodyType() {
        new FieldRestrictingPerson(null, null).setBodyType(new BodyTypeImpl());
    }

    @Test(expected = NotSupportedException.class)
    public void setBooks() {
        new FieldRestrictingPerson(null, null).setBooks(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setCars() {
        new FieldRestrictingPerson(null, null).setCars(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setChildren() {
        new FieldRestrictingPerson(null, null).setChildren(DISPLAY_NAME);
    }

    @Test(expected = NotSupportedException.class)
    public void setCurrentLocation() {
        new FieldRestrictingPerson(null, null).setCurrentLocation(new org.apache.shindig.social.core.model.AddressImpl());
    }

    @Test(expected = NotSupportedException.class)
    public void setDrinker() {
        new FieldRestrictingPerson(null, null).setDrinker(new EnumImpl<Drinker>(Drinker.QUIT));
    }

    @Test(expected = NotSupportedException.class)
    public void setEthnicity() {
        new FieldRestrictingPerson(null, null).setEthnicity(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setFashion() {
        new FieldRestrictingPerson(null, null).setFashion(SUFFIX);
    }

    @Test(expected = NotSupportedException.class)
    public void setFood() {
        new FieldRestrictingPerson(null, null).setFood(new ArrayList<String>());
    }

    @Test(expected = NotSupportedException.class)
    public void setEmails() {
        new FieldRestrictingPerson(null, null).setEmails(new ArrayList<ListField>());
    }

    @Test(expected = NotSupportedException.class)
    public void setGender() {
        new FieldRestrictingPerson(null, null).setGender(Person.Gender.male);
    }

    @Test(expected = NotSupportedException.class)
    public void setHappiestWhen() {
        new FieldRestrictingPerson(null, null).setHappiestWhen(SUFFIX);
    }

    private org.apache.rave.model.Person getTestPerson() {
        org.apache.rave.model.Person person = new PersonImpl();
        person.setUsername(USERNAME);
        person.setAboutMe(ABOUT_ME);
        person.setAdditionalName(ADDITIONAL_NAME);
        person.setDisplayName(DISPLAY_NAME);
        person.setEmail(E_MAIL_ADDRESS);
        person.setFamilyName(FIRST_NAME);
        person.setGivenName(GIVEN_NAME);
        person.setHonorificPrefix(PREFIX);
        person.setHonorificSuffix(SUFFIX);
        person.setPreferredName(PREFERRED_NAME);
        person.setStatus(STATUS);
        List<PersonProperty> properties = new ArrayList<PersonProperty>();
        properties.add(new PersonPropertyImpl("1", "gender", Person.Gender.female.toString(), null, "", false));
        properties.add(new PersonPropertyImpl("1", "drinker", Drinker.HEAVILY.toString(), null, "", false));
        properties.add(new PersonPropertyImpl("1", "age", AGE.toString(), null, "", false));
        properties.add(new PersonPropertyImpl("1", "birthday", BIRTHDAY_STRING, null, "", false));
        properties.add(new PersonPropertyImpl("1", "bodyType", BODY_BUILD, null, "build", false));
        properties.add(new PersonPropertyImpl("1", "bodyType", BODY_EYE_COLOR, null, "eyeColor", false));
        properties.add(new PersonPropertyImpl("1", "bodyType", "25.24", null, "height", false));
        properties.add(new PersonPropertyImpl("1", "ims", IM_1, null, IM_PROVIDER_1, true));
        properties.add(new PersonPropertyImpl("1", "ims", IM_2, null, IM_PROVIDER_2, false));
        properties.add(new PersonPropertyImpl("1", "emails", E_MAIL_ADDRESS_2, null, "personal", false));
        properties.add(new PersonPropertyImpl("1", "emails", E_MAIL_ADDRESS_3, null, "junk", true));
        properties.add(new PersonPropertyImpl("1", "activities", ACTIVITY_1, null, "", false));
        properties.add(new PersonPropertyImpl("1", "activities", ACTIVITY_2, null, "", false));
        properties.add(new PersonPropertyImpl("1", "profileSong", LINK_VALUE, LINK_TEXT, null, false));
        properties.add(new PersonPropertyImpl("1", "lookingFor", LookingFor.FRIENDS.toString(), null, null, false));
        properties.add(new PersonPropertyImpl("1", "currentLocation", QUALIFIER, null, null, null));
        properties.add(new PersonPropertyImpl("1", "account", IM_1, "1", IM_PROVIDER_1, false));
        person.setProperties(properties);
        org.apache.rave.model.Address address = new AddressImpl();
        address.setCountry(COUNTRY);
        address.setLatitude(LATITUDE);
        address.setLongitude(LONGITUDE);
        address.setLocality(CITY);
        address.setRegion(STATE);
        address.setPostalCode(POSTAL_CODE);
        address.setStreetAddress(STREET);
        address.setQualifier(QUALIFIER);
        List<org.apache.rave.model.Address> addresses = new ArrayList<org.apache.rave.model.Address>();
        addresses.add(new AddressImpl());
        addresses.add(address);
        person.setAddresses(addresses);

        return person;
    }

    private Set<String> getFieldSet(Person.Field... fields) {
        Set<String> set = new HashSet<String>();
        for(Person.Field field : fields) {
            set.add(field.toString());
        }
        return set;
    }

    private static Date parseDate(String birthdayString) {
        try {
            return new SimpleDateFormat(ModelUtils.STANDARD_DATE_FORMAT).parse(birthdayString);
        } catch (ParseException e) {
            throw new RuntimeException("Parse Exception...",e);
        }
    }

    private Boolean isValidEmailField(ListField field) {
        boolean valid;
        if(E_MAIL_ADDRESS.equals(field.getValue())) {
            valid = "Registered".equals(field.getType());
            valid &= field.getPrimary();
        } else if(E_MAIL_ADDRESS_2.equals(field.getValue())) {
            valid = "personal".equals(field.getType());
            valid &= !field.getPrimary();
        } else if(E_MAIL_ADDRESS_3.equals(field.getValue())) {
            valid = "junk".equals(field.getType());
            valid &= !field.getPrimary();
        } else {
            valid = false;
        }
        return valid;
    }
}
