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

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * A tag for a widget.
 */
@Entity
@Table(name = "widget_tag")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = WidgetTag.FIND_BY_WIDGET_AND_KEYWORD, query = "select t from WidgetTag t where t.widgetId=:widgetId and UPPER(t.tag.keyword) = UPPER(:keyword)")
})

public class WidgetTag implements BasicEntity, Serializable {

    public static final String FIND_BY_WIDGET_AND_KEYWORD = "findByWidgetAndKeyword";

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "widgetTagIdGenerator")
    @TableGenerator(name = "widgetTagIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "widget_tag", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "widget_id")
    private Long widgetId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Basic
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public WidgetTag() {

    }

    public WidgetTag(Long entityId, Long widgetId, User user, Date created, Tag tag) {
        this.entityId = entityId;
        this.widgetId = widgetId;
        this.user = user;
        this.tag = tag;
        this.createdDate = created;
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WidgetTag other = (WidgetTag) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
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
        StringBuilder sb=new StringBuilder("WidgetTag") ;
        sb.append("{").append("entityId=").append(entityId).append( ", widgetId=").append(widgetId);
        if (tag!=null) sb.append("tag keyword=").append(tag.getKeyword());
        sb.append("}") ;
        return sb.toString();
    }
}
