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
package org.apache.rave.portal.model;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.rave.persistence.BasicEntity;

/**
 * A widget
 */
@Entity
@Table(name="widget")
@SequenceGenerator(name="widgetIdSeq", sequenceName = "widget_id_seq")
@NamedQueries({
        @NamedQuery(name = Widget.WIDGET_GET_ALL, query = "SELECT w from Widget w"),
        @NamedQuery(name = Widget.WIDGET_COUNT_ALL, query = "SELECT count(w) FROM Widget w"),
        @NamedQuery(name = Widget.WIDGET_GET_BY_FREE_TEXT,
                query = "SELECT w FROM Widget w WHERE lower(w.title) LIKE :searchTerm OR w.description LIKE :description"),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_FREE_TEXT,
                query = "SELECT count(w) FROM Widget w WHERE lower(w.title) LIKE :searchTerm OR w.description LIKE :description"),

        @NamedQuery(name = Widget.WIDGET_GET_BY_STATUS,
                query = "SELECT w from Widget w WHERE w.widgetStatus = :widgetStatus"),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_STATUS,
                query = "SELECT count(w) FROM Widget w WHERE w.widgetStatus = :widgetStatus"),
        @NamedQuery(name = Widget.WIDGET_GET_BY_STATUS_AND_FREE_TEXT,
                query = "SELECT w FROM Widget w WHERE w.widgetStatus = :widgetStatus AND lower(w.title) LIKE :searchTerm OR w.description LIKE :description"),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_STATUS_AND_FREE_TEXT,
                query = "SELECT count(w) FROM Widget w WHERE w.widgetStatus = :widgetStatus AND lower(w.title) LIKE :searchTerm OR w.description LIKE :description"),

        @NamedQuery(name = Widget.WIDGET_GET_BY_URL, query = "SELECT w FROM Widget w WHERE w.url = :url")
})
public class Widget implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String PARAM_SEARCH_TERM = "searchTerm";
    public static final String PARAM_STATUS = "widgetStatus";
    public static final String PARAM_URL = "url";

    public static final String WIDGET_GET_ALL = "Widget.getAll";
    public static final String WIDGET_COUNT_ALL = "Widget.countAll";
    public static final String WIDGET_GET_BY_FREE_TEXT = "Widget.getByFreeText";
    public static final String WIDGET_COUNT_BY_FREE_TEXT = "Widget.countByFreeText";
    public static final String WIDGET_GET_BY_STATUS = "Widget.getByStatus";
    public static final String WIDGET_COUNT_BY_STATUS = "Widget.countByStatus";
    public static final String WIDGET_GET_BY_STATUS_AND_FREE_TEXT =
            "Widget.getByStatusAndFreeText";
    public static final String WIDGET_COUNT_BY_STATUS_AND_FREE_TEXT =
            "Widget.countByStatusAndFreeText";
    public static final String WIDGET_GET_BY_URL = "Widget.getByUrl";

    @Id @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "widgetIdSeq")
    private Long id;

    /*
        TODO 1: Figure out what the OpenJPA strategy is for functionality provided by Eclisplink's @Convert
     */

    @Basic @Column(name="title")
    private String title;
    //private InternationalString title;

    @Basic @Column(name="url", unique = true)
    private String url;

    @Basic @Column(name="thumbnail_url")
    private String thumbnailUrl;

    @Basic @Column(name="screenshot_url")
    private String screenshotUrl;

    @Basic @Column(name="type")
    private String type;

    @Basic @Column(name="author")
    private String author;

    @Basic @Column(name = "description") @Lob
    private String description;

    @Basic @Column(name = "widget_status")
    @Enumerated(EnumType.STRING)
    private WidgetStatus widgetStatus;


    public Widget() {
    }

    public Widget(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    //See TODO 1
//    public InternationalString getTitle() {
//        return title;
//    }
//
//    public void setTitle(InternationalString title) {
//        this.title = title;
//
// }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WidgetStatus getWidgetStatus() {
        return widgetStatus;
    }

    public void setWidgetStatus(WidgetStatus widgetStatus) {
        this.widgetStatus = widgetStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Widget other = (Widget) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", widgetStatus=" + widgetStatus +
                '}';
    }
}