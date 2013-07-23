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

import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.RegionWidget;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pages")
public interface PagesResource {

    /*
    --- Page operations
     */

    /**
     * Returns a list of pages
     *
     * @return
     */
    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    Response getPages();

    /**
     * Creates a new page
     *
     * @param page the definition of the new page
     * @return
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response createPage(Page page);

    /**
     * Deletes the given page
     *
     * @param pageId ID of the page on which the operation is to take place
     * @return
     */
    @DELETE
    @Path("/{pageId}")
    Response deletePage(@PathParam("pageId") String pageId);

    /**
     * Returns the given page
     *
     *
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @GET
    @Path("/{pageId}")
    @Produces(MediaType.APPLICATION_JSON)
    Page getPage(@PathParam("pageId") String id);

    /**
     * Updates the given page
     *
     * @param pageId ID of the page on which the operation is to take place
     * @param page   the new definition of the page
     * @return
     */
    @PUT
    @Path("/{pageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updatePage(@PathParam("pageId") String pageId, Page page);

    /**
     * Returns the OMDL representation of the page
     *
     * @param pageId ID of the page on which the operation is to take place
     * @return
     */
    @GET
    @Path("/{pageId}")
    @Produces({"application/vnd.omdl+xml"})
    Response getPageOmdl(@PathParam("pageId") String pageId);

    /*
    --- Region Operations
     */

    /**
     * Returns the regions associated with a page
     *
     * @param pageId the page id
     * @return
     */
    @GET
    @Path("/{pageId}/regions")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPageRegions(@PathParam("pageId") String pageId);

    /**
     * Creates a new page region
     *
     * @param pageId the page id
     * @param region the definition of the region
     * @return
     */
    @POST
    @Path("/{pageId}/regions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createPageRegion(@PathParam("pageId") String pageId, Region region);

    /**
     * Returns a particular region associated with a page
     *
     * @param pageId   the page id
     * @param regionId the region id
     * @return
     */
    @GET
    @Path("/{pageId}/regions/{regionId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPageRegion(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId);

    /**
     * Update a page region
     *
     * @param pageId   the page id
     * @param regionId the region id
     * @param region   the new region definition
     * @return
     */
    @PUT
    @Path("/{pageId}/regions/{regionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updatePageRegion(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId, Region region);

    /**
     * Deletes a page region
     *
     * @param pageId   the id of the page
     * @param regionId the region id
     * @return
     */
    @DELETE
    @Path("/{pageId}/regions/{regionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response deletePageRegion(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId);

    /*
    --- RegionWidget Operations
     */

    /**
     * Returns the regionWidgets associated with a page and a region
     *
     * @param pageId   the page id
     * @param regionId the region id
     * @return
     */
    @GET
    @Path("/{pageId}/regions/{regionId}/regionWidgets")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPageRegionRegionWidgets(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId);

    @POST
    @Path("/{pageId}/regions/{regionId}/regionWidgets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createPageRegionRegionWidget(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId,
                                          RegionWidget regionWidget);

    /**
     * Returns a regionWidget associated with a page and a region
     *
     * @param pageId         the page id
     * @param regionId       the region id
     * @param regionWidgetId the regionWidget id
     * @return
     */
    @GET
    @Path("/{pageId}/regions/{regionId}/regionWidgets/{regionWidgetId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPageRegionRegionWidget(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId,
                                       @PathParam("regionWidgetId") String regionWidgetId);

    /**
     * Updates a regionWidget associated with a page and a region
     *
     * @param pageId         the page id
     * @param regionId       the region id
     * @param regionWidgetId the regionWidget id
     * @param regionWidget   the new regionWidget definition
     * @return
     */
    @PUT
    @Path("/{pageId}/regions/{regionId}/regionWidgets/{regionWidgetId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updatePageRegionRegionWidget(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId,
                                          @PathParam("regionWidgetId") String regionWidgetId, RegionWidget regionWidget);

    /**
     * Deletes a regionWidget from a page region
     *
     * @param pageId         the page id
     * @param regionId       the region id
     * @param regionWidgetId the regionWidget id
     * @return
     */
    @DELETE
    @Path("/{pageId}/regions/{regionId}/regionWidgets/{regionWidgetId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response deletePageRegionRegionWidget(@PathParam("pageId") String pageId, @PathParam("regionId") String regionId,
                                          @PathParam("regionWidgetId") String regionWidgetId);
}
