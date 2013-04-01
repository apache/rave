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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/pages")
public interface PagesResource {

    /**
     * Deletes the given page
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @DELETE
    @Path("/{id}")
    Response deletePage(@PathParam("id") String id);

    /**
     * Returns the given page
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getPage(@PathParam("id") String id);

    /**
     * Updates the given page
     * @param id ID of the page on which the operation is to take place
     * @param page the new definition of the page
     * @return
     */
    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response updatePage(@PathParam("id") String id, Page page);

    /**
     * Returns the OMDL representation of the page
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces({"application/vnd.omdl+xml"})
    Response getPageOmdl(@PathParam("id") String id);


    /**
     * Gets the render-ready page definitions for the given context & identifier
     *
     * @param context the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc)
     * @return
     */
    @GET
    @Path("/{context}/{identifier}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getPagesForRender(@PathParam("context") String context, @PathParam("identifier") String identifier);


    /**
     * Gets the render-ready page definition for the given context, identifier & page id
     *
     * @param context the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc)
     * @return
     */
    @GET
    @Path("/{context}/{identifier}/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response getPageForRender(@PathParam("context") String context,
                              @PathParam("identifier") String identifier,
                              @PathParam("id") String id);

    /**
     *
     * Clones the specific page for the specified user
     *
     * @param context the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc) to clone the page to
     * @param id the page to clone to the new context identifier
     * @return
     */
    @POST
    @Path("/{context}/{identifier}/{id}/clone")
    Response clonePage(@PathParam("context") String context,
                       @PathParam("identifier") String identifier,
                       @PathParam("id") String id);

    /**
     * Creates a new page
     * @param context the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc) to clone the page to
     * @param page the OMDL page to create
     * @return
     */
    @POST
    @Path("/{context}/{identifier}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({"application/vnd.omdl+xml"})
    Response importOmdlPage(@PathParam("context") String context,
                            @PathParam("identifier") String identifier,
                            @Multipart(value = "root", type = "application/octet-stream") File page);

    /**
     * Creates a new page
     * @param context the context under which the operation takes place (portal, profile, etc)
     * @param identifier the target context identifier (username, group, etc) to clone the page to
     * @param page the page to create
     * @return
     */
    @POST
    @Path("/{context}/{identifier}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response createPage(@PathParam("context") String context,
                        @PathParam("identifier") String identifier,
                        Page page);

    /**
     * Deletes the given page
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @DELETE
    @Path("/{context}/{identifier}/{id}")
    Response deletePageInContext(@PathParam("id") String id);

    /**
     * Updates the given page
     * @param id ID of the page on which the operation is to take place
     * @param page the new definition of the page
     * @return
     */
    @PUT
    @Path("/{context}/{identifier}/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response updatePageInContext(@PathParam("id") String id, Page page);

    /**
     * Modifies the page's render order for the current user
     * @param id ID of the page on which the operation is to take place
     * @param moveAfterPageId ID of the page to move after
     * @return
     */
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{context}/{identifier}/{id}/move")
    Response movePage(@PathParam("id") String id, @QueryParam("moveAfterPageId") final String moveAfterPageId);


    /**
     * Adds the specified widget to the first region on the page
     * @param id  ID of the page on which the operation is to take place
     * @param widget the widget to add.  widgetId is the only required property
     * @return
     */
    @POST
    @Path("/{context}/{identifier}/{id}/regionWidgets")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response addWidgetToPage(@PathParam("id") String id, RegionWidget widget);

    /**
     * Removes the widget from the page
     * @param id  ID of the page on which the operation is to take place
     * @param regionWidgetId ID of the regionWidget to move
     * @return
     */
    @DELETE
    @Path("/{context}/{identifier}/{id}/regionWidgets/{regionWidgetId}")
    Response removeWidgetFromPage(@PathParam("id") String id, @PathParam("regionWidgetId") String regionWidgetId);

    /**
     * Adds the specified widget to the given region on the page
     * @param id  ID of the page on which the operation is to take place
     * @param regionId the target Region ID
     * @param widget the widget to add.  widgetId is the only required property
     * @return
     */
    @POST
    @Path("/{context}/{identifier}/{id}/regions/{regionId}/regionWidgets")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response addWidgetToRegion(@PathParam("id") String id, @PathParam("regionId") String regionId, RegionWidget widget);

    /**
     * Moves the region widget to the specified Region
     * @param id ID of the page on which the operation is to take place
     * @param toRegionId the target Region ID
     * @param regionWidgetId ID of the regionWidget to move
     * @param position the position of the widget in the new region
     * @return
     */
    @PUT
    @Path("/{context}/{identifier}/{id}/regions/{toRegionId}/regionWidgets/{regionWidgetId}/move")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response moveWidgetOnPage(@PathParam("id") String id,
                              @PathParam("toRegionId") String toRegionId,
                              @PathParam("regionWidgetId") String regionWidgetId,
                              @QueryParam("position") int position);

    /**
     * Moves the region widget to the specified Region
     * @param targetPageId ID of the page to move to
     * @param regionWidgetId ID of the regionWidget to move
     * @return
     */
    @PUT
    @Path("/{context}/{identifier}/{targetPageId}/regionWidgets/{regionWidgetId}/move")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response moveWidgetToPage(@PathParam("targetPageId") String targetPageId,
                              @PathParam("regionWidgetId") String regionWidgetId);
    /**
     * Adds a new member to the page
     * @param id ID of the page on which the operation is to take place
     * @return
     */
    @POST
    @Path("/{context}/{identifier}/{id}/members")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response addMemberToPage(@PathParam("id") String id);

    @DELETE
    @Path("/{context}/{identifier}/{id}/members/{userId}")
    Response removeMemberFromPage(@PathParam("id") String id, @PathParam("userId") String userId);

    @PUT
    @Path("/{context}/{identifier}/{id}/members/{userId}/status")
    Response updateSharedPageStatus(@PathParam("id") String id, @PathParam("userId") String userId, String status);

    @PUT
    @Path("/{context}/{identifier}/{id}/members/{userId}/editor")
    Response updatePageEditingStatus(@PathParam("id") String id, @PathParam("userId") String userId, boolean editor);
}
