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

import org.apache.rave.model.WidgetTag;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
/**
 * A tag for a widget.
 */
@Entity
@Table(name = "widget_tag", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"widget_id", "user_id", "tag_id"})
})
@Access(AccessType.FIELD)
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = JpaWidgetTag.FIND_BY_WIDGETID_AND_TAGID, query = "select t from JpaWidgetTag t where t.widgetId=:widgetId and t.tagId=:tagId")
})
public class JpaWidgetTag implements WidgetTag, Serializable {

    public static final String FIND_BY_WIDGETID_AND_TAGID = "findByWidgetIDAndTagID";
    public static final String WIDGET_ID_PARAM = "widgetId";
    public static final String TAG_ID_PARAM = "tagId";
    public static final String TAG_KEYWORD_PARAM = "keyword";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "widgetTagIdGenerator")
    @TableGenerator(name = "widgetTagIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "widget_tag", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "widget_id")
    private Long widgetId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tag_id")
    private Long tagId;

    @Basic
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public JpaWidgetTag() {}

    public JpaWidgetTag(long widgetId) {
        this.widgetId = widgetId;
    }

    public JpaWidgetTag(long id, long widgetId, long userId, Date date, long tagId) {
        this.entityId = id;
        this.widgetId = widgetId;
        this.userId = userId;
        this.createdDate = date;
        this.tagId = tagId;
    }

    public Long getEntityId(){
        return this.entityId;
    }
    
    public void setEntityId(Long id){
        this.entityId = id;
    }

    public String getId() {
        return entityId == null ? null : entityId.toString();
    }

    public void setWidgetEntityId(Long id) {
        this.widgetId = id;
    }

    public String getWidgetId() {
        return this.widgetId == null ? null : this.widgetId.toString();
    }

    @Override
    public String getUserId() {
        return this.userId.toString();
    }

    public void setUserEntityId(Long id) {
        this.userId = id;
    }

    public Long getUserEntityId() {
        return userId;
    }

    @Override
    public String getTagId() {
        return this.tagId.toString();
    }

    public void setTagEntityId(Long id) {
        this.tagId = id;
    }

    public Long getTagEntityId() {
        return tagId;
    }

    @Override
    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaWidgetTag)) return false;

        JpaWidgetTag that = (JpaWidgetTag) o;

        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (tagId != null ? !tagId.equals(that.tagId) : that.tagId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (widgetId != null ? !widgetId.equals(that.widgetId) : that.widgetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = widgetId != null ? widgetId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("WidgetTag") ;
        sb.append("{").append("entityId=").append(entityId).append( ", widgetId=").append(widgetId);
        if (tagId!=null) sb.append("tagId=").append(tagId);
        sb.append("}") ;
        return sb.toString();
    }
}
