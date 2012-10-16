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

import org.apache.rave.portal.model.MongoDbPage;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 */
public class MongoPageTemplate implements MongoPageOperations {
    public static final String COLLECTION = "page";
    public static final Class<MongoDbPage> CLASS = MongoDbPage.class;

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, COLLECTION);
    }

    @Override
    public void remove(Query query) {
        mongoTemplate.remove(query, COLLECTION);
    }

    @Override
    public Page get(long id) {
        MongoDbPage fromDb = mongoTemplate.findById(id, CLASS, COLLECTION);
        if(fromDb == null) {
            throw new IllegalStateException("Could not find requested page: " + id);
        }
        return hydrate(fromDb);
    }

    @Override
    public Page save(Page item) {
        MongoDbPage converted = converter.convert(item, Page.class);
        mongoTemplate.save(converted, COLLECTION);
        converter.hydrate(converted, Page.class);
        return converted;
    }

    @Override
    public Page findOne(Query query) {
        return hydrate(mongoTemplate.findOne(query, CLASS, COLLECTION));
    }

    @Override
    public List<Page> find(Query query) {
        return hydrate(mongoTemplate.find(query, CLASS, COLLECTION));
    }

    private List<Page> hydrate(List<MongoDbPage> mongoDbPages) {
        for(MongoDbPage p : mongoDbPages) {
            hydrate(p);
        }
        return CollectionUtils.<Page>toBaseTypedList(mongoDbPages);
    }

    private Page hydrate(MongoDbPage page) {
        converter.hydrate(page, Page.class);
        return page;
    }
}
