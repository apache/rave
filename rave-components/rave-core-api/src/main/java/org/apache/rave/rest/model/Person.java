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

package org.apache.rave.rest.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person", propOrder = {
        "id", "username", "displayName", "emailAddress"
})
@XmlRootElement(name = "Person")
public class Person  implements RestEntity{
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "Username")
    private String username;
    @XmlElement(name = "DisplayName")
    private String displayName;
    @XmlElement(name = "EmailAddress")
    private String emailAddress;
    @XmlElement(name = "AboutMe")
    private String aboutMe;
    @XmlElement(name = "PreferredName")
    private String preferredName;
    @XmlElement(name = "Status")
    private String status;
    @XmlElement(name = "AdditionalName")
    private String additionalName;
    @XmlElement(name = "FamilyName")
    private String familyName;
    @XmlElement(name = "GivenName")
    private String givenName;
    @XmlElement(name = "HonorificPrefix")
    private String honorificPrefix;
    @XmlElement(name = "HonorificSuffix")
    private String honorificSuffix;

    public Person(org.apache.rave.model.Person person) {
        id = person.getId();
        username = person.getUsername();
        displayName = person.getDisplayName();
        emailAddress = person.getEmail();
        aboutMe = person.getAboutMe();
        preferredName = person.getPreferredName();
        status = person.getStatus();
        additionalName = person.getAdditionalName();
        familyName = person.getFamilyName();
        givenName = person.getGivenName();
        honorificPrefix = person.getHonorificPrefix();
        honorificSuffix = person.getHonorificSuffix();
    }

    public Person() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public void saveToPerson(org.apache.rave.model.Person person) {
        if (person.getId() != null && !person.getId().equals(id)) {
            throw new RuntimeException("You cannot change the ID of a Person object");
        }

        if (username != null)  { person.setUsername(username); }
        if (displayName != null)  { person.setDisplayName(displayName); }
        if (emailAddress != null)  { person.setEmail(emailAddress); }
        if (aboutMe != null) { person.setAboutMe(aboutMe); }
        if (preferredName != null) { person.setPreferredName(preferredName); }
        if (additionalName != null) { person.setAdditionalName(additionalName); }
        if (familyName != null) { person.setFamilyName(familyName); }
        if (givenName != null) { person.setGivenName(givenName); }
        if (honorificPrefix != null) { person.setHonorificPrefix(honorificPrefix); }
        if (honorificSuffix != null) { person.setHonorificSuffix(honorificSuffix); }
        if (status != null) { person.setStatus(status); }
    }
}
