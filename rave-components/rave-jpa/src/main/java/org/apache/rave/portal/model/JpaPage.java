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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.conversion.ConvertingListProxyFactory;
import org.apache.rave.portal.model.conversion.JpaConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A page, which consists of regions, and which may be owned by a {@link JpaUser} (note the ownership will likely need to
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
        @NamedQuery(name = JpaPage.GET_ALL, query="SELECT p FROM JpaPage p"),
        @NamedQuery(name = JpaPage.GET_COUNT, query="SELECT count(p) FROM JpaPage p"),
        @NamedQuery(name = JpaPage.DELETE_BY_USER_ID_AND_PAGE_TYPE, query="DELETE FROM JpaPage p WHERE p.ownerId = :userId and p.pageType = :pageType"),
        @NamedQuery(name = JpaPage.USER_HAS_PERSON_PAGE, query="SELECT count(p) FROM JpaPage p WHERE p.ownerId = :userId and p.pageType = :pageType"),
        @NamedQuery(name = JpaPage.GET_BY_CONTEXT_AND_PAGE_TYPE, query="SELECT p FROM JpaPage p WHERE p.contextId = :contextId and p.pageType = :pageType")
})
@Access(AccessType.FIELD)
public class JpaPage implements BasicEntity, Serializable, Page {
    private static final long serialVersionUID = 1L;

    public static final String DELETE_BY_USER_ID_AND_PAGE_TYPE = "JpaPage.deleteByUserIdAndPageType";
    public static final String USER_HAS_PERSON_PAGE = "JpaPage.hasPersonPage";
    public static final String GET_ALL = "JpaPage.getAll";
    public static final String GET_COUNT = "JpaPage.getCount";
    public static final String GET_BY_CONTEXT_AND_PAGE_TYPE = "JpaPage.getByContextAndPageType";

    @XmlAttribute(name="id")
    @Id @Column(name="entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pageIdGenerator")
    @TableGenerator(name = "pageIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "page", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @XmlElement
    @Basic(optional=false) @Column(name="name")
    private String name;

    @Basic
    @Column(name = "owner_id")
    private String ownerId;

    @Basic
    @Column(name = "context_id")
    private String contextId;

    @ManyToOne(cascade=CascadeType.ALL, optional = true)
    @JoinColumn(name="parent_page_id")
    private JpaPage parentPage;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="parentPage")
    private List<JpaPage> subPages;

    @ManyToOne
    @JoinColumn(name="page_layout_id")
    private JpaPageLayout pageLayout;

    @XmlElement(name="region")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderOrder")
    @JoinColumn(name="page_id")
    private List<JpaRegion> regions;

    @Basic
    @Column(name = "page_type")
    private String pageType;

    @OneToMany(targetEntity=JpaPageUser.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="page", orphanRemoval=true)
    private List<JpaPageUser> members;


    public JpaPage() {
    }

    public JpaPage(Long entityId) {
        this.entityId = entityId;
    }

    public JpaPage(Long entityId, String ownerId) {
        this.entityId = entityId;
        this.ownerId = ownerId;
    }

    @Override
    public String getId() {
        return getEntityId().toString();
    }

    @Override
    public void setId(String id) {
        setEntityId(id == null ? null : Long.parseLong(id));
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
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the {@link User} that owns the page
     *
     * @return Valid {@link org.apache.rave.model.User}
     */
    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Gets the {@link User} that owns the page
     *
     * @return Valid {@link org.apache.rave.model.User}
     */
    @Override
    public String getContextId() {
        return contextId;
    }

    @Override
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    /**
     * Gets the {@link JpaPageLayout}
     *
     * @return Valid {@link JpaPageLayout}
     */
    @Override
    public PageLayout getPageLayout() {
        return pageLayout;
    }

    @Override
    public void setPageLayout(PageLayout pageLayout) {
        this.pageLayout = JpaConverter.getInstance().convert(pageLayout, PageLayout.class);
    }

    /**
     * Gets the widget containing {@link Region}s of the page
     *
     * @return Valid list of {@link Region}s
     */
    @Override
    @JsonManagedReference
    public List<Region> getRegions() {
        return ConvertingListProxyFactory.createProxyList(Region.class, regions);
    }

    @Override
    public void setRegions(List<Region> regions) {
        if(this.regions == null) {
            this.regions = new ArrayList<JpaRegion>();
        }
        this.getRegions().clear();
        if(regions != null) {
            for(Region region : regions) {
                region.setPage(this);
                this.getRegions().add(region);
            }
        }
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
    public Page getParentPage() {
        return parentPage;
    }

    @Override
    public void setParentPage(Page parentPage) {
        this.parentPage = JpaConverter.getInstance().convert(parentPage, Page.class);
    }

    @Override
    public List<Page> getSubPages() {
        // we need to manually sort the sub pages due to limitations in JPA's OrderBy annotation dealing with
        // sub-lists
        List<Page> orderedSubPages = null;
        if (this.subPages != null) {
            orderedSubPages = ConvertingListProxyFactory.createProxyList(Page.class, this.subPages);
            Collections.sort(orderedSubPages, new SubPageComparator());
        }
        return orderedSubPages;
    }

    @Override
    public void setSubPages(List<Page> subPages) {
        if(this.subPages == null) {
            this.subPages = new ArrayList<JpaPage>();
        }
        this.getSubPages().clear();

        if(subPages != null) {
            for(Page subPage : subPages) {
                subPage.setParentPage(this);
                this.getSubPages().add(subPage);
            }
        }
    }

    @Override
    @JsonManagedReference
    public List<PageUser> getMembers() {
        return ConvertingListProxyFactory.createProxyList(PageUser.class, members);
    }

    @Override
    public void setMembers(List<PageUser> members) {
        if (this.members == null) {
            this.members = new ArrayList<JpaPageUser>();
        }
        //Ensure that all operations go through the conversion proxy
        this.getMembers().clear();
        if (members != null) {
            this.getMembers().addAll(members);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaPage other = (JpaPage) obj;
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
        return "Page{" + "entityId=" + entityId + ", name=" + name + ", ownerId=" + ownerId + ", pageLayout=" + pageLayout + ", pageType=" + pageType + "}";
    }

    /**
     * Comparator used to sort sub pages.  It looks for PageUser objects representing the sub pages that are owned
     * by the parent page user, and uses the renderSequence as the sorting field
     */
    class SubPageComparator implements Comparator<Page> {
        @Override
        public int compare(Page o1, Page o2) {
            if (o1 == null || o1.getMembers() == null || o1.getMembers().isEmpty()) {
                return 1;
            }

            if (o2 == null || o2.getMembers() == null || o2.getMembers().isEmpty()) {
                return -1;
            }

            Long o1RenderSequence = null;
            Long o2RenderSequence = null;

            // find the PageUser object representing the sub page owned by the user
            for (PageUser pageUser : o1.getMembers()) {
                if (pageUser.getUserId().equals(o1.getOwnerId())) {
                    o1RenderSequence = pageUser.getRenderSequence();
                    break;
                }
            }

            // find the PageUser object representing the sub page owned by the user
            for (PageUser pageUser : o2.getMembers()) {
                if (pageUser.getUserId().equals(o2.getOwnerId())) {
                    o2RenderSequence = pageUser.getRenderSequence();
                    break;
                }
            }

            // compare the renderSequences of these two PageUser objects to determine the sort order
            return o1RenderSequence.compareTo(o2RenderSequence);
        }
    }
}
