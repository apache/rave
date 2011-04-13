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
package org.apache.rave.portal.web.model;

import java.util.List;

import org.apache.rave.portal.web.model.util.InternationalString;

/**
 * A page, which consists of regions, and which may be owned by a user (or other principal)
 */
public interface Page {

    /**
     * Gets the persistence unique identifier
     *
     * @return id of the persisted object; null if not persisted
     */
    Long getId();

    void setId(Long id);

    /**
     * Get the name of the page that is displayed to the user
     *
     * @return  valid name
     */
    InternationalString getName();

    void setName(InternationalString name);

    /**
     * Get the principal that owns the page and the widgets on it
     *
     * @return valid principal
     */
    Person getOwner();

    void setOwner(Person owner);

    /**
     * Gets the order of the page instance in all pages for the owner
     *
     * @return valid, unique render sequence
     */
    long getRenderSeq();

    void setRenderSeq(long renderSeq);

    /**
     * Get the {@link PageLayout}
     *
     * @return valid layout
     */
    PageLayout getPageLayout();

    void setPageLayout(PageLayout layout);

    /**
     * Get the widget containing regions of the page
     *
     * @return valid list
     */
    List<Region> getRegions();

    void setRegions(List<Region> regions);
}
