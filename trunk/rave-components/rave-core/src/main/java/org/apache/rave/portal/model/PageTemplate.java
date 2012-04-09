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

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="page_template")
@NamedQueries({
        @NamedQuery(name = PageTemplate.PAGE_TEMPLATE_GET_ALL, query = "SELECT p FROM PageTemplate p ORDER BY p.renderSequence"),
        @NamedQuery(name = PageTemplate.PAGE_TEMPLATE_GET_DEFAULT_PAGE_BY_TYPE, query = "SELECT p FROM PageTemplate p WHERE p.defaultTemplate = true and p.pageType = :pageType")
})
@Access(AccessType.FIELD)
public class PageTemplate implements BasicEntity, Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PAGE_TEMPLATE_GET_ALL = "PageTemplate.getAll";
    public static final String PAGE_TEMPLATE_GET_DEFAULT_PAGE_BY_TYPE = "PageTemplate.getDefaultPage";

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
    @Enumerated(EnumType.STRING)
    private PageType pageType;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parent_page_template_id")
    private PageTemplate parentPageTemplate;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="parentPageTemplate")
    @OrderBy("renderSequence")
    private List<PageTemplate> subPageTemplates;

    @ManyToOne
    @JoinColumn(name = "page_layout_id")
    private PageLayout pageLayout;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderSequence")
    @JoinColumn(name="page_template_id")
    private List<PageTemplateRegion> pageTemplateRegions;

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

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PageTemplate getParentPageTemplate() {
        return parentPageTemplate;
    }

    public void setParentPageTemplate(PageTemplate parentPageTemplate) {
        this.parentPageTemplate =  parentPageTemplate;
    }

    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    public List<PageTemplateRegion> getPageTemplateRegions() {
        return pageTemplateRegions;
    }

    public void setPageTemplateRegions(List<PageTemplateRegion> pageTemplateRegions) {
        this.pageTemplateRegions = pageTemplateRegions;
    }

    public long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public boolean isDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public List<PageTemplate> getSubPageTemplates() {
        return subPageTemplates;
    }

    public void setSubPageTemplates(List<PageTemplate> subPageTemplates) {
        this.subPageTemplates = subPageTemplates;
    }    
}
