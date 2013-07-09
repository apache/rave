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
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.PAGE_TEMPLATE_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 */
@Repository
public class MongoDbPageTemplateRepository implements PageTemplateRepository {

    @Autowired
    private HydratingConverterFactory converter;

    @Autowired
    private MongoOperations template;

    @Override
    public List<PageTemplate> getAll() {
        List<MongoDbPageTemplate> templates = template.findAll(MongoDbPageTemplate.class, PAGE_TEMPLATE_COLLECTION);
        for(MongoDbPageTemplate temp : templates) {
            converter.hydrate(temp, PageTemplate.class);
        }
        return CollectionUtils.<PageTemplate>toBaseTypedList(templates);
    }

    @Override
    public PageTemplate getDefaultPage(PageType pageType) {
        PageTemplate temp = template.findOne(new Query(where("pageType").is(pageType.getPageType().toUpperCase()).andOperator(where("defaultTemplate").is(true))), MongoDbPageTemplate.class, PAGE_TEMPLATE_COLLECTION);
        converter.hydrate(temp, PageTemplate.class);
        return temp;
    }

    @Override
    public PageTemplate save(PageTemplate pageTemplate) {
        MongoDbPageTemplate converted = converter.convert(pageTemplate, PageTemplate.class);
        template.save(converted, PAGE_TEMPLATE_COLLECTION);
        converter.hydrate(converted, PageTemplate.class);
        return converted;
    }

    public void setConverter(HydratingConverterFactory converter) {
        this.converter = converter;
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }
}
