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

package org.apache.rave.rest.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.rest.PageTemplatesResource;
import org.apache.rave.rest.model.PageTemplate;
import org.apache.rave.rest.model.SearchResult;

import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Default JAXRS implementation of teh {@link PageTemplatesResource}
 */
public class DefaultPageTemplatesResource implements PageTemplatesResource {

    private PageTemplateRepository repository;

    @Override
    public SearchResult<PageTemplate> getAll() {
        return getListFromDb(repository.getAll());
    }

    @Override
    public SearchResult<PageTemplate> getAllForContext(String context) {
        return getListFromDb(repository.getAll(context));
    }

    @Override
    public PageTemplate get(String id) {
        return new PageTemplate(repository.get(id));
    }

    @Inject
    public void setRepository(PageTemplateRepository repository) {
        this.repository = repository;
    }

    private SearchResult<PageTemplate> getListFromDb(List<org.apache.rave.model.PageTemplate> fromDb) {
        List<PageTemplate> converted = Lists.newArrayList();
        for(org.apache.rave.model.PageTemplate template : fromDb) {
            converted.add(new PageTemplate(template));
        }
        return new SearchResult<PageTemplate>(converted, converted.size());
    }
}
