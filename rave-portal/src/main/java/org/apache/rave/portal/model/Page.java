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

import javax.persistence.*;
import java.util.List;

/**
 * A page, which consists of regions, and which may be owned by a {@link User} (note the ownership will likely need to
 * become more flexible to enable things like group ownership in the future).
 */
@Entity
@Table(name="page")
@SequenceGenerator(name="pageIdSeq", sequenceName = "page_id_seq")
@NamedQueries({
        @NamedQuery(name = "Page.getByUserId", query="SELECT p FROM Page p WHERE p.owner.userId = :userId")
})
@Access(AccessType.FIELD)
public class Page {
    @Id @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pageIdSeq")
    private Long id;

    @Basic @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Basic @Column(name="render_sequence")
    private Long renderSequence;

    @ManyToOne
    @JoinColumn(name="page_layout_id")
    private PageLayout pageLayout;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="page_id")
    private List<Region> regions;

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
}