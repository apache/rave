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

    public Person(org.apache.rave.model.Person person) {
        id = person.getId();
        username = person.getUsername();
        displayName = person.getDisplayName();
        emailAddress = person.getEmail();
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

    public void saveToPerson(org.apache.rave.model.Person person) {
        if (person.getId() != null && !person.getId().equals(id)) {
            throw new RuntimeException("You cannot change the ID of a Person object");
        }

        if (username != null)  { person.setUsername(username); }
        if (displayName != null)  { person.setDisplayName(displayName); }
        if (emailAddress != null)  { person.setEmail(emailAddress); }
    }

}
