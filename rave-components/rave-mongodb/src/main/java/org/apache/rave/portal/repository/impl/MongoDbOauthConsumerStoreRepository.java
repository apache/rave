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
import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.portal.model.impl.OAuthConsumerStoreImpl;
import org.apache.rave.portal.repository.OAuthConsumerStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.OAUTH_CONSUMER_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbOauthConsumerStoreRepository implements OAuthConsumerStoreRepository {
    public static final Class<OAuthConsumerStoreImpl> CLASS = OAuthConsumerStoreImpl.class;

    @Autowired
    private MongoOperations template;

    @Override
    public OAuthConsumerStore findByUriAndServiceName(String gadgetUri, String serviceName) {
        return template.findOne(query(where("gadgetUri").is(gadgetUri).andOperator(where("serviceName").is(serviceName))), CLASS, OAUTH_CONSUMER_COLLECTION);
    }

    @Override
    public Class<? extends OAuthConsumerStore> getType() {
        return CLASS;
    }

    @Override
    public OAuthConsumerStore get(String id) {
        return template.findById(id, CLASS, OAUTH_CONSUMER_COLLECTION);
    }

    @Override
    public OAuthConsumerStore save(OAuthConsumerStore item) {
        template.save(item, OAUTH_CONSUMER_COLLECTION);
        return item;
    }

    @Override
    public void delete(OAuthConsumerStore item) {
        template.remove(get(item.getId()));
    }

    @Override
    public List<OAuthConsumerStore> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<OAuthConsumerStore> getLimitedList(int offset, int limit) {
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
