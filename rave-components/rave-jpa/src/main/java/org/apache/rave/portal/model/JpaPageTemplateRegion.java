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

import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageTemplateWidget;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "page_template_region")
@NamedQueries({
        @NamedQuery(name = "PageTemplateRegion.findAll", query = "SELECT p FROM JpaPageTemplateRegion p"),
        @NamedQuery(name = "PageTemplateRegion.findByPageTemplateRegionId", query = "SELECT p FROM JpaPageTemplateRegion p WHERE p.entityId = :id")
})
@Access(AccessType.FIELD)
public class JpaPageTemplateRegion implements BasicEntity, Serializable, PageTemplateRegion {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageTemplateRegionIdGenerator")
    @TableGenerator(name = "pageTemplateRegionIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_template_region", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic(optional = false)
    @Column(name = "render_sequence")
    private long renderSequence;

    @JoinColumn(name = "page_template_id")
    @ManyToOne(optional = false)
    private JpaPageTemplate pageTemplate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderSequence")
    @JoinColumn(name = "page_template_region_id")
    private List<JpaPageTemplateWidget> pageTemplateWidgets;
    
    @Basic(optional = false)
    @Column(name = "locked")
    private boolean locked;

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
    public long getRenderSequence() {
        return renderSequence;
    }

    @Override
    public void setRenderSequence(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    @Override
    public JpaPageTemplate getPageTemplate() {
        return pageTemplate;
    }

    @Override
    public void setPageTemplate(PageTemplate pageTemplate) {
        this.pageTemplate = JpaConverter.getInstance().convert(pageTemplate, PageTemplate.class);
    }

    @Override
    public List<PageTemplateWidget> getPageTemplateWidgets() {
        return ConvertingListProxyFactory.createProxyList(PageTemplateWidget.class, this.pageTemplateWidgets);
    }

    @Override
    public void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets) {
        if(this.pageTemplateWidgets == null) {
            this.pageTemplateWidgets = new ArrayList<JpaPageTemplateWidget>();
        }
        this.getPageTemplateWidgets().clear();
        if(pageTemplateWidgets != null) {
            for(PageTemplateWidget widget : pageTemplateWidgets) {
                widget.setPageTemplateRegion(this);
                this.getPageTemplateWidgets().add(widget);
            }
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
