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
import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.MongoDbAuthority;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.apache.rave.portal.repository.util.CollectionNames.AUTHORITY_COLLECTION;

@Repository
public class MongoDbAuthorityRepository implements AuthorityRepository {

    public static final Class<MongoDbAuthority> CLASS = MongoDbAuthority.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public Authority getByAuthority(String authorityName) {
        return template.findOne(query(where("authority").is(authorityName)), CLASS, AUTHORITY_COLLECTION);
    }

    @Override
    public List<Authority> getAll() {
        return CollectionUtils.<Authority>toBaseTypedList(template.findAll(CLASS, AUTHORITY_COLLECTION));
    }

    @Override
    public List<Authority> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<Authority> getAllDefault() {
        return CollectionUtils.<Authority>toBaseTypedList(template.find(query(where("defaultForNewUser").is(true)), CLASS, AUTHORITY_COLLECTION));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query(), AUTHORITY_COLLECTION);
    }

    @Override
    public Class<? extends Authority> getType() {
        return AuthorityImpl.class;
    }

    @Override
    public Authority get(String id) {
        throw new NotSupportedException();
    }

    @Override
    public Authority save(Authority item) {
        Authority fromDb = getByAuthority(item.getAuthority());
        Authority save;
        if(fromDb == null) {
            save = converter.convert(item, Authority.class);
        } else {
            fromDb.setDefaultForNewUser(item.isDefaultForNewUser());
            save=fromDb;
        }
        template.save(save, AUTHORITY_COLLECTION);
        return save;
    }

    @Override
    public void delete(Authority item) {
        template.remove(getByAuthority(item.getAuthority()), AUTHORITY_COLLECTION);
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }

    public void setConverter(HydratingConverterFactory converter) {
        this.converter = converter;
    }

    public MongoOperations getTemplate() {
        return template;
    }

    public HydratingConverterFactory getConverter() {
        return converter;
    }
}
