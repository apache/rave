package org.apache.rave.portal.model;

/**
 *
 */
public interface PersonProperty {
    Long getId();

    void setId(Long id);

    String getType();

    void setType(String type);

    String getValue();

    void setValue(String value);

    String getQualifier();

    void setQualifier(String qualifier);

    Boolean getPrimary();

    void setPrimary(Boolean primary);

    String getExtendedValue();

    void setExtendedValue(String extendedValue);
}
