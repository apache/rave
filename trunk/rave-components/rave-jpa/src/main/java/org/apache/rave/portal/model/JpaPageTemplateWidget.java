/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model;

import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageTemplateWidget;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "page_template_widget")
@NamedQueries({
        @NamedQuery(name = "PageTemplateGadget.findAll", query = "SELECT p FROM JpaPageTemplateWidget p"),
        @NamedQuery(name = "PageTemplateGadget.findByPageTemplateGadgetId", query = "SELECT p FROM JpaPageTemplateWidget p WHERE p.entityId = :id")
})
@Access(AccessType.FIELD)
public class JpaPageTemplateWidget implements BasicEntity, Serializable, PageTemplateWidget {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageTemplateWidgetIdGenerator")
    @TableGenerator(name = "pageTemplateWidgetIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_template_widget", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @JoinColumn(name = "page_template_region_id")
    @ManyToOne(optional = false)
    private JpaPageTemplateRegion pageTemplateRegion;

    @Basic(optional = false)
    @Column(name = "render_sequence")
    private long renderSequence;

    @Column(name = "widget_id")
    @Basic(optional = false)
    private Long widgetId;

    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

    @Basic(optional = false)
    @Column(name = "hide_chrome")
    private boolean hideChrome;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }

    @Override
    public PageTemplateRegion getPageTemplateRegion() {
        return pageTemplateRegion;
    }

    @Override
    public void setPageTemplateRegion(PageTemplateRegion pageTemplateRegion) {
        this.pageTemplateRegion = JpaConverter.getInstance().convert(pageTemplateRegion, PageTemplateRegion.class);
    }

    @Override
    public long getRenderSeq() {
        return renderSequence;
    }

    @Override
    public void setRenderSeq(long renderSeq) {
        this.renderSequence = renderSeq;
    }

    @Override
    public String getWidgetId() {
        return widgetId.toString();
    }

    @Override
    public void setWidgetId(String widgetId) {
        this.widgetId = Long.parseLong(widgetId);
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isHideChrome() {
        return hideChrome;
    }

    @Override
    public void setHideChrome(boolean hideChrome) {
        this.hideChrome = hideChrome;
    }
}
