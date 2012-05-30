package org.apache.rave.portal.model;

import java.util.List;

/**
 */
public interface Person {
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

    List<Person> getFriends();

    void setFriends(List<Person> friends);

    List<Organization> getOrganizations();

    void setOrganizations(List<Organization> organizations);
}
