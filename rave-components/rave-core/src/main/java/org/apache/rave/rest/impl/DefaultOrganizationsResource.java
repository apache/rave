/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.rest.impl;

import org.apache.rave.rest.OrganizationsResource;
import org.apache.rave.rest.model.Organization;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class DefaultOrganizationsResource implements OrganizationsResource {

    @Override
    public Response getOrganizations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getOrganization(@PathParam("id") String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updateOrganization(@PathParam("id") String id, Organization organization, @Context UriInfo uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response createOrganization(Organization organization) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
