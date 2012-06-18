package org.apache.rave.portal.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 */
@XmlTransient
public interface WidgetRating {
    /**
     * Gets the ID of the Widget this rating is for
     * @return The ID of the Widget this rating is for
     */
    Long getWidgetId();

    void setWidgetId(Long widgetId);

    /**
     * Gets the ID of the User this rating is for
     * @return The ID of the User this rating is for
     */
    Long getUserId();

    void setUserId(Long userId);

    /**
     * Gets the score of this rating
     *
     * @return The score of this rating
     */
    Integer getScore();

    void setScore(Integer value);

    Long getId();

    void setId(Long id);
}
