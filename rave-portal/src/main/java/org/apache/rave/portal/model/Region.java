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
 * A region of a page, which can contain widget instances {@link RegionWidget}
 */
@Entity
@Table(name="region")
@SequenceGenerator(name="regionIdSeq", sequenceName = "region_id_seq")
@Access(AccessType.FIELD)
public class Region {
    @Id @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regionIdSeq")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "region_id")
    private List<RegionWidget> regionWidgets;

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
     * Gets the ordered list of widget instances for the region
     *
     * @return Valid list
     */
    public List<RegionWidget> getRegionWidgets() {
        return regionWidgets;
    }

    public void setRegionWidgets(List<RegionWidget> regionWidgets) {
        this.regionWidgets = regionWidgets;
    }
}
