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

import org.apache.rave.portal.model.MongoDbPortalPreference;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.apache.rave.portal.repository.util.CollectionNames.PREFERENCE_COLLECTION;

@Repository
public class MongoDbPortalPreferenceRepository implements PortalPreferenceRepository {


    public static final Class<PortalPreferenceImpl> CLASS = PortalPreferenceImpl.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public List<PortalPreference> getAll() {
        return CollectionUtils.<PortalPreference>toBaseTypedList(template.findAll(CLASS, PREFERENCE_COLLECTION));
    }

    @Override
    public List<PortalPreference> getLimitedList(int offset, int pageSize) {
        Query q = new Query().skip(offset).limit(pageSize);
        return CollectionUtils.<PortalPreference>toBaseTypedList(template.find(q, CLASS, PREFERENCE_COLLECTION));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query(), CLASS);
    }

    @Override
    public PortalPreference getByKey(String key) {
        return template.findOne(query(where("key").is(key)), CLASS, PREFERENCE_COLLECTION);
    }

    @Override
    public Class<? extends PortalPreference> getType() {
        return CLASS;
    }

    @Override
    public PortalPreference get(String id) {
        return template.findById(id, CLASS, PREFERENCE_COLLECTION);
    }

    @Override
    public PortalPreference save(PortalPreference item) {
        PortalPreference fromDb = getByKey(item.getKey());
        MongoDbPortalPreference converted = converter.convert(item, PortalPreference.class);
        if(fromDb != null) {
            converted.setId(((MongoDbPortalPreference)fromDb).getId());
        }
        template.save(converted, PREFERENCE_COLLECTION);
        converter.hydrate(converted, PortalPreference.class);
        return converted;
    }

    @Override
    public void delete(PortalPreference item) {
        template.remove(getByKey(item.getKey()), PREFERENCE_COLLECTION);
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }

    public void setConverter(HydratingConverterFactory converter) {
        this.converter = converter;
    }
}
