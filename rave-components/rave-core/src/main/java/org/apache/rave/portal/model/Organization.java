package org.apache.rave.portal.model;

import java.util.Date;

/**
 */
public interface Organization {
    Address getAddress();

    void setAddress(Address address);

    String getDescription();

    void setDescription(String description);

    Date getEndDate();

    void setEndDate(Date endDate);

    String getField();

    void setField(String field);

    String getName();

    void setName(String name);

    Date getStartDate();

    void setStartDate(Date startDate);

    String getSubField();

    void setSubField(String subField);

    String getTitle();

    void setTitle(String title);

    String getWebpage();

    void setWebpage(String webpage);

    String getQualifier();

    void setQualifier(String qualifier);

    Boolean getPrimary();

    void setPrimary(Boolean primary);
}
