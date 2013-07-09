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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlTransient
public interface Widget {
    String getId();

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

    String getOwnerId();

    void setOwnerId(String owner);

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
