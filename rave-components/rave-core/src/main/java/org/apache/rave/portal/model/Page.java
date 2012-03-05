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
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

/**
 * A page, which consists of regions, and which may be owned by a {@link User} (note the ownership will likely need to
 * become more flexible to enable things like group ownership in the future).
 * 
 * TODO RAVE-231: not all database providers will be able to support deferrable constraints
 * so for the time being I'm commenting out the owner/render sequence since it
 * will get updated in batches and blow up
 * @UniqueConstraint(columnNames={"owner_id","render_sequence"}
 * 
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Table(name="page", uniqueConstraints={@UniqueConstraint(columnNames={"owner_id","name","page_type"})})
@NamedQueries({
        @NamedQuery(name = Page.GET_BY_USER_ID_AND_PAGE_TYPE, query="SELECT p FROM Page p WHERE p.owner.entityId = :userId and p.pageType = :pageType ORDER BY p.renderSequence"),
        @NamedQuery(name = Page.DELETE_BY_USER_ID_AND_PAGE_TYPE, query="DELETE FROM Page p WHERE p.owner.entityId = :userId and p.pageType = :pageType"),
        @NamedQuery(name = Page.USER_HAS_PERSON_PAGE, query="SELECT count(p) FROM Page p WHERE p.owner.entityId = :userId and p.pageType = :pageType")
})
@Access(AccessType.FIELD)
public class Page implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String GET_BY_USER_ID_AND_PAGE_TYPE = "Page.getByUserIdAndPageType";
    public static final String DELETE_BY_USER_ID_AND_PAGE_TYPE = "Page.deleteByUserIdAndPageType";
    public static final String USER_HAS_PERSON_PAGE = "Page.hasPersonPage";

    @XmlAttribute(name="id")
    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageIdGenerator")
    @TableGenerator(name = "pageIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @XmlElement
    @Basic(optional=false) @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parent_page_id")
    private Page parentPage;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="parentPage")
    @OrderBy("renderSequence")
    private List<Page> subPages;

    @Basic(optional=false) @Column(name="render_sequence")
    private Long renderSequence;

    @ManyToOne
    @JoinColumn(name="page_layout_id")
    private PageLayout pageLayout;

    @XmlElement(name="region")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderOrder")
    @JoinColumn(name="page_id")
    private List<Region> regions;

    @Basic
    @Column(name = "page_type")
    @Enumerated(EnumType.STRING)
    private PageType pageType;

    public Page() {
    }
    
    public Page(Long entityId) {
        this.entityId = entityId;
    }

    public Page(Long entityId, User owner) {
        this.entityId = entityId;
        this.owner = owner;
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

    /**
     * Gets the user defined name of the page
     *
     * @return Valid name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the {@link User} that owns the page
     *
     * @return Valid {@link User}
     */
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Gets the order of the page instance relative to all pages for the owner (useful when presenting pages in an
     * ordered layout like tabs or an accordion container)
     *
     * @return Valid, unique render sequence
     */
    public Long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(Long renderSequence) {
        this.renderSequence = renderSequence;
    }

    /**
     * Gets the {@link PageLayout}
     *
     * @return Valid {@link PageLayout}
     */
    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    /**
     * Gets the widget containing {@link Region}s of the page
     *
     * @return Valid list of {@link Region}s
     */
    @JsonManagedReference
    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public Page getParentPage() {
        return parentPage;
    }

    public void setParentPage(Page parentPage) {
        this.parentPage = parentPage;
    }

    public List<Page> getSubPages() {
        return subPages;
    }

    public void setSubPages(List<Page> subPages) {
        this.subPages = subPages;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Page other = (Page) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Page{" + "entityId=" + entityId + ", name=" + name + ", owner=" + owner + ", renderSequence=" + renderSequence + ", pageLayout=" + pageLayout + ", pageType=" + pageType + "}";
    }
}
