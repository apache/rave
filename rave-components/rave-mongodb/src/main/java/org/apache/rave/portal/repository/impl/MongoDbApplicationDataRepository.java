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
import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.apache.rave.portal.repository.ApplicationDataRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.APP_DATA_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbApplicationDataRepository implements ApplicationDataRepository {
    public static final Class<ApplicationDataImpl> CLASS = ApplicationDataImpl.class;

    @Autowired
    private MongoOperations template;

    @Override
    public List<ApplicationData> getApplicationData(List<String> userIds, String appId) {
        return CollectionUtils.<ApplicationData>toBaseTypedList(template.find(query(where("appUrl").is(appId).andOperator(where("userId").in(userIds))), CLASS, APP_DATA_COLLECTION));
    }

    @Override
    public ApplicationData getApplicationData(String personId, String appId) {
        return template.findOne(query(where("appUrl").is(appId).andOperator(where("userId").is(personId))), CLASS, APP_DATA_COLLECTION);
    }

    @Override
    public Class<? extends ApplicationData> getType() {
        return CLASS;
    }

    @Override
    public ApplicationData get(String id) {
        return template.findById(id, CLASS, APP_DATA_COLLECTION);
    }

    @Override
    public ApplicationData save(ApplicationData item) {
        template.save(item, APP_DATA_COLLECTION);
        return item;
    }

    @Override
    public void delete(ApplicationData item) {
        template.remove(item, APP_DATA_COLLECTION);
    }

    @Override
    public List<ApplicationData> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<ApplicationData> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }
}
