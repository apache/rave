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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A widget
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Access(AccessType.FIELD)
@Table(name = "widget")
@NamedQueries({
        @NamedQuery(name = JpaWidget.GET_ALL, query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = JpaWidget.GET_COUNT, query = JpaWidget.SELECT_COUNT_W_FROM_WIDGET_W),

        @NamedQuery(name = JpaWidget.WIDGET_GET_BY_OWNER,
                query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_OWNER + JpaWidget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = JpaWidget.WIDGET_COUNT_BY_OWNER,
                query = JpaWidget.SELECT_COUNT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_OWNER),

        @NamedQuery(name = JpaWidget.WIDGET_GET_BY_FREE_TEXT,
                query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_FREE_TEXT + JpaWidget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = JpaWidget.WIDGET_COUNT_BY_FREE_TEXT,
                query = JpaWidget.SELECT_COUNT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_FREE_TEXT),

        @NamedQuery(name = JpaWidget.WIDGET_GET_BY_STATUS,
                query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_STATUS + JpaWidget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = JpaWidget.WIDGET_COUNT_BY_STATUS,
                query = JpaWidget.SELECT_COUNT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_STATUS),

        @NamedQuery(name = JpaWidget.WIDGET_GET_BY_URL, query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.WHERE_CLAUSE_URL) ,

        @NamedQuery(name = JpaWidget.WIDGET_GET_BY_TAG, query = JpaWidget.SELECT_W_FROM_WIDGET_W + JpaWidget.JOIN_TAGS + JpaWidget.WHERE_CLAUSE_TAG_ID + JpaWidget.ORDER_BY_TITLE_ASC),
        @NamedQuery(name = JpaWidget.WIDGET_COUNT_BY_TAG, query = JpaWidget.SELECT_COUNT_W_FROM_WIDGET_W + JpaWidget.JOIN_TAGS + JpaWidget.WHERE_CLAUSE_TAG_ID),
        @NamedQuery(name = JpaWidget.WIDGET_UNASSIGN_OWNER, query = "UPDATE JpaWidget w SET w.ownerId = null " + JpaWidget.WHERE_CLAUSE_OWNER )
})
public class JpaWidget implements BasicEntity, Serializable, Widget {
    private static final long serialVersionUID = 1L;

    public static final String PARAM_SEARCH_TERM = "searchTerm";
    public static final String PARAM_STATUS = "widgetStatus";
    public static final String PARAM_URL = "url";
    public static final String PARAM_OWNER = "owner";
    public static final String PARAM_TAG_ID = "tagId";

    public static final String GET_ALL = "Widget.getAll";
    public static final String GET_COUNT = "Widget.countAll";
    public static final String WIDGET_GET_BY_OWNER = "Widget.getByOwner";
    public static final String WIDGET_COUNT_BY_OWNER = "Widget.countByOwner";
    public static final String WIDGET_GET_BY_FREE_TEXT = "Widget.getByFreeText";
    public static final String WIDGET_COUNT_BY_FREE_TEXT = "Widget.countByFreeText";
    public static final String WIDGET_GET_BY_STATUS = "Widget.getByStatus";
    public static final String WIDGET_COUNT_BY_STATUS = "Widget.countByStatus";
    public static final String WIDGET_GET_BY_URL = "Widget.getByUrl";
    public static final String WIDGET_GET_BY_TAG = "Widget.getByTag";
    public static final String WIDGET_COUNT_BY_TAG = "Widget.countByTag";
    public static final String WIDGET_UNASSIGN_OWNER = "Widget.unassignOwner";

    static final String SELECT_W_FROM_WIDGET_W = "SELECT w FROM JpaWidget w ";
    static final String SELECT_COUNT_W_FROM_WIDGET_W = "SELECT count(w) FROM JpaWidget w ";

    static final String WHERE_CLAUSE_FREE_TEXT =
            " WHERE lower(w.title) LIKE :" + PARAM_SEARCH_TERM + " OR w.description LIKE :description";
    static final String WHERE_CLAUSE_STATUS = " WHERE w.widgetStatus = :" + PARAM_STATUS;
    static final String WHERE_CLAUSE_URL = " WHERE w.url = :" + PARAM_URL;
    static final String WHERE_CLAUSE_OWNER = " WHERE w.ownerId = :" + PARAM_OWNER;
    static final String WHERE_CLAUSE_TAG_ID = " WHERE wt.tagId = :" + PARAM_TAG_ID;
    static final String JOIN_TAGS=" join w.tags wt";

    static final String ORDER_BY_TITLE_ASC = " ORDER BY w.featured DESC, w.title ASC ";


    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "widgetIdGenerator")
    @TableGenerator(name = "widgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "widget", allocationSize = 1, initialValue = 1)
    private Long entityId;

    /*
        TODO RAVE-234: Figure out what the OpenJPA strategy is for functionality provided by Eclisplink's @Convert
     */                                                                                                                                          @XmlElement
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
    private List<JpaWidgetComment> comments;

    @Basic
    @Column(name = "owner_id")
    private String ownerId;

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
    private List<JpaWidgetRating> ratings;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "widget_id", referencedColumnName = "entity_id")
    private List<JpaWidgetTag> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="widget_category",
            joinColumns=@JoinColumn(name="widget_id", referencedColumnName = "entity_id"),
            inverseJoinColumns=@JoinColumn(name="category_id", referencedColumnName = "entity_id")
    )
    @OrderBy("text")
    private List<JpaCategory> categories;

    @XmlElement
    @Basic
    @Column(name = "featured", columnDefinition = "boolean default false")
    private boolean featured;

    public JpaWidget() {
    }

    public JpaWidget(Long entityId, String url) {
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

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    @Override
    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    @Override
    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getAuthorEmail() {
        return authorEmail;
    }

    @Override
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitleUrl() {
        return titleUrl;
    }

    @Override
    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public WidgetStatus getWidgetStatus() {
        return widgetStatus;
    }

    @Override
    public void setWidgetStatus(WidgetStatus widgetStatus) {
        this.widgetStatus = widgetStatus;
    }

    @Override
    public List<WidgetComment> getComments() {
        return ConvertingListProxyFactory.createProxyList(WidgetComment.class, comments);
    }

    @Override
    public void setComments(List<WidgetComment> comments) {
        if(this.comments == null) {
            this.comments = new ArrayList<JpaWidgetComment>();
        }
        //Ensure that all operations go through the conversion proxy
        this.getComments().clear();
        if (comments != null) {
            this.getComments().addAll(comments);
        }
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(String owner) {
        this.ownerId = owner;
    }

    @Override
    public List<WidgetRating> getRatings() {
        return ConvertingListProxyFactory.createProxyList(WidgetRating.class, this.ratings);
    }

    @Override
    public void setRatings(List<WidgetRating> ratings) {
        if (this.ratings == null) {
            this.ratings = new ArrayList<JpaWidgetRating>();
        }
        //Ensure that all operations go through the conversion proxy
        this.getRatings().clear();
        if (ratings != null) {
            this.getRatings().addAll(ratings);
        }
    }

    @Override
    public boolean isDisableRendering() {
        return disableRendering;
    }

    @Override
    public void setDisableRendering(boolean disableRendering) {
        this.disableRendering = disableRendering;
    }

    @Override
    public String getDisableRenderingMessage() {
        return disableRenderingMessage;
    }

    @Override
    public void setDisableRenderingMessage(String disableRenderingMessage) {
        this.disableRenderingMessage = disableRenderingMessage;
    }

    @Override
    public List<WidgetTag> getTags() {
        return ConvertingListProxyFactory.createProxyList(WidgetTag.class, tags);
    }

    @Override
    public void setTags(List<WidgetTag> tags) {
        if(this.tags == null) {
            this.tags = new ArrayList<JpaWidgetTag>();
        }
        //Ensure that all operations go through the conversion proxy
        this.getTags().clear();
        if(tags != null) {
            this.getTags().addAll(tags);
        }
    }

    @Override
    public List<Category> getCategories() {
        return ConvertingListProxyFactory.createProxyList(Category.class, categories);
    }

    @Override
    public void setCategories(List<Category> categories) {
        if (this.categories == null) {
            this.categories = new ArrayList<JpaCategory>();
        }
        //Ensure that all operations go through the conversion proxy
        this.getCategories().clear();
        if (categories != null) {
            this.getCategories().addAll(categories);
        }
    }

    @Override
    public boolean isFeatured() {
        return featured;
    }

    @Override
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaWidget other = (JpaWidget) obj;
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
                ", ownerId=" + ownerId + '\'' +
                ", featured=" + featured + '\'' +
                ", disable_rendering=" + disableRendering + '\'' +
                ", disable_rendering_message=" + disableRenderingMessage +
                '}';
    }
}
