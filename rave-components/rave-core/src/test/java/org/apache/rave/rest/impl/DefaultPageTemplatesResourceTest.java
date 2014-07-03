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
import org.apache.rave.model.PageTemplate;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.rest.PageTemplatesResource;
import org.apache.rave.rest.model.SearchResult;
import org.junit.Before;

import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultPageTemplatesResourceTest {

    private PageTemplatesResource resource;
    private PageTemplateRepository repository;

    @Before
    public void setup() {
        resource = new DefaultPageTemplatesResource();
        repository = createMock(PageTemplateRepository.class);
        ((DefaultPageTemplatesResource)resource).setRepository(repository);
    }

    public void getAll() {
        int count = 4;
        List<PageTemplate> answer = getRepositoryTemplates(count);
        expect(repository.getAll()).andReturn(answer);
        replay(repository);

        SearchResult< org.apache.rave.rest.model.PageTemplate> result = resource.getAll();
        assertThat(result.getNumberOfPages(), is(equalTo(1)));
        assertThat(result.getCurrentPage(), is(equalTo(0)));
        assertThat(result.getTotalResults(), is(equalTo(1)));
    }

    public void getAllForContext() {
        int count = 4;
        String context = "context";
        List<PageTemplate> answer = getRepositoryTemplates(count);
        expect(repository.getAll(context)).andReturn(answer);
        replay(repository);

        SearchResult< org.apache.rave.rest.model.PageTemplate> result = resource.getAllForContext(context);
        assertThat(result.getNumberOfPages(), is(equalTo(1)));
        assertThat(result.getCurrentPage(), is(equalTo(0)));
        assertThat(result.getTotalResults(), is(equalTo(1)));
    }

    private List<PageTemplate> getRepositoryTemplates(int i) {
        List<PageTemplate> templates = Lists.newArrayList();
        for(int j=0; j<i; j++) {
            templates.add(new PageTemplateImpl());
        }
        return templates;
    }


}
