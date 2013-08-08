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
import org.apache.rave.rest.model.SearchResult;

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
    @Produces(MediaType.APPLICATION_JSON)
    SearchResult<Page> getPages();

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
    Page createPage(Page page);

    /**
     * Deletes the given page
     *
     * @param pageId ID of the page on which the operation is to take place
     * @return
     */
    @DELETE
    @Path("/{pageId}")
    Page deletePage(@PathParam("pageId") String pageId);

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
    Page updatePage(@PathParam("pageId") String pageId, Page page);

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

    /**
     * Delegates to the RegionsResource sub-resource.
     *
     * @param pageId the page id
     * @return
     */
    @Path("/{pageId}/regions")
    RegionsResource getRegionsResource(@PathParam("pageId") String pageId);

    /**
     * Delegates to the RegionsResource sub-resource.
     *
     * @param pageId the page id
     * @return
     */
    @Path("/{pageId}/members")
    PageUsersResource getPageMembersResource(@PathParam("pageId") String pageId);
}
