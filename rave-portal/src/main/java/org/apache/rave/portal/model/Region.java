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
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

/**
 * A region of a page, which can contain widget instances {@link RegionWidget}
 */
@Entity
@Table(name="region")
@SequenceGenerator(name="regionIdSeq", sequenceName = "region_id_seq")
@Access(AccessType.FIELD)
public class Region implements BasicEntity {
    @Id @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regionIdSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("renderOrder")
    @JoinColumn(name = "region_id")
    private List<RegionWidget> regionWidgets;

    public Region() {
    }

    public Region(Long id) {
        this.id = id;
    }

    public Region(Long id, Page page) {
        this.id = id;
        this.page = page;
    }

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
     * Gets the associated page
     *
     * @return the associated page
     */
    @JsonBackReference
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * Gets the ordered list of widget instances for the region
     *
     * @return Valid list
     */
    @JsonManagedReference
    public List<RegionWidget> getRegionWidgets() {
        return regionWidgets;
    }

    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        this.regionWidgets = regionWidgets;
    }
}
