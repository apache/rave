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

import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 */
@Repository
public class MongoDbUserRepository implements UserRepository {

    public static final String COLLECTION = "person";
    public static final Class<MongoDbUser> CLAZZ = MongoDbUser.class;

    @Autowired
    private MongoOperations template;

    @Autowired
    private HydratingConverterFactory converter;

    @Override
    public User getByUsername(String username) {
        return hydrate(template.findOne(query(where("username").is(username)), CLAZZ, COLLECTION));
    }

    @Override
    public User getByUserEmail(String userEmail) {
        return hydrate(template.findOne(query(where("email").is(userEmail)), CLAZZ, COLLECTION));
    }

    @Override
    public User getByOpenId(String openId) {
        return hydrate(template.findOne(query(where("openId").is(openId)), CLAZZ, COLLECTION));
    }

    @Override
    public List<User> getLimitedList(int offset, int pageSize) {
        Query query = new Query().skip(offset).limit(pageSize);
        return hydrate(template.find(query, CLAZZ, COLLECTION));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query(), COLLECTION);
    }

    @Override
    public List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize) {
        return hydrate(template.find(getSearchQuery(searchTerm).skip(offset).limit(pageSize), CLAZZ, COLLECTION));
    }

    @Override
    public int getCountByUsernameOrEmail(String searchTerm) {
        return (int)template.count(getSearchQuery(searchTerm), COLLECTION);
    }

    @Override
    public List<User> getAllByAddedWidget(long widgetId) {
        return null;  //TODO COMPLETE
    }

    @Override
    public User getByForgotPasswordHash(String hash) {
        return hydrate(template.findOne(query(where("forgotPasswordHash").is(hash)), CLAZZ, COLLECTION));
    }

    @Override
    public Class<? extends User> getType() {
        return MongoDbUser.class;
    }

    @Override
    public User get(long id) {
        return hydrate(template.findById(id, CLAZZ, COLLECTION));
    }

    @Override
    public User save(User item) {
        MongoDbUser converted = converter.convert(item, User.class);
        template.save(converted, COLLECTION);
        return hydrate(converted);
    }

    @Override
    public void delete(User item) {
        template.remove(get(item.getId()), COLLECTION);
    }

    private User hydrate(MongoDbUser user) {
        converter.hydrate(user, User.class);
        return user;
    }

    private List<User> hydrate(List<MongoDbUser> userList) {
        for(MongoDbUser user : userList) {
            converter.hydrate(user, User.class);
        }
        return CollectionUtils.<User>toBaseTypedList(userList);
    }

    private Query query(Criteria criteria) {
        return new Query(criteria);
    }

    private Query getSearchQuery(String searchTerm) {
        return query(where("username").is(searchTerm).orOperator(where("email").is(searchTerm)));
    }
}
