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
package org.apache.rave.model;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 */
@XmlTransient
public interface Person {
    String getId();

    void setId(String userId);

    String getUsername();

    void setUsername(String username);

    String getEmail();

    void setEmail(String email);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getAboutMe();

    void setAboutMe(String aboutMe);

    String getPreferredName();

    void setPreferredName(String preferredName);

    String getStatus();

    void setStatus(String status);

    String getAdditionalName();

    void setAdditionalName(String additionalName);

    String getFamilyName();

    void setFamilyName(String familyName);

    String getGivenName();

    void setGivenName(String givenName);

    String getHonorificPrefix();

    void setHonorificPrefix(String honorificPrefix);

    String getHonorificSuffix();

    void setHonorificSuffix(String honorificSuffix);

    List<Address> getAddresses();

    void setAddresses(List<Address> addresses);

    List<PersonProperty> getProperties();

    void setProperties(List<PersonProperty> properties);

    List<Organization> getOrganizations();

    void setOrganizations(List<Organization> organizations);
}
