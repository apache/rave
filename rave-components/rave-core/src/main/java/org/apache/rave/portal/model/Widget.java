package org.apache.rave.portal.model;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlTransient
public interface Widget {
    Long getId();

    String getScreenshotUrl();

    void setScreenshotUrl(String screenshotUrl);

    String getAuthor();

    void setAuthor(String author);

    String getAuthorEmail();

    void setAuthorEmail(String authorEmail);

    String getDescription();

    void setDescription(String description);

    String getThumbnailUrl();

    void setThumbnailUrl(String thumbnailUrl);

    String getType();

    void setType(String type);

    String getTitle();

    void setTitle(String title);

    String getTitleUrl();

    void setTitleUrl(String titleUrl);

    String getUrl();

    void setUrl(String url);

    WidgetStatus getWidgetStatus();

    void setWidgetStatus(WidgetStatus widgetStatus);

    @XmlElementWrapper
    List<WidgetComment> getComments();

    void setComments(List<WidgetComment> comments);

    User getOwner();

    void setOwner(User owner);

    /**
     * Gets the collection of user ratings for this Widget.
     *
     * @return The user ratings for this Widget.
     */
    @XmlElementWrapper
    List<WidgetRating> getRatings();

    void setRatings(List<WidgetRating> ratings);

    boolean isDisableRendering();

    void setDisableRendering(boolean disableRendering);

    String getDisableRenderingMessage();

    void setDisableRenderingMessage(String disableRenderingMessage);

    @XmlElementWrapper
    List<WidgetTag> getTags();

    void setTags(List<WidgetTag> tags);

    @XmlElementWrapper
    List<Category> getCategories();

    void setCategories(List<Category> categories);

    boolean isFeatured();

    void setFeatured(boolean featured);
}
