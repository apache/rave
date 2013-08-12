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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/preferences")
public interface PortalPreferenceResource {

    /*
    --- Preference operations
     */

    /**
     * Returns a list of preferences
     *
     * @return
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    SearchResult<PortalPreference> getPreferences();

    /**
     * Creates a new page
     *
     * @param preference the definition of the new page
     * @return
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PortalPreference updatePreferences(PortalPreference preference);

    /**
     * Returns the given page
     *
     * @param id ID of the preference on which the operation is to take place
     * @return
     */
    @GET
    @Path("/{preferenceId}")
    @Produces(MediaType.APPLICATION_JSON)
    PortalPreference getPreference(@PathParam("preferenceId") String id);

}
