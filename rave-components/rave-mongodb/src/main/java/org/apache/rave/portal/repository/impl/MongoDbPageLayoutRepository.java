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

import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class MongoDbPageLayoutRepository implements PageLayoutRepository {

    public static final String COLLECTION = "pageLayout";

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public PageLayout getByPageLayoutCode(String codename) {
        return template.findOne(new Query(where("code").is(codename)), PageLayoutImpl.class, COLLECTION);
    }

    @Override
    public List<PageLayout> getAll() {
        return CollectionUtils.<PageLayout>toBaseTypedList(template.findAll(PageLayoutImpl.class, COLLECTION));
    }

    @Override
    public List<PageLayout> getAllUserSelectable() {
        List<PageLayoutImpl> userSelectable = template.find(new Query(where("userSelectable").is(true)), PageLayoutImpl.class, COLLECTION);
        return CollectionUtils.<PageLayout>toBaseTypedList(userSelectable);
    }

    @Override
    public Class<? extends PageLayout> getType() {
        return PageLayoutImpl.class;
    }

    @Override
    public PageLayout get(long id) {
        throw new NotImplementedException("No use for an id");
    }

    @Override
    public PageLayout save(PageLayout item) {
        template.save(converter.convert(item, PageLayout.class), COLLECTION);
        return item;
    }

    @Override
    public void delete(PageLayout item) {
        template.remove(getByPageLayoutCode(item.getCode()));
    }
}
