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

import org.apache.rave.model.PageTemplate;
import org.apache.rave.portal.repository.MongoPageTemplateOperations;
import org.apache.rave.portal.repository.PageTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 */
@Repository
public class MongoDbPageTemplateRepository implements PageTemplateRepository {

    @Autowired
    private MongoPageTemplateOperations template;

    @Override
    public List<PageTemplate> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<PageTemplate> getLimitedList(int offset, int limit) {
        return template.find(new Query().limit(limit).skip(offset));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query());
    }

    @Override
    public List<PageTemplate> getAll(String pageType) {
        return template.find(query(where("pageType").is(pageType.toUpperCase())));
    }

    @Override
    public PageTemplate getDefaultPage(String pageType) {
        return template.findOne(new Query(where("pageType").is(pageType.toUpperCase()).andOperator(where("defaultTemplate").is(true))));
    }

    @Override
    public Class<? extends PageTemplate> getType() {
        return PageTemplate.class;
    }

    @Override
    public PageTemplate get(String id) {
        return template.get(id);
    }

    @Override
    public PageTemplate save(PageTemplate pageTemplate) {
        return template.save(pageTemplate);
    }

    @Override
    public void delete(PageTemplate item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    public void setTemplate(MongoPageTemplateOperations template) {
        this.template = template;
    }
}
