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

package org.apache.rave.rest;

import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface RegionsResource {

    /*
    --- Region Operations
     */

    /**
     * Returns the regions associated with a page
     *
     * @return
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    SearchResult<Region> getPageRegions();

    /**
     * Creates a new page region
     *
     * @param region the definition of the region
     * @return
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Region createPageRegion( Region region);

    /**
     * Returns a particular region associated with a page
     *
     * @param regionId the region id
     * @return
     */
    @GET
    @Path("/{regionId}")
    @Produces(MediaType.APPLICATION_JSON)
    Region getPageRegion( @PathParam("regionId") String regionId);

    /**
     * Update a page region
     *
     * @param regionId the region id
     * @param region   the new region definition
     * @return
     */
    @PUT
    @Path("/{regionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Region updatePageRegion( @PathParam("regionId") String regionId, Region region);

    /**
     * Deletes a page region
     *
     * @param regionId the region id
     * @return
     */
    @DELETE
    @Path("/{regionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Region deletePageRegion( @PathParam("regionId") String regionId);

    /*
    --- RegionWidget Operations
     */

    /**
     * Delegates to the RegionWidgetsResource sub-resource
     *
     * @param regionId the region id
     * @return
     */
    @Path("/{regionId}/regionWidgets")
    RegionWidgetsResource getRegionWidgetsResource( @PathParam("regionId") String regionId);
}
