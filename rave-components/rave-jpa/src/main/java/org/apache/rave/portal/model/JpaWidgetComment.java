/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.model;

import org.apache.rave.model.WidgetComment;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * A comment for a widget.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "widget_comment")
@NamedQueries({
        @NamedQuery(name = JpaWidgetComment.DELETE_ALL_BY_USER,
                query="DELETE FROM JpaWidgetComment wc WHERE wc.userId = :userId")
})
@XmlRootElement
public class JpaWidgetComment implements BasicEntity, Serializable, WidgetComment {
    public static final String DELETE_ALL_BY_USER = "WidgetComment.deleteAllByUserId";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "widgetCommentIdGenerator")
    @TableGenerator(name = "widgetCommentIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "widget_comment", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "widget_id")
    private Long widgetId;

    @Basic
    @Column(name="user_id")
    private String userId;

    @Basic @Lob
    @Column(name = "text")
    private String text;

    @Basic
    @Column(name ="last_modified_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Basic
    @Column(name ="created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public JpaWidgetComment() {

    }

    public JpaWidgetComment(Long entityId, Long widgetId, JpaUser user, String text, Date lastModified, Date created) {
        this.entityId = entityId;
        this.widgetId = widgetId;
        this.userId = user.getId();
        this.text = text;
        this.lastModifiedDate = lastModified;
        this.createdDate = created;
    }

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getWidgetId() {
        return widgetId.toString();
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = Long.parseLong(widgetId);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModified) {
        this.lastModifiedDate = lastModified;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JpaWidgetComment comment = (JpaWidgetComment) obj;
        if (entityId != null ? !entityId.equals(comment.entityId) : comment.entityId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "WidgetComment{" +
                "entityId=" + entityId +
                ", widgetId=" + widgetId +
                ", text='" + text + '\'' +
                '}';
    }
}
