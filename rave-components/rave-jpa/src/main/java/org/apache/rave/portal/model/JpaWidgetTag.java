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

import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
/**
 * A tag for a widget.
 */
@Entity
@Table(name = "widget_tag")
@Access(AccessType.FIELD)
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = JpaWidgetTag.FIND_BY_WIDGET_AND_KEYWORD, query = "select t from JpaWidgetTag t where t.widgetId=:widgetId and UPPER(t.tag.keyword) = UPPER(:keyword)")
})
public class JpaWidgetTag implements WidgetTag, Serializable {

    public static final String FIND_BY_WIDGET_AND_KEYWORD = "findByWidgetAndKeyword";
    public static final String WIDGET_ID_PARAM = "widgetId";
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private JpaUser user;

    @ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private JpaTag tag;

    @Basic
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public JpaWidgetTag() {}

    public JpaWidgetTag(long id, long widgetId, JpaUser user, Date date, JpaTag tag) {
        this.entityId = id;
        this.widgetId = widgetId;
        this.user = user;
        this.createdDate = date;
        this.tag = tag;
    }

    public Long getEntityId(){
        return this.entityId;
    }
    
    public void setEntityId(Long id){
        this.entityId = id;
    }
    
    @Override
    public Long getWidgetId() {
        return this.widgetId;
    }

    @Override
    public void setWidgetId(Long id) {
        this.widgetId = id;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = JpaConverter.getInstance().convert(user, User.class);
    }

    @Override
    public Tag getTag() {
        return this.tag;
    }

    @Override
    public void setTag(Tag tag) {
        this.tag = JpaConverter.getInstance().convert(tag, Tag.class);
    }

    @Override
    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaWidgetTag)) return false;

        JpaWidgetTag that = (JpaWidgetTag) o;

        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (widgetId != null ? !widgetId.equals(that.widgetId) : that.widgetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = widgetId != null ? widgetId.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
