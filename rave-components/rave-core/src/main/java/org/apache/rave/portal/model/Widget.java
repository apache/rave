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

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * A widget
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "widget")
@NamedQueries({
        @NamedQuery(name = Widget.WIDGET_GET_ALL, query = Widget.SELECT_W_FROM_WIDGET_W + Widget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = Widget.WIDGET_COUNT_ALL, query = Widget.SELECT_COUNT_W_FROM_WIDGET_W),

        @NamedQuery(name = Widget.WIDGET_GET_BY_OWNER,
                query = Widget.SELECT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_OWNER + Widget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_OWNER,
                query = Widget.SELECT_COUNT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_OWNER),

        @NamedQuery(name = Widget.WIDGET_GET_BY_FREE_TEXT,
                query = Widget.SELECT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_FREE_TEXT + Widget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_FREE_TEXT,
                query = Widget.SELECT_COUNT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_FREE_TEXT),

        @NamedQuery(name = Widget.WIDGET_GET_BY_STATUS,
                query = Widget.SELECT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_STATUS + Widget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_STATUS,
                query = Widget.SELECT_COUNT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_STATUS),

        @NamedQuery(name = Widget.WIDGET_GET_BY_URL, query = Widget.SELECT_W_FROM_WIDGET_W + Widget.WHERE_CLAUSE_URL) ,

        @NamedQuery(name = Widget.WIDGET_GET_BY_TAG, query = Widget.SELECT_W_FROM_WIDGET_W + Widget.JOIN_TAGS+Widget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = Widget.WIDGET_COUNT_BY_TAG, query = Widget.SELECT_COUNT_W_FROM_WIDGET_W + Widget.JOIN_TAGS)
})
public class Widget implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String PARAM_SEARCH_TERM = "searchTerm";
    public static final String PARAM_STATUS = "widgetStatus";
    public static final String PARAM_URL = "url";
    public static final String PARAM_OWNER = "owner";
     public static final String PARAM_TAG = "keyword";

    public static final String WIDGET_GET_ALL = "Widget.getAll";
    public static final String WIDGET_COUNT_ALL = "Widget.countAll";
    public static final String WIDGET_GET_BY_OWNER = "Widget.getByOwner";
    public static final String WIDGET_COUNT_BY_OWNER = "Widget.countByOwner";
    public static final String WIDGET_GET_BY_FREE_TEXT = "Widget.getByFreeText";
    public static final String WIDGET_COUNT_BY_FREE_TEXT = "Widget.countByFreeText";
    public static final String WIDGET_GET_BY_STATUS = "Widget.getByStatus";
    public static final String WIDGET_COUNT_BY_STATUS = "Widget.countByStatus";
    public static final String WIDGET_GET_BY_URL = "Widget.getByUrl";
    public static final String WIDGET_GET_BY_TAG = "Widget.getByTag";
    public static final String WIDGET_COUNT_BY_TAG = "Widget.countByTag";

    static final String SELECT_W_FROM_WIDGET_W = "SELECT w FROM Widget w ";
    static final String SELECT_COUNT_W_FROM_WIDGET_W = "SELECT count(w) FROM Widget w ";

    static final String WHERE_CLAUSE_FREE_TEXT =
            " WHERE lower(w.title) LIKE :" + PARAM_SEARCH_TERM + " OR w.description LIKE :description";
    static final String WHERE_CLAUSE_STATUS = " WHERE w.widgetStatus = :" + PARAM_STATUS;
    static final String WHERE_CLAUSE_URL = " WHERE w.url = :" + PARAM_URL;
    static final String WHERE_CLAUSE_OWNER = " WHERE w.owner = :" + PARAM_OWNER;
    static final String WIDGET_TAG_BY_KEYWORD=" (select t.widgetId from WidgetTag t where lower(t.tag.keyword)=:"+PARAM_TAG+")";
    static final String JOIN_TAGS=" WHERE w.entityId in"+WIDGET_TAG_BY_KEYWORD;

    static final String ORDER_BY_TITLE_ASC = " ORDER BY w.title ASC ";


    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "widgetIdGenerator")
    @TableGenerator(name = "widgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "widget", allocationSize = 1, initialValue = 1)
    private Long entityId;

    /*
        TODO RAVE-234: Figure out what the OpenJPA strategy is for functionality provided by Eclisplink's @Convert
     */
    @XmlElement
    @Basic
    @Column(name = "title")
    private String title;
    //private InternationalString title;

    @XmlElement
    @Basic
    @Column(name = "title_url")
    private String titleUrl;

    @XmlElement
    @Basic
    @Column(name = "url", unique = true)
    private String url;

    @Basic
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Basic
    @Column(name = "screenshot_url")
    private String screenshotUrl;

    @XmlElement
    @Basic
    @Column(name = "type")
    private String type;

    @XmlElement
    @Basic
    @Column(name = "author")
    private String author;

    @XmlElement
    @Basic
    @Column(name = "author_email")
    private String authorEmail;

    @XmlElement
    @Basic
    @Column(name = "description")
    @Lob
    private String description;

    @XmlElement(name = "status")
    @Basic
    @Column(name = "widget_status")
    @Enumerated(EnumType.STRING)
    private WidgetStatus widgetStatus;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "widget_id", referencedColumnName = "entity_id")
    private List<WidgetComment> comments;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @XmlElement
    @Basic
    @Column(name = "disable_rendering")
    private boolean disableRendering;

    @XmlElement
    @Basic
    @Column(name = "disable_rendering_message")
    private String disableRenderingMessage;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "widget_id", referencedColumnName = "entity_id")
    private List<WidgetRating> ratings;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "widget_id", referencedColumnName = "entity_id")
    private List<WidgetTag> tags;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="widget_category",
            joinColumns=@JoinColumn(name="widget_id", referencedColumnName = "entity_id"),
            inverseJoinColumns=@JoinColumn(name="category_id", referencedColumnName = "entity_id")
    )
    @OrderBy("text")
    private List<Category> categories;

    public Widget() {
    }

    public Widget(Long entityId, String url) {
        this.entityId = entityId;
        this.url = url;
    }

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    //See TODO RAVE-234
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Gets the collection of user ratings for this Widget.
     *
     * @return The user ratings for this Widget.
     */
    public List<WidgetRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<WidgetRating> ratings) {
        this.ratings = ratings;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Widget other = (Widget) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Widget{" +
                "entityId=" + entityId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", widgetStatus=" + widgetStatus + '\'' +
                ", owner=" + owner + '\'' +
                ", disable_rendering=" + disableRendering + '\'' +
                ", disable_rendering_message=" + disableRenderingMessage +
                '}';
    }
}
