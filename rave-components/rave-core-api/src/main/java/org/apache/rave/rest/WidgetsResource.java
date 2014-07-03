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

import org.apache.rave.rest.model.Widget;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Widget REST API
 */
@Path("/widgets")
public interface WidgetsResource {

    /**
     * Gets all widgets in the system
     * @return a list of all widgets
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResult<Widget> getWidgets();

    /**
     * Gets a widget by its ID
     * @param id ID of the widget
     * @return a valid widget or null if not found
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Widget getWidget(@PathParam("id") String id);

    /**
     * Updates the widget in system
     * @param id ID of the widget
     * @param Widget The widget to use to update all properties
     * @return the updated widget from the system
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Widget updateWidget(@PathParam("id") String id, Widget Widget);

    /**
     * Creates a new widget in the system
     * @param Widget the widget to create
     * @return the new widget from the system
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Widget createWidget(Widget Widget);
    
}
