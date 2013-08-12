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

import org.apache.rave.rest.model.PageUser;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface PageUsersResource {

    /**
     * Returns the regions associated with a page
     *
     * @return
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    SearchResult<PageUser> getPageUsers();

    /**
     * Creates a new page region
     *
     * @param user the new page user definition
     * @return
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PageUser createPageUser(PageUser user);

    /**
     * Returns a particular region associated with a page
     *
     * @param userId the page user's id
     * @return
     */
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    PageUser getPageUser(@PathParam("userId") String userId);

    /**
     * Update a page region
     *
     * @param userId the page user's id
     * @param user   the new page user definition
     * @return
     */
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PageUser updatePageUser(@PathParam("userId") String userId, PageUser user);

    /**
     * Deletes a page region
     *
     * @param userId the page user's id
     * @return
     */
    @DELETE
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PageUser deletePageUser(@PathParam("userId") String userId);
}
