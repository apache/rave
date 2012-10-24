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

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.MongoUserOperations;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


/**
 */
@Repository
public class MongoDbUserRepository implements UserRepository {

    @Autowired
    private MongoUserOperations template;

    @Autowired
    private MongoPageTemplate pageTemplate;

    @Override
    public User getByUsername(String username) {
        return template.findOne(query(where("username").is(username)));
    }

    @Override
    public User getByUserEmail(String userEmail) {
        return template.findOne(query(where("email").is(userEmail)));
    }

    @Override
    public User getByOpenId(String openId) {
        return template.findOne(query(where("openId").is(openId)));
    }

    @Override
    public List<User> getLimitedList(int offset, int pageSize) {
        Query query = new Query().skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query());
    }

    @Override
    public List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize) {
        return template.find(addSort(getSearchQuery(searchTerm).skip(offset).limit(pageSize)));
    }

    @Override
    public int getCountByUsernameOrEmail(String searchTerm) {
        return (int)template.count(getSearchQuery(searchTerm));
    }

    @Override
    public List<User> getAllByAddedWidget(long widgetId) {
        Query q = query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widgetId))));
        List<Page> pages = pageTemplate.find(q);
        List<User> users = Lists.newArrayList();
        for(Page p : pages) {
            if(!users.contains(p.getOwner())){
                users.add(p.getOwner());
            }
        }
        return users;
    }

    @Override
    public User getByForgotPasswordHash(String hash) {
        return template.findOne(query(where("forgotPasswordHash").is(hash)));
    }

    @Override
    public Class<? extends User> getType() {
        return MongoDbUser.class;
    }

    @Override
    public User get(long id) {
        return template.get(id);
    }

    @Override
    public User save(User item) {
        return template.save(item);
    }

    @Override
    public void delete(User item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    private Query getSearchQuery(String searchTerm) {
        return query(where("username").is(searchTerm).orOperator(where("email").is(searchTerm)));
    }

    private Query addSort(Query query) {
        query.sort().on("username", Order.ASCENDING);
        return query;
    }
}
