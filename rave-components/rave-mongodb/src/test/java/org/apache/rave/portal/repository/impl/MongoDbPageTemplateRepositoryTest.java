/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.MongoDbPageTemplate;
import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageType;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.repository.MongoPageTemplateOperations;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.apache.rave.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * User: DSULLIVAN
 * Date: 12/5/12
 * Time: 2:46 PM
 */
public class MongoDbPageTemplateRepositoryTest {

    private MongoDbPageTemplateRepository templateRepository;
    private MongoPageTemplateOperations template;

    @Before
    public void setup(){
        templateRepository = new MongoDbPageTemplateRepository();
        template = createMock(MongoPageTemplateOperations.class);
        templateRepository.setTemplate(template);
    }

    @Test
    public void getAll_Valid() {
        List<PageTemplate> templates = new ArrayList<PageTemplate>();
        PageTemplate temp = new MongoDbPageTemplate();
        templates.add((MongoDbPageTemplate)temp);
        expect(template.find(new Query())).andReturn(templates);
        replay(template);

        List<PageTemplate> returned = templateRepository.getAll();
        assertThat(returned, is(sameInstance(CollectionUtils.<PageTemplate>toBaseTypedList(templates))));
    }

    @Test
    public void getDefaultPage_Valid(){
        PageType pageType = PageType.get("user");
        MongoDbPageTemplate found = new MongoDbPageTemplate();
        expect(template.findOne(new Query(where("pageType").is(pageType.getPageType().toUpperCase()).andOperator(where("defaultTemplate").is(true))))).andReturn(found);
        replay(template);

        PageTemplate returned = templateRepository.getDefaultPage(pageType.toString());

        assertThat((MongoDbPageTemplate)returned, is(sameInstance(found)));
    }

    @Test
    public void save_Valid(){
        PageTemplate pageTemplate = new PageTemplateImpl();
        MongoDbPageTemplate converted = new MongoDbPageTemplate();

        expect(template.save(eq(converted))).andReturn(converted);
        replay(template);

        PageTemplate saved = templateRepository.save(pageTemplate);
        assertThat(converted, is(sameInstance(saved)));
    }
}
