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
package org.apache.rave.rest.model;

import org.apache.rave.model.WidgetStatus;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Widget", propOrder = {
        "id", "type", "title", "titleUrl", "url", "thumbnailUrl", "screenshotUrl", "author", "authorEmail",
        "description", "status", "disable", "disabledMessage", "featured"
})
@XmlRootElement(name = "RegionWidget")
public class Widget {
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "titleUrl")
    private String titleUrl;
    @XmlElement(name = "url")
    private String url;
    @XmlElement(name = "thumbnailUrl")
    private String thumbnailUrl;
    @XmlElement(name = "screenshotUrl")
    private String screenshotUrl;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "authorEmail")
    private String authorEmail;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "status")
    private WidgetStatus status;
    @XmlElement(name = "disable")
    private boolean disable;
    @XmlElement(name = "disabledMessage")
    private String disabledMessage;
    @XmlElement(name = "featured")
    private boolean featured;

    public Widget() {    }

    public Widget(org.apache.rave.model.Widget base) {
        this.id = base.getId();
        this.title = base.getTitle();
        this.titleUrl = base.getTitleUrl();
        this.url = base.getUrl();
        this.thumbnailUrl = base.getThumbnailUrl();
        this.screenshotUrl = base.getScreenshotUrl();
        this.type = base.getType();
        this.author = base.getAuthor();
        this.authorEmail = base.getAuthorEmail();
        this.description = base.getDescription();
        this.status = base.getWidgetStatus();
        this.disable = base.isDisableRendering();
        this.disabledMessage = base.getDisableRenderingMessage();
        this.featured = base.isFeatured();
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

    public WidgetStatus getStatus() {
        return status;
    }

    public void setStatus(WidgetStatus status) {
        this.status = status;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getDisabledMessage() {
        return disabledMessage;
    }

    public void setDisabledMessage(String disabledMessage) {
        this.disabledMessage = disabledMessage;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
}
