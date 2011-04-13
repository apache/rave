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

/**
 * Represents an organization of regions within a page that is supported by the
 * rendering engine
 */
public interface PageLayout {

    /**
     * Gets the persistence unique identifier
     *
     * @return id of the persisted object; null if not persisted
     */
    Long getId();

    void setId(Long id);

    /**
     * Gets the code used by the rendering engine to identify the page layout
     *
     * @return valid code known by rendering engine
     */
    String getCode();

    void setCode(String code);

    /**
     * Gets the display name shown to users
     *
     * @return valid name
     */
    String getName();

    void setName(String name);

    /**
     * Gets the number of regions supported by this page layout
     *
     * @return valid number of regions > 0
     */
    long getNumRegions();

    void setNumRegions(long numRegions);
}
