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

import org.apache.rave.model.PageLayout;
import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageType;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="page_template")
@NamedQueries({
        @NamedQuery(name = JpaPageTemplate.PAGE_TEMPLATE_GET_ALL, query = "SELECT p FROM JpaPageTemplate p WHERE p.pageType <> 'SUB_PAGE' ORDER BY p.renderSequence"),
        @NamedQuery(name = JpaPageTemplate.PAGE_TEMPLATE_GET_DEFAULT_PAGE_BY_TYPE, query = "SELECT p FROM JpaPageTemplate p WHERE p.defaultTemplate = true and p.pageType = :pageType"),
        @NamedQuery(name = JpaPageTemplate.PAGE_TEMPLATE_GET_ALL_FOR_TYPE, query = "SELECT p FROM JpaPageTemplate p WHERE p.pageType = :pageType")
})
@Access(AccessType.FIELD)
public class JpaPageTemplate implements BasicEntity, Serializable, PageTemplate {

    private static final long serialVersionUID = 1L;
    public static final String PAGE_TEMPLATE_GET_ALL = "PageTemplate.getAll";
    public static final String PAGE_TEMPLATE_GET_DEFAULT_PAGE_BY_TYPE = "PageTemplate.getDefaultPage";
    public static final String PAGE_TEMPLATE_GET_ALL_FOR_TYPE = "PageTemplate.getAllByType";

    @Id
    @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageTemplateIdGenerator")
    @TableGenerator(name = "pageTemplateIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page_template", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic @Column(name="name", unique = false)
    private String name;
    
    @Basic @Column(name="description", unique = false)
    private String description;

    @Basic(optional = false)
    @Column(name="page_type", unique = false)
    private String pageType;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parent_page_template_id")
    private JpaPageTemplate parentPageTemplate;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="parentPageTemplate")
    @OrderBy("renderSequence")
    private List<JpaPageTemplate> subPageTemplates;

    @ManyToOne
    @JoinColumn(name = "page_layout_id")
    private JpaPageLayout pageLayout;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderSequence")
    @JoinColumn(name="page_template_id")
    private List<JpaPageTemplateRegion> pageTemplateRegions;

    @Basic(optional = false)
    @Column(name = "render_sequence")
    private long renderSequence;

    @Basic(optional = false)
    @Column(name = "default_template")
    private boolean defaultTemplate;

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getPageType() {
        return pageType;
    }

    @Override
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public PageTemplate getParentPageTemplate() {
        return parentPageTemplate;
    }

    @Override
    public void setParentPageTemplate(PageTemplate parentPageTemplate) {
        this.parentPageTemplate = JpaConverter.getInstance().convert(parentPageTemplate, PageTemplate.class);
    }

    @Override
    public PageLayout getPageLayout() {
        return pageLayout;
    }

    @Override
    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = JpaConverter.getInstance().convert(pageLayout, PageLayout.class);
    }

    @Override
    public List<PageTemplateRegion> getPageTemplateRegions() {
        return ConvertingListProxyFactory.createProxyList(PageTemplateRegion.class, pageTemplateRegions);
    }

    @Override
    public void setPageTemplateRegions(List<PageTemplateRegion> pageTemplateRegions) {
        if(this.pageTemplateRegions == null) {
            this.pageTemplateRegions = new ArrayList<JpaPageTemplateRegion>();
        }
        this.getPageTemplateRegions().clear();
        if(pageTemplateRegions != null) {
            for(PageTemplateRegion region : pageTemplateRegions) {
                region.setPageTemplate(this);
                this.getPageTemplateRegions().add(region);
            }
        }
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
    public boolean isDefaultTemplate() {
        return defaultTemplate;
    }

    @Override
    public void setDefaultTemplate(boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    @Override
    public List<PageTemplate> getSubPageTemplates() {
        return ConvertingListProxyFactory.createProxyList(PageTemplate.class, subPageTemplates);
    }

    @Override
    public void setSubPageTemplates(List<PageTemplate> subPageTemplates) {
        if(this.subPageTemplates == null) {
            this.subPageTemplates = new ArrayList<JpaPageTemplate>();
        }
        this.getSubPageTemplates().clear();
        if(subPageTemplates != null) {
            for(PageTemplate subPageTemplate : subPageTemplates) {
                subPageTemplate.setParentPageTemplate(this);
                this.getSubPageTemplates().add(subPageTemplate);
            }
        }
    }

    @Override
    public String getId() {
        return this.getEntityId() == null ? null : this.getEntityId().toString();
    }
}
