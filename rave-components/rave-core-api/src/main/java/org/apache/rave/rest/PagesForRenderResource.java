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

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.RegionWidget;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/pages/render")
public interface PagesForRenderResource {

    /**
     * Gets the render-ready page definitions for the given context & identifier
     *
     * @param context    the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc)
     * @return
     */
    @GET
    @Path("/{context}/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    SearchResult<Page> getPagesForRender(@PathParam("context") String context, @PathParam("identifier") String identifier);

    /**
     * Gets the render-ready page definition for the given context, identifier & page id
     *
     * @param context    the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc)
     * @return
     */
    @GET
    @Path("/{context}/{identifier}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Page getPageForRender(@PathParam("context") String context,
                              @PathParam("identifier") String identifier,
                              @PathParam("id") String id);
}
