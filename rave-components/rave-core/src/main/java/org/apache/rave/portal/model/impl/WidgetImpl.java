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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.*;

import java.util.ArrayList;
import java.util.List;

public class WidgetImpl implements Widget {
    private String id;
    private String title;
    private String titleUrl;
    private String url;
    private String thumbnailUrl;
    private String screenshotUrl;
    private String type;
    private String author;
    private String authorEmail;
    private String description;
    private WidgetStatus widgetStatus;
    private List<WidgetComment> comments = new ArrayList<WidgetComment>();
    private String ownerId;
    private boolean disableRendering;
    private String disableRenderingMessage;
    private List<WidgetRating> ratings = new ArrayList<WidgetRating>();
    private List<WidgetTag> tags = new ArrayList<WidgetTag>();
    private List<Category> categories = new ArrayList<Category>();
    private boolean featured;

    public WidgetImpl() {}

    public WidgetImpl(String id) {
        this.id = id;
    }

    public WidgetImpl(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WidgetStatus getWidgetStatus() {
        return widgetStatus;
    }

    public void setWidgetStatus(WidgetStatus widgetStatus) {
        this.widgetStatus = widgetStatus;
    }

    public List<WidgetComment> getComments() {
        return comments;
    }

    public void setComments(List<WidgetComment> comments) {
        this.comments = comments;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String owner) {
        this.ownerId = owner;
    }

    public boolean isDisableRendering() {
        return disableRendering;
    }

    public void setDisableRendering(boolean disableRendering) {
        this.disableRendering = disableRendering;
    }

    public String getDisableRenderingMessage() {
        return disableRenderingMessage;
    }

    public void setDisableRenderingMessage(String disableRenderingMessage) {
        this.disableRenderingMessage = disableRenderingMessage;
    }

    public List<WidgetRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<WidgetRating> ratings) {
        this.ratings = ratings;
    }

    public List<WidgetTag> getTags() {
        return tags;
    }

    public void setTags(List<WidgetTag> tags) {
        this.tags = tags;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WidgetImpl)) return false;

        WidgetImpl widget = (WidgetImpl) o;

        if (disableRendering != widget.disableRendering) return false;
        if (featured != widget.featured) return false;
        if (author != null ? !author.equals(widget.author) : widget.author != null) return false;
        if (authorEmail != null ? !authorEmail.equals(widget.authorEmail) : widget.authorEmail != null) return false;
        if (categories != null ? !categories.equals(widget.categories) : widget.categories != null) return false;
        if (comments != null ? !comments.equals(widget.comments) : widget.comments != null) return false;
        if (description != null ? !description.equals(widget.description) : widget.description != null) return false;
        if (disableRenderingMessage != null ? !disableRenderingMessage.equals(widget.disableRenderingMessage) : widget.disableRenderingMessage != null)
            return false;
        if (id != null ? !id.equals(widget.id) : widget.id != null) return false;
        if (ownerId != null ? !ownerId.equals(widget.ownerId) : widget.ownerId != null) return false;
        if (ratings != null ? !ratings.equals(widget.ratings) : widget.ratings != null) return false;
        if (screenshotUrl != null ? !screenshotUrl.equals(widget.screenshotUrl) : widget.screenshotUrl != null)
            return false;
        if (tags != null ? !tags.equals(widget.tags) : widget.tags != null) return false;
        if (thumbnailUrl != null ? !thumbnailUrl.equals(widget.thumbnailUrl) : widget.thumbnailUrl != null)
            return false;
        if (title != null ? !title.equals(widget.title) : widget.title != null) return false;
        if (titleUrl != null ? !titleUrl.equals(widget.titleUrl) : widget.titleUrl != null) return false;
        if (type != null ? !type.equals(widget.type) : widget.type != null) return false;
        if (url != null ? !url.equals(widget.url) : widget.url != null) return false;
        if (widgetStatus != widget.widgetStatus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (titleUrl != null ? titleUrl.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        result = 31 * result + (screenshotUrl != null ? screenshotUrl.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (authorEmail != null ? authorEmail.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (widgetStatus != null ? widgetStatus.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (disableRendering ? 1 : 0);
        result = 31 * result + (disableRenderingMessage != null ? disableRenderingMessage.hashCode() : 0);
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (featured ? 1 : 0);
        return result;
    }
}
