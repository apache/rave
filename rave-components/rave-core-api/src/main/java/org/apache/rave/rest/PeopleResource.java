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

import org.apache.rave.model.FriendRequest;
import org.apache.rave.rest.model.Person;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/people")
public interface PeopleResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SearchResult<Person> getPeople();

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Person getPerson(@PathParam("id") String id);

    @GET
    @Path("/{id}/friends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFriends(@PathParam("id") String id);

    @GET
    @Path("/{id}/friends/{friendID}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFriend(@PathParam("id") String id, @PathParam("friendID") String friendID);

    @DELETE
    @Path("/{id}/friends/{friendID}")
    public Response deleteFriend(@PathParam("id") String id, @PathParam("friendID") String friendID);

    @GET
    @Path("/{id}/requests")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRequests(@PathParam("id") String id);

    @POST
    @Path("/{id}/requests")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createRequest(@PathParam("id") String id, FriendRequest request);

    @GET
    @Path("/{id}/requests/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRequest(@PathParam("id") String id, @PathParam("id") String requestID);

    @DELETE
    @Path("/{id}/requests/{id}")
    public Response deleteRequest(@PathParam("id") String id, @PathParam("id") String requestID);
}
