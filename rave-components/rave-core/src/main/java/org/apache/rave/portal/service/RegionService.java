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
package org.apache.rave.portal.service;

import org.apache.rave.model.Region;
import org.apache.rave.rest.model.SearchResult;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RegionService {

     /**
      * Register a new region
      * @param region the region object to register
      */
     @PreAuthorize("hasPermission(#region.id, 'org.apache.rave.model.Region', 'create')")
     void registerNewRegion(Region region);

    //TODO: Put correct spring security annotations on following three methods (getAll, getLimitedList, getCountAll)
    /**
     * Gets a {@link org.apache.rave.rest.model.SearchResult} for {@link Region}'s that a user can add to their context
     * <p/>
     * May return a very large resultset
     *
     * @return SearchResult
     */
    SearchResult<Region> getAll();


    /**
     * Gets a limited {@link org.apache.rave.rest.model.SearchResult} for {@link Region}'s that a user can add to their
     * context.
     *
     * @param offset   start point within the resultset (for paging)
     * @param pageSize maximum number of items to be returned (for paging)
     * @return SearchResult
     */
    SearchResult<Region> getLimitedList(int offset, int pageSize);
}
