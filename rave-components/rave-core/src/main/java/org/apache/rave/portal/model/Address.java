package org.apache.rave.portal.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 */
@XmlTransient
public interface Address {
    String getCountry();

    void setCountry(String country);

    Float getLatitude();

    void setLatitude(Float latitude);

    Float getLongitude();

    void setLongitude(Float longitude);

    String getLocality();

    void setLocality(String locality);

    String getPostalCode();

    void setPostalCode(String postalCode);

    String getRegion();

    void setRegion(String region);

    String getStreetAddress();

    void setStreetAddress(String streetAddress);

    String getQualifier();

    void setQualifier(String qualifier);

    String getFormatted();

    void setFormatted(String formatted);

    Boolean getPrimary();

    void setPrimary(Boolean primary);
}
