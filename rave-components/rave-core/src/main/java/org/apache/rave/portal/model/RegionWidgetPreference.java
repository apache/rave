package org.apache.rave.portal.model;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public interface RegionWidgetPreference {
    /**
     * Gets the ID of the RegionWidget this preference is for
     * @return The ID of the RegionWidget this preference is for
     */
    Long getRegionWidgetId();

    void setRegionWidgetId(Long regionWidgetId);

    /**
     * Gets the name of the preference
     *
     * @return The name of the preference
     */
    String getName();

    void setName(String name);

    /**
     * Gets the value of this preference
     *
     * @return The value of this preference
     */
    String getValue();

    void setValue(String value);
}
