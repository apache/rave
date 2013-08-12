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
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.OAUTH_TOKEN_COLLECTION;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbOAuthTokenInfoRepository implements OAuthTokenInfoRepository {

    public static final Class<OAuthTokenInfoImpl> CLASS = OAuthTokenInfoImpl.class;

    @Autowired
    private MongoOperations template;

    @Override
    public OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId, String tokenName, String serviceName) {
        return template.findOne(
                query(where("userId").is(userId)
                .andOperator(where("appUrl").is(appUrl))
                .andOperator(where("moduleId").is(moduleId))
                .andOperator(where("tokenName").is(tokenName))
                .andOperator(where("serviceName").is(serviceName))
        ), CLASS, OAUTH_TOKEN_COLLECTION);
    }

    @Override
    public Class<? extends OAuthTokenInfo> getType() {
        return CLASS;
    }

    @Override
    public OAuthTokenInfo get(String id) {
        return template.findById(id, CLASS, OAUTH_TOKEN_COLLECTION);
    }

    @Override
    public OAuthTokenInfo save(OAuthTokenInfo item) {
        template.save(item, OAUTH_TOKEN_COLLECTION);
        return item;
    }

    @Override
    public void delete(OAuthTokenInfo item) {
        template.remove(get(item.getId()));
    }

    @Override
    public List<OAuthTokenInfo> getAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<OAuthTokenInfo> getLimitedList(int offset, int limit) {
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
