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

package org.apache.rave.rest.filters;

import org.apache.cxf.jaxrs.ext.ResponseHandler;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.rave.rest.model.JsonResponseWrapper;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;

public class JsonWrapperResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (containerResponseContext.getStatus() == Response.Status.OK.getStatusCode()) {

            Object o = containerResponseContext.getEntity();
            JsonResponseWrapper wrapper;

            Class clazz = o.getClass();
            if (List.class.isAssignableFrom(clazz)) {
                wrapper = new JsonResponseWrapper((List) o);
            } else if (SearchResult.class.isAssignableFrom(clazz)) {
                wrapper = new JsonResponseWrapper((SearchResult) o);
            } else {
                wrapper = new JsonResponseWrapper(o);
            }

            containerResponseContext.setEntity(wrapper, containerResponseContext.getEntityAnnotations(), containerResponseContext.getMediaType());
        }
    }
}
