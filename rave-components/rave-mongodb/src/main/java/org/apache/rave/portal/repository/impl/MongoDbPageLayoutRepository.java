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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.MongoDbPageLayout;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.PAGE_LAYOUT_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class MongoDbPageLayoutRepository implements PageLayoutRepository {
    public static final Class<MongoDbPageLayout> CLASS = MongoDbPageLayout.class;

    @Autowired
    private MongoOperations template;

    @Override
    public PageLayout getByPageLayoutCode(String codename) {
        return template.findOne(new Query(where("code").is(codename)), CLASS, PAGE_LAYOUT_COLLECTION);
    }

    @Override
    public List<PageLayout> getAll() {
        return CollectionUtils.<PageLayout>toBaseTypedList(template.findAll(CLASS, PAGE_LAYOUT_COLLECTION));
    }

    @Override
    public List<PageLayout> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<PageLayout> getAllUserSelectable() {
        List<MongoDbPageLayout> userSelectable = template.find(new Query(where("userSelectable").is(true)), CLASS, PAGE_LAYOUT_COLLECTION);
        return CollectionUtils.<PageLayout>toBaseTypedList(userSelectable);
    }

    @Override
    public Class<? extends PageLayout> getType() {
        return CLASS;
    }

    @Override
    public PageLayout get(String id) {
        throw new UnsupportedOperationException("No use for an id");
    }

    @Override
    public PageLayout save(PageLayout item) {
        MongoDbPageLayout toSave = (MongoDbPageLayout)getByPageLayoutCode(item.getCode());
        if(toSave == null) {
            toSave = new MongoDbPageLayout();
        }
        update(item, toSave);
        template.save(toSave, PAGE_LAYOUT_COLLECTION);
        return toSave;
    }

    @Override
    public void delete(PageLayout item) {
        template.remove(getByPageLayoutCode(item.getCode()));
    }

    private void update(PageLayout source, PageLayout converted) {
        converted.setCode(source.getCode());
        converted.setNumberOfRegions(source.getNumberOfRegions());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setUserSelectable(source.isUserSelectable());
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }
}
