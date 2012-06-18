package org.apache.rave.portal.model;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlTransient
public interface PortalPreference {
    /**
     * Gets the key of the preference, e.g. "availableFruit"
     *
     * @return name of the preference key
     */
    String getKey();

    void setKey(String key);

    /**
     * Gets a String array of the preference values, e.g. {"apple", "pear", "orange"}
     *
     * @return String array of the preference values
     */
    List<String> getValues();

    void setValues(List<String> values);

    /**
     * Helper method for the view layer to get a single value for a preference.
     * If there is no value, it returns {@literal null}.
     * If there is 1 value, it returns that value.
     *
     * @return the single value of the preference or {@literal null} if not set
     * @throws org.apache.rave.exception.NotSupportedException if the preference has multiple values
     */
    String getValue();

    /**
     * Sets a single value for a preference. Will overwrite any exisiting value(s)
     *
     * @param value String value of the preference
     */
    void setValue(String value);
}
